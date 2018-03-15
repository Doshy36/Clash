package net.minespree.games.clash.units;

import com.google.common.collect.Lists;
import net.minespree.cartographer.util.GameArea;
import net.minespree.cartographer.util.GameLocation;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class ClashHordeUnit extends ClashUnit {

    protected List<ClashEntity> entities = Lists.newArrayList();

    private int amount;

    public ClashHordeUnit(ClashUnitType type, Player owner, Team team, Side side, int amount) {
        super(type, owner, team, side);

        this.amount = amount;
    }

    public void spawn(Location location) {
        spawn(location, new GameArea(new GameLocation(location.clone().add(2, 0, 2)), new GameLocation(location.clone().subtract(2, 0, 2))));
    }

    protected abstract ClashEntity spawnOne(Location location);

    protected void spawn(Location location, GameArea area) {
        for (int i = 0; i < amount; i++) {
            int attempts = 0;
            Location loc;
            do {
                loc = area.randomLocation();
                attempts++;
            } while (loc.getBlock().getType() == Material.AIR && attempts < 20);
            RisePlugin.getPlugin().getGameManager().getGameInProgress().get().changeStatistic(owner, "unitsspawned", 1);
            entities.add(spawnOne(attempts >= 20 ? location : loc));
        }
    }

}
