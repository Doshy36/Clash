package net.minespree.games.clash;

import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Damageable {

    void damage(Player damager, float damage);

    void kill(Player killer);

    boolean isDead();

    Location getLocation();

    Team getTeam();

    Side getSide();

}
