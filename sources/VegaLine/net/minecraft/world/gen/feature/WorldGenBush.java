/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBush
extends WorldGenerator {
    private final BlockBush block;

    public WorldGenBush(BlockBush blockIn) {
        this.block = blockIn;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (!worldIn.isAirBlock(blockpos) || worldIn.provider.getHasNoSky() && blockpos.getY() >= 255 || !this.block.canBlockStay(worldIn, blockpos, this.block.getDefaultState())) continue;
            worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
        }
        return true;
    }
}

