package net.minecraft.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor extends Block {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool OPEN = PropertyBool.create("open");
	public static final PropertyEnum<BlockDoor.EnumHingePosition> HINGE = PropertyEnum.<BlockDoor.EnumHingePosition> create("hinge", BlockDoor.EnumHingePosition.class);
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyEnum<BlockDoor.EnumDoorHalf> HALF = PropertyEnum.<BlockDoor.EnumDoorHalf> create("half", BlockDoor.EnumDoorHalf.class);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

	protected BlockDoor(Material materialIn) {
		super(materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, Boolean.valueOf(false)).withProperty(HINGE, BlockDoor.EnumHingePosition.LEFT)
				.withProperty(POWERED, Boolean.valueOf(false)).withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = state.getActualState(source, pos);
		EnumFacing enumfacing = state.getValue(FACING);
		boolean flag = !state.getValue(OPEN).booleanValue();
		boolean flag1 = state.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT;

		switch (enumfacing) {
		case EAST:
		default:
			return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);

		case SOUTH:
			return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);

		case WEST:
			return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);

		case NORTH:
			return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
		}
	}

	/**
	 * Gets the localized name of this block. Used for the statistics page.
	 */
	@Override
	public String getLocalizedName() {
		return I18n.translateToLocal((this.getUnlocalizedName() + ".name").replaceAll("tile", "item"));
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return isOpen(combineMetadata(worldIn, pos));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	private int getCloseSound() {
		return this.blockMaterial == Material.IRON ? 1011 : 1012;
	}

	private int getOpenSound() {
		return this.blockMaterial == Material.IRON ? 1005 : 1006;
	}

	/**
	 * Get the MapColor for this Block and the given BlockState
	 */
	@Override
	public MapColor getMapColor(IBlockState state) {
		return state.getBlock() == Blocks.IRON_DOOR ? MapColor.IRON
				: (state.getBlock() == Blocks.OAK_DOOR ? BlockPlanks.EnumType.OAK.getMapColor()
						: (state.getBlock() == Blocks.SPRUCE_DOOR ? BlockPlanks.EnumType.SPRUCE.getMapColor()
								: (state.getBlock() == Blocks.BIRCH_DOOR ? BlockPlanks.EnumType.BIRCH.getMapColor()
										: (state.getBlock() == Blocks.JUNGLE_DOOR ? BlockPlanks.EnumType.JUNGLE.getMapColor()
												: (state.getBlock() == Blocks.ACACIA_DOOR ? BlockPlanks.EnumType.ACACIA.getMapColor()
														: (state.getBlock() == Blocks.DARK_OAK_DOOR ? BlockPlanks.EnumType.DARK_OAK.getMapColor() : super.getMapColor(state)))))));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY,
			float hitZ) {
		if (this.blockMaterial == Material.IRON) {
			return true;
		} else {
			BlockPos blockpos = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
			IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);

			if (iblockstate.getBlock() != this) {
				return false;
			} else {
				state = iblockstate.cycleProperty(OPEN);
				worldIn.setBlockState(blockpos, state, 10);
				worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
				worldIn.playEvent(playerIn, state.getValue(OPEN).booleanValue() ? this.getOpenSound() : this.getCloseSound(), pos, 0);
				return true;
			}
		}
	}

	public void toggleDoor(World worldIn, BlockPos pos, boolean open) {
		IBlockState iblockstate = worldIn.getBlockState(pos);

		if (iblockstate.getBlock() == this) {
			BlockPos blockpos = iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
			IBlockState iblockstate1 = pos == blockpos ? iblockstate : worldIn.getBlockState(blockpos);

			if ((iblockstate1.getBlock() == this) && (iblockstate1.getValue(OPEN).booleanValue() != open)) {
				worldIn.setBlockState(blockpos, iblockstate1.withProperty(OPEN, Boolean.valueOf(open)), 10);
				worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
				worldIn.playEvent((EntityPlayer) null, open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
			}
		}
	}

	@Override
	public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_) {
		if (p_189540_1_.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			BlockPos blockpos = p_189540_3_.down();
			IBlockState iblockstate = p_189540_2_.getBlockState(blockpos);

			if (iblockstate.getBlock() != this) {
				p_189540_2_.setBlockToAir(p_189540_3_);
			} else if (p_189540_4_ != this) {
				iblockstate.func_189546_a(p_189540_2_, blockpos, p_189540_4_);
			}
		} else {
			boolean flag1 = false;
			BlockPos blockpos1 = p_189540_3_.up();
			IBlockState iblockstate1 = p_189540_2_.getBlockState(blockpos1);

			if (iblockstate1.getBlock() != this) {
				p_189540_2_.setBlockToAir(p_189540_3_);
				flag1 = true;
			}

			if (!p_189540_2_.getBlockState(p_189540_3_.down()).isFullyOpaque()) {
				p_189540_2_.setBlockToAir(p_189540_3_);
				flag1 = true;

				if (iblockstate1.getBlock() == this) {
					p_189540_2_.setBlockToAir(blockpos1);
				}
			}

			if (flag1) {
				if (!p_189540_2_.isRemote) {
					this.dropBlockAsItem(p_189540_2_, p_189540_3_, p_189540_1_, 0);
				}
			} else {
				boolean flag = p_189540_2_.isBlockPowered(p_189540_3_) || p_189540_2_.isBlockPowered(blockpos1);

				if ((p_189540_4_ != this) && (flag || p_189540_4_.getDefaultState().canProvidePower()) && (flag != iblockstate1.getValue(POWERED).booleanValue())) {
					p_189540_2_.setBlockState(blockpos1, iblockstate1.withProperty(POWERED, Boolean.valueOf(flag)), 2);

					if (flag != p_189540_1_.getValue(OPEN).booleanValue()) {
						p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.withProperty(OPEN, Boolean.valueOf(flag)), 2);
						p_189540_2_.markBlockRangeForRenderUpdate(p_189540_3_, p_189540_3_);
						p_189540_2_.playEvent((EntityPlayer) null, flag ? this.getOpenSound() : this.getCloseSound(), p_189540_3_, 0);
					}
				}
			}
		}
	}

	@Override
	@Nullable

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : this.getItem();
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return pos.getY() >= 255 ? false : worldIn.getBlockState(pos.down()).isFullyOpaque() && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	public static int combineMetadata(IBlockAccess worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		int i = iblockstate.getBlock().getMetaFromState(iblockstate);
		boolean flag = isTop(i);
		IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
		int j = iblockstate1.getBlock().getMetaFromState(iblockstate1);
		int k = flag ? j : i;
		IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
		int l = iblockstate2.getBlock().getMetaFromState(iblockstate2);
		int i1 = flag ? i : l;
		boolean flag1 = (i1 & 1) != 0;
		boolean flag2 = (i1 & 2) != 0;
		return removeHalfBit(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this.getItem());
	}

	private Item getItem() {
		return this == Blocks.IRON_DOOR ? Items.IRON_DOOR
				: (this == Blocks.SPRUCE_DOOR ? Items.SPRUCE_DOOR
						: (this == Blocks.BIRCH_DOOR ? Items.BIRCH_DOOR
								: (this == Blocks.JUNGLE_DOOR ? Items.JUNGLE_DOOR
										: (this == Blocks.ACACIA_DOOR ? Items.ACACIA_DOOR : (this == Blocks.DARK_OAK_DOOR ? Items.DARK_OAK_DOOR : Items.OAK_DOOR)))));
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		BlockPos blockpos = pos.down();
		BlockPos blockpos1 = pos.up();

		if (player.capabilities.isCreativeMode && (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) && (worldIn.getBlockState(blockpos).getBlock() == this)) {
			worldIn.setBlockToAir(blockpos);
		}

		if ((state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER) && (worldIn.getBlockState(blockpos1).getBlock() == this)) {
			if (player.capabilities.isCreativeMode) {
				worldIn.setBlockToAir(pos);
			}

			worldIn.setBlockToAir(blockpos1);
		}
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata, such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER) {
			IBlockState iblockstate = worldIn.getBlockState(pos.up());

			if (iblockstate.getBlock() == this) {
				state = state.withProperty(HINGE, iblockstate.getValue(HINGE)).withProperty(POWERED, iblockstate.getValue(POWERED));
			}
		} else {
			IBlockState iblockstate1 = worldIn.getBlockState(pos.down());

			if (iblockstate1.getBlock() == this) {
				state = state.withProperty(FACING, iblockstate1.getValue(FACING)).withProperty(OPEN, iblockstate1.getValue(OPEN));
			}
		}

		return state;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.getValue(HALF) != BlockDoor.EnumDoorHalf.LOWER ? state : state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return mirrorIn == Mirror.NONE ? state : state.withRotation(mirrorIn.toRotation(state.getValue(FACING))).cycleProperty(HINGE);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return (meta & 8) > 0
				? this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(HINGE, (meta & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT)
						.withProperty(POWERED, Boolean.valueOf((meta & 2) > 0))
				: this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW()).withProperty(OPEN,
						Boolean.valueOf((meta & 4) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;

		if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			i = i | 8;

			if (state.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT) {
				i |= 1;
			}

			if (state.getValue(POWERED).booleanValue()) {
				i |= 2;
			}
		} else {
			i = i | state.getValue(FACING).rotateY().getHorizontalIndex();

			if (state.getValue(OPEN).booleanValue()) {
				i |= 4;
			}
		}

		return i;
	}

	protected static int removeHalfBit(int meta) {
		return meta & 7;
	}

	public static boolean isOpen(IBlockAccess worldIn, BlockPos pos) {
		return isOpen(combineMetadata(worldIn, pos));
	}

	public static EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
		return getFacing(combineMetadata(worldIn, pos));
	}

	public static EnumFacing getFacing(int combinedMeta) {
		return EnumFacing.getHorizontal(combinedMeta & 3).rotateYCCW();
	}

	protected static boolean isOpen(int combinedMeta) {
		return (combinedMeta & 4) != 0;
	}

	protected static boolean isTop(int meta) {
		return (meta & 8) != 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { HALF, FACING, OPEN, HINGE, POWERED });
	}

	public static enum EnumDoorHalf implements IStringSerializable {
		UPPER, LOWER;

		@Override
		public String toString() {
			return this.getName();
		}

		@Override
		public String getName() {
			return this == UPPER ? "upper" : "lower";
		}
	}

	public static enum EnumHingePosition implements IStringSerializable {
		LEFT, RIGHT;

		@Override
		public String toString() {
			return this.getName();
		}

		@Override
		public String getName() {
			return this == LEFT ? "left" : "right";
		}
	}
}
