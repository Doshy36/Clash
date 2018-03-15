package net.minespree.games.clash;

public enum Side {

    LEFT,
    RIGHT,
    BOTH;

    public boolean isOpposite(Side side) {
        return this == BOTH || side == BOTH || this != side;
    }

    public static Side from(boolean bool) {
        return bool ? RIGHT : LEFT;
    }

}
