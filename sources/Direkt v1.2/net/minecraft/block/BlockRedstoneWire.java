package net.minecraft.block;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block {
	public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> NORTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition> create("north", BlockRedstoneWire.EnumAttachPosition.class);
	public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> EAST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition> create("east", BlockRedstoneWire.EnumAttachPosition.class);
	public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> SOUTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition> create("south", BlockRedstoneWire.EnumAttachPosition.class);
	public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> WEST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition> create("west", BlockRedstoneWire.EnumAttachPosition.class);
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	protected static final AxisAlignedBB[] REDSTONE_WIRE_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D),
			new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D) };
	private boolean canProvidePower = true;
	private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos> newHashSet();

	public BlockRedstoneWire() {
		super(Material.CIRCUITS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(EAST, BlockRedstoneWire.EnumAttachPosition.NONE)
				.withProperty(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(WEST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(POWER, Integer.valueOf(0)));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return REDSTONE_WIRE_AABB[getAABBIndex(state.getActualState(source, pos))];
	}

	private static int getAABBIndex(IBlockState state) {
		int i = 0;
		boolean flag = state.getValue(NORTH) != BlockRedstoneWire.EnumAttachPosition.NONE;
		boolean flag1 = state.getValue(EAST) != BlockRedstoneWire.EnumAttachPosition.NONE;
		boolean flag2 = state.getValue(SOUTH) != BlockRedstoneWire.EnumAttachPosition.NONE;
		boolean flag3 = state.getValue(WEST) != BlockRedstoneWire.EnumAttachPosition.NONE;

		if (flag || (flag2 && !flag && !flag1 && !flag3)) {
			i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
		}

		if (flag1 || (flag3 && !flag && !flag1 && !flag2)) {
			i |= 1 << EnumFacing.EAST.getHorizontalIndex();
		}

		if (flag2 || (flag && !flag1 && !flag2 && !flag3)) {
			i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
		}

		if (flag3 || (flag1 && !flag && !flag2 && !flag3)) {
			i |= 1 << EnumFacing.WEST.getHorizontalIndex();
		}

		return i;
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata, such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
		state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
		state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
		state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
		return state;
	}

	private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
		BlockPos blockpos = pos.offset(direction);
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(direction));

		if (!canConnectTo(worldIn.getBlockState(blockpos), direction) && (iblockstate.isNormalCube() || !canConnectUpwardsTo(worldIn.getBlockState(blockpos.down())))) {
			IBlockState iblockstate1 = worldIn.getBlockState(pos.up());

			if (!iblockstate1.isNormalCube()) {
				boolean flag = worldIn.getBlockState(blockpos).isFullyOpaque() || (worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE);

				if (flag && canConnectUpwardsTo(worldIn.getBlockState(blockpos.up()))) {
					if (iblockstate.isBlockNormalCube()) { return BlockRedstoneWire.EnumAttachPosition.UP; }

					return BlockRedstoneWire.EnumAttachPosition.SIDE;
				}
			}

			return BlockRedstoneWire.EnumAttachPosition.NONE;
		} else {
			return BlockRedstoneWire.EnumAttachPosition.SIDE;
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

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isFullyOpaque() || (worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE);
	}

	private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
		state = this.calculateCurrentChanges(worldIn, pos, pos, state);
		List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();

		for (BlockPos blockpos : list) {
			worldIn.notifyNeighborsOfStateChange(blockpos, this);
		}

		return state;
	}

	private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
		IBlockState iblockstate = state;
		int i = state.getValue(POWER).intValue();
		int j = 0;
		j = this.getMaxCurrentStrength(worldIn, pos2, j);
		this.canProvidePower = false;
		int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
		this.canProvidePower = true;

		if ((k > 0) && (k > (j - 1))) {
			j = k;
		}

		int l = 0;

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
			BlockPos blockpos = pos1.offset(enumfacing);
			boolean flag = (blockpos.getX() != pos2.getX()) || (blockpos.getZ() != pos2.getZ());

			if (flag) {
				l = this.getMaxCurrentStrength(worldIn, blockpos, l);
			}

			if (worldIn.getBlockState(blockpos).isNormalCube() && !worldIn.getBlockState(pos1.up()).isNormalCube()) {
				if (flag && (pos1.getY() >= pos2.getY())) {
					l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
				}
			} else if (!worldIn.getBlockState(blockpos).isNormalCube() && flag && (pos1.getY() <= pos2.getY())) {
				l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
			}
		}

		if (l > j) {
			j = l - 1;
		} else if (j > 0) {
			--j;
		} else {
			j = 0;
		}

		if (k > (j - 1)) {
			j = k;
		}

		if (i != j) {
			state = state.withProperty(POWER, Integer.valueOf(j));

			if (worldIn.getBlockState(pos1) == iblockstate) {
				worldIn.setBlockState(pos1, state, 2);
			}

			this.blocksNeedingUpdate.add(pos1);

			for (EnumFacing enumfacing1 : EnumFacing.values()) {
				this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
			}
		}

		return state;
	}

	/**
	 * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a redstone wire.
	 */
	private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos).getBlock() == this) {
			worldIn.notifyNeighborsOfStateChange(pos, this);

			for (EnumFacing enumfacing : EnumFacing.values()) {
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			this.updateSurroundingRedstone(worldIn, pos, state);

			for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
			}

			for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
				this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
			}

			for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
				BlockPos blockpos = pos.offset(enumfacing2);

				if (worldIn.getBlockState(blockpos).isNormalCube()) {
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
				} else {
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
				}
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);

		if (!worldIn.isRemote) {
			for (EnumFacing enumfacing : EnumFacing.values()) {
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
			}

			this.updateSurroundingRedstone(worldIn, pos, state);

			for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
				this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
			}

			for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
				BlockPos blockpos = pos.offset(enumfacing2);

				if (worldIn.getBlockState(blockpos).isNormalCube()) {
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
				} else {
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
				}
			}
		}
	}

	private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
		if (worldIn.getBlockState(pos).getBlock() != this) {
			return strength;
		} else {
			int i = worldIn.getBlockState(pos).getValue(POWER).intValue();
			return i > strength ? i : strength;
		}
	}

	@Override
	public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_) {
		if (!p_189540_2_.isRemote) {
			if (this.canPlaceBlockAt(p_189540_2_, p_189540_3_)) {
				this.updateSurroundingRedstone(p_189540_2_, p_189540_3_, p_189540_1_);
			} else {
				this.dropBlockAsItem(p_189540_2_, p_189540_3_, p_189540_1_, 0);
				p_189540_2_.setBlockToAir(p_189540_3_);
			}
		}
	}

	@Override
	@Nullable

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.REDSTONE;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if (!this.canProvidePower) {
			return 0;
		} else {
			int i = blockState.getValue(POWER).intValue();

			if (i == 0) {
				return 0;
			} else if (side == EnumFacing.UP) {
				return i;
			} else {
				EnumSet<EnumFacing> enumset = EnumSet.<EnumFacing> noneOf(EnumFacing.class);

				for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
					if (this.isPowerSourceAt(blockAccess, pos, enumfacing)) {
						enumset.add(enumfacing);
					}
				}

				if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
					return i;
				} else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY())) {
					return i;
				} else {
					return 0;
				}
			}
		}
	}

	private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		BlockPos blockpos = pos.offset(side);
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		boolean flag = iblockstate.isNormalCube();
		boolean flag1 = worldIn.getBlockState(pos.up()).isNormalCube();
		return !flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up()) ? true
				: (canConnectTo(iblockstate, side) ? true
						: ((iblockstate.getBlock() == Blocks.POWERED_REPEATER) && (iblockstate.getValue(BlockHorizontal.FACING) == side) ? true
								: !flag && canConnectUpwardsTo(worldIn, blockpos.down())));
	}

	protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos) {
		return canConnectUpwardsTo(worldIn.getBlockState(pos));
	}

	protected static boolean canConnectUpwardsTo(IBlockState state) {
		return canConnectTo(state, (EnumFacing) null);
	}

	protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side) {
		Block block = blockState.getBlock();

		if (block == Blocks.REDSTONE_WIRE) {
			return true;
		} else if (Blocks.UNPOWERED_REPEATER.isSameDiode(blockState)) {
			EnumFacing enumfacing = blockState.getValue(BlockHorizontal.FACING);
			return (enumfacing == side) || (enumfacing.getOpposite() == side);
		} else {
			return blockState.canProvidePower() && (side != null);
		}
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	@Override
	public boolean canProvidePower(IBlockState state) {
		return this.canProvidePower;
	}

	public static int colorMultiplier(int p_176337_0_) {
		float f = p_176337_0_ / 15.0F;
		float f1 = (f * 0.6F) + 0.4F;

		if (p_176337_0_ == 0) {
			f1 = 0.3F;
		}

		float f2 = (f * f * 0.7F) - 0.5F;
		float f3 = (f * f * 0.6F) - 0.7F;

		if (f2 < 0.0F) {
			f2 = 0.0F;
		}

		if (f3 < 0.0F) {
			f3 = 0.0F;
		}

		int i = MathHelper.clamp_int((int) (f1 * 255.0F), 0, 255);
		int j = MathHelper.clamp_int((int) (f2 * 255.0F), 0, 255);
		int k = MathHelper.clamp_int((int) (f3 * 255.0F), 0, 255);
		return -16777216 | (i << 16) | (j << 8) | k;
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		int i = stateIn.getValue(POWER).intValue();

		if (i != 0) {
			double d0 = pos.getX() + 0.5D + ((rand.nextFloat() - 0.5D) * 0.2D);
			double d1 = pos.getY() + 0.0625F;
			double d2 = pos.getZ() + 0.5D + ((rand.nextFloat() - 0.5D) * 0.2D);
			float f = i / 15.0F;
			float f1 = (f * 0.6F) + 0.4F;
			float f2 = Math.max(0.0F, (f * f * 0.7F) - 0.5F);
			float f3 = Math.max(0.0F, (f * f * 0.6F) - 0.7F);
			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, f1, f2, f3, new int[0]);
		}
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(Items.REDSTONE);
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
		return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POWER).intValue();
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		switch (rot) {
		case CLOCKWISE_180:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));

		case COUNTERCLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));

		case CLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));

		default:
			return state;
		}
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		switch (mirrorIn) {
		case LEFT_RIGHT:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));

		case FRONT_BACK:
			return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));

		default:
			return super.withMirror(state, mirrorIn);
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, SOUTH, WEST, POWER });
	}

	static enum EnumAttachPosition implements IStringSerializable {
		UP("up"), SIDE("side"), NONE("none");

		private final String name;

		private EnumAttachPosition(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.getName();
		}

		@Override
		public String getName() {
			return this.name;
		}
	}
}
