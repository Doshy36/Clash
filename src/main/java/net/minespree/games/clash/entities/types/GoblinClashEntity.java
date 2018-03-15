package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashGoblin;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GoblinClashEntity extends ClashEntity {

    public GoblinClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.GOBLIN, ClashMovementType.WALKING, AttackType.GROUND, location, side, 1.0, 150.0F, 20.0F, 1.2F, 7.5F, 20);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashGoblin(this);
    }

    @Override
    public void attack(Location target) {}
}
