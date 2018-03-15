package net.minespree.games.clash.entities.nms.pathfinding;

import net.minecraft.server.v1_8_R3.*;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.rise.RisePlugin;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.List;
import java.util.stream.Collectors;

public class PathfinderGoalEnemyTarget extends PathfinderGoal {

    private ClashMapData data;
    private final PathfinderGoalNearestAttackableTarget.DistanceComparator distanceComparator;
    private EntityLiving target;
    private ClashEntity entity;
    private EntityInsentient creature;

    public PathfinderGoalEnemyTarget(ClashEntity entity, EntityInsentient creature) {
        this.entity = entity;
        this.creature = creature;

        this.distanceComparator = new PathfinderGoalNearestAttackableTarget.DistanceComparator(creature);
        this.data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
    }

    @Override
    public boolean a() {
        if(data.isEnded()) {
            return false;
        } else if(creature.getGoalTarget() != null || !creature.isAlive()) {
            return false;
        }
        List<Entity> list = creature.getBukkitEntity().getWorld().getNearbyEntities(entity.getLocation(), entity.getRange(),
                entity.getAttackType().isAir() ? entity.getRange() : 1.0D, entity.getRange()).stream()
                .map(entity -> ((CraftEntity) entity).getHandle()).collect(Collectors.toList());
        list.removeIf(e -> e instanceof Player || !creature.getEntitySenses().a(e) || !data.getEntities().containsKey(e) || data.getEntities().get(e).getTeam().equals(entity.getTeam())
                || !data.getEntities().get(e).getAttackType().canAttack(entity));
        if(list.isEmpty()) {
            return false;
        }
        list.sort(distanceComparator);
        target = (EntityLiving) list.get(0);
        return true;
    }

    @Override
    public void c() {
        creature.setGoalTarget(target, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
        super.c();
    }

    public void d() {
        creature.setGoalTarget(null);
        super.c();
    }

}
