/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.src;

import net.minecraft.src.Config;
import net.minecraft.src.RangeInt;

public class RangeListInt {
    private RangeInt[] ranges = new RangeInt[0];

    public void addRange(RangeInt ri) {
        this.ranges = (RangeInt[])Config.addObjectToArray(this.ranges, ri);
    }

    public boolean isInRange(int val) {
        for (int i = 0; i < this.ranges.length; ++i) {
            RangeInt ri = this.ranges[i];
            if (!ri.isInRange(val)) continue;
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < this.ranges.length; ++i) {
            RangeInt ri = this.ranges[i];
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(ri.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}

