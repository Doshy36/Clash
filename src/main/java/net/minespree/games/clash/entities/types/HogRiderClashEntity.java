package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashHogRider;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HogRiderClashEntity extends ClashEntity {

    public HogRiderClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.HOG_RIDER, ClashMovementType.WALKING, AttackType.GROUND, location, side, 2.15, 900.0F, 150.0F, 1.5F, 10.0F, 30);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashHogRider(this);
    }

    @Override
    public void attack(Location target) {
        ((ClashHogRider) nmsEntity).getRider().bw();
    }
}
