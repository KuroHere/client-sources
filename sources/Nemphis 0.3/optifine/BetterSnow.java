/*
 * Decompiled with CFR 0_118.
 */
package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import optifine.Config;

public class BetterSnow {
    private static IBakedModel modelSnowLayer = null;

    public static void update() {
        modelSnowLayer = Config.getMinecraft().getBlockRendererDispatcher().func_175023_a().func_178125_b(Blocks.snow_layer.getDefaultState());
    }

    public static IBakedModel getModelSnowLayer() {
        return modelSnowLayer;
    }

    public static IBlockState getStateSnowLayer() {
        return Blocks.snow_layer.getDefaultState();
    }

    public static boolean shouldRender(IBlockAccess blockAccess, Block block, IBlockState blockState, BlockPos blockPos) {
        return !BetterSnow.checkBlock(block, blockState) ? false : BetterSnow.hasSnowNeighbours(blockAccess, blockPos);
    }

    private static boolean hasSnowNeighbours(IBlockAccess blockAccess, BlockPos pos) {
        Block blockSnow = Blocks.snow_layer;
        return blockAccess.getBlockState(pos.offsetNorth()).getBlock() != blockSnow && blockAccess.getBlockState(pos.offsetSouth()).getBlock() != blockSnow && blockAccess.getBlockState(pos.offsetWest()).getBlock() != blockSnow && blockAccess.getBlockState(pos.offsetEast()).getBlock() != blockSnow ? false : blockAccess.getBlockState(pos.offsetDown()).getBlock().isOpaqueCube();
    }

    private static boolean checkBlock(Block block, IBlockState blockState) {
        if (block.isFullCube()) {
            return false;
        }
        if (block.isOpaqueCube()) {
            return false;
        }
        if (block instanceof BlockSnow) {
            return false;
        }
        if (block instanceof BlockBush && (block instanceof BlockDoublePlant || block instanceof BlockFlower || block instanceof BlockMushroom || block instanceof BlockSapling || block instanceof BlockTallGrass)) {
            return true;
        }
        if (!(block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockFlowerPot || block instanceof BlockPane || block instanceof BlockReed || block instanceof BlockWall)) {
            Comparable orient;
            if (block instanceof BlockRedstoneTorch && blockState.getValue(BlockTorch.FACING_PROP) == EnumFacing.UP) {
                return true;
            }
            if (block instanceof BlockLever && ((orient = blockState.getValue(BlockLever.FACING)) == BlockLever.EnumOrientation.UP_X || orient == BlockLever.EnumOrientation.UP_Z)) {
                return true;
            }
            return false;
        }
        return true;
    }
}

