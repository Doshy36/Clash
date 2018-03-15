package net.minespree.games.clash.units;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.world.DataException;
import lombok.Getter;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.ClashTarget;
import net.minespree.games.clash.FractionalDisplay;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.ClashTargetType;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.particle.ParticleEffect;
import net.minespree.wizard.util.Area;
import net.minespree.wizard.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ClashBuildingUnit extends ClashUnit implements ClashTarget {

    @Getter
    protected boolean built, dead;

    private Area building;
    @Getter
    private Area area;
    @Getter
    protected Location location;

    private List<BlockState> states = Lists.newArrayList();

    private CuboidClipboard selection;
    private float health, decayAmount;
    private long buildSpeed;

    private FractionalDisplay display;

    public ClashBuildingUnit(ClashUnitType type, Player owner, Team team, Side side, String schematic, float health, long buildSpeed) {
        super(type, owner, team, side);

        this.health = health;
        this.decayAmount = health * 0.0167F;

        display = new FractionalDisplay(health, 15, Chat.BIG_HORIZONTAL_LINE, team.getColour().toString() + Chat.BOLD, Chat.GRAY + Chat.BOLD);

        try {
            selection = CuboidClipboard.loadSchematic(new File(Clash.getPlugin().getDataFolder(), "schematics/" + schematic + ".schematic"));
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }

        this.buildSpeed = buildSpeed;
    }

    @Override
    protected void spawn(Location location) {
        Vector size = selection.getSize();

        building = new Area(location, type.getXWidth(), type.getZWidth());
        building.setYMax(building.getYMax() + size.getY() + 1);

        this.location = building.centre(location.getWorld());

        display.spawn(location.clone().add(0.5, size.getY() + 2.0, 0.5));

        Vector offset = new Vector((building.getXMax() - building.getXMin()) / 2, 0, (building.getZMax() - building.getZMin()) / 2);

        BaseBlock[][][] blocks = new BaseBlock[(int) size.getX()][(int) size.getY()][(int) size.getZ()];
        for (int y = 0; y < size.getY(); y++) {
            for (int x = 0; x < size.getX(); x++) {
                for (int z = 0; z < size.getZ(); z++) {
                    blocks[x][y][z] = selection.getBlock(new Vector(x, y, z));
                }
            }
        }

        area = new Area(location, type.getXWidth() + 1, type.getZWidth() + 1);
        area.setYMax(area.getYMax() + size.getY() + 1);

        data.getTargets().add(this);
        data.getDamageable().add(this);

        WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        EditSession session = we.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), Integer.MAX_VALUE);

        AtomicInteger layer = new AtomicInteger(0);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(dead) {
                    cancel();
                    return;
                }
                int y = layer.get();
                for (int x = 0; x < size.getX(); x++) {
                    for (int z = 0; z < size.getZ(); z++) {
                        BaseBlock block = blocks[x][y][z];
                        try {
                            Vector vector = new Vector(location.getX() + x, location.getY() + y + 1, location.getZ() + z).subtract(offset);
                            states.add(location.getWorld().getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).getState());
                            if(session.setBlock(vector, block)) {
                                Block b = location.getWorld().getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
                                if(b.getType() == Material.STAINED_CLAY) {
                                    b.setData((byte) team.getWoolColour());
                                }
                            }
                        } catch (MaxChangedBlocksException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(layer.incrementAndGet() >= size.getY()) {
                    built = true;
                    cancel();
                    new BukkitRunnable() {
                        public void run() {
                            if(dead) {
                                cancel();
                                return;
                            }
                            damage(null, decayAmount);
                        }
                    }.runTaskTimer(Clash.getPlugin(), 20L, 20L);
                }
            }
        }.runTaskTimer(Clash.getPlugin(), 0L, buildSpeed);
    }

    @Override
    public void damage(Player damager, float damage) {
        health -= damage;
        display.build(health);
        if(health <= 0.0) {
            kill(damager);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void kill(Player killer) {
        dead = true;
        display.destroy();
        if(states.size() > 0) {
            ParticleEffect effect = new ParticleEffect();
            effect.setParticle(EnumWrappers.Particle.EXPLOSION_NORMAL);
            effect.setCount(1);
            Location location = states.get(0).getLocation();
            location.getWorld().playSound(location, Sound.EXPLODE, 1F, 1F);
            for (BlockState state : states) {
                effect.sendParticle(state.getLocation(), (Collection<Player>) Bukkit.getOnlinePlayers());
                state.update(true, true);
            }
        }
        data.getDamageable().remove(this);
        data.getTargets().remove(this);
    }

    @Override
    public boolean near(ClashEntity entity, Location target) {
        if(entity.getMovementType() == ClashMovementType.FLYING) {
            return entity.getLocation().distanceSquared(location) <= Math.pow(entity.getRange(), 2);
        }
        return entity.getLocation().distanceSquared(target) <= 25.0F;
    }

    @Override
    public Location getTarget(ClashEntity entity) {
        Location location = area.randomLocation(entity.getWorld());
        if(entity.getMovementType() == ClashMovementType.FLYING) {
            location.setY(entity.getLocation().getY());
        }
        return location;
    }

    @Override
    public ClashTargetType getTargetType() {
        return ClashTargetType.BUILDING;
    }
}
