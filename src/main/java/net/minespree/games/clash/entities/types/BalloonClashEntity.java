package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashBalloon;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BalloonClashEntity extends ClashEntity {

    public BalloonClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.BALLOON, ClashMovementType.FLYING, AttackType.AIR, location, side, 5.0, 1300.0F, 500.0F, 0.5F, 15.0F, 60);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashBalloon(this);
    }

    @Override
    public void attack(Location target) {
        ClashBalloon balloon = (ClashBalloon) nmsEntity;
        double x = target.getX() - balloon.locX;
        double y = target.getY() - (balloon.locY + (double) (balloon.length / 2.0F));
        double z = target.getZ() - balloon.locZ;

        EntityLargeFireball ball = new EntityLargeFireball(balloon.world, balloon, x + balloon.bc().nextGaussian(), y, z + balloon.bc().nextGaussian());
        ball.getBukkitEntity().setMetadata("damage", new FixedMetadataValue(Clash.getPlugin(), 0.0F));
        ball.getBukkitEntity().setMetadata("team", new FixedMetadataValue(Clash.getPlugin(), getTeam()));
        ball.locY = balloon.locY + balloon.length + 1;
        balloon.world.addEntity(ball);
    }
}
