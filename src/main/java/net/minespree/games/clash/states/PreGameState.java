package net.minespree.games.clash.states;

import net.minespree.babel.Babel;
import net.minespree.babel.BabelMessage;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.player.ClashPlayer;
import net.minespree.games.clash.towers.ClashTower;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.SpawnHandler;
import net.minespree.rise.states.GameState;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.util.Chat;
import net.minespree.wizard.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PreGameState extends ClashGameState {

    private final static BabelMessage PREGAME = Babel.translate("cl_pregame");

    private final static int END_TIME = 600;

    @Override
    public void onStart(GameState previous) {
        super.onStart(previous);

        seeTarget = false;

        teamHandler.enableTeamChat();

        data.getTowers().forEach(ClashTower::initialize);

        int singleTeam = 0;
        for (Team team : teamHandler.getTeams()) {
            data.getStars().put(team, 0);
            for (UUID uuid : team.getPlayers()) {
                Player player = Bukkit.getPlayer(uuid);
                player.spigot().setCollidesWithEntities(false);

                ClashPlayer clashPlayer = new ClashPlayer(player, team, 10.0F, 3.0F);
                data.getPlayers().put(uuid, clashPlayer);
                if(team.size() == 1) {
                    clashPlayer.setIndividualMultiplier(1.5F);
                    singleTeam++;
                }
                data.getSpawnHandler().spawnPlayer(player, SpawnHandler.SpawnReason.GAME_START);
            }
        }
        if(singleTeam == 2) {
            for (ClashPlayer player : data.getPlayers().values()) {
                player.setIndividualMultiplier(2.0F);
            }
        }
        ClashPlayer.setMultiplier(0.0F);

        data.setScoreboardTitle(PREGAME);
        data.setTimeEnded(END_TIME);
        data.getScoreboard().updateTime();

        data.getScoreboard().onStart();

        Bukkit.getPluginManager().registerEvents(data.getListener(), Clash.getPlugin());
    }

    @Override
    public void time(int time) {
        data.getScoreboard().updateTime();
        if(time > END_TIME && time <= END_TIME + 5) {
            Bukkit.getOnlinePlayers().forEach(player -> MessageUtil.sendTitleSubtitle(player, Babel.translate("cl_startingin").toString(player), Chat.YELLOW + (time - END_TIME), 0, 60, 0));
        } else if(time == END_TIME) {
            Bukkit.getOnlinePlayers().forEach(player -> MessageUtil.sendTitle(player, Babel.translate("cl_fight").toString(player), 0, 30, 20));

            RisePlugin.getPlugin().getGameStateManager().changeState(new DefaultState());
        }
    }
}
