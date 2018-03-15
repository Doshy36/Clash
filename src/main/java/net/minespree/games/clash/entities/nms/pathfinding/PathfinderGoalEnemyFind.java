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

public class PathfinderGoalEnemyFind extends PathfinderGoalTarget {

    private ClashMapData data;
    private ClashEntity entity;
    private EntityLiving target;
    private final PathfinderGoalNearestAttackableTarget.DistanceComparator distanceComparator;

    public PathfinderGoalEnemyFind(ClashEntity entity, EntityCreature e) {
        super(e, false, false);

        this.entity = entity;
        this.distanceComparator = new PathfinderGoalNearestAttackableTarget.DistanceComparator(e);

        this.data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
    }

    @Override @SuppressWarnings("unchecked")
    public boolean a() {
        if(data.isEnded()) {
            return false;
        } else if(e.getGoalTarget() != null || !e.isAlive()) {
            return false;
        }

        List<Entity> list = e.getBukkitEntity().getWorld().getNearbyEntities(entity.getLocation(), entity.getRange(),
                entity.getAttackType().isAir() ? entity.getRange() : 1.0D, entity.getRange()).stream()
                .map(entity -> ((CraftEntity) entity).getHandle()).collect(Collectors.toList());
        list.removeIf(e -> e instanceof Player || !this.e.getEntitySenses().a(e) || !data.getEntities().containsKey(e) || data.getEntities().get(e).getTeam().equals(entity.getTeam())
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
        e.setGoalTarget(target, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
        super.c();
    }


}
