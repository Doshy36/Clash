package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashGolem;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GolemClashEntity extends ClashEntity {

    public GolemClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.GOLEM, ClashMovementType.WALKING, AttackType.GROUND, location, side, 2.5,3000.0F, 600.0F, 0.7F, 7.5F, 60);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashGolem(this);
    }

    @Override
    public void attack(Location target) {}
}
