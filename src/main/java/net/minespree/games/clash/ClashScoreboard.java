package net.minespree.games.clash;

import net.minespree.games.clash.towers.ClashTower;
import net.minespree.rise.features.ScoreboardFeature;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.util.Chat;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ClashScoreboard extends ScoreboardFeature {

    private ClashMapData data;

    public ClashScoreboard(Plugin plugin, ClashMapData data) {
        super(plugin, Chat.YELLOW + Chat.BOLD + "Clash");

        this.data = data;
    }

    @Override
    public void initialize(Player player) {
        int base = (data.getStars().size() * 2) + 4;
        setScore(player, "data", data.getScoreboardTitle(), base);
        setScore(player, "time", getTime(), base - 1);
        setScore(player, "blank1", " ", base - 2);
        int i = 2;
        for (Team team : data.getStars().keySet()) {
            for (UUID uuid : team.getPlayers()) {
                setTeamName(player, team, team.getColour().toString(), null);
                addToTeam(player, Bukkit.getPlayer(uuid), team);
            }
            setScore(player, team.getName().getKey(), team.getColour() + buildTowers(team), i + 1);
            setScore(player, team.getName().getKey() + "blank", team.getColour().toString(), i);
            i += 2;
        }
        setScore(player, "gameId", ChatColor.GRAY + Bukkit.getServerName(), 1);
        setScore(player, "ip", Chat.GOLD + "play.minespree.net", 0);
    }

    public void updateTime() {
        updateScore("time", getTime());
    }

    public void updateTowers(Team team) {
        updateScore(team.getName().getKey(), team.getColour() + buildTowers(team));
    }

    private String getTime() {
        return Chat.YELLOW + DateFormatUtils.format((data.getTime() - data.getTimeEnded()) * 1000, "mm:ss");
    }

    private String buildTowers(Team team) {
        ClashTower left = null, right = null, castle = null;
        for (ClashTower tower : data.getTowers()) {
            if(tower.getTeam() == team) {
                if (tower.getSide() == Side.LEFT) {
                    left = tower;
                } else if (tower.getSide() == Side.RIGHT) {
                    right = tower;
                } else if(tower.getSide() == Side.BOTH) {
                    castle = tower;
                }
            }
        }
        return getColour(left, "♜") + " " + getColour(castle, "✪") + " " + getColour(right, "♜");
    }

    private String getColour(ClashTower tower, String symbol) {
        if(tower == null || tower.isDead()) {
            return Chat.DARK_GRAY + symbol;
        } else if(tower.isUnderAttack() && System.currentTimeMillis() % 500 <= 250) {
            return Chat.WHITE + symbol;
        }
        return tower.getTeam().getColour() + symbol;
    }

}
