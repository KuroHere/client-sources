/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipesCrafting {
    private static final String __OBFID = "CL_00000095";

    public void addRecipes(CraftingManager p_77589_1_) {
        p_77589_1_.addRecipe(new ItemStack(Blocks.chest), "###", "# #", "###", Character.valueOf('#'), Blocks.planks);
        p_77589_1_.addRecipe(new ItemStack(Blocks.trapped_chest), "#-", Character.valueOf('#'), Blocks.chest, Character.valueOf('-'), Blocks.tripwire_hook);
        p_77589_1_.addRecipe(new ItemStack(Blocks.ender_chest), "###", "#E#", "###", Character.valueOf('#'), Blocks.obsidian, Character.valueOf('E'), Items.ender_eye);
        p_77589_1_.addRecipe(new ItemStack(Blocks.furnace), "###", "# #", "###", Character.valueOf('#'), Blocks.cobblestone);
        p_77589_1_.addRecipe(new ItemStack(Blocks.crafting_table), "##", "##", Character.valueOf('#'), Blocks.planks);
        p_77589_1_.addRecipe(new ItemStack(Blocks.sandstone), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sand, 1, BlockSand.EnumType.SAND.func_176688_a()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.red_sandstone), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sand, 1, BlockSand.EnumType.RED_SAND.func_176688_a()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.sandstone, 4, BlockSandStone.EnumType.SMOOTH.func_176675_a()), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.DEFAULT.func_176675_a()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.red_sandstone, 4, BlockRedSandstone.EnumType.SMOOTH.getMetaFromState()), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.DEFAULT.getMetaFromState()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.CHISELED.func_176675_a()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SAND.func_176624_a()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.CHISELED.getMetaFromState()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab2, 1, BlockStoneSlabNew.EnumType.RED_SANDSTONE.func_176915_a()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.CHISELED.getMetaFromState()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.QUARTZ.func_176624_a()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.quartz_block, 2, BlockQuartz.EnumType.LINES_Y.getMetaFromState()), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.DEFAULT.getMetaFromState()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.stonebrick, 4), "##", "##", Character.valueOf('#'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetaFromState()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CHISELED_META), "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a()));
        p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.MOSSY_META), Blocks.stonebrick, Blocks.vine);
        p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.mossy_cobblestone, 1), Blocks.cobblestone, Blocks.vine);
        p_77589_1_.addRecipe(new ItemStack(Blocks.iron_bars, 16), "###", "###", Character.valueOf('#'), Items.iron_ingot);
        p_77589_1_.addRecipe(new ItemStack(Blocks.glass_pane, 16), "###", "###", Character.valueOf('#'), Blocks.glass);
        p_77589_1_.addRecipe(new ItemStack(Blocks.redstone_lamp, 1), " R ", "RGR", " R ", Character.valueOf('R'), Items.redstone, Character.valueOf('G'), Blocks.glowstone);
        p_77589_1_.addRecipe(new ItemStack(Blocks.beacon, 1), "GGG", "GSG", "OOO", Character.valueOf('G'), Blocks.glass, Character.valueOf('S'), Items.nether_star, Character.valueOf('O'), Blocks.obsidian);
        p_77589_1_.addRecipe(new ItemStack(Blocks.nether_brick, 1), "NN", "NN", Character.valueOf('N'), Items.netherbrick);
        p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.DIORITE.getMetaFromState()), "CQ", "QC", Character.valueOf('C'), Blocks.cobblestone, Character.valueOf('Q'), Items.quartz);
        p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetaFromState()), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetaFromState()), Items.quartz);
        p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.ANDESITE.getMetaFromState()), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetaFromState()), Blocks.cobblestone);
        p_77589_1_.addRecipe(new ItemStack(Blocks.dirt, 4, BlockDirt.DirtType.COARSE_DIRT.getMetadata()), "DG", "GD", Character.valueOf('D'), new ItemStack(Blocks.dirt, 1, BlockDirt.DirtType.DIRT.getMetadata()), Character.valueOf('G'), Blocks.gravel);
        p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.DIORITE_SMOOTH.getMetaFromState()), "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetaFromState()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.GRANITE_SMOOTH.getMetaFromState()), "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetaFromState()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.ANDESITE_SMOOTH.getMetaFromState()), "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.ANDESITE.getMetaFromState()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.ROUGHMETA), "SS", "SS", Character.valueOf('S'), Items.prismarine_shard);
        p_77589_1_.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.BRICKSMETA), "SSS", "SSS", "SSS", Character.valueOf('S'), Items.prismarine_shard);
        p_77589_1_.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.DARKMETA), "SSS", "SIS", "SSS", Character.valueOf('S'), Items.prismarine_shard, Character.valueOf('I'), new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeColorDamage()));
        p_77589_1_.addRecipe(new ItemStack(Blocks.sea_lantern, 1, 0), "SCS", "CCC", "SCS", Character.valueOf('S'), Items.prismarine_shard, Character.valueOf('C'), Items.prismarine_crystals);
    }
}

