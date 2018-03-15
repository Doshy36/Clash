package net.minespree.games.clash.entities.nms.pathfinding;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.rise.RisePlugin;

public class PathfinderGoalEnemyAttack extends PathfinderGoalClash {

    private ClashMapData data;
    private int tick;

    public PathfinderGoalEnemyAttack(ClashEntity entity, EntityInsentient creature) {
        super(entity, creature);

        data = (ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData();
    }

    @Override
    public boolean should() {
        if(creature.getGoalTarget() == null) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        EntityLiving target = creature.getGoalTarget();

        if(target instanceof EntityPlayer || !creature.getGoalTarget().isAlive()) {
            creature.getNavigation().n();
            creature.setGoalTarget(null);
            return;
        }

        double var2 = creature.e(target.locX, target.getBoundingBox().b, target.locZ);
        double var4 = a((EntityCreature) creature, target);

        this.tick = Math.max(this.tick - 1, 0);
        if(creature.getEntitySenses().a(target)) {
            if (var2 <= var4 && tick <= 0) { // Is nearby
                creature.bw();
                if(data.getEntities().containsKey(creature.getGoalTarget())) {
                    data.getEntities().get(creature.getGoalTarget()).damage(entity.getOwner(), entity.getDamage());
                }
                tick = entity.getDamageSpeed();
            } else {
                creature.getControllerLook().a(target, 10.0F, 10.0F);
                if(entity.getMovementType() == ClashMovementType.FLYING) {
                    creature.getControllerMove().a(target.locX, ((EntityCreature) creature).locY, target.locZ, entity.getSpeed());
                } else {
                    creature.getNavigation().a(target, entity.getSpeed());
                }
            }
        }
    }

    private double a(EntityCreature entity, EntityLiving target) {
        return (double) (entity.width * 2.0F * entity.width * 2.0F + target.width) + 1.0;
    }

}
