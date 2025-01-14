/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class BiomeGenMesa
extends BiomeGenBase {
    private IBlockState[] field_150621_aC;
    private long field_150622_aD;
    private NoiseGeneratorPerlin field_150623_aE;
    private NoiseGeneratorPerlin field_150624_aF;
    private NoiseGeneratorPerlin field_150625_aG;
    private boolean field_150626_aH;
    private boolean field_150620_aI;
    private static final String __OBFID = "CL_00000176";

    public BiomeGenMesa(int p_i45380_1_, boolean p_i45380_2_, boolean p_i45380_3_) {
        super(p_i45380_1_);
        this.field_150626_aH = p_i45380_2_;
        this.field_150620_aI = p_i45380_3_;
        this.setDisableRain();
        this.setTemperatureRainfall(2.0f, 0.0f);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT_PROP, (Comparable)((Object)BlockSand.EnumType.RED_SAND));
        this.fillerBlock = Blocks.stained_hardened_clay.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 20;
        this.theBiomeDecorator.reedsPerChunk = 3;
        this.theBiomeDecorator.cactiPerChunk = 5;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        if (p_i45380_3_) {
            this.theBiomeDecorator.treesPerChunk = 5;
        }
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random p_150567_1_) {
        return this.worldGeneratorTrees;
    }

    @Override
    public int func_180625_c(BlockPos p_180625_1_) {
        return 10387789;
    }

    @Override
    public int func_180627_b(BlockPos p_180627_1_) {
        return 9470285;
    }

    @Override
    public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_) {
        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random p_180622_2_, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_) {
        int var10;
        int var11;
        if (this.field_150621_aC == null || this.field_150622_aD != worldIn.getSeed()) {
            this.func_150619_a(worldIn.getSeed());
        }
        if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != worldIn.getSeed()) {
            Random var8 = new Random(this.field_150622_aD);
            this.field_150623_aE = new NoiseGeneratorPerlin(var8, 4);
            this.field_150624_aF = new NoiseGeneratorPerlin(var8, 1);
        }
        this.field_150622_aD = worldIn.getSeed();
        double var22 = 0.0;
        if (this.field_150626_aH) {
            var10 = (p_180622_4_ & -16) + (p_180622_5_ & 15);
            var11 = (p_180622_5_ & -16) + (p_180622_4_ & 15);
            double var12 = Math.min(Math.abs(p_180622_6_), this.field_150623_aE.func_151601_a((double)var10 * 0.25, (double)var11 * 0.25));
            if (var12 > 0.0) {
                var22 = var12 * var12 * 2.5;
                double var14 = 0.001953125;
                double var16 = Math.abs(this.field_150624_aF.func_151601_a((double)var10 * var14, (double)var11 * var14));
                double var18 = Math.ceil(var16 * 50.0) + 14.0;
                if (var22 > var18) {
                    var22 = var18;
                }
                var22 += 64.0;
            }
        }
        var10 = p_180622_4_ & 15;
        var11 = p_180622_5_ & 15;
        boolean var23 = true;
        IBlockState var13 = Blocks.stained_hardened_clay.getDefaultState();
        IBlockState var24 = this.fillerBlock;
        int var15 = (int)(p_180622_6_ / 3.0 + 3.0 + p_180622_2_.nextDouble() * 0.25);
        boolean var25 = Math.cos(p_180622_6_ / 3.0 * 3.141592653589793) > 0.0;
        int var17 = -1;
        boolean var26 = false;
        for (int var19 = 255; var19 >= 0; --var19) {
            IBlockState var21;
            if (p_180622_3_.getBlockState(var11, var19, var10).getBlock().getMaterial() == Material.air && var19 < (int)var22) {
                p_180622_3_.setBlockState(var11, var19, var10, Blocks.stone.getDefaultState());
            }
            if (var19 <= p_180622_2_.nextInt(5)) {
                p_180622_3_.setBlockState(var11, var19, var10, Blocks.bedrock.getDefaultState());
                continue;
            }
            IBlockState var20 = p_180622_3_.getBlockState(var11, var19, var10);
            if (var20.getBlock().getMaterial() == Material.air) {
                var17 = -1;
                continue;
            }
            if (var20.getBlock() != Blocks.stone) continue;
            if (var17 == -1) {
                var26 = false;
                if (var15 <= 0) {
                    var13 = null;
                    var24 = Blocks.stone.getDefaultState();
                } else if (var19 >= 59 && var19 <= 64) {
                    var13 = Blocks.stained_hardened_clay.getDefaultState();
                    var24 = this.fillerBlock;
                }
                if (var19 < 63 && (var13 == null || var13.getBlock().getMaterial() == Material.air)) {
                    var13 = Blocks.water.getDefaultState();
                }
                var17 = var15 + Math.max(0, var19 - 63);
                if (var19 >= 62) {
                    if (this.field_150620_aI && var19 > 86 + var15 * 2) {
                        if (var25) {
                            p_180622_3_.setBlockState(var11, var19, var10, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, (Comparable)((Object)BlockDirt.DirtType.COARSE_DIRT)));
                            continue;
                        }
                        p_180622_3_.setBlockState(var11, var19, var10, Blocks.grass.getDefaultState());
                        continue;
                    }
                    if (var19 > 66 + var15) {
                        var21 = var19 >= 64 && var19 <= 127 ? (var25 ? Blocks.hardened_clay.getDefaultState() : this.func_180629_a(p_180622_4_, var19, p_180622_5_)) : Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.ORANGE));
                        p_180622_3_.setBlockState(var11, var19, var10, var21);
                        continue;
                    }
                    p_180622_3_.setBlockState(var11, var19, var10, this.topBlock);
                    var26 = true;
                    continue;
                }
                p_180622_3_.setBlockState(var11, var19, var10, var24);
                if (var24.getBlock() != Blocks.stained_hardened_clay) continue;
                p_180622_3_.setBlockState(var11, var19, var10, var24.getBlock().getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.ORANGE)));
                continue;
            }
            if (var17 <= 0) continue;
            --var17;
            if (var26) {
                p_180622_3_.setBlockState(var11, var19, var10, Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.ORANGE)));
                continue;
            }
            var21 = this.func_180629_a(p_180622_4_, var19, p_180622_5_);
            p_180622_3_.setBlockState(var11, var19, var10, var21);
        }
    }

    private void func_150619_a(long p_150619_1_) {
        int var7;
        int var9;
        int var5;
        int var8;
        int var4;
        int var6;
        this.field_150621_aC = new IBlockState[64];
        Arrays.fill(this.field_150621_aC, Blocks.hardened_clay.getDefaultState());
        Random var3 = new Random(p_150619_1_);
        this.field_150625_aG = new NoiseGeneratorPerlin(var3, 1);
        for (var4 = 0; var4 < 64; ++var4) {
            if ((var4 += var3.nextInt(5) + 1) >= 64) continue;
            this.field_150621_aC[var4] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.ORANGE));
        }
        var4 = var3.nextInt(4) + 2;
        for (var5 = 0; var5 < var4; ++var5) {
            var6 = var3.nextInt(3) + 1;
            var7 = var3.nextInt(64);
            for (var8 = 0; var7 + var8 < 64 && var8 < var6; ++var8) {
                this.field_150621_aC[var7 + var8] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.YELLOW));
            }
        }
        var5 = var3.nextInt(4) + 2;
        for (var6 = 0; var6 < var5; ++var6) {
            var7 = var3.nextInt(3) + 2;
            var8 = var3.nextInt(64);
            for (var9 = 0; var8 + var9 < 64 && var9 < var7; ++var9) {
                this.field_150621_aC[var8 + var9] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.BROWN));
            }
        }
        var6 = var3.nextInt(4) + 2;
        for (var7 = 0; var7 < var6; ++var7) {
            var8 = var3.nextInt(3) + 1;
            var9 = var3.nextInt(64);
            for (int var10 = 0; var9 + var10 < 64 && var10 < var8; ++var10) {
                this.field_150621_aC[var9 + var10] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.RED));
            }
        }
        var7 = var3.nextInt(3) + 3;
        var8 = 0;
        for (var9 = 0; var9 < var7; ++var9) {
            int var12 = 1;
            for (int var11 = 0; (var8 += var3.nextInt(16) + 4) + var11 < 64 && var11 < var12; ++var11) {
                this.field_150621_aC[var8 + var11] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.WHITE));
                if (var8 + var11 > 1 && var3.nextBoolean()) {
                    this.field_150621_aC[var8 + var11 - 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.SILVER));
                }
                if (var8 + var11 >= 63 || !var3.nextBoolean()) continue;
                this.field_150621_aC[var8 + var11 + 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, (Comparable)((Object)EnumDyeColor.SILVER));
            }
        }
    }

    private IBlockState func_180629_a(int p_180629_1_, int p_180629_2_, int p_180629_3_) {
        int var4 = (int)Math.round(this.field_150625_aG.func_151601_a((double)p_180629_1_ * 1.0 / 512.0, (double)p_180629_1_ * 1.0 / 512.0) * 2.0);
        return this.field_150621_aC[(p_180629_2_ + var4 + 64) % 64];
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
        boolean var2 = this.biomeID == BiomeGenBase.mesa.biomeID;
        BiomeGenMesa var3 = new BiomeGenMesa(p_180277_1_, var2, this.field_150620_aI);
        if (!var2) {
            var3.setHeight(height_LowHills);
            var3.setBiomeName(String.valueOf(this.biomeName) + " M");
        } else {
            var3.setBiomeName(String.valueOf(this.biomeName) + " (Bryce)");
        }
        var3.func_150557_a(this.color, true);
        return var3;
    }
}

