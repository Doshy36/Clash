package net.minespree.games.clash.player.decks;

import net.minespree.games.clash.player.ClashDeck;
import net.minespree.games.clash.player.ClashPlayer;
import net.minespree.games.clash.units.ClashUnitType;

import java.util.Arrays;
import java.util.Collections;

public class ClashRandomDeck extends ClashDeck {

    @Override
    public void initialize(ClashPlayer player) {
        units = Arrays.asList(ClashUnitType.values());
        Collections.shuffle(units);
        for (int i = 0; i < 4; i++) {
            if(i < units.size()) {
                current.add(units.get(i));
            }
        }
    }
}
