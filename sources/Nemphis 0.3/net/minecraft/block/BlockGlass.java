/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumWorldBlockLayer;

public class BlockGlass
extends BlockBreakable {
    private static final String __OBFID = "CL_00000249";

    public BlockGlass(Material p_i45408_1_, boolean p_i45408_2_) {
        super(p_i45408_1_, p_i45408_2_);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
}

