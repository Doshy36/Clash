package net.minespree.games.clash.entities.nms;

import net.minecraft.server.v1_8_R3.*;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.nms.navigation.NavigationClash;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalEnemyFind;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalTargetAttack;
import net.minespree.games.clash.units.spells.PoisonSpellUnit;
import net.minespree.rise.RisePlugin;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class ClashWitch extends EntityWitch {

    private ClashEntity entity;

    public ClashWitch(World world) {
        super(world);
    }

    public ClashWitch(ClashEntity entity) {
        super(((CraftWorld) entity.getWorld()).getHandle());

        this.entity = entity;

        goalSelector = new PathfinderGoalSelector(new MethodProfiler());
        targetSelector = new PathfinderGoalSelector(new MethodProfiler());

        navigation = new NavigationClash(this, world);

        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(300.0D); // Increase the range it can move to
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(entity.getDamage());

        goalSelector.a(0, new PathfinderGoalArrowAttack(this, entity.getSpeed(), entity.getDamageSpeed(), 10.0F));
        goalSelector.a(1, new PathfinderGoalTargetAttack(entity, this, null));

        targetSelector.a(0, new PathfinderGoalEnemyFind(entity, this));

        setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());

        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public void a(EntityLiving var1, float var2) {
        if(!this.n()) {
            EntityPotion var3 = new EntityPotion(this.world, this, 32732);
            double var4 = var1.locY + (double)var1.getHeadHeight() - 1.100000023841858D;
            var3.pitch -= -20.0F;
            double var6 = var1.locX + var1.motX - this.locX;
            double var8 = var4 - this.locY;
            double var10 = var1.locZ + var1.motZ - this.locZ;
            float var12 = MathHelper.sqrt(var6 * var6 + var10 * var10);
            if(var12 >= 8.0F && !var1.hasEffect(MobEffectList.SLOWER_MOVEMENT)) {
                var3.setPotionValue(32698);
            } else if(var1.getHealth() >= 8.0F && !var1.hasEffect(MobEffectList.POISON)) {
                var3.setPotionValue(32660);
            } else if(var12 <= 3.0F && !var1.hasEffect(MobEffectList.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                var3.setPotionValue(32696);
            }

            var3.shoot(var6, var8 + (double)(var12 * 0.2F), var10, 0.75F, 8.0F);
            this.world.addEntity(var3);

            ((ClashMapData) RisePlugin.getPlugin().getMapManager().getMapData()).getSpells().put((ThrownPotion) var3.getBukkitEntity(),
                    new PoisonSpellUnit(entity.getOwner(), entity.getTeam(), entity.getSide(), 1.5F));
        }
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public void bL() {}

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
