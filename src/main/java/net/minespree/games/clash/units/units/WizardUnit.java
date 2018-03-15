package net.minespree.games.clash.units.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.types.WizardClashEntity;
import net.minespree.games.clash.units.ClashSingleUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WizardUnit extends ClashSingleUnit {

    public WizardUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.WIZARD, owner, team, side);
    }

    @Override
    protected void spawn(Location location) {
        entity = new WizardClashEntity(owner, team, location.clone().add(0, 1.5, 0), side);
    }
}
