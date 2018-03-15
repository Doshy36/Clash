package net.minespree.games.clash;

import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashTargetType;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;

public interface ClashTarget extends Damageable {

    Location getTarget(ClashEntity entity);

    ClashTargetType getTargetType();

    Team getTeam();

    boolean near(ClashEntity entity, Location target);

}
