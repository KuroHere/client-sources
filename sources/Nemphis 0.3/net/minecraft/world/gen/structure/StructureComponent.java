/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public abstract class StructureComponent {
    protected StructureBoundingBox boundingBox;
    protected EnumFacing coordBaseMode;
    protected int componentType;
    private static final String __OBFID = "CL_00000511";

    public StructureComponent() {
    }

    protected StructureComponent(int p_i2091_1_) {
        this.componentType = p_i2091_1_;
    }

    public NBTTagCompound func_143010_b() {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("id", MapGenStructureIO.func_143036_a(this));
        var1.setTag("BB", this.boundingBox.func_151535_h());
        var1.setInteger("O", this.coordBaseMode == null ? -1 : this.coordBaseMode.getHorizontalIndex());
        var1.setInteger("GD", this.componentType);
        this.writeStructureToNBT(var1);
        return var1;
    }

    protected abstract void writeStructureToNBT(NBTTagCompound var1);

    public void func_143009_a(World worldIn, NBTTagCompound p_143009_2_) {
        int var3;
        if (p_143009_2_.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(p_143009_2_.getIntArray("BB"));
        }
        this.coordBaseMode = (var3 = p_143009_2_.getInteger("O")) == -1 ? null : EnumFacing.getHorizontal(var3);
        this.componentType = p_143009_2_.getInteger("GD");
        this.readStructureFromNBT(p_143009_2_);
    }

    protected abstract void readStructureFromNBT(NBTTagCompound var1);

    public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
    }

    public abstract boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3);

    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public int getComponentType() {
        return this.componentType;
    }

    public static StructureComponent findIntersecting(List p_74883_0_, StructureBoundingBox p_74883_1_) {
        StructureComponent var3;
        Iterator<E> var2 = p_74883_0_.iterator();
        do {
            if (var2.hasNext()) continue;
            return null;
        } while ((var3 = (StructureComponent)var2.next()).getBoundingBox() == null || !var3.getBoundingBox().intersectsWith(p_74883_1_));
        return var3;
    }

    public BlockPos func_180776_a() {
        return new BlockPos(this.boundingBox.func_180717_f());
    }

    protected boolean isLiquidInStructureBoundingBox(World worldIn, StructureBoundingBox p_74860_2_) {
        int var10;
        int var3 = Math.max(this.boundingBox.minX - 1, p_74860_2_.minX);
        int var4 = Math.max(this.boundingBox.minY - 1, p_74860_2_.minY);
        int var5 = Math.max(this.boundingBox.minZ - 1, p_74860_2_.minZ);
        int var6 = Math.min(this.boundingBox.maxX + 1, p_74860_2_.maxX);
        int var7 = Math.min(this.boundingBox.maxY + 1, p_74860_2_.maxY);
        int var8 = Math.min(this.boundingBox.maxZ + 1, p_74860_2_.maxZ);
        int var9 = var3;
        while (var9 <= var6) {
            var10 = var5;
            while (var10 <= var8) {
                if (worldIn.getBlockState(new BlockPos(var9, var4, var10)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(new BlockPos(var9, var7, var10)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                ++var10;
            }
            ++var9;
        }
        var9 = var3;
        while (var9 <= var6) {
            var10 = var4;
            while (var10 <= var7) {
                if (worldIn.getBlockState(new BlockPos(var9, var10, var5)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(new BlockPos(var9, var10, var8)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                ++var10;
            }
            ++var9;
        }
        var9 = var5;
        while (var9 <= var8) {
            var10 = var4;
            while (var10 <= var7) {
                if (worldIn.getBlockState(new BlockPos(var3, var10, var9)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(new BlockPos(var6, var10, var9)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                ++var10;
            }
            ++var9;
        }
        return false;
    }

    protected int getXWithOffset(int p_74865_1_, int p_74865_2_) {
        if (this.coordBaseMode == null) {
            return p_74865_1_;
        }
        switch (SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()]) {
            case 1: 
            case 2: {
                return this.boundingBox.minX + p_74865_1_;
            }
            case 3: {
                return this.boundingBox.maxX - p_74865_2_;
            }
            case 4: {
                return this.boundingBox.minX + p_74865_2_;
            }
        }
        return p_74865_1_;
    }

    protected int getYWithOffset(int p_74862_1_) {
        return this.coordBaseMode == null ? p_74862_1_ : p_74862_1_ + this.boundingBox.minY;
    }

    protected int getZWithOffset(int p_74873_1_, int p_74873_2_) {
        if (this.coordBaseMode == null) {
            return p_74873_2_;
        }
        switch (SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()]) {
            case 1: {
                return this.boundingBox.maxZ - p_74873_2_;
            }
            case 2: {
                return this.boundingBox.minZ + p_74873_2_;
            }
            case 3: 
            case 4: {
                return this.boundingBox.minZ + p_74873_1_;
            }
        }
        return p_74873_2_;
    }

    protected int getMetadataWithOffset(Block p_151555_1_, int p_151555_2_) {
        if (p_151555_1_ == Blocks.rail) {
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST) {
                if (p_151555_2_ == 1) {
                    return 0;
                }
                return 1;
            }
        } else if (p_151555_1_ instanceof BlockDoor) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
                if (p_151555_2_ == 0) {
                    return 2;
                }
                if (p_151555_2_ == 2) {
                    return 0;
                }
            } else {
                if (this.coordBaseMode == EnumFacing.WEST) {
                    return p_151555_2_ + 1 & 3;
                }
                if (this.coordBaseMode == EnumFacing.EAST) {
                    return p_151555_2_ + 3 & 3;
                }
            }
        } else if (p_151555_1_ != Blocks.stone_stairs && p_151555_1_ != Blocks.oak_stairs && p_151555_1_ != Blocks.nether_brick_stairs && p_151555_1_ != Blocks.stone_brick_stairs && p_151555_1_ != Blocks.sandstone_stairs) {
            if (p_151555_1_ == Blocks.ladder) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
            } else if (p_151555_1_ == Blocks.stone_button) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (p_151555_2_ == 3) {
                        return 4;
                    }
                    if (p_151555_2_ == 4) {
                        return 3;
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (p_151555_2_ == 3) {
                        return 1;
                    }
                    if (p_151555_2_ == 4) {
                        return 2;
                    }
                    if (p_151555_2_ == 2) {
                        return 3;
                    }
                    if (p_151555_2_ == 1) {
                        return 4;
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (p_151555_2_ == 3) {
                        return 2;
                    }
                    if (p_151555_2_ == 4) {
                        return 1;
                    }
                    if (p_151555_2_ == 2) {
                        return 3;
                    }
                    if (p_151555_2_ == 1) {
                        return 4;
                    }
                }
            } else if (p_151555_1_ != Blocks.tripwire_hook && !(p_151555_1_ instanceof BlockDirectional)) {
                if (p_151555_1_ == Blocks.piston || p_151555_1_ == Blocks.sticky_piston || p_151555_1_ == Blocks.lever || p_151555_1_ == Blocks.dispenser) {
                    if (this.coordBaseMode == EnumFacing.SOUTH) {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex() || p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.getFront(p_151555_2_).getOpposite().getIndex();
                        }
                    } else if (this.coordBaseMode == EnumFacing.WEST) {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    } else if (this.coordBaseMode == EnumFacing.EAST) {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (p_151555_2_ == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                }
            } else {
                EnumFacing var3 = EnumFacing.getHorizontal(p_151555_2_);
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (var3 == EnumFacing.SOUTH || var3 == EnumFacing.NORTH) {
                        return var3.getOpposite().getHorizontalIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (var3 == EnumFacing.NORTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.SOUTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                } else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (var3 == EnumFacing.NORTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.SOUTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (var3 == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
            }
        } else if (this.coordBaseMode == EnumFacing.SOUTH) {
            if (p_151555_2_ == 2) {
                return 3;
            }
            if (p_151555_2_ == 3) {
                return 2;
            }
        } else if (this.coordBaseMode == EnumFacing.WEST) {
            if (p_151555_2_ == 0) {
                return 2;
            }
            if (p_151555_2_ == 1) {
                return 3;
            }
            if (p_151555_2_ == 2) {
                return 0;
            }
            if (p_151555_2_ == 3) {
                return 1;
            }
        } else if (this.coordBaseMode == EnumFacing.EAST) {
            if (p_151555_2_ == 0) {
                return 2;
            }
            if (p_151555_2_ == 1) {
                return 3;
            }
            if (p_151555_2_ == 2) {
                return 1;
            }
            if (p_151555_2_ == 3) {
                return 0;
            }
        }
        return p_151555_2_;
    }

    protected void func_175811_a(World worldIn, IBlockState p_175811_2_, int p_175811_3_, int p_175811_4_, int p_175811_5_, StructureBoundingBox p_175811_6_) {
        BlockPos var7 = new BlockPos(this.getXWithOffset(p_175811_3_, p_175811_5_), this.getYWithOffset(p_175811_4_), this.getZWithOffset(p_175811_3_, p_175811_5_));
        if (p_175811_6_.func_175898_b(var7)) {
            worldIn.setBlockState(var7, p_175811_2_, 2);
        }
    }

    protected IBlockState func_175807_a(World worldIn, int p_175807_2_, int p_175807_3_, int p_175807_4_, StructureBoundingBox p_175807_5_) {
        int var7;
        int var8;
        int var6 = this.getXWithOffset(p_175807_2_, p_175807_4_);
        return !p_175807_5_.func_175898_b(new BlockPos(var6, var7 = this.getYWithOffset(p_175807_3_), var8 = this.getZWithOffset(p_175807_2_, p_175807_4_))) ? Blocks.air.getDefaultState() : worldIn.getBlockState(new BlockPos(var6, var7, var8));
    }

    protected void fillWithAir(World worldIn, StructureBoundingBox p_74878_2_, int p_74878_3_, int p_74878_4_, int p_74878_5_, int p_74878_6_, int p_74878_7_, int p_74878_8_) {
        int var9 = p_74878_4_;
        while (var9 <= p_74878_7_) {
            int var10 = p_74878_3_;
            while (var10 <= p_74878_6_) {
                int var11 = p_74878_5_;
                while (var11 <= p_74878_8_) {
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), var10, var9, var11, p_74878_2_);
                    ++var11;
                }
                ++var10;
            }
            ++var9;
        }
    }

    protected void func_175804_a(World worldIn, StructureBoundingBox p_175804_2_, int p_175804_3_, int p_175804_4_, int p_175804_5_, int p_175804_6_, int p_175804_7_, int p_175804_8_, IBlockState p_175804_9_, IBlockState p_175804_10_, boolean p_175804_11_) {
        int var12 = p_175804_4_;
        while (var12 <= p_175804_7_) {
            int var13 = p_175804_3_;
            while (var13 <= p_175804_6_) {
                int var14 = p_175804_5_;
                while (var14 <= p_175804_8_) {
                    if (!p_175804_11_ || this.func_175807_a(worldIn, var13, var12, var14, p_175804_2_).getBlock().getMaterial() != Material.air) {
                        if (var12 != p_175804_4_ && var12 != p_175804_7_ && var13 != p_175804_3_ && var13 != p_175804_6_ && var14 != p_175804_5_ && var14 != p_175804_8_) {
                            this.func_175811_a(worldIn, p_175804_10_, var13, var12, var14, p_175804_2_);
                        } else {
                            this.func_175811_a(worldIn, p_175804_9_, var13, var12, var14, p_175804_2_);
                        }
                    }
                    ++var14;
                }
                ++var13;
            }
            ++var12;
        }
    }

    protected void fillWithRandomizedBlocks(World worldIn, StructureBoundingBox p_74882_2_, int p_74882_3_, int p_74882_4_, int p_74882_5_, int p_74882_6_, int p_74882_7_, int p_74882_8_, boolean p_74882_9_, Random p_74882_10_, BlockSelector p_74882_11_) {
        int var12 = p_74882_4_;
        while (var12 <= p_74882_7_) {
            int var13 = p_74882_3_;
            while (var13 <= p_74882_6_) {
                int var14 = p_74882_5_;
                while (var14 <= p_74882_8_) {
                    if (!p_74882_9_ || this.func_175807_a(worldIn, var13, var12, var14, p_74882_2_).getBlock().getMaterial() != Material.air) {
                        p_74882_11_.selectBlocks(p_74882_10_, var13, var12, var14, var12 == p_74882_4_ || var12 == p_74882_7_ || var13 == p_74882_3_ || var13 == p_74882_6_ || var14 == p_74882_5_ || var14 == p_74882_8_);
                        this.func_175811_a(worldIn, p_74882_11_.func_180780_a(), var13, var12, var14, p_74882_2_);
                    }
                    ++var14;
                }
                ++var13;
            }
            ++var12;
        }
    }

    protected void func_175805_a(World worldIn, StructureBoundingBox p_175805_2_, Random p_175805_3_, float p_175805_4_, int p_175805_5_, int p_175805_6_, int p_175805_7_, int p_175805_8_, int p_175805_9_, int p_175805_10_, IBlockState p_175805_11_, IBlockState p_175805_12_, boolean p_175805_13_) {
        int var14 = p_175805_6_;
        while (var14 <= p_175805_9_) {
            int var15 = p_175805_5_;
            while (var15 <= p_175805_8_) {
                int var16 = p_175805_7_;
                while (var16 <= p_175805_10_) {
                    if (!(p_175805_3_.nextFloat() > p_175805_4_ || p_175805_13_ && this.func_175807_a(worldIn, var15, var14, var16, p_175805_2_).getBlock().getMaterial() == Material.air)) {
                        if (var14 != p_175805_6_ && var14 != p_175805_9_ && var15 != p_175805_5_ && var15 != p_175805_8_ && var16 != p_175805_7_ && var16 != p_175805_10_) {
                            this.func_175811_a(worldIn, p_175805_12_, var15, var14, var16, p_175805_2_);
                        } else {
                            this.func_175811_a(worldIn, p_175805_11_, var15, var14, var16, p_175805_2_);
                        }
                    }
                    ++var16;
                }
                ++var15;
            }
            ++var14;
        }
    }

    protected void func_175809_a(World worldIn, StructureBoundingBox p_175809_2_, Random p_175809_3_, float p_175809_4_, int p_175809_5_, int p_175809_6_, int p_175809_7_, IBlockState p_175809_8_) {
        if (p_175809_3_.nextFloat() < p_175809_4_) {
            this.func_175811_a(worldIn, p_175809_8_, p_175809_5_, p_175809_6_, p_175809_7_, p_175809_2_);
        }
    }

    protected void func_180777_a(World worldIn, StructureBoundingBox p_180777_2_, int p_180777_3_, int p_180777_4_, int p_180777_5_, int p_180777_6_, int p_180777_7_, int p_180777_8_, IBlockState p_180777_9_, boolean p_180777_10_) {
        float var11 = p_180777_6_ - p_180777_3_ + 1;
        float var12 = p_180777_7_ - p_180777_4_ + 1;
        float var13 = p_180777_8_ - p_180777_5_ + 1;
        float var14 = (float)p_180777_3_ + var11 / 2.0f;
        float var15 = (float)p_180777_5_ + var13 / 2.0f;
        int var16 = p_180777_4_;
        while (var16 <= p_180777_7_) {
            float var17 = (float)(var16 - p_180777_4_) / var12;
            int var18 = p_180777_3_;
            while (var18 <= p_180777_6_) {
                float var19 = ((float)var18 - var14) / (var11 * 0.5f);
                int var20 = p_180777_5_;
                while (var20 <= p_180777_8_) {
                    float var22;
                    float var21 = ((float)var20 - var15) / (var13 * 0.5f);
                    if (!(p_180777_10_ && this.func_175807_a(worldIn, var18, var16, var20, p_180777_2_).getBlock().getMaterial() == Material.air || (var22 = var19 * var19 + var17 * var17 + var21 * var21) > 1.05f)) {
                        this.func_175811_a(worldIn, p_180777_9_, var18, var16, var20, p_180777_2_);
                    }
                    ++var20;
                }
                ++var18;
            }
            ++var16;
        }
    }

    protected void clearCurrentPositionBlocksUpwards(World worldIn, int p_74871_2_, int p_74871_3_, int p_74871_4_, StructureBoundingBox p_74871_5_) {
        BlockPos var6 = new BlockPos(this.getXWithOffset(p_74871_2_, p_74871_4_), this.getYWithOffset(p_74871_3_), this.getZWithOffset(p_74871_2_, p_74871_4_));
        if (p_74871_5_.func_175898_b(var6)) {
            while (!worldIn.isAirBlock(var6) && var6.getY() < 255) {
                worldIn.setBlockState(var6, Blocks.air.getDefaultState(), 2);
                var6 = var6.offsetUp();
            }
        }
    }

    protected void func_175808_b(World worldIn, IBlockState p_175808_2_, int p_175808_3_, int p_175808_4_, int p_175808_5_, StructureBoundingBox p_175808_6_) {
        int var9;
        int var8;
        int var7 = this.getXWithOffset(p_175808_3_, p_175808_5_);
        if (p_175808_6_.func_175898_b(new BlockPos(var7, var8 = this.getYWithOffset(p_175808_4_), var9 = this.getZWithOffset(p_175808_3_, p_175808_5_)))) {
            while ((worldIn.isAirBlock(new BlockPos(var7, var8, var9)) || worldIn.getBlockState(new BlockPos(var7, var8, var9)).getBlock().getMaterial().isLiquid()) && var8 > 1) {
                worldIn.setBlockState(new BlockPos(var7, var8, var9), p_175808_2_, 2);
                --var8;
            }
        }
    }

    protected boolean func_180778_a(World worldIn, StructureBoundingBox p_180778_2_, Random p_180778_3_, int p_180778_4_, int p_180778_5_, int p_180778_6_, List p_180778_7_, int p_180778_8_) {
        BlockPos var9 = new BlockPos(this.getXWithOffset(p_180778_4_, p_180778_6_), this.getYWithOffset(p_180778_5_), this.getZWithOffset(p_180778_4_, p_180778_6_));
        if (p_180778_2_.func_175898_b(var9) && worldIn.getBlockState(var9).getBlock() != Blocks.chest) {
            IBlockState var10 = Blocks.chest.getDefaultState();
            worldIn.setBlockState(var9, Blocks.chest.func_176458_f(worldIn, var9, var10), 2);
            TileEntity var11 = worldIn.getTileEntity(var9);
            if (var11 instanceof TileEntityChest) {
                WeightedRandomChestContent.generateChestContents(p_180778_3_, p_180778_7_, (TileEntityChest)var11, p_180778_8_);
            }
            return true;
        }
        return false;
    }

    protected boolean func_175806_a(World worldIn, StructureBoundingBox p_175806_2_, Random p_175806_3_, int p_175806_4_, int p_175806_5_, int p_175806_6_, int p_175806_7_, List p_175806_8_, int p_175806_9_) {
        BlockPos var10 = new BlockPos(this.getXWithOffset(p_175806_4_, p_175806_6_), this.getYWithOffset(p_175806_5_), this.getZWithOffset(p_175806_4_, p_175806_6_));
        if (p_175806_2_.func_175898_b(var10) && worldIn.getBlockState(var10).getBlock() != Blocks.dispenser) {
            worldIn.setBlockState(var10, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, p_175806_7_)), 2);
            TileEntity var11 = worldIn.getTileEntity(var10);
            if (var11 instanceof TileEntityDispenser) {
                WeightedRandomChestContent.func_177631_a(p_175806_3_, p_175806_8_, (TileEntityDispenser)var11, p_175806_9_);
            }
            return true;
        }
        return false;
    }

    protected void func_175810_a(World worldIn, StructureBoundingBox p_175810_2_, Random p_175810_3_, int p_175810_4_, int p_175810_5_, int p_175810_6_, EnumFacing p_175810_7_) {
        BlockPos var8 = new BlockPos(this.getXWithOffset(p_175810_4_, p_175810_6_), this.getYWithOffset(p_175810_5_), this.getZWithOffset(p_175810_4_, p_175810_6_));
        if (p_175810_2_.func_175898_b(var8)) {
            ItemDoor.func_179235_a(worldIn, var8, p_175810_7_.rotateYCCW(), Blocks.oak_door);
        }
    }

    public static abstract class BlockSelector {
        protected IBlockState field_151562_a = Blocks.air.getDefaultState();
        private static final String __OBFID = "CL_00000512";

        protected BlockSelector() {
        }

        public abstract void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5);

        public IBlockState func_180780_a() {
            return this.field_151562_a;
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_176100_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00001969";

        static {
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var0) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var0_1) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var0_2) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176100_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var0_3) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }

}

