package net.minespree.games.clash.states;

import com.google.common.collect.Lists;
import net.citizensnpcs.api.CitizensAPI;
import net.minespree.babel.Babel;
import net.minespree.babel.BabelMessage;
import net.minespree.babel.ComplexBabelMessage;
import net.minespree.cartographer.util.GameArea;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.ClashMapData;
import net.minespree.games.clash.Damageable;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.player.ClashPlayer;
import net.minespree.games.clash.units.*;
import net.minespree.rise.states.BaseGameState;
import net.minespree.rise.states.GameState;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.util.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class ClashGameState extends BaseGameState {

    static final BabelMessage DESTROY_ENEMY_CASTLE = Babel.translate("cl_destroy_enemy_castle");

    protected ClashMapData data;
    private int tick;

    private BukkitTask task;

    boolean seeTarget;

    ClashGameState() {
        data = (ClashMapData) mapManager.getMapData();

        seeTarget = true;
    }

    @Override
    public void onStart(GameState previous) {
        task = Bukkit.getScheduler().runTaskTimer(Clash.getPlugin(), this::tick, 1L, 1L);
    }

    @Override
    public void onStop(GameState next) {
        if(task != null) {
            task.cancel();
        }
    }

    void tick() {
        tick++;
        if(tick % 20 == 0) {
            tick = 0;

            data.setTime(data.getTime() - 1);
            time(data.getTime());
        }
        for (Team team : teamHandler.getTeams()) {
            data.getScoreboard().updateTowers(team);
        }
        for (ClashPlayer player : data.getPlayers().values()) {
            player.increment();
            player.update();
        }
        for (net.minecraft.server.v1_8_R3.Entity entity : data.getEntities().keySet()) {
            ClashEntity e = data.getEntities().get(entity);
            e.getDisplay().teleport(((LivingEntity) entity.getBukkitEntity()).getEyeLocation());
            if(entity.inWater) {
                e.damage(null, e.getMaxHealth());
            }
        }
        if(seeTarget) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (data.getPlayers().containsKey(player.getUniqueId())) {
                    int heldItemSlot = player.getInventory().getHeldItemSlot();
                    ClashPlayer clashPlayer = data.getPlayers().get(player.getUniqueId());
                    if(clashPlayer.has(heldItemSlot)) {
                        ClashUnitType type = clashPlayer.get(heldItemSlot);
                        if(type.getType() != UnitType.SPELL) {
                            Location location = player.getTargetBlock((HashSet<Material>) null, 150).getLocation();
                            Area area = new Area(location, (double) type.getXWidth(), (double) type.getZWidth());

                            if(canPlace(player, area)) {
                                List<BlockState> currentStates = data.getLookingAt().getOrDefault(player.getUniqueId(), Lists.newArrayList());
                                List<BlockState> states = Lists.newArrayList();
                                area.getBlocks(location.getWorld(), true).forEach(block -> {
                                    states.add(block.getState());
                                    player.sendBlockChange(block.getLocation(), Material.WOOL, (byte) ((clashPlayer.hasEnough(type.getElixirCost())) ? 5 : 14));
                                });
                                for (BlockState blockState : currentStates) {
                                    if (!states.contains(blockState)) {
                                        blockState.update(true, true);
                                    }
                                }
                                data.getLookingAt().put(player.getUniqueId(), states);
                            } else {
                                data.getLookingAt().getOrDefault(player.getUniqueId(), Collections.emptyList()).forEach(state -> state.update(true, true));
                            }
                        } else {
                            data.getLookingAt().getOrDefault(player.getUniqueId(), Collections.emptyList()).forEach(state -> state.update(true, true));
                        }
                    }
                }
            }
        }
    }

    public abstract void time(int time);

    @Override
    public void onJoin(Player player) {
        super.onJoin(player);

        data.getScoreboard().onStart(player);
        spectatorHandler.setSpectator(player);
    }

    @Override
    public void onQuit(Player player) {
        data.getPlayers().remove(player.getUniqueId());

        Team winner = null;
        boolean end = false;
        for (Team team : teamHandler.getTeams()) {
            if (team.size() > 0) {
                winner = team;
            } else {
                end = true;
            }
        }
        if(end) {
            end(winner);
        } else {
            for (Team team : teamHandler.getTeams()) {
                if(team.size() == 1) {
                    data.getPlayers().get(team.getPlayers().get(0)).setIndividualMultiplier(1.5F);
                }
            }
        }
    }

    private boolean canPlace(Player player, Area area) {
        if(!area.inside(data.getLeft().get(teamHandler.getTeam(player))) && !area.inside(data.getRight().get(teamHandler.getTeam(player)))) {
            return false;
        }
        for (GameArea disabledArea : data.getMap().getDisabledAreas()) {
            if (disabledArea.intersects(area)) {
                return false;
            }
        }
        for (Damageable d : data.getDamageable()) {
            if(d instanceof ClashBuildingUnit) {
                if (((ClashBuildingUnit) d).getArea().intersects(area)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void end(Team winner) {
        if (!data.isEnded()) {
            teamHandler.disableTeamChat();
            CitizensAPI.getNPCRegistry().deregisterAll();
            mapManager.getCurrentWorld().get().getEntities().stream()
                    .filter(entity -> !(entity instanceof Player) && !(entity instanceof ArmorStand))
                    .forEach(Entity::remove);
            if(task != null) {
                task.cancel();
            }
            HandlerList.unregisterAll(data.getListener());
            data.setEnded(true);

            if (winner != null) {
                game.endGame(new ComplexBabelMessage().append(Babel.translate("cl_winner"), winner.getColour() + winner.getName().toString()), winner.getName().toString());
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(data.getPlayers().containsKey(player.getUniqueId())) {
                        if(winner.hasPlayer(player)) {
                            game.changeStatistic(player, "win", 1);
                        } else {
                            game.changeStatistic(player, "loss", 1);
                        }
                    }
                }
            } else {
                game.endGame(Babel.translate("cl_draw"), Babel.translate("cl_draw").toString());
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(data.getPlayers().containsKey(player.getUniqueId())) {
                        game.changeStatistic(player, "draw", 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        event.getPlayer().updateInventory();
        event.setCancelled(true);
        if(!data.getPlayers().containsKey(event.getPlayer().getUniqueId()) || event.getItem() == null)
            return;
        Player player = event.getPlayer();
        ClashPlayer clashPlayer = data.getPlayers().get(player.getUniqueId());
        ClashUnitType type = clashPlayer.get(player.getInventory().getHeldItemSlot());

        if(seeTarget && clashPlayer.has(player.getInventory().getHeldItemSlot())) {
            if(type.getType() != UnitType.SPELL) {
                Location location = player.getTargetBlock((HashSet<Material>) null, 150).getLocation();
                Area area = new Area(location, (double) type.getXWidth(), (double) type.getZWidth());
                if (canPlace(event.getPlayer(), area)) {
                    clashPlayer.apply(location, event.getPlayer().getInventory().getHeldItemSlot(), Side.from(data.getRight().get(teamHandler.getTeam(event.getPlayer())).intersects(area)));
                }
            } else {
                ClashUnit unit = clashPlayer.apply(player.getEyeLocation(), player.getInventory().getHeldItemSlot(), Side.BOTH);
                if(unit != null) {
                    Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(1.5F));
                    ThrownPotion potion = (ThrownPotion) player.getWorld().spawnEntity(location, EntityType.SPLASH_POTION);
                    potion.setVelocity(player.getEyeLocation().getDirection().multiply(0.8F));
                    potion.setShooter(player);
                    data.getSpells().put(potion, (ClashSpellUnit) unit);
                }
            }
        }
    }

}
