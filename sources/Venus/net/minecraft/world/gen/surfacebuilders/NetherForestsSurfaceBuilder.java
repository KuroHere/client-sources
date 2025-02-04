/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.surfacebuilders;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class NetherForestsSurfaceBuilder
extends SurfaceBuilder<SurfaceBuilderConfig> {
    private static final BlockState field_237178_b_ = Blocks.CAVE_AIR.getDefaultState();
    protected long field_237177_a_;
    private OctavesNoiseGenerator field_237179_c_;

    public NetherForestsSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void buildSurface(Random random2, IChunk iChunk, Biome biome, int n, int n2, int n3, double d, BlockState blockState, BlockState blockState2, int n4, long l, SurfaceBuilderConfig surfaceBuilderConfig) {
        int n5 = n4;
        int n6 = n & 0xF;
        int n7 = n2 & 0xF;
        double d2 = this.field_237179_c_.func_205563_a((double)n * 0.1, n4, (double)n2 * 0.1);
        boolean bl = d2 > 0.15 + random2.nextDouble() * 0.35;
        double d3 = this.field_237179_c_.func_205563_a((double)n * 0.1, 109.0, (double)n2 * 0.1);
        boolean bl2 = d3 > 0.25 + random2.nextDouble() * 0.9;
        int n8 = (int)(d / 3.0 + 3.0 + random2.nextDouble() * 0.25);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int n9 = -1;
        BlockState blockState3 = surfaceBuilderConfig.getUnder();
        for (int i = 127; i >= 0; --i) {
            mutable.setPos(n6, i, n7);
            BlockState blockState4 = surfaceBuilderConfig.getTop();
            BlockState blockState5 = iChunk.getBlockState(mutable);
            if (blockState5.isAir()) {
                n9 = -1;
                continue;
            }
            if (!blockState5.isIn(blockState.getBlock())) continue;
            if (n9 == -1) {
                boolean bl3 = false;
                if (n8 <= 0) {
                    bl3 = true;
                    blockState3 = surfaceBuilderConfig.getUnder();
                }
                if (bl) {
                    blockState4 = surfaceBuilderConfig.getUnder();
                } else if (bl2) {
                    blockState4 = surfaceBuilderConfig.getUnderWaterMaterial();
                }
                if (i < n5 && bl3) {
                    blockState4 = blockState2;
                }
                n9 = n8;
                if (i >= n5 - 1) {
                    iChunk.setBlockState(mutable, blockState4, false);
                    continue;
                }
                iChunk.setBlockState(mutable, blockState3, false);
                continue;
            }
            if (n9 <= 0) continue;
            --n9;
            iChunk.setBlockState(mutable, blockState3, false);
        }
    }

    @Override
    public void setSeed(long l) {
        if (this.field_237177_a_ != l || this.field_237179_c_ == null) {
            this.field_237179_c_ = new OctavesNoiseGenerator(new SharedSeedRandom(l), ImmutableList.of(0));
        }
        this.field_237177_a_ = l;
    }

    @Override
    public void buildSurface(Random random2, IChunk iChunk, Biome biome, int n, int n2, int n3, double d, BlockState blockState, BlockState blockState2, int n4, long l, ISurfaceBuilderConfig iSurfaceBuilderConfig) {
        this.buildSurface(random2, iChunk, biome, n, n2, n3, d, blockState, blockState2, n4, l, (SurfaceBuilderConfig)iSurfaceBuilderConfig);
    }
}

