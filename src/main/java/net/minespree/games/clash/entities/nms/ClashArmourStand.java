package net.minespree.games.clash.entities.nms;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;

public class ClashArmourStand extends EntityArmorStand {

    public ClashArmourStand(World world) {
        super(world);
    }

    public ClashArmourStand(Location location) { // So the player can't see the armour stands loading in.
        super(((CraftWorld) location.getWorld()).getHandle());

        noclip = true;
        setInvisible(true);
        setBasePlate(false);
        setGravity(true);
        ((ArmorStand) getBukkitEntity()).setRemoveWhenFarAway(true);

        setPosition(location.getX(), location.getY(), location.getZ());

        world.addEntity(this);
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public void bL() {}
}
