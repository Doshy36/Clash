package net.minespree.games.clash.towers;

import net.citizensnpcs.api.npc.NPC;
import net.minespree.babel.Babel;
import net.minespree.cartographer.util.GameArea;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.states.ClashGameState;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.teams.Team;
import org.bukkit.entity.Player;

public class CastleClashTower extends ClashTower {

    public CastleClashTower(ClashMapData data, Team team, GameArea tower, GameArea area) {
        super(data, Babel.translate("cl_clashtower"), team, tower, area, 4000.0F, 150.0F, 20);
    }

    @Override
    public void initialize() {
        super.initialize();

        side = Side.BOTH;
    }

    @Override
    public void setNpcSkin(NPC npc) {
        if(team.getWoolColour() == 11) {
            try {
                Clash.changeSkin(npc, "King", "eyJ0aW1lc3RhbXAiOjE1MTU2NjM4MjI1MjksInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWJkOWQyMmJhMmM0N2Q4MjhiOGVlZTU2M2Y0YjdmOWZmYzkyNWFhN2NhYjFhNDljY2VhNTFlZmU0NzU0M2U2MCJ9fX0=",
                        "BunW171c1bGHBmOWg2KMOZG1bwqRHNC1sRJ/B8q6+W8ta8oBSXH9rZv2XlzFS+QtodAPGaRTvaS30v0E+E6IHNSoJFkDEskUFWlaPPTB3ECmb0waRw0LIiCpgci7A7ces1sO6TP9stFoFRkRidH1yyalFxeMMdiTIJFE5w4UL0iV5IZ34AHk63dXU8VhV5ez1QKfFrZYcY0qyMCIrGUWPjJ6hjSimF/qIvA8Qe9DyX+eH6NTifJWNDEOO+yxuPvgqvMxyMaNa10Qj4eeSkh35vb5ntg0VlPeTlaEQEi+f0V/ZSV3cbOdY1osh582johrBaPir0aGymzQ/U1oGGOiMwQkfj/D+nFLRfQ8tr0oB8YM+7ti+Oe01GBnzOzeUhQbNpSifmjbeHqxhzhANZ9So1OpR0ixgk8kiURZ39bNZkOQfhFb4OHTmFnQLfVG9py3nJjPa1j+HSLnMvXmQ4uLN/XQ6NYTVqV42yK+/pEy5xz7kSKFU9vQAQVLmIm+uekNObKN2YIEKIAXveoRCh3X1O41w9TzQD6MlmiRMH4iXljFknKH3tz1KXT6hMfVNu3PhOH8byT5l25eJXfXIUaXlhc18yGQVCLwB6qCCbGMR8TEev3boHeJ3zYrUWJps9jrWMe5MXurxISw0VuXhh4kvQg2Y7p4JxpvwXITq0Z+4Sw=");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Clash.changeSkin(npc, "King", "eyJ0aW1lc3RhbXAiOjE1MTUyNTM1ODY2MjEsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM2NzNlNTIxMzQyODJlZmQ1ZDUyNGQzNDYyYTNkNjQ5MWVkZGMxNzdhNTY0OWVlZTNmYmJjMjJhMzEzNDYzIn19fQ==",
                        "U6p+09dHfLNiNWVnGYLfLe1USFesrR/sFdX53pNOMY/XFf8mKBZUqmp6uKmohebNsAQRrUHw+OOIdcNwNMnKiZCEJPcq5A/EClgXM4VeA6+JkKjf6AxJDYWkj72f/O90VNTpPsqInlM+WD4iYwCJT7nJkLnuCRQ+Pg08Un/LT/4NjGyACROW+eA7z08a/6PWAD5s9o4xfURQU0tp8ldgtJuDrAvUF9J2QfRl4fSPW6UdvD9kWmlR6h11NuCcvW8GuRiwSun/B4yf+9cwoPzlZuFpb0fAu6yEIgaBbKKz+qmaafAYuEM10IU9D25ZKaPNn0IwjaQw8X3E1CDKvihYadPKeMvzWpo4lJjKDlm7uCtLy+sr83l5I0I8GffkrafqEQpQG+fre3VYDPs4Pio+iaVs5P4JDaNblV6ykfVIOngflk5y0myjL0YSY0uzpMGqc0jJDRFjI96Yu/v7am6HFKeWGjEY+B2wrMsMNsVR3rsQAvMsvvARvvifID7Qe2qW1KvmBKcl76n9zkNAs4BZ0eA/X+ctAYB11NGHZQkWASVWiCQyfhyALl19EhuMk5kfw8YrrBJIQnZpKvL4HFaVK563Ksg/EP/EX0mmY9QLRQyYWPOPRgnwNsX6SM1Fj3vgBEqk2t2Lu4SjKmWhSAyOJgvdfvu03KbWUQ6ybwhhPZ8=");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void kill(Player killer) {
        super.kill(killer);

        for (int i = 0; i < 3; i++) {
            location.getWorld().createExplosion(location.add(0, 1, 0), 7.5F);
        }

        if(RisePlugin.getPlugin().getGameStateManager().getCurrentState() instanceof ClashGameState) {
            for (Team t : RisePlugin.getPlugin().getGameManager().getTeamHandler().get().getTeams()) {
                if (t != team) {
                    ((ClashGameState) RisePlugin.getPlugin().getGameStateManager().getCurrentState()).end(t);
                    break;
                }
            }
        }
    }
}
