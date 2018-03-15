package net.minespree.games.clash;

import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.SpawnHandler;
import net.minespree.rise.teams.Team;
import net.minespree.rise.teams.TeamHandler;
import net.minespree.wizard.util.SetupUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ClashSpawnHandler implements SpawnHandler {
    @Override
    public void spawnPlayer(@Nonnull Player player, @Nonnull SpawnReason reason) {
        ClashMapData data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
        TeamHandler teamHandler = RisePlugin.getPlugin().getGameManager().getTeamHandler().get();
        ClashMapData mapData = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
        Team team = teamHandler.getTeam(player);

        SetupUtil.setupPlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);

        if(team != null && data.getPlayers().containsKey(player.getUniqueId())) {
            Pair<Float, Float> yawPitch = mapData.getYawPitch().get(team);
            player.teleport(mapData.getSpawns().get(team).randomLocation(yawPitch.getLeft(), yawPitch.getRight()));
            data.getPlayers().get(player.getUniqueId()).setHotbar();
        }
    }
}
