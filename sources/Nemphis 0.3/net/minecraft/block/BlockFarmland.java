/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class BlockFarmland
extends Block {
    public static final PropertyInteger field_176531_a = PropertyInteger.create("moisture", 0, 7);
    private static final String __OBFID = "CL_00000241";

    protected BlockFarmland() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176531_a, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
        this.setLightOpacity(255);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var5 = (Integer)state.getValue(field_176531_a);
        if (!this.func_176530_e(worldIn, pos) && !worldIn.func_175727_C(pos.offsetUp())) {
            if (var5 > 0) {
                worldIn.setBlockState(pos, state.withProperty(field_176531_a, Integer.valueOf(var5 - 1)), 2);
            } else if (!this.func_176529_d(worldIn, pos)) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
        } else if (var5 < 7) {
            worldIn.setBlockState(pos, state.withProperty(field_176531_a, Integer.valueOf(7)), 2);
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn instanceof EntityLivingBase) {
            if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5f) {
                if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    return;
                }
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
    }

    private boolean func_176529_d(World worldIn, BlockPos p_176529_2_) {
        Block var3 = worldIn.getBlockState(p_176529_2_.offsetUp()).getBlock();
        if (!(var3 instanceof BlockCrops) && !(var3 instanceof BlockStem)) {
            return false;
        }
        return true;
    }

    private boolean func_176530_e(World worldIn, BlockPos p_176530_2_) {
        BlockPos.MutableBlockPos var4;
        Iterator var3 = BlockPos.getAllInBoxMutable(p_176530_2_.add(-4, 0, -4), p_176530_2_.add(4, 1, 4)).iterator();
        do {
            if (var3.hasNext()) continue;
            return false;
        } while (worldIn.getBlockState(var4 = (BlockPos.MutableBlockPos)var3.next()).getBlock().getMaterial() != Material.water);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        if (worldIn.getBlockState(pos.offsetUp()).getBlock().getMaterial().isSolid()) {
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, (Comparable)((Object)BlockDirt.DirtType.DIRT)), rand, fortune);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.dirt);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176531_a, Integer.valueOf(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(field_176531_a);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176531_a);
    }
}

