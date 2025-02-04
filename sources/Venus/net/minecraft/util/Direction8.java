/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.util;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;
import net.minecraft.util.Direction;

public enum Direction8 {
    NORTH(Direction.NORTH),
    NORTH_EAST(Direction.NORTH, Direction.EAST),
    EAST(Direction.EAST),
    SOUTH_EAST(Direction.SOUTH, Direction.EAST),
    SOUTH(Direction.SOUTH),
    SOUTH_WEST(Direction.SOUTH, Direction.WEST),
    WEST(Direction.WEST),
    NORTH_WEST(Direction.NORTH, Direction.WEST);

    private static final int NW_DIR_MASK;
    private static final int W_DIR_MASK;
    private static final int SW_DIR_MASK;
    private static final int S_DIR_MASK;
    private static final int SE_DIR_MASK;
    private static final int E_DIR_MASK;
    private static final int NE_DIR_MASK;
    private static final int N_DIR_MASK;
    private final Set<Direction> directions;

    private Direction8(Direction ... directionArray) {
        this.directions = Sets.immutableEnumSet(Arrays.asList(directionArray));
    }

    public Set<Direction> getDirections() {
        return this.directions;
    }

    static {
        NW_DIR_MASK = 1 << NORTH_WEST.ordinal();
        W_DIR_MASK = 1 << WEST.ordinal();
        SW_DIR_MASK = 1 << SOUTH_WEST.ordinal();
        S_DIR_MASK = 1 << SOUTH.ordinal();
        SE_DIR_MASK = 1 << SOUTH_EAST.ordinal();
        E_DIR_MASK = 1 << EAST.ordinal();
        NE_DIR_MASK = 1 << NORTH_EAST.ordinal();
        N_DIR_MASK = 1 << NORTH.ordinal();
    }
}

