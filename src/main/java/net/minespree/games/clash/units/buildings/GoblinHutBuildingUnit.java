package net.minespree.games.clash.units.buildings;

import net.minespree.games.clash.Clash;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.types.GoblinClashEntity;
import net.minespree.games.clash.units.ClashBuildingUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GoblinHutBuildingUnit extends ClashBuildingUnit {

    public GoblinHutBuildingUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.GOBLIN_HUT_BUILDING, owner, team, side, "goblinhut",1200.0F, 8L);
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

                new GoblinClashEntity(owner, team, GoblinHutBuildingUnit.this.location.clone().subtract(-0.5, 2.0, -0.5), side);
            }
        }.runTaskTimer(Clash.getPlugin(), 100L, 100L);
    }
}
