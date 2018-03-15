package net.minespree.games.clash.states;

import net.minespree.babel.Babel;
import net.minespree.babel.BabelMessage;
import net.minespree.games.clash.player.ClashPlayer;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.states.GameState;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.util.MessageUtil;
import org.bukkit.Bukkit;

public class DoubleElixirState extends ClashGameState {

    private final static BabelMessage DOUBLE_ELIXIR = Babel.translate("cl_doubleelixir");

    private final static int END_TIME = 120;

    @Override
    public void onStart(GameState previous) {
        super.onStart(previous);

        ClashPlayer.setMultiplier(2.0F);

        Bukkit.getOnlinePlayers().forEach(player -> MessageUtil.sendTitleSubtitle(player, DOUBLE_ELIXIR, DESTROY_ENEMY_CASTLE, 20, 40, 20));
        data.setScoreboardTitle(DOUBLE_ELIXIR);
        data.getScoreboard().updateScore("data", DOUBLE_ELIXIR);
        data.setTimeEnded(END_TIME);
    }

    @Override
    public void time(int time) {
        data.getScoreboard().updateTime();
        if(time == END_TIME) {
            boolean equal = false;
            Team top = null;
            for (Team team : data.getStars().keySet()) {
                int stars = data.getStars().get(team);
                if(top == null) {
                    top = team;
                } else if(data.getStars().get(top) == stars) {
                    equal = true;
                } else if(data.getStars().get(top) < stars) {
                    top = team;
                }
            }
            if(equal) {
                RisePlugin.getPlugin().getGameStateManager().changeState(new OvertimeState());
            } else {
                end(top);
            }
        }
    }
}
