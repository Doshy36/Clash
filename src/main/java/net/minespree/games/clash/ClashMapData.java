package net.minespree.games.clash;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.Entity;
import net.minespree.babel.BabelMessage;
import net.minespree.cartographer.maps.ClashGameMap;
import net.minespree.cartographer.maps.GameMap;
import net.minespree.cartographer.util.ColourData;
import net.minespree.cartographer.util.GameArea;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.player.ClashPlayer;
import net.minespree.games.clash.towers.BasicClashTower;
import net.minespree.games.clash.towers.CastleClashTower;
import net.minespree.games.clash.towers.ClashTower;
import net.minespree.games.clash.units.ClashSpellUnit;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.maps.MapData;
import net.minespree.rise.teams.Team;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ThrownPotion;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
public class ClashMapData implements MapData {

    private ClashGameMap map;

    private List<ClashTower> towers = Lists.newArrayList();
    private Map<Team, GameArea> left = Maps.newHashMap(), right = Maps.newHashMap();
    private Map<Team, GameArea> spawns = Maps.newHashMap();
    private Map<Team, Pair<Float, Float>> yawPitch = Maps.newHashMap();

    private Set<Damageable> damageable = Sets.newConcurrentHashSet();
    private Set<ClashTarget> targets = Sets.newConcurrentHashSet(); // Minimize pathfinding cost so don't have to filter out targets.

    private Map<Entity, ClashEntity> entities = Maps.newConcurrentMap();
    private Map<ThrownPotion, ClashSpellUnit> spells = Maps.newConcurrentMap();
    private Map<UUID, List<BlockState>> lookingAt = Maps.newConcurrentMap();

    private Map<UUID, ClashPlayer> players = Maps.newConcurrentMap();
    private Map<Team, Integer> stars = Maps.newConcurrentMap();

    @Setter
    private BabelMessage scoreboardTitle;
    @Setter
    private int time, timeEnded;

    private ClashSpawnHandler spawnHandler;
    private ClashScoreboard scoreboard;
    private ClashListener listener;

    @Setter
    private boolean ended;

    public void create(GameMap m) {
        map = (ClashGameMap) m;

        time = 610;

        for (String side : map.getSlots().keySet()) {
            Team team = new Team(ColourData.valueOf(side.toUpperCase()), map.getSlots().get(side));
            for (ClashGameMap.ClashMapTower tower : map.getTowerAreas().get(side)) {
                map.getDisabledAreas().add(tower.getTower());
                map.getDisabledAreas().add(tower.getFront());
                towers.add(new BasicClashTower(this, team, tower.getTower(), tower.getFront()));
            }
            ClashGameMap.ClashMapTower tower = map.getCastleAreas().get(side);
            towers.add(new CastleClashTower(this, team, tower.getTower(), tower.getFront()));
            map.getDisabledAreas().add(tower.getTower());
            map.getDisabledAreas().add(tower.getFront());
            left.put(team, map.getLeft().get(side));
            right.put(team, map.getRight().get(side));
            spawns.put(team, map.getSpawns().get(side));
            yawPitch.put(team, map.getSpawnYawPitch().get(side));
        }
        spawnHandler = (ClashSpawnHandler) RisePlugin.getPlugin().getGameManager().getGamemode().get().getSpawnHandler().get();
        scoreboard = new ClashScoreboard(RisePlugin.getPlugin(), this);
        listener = new ClashListener(this);
    }
}
