/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public class MapGenRavine
extends MapGenBase {
    private float[] field_75046_d = new float[1024];
    private static final String __OBFID = "CL_00000390";

    protected void func_180707_a(long p_180707_1_, int p_180707_3_, int p_180707_4_, ChunkPrimer p_180707_5_, double p_180707_6_, double p_180707_8_, double p_180707_10_, float p_180707_12_, float p_180707_13_, float p_180707_14_, int p_180707_15_, int p_180707_16_, double p_180707_17_) {
        Random var19 = new Random(p_180707_1_);
        double var20 = p_180707_3_ * 16 + 8;
        double var22 = p_180707_4_ * 16 + 8;
        float var24 = 0.0f;
        float var25 = 0.0f;
        if (p_180707_16_ <= 0) {
            int var26 = this.range * 16 - 16;
            p_180707_16_ = var26 - var19.nextInt(var26 / 4);
        }
        boolean var52 = false;
        if (p_180707_15_ == -1) {
            p_180707_15_ = p_180707_16_ / 2;
            var52 = true;
        }
        float var27 = 1.0f;
        for (int var28 = 0; var28 < 256; ++var28) {
            if (var28 == 0 || var19.nextInt(3) == 0) {
                var27 = 1.0f + var19.nextFloat() * var19.nextFloat() * 1.0f;
            }
            this.field_75046_d[var28] = var27 * var27;
        }
        while (p_180707_15_ < p_180707_16_) {
            double var53 = 1.5 + (double)(MathHelper.sin((float)p_180707_15_ * 3.1415927f / (float)p_180707_16_) * p_180707_12_ * 1.0f);
            double var30 = var53 * p_180707_17_;
            var53 *= (double)var19.nextFloat() * 0.25 + 0.75;
            var30 *= (double)var19.nextFloat() * 0.25 + 0.75;
            float var32 = MathHelper.cos(p_180707_14_);
            float var33 = MathHelper.sin(p_180707_14_);
            p_180707_6_ += (double)(MathHelper.cos(p_180707_13_) * var32);
            p_180707_8_ += (double)var33;
            p_180707_10_ += (double)(MathHelper.sin(p_180707_13_) * var32);
            p_180707_14_ *= 0.7f;
            p_180707_14_ += var25 * 0.05f;
            p_180707_13_ += var24 * 0.05f;
            var25 *= 0.8f;
            var24 *= 0.5f;
            var25 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0f;
            var24 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0f;
            if (var52 || var19.nextInt(4) != 0) {
                double var34 = p_180707_6_ - var20;
                double var36 = p_180707_10_ - var22;
                double var38 = p_180707_16_ - p_180707_15_;
                double var40 = p_180707_12_ + 2.0f + 16.0f;
                if (var34 * var34 + var36 * var36 - var38 * var38 > var40 * var40) {
                    return;
                }
                if (p_180707_6_ >= var20 - 16.0 - var53 * 2.0 && p_180707_10_ >= var22 - 16.0 - var53 * 2.0 && p_180707_6_ <= var20 + 16.0 + var53 * 2.0 && p_180707_10_ <= var22 + 16.0 + var53 * 2.0) {
                    int var41;
                    int var54 = MathHelper.floor_double(p_180707_6_ - var53) - p_180707_3_ * 16 - 1;
                    int var35 = MathHelper.floor_double(p_180707_6_ + var53) - p_180707_3_ * 16 + 1;
                    int var55 = MathHelper.floor_double(p_180707_8_ - var30) - 1;
                    int var37 = MathHelper.floor_double(p_180707_8_ + var30) + 1;
                    int var56 = MathHelper.floor_double(p_180707_10_ - var53) - p_180707_4_ * 16 - 1;
                    int var39 = MathHelper.floor_double(p_180707_10_ + var53) - p_180707_4_ * 16 + 1;
                    if (var54 < 0) {
                        var54 = 0;
                    }
                    if (var35 > 16) {
                        var35 = 16;
                    }
                    if (var55 < 1) {
                        var55 = 1;
                    }
                    if (var37 > 248) {
                        var37 = 248;
                    }
                    if (var56 < 0) {
                        var56 = 0;
                    }
                    if (var39 > 16) {
                        var39 = 16;
                    }
                    boolean var57 = false;
                    for (var41 = var54; !var57 && var41 < var35; ++var41) {
                        for (int var42 = var56; !var57 && var42 < var39; ++var42) {
                            for (int var43 = var37 + 1; !var57 && var43 >= var55 - 1; --var43) {
                                if (var43 < 0 || var43 >= 256) continue;
                                IBlockState var44 = p_180707_5_.getBlockState(var41, var43, var42);
                                if (var44.getBlock() == Blocks.flowing_water || var44.getBlock() == Blocks.water) {
                                    var57 = true;
                                }
                                if (var43 == var55 - 1 || var41 == var54 || var41 == var35 - 1 || var42 == var56 || var42 == var39 - 1) continue;
                                var43 = var55;
                            }
                        }
                    }
                    if (!var57) {
                        for (var41 = var54; var41 < var35; ++var41) {
                            double var58 = ((double)(var41 + p_180707_3_ * 16) + 0.5 - p_180707_6_) / var53;
                            for (int var59 = var56; var59 < var39; ++var59) {
                                double var45 = ((double)(var59 + p_180707_4_ * 16) + 0.5 - p_180707_10_) / var53;
                                boolean var47 = false;
                                if (!(var58 * var58 + var45 * var45 < 1.0)) continue;
                                for (int var48 = var37; var48 > var55; --var48) {
                                    double var49 = ((double)(var48 - 1) + 0.5 - p_180707_8_) / var30;
                                    if (!((var58 * var58 + var45 * var45) * (double)this.field_75046_d[var48 - 1] + var49 * var49 / 6.0 < 1.0)) continue;
                                    IBlockState var51 = p_180707_5_.getBlockState(var41, var48, var59);
                                    if (var51.getBlock() == Blocks.grass) {
                                        var47 = true;
                                    }
                                    if (var51.getBlock() != Blocks.stone && var51.getBlock() != Blocks.dirt && var51.getBlock() != Blocks.grass) continue;
                                    if (var48 - 1 < 10) {
                                        p_180707_5_.setBlockState(var41, var48, var59, Blocks.flowing_lava.getDefaultState());
                                        continue;
                                    }
                                    p_180707_5_.setBlockState(var41, var48, var59, Blocks.air.getDefaultState());
                                    if (!var47 || p_180707_5_.getBlockState(var41, var48 - 1, var59).getBlock() != Blocks.dirt) continue;
                                    p_180707_5_.setBlockState(var41, var48 - 1, var59, this.worldObj.getBiomeGenForCoords((BlockPos)new BlockPos((int)(var41 + p_180707_3_ * 16), (int)0, (int)(var59 + p_180707_4_ * 16))).topBlock);
                                }
                            }
                        }
                        if (var52) break;
                    }
                }
            }
            ++p_180707_15_;
        }
    }

    @Override
    protected void func_180701_a(World worldIn, int p_180701_2_, int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer p_180701_6_) {
        if (this.rand.nextInt(50) == 0) {
            double var7 = p_180701_2_ * 16 + this.rand.nextInt(16);
            double var9 = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
            double var11 = p_180701_3_ * 16 + this.rand.nextInt(16);
            int var13 = 1;
            for (int var14 = 0; var14 < var13; ++var14) {
                float var15 = this.rand.nextFloat() * 3.1415927f * 2.0f;
                float var16 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float var17 = (this.rand.nextFloat() * 2.0f + this.rand.nextFloat()) * 2.0f;
                this.func_180707_a(this.rand.nextLong(), p_180701_4_, p_180701_5_, p_180701_6_, var7, var9, var11, var17, var15, var16, 0, 0, 3.0);
            }
        }
    }
}

