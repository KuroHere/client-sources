/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.world.gen.structure;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

public class StructureBoundingBox {
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    private static final String __OBFID = "CL_00000442";

    public StructureBoundingBox() {
    }

    public StructureBoundingBox(int[] p_i43000_1_) {
        if (p_i43000_1_.length == 6) {
            this.minX = p_i43000_1_[0];
            this.minY = p_i43000_1_[1];
            this.minZ = p_i43000_1_[2];
            this.maxX = p_i43000_1_[3];
            this.maxY = p_i43000_1_[4];
            this.maxZ = p_i43000_1_[5];
        }
    }

    public static StructureBoundingBox getNewBoundingBox() {
        return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static StructureBoundingBox func_175897_a(int p_175897_0_, int p_175897_1_, int p_175897_2_, int p_175897_3_, int p_175897_4_, int p_175897_5_, int p_175897_6_, int p_175897_7_, int p_175897_8_, EnumFacing p_175897_9_) {
        switch (SwitchEnumFacing.field_175895_a[p_175897_9_.ordinal()]) {
            case 1: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ - p_175897_8_ + 1 + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_5_);
            }
            case 2: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
            }
            case 3: {
                return new StructureBoundingBox(p_175897_0_ - p_175897_8_ + 1 + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
            }
            case 4: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_8_ - 1 + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
            }
        }
        return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
    }

    public static StructureBoundingBox func_175899_a(int p_175899_0_, int p_175899_1_, int p_175899_2_, int p_175899_3_, int p_175899_4_, int p_175899_5_) {
        return new StructureBoundingBox(Math.min(p_175899_0_, p_175899_3_), Math.min(p_175899_1_, p_175899_4_), Math.min(p_175899_2_, p_175899_5_), Math.max(p_175899_0_, p_175899_3_), Math.max(p_175899_1_, p_175899_4_), Math.max(p_175899_2_, p_175899_5_));
    }

    public StructureBoundingBox(StructureBoundingBox p_i2031_1_) {
        this.minX = p_i2031_1_.minX;
        this.minY = p_i2031_1_.minY;
        this.minZ = p_i2031_1_.minZ;
        this.maxX = p_i2031_1_.maxX;
        this.maxY = p_i2031_1_.maxY;
        this.maxZ = p_i2031_1_.maxZ;
    }

    public StructureBoundingBox(int p_i2032_1_, int p_i2032_2_, int p_i2032_3_, int p_i2032_4_, int p_i2032_5_, int p_i2032_6_) {
        this.minX = p_i2032_1_;
        this.minY = p_i2032_2_;
        this.minZ = p_i2032_3_;
        this.maxX = p_i2032_4_;
        this.maxY = p_i2032_5_;
        this.maxZ = p_i2032_6_;
    }

    public StructureBoundingBox(Vec3i p_i45626_1_, Vec3i p_i45626_2_) {
        this.minX = Math.min(p_i45626_1_.getX(), p_i45626_2_.getX());
        this.minY = Math.min(p_i45626_1_.getY(), p_i45626_2_.getY());
        this.minZ = Math.min(p_i45626_1_.getZ(), p_i45626_2_.getZ());
        this.maxX = Math.max(p_i45626_1_.getX(), p_i45626_2_.getX());
        this.maxY = Math.max(p_i45626_1_.getY(), p_i45626_2_.getY());
        this.maxZ = Math.max(p_i45626_1_.getZ(), p_i45626_2_.getZ());
    }

    public StructureBoundingBox(int p_i2033_1_, int p_i2033_2_, int p_i2033_3_, int p_i2033_4_) {
        this.minX = p_i2033_1_;
        this.minZ = p_i2033_2_;
        this.maxX = p_i2033_3_;
        this.maxZ = p_i2033_4_;
        this.minY = 1;
        this.maxY = 512;
    }

    public boolean intersectsWith(StructureBoundingBox p_78884_1_) {
        return this.maxX >= p_78884_1_.minX && this.minX <= p_78884_1_.maxX && this.maxZ >= p_78884_1_.minZ && this.minZ <= p_78884_1_.maxZ && this.maxY >= p_78884_1_.minY && this.minY <= p_78884_1_.maxY;
    }

    public boolean intersectsWith(int p_78885_1_, int p_78885_2_, int p_78885_3_, int p_78885_4_) {
        return this.maxX >= p_78885_1_ && this.minX <= p_78885_3_ && this.maxZ >= p_78885_2_ && this.minZ <= p_78885_4_;
    }

    public void expandTo(StructureBoundingBox p_78888_1_) {
        this.minX = Math.min(this.minX, p_78888_1_.minX);
        this.minY = Math.min(this.minY, p_78888_1_.minY);
        this.minZ = Math.min(this.minZ, p_78888_1_.minZ);
        this.maxX = Math.max(this.maxX, p_78888_1_.maxX);
        this.maxY = Math.max(this.maxY, p_78888_1_.maxY);
        this.maxZ = Math.max(this.maxZ, p_78888_1_.maxZ);
    }

    public void offset(int p_78886_1_, int p_78886_2_, int p_78886_3_) {
        this.minX += p_78886_1_;
        this.minY += p_78886_2_;
        this.minZ += p_78886_3_;
        this.maxX += p_78886_1_;
        this.maxY += p_78886_2_;
        this.maxZ += p_78886_3_;
    }

    public boolean func_175898_b(Vec3i p_175898_1_) {
        return p_175898_1_.getX() >= this.minX && p_175898_1_.getX() <= this.maxX && p_175898_1_.getZ() >= this.minZ && p_175898_1_.getZ() <= this.maxZ && p_175898_1_.getY() >= this.minY && p_175898_1_.getY() <= this.maxY;
    }

    public Vec3i func_175896_b() {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }

    public int getXSize() {
        return this.maxX - this.minX + 1;
    }

    public int getYSize() {
        return this.maxY - this.minY + 1;
    }

    public int getZSize() {
        return this.maxZ - this.minZ + 1;
    }

    public Vec3i func_180717_f() {
        return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
    }

    public NBTTagIntArray func_151535_h() {
        return new NBTTagIntArray(new int[]{this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ});
    }

    static final class SwitchEnumFacing {
        static final int[] field_175895_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00001999";

        static {
            try {
                SwitchEnumFacing.field_175895_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_175895_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_175895_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_175895_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }

}

