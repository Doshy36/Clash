package net.minespree.games.clash.units;

import com.google.common.collect.Lists;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.Damageable;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.states.ClashGameState;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

public abstract class ClashSpellUnit extends ClashUnit {

    private Location centre;
    private List<Location> locations = Lists.newArrayList();

    protected ParticleEffect effect;
    protected float radius;
    private long length, period;

    public ClashSpellUnit(ClashUnitType type, Player owner, Team team, Side side, float radius, long length, long period) {
        super(type, owner, team, side);

        this.effect = new ParticleEffect();
        this.radius = radius;
        this.length = length;
        this.period = period;
    }

    public abstract void effect(Damageable damageable);

    @SuppressWarnings("unchecked")
    public void hit(Location location) {
        this.centre = location;

        if(radius > 0.0) {
            for (float x = -radius; x < radius; x++) {
                for (float z = -radius; z < radius; z++) {
                    Location loc = location.clone().add(new Vector(x, 0, z));
                    if (loc.distance(centre) < radius) {
                        locations.add(loc);
                    }
                }
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (length <= 0.0 || data.isEnded()) {
                        cancel();
                        return;
                    }
                    length -= period;
                    for (Location loc : locations) {
                        effect.sendParticle(loc, (Collection<Player>) Bukkit.getOnlinePlayers());
                    }
                    if (RisePlugin.getPlugin().getGameStateManager().getCurrentState() instanceof ClashGameState) {
                        ClashMapData data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
                        for (Damageable damageable : data.getDamageable()) {
                            Location loc = damageable.getLocation().clone();
                            loc.setY(centre.getY());
                            if (isInside(loc)) {
                                effect(damageable);
                            }
                        }
                    }
                }
            }.runTaskTimer(Clash.getPlugin(), 0L, period);
        }
    }

    public boolean isInside(Location location) {
        return centre.distanceSquared(location) <= Math.pow(radius, 2);
    }

    @Override
    protected void spawn(Location location) {}

}
