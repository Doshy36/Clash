package net.minespree.games.clash.entities.nms;

import net.minecraft.server.v1_8_R3.*;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalTargetAttack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.UUID;

public class ClashBalloon extends EntityGhast {

    private ClashEntity entity;
    private double yMin, yMax;

    public ClashBalloon(World world) {
        super(world);
    }

    public ClashBalloon(ClashEntity entity) {
        super(((CraftWorld) entity.getWorld()).getHandle());

        this.entity = entity;

        setSize(6.0F, 6.0F);

        setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());

        yMin = locY + 2.5F;
        yMax = locY + 5.0F;

        goalSelector = new PathfinderGoalSelector(new MethodProfiler());
        targetSelector = new PathfinderGoalSelector(new MethodProfiler());

        moveController = new ControllerMove(this);

        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(300.0D); // Increase the range it can move to

        goalSelector.a(0, new PathfinderGoalTargetAttack(entity, this, null));

        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public void m() {
        double height = locY;
        int direction;
        if(height > yMax) {
            direction = -1;
        } else if(height < yMin) {
            direction = 1;
        } else direction = 1;

        this.motY = direction * 0.05;

        super.m();
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public void bL() {}

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource != null && damagesource.getEntity() != null && damagesource.getEntity().getBukkitEntity().hasMetadata("damage")) {
            Player player = null;
            if(damagesource.getEntity().getBukkitEntity().hasMetadata("owner")) {
                player = Bukkit.getPlayer((UUID) damagesource.getEntity().getBukkitEntity().getMetadata("owner").get(0).value());
            }
            entity.damage(player, damagesource.getEntity().getBukkitEntity().getMetadata("damage").get(0).asFloat());
        } else {
            entity.damage(null, f);
        }
        return true;
    }

}
