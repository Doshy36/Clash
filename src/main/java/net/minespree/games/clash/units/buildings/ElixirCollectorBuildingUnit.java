package net.minespree.games.clash.units.buildings;

import net.minespree.games.clash.Clash;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.units.ClashBuildingUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ElixirCollectorBuildingUnit extends ClashBuildingUnit {

    public ElixirCollectorBuildingUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.ELIXIR_COLLECTOR_BUILDING, owner, team, side, "elixircollector", 800.0F, 3L);
    }

    @Override
    public void spawn(Location location) {
        super.spawn(location);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!built) {
                    return;
                }
                if(dead || data.isEnded()) {
                    cancel();
                    return;
                }
                data.getPlayers().get(owner.getUniqueId()).add(1.0F);
            }
        }.runTaskTimer(Clash.getPlugin(), 150L, 150L);
    }
}
