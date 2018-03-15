package net.minespree.games.clash.units;

import lombok.Getter;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.Side;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class ClashUnit {

    protected ClashMapData data;
    protected ClashUnitType type;
    protected Player owner;
    @Getter
    protected Team team;
    @Getter
    protected Side side;

    public ClashUnit(ClashUnitType type, Player owner, Team team, Side side) {
        this.data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
        this.type = type;
        this.owner = owner;
        this.team = team;
        this.side = side;
    }

    protected abstract void spawn(Location location);

}
