package net.minespree.games.clash.player;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minespree.games.clash.units.ClashUnitType;

import java.util.Collections;
import java.util.List;

public abstract class ClashDeck {

    protected List<ClashUnitType> units = Lists.newArrayList();
    @Getter
    protected List<ClashUnitType> current = Lists.newArrayList();

    public abstract void initialize(ClashPlayer player);

    public ClashUnitType next(ClashUnitType type) {
        Collections.shuffle(units);

        int i = -1;
        ClashUnitType next;
        do {
            i++;
        } while (current.contains(next = units.get(i)));

        current.set(current.indexOf(type), next);

        return type;
    }

}
