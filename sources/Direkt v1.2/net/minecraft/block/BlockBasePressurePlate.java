package net.minecraft.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePressurePlate extends Block {
	/** The bounding box for the pressure plate pressed state */
	protected static final AxisAlignedBB PRESSED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);
	protected static final AxisAlignedBB UNPRESSED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);

	/**
	 * This bounding box is used to check for entities in a certain area and then determine the pressed state.
	 */
	protected static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D);

	protected BlockBasePressurePlate(Material materialIn) {
		this(materialIn, materialIn.getMaterialMapColor());
	}

	protected BlockBasePressurePlate(Material materialIn, MapColor mapColorIn) {
		super(materialIn, mapColorIn);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setTickRandomly(true);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		boolean flag = this.getRedstoneStrength(state) > 0;
		return flag ? PRESSED_AABB : UNPRESSED_AABB;
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World worldIn) {
		return 20;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	/**
	 * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
	 */
	@Override
	public boolean canSpawnInBlock() {
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return this.canBePlacedOn(worldIn, pos.down());
	}

	@Override
	public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_) {
		if (!this.canBePlacedOn(p_189540_2_, p_189540_3_.down())) {
			this.dropBlockAsItem(p_189540_2_, p_189540_3_, p_189540_1_, 0);
			p_189540_2_.setBlockToAir(p_189540_3_);
		}
	}

	private boolean canBePlacedOn(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).isFullyOpaque() || (worldIn.getBlockState(pos).getBlock() instanceof BlockFence);
	}

	/**
	 * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
	 */
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			int i = this.getRedstoneStrength(state);

			if (i > 0) {
				this.updateState(worldIn, pos, state, i);
			}
		}
	}

	/**
	 * Called When an Entity Collided with the Block
	 */
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote) {
			int i = this.getRedstoneStrength(state);

			if (i == 0) {
				this.updateState(worldIn, pos, state, i);
			}
		}
	}

	/**
	 * Updates the pressure plate when stepped on
	 */
	protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) {
		int i = this.computeRedstoneStrength(worldIn, pos);
		boolean flag = oldRedstoneStrength > 0;
		boolean flag1 = i > 0;

		if (oldRedstoneStrength != i) {
			state = this.setRedstoneStrength(state, i);
			worldIn.setBlockState(pos, state, 2);
			this.updateNeighbors(worldIn, pos);
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
		}

		if (!flag1 && flag) {
			this.playClickOffSound(worldIn, pos);
		} else if (flag1 && !flag) {
			this.playClickOnSound(worldIn, pos);
		}

		if (flag1) {
			worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
		}
	}

	protected abstract void playClickOnSound(World worldIn, BlockPos color);

	protected abstract void playClickOffSound(World worldIn, BlockPos pos);

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (this.getRedstoneStrength(state) > 0) {
			this.updateNeighbors(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);
	}

	/**
	 * Notify block and block below of changes
	 */
	protected void updateNeighbors(World worldIn, BlockPos pos) {
		worldIn.notifyNeighborsOfStateChange(pos, this);
		worldIn.notifyNeighborsOfStateChange(pos.down(), this);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return this.getRedstoneStrength(blockState);
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP ? this.getRedstoneStrength(blockState) : 0;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	protected abstract int computeRedstoneStrength(World worldIn, BlockPos pos);

	protected abstract int getRedstoneStrength(IBlockState state);

	protected abstract IBlockState setRedstoneStrength(IBlockState state, int strength);
}
