package net.minespree.games.clash.states;

import net.minespree.babel.Babel;
import net.minespree.feather.data.gamedata.GameRegistry;
import net.minespree.feather.data.gamedata.perks.PerkHandler;
import net.minespree.games.clash.Clash;
import net.minespree.rise.states.WaitingState;
import net.minespree.wizard.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClashWaitingStateExtension implements WaitingState.Extension, Listener {

    private final ItemBuilder builder;

    public ClashWaitingStateExtension() {
        builder = new ItemBuilder(Material.CHEST).displayName(Babel.translate("cl_decks"));
    }

    @Override
    public void onWaitingStateStart() {
        Bukkit.getPluginManager().registerEvents(this, Clash.getPlugin());
    }

    @Override
    public void onWaitingStateStop() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onJoin(Player player) {
        player.getInventory().setItem(0, builder.build(player));
    }

    @Override
    public void onLeave(Player player) {

    }

    @Override
    public void onCountdownStart() {

    }

    @Override
    public void onCountdownAbort() {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() != null && event.getItem().getType() == Material.CHEST) {
            PerkHandler.getInstance().open(event.getPlayer(), GameRegistry.Type.CLASH);
        }
    }
}
