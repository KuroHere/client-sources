package net.minecraft.block;

import java.util.Random;

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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block {
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool[] ALL_FACES = new PropertyBool[] { UP, NORTH, SOUTH, WEST, EAST };
	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

	public BlockVine() {
		super(Material.VINE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false))
				.withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = state.getActualState(source, pos);
		int i = 0;
		AxisAlignedBB axisalignedbb = FULL_BLOCK_AABB;

		if (state.getValue(UP).booleanValue()) {
			axisalignedbb = UP_AABB;
			++i;
		}

		if (state.getValue(NORTH).booleanValue()) {
			axisalignedbb = NORTH_AABB;
			++i;
		}

		if (state.getValue(EAST).booleanValue()) {
			axisalignedbb = EAST_AABB;
			++i;
		}

		if (state.getValue(SOUTH).booleanValue()) {
			axisalignedbb = SOUTH_AABB;
			++i;
		}

		if (state.getValue(WEST).booleanValue()) {
			axisalignedbb = WEST_AABB;
			++i;
		}

		return i == 1 ? axisalignedbb : FULL_BLOCK_AABB;
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata, such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(UP, Boolean.valueOf(worldIn.getBlockState(pos.up()).isBlockNormalCube()));
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

	/**
	 * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
	 */
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	/**
	 * Check whether this Block can be placed on the given side
	 */
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		switch (side) {
		case UP:
			return this.canAttachVineOn(worldIn.getBlockState(pos.up()));

		case NORTH:
		case SOUTH:
		case EAST:
		case WEST:
			return this.canAttachVineOn(worldIn.getBlockState(pos.offset(side.getOpposite())));

		default:
			return false;
		}
	}

	/**
	 * Determines whether you can place a vine block on this kind of block.
	 */
	private boolean canAttachVineOn(IBlockState state) {
		return state.isFullCube() && state.getMaterial().blocksMovement();
	}

	private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
		IBlockState iblockstate = state;

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
			PropertyBool propertybool = getPropertyFor(enumfacing);

			if (state.getValue(propertybool).booleanValue() && !this.canAttachVineOn(worldIn.getBlockState(pos.offset(enumfacing)))) {
				IBlockState iblockstate1 = worldIn.getBlockState(pos.up());

				if ((iblockstate1.getBlock() != this) || !iblockstate1.getValue(propertybool).booleanValue()) {
					state = state.withProperty(propertybool, Boolean.valueOf(false));
				}
			}
		}

		if (getNumGrownFaces(state) == 0) {
			return false;
		} else {
			if (iblockstate != state) {
				worldIn.setBlockState(pos, state, 2);
			}

			return true;
		}
	}

	@Override
	public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_) {
		if (!p_189540_2_.isRemote && !this.recheckGrownSides(p_189540_2_, p_189540_3_, p_189540_1_)) {
			this.dropBlockAsItem(p_189540_2_, p_189540_3_, p_189540_1_, 0);
			p_189540_2_.setBlockToAir(p_189540_3_);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			if (worldIn.rand.nextInt(4) == 0) {
				int i = 4;
				int j = 5;
				boolean flag = false;
				label58:

				for (int k = -4; k <= 4; ++k) {
					for (int l = -4; l <= 4; ++l) {
						for (int i1 = -1; i1 <= 1; ++i1) {
							if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() == this) {
								--j;

								if (j <= 0) {
									flag = true;
									break label58;
								}
							}
						}
					}
				}

				EnumFacing enumfacing1 = EnumFacing.random(rand);
				BlockPos blockpos2 = pos.up();

				if ((enumfacing1 == EnumFacing.UP) && (pos.getY() < 255) && worldIn.isAirBlock(blockpos2)) {
					if (!flag) {
						IBlockState iblockstate2 = state;

						for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
							if (rand.nextBoolean() || !this.canAttachVineOn(worldIn.getBlockState(blockpos2.offset(enumfacing2)))) {
								iblockstate2 = iblockstate2.withProperty(getPropertyFor(enumfacing2), Boolean.valueOf(false));
							}
						}

						if (iblockstate2.getValue(NORTH).booleanValue() || iblockstate2.getValue(EAST).booleanValue() || iblockstate2.getValue(SOUTH).booleanValue()
								|| iblockstate2.getValue(WEST).booleanValue()) {
							worldIn.setBlockState(blockpos2, iblockstate2, 2);
						}
					}
				} else if (enumfacing1.getAxis().isHorizontal() && !state.getValue(getPropertyFor(enumfacing1)).booleanValue()) {
					if (!flag) {
						BlockPos blockpos4 = pos.offset(enumfacing1);
						IBlockState iblockstate3 = worldIn.getBlockState(blockpos4);
						Block block1 = iblockstate3.getBlock();

						if (block1.blockMaterial == Material.AIR) {
							EnumFacing enumfacing3 = enumfacing1.rotateY();
							EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
							boolean flag1 = state.getValue(getPropertyFor(enumfacing3)).booleanValue();
							boolean flag2 = state.getValue(getPropertyFor(enumfacing4)).booleanValue();
							BlockPos blockpos = blockpos4.offset(enumfacing3);
							BlockPos blockpos1 = blockpos4.offset(enumfacing4);

							if (flag1 && this.canAttachVineOn(worldIn.getBlockState(blockpos))) {
								worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty(getPropertyFor(enumfacing3), Boolean.valueOf(true)), 2);
							} else if (flag2 && this.canAttachVineOn(worldIn.getBlockState(blockpos1))) {
								worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty(getPropertyFor(enumfacing4), Boolean.valueOf(true)), 2);
							} else if (flag1 && worldIn.isAirBlock(blockpos) && this.canAttachVineOn(worldIn.getBlockState(pos.offset(enumfacing3)))) {
								worldIn.setBlockState(blockpos, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
							} else if (flag2 && worldIn.isAirBlock(blockpos1) && this.canAttachVineOn(worldIn.getBlockState(pos.offset(enumfacing4)))) {
								worldIn.setBlockState(blockpos1, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
							} else if (this.canAttachVineOn(worldIn.getBlockState(blockpos4.up()))) {
								worldIn.setBlockState(blockpos4, this.getDefaultState(), 2);
							}
						} else if (block1.blockMaterial.isOpaque() && iblockstate3.isFullCube()) {
							worldIn.setBlockState(pos, state.withProperty(getPropertyFor(enumfacing1), Boolean.valueOf(true)), 2);
						}
					}
				} else {
					if (pos.getY() > 1) {
						BlockPos blockpos3 = pos.down();
						IBlockState iblockstate = worldIn.getBlockState(blockpos3);
						Block block = iblockstate.getBlock();

						if (block.blockMaterial == Material.AIR) {
							IBlockState iblockstate1 = state;

							for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
								if (rand.nextBoolean()) {
									iblockstate1 = iblockstate1.withProperty(getPropertyFor(enumfacing), Boolean.valueOf(false));
								}
							}

							if (iblockstate1.getValue(NORTH).booleanValue() || iblockstate1.getValue(EAST).booleanValue() || iblockstate1.getValue(SOUTH).booleanValue()
									|| iblockstate1.getValue(WEST).booleanValue()) {
								worldIn.setBlockState(blockpos3, iblockstate1, 2);
							}
						} else if (block == this) {
							IBlockState iblockstate4 = iblockstate;

							for (EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
								PropertyBool propertybool = getPropertyFor(enumfacing5);

								if (rand.nextBoolean() && state.getValue(propertybool).booleanValue()) {
									iblockstate4 = iblockstate4.withProperty(propertybool, Boolean.valueOf(true));
								}
							}

							if (iblockstate4.getValue(NORTH).booleanValue() || iblockstate4.getValue(EAST).booleanValue() || iblockstate4.getValue(SOUTH).booleanValue()
									|| iblockstate4.getValue(WEST).booleanValue()) {
								worldIn.setBlockState(blockpos3, iblockstate4, 2);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState iblockstate = this.getDefaultState().withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false))
				.withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false));
		return facing.getAxis().isHorizontal() ? iblockstate.withProperty(getPropertyFor(facing.getOpposite()), Boolean.valueOf(true)) : iblockstate;
	}

	@Override
	@Nullable

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
		if (!worldIn.isRemote && (stack != null) && (stack.getItem() == Items.SHEARS)) {
			player.addStat(StatList.getBlockStats(this));
			spawnAsEntity(worldIn, pos, new ItemStack(Blocks.VINE, 1, 0));
		} else {
			super.harvestBlock(worldIn, player, pos, state, te, stack);
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
		return this.getDefaultState().withProperty(SOUTH, Boolean.valueOf((meta & 1) > 0)).withProperty(WEST, Boolean.valueOf((meta & 2) > 0)).withProperty(NORTH, Boolean.valueOf((meta & 4) > 0))
				.withProperty(EAST, Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;

		if (state.getValue(SOUTH).booleanValue()) {
			i |= 1;
		}

		if (state.getValue(WEST).booleanValue()) {
			i |= 2;
		}

		if (state.getValue(NORTH).booleanValue()) {
			i |= 4;
		}

		if (state.getValue(EAST).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { UP, NORTH, EAST, SOUTH, WEST });
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

	public static PropertyBool getPropertyFor(EnumFacing side) {
		switch (side) {
		case UP:
			return UP;

		case NORTH:
			return NORTH;

		case SOUTH:
			return SOUTH;

		case EAST:
			return EAST;

		case WEST:
			return WEST;

		default:
			throw new IllegalArgumentException(side + " is an invalid choice");
		}
	}

	public static int getNumGrownFaces(IBlockState state) {
		int i = 0;

		for (PropertyBool propertybool : ALL_FACES) {
			if (state.getValue(propertybool).booleanValue()) {
				++i;
			}
		}

		return i;
	}
}
