package net.minespree.games.clash.entities.nms.pathfinding;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.ClashTarget;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.ClashTargetType;
import net.minespree.rise.RisePlugin;
import org.bukkit.Location;

public class PathfinderGoalTargetAttack extends PathfinderGoalClash {

    private ClashMapData data;
    private int tick, maxTicks;

    private ClashTargetType priority;
    //private PathEntity pathEntity; // cached path, don't want to re-calculate every tick.

    private ClashTarget target;
    private Location location;

    public PathfinderGoalTargetAttack(ClashEntity entity, EntityInsentient creature, ClashTargetType priority) {
        super(entity, creature);

        this.data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
        this.priority = priority;

        this.maxTicks = Math.max(entity.getDamageSpeed(), 100);

        findTarget();
    }

    @Override
    public boolean should() {
        if(data.isEnded()) {
            return false;
        } else if(creature.getGoalTarget() != null && creature.getGoalTarget().isAlive()) {
            target = null;
            return false;
        } else if(target == null || target.isDead()) {
            return findTarget();
        }
        return true;
    }

    @Override
    public void run() {
        tick++;

        boolean near = target.near(entity, location);

        if(creature.getNavigation().m()) {
            if(entity.getMovementType() == ClashMovementType.FLYING && !near) {
                creature.getControllerMove().a(location.getX(), location.getY(), location.getZ(), entity.getSpeed());
            } else {
                creature.getNavigation().a(location.getX(), location.getY(), location.getZ(), entity.getSpeed());
            }
            creature.getControllerLook().a(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), 10.0F, 10.0F);
        }

        if(near) {
            if(tick % entity.getDamageSpeed() == 0) {
                creature.getNavigation().n();

                creature.bw();
                target.damage(entity.getOwner(), entity.getDamage());
                entity.attack(target.getLocation());
            }
        } else if(tick >= maxTicks) {
            creature.getNavigation().n();
            findTarget();

            tick = 0;
        }
    }

    private boolean findTarget() {
        ClashTarget closest = null;
        Location closestLoc = null;
        for (ClashTarget target : data.getTargets()) {
            if(target.getTeam() == entity.getTeam() || !target.getSide().isOpposite(entity.getSide())) {
                continue;
            }
            if(closest == null) {
                closest = target;
                closestLoc = target.getTarget(entity);
            } else if(priority != null && closest.getTargetType() == priority) {
                if(target.getTargetType() == priority) {
                    Location targetLoc = target.getTarget(entity);
                    if(closer(closestLoc, targetLoc)) {
                        closest = target;
                        closestLoc = targetLoc;
                    }
                }
            } else {
                Location targetLoc = target.getTarget(entity);
                if(closer(closestLoc, targetLoc)) {
                    closest = target;
                    closestLoc = targetLoc;
                }
            }
        }
        if(closest == null) {
            return false;
        }
        this.target = closest;
        this.location = closestLoc;
        return true;
    }

    private boolean closer(Location closest, Location target) {
        return closest.distanceSquared(entity.getLocation()) > target.distanceSquared(entity.getLocation());
    }

}
