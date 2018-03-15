package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashWitch;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WitchClashEntity extends ClashEntity {

    public WitchClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.WITCH, ClashMovementType.WALKING, AttackType.GROUND, location, side, 1.5, 700.0F, 0.0F, 1.0F, 8.0F, 80);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashWitch(this);
    }

    @Override
    public void attack(Location target) {
        EntityPotion var3 = new EntityPotion(nmsEntity.world, nmsEntity, 16);
        double var4 = target.getY() + 2 * Math.random();
        var3.pitch -= -20.0F;
        double var6 = target.getX() - nmsEntity.locX;
        double var8 = var4 - nmsEntity.locY;
        double var10 = target.getZ() - nmsEntity.locZ;
        float var12 = MathHelper.sqrt(var6 * var6 + var10 * var10);

        var3.shoot(var6, var8 + (double)(var12 * 0.2F), var10, 0.75F, 8.0F);
        nmsEntity.world.addEntity(var3);
    }
}
