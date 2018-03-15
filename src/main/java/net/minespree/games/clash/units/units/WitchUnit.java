package net.minespree.games.clash.units.units;

import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.types.WitchClashEntity;
import net.minespree.games.clash.units.ClashSingleUnit;
import net.minespree.games.clash.units.ClashUnitType;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class WitchUnit extends ClashSingleUnit {

    private static final ParticleEffect EFFECT = new ParticleEffect();

    static {
        EFFECT.setCount(30);
        EFFECT.setYOffset(2.0F);
        EFFECT.setXOffset(2.0F);
        EFFECT.setZOffset(2.0F);
        EFFECT.setParticle(EnumWrappers.Particle.CRIT_MAGIC);
    }

    private int spawned;

    @SuppressWarnings("unchecked")
    public WitchUnit(Player owner, Team team, Side side) {
        super(ClashUnitType.WITCH, owner, team, side);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(entity.isDead() || spawned == 12) {
                    cancel();
                    return;
                }

                EFFECT.sendParticle(entity.getLocation(), (Collection<Player>) Bukkit.getOnlinePlayers());
                ClashUnitType.GOBLINS.spawn(owner, team, entity.getLocation(), side);
                spawned += 3;
            }
        }.runTaskTimer(Clash.getPlugin(), 120L, 120L);
    }

    @Override
    protected void spawn(Location location) {
        entity = new WitchClashEntity(owner, team, location.clone().add(0, 1.5, 0), side);
    }
}
