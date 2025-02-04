/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.world.gen;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCavesHell;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class ChunkProviderHell
implements IChunkProvider {
    private final World worldObj;
    private final boolean field_177466_i;
    private final Random hellRNG;
    private double[] slowsandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] netherrackExclusivityNoise = new double[256];
    private double[] noiseField;
    private final NoiseGeneratorOctaves netherNoiseGen1;
    private final NoiseGeneratorOctaves netherNoiseGen2;
    private final NoiseGeneratorOctaves netherNoiseGen3;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public final NoiseGeneratorOctaves netherNoiseGen6;
    public final NoiseGeneratorOctaves netherNoiseGen7;
    private final WorldGenFire field_177470_t = new WorldGenFire();
    private final WorldGenGlowStone1 field_177469_u = new WorldGenGlowStone1();
    private final WorldGenGlowStone2 field_177468_v = new WorldGenGlowStone2();
    private final WorldGenerator field_177467_w = new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14, BlockHelper.forBlock(Blocks.netherrack));
    private final WorldGenHellLava field_177473_x = new WorldGenHellLava(Blocks.flowing_lava, true);
    private final WorldGenHellLava field_177472_y = new WorldGenHellLava(Blocks.flowing_lava, false);
    private final GeneratorBushFeature field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
    private final GeneratorBushFeature field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
    private final MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
    private final MapGenBase netherCaveGenerator = new MapGenCavesHell();
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    private static final String __OBFID = "CL_00000392";

    public ChunkProviderHell(World worldIn, boolean p_i45637_2_, long p_i45637_3_) {
        this.worldObj = worldIn;
        this.field_177466_i = p_i45637_2_;
        this.hellRNG = new Random(p_i45637_3_);
        this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
        this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
    }

    public void func_180515_a(int p_180515_1_, int p_180515_2_, ChunkPrimer p_180515_3_) {
        int var4 = 4;
        int var5 = 32;
        int var6 = var4 + 1;
        int var7 = 17;
        int var8 = var4 + 1;
        this.noiseField = this.initializeNoiseField(this.noiseField, p_180515_1_ * var4, 0, p_180515_2_ * var4, var6, var7, var8);
        for (int var9 = 0; var9 < var4; ++var9) {
            for (int var10 = 0; var10 < var4; ++var10) {
                for (int var11 = 0; var11 < 16; ++var11) {
                    double var12 = 0.125;
                    double var14 = this.noiseField[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 0];
                    double var16 = this.noiseField[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 0];
                    double var18 = this.noiseField[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 0];
                    double var20 = this.noiseField[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 0];
                    double var22 = (this.noiseField[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 1] - var14) * var12;
                    double var24 = (this.noiseField[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 1] - var16) * var12;
                    double var26 = (this.noiseField[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 1] - var18) * var12;
                    double var28 = (this.noiseField[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 1] - var20) * var12;
                    for (int var30 = 0; var30 < 8; ++var30) {
                        double var31 = 0.25;
                        double var33 = var14;
                        double var35 = var16;
                        double var37 = (var18 - var14) * var31;
                        double var39 = (var20 - var16) * var31;
                        for (int var41 = 0; var41 < 4; ++var41) {
                            double var42 = 0.25;
                            double var44 = var33;
                            double var46 = (var35 - var33) * var42;
                            for (int var48 = 0; var48 < 4; ++var48) {
                                IBlockState var49 = null;
                                if (var11 * 8 + var30 < var5) {
                                    var49 = Blocks.lava.getDefaultState();
                                }
                                if (var44 > 0.0) {
                                    var49 = Blocks.netherrack.getDefaultState();
                                }
                                int var50 = var41 + var9 * 4;
                                int var51 = var30 + var11 * 8;
                                int var52 = var48 + var10 * 4;
                                p_180515_3_.setBlockState(var50, var51, var52, var49);
                                var44 += var46;
                            }
                            var33 += var37;
                            var35 += var39;
                        }
                        var14 += var22;
                        var16 += var24;
                        var18 += var26;
                        var20 += var28;
                    }
                }
            }
        }
    }

    public void func_180516_b(int p_180516_1_, int p_180516_2_, ChunkPrimer p_180516_3_) {
        int var4 = 64;
        double var5 = 0.03125;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, var5, var5, 1.0);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_180516_1_ * 16, 109, p_180516_2_ * 16, 16, 1, 16, var5, 1.0, var5);
        this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, var5 * 2.0, var5 * 2.0, var5 * 2.0);
        for (int var7 = 0; var7 < 16; ++var7) {
            for (int var8 = 0; var8 < 16; ++var8) {
                boolean var9 = this.slowsandNoise[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                boolean var10 = this.gravelNoise[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                int var11 = (int)(this.netherrackExclusivityNoise[var7 + var8 * 16] / 3.0 + 3.0 + this.hellRNG.nextDouble() * 0.25);
                int var12 = -1;
                IBlockState var13 = Blocks.netherrack.getDefaultState();
                IBlockState var14 = Blocks.netherrack.getDefaultState();
                for (int var15 = 127; var15 >= 0; --var15) {
                    if (var15 < 127 - this.hellRNG.nextInt(5) && var15 > this.hellRNG.nextInt(5)) {
                        IBlockState var16 = p_180516_3_.getBlockState(var8, var15, var7);
                        if (var16.getBlock() != null && var16.getBlock().getMaterial() != Material.air) {
                            if (var16.getBlock() != Blocks.netherrack) continue;
                            if (var12 == -1) {
                                if (var11 <= 0) {
                                    var13 = null;
                                    var14 = Blocks.netherrack.getDefaultState();
                                } else if (var15 >= var4 - 4 && var15 <= var4 + 1) {
                                    var13 = Blocks.netherrack.getDefaultState();
                                    var14 = Blocks.netherrack.getDefaultState();
                                    if (var10) {
                                        var13 = Blocks.gravel.getDefaultState();
                                        var14 = Blocks.netherrack.getDefaultState();
                                    }
                                    if (var9) {
                                        var13 = Blocks.soul_sand.getDefaultState();
                                        var14 = Blocks.soul_sand.getDefaultState();
                                    }
                                }
                                if (var15 < var4 && (var13 == null || var13.getBlock().getMaterial() == Material.air)) {
                                    var13 = Blocks.lava.getDefaultState();
                                }
                                var12 = var11;
                                if (var15 >= var4 - 1) {
                                    p_180516_3_.setBlockState(var8, var15, var7, var13);
                                    continue;
                                }
                                p_180516_3_.setBlockState(var8, var15, var7, var14);
                                continue;
                            }
                            if (var12 <= 0) continue;
                            --var12;
                            p_180516_3_.setBlockState(var8, var15, var7, var14);
                            continue;
                        }
                        var12 = -1;
                        continue;
                    }
                    p_180516_3_.setBlockState(var8, var15, var7, Blocks.bedrock.getDefaultState());
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        this.hellRNG.setSeed((long)p_73154_1_ * 341873128712L + (long)p_73154_2_ * 132897987541L);
        ChunkPrimer var3 = new ChunkPrimer();
        this.func_180515_a(p_73154_1_, p_73154_2_, var3);
        this.func_180516_b(p_73154_1_, p_73154_2_, var3);
        this.netherCaveGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        if (this.field_177466_i) {
            this.genNetherBridge.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        Chunk var4 = new Chunk(this.worldObj, var3, p_73154_1_, p_73154_2_);
        BiomeGenBase[] var5 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        byte[] var6 = var4.getBiomeArray();
        for (int var7 = 0; var7 < var6.length; ++var7) {
            var6[var7] = (byte)var5[var7].biomeID;
        }
        var4.resetRelightChecks();
        return var4;
    }

    private double[] initializeNoiseField(double[] p_73164_1_, int p_73164_2_, int p_73164_3_, int p_73164_4_, int p_73164_5_, int p_73164_6_, int p_73164_7_) {
        int var14;
        if (p_73164_1_ == null) {
            p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
        }
        double var8 = 684.412;
        double var10 = 2053.236;
        this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 1.0, 0.0, 1.0);
        this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 100.0, 0.0, 100.0);
        this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, var8 / 80.0, var10 / 60.0, var8 / 80.0);
        this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, var8, var10, var8);
        this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, var8, var10, var8);
        int var12 = 0;
        double[] var13 = new double[p_73164_6_];
        for (var14 = 0; var14 < p_73164_6_; ++var14) {
            var13[var14] = Math.cos((double)var14 * 3.141592653589793 * 6.0 / (double)p_73164_6_) * 2.0;
            double var15 = var14;
            if (var14 > p_73164_6_ / 2) {
                var15 = p_73164_6_ - 1 - var14;
            }
            if (!(var15 < 4.0)) continue;
            var15 = 4.0 - var15;
            double[] arrd = var13;
            int n = var14;
            arrd[n] = arrd[n] - var15 * var15 * var15 * 10.0;
        }
        for (var14 = 0; var14 < p_73164_5_; ++var14) {
            for (int var31 = 0; var31 < p_73164_7_; ++var31) {
                double var16 = 0.0;
                for (int var18 = 0; var18 < p_73164_6_; ++var18) {
                    double var29;
                    double var19 = 0.0;
                    double var21 = var13[var18];
                    double var23 = this.noiseData2[var12] / 512.0;
                    double var25 = this.noiseData3[var12] / 512.0;
                    double var27 = (this.noiseData1[var12] / 10.0 + 1.0) / 2.0;
                    var19 = var27 < 0.0 ? var23 : (var27 > 1.0 ? var25 : var23 + (var25 - var23) * var27);
                    var19 -= var21;
                    if (var18 > p_73164_6_ - 4) {
                        var29 = (float)(var18 - (p_73164_6_ - 4)) / 3.0f;
                        var19 = var19 * (1.0 - var29) + -10.0 * var29;
                    }
                    if ((double)var18 < var16) {
                        var29 = (var16 - (double)var18) / 4.0;
                        var29 = MathHelper.clamp_double(var29, 0.0, 1.0);
                        var19 = var19 * (1.0 - var29) + -10.0 * var29;
                    }
                    p_73164_1_[var12] = var19;
                    ++var12;
                }
            }
        }
        return p_73164_1_;
    }

    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        int var6;
        BlockFalling.fallInstantly = true;
        BlockPos var4 = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        ChunkCoordIntPair var5 = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        this.genNetherBridge.func_175794_a(this.worldObj, this.hellRNG, var5);
        for (var6 = 0; var6 < 8; ++var6) {
            this.field_177472_y.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (var6 = 0; var6 < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1; ++var6) {
            this.field_177470_t.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (var6 = 0; var6 < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1); ++var6) {
            this.field_177469_u.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (var6 = 0; var6 < 10; ++var6) {
            this.field_177468_v.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177471_z.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177465_A.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        for (var6 = 0; var6 < 16; ++var6) {
            this.field_177467_w.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
        }
        for (var6 = 0; var6 < 16; ++var6) {
            this.field_177473_x.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
        }
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
        return "HellRandomLevelSource";
    }

    @Override
    public List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
        if (p_177458_1_ == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.func_175795_b(p_177458_2_)) {
                return this.genNetherBridge.getSpawnList();
            }
            if (this.genNetherBridge.func_175796_a(this.worldObj, p_177458_2_) && this.worldObj.getBlockState(p_177458_2_.offsetDown()).getBlock() == Blocks.nether_brick) {
                return this.genNetherBridge.getSpawnList();
            }
        }
        BiomeGenBase var3 = this.worldObj.getBiomeGenForCoords(p_177458_2_);
        return var3.getSpawnableList(p_177458_1_);
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
        this.genNetherBridge.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
    }

    @Override
    public Chunk func_177459_a(BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}

