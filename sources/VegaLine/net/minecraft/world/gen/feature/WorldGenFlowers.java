/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenFlowers
extends WorldGenerator {
    private BlockFlower flower;
    private IBlockState state;

    public WorldGenFlowers(BlockFlower flowerIn, BlockFlower.EnumFlowerType type2) {
        this.setGeneratedBlock(flowerIn, type2);
    }

    public void setGeneratedBlock(BlockFlower flowerIn, BlockFlower.EnumFlowerType typeIn) {
        this.flower = flowerIn;
        this.state = flowerIn.getDefaultState().withProperty(flowerIn.getTypeProperty(), typeIn);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (!worldIn.isAirBlock(blockpos) || worldIn.provider.getHasNoSky() && blockpos.getY() >= 255 || !this.flower.canBlockStay(worldIn, blockpos, this.state)) continue;
            worldIn.setBlockState(blockpos, this.state, 2);
        }
        return true;
    }
}

