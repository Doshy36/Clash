package net.minespree.games.clash.units.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.types.HogRiderClashEntity;
import net.minespree.games.clash.units.ClashSingleUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HogRiderUnit extends ClashSingleUnit {

    public HogRiderUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.HOG_RIDER, owner, team, side);
    }

    @Override
    protected void spawn(Location location) {
        entity = new HogRiderClashEntity(owner, team, location.clone().add(0, 1.5, 0), side);
    }
}
