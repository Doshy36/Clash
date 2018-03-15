package net.minespree.games.clash.units.spells;

import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minespree.games.clash.Damageable;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.towers.ClashTower;
import net.minespree.games.clash.units.ClashSpellUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import org.bukkit.entity.Player;

public class PoisonSpellUnit extends ClashSpellUnit {

    public PoisonSpellUnit(Player owner, Team team, Side side, float radius) {
        super(ClashUnitType.POISON_SPELL, owner, team, side, radius, 200L, 10L);

        effect.setParticle(EnumWrappers.Particle.SPELL);
        effect.setCount(5);
        effect.setXOffset(0.4F);
        effect.setYOffset(0.9F);
        effect.setZOffset(0.4F);
    }

    public PoisonSpellUnit(Player owner, Team team, Side side) {
        this(owner, team, side, 5.0F);
    }

    @Override
    public void effect(Damageable damageable) {
        if(damageable.getTeam() != team && !(damageable instanceof ClashTower)) {
            damageable.damage(owner, 25.0F);
        }
    }
}
