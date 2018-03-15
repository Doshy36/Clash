package net.minespree.games.clash.entities.types;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minespree.games.clash.Side;
import net.minespree.games.clash.entities.AttackType;
import net.minespree.games.clash.entities.ClashEntity;
import net.minespree.games.clash.entities.ClashEntityType;
import net.minespree.games.clash.entities.ClashMovementType;
import net.minespree.games.clash.entities.nms.ClashWizard;
import net.minespree.rise.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WizardClashEntity extends ClashEntity {

    public WizardClashEntity(Player owner, Team team, Location location, Side side) {
        super(owner, team, ClashEntityType.WIZARD, ClashMovementType.WALKING, AttackType.ALL, location, side, 2.0,650.0F, 15.0F, 1.2F, 15.0F, 5);
    }

    @Override
    public EntityInsentient spawn() {
        return new ClashWizard(this);
    }

    @Override
    public void attack(Location target) {
        EntitySnowball entitysnowball = new EntitySnowball(nmsEntity.world, nmsEntity);
        double d0 = target.getY() + 2 * Math.random();
        double d1 = target.getX() - getLocation().getX();
        double d2 = d0 - entitysnowball.locY;
        double d3 = target.getZ() - getLocation().getZ();
        float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        entitysnowball.shoot(d1, d2 + (double)f1, d3, 1.6F, 12.0F);
        nmsEntity.makeSound("random.bow", 1.0F, 1.0F / (nmsEntity.bc().nextFloat() * 0.4F + 0.8F));
        nmsEntity.world.addEntity(entitysnowball);
    }
}
