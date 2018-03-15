package net.minespree.games.clash.states;

import net.minespree.babel.Babel;
import net.minespree.babel.BabelMessage;
import net.minespree.games.clash.player.ClashPlayer;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.states.GameState;
import net.minespree.wizard.util.MessageUtil;
import org.bukkit.Bukkit;

public class DefaultState extends ClashGameState {

    private final static BabelMessage FIGHT = Babel.translate("cl_fight");

    private final static int END_TIME = 300;

    @Override
    public void onStart(GameState previous) {
        super.onStart(previous);

        ClashPlayer.setMultiplier(1.0F);

        Bukkit.getOnlinePlayers().forEach(player -> MessageUtil.sendTitleSubtitle(player, FIGHT, DESTROY_ENEMY_CASTLE, 20, 40, 20));
        data.setScoreboardTitle(FIGHT);
        data.getScoreboard().updateScore("data", FIGHT);
        data.setTimeEnded(END_TIME);
    }

    @Override
    public void time(int time) {
        data.getScoreboard().updateTime();
        if(time == END_TIME) {
            RisePlugin.getPlugin().getGameStateManager().changeState(new DoubleElixirState());
        }
    }
}
