package net.minespree.games.clash.entities.nms;

import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_8_R3.*;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.nms.navigation.NavigationClash;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalEnemyAttack;
import net.minespree.games.clash.entities.nms.pathfinding.PathfinderGoalTargetAttack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class ClashHogRider extends EntityPig {

    @Getter
    private EntityPlayer rider;

    public ClashHogRider(World world) {
        super(world);
    }

    public ClashHogRider(ClashEntity entity) {
        super(((CraftWorld) entity.getWorld()).getHandle());

        goalSelector = new PathfinderGoalSelector(new MethodProfiler());
        targetSelector = new PathfinderGoalSelector(new MethodProfiler());

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "");

        npc.spawn(entity.getLocation());
        npc.setProtected(false);
        npc.getEntity().setMetadata("npc", new FixedMetadataValue(Clash.getPlugin(), this));

        try {
            Clash.changeSkin(npc, "Hog Rider", "eyJ0aW1lc3RhbXAiOjE1MTQxMzczMzA2NTUsInByb2ZpbGVJZCI6IjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwicHJvZmlsZU5hbWUiOiJHb2xkYXBmZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U4M2VhMmMzNGE5MjU1NWFlOWVmYTY0NGZiMWE2OTI4MzgzZmJhN2IzZmM2YTgwODUyN2VkZTQ2ZWNhOWQ0In19fQ==",
                    "U2tcwiRRPfQudYT6bFwmI0I5x34AG12mJHB2jd34U88eoVwVEF3KDr1Tc1dTBGrDFnynOzO+ZOkhCTlyax2/D+f9USB8C56LFPqFA7Ds/LFOcYxOOwBTm7Fmok0fvqxaDtcu6n6GOqxN7kj/u0zzo7ZrJ2mOCHxCiuz72v1yTFzSy+dsArj20/koMp4gqa+VGJwgt21Pkk2h3ikedsiEAnMaXX6G49GcDTb1yw5jnF/ky2mMQPB0vgp1XvGlz4m5JfPOcdOAi28ZQV05Kc1UPd1yZNtmppLL4aVi+pMRZ6WxSR35Aqr8N0p8Kb0RGt6q5latgVMloNemZdovdDG93IjzoDZJGMwAWemw8Sl95DFSXG8JVveWaQ55KYDllSBbQOGEfrpGLirBkUfJs9Y8UQC/NeRNrwnBe88hfHbY6O3ZJGF8BMUbyXwnYcoT2IUT3DGwzzvHBlXuzBh4UX6Du//jZWPrhHIqALnrZPAXgeJQjTOdgPPCCY9Y4vKHqZKKFGpFxs9YVqTWJci0eA6MjFtODguT/fELxLhA/UWCpkCnJ1sQdW5H7ne6DnPY6n3tPCUY/Lau0wAQvafeHkx/9X/UFkt2XfPOHMkr/9eVpPQvX0xu4EkBCkFQpgX0cQDgtnJvLJLJvVJrecf7c/EIP92UOZE53vycqv6iOGioKu0=");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        rider = (EntityPlayer) ((CraftEntity) npc.getEntity()).getHandle();
        rider.getBukkitEntity().getInventory().setItem(0, new ItemStack(Material.STONE_AXE));

        getBukkitEntity().setPassenger(npc.getEntity());

        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(300.0D); // Increase the range it can move to
        getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(entity.getDamage());

        navigation = new NavigationClash(this, world);

        goalSelector.a(0, new PathfinderGoalEnemyAttack(entity, this));
        goalSelector.a(1, new PathfinderGoalTargetAttack(entity, this, null));

        setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());

        setSaddle(true);

        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if(super.damageEntity(damagesource, f)) {
            rider.damageEntity(damagesource, 0.0F);
            return true;
        }
        return false;
    }

    @Override
    public void onLightningStrike(EntityLightning entitylightning) {}

    @Override
    public void bw() {
        super.bw();

        rider.bw();
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public void bL() {}

}
