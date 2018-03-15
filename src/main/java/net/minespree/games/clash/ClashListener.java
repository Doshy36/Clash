package net.minespree.games.clash;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minespree.games.clash.entities.nms.ClashHogRider;
import net.minespree.rise.teams.Team;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Random;

public class ClashListener implements Listener {

    private static final Random RANDOM = new Random();

    private ClashMapData data;

    public ClashListener(ClashMapData data) {
        this.data = data;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!event.getEntity().hasMetadata("npc") && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.isCancelled() || event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING || event.getEntity() instanceof ArmorStand || event.getDamager() instanceof Player || (!event.getEntity().hasMetadata("npc") && event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        if(event.getDamager().hasMetadata("damage")) {
            Team team = null;
            if(event.getDamager().hasMetadata("team")) {
                team = (Team) event.getDamager().getMetadata("team").get(0).value();
            }
            float damage = event.getDamager().getMetadata("damage").get(0).asFloat();
            net.minecraft.server.v1_8_R3.Entity entity = ((CraftEntity) event.getEntity()).getHandle();
            if(event.getEntity().hasMetadata("npc")) {
                entity = (ClashHogRider) event.getEntity().getMetadata("npc").get(0).value();
            }
            if(team != null && data.getEntities().containsKey(entity) && team == data.getEntities().get(entity).getTeam()) {
                damage = 0.0F;
            }
            if(damage <= 0.0) {
                return;
            }
            if(data.getEntities().containsKey(entity)) {
                data.getEntities().get(entity).damage(null, damage);
            }
        } else {
            if (event.getFinalDamage() > 0.0 && (event.getEntity().hasMetadata("npc") || !(event.getEntity() instanceof Player))) {
                net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) event.getEntity()).getHandle();
                if(event.getEntity().hasMetadata("npc")) {
                    nmsEntity = (ClashHogRider) event.getEntity().getMetadata("npc").get(0).value();
                }
                if(nmsEntity != null) {
                    if (data.getEntities().containsKey(nmsEntity)) {
                        net.minecraft.server.v1_8_R3.Entity nmsDamager = ((CraftEntity) event.getDamager()).getHandle();
                        if(data.getEntities().containsKey(nmsDamager)) {
                            data.getEntities().get(nmsEntity).damage(data.getEntities().get(nmsDamager).getOwner(), (float) event.getFinalDamage());
                        } else {
                            data.getEntities().get(nmsEntity).damage(null, (float) event.getFinalDamage());
                        }
                    }
                }
            }
        }
        event.setCancelled(true);
        event.setDamage(0.0);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        net.minecraft.server.v1_8_R3.Entity entity = ((CraftEntity) event.getEntity()).getHandle();
        if(entity instanceof EntityCreature) {
            if (data.getEntities().containsKey(entity)) {
                data.getEntities().remove(entity).kill(null);
            }
        }

        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if(event.getDismounted() instanceof ArmorStand) {
            event.getDismounted().remove();
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
            ((Player) event.getEntity().getShooter()).updateInventory();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion) event.getEntity();
            if(data.getSpells().containsKey(potion)) {
                data.getSpells().get(potion).hit(potion.getLocation());
            }
        } else {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFire(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        for (org.bukkit.block.Block block : event.blockList()) {
            if (Math.random() <= 0.25) {
                FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getTypeId(), block.getData());
                fallingBlock.setVelocity(new Vector((RANDOM.nextBoolean() ? 1.0 : -1.0) * RANDOM.nextDouble(), 2.0 * RANDOM.nextDouble(), (RANDOM.nextBoolean() ? 1.0 : -1.0) * RANDOM.nextDouble()));

                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event) {
        event.getEntity().remove();
        event.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

}
