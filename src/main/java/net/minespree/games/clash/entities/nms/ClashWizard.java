package net.minespree.games.clash.entities.nms;

import net.minecraft.server.v1_8_R3.*;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.nms.navigation.NavigationClash;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalEnemyFind;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalTargetAttack;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class ClashWizard extends EntitySnowman {

    private ClashEntity entity;

    public ClashWizard(World world) {
        super(world);
    }

    public ClashWizard(ClashEntity entity) {
        super(((CraftWorld) entity.getWorld()).getHandle());

        this.entity = entity;

        goalSelector = new PathfinderGoalSelector(new MethodProfiler());
        targetSelector = new PathfinderGoalSelector(new MethodProfiler());

        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(300.0D); // Increase the range it can move to
        getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(entity.getDamage());

        navigation = new NavigationClash(this, world);

        goalSelector.a(0, new PathfinderGoalArrowAttack(this, entity.getSpeed(), entity.getDamageSpeed(), 10.0F));
        goalSelector.a(1, new PathfinderGoalTargetAttack(entity, this, null));

        targetSelector.a(0, new PathfinderGoalEnemyFind(entity, this));

        setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());

        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public void bL() {}

    public void a(EntityLiving entityliving, float f) {
        EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
        double d0 = entityliving.locY + (double)entityliving.getHeadHeight() - 1.100000023841858D;
        double d1 = entityliving.locX - this.locX;
        double d2 = d0 - entitysnowball.locY;
        double d3 = entityliving.locZ - this.locZ;
        float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        entitysnowball.shoot(d1, d2 + (double)f1, d3, 1.6F, 12.0F);
        entitysnowball.getBukkitEntity().setMetadata("damage", new FixedMetadataValue(Clash.getPlugin(), entity.getDamage()));
        entitysnowball.getBukkitEntity().setMetadata("owner", new FixedMetadataValue(Clash.getPlugin(), entity.getOwner().getUniqueId()));
        entitysnowball.getBukkitEntity().setMetadata("team", new FixedMetadataValue(Clash.getPlugin(), entity.getTeam()));
        this.makeSound("random.bow", 1.0F, 1.0F / (this.bc().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(entitysnowball);
    }

    @Override
    public void m() {
        if(this.bc > 0) {
            double d0 = this.locX + (this.bd - this.locX) / (double)this.bc;
            double d1 = this.locY + (this.be - this.locY) / (double)this.bc;
            double d2 = this.locZ + (this.bf - this.locZ) / (double)this.bc;
            double d3 = MathHelper.g(this.bg - (double)this.yaw);
            this.yaw = (float)((double)this.yaw + d3 / (double)this.bc);
            this.pitch = (float)((double)this.pitch + (this.bh - (double)this.pitch) / (double)this.bc);
            --this.bc;
            this.setPosition(d0, d1, d2);
            this.setYawPitch(this.yaw, this.pitch);
        } else if(!this.bM()) {
            this.motX *= 0.98D;
            this.motY *= 0.98D;
            this.motZ *= 0.98D;
        }

        if(Math.abs(this.motX) < 0.005D) {
            this.motX = 0.0D;
        }

        if(Math.abs(this.motY) < 0.005D) {
            this.motY = 0.0D;
        }

        if(Math.abs(this.motZ) < 0.005D) {
            this.motZ = 0.0D;
        }

        this.world.methodProfiler.a("ai");
        if(this.bD()) {
            this.aY = false;
            this.aZ = 0.0F;
            this.ba = 0.0F;
            this.bb = 0.0F;
        } else if(this.bM()) {
            this.world.methodProfiler.a("newAi");
            this.doTick();
            this.world.methodProfiler.b();
        }

        this.world.methodProfiler.b();

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("travel");
        this.aZ *= 0.98F;
        this.ba *= 0.98F;
        this.bb *= 0.9F;
        this.g(this.aZ, this.ba);
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("push");
        if(!this.world.isClientSide) {
            this.bL();
        }

        this.world.methodProfiler.b();
    }
}
