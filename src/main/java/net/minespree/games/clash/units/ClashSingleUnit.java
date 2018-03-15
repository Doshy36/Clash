package net.minespree.games.clash.units;

import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.rise.teams.Team;
import org.bukkit.entity.Player;

public abstract class ClashSingleUnit extends ClashUnit {

    protected ClashEntity entity;

    public ClashSingleUnit(ClashUnitType type, Player owner, Team team, Side side) {
        super(type, owner, team, side);
    }
}
