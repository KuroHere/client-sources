/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class ChunkProviderEnd
implements IChunkProvider {
    private Random endRNG;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    public NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    private World endWorld;
    private double[] densities;
    private BiomeGenBase[] biomesForGeneration;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    private static final String __OBFID = "CL_00000397";

    public ChunkProviderEnd(World worldIn, long p_i2007_2_) {
        this.endWorld = worldIn;
        this.endRNG = new Random(p_i2007_2_);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
    }

    public void func_180520_a(int p_180520_1_, int p_180520_2_, ChunkPrimer p_180520_3_) {
        int var4 = 2;
        int var5 = var4 + 1;
        int var6 = 33;
        int var7 = var4 + 1;
        this.densities = this.initializeNoiseField(this.densities, p_180520_1_ * var4, 0, p_180520_2_ * var4, var5, var6, var7);
        int var8 = 0;
        while (var8 < var4) {
            int var9 = 0;
            while (var9 < var4) {
                int var10 = 0;
                while (var10 < 32) {
                    double var11 = 0.25;
                    double var13 = this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var15 = this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 0];
                    double var17 = this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var19 = this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 0];
                    double var21 = (this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 1] - var13) * var11;
                    double var23 = (this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 1] - var15) * var11;
                    double var25 = (this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 1] - var17) * var11;
                    double var27 = (this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 1] - var19) * var11;
                    int var29 = 0;
                    while (var29 < 4) {
                        double var30 = 0.125;
                        double var32 = var13;
                        double var34 = var15;
                        double var36 = (var17 - var13) * var30;
                        double var38 = (var19 - var15) * var30;
                        int var40 = 0;
                        while (var40 < 8) {
                            double var41 = 0.125;
                            double var43 = var32;
                            double var45 = (var34 - var32) * var41;
                            int var47 = 0;
                            while (var47 < 8) {
                                IBlockState var48 = null;
                                if (var43 > 0.0) {
                                    var48 = Blocks.end_stone.getDefaultState();
                                }
                                int var49 = var40 + var8 * 8;
                                int var50 = var29 + var10 * 4;
                                int var51 = var47 + var9 * 8;
                                p_180520_3_.setBlockState(var49, var50, var51, var48);
                                var43 += var45;
                                ++var47;
                            }
                            var32 += var36;
                            var34 += var38;
                            ++var40;
                        }
                        var13 += var21;
                        var15 += var23;
                        var17 += var25;
                        var19 += var27;
                        ++var29;
                    }
                    ++var10;
                }
                ++var9;
            }
            ++var8;
        }
    }

    public void func_180519_a(ChunkPrimer p_180519_1_) {
        int var2 = 0;
        while (var2 < 16) {
            int var3 = 0;
            while (var3 < 16) {
                int var4 = 1;
                int var5 = -1;
                IBlockState var6 = Blocks.end_stone.getDefaultState();
                IBlockState var7 = Blocks.end_stone.getDefaultState();
                int var8 = 127;
                while (var8 >= 0) {
                    IBlockState var9 = p_180519_1_.getBlockState(var2, var8, var3);
                    if (var9.getBlock().getMaterial() == Material.air) {
                        var5 = -1;
                    } else if (var9.getBlock() == Blocks.stone) {
                        if (var5 == -1) {
                            if (var4 <= 0) {
                                var6 = Blocks.air.getDefaultState();
                                var7 = Blocks.end_stone.getDefaultState();
                            }
                            var5 = var4;
                            if (var8 >= 0) {
                                p_180519_1_.setBlockState(var2, var8, var3, var6);
                            } else {
                                p_180519_1_.setBlockState(var2, var8, var3, var7);
                            }
                        } else if (var5 > 0) {
                            --var5;
                            p_180519_1_.setBlockState(var2, var8, var3, var7);
                        }
                    }
                    --var8;
                }
                ++var3;
            }
            ++var2;
        }
    }

    @Override
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        this.endRNG.setSeed((long)p_73154_1_ * 341873128712L + (long)p_73154_2_ * 132897987541L);
        ChunkPrimer var3 = new ChunkPrimer();
        this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        this.func_180520_a(p_73154_1_, p_73154_2_, var3);
        this.func_180519_a(var3);
        Chunk var4 = new Chunk(this.endWorld, var3, p_73154_1_, p_73154_2_);
        byte[] var5 = var4.getBiomeArray();
        int var6 = 0;
        while (var6 < var5.length) {
            var5[var6] = (byte)this.biomesForGeneration[var6].biomeID;
            ++var6;
        }
        var4.generateSkylightMap();
        return var4;
    }

    private double[] initializeNoiseField(double[] p_73187_1_, int p_73187_2_, int p_73187_3_, int p_73187_4_, int p_73187_5_, int p_73187_6_, int p_73187_7_) {
        if (p_73187_1_ == null) {
            p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
        }
        double var8 = 684.412;
        double var10 = 684.412;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 1.121, 1.121, 0.5);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 200.0, 200.0, 0.5);
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8 / 80.0, var10 / 160.0, (var8 *= 2.0) / 80.0);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8, var10, var8);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8, var10, var8);
        int var12 = 0;
        int var13 = 0;
        while (var13 < p_73187_5_) {
            int var14 = 0;
            while (var14 < p_73187_7_) {
                float var15 = (float)(var13 + p_73187_2_) / 1.0f;
                float var16 = (float)(var14 + p_73187_4_) / 1.0f;
                float var17 = 100.0f - MathHelper.sqrt_float(var15 * var15 + var16 * var16) * 8.0f;
                if (var17 > 80.0f) {
                    var17 = 80.0f;
                }
                if (var17 < -100.0f) {
                    var17 = -100.0f;
                }
                int var18 = 0;
                while (var18 < p_73187_6_) {
                    double var28;
                    double var19 = 0.0;
                    double var21 = this.noiseData2[var12] / 512.0;
                    double var23 = this.noiseData3[var12] / 512.0;
                    double var25 = (this.noiseData1[var12] / 10.0 + 1.0) / 2.0;
                    var19 = var25 < 0.0 ? var21 : (var25 > 1.0 ? var23 : var21 + (var23 - var21) * var25);
                    var19 -= 8.0;
                    var19 += (double)var17;
                    int var27 = 2;
                    if (var18 > p_73187_6_ / 2 - var27) {
                        var28 = (float)(var18 - (p_73187_6_ / 2 - var27)) / 64.0f;
                        var28 = MathHelper.clamp_double(var28, 0.0, 1.0);
                        var19 = var19 * (1.0 - var28) + -3000.0 * var28;
                    }
                    if (var18 < (var27 = 8)) {
                        var28 = (float)(var27 - var18) / ((float)var27 - 1.0f);
                        var19 = var19 * (1.0 - var28) + -30.0 * var28;
                    }
                    p_73187_1_[var12] = var19;
                    ++var12;
                    ++var18;
                }
                ++var14;
            }
            ++var13;
        }
        return p_73187_1_;
    }

    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        BlockPos var4 = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        this.endWorld.getBiomeGenForCoords(var4.add(16, 0, 16)).func_180624_a(this.endWorld, this.endWorld.rand, var4);
        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        return false;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    @Override
    public void saveExtraData() {
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "RandomLevelSource";
    }

    @Override
    public List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
        return this.endWorld.getBiomeGenForCoords(p_177458_2_).getSpawnableList(p_177458_1_);
    }

    @Override
    public BlockPos func_180513_a(World worldIn, String p_180513_2_, BlockPos p_180513_3_) {
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
    }

    @Override
    public Chunk func_177459_a(BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}

