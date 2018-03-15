package net.minespree.games.clash.units.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.types.BalloonClashEntity;
import net.minespree.games.clash.units.ClashSingleUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BalloonUnit extends ClashSingleUnit {

    public BalloonUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.BALLOON, owner, team, side);
    }

    @Override
    protected void spawn(Location location) {
        entity = new BalloonClashEntity(owner, team, location.clone().add(0, 5, 0), side);
    }
}
