/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.placement;

import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.HeightmapBasedPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class HeightmapSpreadDoublePlacement<DC extends IPlacementConfig>
extends HeightmapBasedPlacement<DC> {
    public HeightmapSpreadDoublePlacement(Codec<DC> codec) {
        super(codec);
    }

    @Override
    protected Heightmap.Type func_241858_a(DC DC) {
        return Heightmap.Type.MOTION_BLOCKING;
    }

    @Override
    public Stream<BlockPos> func_241857_a(WorldDecoratingHelper worldDecoratingHelper, Random random2, DC DC, BlockPos blockPos) {
        int n = blockPos.getX();
        int n2 = blockPos.getZ();
        int n3 = worldDecoratingHelper.func_242893_a(this.func_241858_a(DC), n, n2);
        return n3 == 0 ? Stream.of(new BlockPos[0]) : Stream.of(new BlockPos(n, random2.nextInt(n3 * 2), n2));
    }
}

