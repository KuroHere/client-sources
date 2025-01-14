/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class WorldGenShrub
extends WorldGenTrees {
    private final IBlockState leavesMetadata;
    private final IBlockState woodMetadata;

    public WorldGenShrub(IBlockState p_i46450_1_, IBlockState p_i46450_2_) {
        super(false);
        this.woodMetadata = p_i46450_1_;
        this.leavesMetadata = p_i46450_2_;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        IBlockState iblockstate = worldIn.getBlockState(position);
        while ((iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) && position.getY() > 0) {
            position = position.down();
            iblockstate = worldIn.getBlockState(position);
        }
        Block block = worldIn.getBlockState(position).getBlock();
        if (block == Blocks.DIRT || block == Blocks.GRASS) {
            position = position.up();
            this.setBlockAndNotifyAdequately(worldIn, position, this.woodMetadata);
            for (int i = position.getY(); i <= position.getY() + 2; ++i) {
                int j = i - position.getY();
                int k = 2 - j;
                for (int l = position.getX() - k; l <= position.getX() + k; ++l) {
                    int i1 = l - position.getX();
                    for (int j1 = position.getZ() - k; j1 <= position.getZ() + k; ++j1) {
                        BlockPos blockpos;
                        Material material;
                        int k1 = j1 - position.getZ();
                        if (Math.abs(i1) == k && Math.abs(k1) == k && rand.nextInt(2) == 0 || (material = worldIn.getBlockState(blockpos = new BlockPos(l, i, j1)).getMaterial()) != Material.AIR && material != Material.LEAVES) continue;
                        this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
                    }
                }
            }
        }
        return true;
    }
}

