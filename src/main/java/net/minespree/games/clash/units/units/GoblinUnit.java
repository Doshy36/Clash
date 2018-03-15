package net.minespree.games.clash.units.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.types.GoblinClashEntity;
import net.minespree.games.clash.units.ClashHordeUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GoblinUnit extends ClashHordeUnit {

    public GoblinUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.GOBLINS, owner, team, side, 3);
    }

    @Override
    public ClashEntity spawnOne(Location location) {
        return new GoblinClashEntity(owner, team, location.clone().add(0, 1.5, 0), side);
    }
}
