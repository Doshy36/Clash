package net.minespree.games.clash.entities;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minespree.games.clash.*;
import net.minespree.games.clash.entities.nms.ClashBalloon;
import net.minespree.games.clash.entities.nms.ClashHogRider;
import net.minespree.games.clash.states.ClashGameState;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.Game;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.util.Chat;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@Getter
public abstract class ClashEntity implements Damageable {

    private Player owner;
    private final Team team;
    private final String teamName;
    protected ClashMovementType movementType;
    protected AttackType attackType;
    protected ClashEntityType entityType;
    protected float health, maxHealth, damage, speed, range;
    protected int damageSpeed;
    protected boolean dead;

    protected Side side;

    private Location location;
    protected World world;

    protected FractionalDisplay display;
    protected EntityInsentient nmsEntity;

    public ClashEntity(Player owner, Team team, ClashEntityType entityType, ClashMovementType movementType, AttackType attackType, Location location, Side side,
                       double height, float health, float damage, float speed, float range, int damageSpeed) {
        this.owner = owner;
        this.team = team;
        this.entityType = entityType;
        this.movementType = movementType;
        this.attackType = attackType;
        this.teamName = team.getName().toString();
        this.maxHealth = health;
        this.side = side;
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.damageSpeed = damageSpeed;

        this.location = location;
        this.world = location.getWorld();

        if(RisePlugin.getPlugin().getGameStateManager().getCurrentState() instanceof ClashGameState) {
            this.nmsEntity = spawn();
            this.nmsEntity.getBukkitEntity().setMetadata("damage", new FixedMetadataValue(Clash.getPlugin(), damage)); // for balloons, since they're awkward...

            display = new FractionalDisplay(maxHealth, 10, Chat.BIG_HORIZONTAL_LINE, team.getColour().toString() + Chat.BOLD, Chat.GRAY + Chat.BOLD);
            display.spawn(location, height);

            ClashMapData data = ((ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData());
            data.getEntities().put(nmsEntity, this);
            data.getDamageable().add(this);

            ((LivingEntity) nmsEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        }
    }

    public abstract EntityInsentient spawn();

    public abstract void attack(Location target);

    @Override
    public void damage(Player damager, float damage) {
        if(!(nmsEntity instanceof ClashBalloon)) {
            ((LivingEntity) nmsEntity.getBukkitEntity()).damage(0.0F);
        }
        this.health -= damage;
        if(health < 0.0) {
            health = 0.0F;
        }
        display.build(health);
        if(health <= 0.0 && !dead) {
            kill(damager);
        }
    }

    @Override
    public void kill(Player killer) {
        dead = true;
        ((ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData()).getEntities().remove(nmsEntity);
        display.destroy();
        if(killer != null) {
            Game game = RisePlugin.getPlugin().getGameManager().getGameInProgress().get();
            game.changeStatistic(killer, "unitskilled", 1);
        }
        if(nmsEntity != null && nmsEntity.isAlive()) {
            nmsEntity.setHealth(0.0F);
            if(nmsEntity instanceof ClashHogRider) {
                ((ClashHogRider) nmsEntity).getRider().setHealth(0.0F);
            }
        }
    }

    @Override
    public Location getLocation() {
        if(nmsEntity == null)
            return location;
        return nmsEntity.getBukkitEntity().getLocation();
    }

}
