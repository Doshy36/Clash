package net.minespree.games.clash.units.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.types.GolemClashEntity;
import net.minespree.games.clash.units.ClashSingleUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GolemUnit extends ClashSingleUnit {

    public GolemUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.GOLEM, owner, team, side);
    }

    @Override
    protected void spawn(Location location) {
        entity = new GolemClashEntity(owner, team, location.clone().add(0, 1.5, 0), side);
    }
}
