/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockCompressedPowered
extends BlockCompressed {
    private static final String __OBFID = "CL_00000287";

    public BlockCompressedPowered(MapColor p_i45416_1_) {
        super(p_i45416_1_);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return 15;
    }
}

