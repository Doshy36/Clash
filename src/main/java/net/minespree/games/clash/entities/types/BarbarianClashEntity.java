package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashBarbarian;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BarbarianClashEntity extends ClashEntity {

    public BarbarianClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.BARBARIAN, ClashMovementType.WALKING, AttackType.GROUND, location, side, 2.5, 600.0F, 100.0F, 1.4F, 7.0F, 30);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashBarbarian(this);
    }

    @Override
    public void attack(Location target) {}

}
