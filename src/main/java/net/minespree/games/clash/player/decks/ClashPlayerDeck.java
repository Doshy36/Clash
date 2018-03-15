package net.minespree.games.clash.player.decks;

import com.google.common.collect.Lists;
import net.minespree.babel.Babel;
import net.minespree.babel.BabelMessage;
import net.minespree.feather.data.gamedata.GameRegistry;
import net.minespree.feather.data.gamedata.perks.Perk;
import net.minespree.feather.data.gamedata.perks.PerkHandler;
import net.minespree.feather.data.gamedata.perks.PerkSet;
import net.minespree.feather.player.NetworkPlayer;
import net.minespree.games.clash.player.ClashDeck;
import net.minespree.games.clash.player.ClashPlayer;
import net.minespree.games.clash.units.ClashUnitType;

import java.util.Arrays;
import java.util.Collections;

public class ClashPlayerDeck extends ClashDeck {

    private static final BabelMessage NOT_SELECTED = Babel.translate("cl_not_selected");

    @Override
    public void initialize(ClashPlayer player) {
        NetworkPlayer networkPlayer = NetworkPlayer.of(player.getPlayer());
        PerkSet set = networkPlayer.getPerks().getPerkSet(GameRegistry.Type.CLASH).getPerkSet(networkPlayer.getPerks().getDefaultSet(GameRegistry.Type.CLASH));
        if(set != null && set.getSelectedPerks() != null && set.getSelectedPerks().size() == PerkHandler.getInstance().getMaxSelected(GameRegistry.Type.CLASH)) {
            units = Lists.newArrayList();
            for (Perk perk : set.getSelectedPerks()) {
                units.add(ClashUnitType.fromPerkId(perk.getId()));
            }
        }
        boolean unitSize = units.size() != PerkHandler.getInstance().getMaxSelected(GameRegistry.Type.CLASH);
        if(units == null || unitSize) {
            if(unitSize) {
                NOT_SELECTED.sendMessage(player.getPlayer());
            }
            units = Arrays.asList(ClashUnitType.ARCHER_BUILDING, ClashUnitType.GOBLINS, ClashUnitType.WITCH, ClashUnitType.BLAZE,
                    ClashUnitType.BALLOON, ClashUnitType.GOLEM, ClashUnitType.POISON_SPELL, ClashUnitType.ZAP_SPELL);
        }

        Collections.shuffle(units);
        for (int i = 0; i < 4; i++) {
            if(i < units.size()) {
                current.add(units.get(i));
            }
        }
    }
}
