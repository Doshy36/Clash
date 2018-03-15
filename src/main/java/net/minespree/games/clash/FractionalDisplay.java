package net.minespree.games.clash;

import net.minespree.games.clash.entities.nms.ClashArmourStand;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class FractionalDisplay {

    private final float max;
    private final float length;
    private final float perLine;
    private String c, has, blank;

    private double height;

    private ArmorStand stand;

    public FractionalDisplay(float max, float length, String c, String has, String blank) {
        this.max = max;
        this.length = length;
        this.c = c;
        this.has = has;
        this.blank = blank;

        this.perLine = max / length;
    }

    public ArmorStand spawn(Location location) {
        stand = (ArmorStand) new ClashArmourStand(location).getBukkitEntity();
        stand.setCustomName(build(max));
        stand.setCustomNameVisible(true);
        stand.setMarker(true);
        return stand;
    }

    public ArmorStand spawn(Location location, double height) {
        this.height = height;

        return spawn(location.clone().add(0, height, 0));
    }

    public void teleport(Location location) {
        stand.teleport(location.clone().add(0, height, 0));
        stand.setMarker(true);
        stand.setSmall(true);
    }

    public void destroy() {
        if(stand != null) {
            stand.remove();
        }
    }

    public String build(float current) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if(current > (perLine * i)) {
                builder.append(has);
            } else {
                builder.append(blank);
            }
            builder.append(c);
        }
        if(stand != null) {
            stand.setCustomName(builder.toString().trim());
        }
        return builder.toString().trim();
    }

}
