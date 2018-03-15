package net.minespree.games.clash.states;

import net.minespree.babel.Babel;
import net.minespree.babel.BabelMessage;
import net.minespree.rise.states.GameState;
import net.minespree.wizard.util.MessageUtil;
import org.bukkit.Bukkit;

public class OvertimeState extends ClashGameState {

    private final static BabelMessage OVERTIME = Babel.translate("cl_overtime");
    private final static BabelMessage NEXT_STAR_WINS = Babel.translate("cl_next_star_wins");

    private static final int END_TIME = 0;

    @Override
    public void onStart(GameState previous) {
        super.onStart(previous);

        Bukkit.getOnlinePlayers().forEach(player -> MessageUtil.sendTitleSubtitle(player, OVERTIME, NEXT_STAR_WINS, 20, 40, 20));
        data.setScoreboardTitle(OVERTIME);
        data.getScoreboard().updateScore("data", OVERTIME);
        data.setTimeEnded(END_TIME);
    }

    @Override
    public void time(int time) {
        data.getScoreboard().updateTime();
        if(time == END_TIME) {
            end(null);
        }
    }
}
