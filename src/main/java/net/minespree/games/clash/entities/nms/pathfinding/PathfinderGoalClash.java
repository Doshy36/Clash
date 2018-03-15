package net.minespree.games.clash.entities.nms.pathfinding;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minespree.games.clash.entities.ClashEntity;

public abstract class PathfinderGoalClash extends PathfinderGoal {

    protected ClashEntity entity;
    protected EntityInsentient creature;

    public PathfinderGoalClash(ClashEntity entity, EntityInsentient creature) {
        this.entity = entity;
        this.creature = creature;
    }

    @Override
    public boolean a() {
        return creature.isAlive() && should();
    }

    @Override
    public boolean b() {
        return a();
    }

    @Override
    public void e() {
        run();
    }

    public abstract boolean should();

    public void run() {}

}
