package net.minespree.games.clash.entities.nms;

import net.minecraft.server.v1_8_R3.*;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.nms.navigation.NavigationClash;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalEnemyAttack;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalEnemyFind;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalTargetAttack;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class ClashBarbarian extends EntityPigZombie {

    public ClashBarbarian(World world) {
        super(world);
    }

    public ClashBarbarian(ClashEntity entity) {
        super(((CraftWorld) entity.getWorld()).getHandle());

        goalSelector = new PathfinderGoalSelector(new MethodProfiler());
        targetSelector = new PathfinderGoalSelector(new MethodProfiler());

        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(300.0D); // Increase the range it can move to
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(entity.getDamage());

        navigation = new NavigationClash(this, world);

        goalSelector.a(0, new PathfinderGoalEnemyAttack(entity, this));
        goalSelector.a(1, new PathfinderGoalTargetAttack(entity, this, null));

        targetSelector.a(0, new PathfinderGoalEnemyFind(entity, this));

        setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
        setBaby(false);

        ((PigZombie) getBukkitEntity()).getEquipment().setItemInHand(new org.bukkit.inventory.ItemStack(org.bukkit.Material.IRON_SWORD));

        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public void bL() {}

}
