package net.minespree.games.clash.units.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.types.BarbarianClashEntity;
import net.minespree.games.clash.units.ClashHordeUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BarbarianUnit extends ClashHordeUnit {

    public BarbarianUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.BARBARIAN_HORDE, owner, team, side, 4);
    }

    @Override
    protected ClashEntity spawnOne(Location location) {
        return new BarbarianClashEntity(owner, team, location.clone().add(0, 1.5, 0), side);
    }
}
