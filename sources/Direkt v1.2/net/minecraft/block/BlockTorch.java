package net.minecraft.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTorch extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
		@Override
		public boolean apply(@Nullable EnumFacing p_apply_1_) {
			return p_apply_1_ != EnumFacing.DOWN;
		}
	});
	protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
	protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
	protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
	protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
	protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);

	protected BlockTorch() {
		super(Material.CIRCUITS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case EAST:
			return TORCH_EAST_AABB;

		case WEST:
			return TORCH_WEST_AABB;

		case SOUTH:
			return TORCH_SOUTH_AABB;

		case NORTH:
			return TORCH_NORTH_AABB;

		default:
			return STANDING_AABB;
		}
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

	private boolean canPlaceOn(World worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos).isFullyOpaque()) {
			return true;
		} else {
			Block block = worldIn.getBlockState(pos).getBlock();
			return (block instanceof BlockFence) || (block == Blocks.GLASS) || (block == Blocks.COBBLESTONE_WALL) || (block == Blocks.STAINED_GLASS);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (EnumFacing enumfacing : FACING.getAllowedValues()) {
			if (this.canPlaceAt(worldIn, pos, enumfacing)) { return true; }
		}

		return false;
	}

	private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
		BlockPos blockpos = pos.offset(facing.getOpposite());
		boolean flag = facing.getAxis().isHorizontal();
		return (flag && worldIn.isBlockNormalCube(blockpos, true)) || (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos));
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		if (this.canPlaceAt(worldIn, pos, facing)) {
			return this.getDefaultState().withProperty(FACING, facing);
		} else {
			for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
				if (worldIn.isBlockNormalCube(pos.offset(enumfacing.getOpposite()), true)) { return this.getDefaultState().withProperty(FACING, enumfacing); }
			}

			return this.getDefaultState();
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		this.checkForDrop(worldIn, pos, state);
	}

	@Override
	public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_) {
		this.onNeighborChangeInternal(p_189540_2_, p_189540_3_, p_189540_1_);
	}

	protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.checkForDrop(worldIn, pos, state)) {
			return true;
		} else {
			EnumFacing enumfacing = state.getValue(FACING);
			EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			boolean flag = false;

			if (enumfacing$axis.isHorizontal() && !worldIn.isBlockNormalCube(pos.offset(enumfacing1), true)) {
				flag = true;
			} else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, pos.offset(enumfacing1))) {
				flag = true;
			}

			if (flag) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
				return true;
			} else {
				return false;
			}
		}
	}

	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if ((state.getBlock() == this) && this.canPlaceAt(worldIn, pos, state.getValue(FACING))) {
			return true;
		} else {
			if (worldIn.getBlockState(pos).getBlock() == this) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		EnumFacing enumfacing = stateIn.getValue(FACING);
		double d0 = pos.getX() + 0.5D;
		double d1 = pos.getY() + 0.7D;
		double d2 = pos.getZ() + 0.5D;
		double d3 = 0.22D;
		double d4 = 0.27D;

		if (enumfacing.getAxis().isHorizontal()) {
			EnumFacing enumfacing1 = enumfacing.getOpposite();
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + (0.27D * enumfacing1.getFrontOffsetX()), d1 + 0.22D, d2 + (0.27D * enumfacing1.getFrontOffsetZ()), 0.0D, 0.0D, 0.0D, new int[0]);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + (0.27D * enumfacing1.getFrontOffsetX()), d1 + 0.22D, d2 + (0.27D * enumfacing1.getFrontOffsetZ()), 0.0D, 0.0D, 0.0D, new int[0]);
		} else {
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();

		switch (meta) {
		case 1:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
			break;

		case 2:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
			break;

		case 3:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
			break;

		case 4:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
			break;

		case 5:
		default:
			iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;

		switch (state.getValue(FACING)) {
		case EAST:
			i = i | 1;
			break;

		case WEST:
			i = i | 2;
			break;

		case SOUTH:
			i = i | 3;
			break;

		case NORTH:
			i = i | 4;
			break;

		case DOWN:
		case UP:
		default:
			i = i | 5;
		}

		return i;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}
}
