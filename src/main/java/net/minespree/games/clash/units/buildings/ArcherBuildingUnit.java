package net.minespree.games.clash.units.buildings;

import net.minespree.games.clash.Clash;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.units.ClashBuildingUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ArcherBuildingUnit extends ClashBuildingUnit {

    private static final BlockFace[] FACES = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    private static final float RANGE = 10.0F;
    private static final float DAMAGE = 100.0F;

    public ArcherBuildingUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.ARCHER_BUILDING, owner, team, side, "archer", 1200.0F, 6L);
    }

    @Override
    public void spawn(Location location) {
        super.spawn(location);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!built) {
                    return;
                }
                if(dead || data.isEnded()) {
                    cancel();
                    return;
                }

                for (BlockFace face : FACES) {
                    Location block = location.getBlock().getRelative(face).getRelative(face).getRelative(face).getRelative(face).getLocation().add(0.5, 0.5, 0.5);
                    for (ClashEntity entity : data.getEntities().values()) {
                        if(entity.getTeam() != team && entity.getLocation().distance(block) <= RANGE) {
                            Arrow arrow = entity.getWorld().spawnArrow(block, new Vector(0, 0, 0), 1.2F, 0.0F);
                            if (entity.getNmsEntity().hasLineOfSight(((CraftEntity) arrow).getHandle())) {
                                arrow.setVelocity(entity.getLocation().toVector().subtract(block.toVector()));
                                arrow.setBounce(false);
                                arrow.setMetadata("damage", new FixedMetadataValue(Clash.getPlugin(), DAMAGE));
                                break;
                            }
                            arrow.remove();
                        }
                    }
                }
            }
        }.runTaskTimer(Clash.getPlugin(), 30L, 30L);
    }

}
