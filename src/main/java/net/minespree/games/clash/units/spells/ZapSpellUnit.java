package net.minespree.games.clash.units.spells;

import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.Damageable;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.towers.ClashTower;
import net.minespree.games.clash.units.ClashSpellUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ZapSpellUnit extends ClashSpellUnit {

    private final static ParticleEffect EFFECT = new ParticleEffect();

    static {
        EFFECT.setParticle(EnumWrappers.Particle.FIREWORKS_SPARK);
        EFFECT.setCount(30);
        EFFECT.setXOffset(2.0F);
        EFFECT.setZOffset(2.0F);
        EFFECT.setYOffset(0.3F);
    }

    public ZapSpellUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.ZAP_SPELL, owner, team, side, 4.0F, 0L, 0L);
    }

    @Override
    public void effect(Damageable damageable) {}

    @Override @SuppressWarnings("unchecked")
    public void hit(Location location) {
        location.getWorld().strikeLightning(location);

        Location hit = location.clone();

        EFFECT.sendParticle(location, (Collection<Player>) Bukkit.getOnlinePlayers());

        ClashMapData data = ((ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData());
        for (Damageable damageable : data.getDamageable()) {
            if(damageable.getTeam() != team && !(damageable instanceof ClashTower)) {
                Location loc = damageable.getLocation().clone();
                loc.setY(hit.getY());
                double distance = loc.distance(hit);
                if (distance <= radius) {
                    damageable.damage(owner, (float) Math.min(200 / distance, 300.0F));
                }
            }
        }
    }
}
