/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockMelon
extends Block {
    private static final String __OBFID = "CL_00000267";

    protected BlockMelon() {
        super(Material.gourd);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.melon;
    }

    @Override
    public int quantityDropped(Random random) {
        return 3 + random.nextInt(5);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return Math.min(9, this.quantityDropped(random) + random.nextInt(1 + fortune));
    }
}

