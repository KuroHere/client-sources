/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public class WorldGenMegaJungle
extends WorldGenHugeTrees {
    private static final String __OBFID = "CL_00000420";

    public WorldGenMegaJungle(boolean p_i45456_1_, int p_i45456_2_, int p_i45456_3_, int p_i45456_4_, int p_i45456_5_) {
        super(p_i45456_1_, p_i45456_2_, p_i45456_3_, p_i45456_4_, p_i45456_5_);
    }

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        int var4 = this.func_150533_a(p_180709_2_);
        if (!this.func_175929_a(worldIn, p_180709_2_, p_180709_3_, var4)) {
            return false;
        }
        this.func_175930_c(worldIn, p_180709_3_.offsetUp(var4), 2);
        int var5 = p_180709_3_.getY() + var4 - 2 - p_180709_2_.nextInt(4);
        while (var5 > p_180709_3_.getY() + var4 / 2) {
            float var6 = p_180709_2_.nextFloat() * 3.1415927f * 2.0f;
            int var7 = p_180709_3_.getX() + (int)(0.5f + MathHelper.cos(var6) * 4.0f);
            int var8 = p_180709_3_.getZ() + (int)(0.5f + MathHelper.sin(var6) * 4.0f);
            int var9 = 0;
            while (var9 < 5) {
                var7 = p_180709_3_.getX() + (int)(1.5f + MathHelper.cos(var6) * (float)var9);
                var8 = p_180709_3_.getZ() + (int)(1.5f + MathHelper.sin(var6) * (float)var9);
                this.func_175905_a(worldIn, new BlockPos(var7, var5 - 3 + var9 / 2, var8), Blocks.log, this.woodMetadata);
                ++var9;
            }
            var9 = 1 + p_180709_2_.nextInt(2);
            int var10 = var5;
            int var11 = var5 - var9;
            while (var11 <= var10) {
                int var12 = var11 - var10;
                this.func_175928_b(worldIn, new BlockPos(var7, var11, var8), 1 - var12);
                ++var11;
            }
            var5 -= 2 + p_180709_2_.nextInt(4);
        }
        int var13 = 0;
        while (var13 < var4) {
            BlockPos var14 = p_180709_3_.offsetUp(var13);
            if (this.func_175931_a(worldIn.getBlockState(var14).getBlock().getMaterial())) {
                this.func_175905_a(worldIn, var14, Blocks.log, this.woodMetadata);
                if (var13 > 0) {
                    this.func_175932_b(worldIn, p_180709_2_, var14.offsetWest(), BlockVine.field_176275_S);
                    this.func_175932_b(worldIn, p_180709_2_, var14.offsetNorth(), BlockVine.field_176272_Q);
                }
            }
            if (var13 < var4 - 1) {
                BlockPos var16;
                BlockPos var17;
                BlockPos var15 = var14.offsetEast();
                if (this.func_175931_a(worldIn.getBlockState(var15).getBlock().getMaterial())) {
                    this.func_175905_a(worldIn, var15, Blocks.log, this.woodMetadata);
                    if (var13 > 0) {
                        this.func_175932_b(worldIn, p_180709_2_, var15.offsetEast(), BlockVine.field_176271_T);
                        this.func_175932_b(worldIn, p_180709_2_, var15.offsetNorth(), BlockVine.field_176272_Q);
                    }
                }
                if (this.func_175931_a(worldIn.getBlockState(var16 = var14.offsetSouth().offsetEast()).getBlock().getMaterial())) {
                    this.func_175905_a(worldIn, var16, Blocks.log, this.woodMetadata);
                    if (var13 > 0) {
                        this.func_175932_b(worldIn, p_180709_2_, var16.offsetEast(), BlockVine.field_176271_T);
                        this.func_175932_b(worldIn, p_180709_2_, var16.offsetSouth(), BlockVine.field_176276_R);
                    }
                }
                if (this.func_175931_a(worldIn.getBlockState(var17 = var14.offsetSouth()).getBlock().getMaterial())) {
                    this.func_175905_a(worldIn, var17, Blocks.log, this.woodMetadata);
                    if (var13 > 0) {
                        this.func_175932_b(worldIn, p_180709_2_, var17.offsetWest(), BlockVine.field_176275_S);
                        this.func_175932_b(worldIn, p_180709_2_, var17.offsetSouth(), BlockVine.field_176276_R);
                    }
                }
            }
            ++var13;
        }
        return true;
    }

    private boolean func_175931_a(Material p_175931_1_) {
        if (p_175931_1_ != Material.air && p_175931_1_ != Material.leaves) {
            return false;
        }
        return true;
    }

    private void func_175932_b(World worldIn, Random p_175932_2_, BlockPos p_175932_3_, int p_175932_4_) {
        if (p_175932_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_175932_3_)) {
            this.func_175905_a(worldIn, p_175932_3_, Blocks.vine, p_175932_4_);
        }
    }

    private void func_175930_c(World worldIn, BlockPos p_175930_2_, int p_175930_3_) {
        int var4 = 2;
        int var5 = - var4;
        while (var5 <= 0) {
            this.func_175925_a(worldIn, p_175930_2_.offsetUp(var5), p_175930_3_ + 1 - var5);
            ++var5;
        }
    }
}

