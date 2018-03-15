package net.minespree.games.clash.units.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.types.BlazeClashEntity;
import net.minespree.games.clash.units.ClashSingleUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BlazeUnit extends ClashSingleUnit {

    public BlazeUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.BLAZE, owner, team, side);
    }

    @Override
    protected void spawn(Location location) {
        entity = new BlazeClashEntity(owner, team, location.clone().add(0, 4, 0), side);
    }
}
