package net.minespree.games.clash.entities;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minespree.feather.util.reflection.ReflectionUtil;
import net.minespree.games.clash.entities.nms.*;

import java.util.Map;

public enum ClashEntityType {

    GOBLIN("ClashGoblin", 54, ClashGoblin.class),
    BLAZE("ClashBlaze", 61, ClashBlaze.class),
    GOLEM("ClashGolem", 99, ClashGolem.class),
    BARBARIAN("ClashBarbarian", 57, ClashBarbarian.class),
    WIZARD("ClashWizard", 97, ClashWizard.class),
    BALLOON("ClashBalloon", 56, ClashBalloon.class),
    HOG_RIDER("ClashHogRider", 90, ClashHogRider.class),
    WITCH("ClashWitch", 66, ClashWitch.class),

    ARMOUR_STAND("ArmorStand", 30, ClashArmourStand.class),

    ;

    private String name;
    private int id;
    private Class<? extends Entity> clazz;

    ClashEntityType(String name, int id, Class<? extends Entity> clazz) {
        this.name = name;
        this.id = id;
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public void register() {
        if(name == null)
            return;
        ((Map) ReflectionUtil.getField(EntityTypes.class, "c").get(null)).put(name, clazz);
        ((Map) ReflectionUtil.getField(EntityTypes.class, "d").get(null)).put(clazz, name);
        ((Map) ReflectionUtil.getField(EntityTypes.class, "e").get(null)).put(id, clazz);
        ((Map) ReflectionUtil.getField(EntityTypes.class, "f").get(null)).put(clazz, id);
        ((Map) ReflectionUtil.getField(EntityTypes.class, "g").get(null)).put(name, id);
    }

}
