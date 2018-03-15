package net.minespree.games.clash.units;

import lombok.Getter;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.units.buildings.ArcherBuildingUnit;
import net.minespree.games.clash.units.buildings.ElixirCollectorBuildingUnit;
import net.minespree.games.clash.units.buildings.GoblinHutBuildingUnit;
import net.minespree.games.clash.units.spells.PoisonSpellUnit;
import net.minespree.games.clash.units.spells.ZapSpellUnit;
import net.minespree.games.clash.units.units.*;
import net.minespree.rise.RisePlugin;
import net.minespree.rise.control.Game;
import net.minespree.rise.teams.Team;
import net.minespree.wizard.util.Chat;
import net.minespree.wizard.util.ItemBuilder;
import net.minespree.wizard.util.TriFunction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public enum ClashUnitType {

    BLAZE("cl_blaze", UnitType.FLYING, BlazeUnit::new, "Blaze", new ItemBuilder(Material.FIREBALL), 4.0F),
    GOLEM("cl_golem", UnitType.SINGLE, GolemUnit::new, "G.O.L.E.M", new ItemBuilder(Material.IRON_INGOT), 7.0F),
    WIZARD("cl_ice_wizard", UnitType.SINGLE, WizardUnit::new, "Ice Wizard", new ItemBuilder(Material.ICE), 3.0F),
    BALLOON("cl_balloon", UnitType.FLYING, BalloonUnit::new, "Balloon", new ItemBuilder(Material.WOOL).durability((short) 14), 5.0F),
    HOG_RIDER("cl_hog_rider", UnitType.SINGLE, HogRiderUnit::new, "Hog Rider", new ItemBuilder(Material.SADDLE), 4.0F),
    WITCH("cl_witch", UnitType.SINGLE, WitchUnit::new, "Witch", new ItemBuilder(Material.CAULDRON_ITEM), 5.0F),

    GOBLINS("cl_goblins", UnitType.HORDE, GoblinUnit::new, "Goblins", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 2), 2.0F),
    GOBLIN_HORDE("cl_goblin_horde", UnitType.HORDE, GoblinHordeUnit::new, "Goblin Horde", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 2), 4.0F),
    BARBARIAN_HORDE("cl_barbarian", UnitType.HORDE, BarbarianUnit::new, "Barbarian Horde", new ItemBuilder(Material.NETHERRACK), 5.0F),

    ARCHER_BUILDING("cl_archer_building", UnitType.BUILDING, ArcherBuildingUnit::new, "Archer Tower", new ItemBuilder(Material.ARROW), 5.0F, 1, 1),
    GOBLIN_HUT_BUILDING("cl_goblin_hut", UnitType.BUILDING, GoblinHutBuildingUnit::new, "Goblin Hut", new ItemBuilder(Material.WOOD), 5.0F, 2, 2),
    ELIXIR_COLLECTOR_BUILDING("cl_elixir_collector", UnitType.BUILDING, ElixirCollectorBuildingUnit::new, "Elixir Collector", new ItemBuilder(Material.STAINED_GLASS).durability((short) 6), 6.0F, 2, 2),

    POISON_SPELL("cl_poison_spell", UnitType.SPELL, PoisonSpellUnit::new, "Poison Spell", new ItemBuilder(Material.POTION).potion(new Potion(PotionType.POISON).splash()), 5.0F, 0, 0),
    ZAP_SPELL("cl_zap_spell", UnitType.SPELL, ZapSpellUnit::new, "Zap Spell", new ItemBuilder(Material.POTION).potion(new Potion(PotionType.SPEED).splash()), 2.0F, 0, 0)
    ;

    private String perkId;
    @Getter
    private UnitType type;
    private TriFunction<Player, Team, Side, ClashUnit> spawnFunction;
    @Getter
    private String name;
    @Getter
    private ItemBuilder builder;
    @Getter
    private float elixirCost;
    @Getter
    private int xWidth, zWidth;

    ClashUnitType(String perkId, UnitType type, TriFunction<Player, Team, Side, ClashUnit> spawnFunction, String name, ItemBuilder builder, float elixirCost, int xWidth, int zWidth) {
        this.perkId = perkId;
        this.type = type;
        this.spawnFunction = spawnFunction;
        this.name = name;
        this.builder = builder;
        this.elixirCost = elixirCost;
        this.xWidth = xWidth;
        this.zWidth = zWidth;

        String displayName;
        switch (type) {
            case BUILDING:
                displayName = Chat.GOLD + Chat.BOLD + name;
                break;
            case SPELL:
                displayName = Chat.PURPLE + Chat.BOLD + name;
                break;
            default:
                displayName = Chat.GREEN + Chat.BOLD + name;
                break;
        }
        displayName += Chat.PINK + Chat.BOLD + " " + elixirCost;
        builder.displayName(displayName);
    }

    ClashUnitType(String perkId, UnitType type, TriFunction<Player, Team, Side, ClashUnit> spawnFunction, String name, ItemBuilder builder, float elixirCost) {
        this(perkId, type, spawnFunction, name, builder, elixirCost, 0, 0);
    }

    public ClashUnit spawn(Player player, Team team, Location location, Side side) {
        ClashUnit unit = spawnFunction.apply(player, team, side);
        Game game = RisePlugin.getPlugin().getGameManager().getGameInProgress().get();
        switch (type) {
            case BUILDING:
                game.changeStatistic(player, "buildingsspawned", 1);
                break;
            case SPELL:
                game.changeStatistic(player, "spellsspawned", 1);
                break;
            default:
                if(type == UnitType.SINGLE || type == UnitType.FLYING) {
                    game.changeStatistic(player, "unitsspawned", 1);
                }
                break;
        }
        unit.spawn(location);
        return unit;
    }

    public static ClashUnitType fromPerkId(String perkId) {
        for (ClashUnitType type : values()) {
            if(type.perkId.equals(perkId)) {
                return type;
            }
        }
        return null;
    }

}
