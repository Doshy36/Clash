package net.minespree.games.clash.player;

import lombok.Getter;
import lombok.Setter;
import net.minespree.babel.Babel;
import net.minespree.babel.BabelStringMessageType;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.FractionalDisplay;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.player.decks.ClashPlayerDeck;
import net.minespree.games.clash.units.ClashUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.Game;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.util.Chat;
import net.minespree.wizard.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClashPlayer {

    private static final BabelStringMessageType CANT_AFFORD = Babel.translate("cl_cant_afford");

    private final static int OFFSET = 2;
    private final static float ELIXIR_GROWTH = 0.0178F;

    private ClashMapData data;

    @Setter
    private static float multiplier = 1.0F;
    @Setter
    private float individualMultiplier;

    private FractionalDisplay display;

    private Game game;

    @Getter
    private ClashDeck deck;
    @Getter
    private Player player;
    private Team team;

    private final float maxElixir;
    private float elixir;

    public ClashPlayer(Player player, Team team, float maxElixir, float startElixir) {
        this.player = player;
        this.team = team;
        this.maxElixir = maxElixir;
        this.elixir = startElixir;
        this.game = RisePlugin.getPlugin().getGameManager().getGameInProgress().get();

        deck = new ClashPlayerDeck();

        display = new FractionalDisplay(maxElixir, 10, Chat.BIG_BLOCK, Chat.PINK + Chat.BOLD, Chat.GRAY + Chat.BOLD);

        data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();

        individualMultiplier = 1.0F;

        deck.initialize(this);
    }

    private void spend(float elixir) {
        this.elixir = Math.max(this.elixir - elixir, 0.0F);
        game.changeStatistic(player, "elixirused", elixir);
        update();
    }

    public boolean hasEnough(float elixir) {
        return this.elixir >= elixir;
    }

    public void add(float elixir) {
        this.elixir = Math.min(this.elixir + elixir, maxElixir);
    }

    public void increment() {
        elixir = Math.min(elixir + (ELIXIR_GROWTH * multiplier * individualMultiplier), maxElixir);
    }

    public void setHotbar() {
        if(player == null || !player.isOnline())
            return;
        for (int i = 0; i < deck.getCurrent().size(); i++) {
            ClashUnitType type = deck.getCurrent().get(i);

            player.getInventory().setItem(i + OFFSET, type.getBuilder().build(player));
        }
    }

    public boolean has(int slot) {
        return slot - OFFSET >= 0 && slot - OFFSET < deck.getCurrent().size();
    }

    public ClashUnitType get(int slot) {
        return has(slot) ? deck.getCurrent().get(slot - OFFSET) : null;
    }

    public ClashUnit apply(Location location, int slot, Side side) {
        if(data.getTime() > 600) {
            return null;
        }
        ClashUnitType type = deck.getCurrent().get(slot - OFFSET);
        if(elixir >= type.getElixirCost()) {
            ClashUnit unit = type.spawn(player, team, location, side);

            spend(type.getElixirCost());
            deck.next(type);

            setHotbar();
            return unit;
        } else {
            CANT_AFFORD.sendMessage(player);
        }
        return null;
    }

    public void update() {
        if(player == null || !player.isOnline())
            return;
        MessageUtil.sendActionBar(player, Chat.AQUA + Chat.BOLD +  "Elixir " + display.build(elixir) + " " +
                Chat.PINK + Chat.BOLD + String.format("%.1f", elixir) + " (" + (individualMultiplier * multiplier) + "x)");
    }

}
