/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.surfacebuilders;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class NoopSurfaceBuilder
extends SurfaceBuilder<SurfaceBuilderConfig> {
    public NoopSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void buildSurface(Random random2, IChunk iChunk, Biome biome, int n, int n2, int n3, double d, BlockState blockState, BlockState blockState2, int n4, long l, SurfaceBuilderConfig surfaceBuilderConfig) {
    }

    @Override
    public void buildSurface(Random random2, IChunk iChunk, Biome biome, int n, int n2, int n3, double d, BlockState blockState, BlockState blockState2, int n4, long l, ISurfaceBuilderConfig iSurfaceBuilderConfig) {
        this.buildSurface(random2, iChunk, biome, n, n2, n3, d, blockState, blockState2, n4, l, (SurfaceBuilderConfig)iSurfaceBuilderConfig);
    }
}

