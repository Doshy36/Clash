package net.minespree.games.clash;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.Skin;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.minespree.feather.data.gamedata.GameRegistry;
import net.minespree.feather.player.stats.local.SessionStatRegistry;
import net.minespree.feather.player.stats.local.StatType;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.states.ClashWaitingStateExtension;
import net.minespree.games.clash.states.PreGameState;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.Gamemode;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public class Clash extends JavaPlugin {

    @Getter
    private static Clash plugin;

    @Override
    public void onEnable() {
        plugin = this;

        RisePlugin.getPlugin().getGameManager().setGamemode(Gamemode
                .builder()
                .plugin(this)
                .teamHandler(true)
                .game(GameRegistry.Type.CLASH)
                .features(ImmutableList.of())
                .initialGameState(PreGameState::new)
                .extraDataLoader(map -> new ClashMapData())
                .waitingStateExtensions(ImmutableList.of(new ClashWaitingStateExtension()))
                .statisticSize(18)
                .statisticMap(new HashMap<String, StatType>() {
                    {put("towersdestroyed", new StatType("cl_towersdestroyed", SessionStatRegistry.Sorter.HIGHEST_SCORE, false, 10, 4, GameRegistry.Type.CLASH));}
                    {put("unitskilled", new StatType("cl_unitskilled", SessionStatRegistry.Sorter.HIGHEST_SCORE, false, 0, 3, GameRegistry.Type.CLASH));}
                    {put("buildingsspawned", new StatType("cl_buildingsspawned", SessionStatRegistry.Sorter.HIGHEST_SCORE, false, 0, 2, GameRegistry.Type.CLASH));}
                    {put("unitsspawned", new StatType("cl_unitsspawned", SessionStatRegistry.Sorter.HIGHEST_SCORE, false, 0, 1, GameRegistry.Type.CLASH));}
                    {put("spellsspawned", new StatType("cl_spellsspawned", SessionStatRegistry.Sorter.HIGHEST_SCORE, false, 0, 0, GameRegistry.Type.CLASH));}
                    {put("win", new StatType("cl_win", SessionStatRegistry.Sorter.HIGHEST_SCORE, true, 20, -1, GameRegistry.Type.CLASH));}
                    {put("loss", new StatType("cl_loss", SessionStatRegistry.Sorter.HIGHEST_SCORE, true, 0, -1, GameRegistry.Type.CLASH));}
                    {put("draw", new StatType("cl_draw", SessionStatRegistry.Sorter.HIGHEST_SCORE, true, 0, -1, GameRegistry.Type.CLASH));}
                    {put("gamesPlayed", new StatType("cl_gameplayed", SessionStatRegistry.Sorter.HIGHEST_SCORE, true, 10, -1, GameRegistry.Type.CLASH));}
                    {put("timePlayed", new StatType("cl_timeplayed", SessionStatRegistry.Sorter.HIGHEST_SCORE,true, 0, -1,
                            (p, o) -> p.getPersistentStats().getLongStatistics(GameRegistry.Type.CLASH).increment("cl_timeplayed", (Long) o)));}
                })
                .spawnHandler(ClashSpawnHandler::new)
                .build());

        for (ClashEntityType type : ClashEntityType.values()) {
            type.register();
        }
    }

    public static void changeSkin(NPC npc, String npcName, String texture, String signature) throws NoSuchFieldException, IllegalAccessException {
        try {
            Method method = Skin.class.getDeclaredMethod("setNPCSkinData", SkinnableEntity.class, String.class, UUID.class, Property.class);
            method.setAccessible(true);

            method.invoke(null, npc.getEntity(), npcName, UUID.nameUUIDFromBytes(npcName.getBytes()), new Property("textures", texture, signature));
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
