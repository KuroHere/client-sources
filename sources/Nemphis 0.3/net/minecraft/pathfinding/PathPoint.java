/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;

public class PathPoint {
    public final int xCoord;
    public final int yCoord;
    public final int zCoord;
    private final int hash;
    int index = -1;
    float totalPathDistance;
    float distanceToNext;
    float distanceToTarget;
    PathPoint previous;
    public boolean visited;
    private static final String __OBFID = "CL_00000574";

    public PathPoint(int p_i2135_1_, int p_i2135_2_, int p_i2135_3_) {
        this.xCoord = p_i2135_1_;
        this.yCoord = p_i2135_2_;
        this.zCoord = p_i2135_3_;
        this.hash = PathPoint.makeHash(p_i2135_1_, p_i2135_2_, p_i2135_3_);
    }

    public static int makeHash(int p_75830_0_, int p_75830_1_, int p_75830_2_) {
        return p_75830_1_ & 255 | (p_75830_0_ & 32767) << 8 | (p_75830_2_ & 32767) << 24 | (p_75830_0_ < 0 ? Integer.MIN_VALUE : 0) | (p_75830_2_ < 0 ? 32768 : 0);
    }

    public float distanceTo(PathPoint p_75829_1_) {
        float var2 = p_75829_1_.xCoord - this.xCoord;
        float var3 = p_75829_1_.yCoord - this.yCoord;
        float var4 = p_75829_1_.zCoord - this.zCoord;
        return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
    }

    public float distanceToSquared(PathPoint p_75832_1_) {
        float var2 = p_75832_1_.xCoord - this.xCoord;
        float var3 = p_75832_1_.yCoord - this.yCoord;
        float var4 = p_75832_1_.zCoord - this.zCoord;
        return var2 * var2 + var3 * var3 + var4 * var4;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof PathPoint)) {
            return false;
        }
        PathPoint var2 = (PathPoint)p_equals_1_;
        if (this.hash == var2.hash && this.xCoord == var2.xCoord && this.yCoord == var2.yCoord && this.zCoord == var2.zCoord) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean isAssigned() {
        if (this.index >= 0) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.valueOf(this.xCoord) + ", " + this.yCoord + ", " + this.zCoord;
    }
}

