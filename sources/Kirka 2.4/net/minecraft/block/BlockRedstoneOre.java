/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockRedstoneOre
extends Block {
    private final boolean isOn;
    private static final String __OBFID = "CL_00000294";

    public BlockRedstoneOre(boolean p_i45420_1_) {
        super(Material.rock);
        if (p_i45420_1_) {
            this.setTickRandomly(true);
        }
        this.isOn = p_i45420_1_;
    }

    @Override
    public int tickRate(World worldIn) {
        return 30;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.setOn(worldIn, pos);
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
        this.setOn(worldIn, pos);
        super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        this.setOn(worldIn, pos);
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    private void setOn(World worldIn, BlockPos p_176352_2_) {
        this.spawnRedstoneParticles(worldIn, p_176352_2_);
        if (this == Blocks.redstone_ore) {
            worldIn.setBlockState(p_176352_2_, Blocks.lit_redstone_ore.getDefaultState());
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this == Blocks.lit_redstone_ore) {
            worldIn.setBlockState(pos, Blocks.redstone_ore.getDefaultState());
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.redstone;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return this.quantityDropped(random) + random.nextInt(fortune + 1);
    }

    @Override
    public int quantityDropped(Random random) {
        return 4 + random.nextInt(2);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (this.getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this)) {
            int var6 = 1 + worldIn.rand.nextInt(5);
            this.dropXpOnBlockBreak(worldIn, pos, var6);
        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.isOn) {
            this.spawnRedstoneParticles(worldIn, pos);
        }
    }

    private void spawnRedstoneParticles(World worldIn, BlockPos p_180691_2_) {
        Random var3 = worldIn.rand;
        double var4 = 0.0625;
        for (int var6 = 0; var6 < 6; ++var6) {
            double var7 = (float)p_180691_2_.getX() + var3.nextFloat();
            double var9 = (float)p_180691_2_.getY() + var3.nextFloat();
            double var11 = (float)p_180691_2_.getZ() + var3.nextFloat();
            if (var6 == 0 && !worldIn.getBlockState(p_180691_2_.offsetUp()).getBlock().isOpaqueCube()) {
                var9 = (double)p_180691_2_.getY() + var4 + 1.0;
            }
            if (var6 == 1 && !worldIn.getBlockState(p_180691_2_.offsetDown()).getBlock().isOpaqueCube()) {
                var9 = (double)p_180691_2_.getY() - var4;
            }
            if (var6 == 2 && !worldIn.getBlockState(p_180691_2_.offsetSouth()).getBlock().isOpaqueCube()) {
                var11 = (double)p_180691_2_.getZ() + var4 + 1.0;
            }
            if (var6 == 3 && !worldIn.getBlockState(p_180691_2_.offsetNorth()).getBlock().isOpaqueCube()) {
                var11 = (double)p_180691_2_.getZ() - var4;
            }
            if (var6 == 4 && !worldIn.getBlockState(p_180691_2_.offsetEast()).getBlock().isOpaqueCube()) {
                var7 = (double)p_180691_2_.getX() + var4 + 1.0;
            }
            if (var6 == 5 && !worldIn.getBlockState(p_180691_2_.offsetWest()).getBlock().isOpaqueCube()) {
                var7 = (double)p_180691_2_.getX() - var4;
            }
            if (!(var7 < (double)p_180691_2_.getX() || var7 > (double)(p_180691_2_.getX() + 1) || var9 < 0.0 || var9 > (double)(p_180691_2_.getY() + 1) || var11 < (double)p_180691_2_.getZ()) && !(var11 > (double)(p_180691_2_.getZ() + 1))) continue;
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var7, var9, var11, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Blocks.redstone_ore);
    }
}

