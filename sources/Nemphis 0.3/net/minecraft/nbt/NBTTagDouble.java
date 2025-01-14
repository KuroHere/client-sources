/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.MathHelper;

public class NBTTagDouble
extends NBTBase.NBTPrimitive {
    private double data;
    private static final String __OBFID = "CL_00001218";

    NBTTagDouble() {
    }

    public NBTTagDouble(double data) {
        this.data = data;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeDouble(this.data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(64);
        this.data = input.readDouble();
    }

    @Override
    public byte getId() {
        return 6;
    }

    @Override
    public String toString() {
        return "" + this.data + "d";
    }

    @Override
    public NBTBase copy() {
        return new NBTTagDouble(this.data);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagDouble var2 = (NBTTagDouble)p_equals_1_;
            if (this.data == var2.data) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        long var1 = Double.doubleToLongBits(this.data);
        return super.hashCode() ^ (int)(var1 ^ var1 >>> 32);
    }

    @Override
    public long getLong() {
        return (long)Math.floor(this.data);
    }

    @Override
    public int getInt() {
        return MathHelper.floor_double(this.data);
    }

    @Override
    public short getShort() {
        return (short)(MathHelper.floor_double(this.data) & 65535);
    }

    @Override
    public byte getByte() {
        return (byte)(MathHelper.floor_double(this.data) & 255);
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public float getFloat() {
        return (float)this.data;
    }
}

