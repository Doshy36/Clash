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

public class ClashBlaze extends EntityBlaze {

    private double yMin, yMax;

    public ClashBlaze(World world) {
        super(world);
    }

    public ClashBlaze(ClashEntity entity) {
        super(((CraftWorld) entity.getWorld()).getHandle());

        setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());

        yMin = locY - 2.0F;
        yMax = locY + 2.0F;

        goalSelector = new PathfinderGoalSelector(new MethodProfiler());
        targetSelector = new PathfinderGoalSelector(new MethodProfiler());

        navigation = new NavigationClash(this, world);

        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(300.0D); // Increase the range it can move to

        goalSelector.a(0, new PathfinderGoalBlazeFireball(this, entity));
        goalSelector.a(1, new PathfinderGoalTargetAttack(entity, this, null));

        targetSelector.a(0, new PathfinderGoalEnemyFind(entity, this));

        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public void bL() {}

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

    public class PathfinderGoalBlazeFireball extends PathfinderGoal {
        private EntityBlaze blaze;
        private ClashEntity entity;
        private int c;

        PathfinderGoalBlazeFireball(EntityBlaze var1, ClashEntity entity) {
            this.blaze = var1;
            this.entity = entity;
            this.a(3);
        }

        public boolean a() {
            EntityLiving var1 = this.blaze.getGoalTarget();
            return var1 != null && var1.isAlive();
        }

        public void d() {
            this.blaze.a(false);
        }

        public void e() {
            --this.c;
            EntityLiving var1 = this.blaze.getGoalTarget();
            double var2 = this.blaze.h(var1);
            if(var2 < 4.0D) {
                if(this.c <= 0) {
                    this.c = entity.getDamageSpeed();
                    this.blaze.r(var1);
                }

                this.blaze.getControllerMove().a(var1.locX, var1.locY, var1.locZ, entity.getSpeed());
            } else if(var2 < 256.0D) {
                double var4 = var1.locX - this.blaze.locX;
                double var6 = var1.getBoundingBox().b + (double)(var1.length / 2.0F) - (this.blaze.locY + (double)(this.blaze.length / 2.0F));
                double var8 = var1.locZ - this.blaze.locZ;
                if(this.c <= 0) {
                    this.c = entity.getDamageSpeed();
                    this.blaze.a(false);


                    float var10 = MathHelper.c(MathHelper.sqrt(var2)) * 0.5F;
                    this.blaze.world.a(null, 1009, new BlockPosition((int) this.blaze.locX, (int) this.blaze.locY, (int) this.blaze.locZ), 0);

                    for (int i = 0; i < 3 + random.nextInt(2); i++) {
                        EntitySmallFireball var12 = new EntitySmallFireball(this.blaze.world, this.blaze, var4 + this.blaze.bc().nextGaussian() * (double) var10, var6, var8 + this.blaze.bc().nextGaussian() * (double) var10);
                        var12.getBukkitEntity().setMetadata("damage", new FixedMetadataValue(Clash.getPlugin(), entity.getDamage()));
                        var12.getBukkitEntity().setMetadata("owner", new FixedMetadataValue(Clash.getPlugin(), entity.getOwner().getUniqueId()));
                        var12.getBukkitEntity().setMetadata("team", new FixedMetadataValue(Clash.getPlugin(), entity.getTeam()));
                        var12.locY = this.blaze.locY + (double) (this.blaze.length / 2.0F) + 0.5D;
                        this.blaze.world.addEntity(var12);
                    }
                }
                this.blaze.getControllerLook().a(var1, 10.0F, 10.0F);
            } else {
                this.blaze.getNavigation().n();
                this.blaze.getControllerMove().a(var1.locX, var1.locY, var1.locZ, entity.getSpeed());
            }

            super.e();
        }
    }

}
