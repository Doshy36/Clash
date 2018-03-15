package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashBlaze;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BlazeClashEntity extends ClashEntity {

    public BlazeClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.BLAZE, ClashMovementType.FLYING, AttackType.ALL, location, side, 2.0,1000.0F, 60.0F, 1.5F, 10.0F, 15);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashBlaze(this);
    }

    @Override
    public void attack(Location target) {
        ClashBlaze blaze = (ClashBlaze) nmsEntity;
        double x = target.getX() - blaze.locX;
        double y = target.getY() - (blaze.locY + (double) (blaze.length / 2.0F));
        double z = target.getZ() - blaze.locZ;

        EntitySmallFireball ball = new EntitySmallFireball(blaze.world, blaze, x + blaze.bc().nextGaussian(), y, z + blaze.bc().nextGaussian());
        ball.getBukkitEntity().setMetadata("damage", new FixedMetadataValue(Clash.getPlugin(), 0.0F));
        ball.getBukkitEntity().setMetadata("team", new FixedMetadataValue(Clash.getPlugin(), getTeam()));
        ball.locY = blaze.locY + blaze.length + 1;
        blaze.world.addEntity(ball);
    }
}
