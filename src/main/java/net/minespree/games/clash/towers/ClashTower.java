package net.minespree.games.clash.towers;

import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.minespree.babel.BabelStringMessageType;
import net.minespree.cartographer.util.GameArea;
import net.minespree.games.clash.*;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.ClashTargetType;
import net.minespree.games.clash.states.OvertimeState;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.Game;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.particle.ParticleEffect;
import net.minespree.wizard.util.Chat;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.UUID;

@Getter
public abstract class ClashTower implements ClashTarget {

    private static final ParticleEffect EXPLOSION_EFFECT = new ParticleEffect();

    static {
        EXPLOSION_EFFECT.setCount(1);
        EXPLOSION_EFFECT.setParticle(EnumWrappers.Particle.EXPLOSION_LARGE);
    }

    protected ClashMapData data;
    protected NPC npc;

    private FractionalDisplay display;

    protected BabelStringMessageType name;
    protected Team team;
    protected GameArea tower, area;
    protected Location location, centreArea;

    protected float health, damage;
    protected long damageSpeed;
    private boolean dead;
    protected Side side;

    private long lastDamage;

    ClashTower(ClashMapData data, BabelStringMessageType name, Team team, GameArea tower, GameArea area, float health, float damage, long damageSpeed) {
        this.data = data;
        this.name = name;
        this.team = team;
        this.tower = tower;
        this.area = area;
        this.health = health;
        this.damage = damage;
        this.damageSpeed = damageSpeed;

        display = new FractionalDisplay(health, 20, Chat.BIG_BLOCK, Chat.GREEN, Chat.RED);
    }

    public void initialize() {
        display.spawn(tower.centreLocation().toLocation().add(0.5, 5, 0.5));

        location = tower.centreLocation().toLocation();
        centreArea = area.centre(location.getWorld());

        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "");

        Location location = tower.centreLocation().clone().toLocation().add(0.5, 3.0, 0.5);
        Pair<Float, Float> yawPitch = data.getYawPitch().get(team);
        location.setYaw(yawPitch.getLeft());
        location.setPitch(yawPitch.getRight());

        npc.spawn(location);
        npc.setProtected(true);

        setNpcSkin(npc);

        side = data.getRight().get(team).inside(location, false) ? Side.RIGHT : Side.LEFT;

        data.getDamageable().add(this);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(dead || data.isEnded()) {
                    cancel();
                    return;
                }

                attack();
            }
        }.runTaskTimer(Clash.getPlugin(), damageSpeed, damageSpeed);
    }

    public abstract void setNpcSkin(NPC npc);

    public void attack() {
        Location block = centreArea.getBlock().getLocation().add(0.5, 4.5, 0.5);
        for (ClashEntity entity : data.getEntities().values()) {
            if(entity.getTeam() != team && entity.getLocation().distance(block) <= 15.0) {
                Arrow arrow = entity.getWorld().spawnArrow(block, new Vector(0, 0, 0), 1.2F, 0.0F);
                if(entity.getNmsEntity().hasLineOfSight(((CraftEntity) arrow).getHandle())) {
                    arrow.setVelocity(entity.getLocation().toVector().subtract(block.toVector()));
                    arrow.setBounce(false);
                    arrow.setMetadata("damage", new FixedMetadataValue(Clash.getPlugin(), damage));
                    break;
                }
                arrow.remove();
            }
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void damage(Player damager, float damage) {
        lastDamage = System.currentTimeMillis();
        health -= damage;
        if(health < 0.0) {
            health = 0.0F;
        }
        display.build(health);
        EXPLOSION_EFFECT.sendParticle(tower.randomLocation(true), (Collection<Player>) Bukkit.getOnlinePlayers());
        if(health <= 0.0 && !dead) {
            kill(damager);
        } else {
            data.getScoreboard().updateTowers(team);
        }
    }

    @Override
    public void kill(Player killer) {
        dead = true;
        display.destroy();

        if(npc != null && npc.getEntity() != null && npc.getEntity() instanceof LivingEntity) {
            npc.despawn();
            npc.destroy();
            npc = null;
        }

        Team enemy = null;
        for (Team t : data.getStars().keySet()) {
            if(t != team) {
                enemy = t;
                break;
            }
        }

        Game game = RisePlugin.getPlugin().getGameManager().getGameInProgress().get();

        if(enemy != null) {
            for (UUID uuid : enemy.getPlayers()) {
                game.changeStatistic(Bukkit.getPlayer(uuid), "towersdestroyed", 1);
            }
        }

        data.getScoreboard().updateTowers(team);
        data.getStars().put(enemy, data.getStars().getOrDefault(enemy, 0) + 1);

        if(RisePlugin.getPlugin().getGameStateManager().getCurrentState() instanceof OvertimeState) {
            for (Team team : RisePlugin.getPlugin().getGameManager().getTeamHandler().get().getTeams()) {
                if(team != this.team) {
                    ((OvertimeState) RisePlugin.getPlugin().getGameStateManager().getCurrentState()).end(team);
                    break;
                }
            }
        }

        data.getDamageable().remove(this);
        data.getTowers().remove(this);
        data.getTargets().remove(this);
    }

    public boolean isUnderAttack() {
        return data.getEntities().values().stream().filter(entity -> entity.getTeam() != team).anyMatch(entity -> {
            Location loc = entity.getLocation();
            loc.setY(centreArea.getY());
            return loc.distance(centreArea) <= 7.5;
        });
    }

    @Override
    public boolean near(ClashEntity entity, Location target) {
        if(entity.getMovementType() == ClashMovementType.FLYING) {
            return entity.getLocation().distanceSquared(location) <= Math.pow(entity.getRange(), 2);
        }
        return entity.getLocation().distanceSquared(target) <= 25.0F;
    }


    @Override
    public Location getTarget(ClashEntity entity) {
        Location location = area.randomLocation();
        if(entity.getMovementType() == ClashMovementType.FLYING) {
            location.setY(entity.getLocation().getY());
        }
        return location;
    }

    @Override
    public ClashTargetType getTargetType() {
        return ClashTargetType.TOWER;
    }
}
