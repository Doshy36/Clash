package net.minespree.games.clash.entities;

public enum AttackType {

    AIR,
    GROUND,
    ALL;

    public boolean canAttack(ClashEntity entity) {
        if(this == ALL) {
            return true;
        } else if(entity.getMovementType() == ClashMovementType.FLYING && this == AIR) {
            return true;
        } else if(entity.getMovementType() == ClashMovementType.WALKING && this == GROUND) {
            return true;
        }
        return false;
    }

    public boolean isAir() {
        return this == ALL || this == AIR;
    }

    public boolean isGround() {
        return this == ALL || this == GROUND;
    }

}
