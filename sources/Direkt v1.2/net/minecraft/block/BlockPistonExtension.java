package net.minecraft.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension extends BlockDirectional {
	public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = PropertyEnum.<BlockPistonExtension.EnumPistonType> create("type", BlockPistonExtension.EnumPistonType.class);
	public static final PropertyBool SHORT = PropertyBool.create("short");
	protected static final AxisAlignedBB PISTON_EXTENSION_EAST_AABB = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB PISTON_EXTENSION_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
	protected static final AxisAlignedBB PISTON_EXTENSION_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB PISTON_EXTENSION_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
	protected static final AxisAlignedBB PISTON_EXTENSION_UP_AABB = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB PISTON_EXTENSION_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
	protected static final AxisAlignedBB UP_ARM_AABB = new AxisAlignedBB(0.375D, -0.25D, 0.375D, 0.625D, 0.75D, 0.625D);
	protected static final AxisAlignedBB DOWN_ARM_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.25D, 0.625D);
	protected static final AxisAlignedBB SOUTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, -0.25D, 0.625D, 0.625D, 0.75D);
	protected static final AxisAlignedBB NORTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.25D);
	protected static final AxisAlignedBB EAST_ARM_AABB = new AxisAlignedBB(-0.25D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
	protected static final AxisAlignedBB WEST_ARM_AABB = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.25D, 0.625D, 0.625D);

	public BlockPistonExtension() {
		super(Material.PISTON);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT).withProperty(SHORT, Boolean.valueOf(false)));
		this.setSoundType(SoundType.STONE);
		this.setHardness(0.5F);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case DOWN:
		default:
			return PISTON_EXTENSION_DOWN_AABB;

		case UP:
			return PISTON_EXTENSION_UP_AABB;

		case NORTH:
			return PISTON_EXTENSION_NORTH_AABB;

		case SOUTH:
			return PISTON_EXTENSION_SOUTH_AABB;

		case WEST:
			return PISTON_EXTENSION_WEST_AABB;

		case EAST:
			return PISTON_EXTENSION_EAST_AABB;
		}
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox(worldIn, pos));
		addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getArmShape(state));
	}

	private AxisAlignedBB getArmShape(IBlockState state) {
		switch (state.getValue(FACING)) {
		case DOWN:
		default:
			return DOWN_ARM_AABB;

		case UP:
			return UP_ARM_AABB;

		case NORTH:
			return NORTH_ARM_AABB;

		case SOUTH:
			return SOUTH_ARM_AABB;

		case WEST:
			return WEST_ARM_AABB;

		case EAST:
			return EAST_ARM_AABB;
		}
	}

	/**
	 * Checks if an IBlockState represents a block that is opaque and a full cube.
	 */
	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return state.getValue(FACING) == EnumFacing.UP;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode) {
			BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());
			Block block = worldIn.getBlockState(blockpos).getBlock();

			if ((block == Blocks.PISTON) || (block == Blocks.STICKY_PISTON)) {
				worldIn.setBlockToAir(blockpos);
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		EnumFacing enumfacing = state.getValue(FACING).getOpposite();
		pos = pos.offset(enumfacing);
		IBlockState iblockstate = worldIn.getBlockState(pos);

		if (((iblockstate.getBlock() == Blocks.PISTON) || (iblockstate.getBlock() == Blocks.STICKY_PISTON)) && iblockstate.getValue(BlockPistonBase.EXTENDED).booleanValue()) {
			iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
			worldIn.setBlockToAir(pos);
		}
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
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return false;
	}

	/**
	 * Check whether this Block can be placed on the given side
	 */
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_) {
		EnumFacing enumfacing = p_189540_1_.getValue(FACING);
		BlockPos blockpos = p_189540_3_.offset(enumfacing.getOpposite());
		IBlockState iblockstate = p_189540_2_.getBlockState(blockpos);

		if ((iblockstate.getBlock() != Blocks.PISTON) && (iblockstate.getBlock() != Blocks.STICKY_PISTON)) {
			p_189540_2_.setBlockToAir(p_189540_3_);
		} else {
			iblockstate.func_189546_a(p_189540_2_, blockpos, p_189540_4_);
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Nullable
	public static EnumFacing getFacing(int meta) {
		int i = meta & 7;
		return i > 5 ? null : EnumFacing.getFront(i);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(TYPE,
				(meta & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(FACING).getIndex();

		if (state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
			i |= 8;
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
		return new BlockStateContainer(this, new IProperty[] { FACING, TYPE, SHORT });
	}

	public static enum EnumPistonType implements IStringSerializable {
		DEFAULT("normal"), STICKY("sticky");

		private final String VARIANT;

		private EnumPistonType(String name) {
			this.VARIANT = name;
		}

		@Override
		public String toString() {
			return this.VARIANT;
		}

		@Override
		public String getName() {
			return this.VARIANT;
		}
	}
}
