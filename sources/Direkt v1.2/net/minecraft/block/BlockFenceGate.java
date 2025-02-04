package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockHorizontal {
	public static final PropertyBool OPEN = PropertyBool.create("open");
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
	protected static final AxisAlignedBB AABB_COLLIDE_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
	protected static final AxisAlignedBB AABB_COLLIDE_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_COLLIDE_ZAXIS_INWALL = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
	protected static final AxisAlignedBB AABB_COLLIDE_XAXIS_INWALL = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_CLOSED_SELECTED_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
	protected static final AxisAlignedBB AABB_CLOSED_SELECTED_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

	public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_) {
		super(Material.WOOD, p_i46394_1_.getMapColor());
		this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)).withProperty(IN_WALL, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = this.getActualState(state, source, pos);
		return state.getValue(IN_WALL).booleanValue() ? (state.getValue(FACING).getAxis() == EnumFacing.Axis.X ? AABB_COLLIDE_XAXIS_INWALL : AABB_COLLIDE_ZAXIS_INWALL)
				: (state.getValue(FACING).getAxis() == EnumFacing.Axis.X ? AABB_COLLIDE_XAXIS : AABB_COLLIDE_ZAXIS);
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata, such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFacing.Axis enumfacing$axis = state.getValue(FACING).getAxis();

		if (((enumfacing$axis == EnumFacing.Axis.Z)
				&& ((worldIn.getBlockState(pos.west()).getBlock() == Blocks.COBBLESTONE_WALL) || (worldIn.getBlockState(pos.east()).getBlock() == Blocks.COBBLESTONE_WALL)))
				|| ((enumfacing$axis == EnumFacing.Axis.X)
						&& ((worldIn.getBlockState(pos.north()).getBlock() == Blocks.COBBLESTONE_WALL) || (worldIn.getBlockState(pos.south()).getBlock() == Blocks.COBBLESTONE_WALL)))) {
			state = state.withProperty(IN_WALL, Boolean.valueOf(true));
		}

		return state;
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
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return blockState.getValue(OPEN).booleanValue() ? NULL_AABB : (blockState.getValue(FACING).getAxis() == EnumFacing.Axis.Z ? AABB_CLOSED_SELECTED_ZAXIS : AABB_CLOSED_SELECTED_XAXIS);
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
		return worldIn.getBlockState(pos).getValue(OPEN).booleanValue();
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(OPEN, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)).withProperty(IN_WALL,
				Boolean.valueOf(false));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY,
			float hitZ) {
		if (state.getValue(OPEN).booleanValue()) {
			state = state.withProperty(OPEN, Boolean.valueOf(false));
			worldIn.setBlockState(pos, state, 10);
		} else {
			EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);

			if (state.getValue(FACING) == enumfacing.getOpposite()) {
				state = state.withProperty(FACING, enumfacing);
			}

			state = state.withProperty(OPEN, Boolean.valueOf(true));
			worldIn.setBlockState(pos, state, 10);
		}

		worldIn.playEvent(playerIn, state.getValue(OPEN).booleanValue() ? 1008 : 1014, pos, 0);
		return true;
	}

	@Override
	public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_) {
		if (!p_189540_2_.isRemote) {
			boolean flag = p_189540_2_.isBlockPowered(p_189540_3_);

			if (flag || p_189540_4_.getDefaultState().canProvidePower()) {
				if (flag && !p_189540_1_.getValue(OPEN).booleanValue() && !p_189540_1_.getValue(POWERED).booleanValue()) {
					p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.withProperty(OPEN, Boolean.valueOf(true)).withProperty(POWERED, Boolean.valueOf(true)), 2);
					p_189540_2_.playEvent((EntityPlayer) null, 1008, p_189540_3_, 0);
				} else if (!flag && p_189540_1_.getValue(OPEN).booleanValue() && p_189540_1_.getValue(POWERED).booleanValue()) {
					p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.withProperty(OPEN, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)), 2);
					p_189540_2_.playEvent((EntityPlayer) null, 1014, p_189540_3_, 0);
				} else if (flag != p_189540_1_.getValue(POWERED).booleanValue()) {
					p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.withProperty(POWERED, Boolean.valueOf(flag)), 2);
				}
			}
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(OPEN, Boolean.valueOf((meta & 4) != 0)).withProperty(POWERED, Boolean.valueOf((meta & 8) != 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(FACING).getHorizontalIndex();

		if (state.getValue(POWERED).booleanValue()) {
			i |= 8;
		}

		if (state.getValue(OPEN).booleanValue()) {
			i |= 4;
		}

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, OPEN, POWERED, IN_WALL });
	}
}
