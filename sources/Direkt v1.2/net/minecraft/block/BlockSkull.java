package net.minecraft.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull extends BlockContainer {
	public static final PropertyDirection FACING = BlockDirectional.FACING;
	public static final PropertyBool NODROP = PropertyBool.create("nodrop");
	private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = new Predicate<BlockWorldState>() {
		@Override
		public boolean apply(@Nullable BlockWorldState p_apply_1_) {
			return (p_apply_1_.getBlockState() != null) && (p_apply_1_.getBlockState().getBlock() == Blocks.SKULL) && (p_apply_1_.getTileEntity() instanceof TileEntitySkull)
					&& (((TileEntitySkull) p_apply_1_.getTileEntity()).getSkullType() == 1);
		}
	};
	protected static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D);
	private BlockPattern witherBasePattern;
	private BlockPattern witherPattern;

	protected BlockSkull() {
		super(Material.CIRCUITS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP, Boolean.valueOf(false)));
	}

	/**
	 * Gets the localized name of this block. Used for the statistics page.
	 */
	@Override
	public String getLocalizedName() {
		return I18n.translateToLocal("tile.skull.skeleton.name");
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case UP:
		default:
			return DEFAULT_AABB;

		case NORTH:
			return NORTH_AABB;

		case SOUTH:
			return SOUTH_AABB;

		case WEST:
			return WEST_AABB;

		case EAST:
			return EAST_AABB;
		}
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(NODROP, Boolean.valueOf(false));
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySkull();
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		int i = 0;
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntitySkull) {
			i = ((TileEntitySkull) tileentity).getSkullType();
		}

		return new ItemStack(Items.SKULL, 1, i);
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode) {
			state = state.withProperty(NODROP, Boolean.valueOf(true));
			worldIn.setBlockState(pos, state, 4);
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			if (!state.getValue(NODROP).booleanValue()) {
				TileEntity tileentity = worldIn.getTileEntity(pos);

				if (tileentity instanceof TileEntitySkull) {
					TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;
					ItemStack itemstack = this.getItem(worldIn, pos, state);

					if ((tileentityskull.getSkullType() == 3) && (tileentityskull.getPlayerProfile() != null)) {
						itemstack.setTagCompound(new NBTTagCompound());
						NBTTagCompound nbttagcompound = new NBTTagCompound();
						NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
						itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
					}

					spawnAsEntity(worldIn, pos, itemstack);
				}
			}

			super.breakBlock(worldIn, pos, state);
		}
	}

	@Override
	@Nullable

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.SKULL;
	}

	public boolean canDispenserPlace(World worldIn, BlockPos pos, ItemStack stack) {
		return (stack.getMetadata() == 1) && (pos.getY() >= 2) && (worldIn.getDifficulty() != EnumDifficulty.PEACEFUL) && !worldIn.isRemote ? this.getWitherBasePattern().match(worldIn, pos) != null
				: false;
	}

	public void checkWitherSpawn(World worldIn, BlockPos pos, TileEntitySkull te) {
		if ((te.getSkullType() == 1) && (pos.getY() >= 2) && (worldIn.getDifficulty() != EnumDifficulty.PEACEFUL) && !worldIn.isRemote) {
			BlockPattern blockpattern = this.getWitherPattern();
			BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);

			if (blockpattern$patternhelper != null) {
				for (int i = 0; i < 3; ++i) {
					BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
					worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty(NODROP, Boolean.valueOf(true)), 2);
				}

				for (int j = 0; j < blockpattern.getPalmLength(); ++j) {
					for (int k = 0; k < blockpattern.getThumbLength(); ++k) {
						BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
						worldIn.setBlockState(blockworldstate1.getPos(), Blocks.AIR.getDefaultState(), 2);
					}
				}

				BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
				EntityWither entitywither = new EntityWither(worldIn);
				BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
				entitywither.setLocationAndAngles(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.55D, blockpos1.getZ() + 0.5D,
						blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
				entitywither.renderYawOffset = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
				entitywither.ignite();

				for (EntityPlayer entityplayer : worldIn.getEntitiesWithinAABB(EntityPlayer.class, entitywither.getEntityBoundingBox().expandXyz(50.0D))) {
					entityplayer.addStat(AchievementList.SPAWN_WITHER);
				}

				worldIn.spawnEntityInWorld(entitywither);

				for (int l = 0; l < 120; ++l) {
					worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos.getX() + worldIn.rand.nextDouble(), blockpos.getY() - 2 + (worldIn.rand.nextDouble() * 3.9D),
							blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
				}

				for (int i1 = 0; i1 < blockpattern.getPalmLength(); ++i1) {
					for (int j1 = 0; j1 < blockpattern.getThumbLength(); ++j1) {
						BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
						worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR);
					}
				}
			}
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(NODROP, Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(FACING).getIndex();

		if (state.getValue(NODROP).booleanValue()) {
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
		return new BlockStateContainer(this, new IProperty[] { FACING, NODROP });
	}

	protected BlockPattern getWitherBasePattern() {
		if (this.witherBasePattern == null) {
			this.witherBasePattern = FactoryBlockPattern.start().aisle(new String[] { "   ", "###", "~#~" }).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND)))
					.where('~', BlockWorldState.hasState(BlockMaterialMatcher.func_189886_a(Material.AIR))).build();
		}

		return this.witherBasePattern;
	}

	protected BlockPattern getWitherPattern() {
		if (this.witherPattern == null) {
			this.witherPattern = FactoryBlockPattern.start().aisle(new String[] { "^^^", "###", "~#~" }).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND)))
					.where('^', IS_WITHER_SKELETON).where('~', BlockWorldState.hasState(BlockMaterialMatcher.func_189886_a(Material.AIR))).build();
		}

		return this.witherPattern;
	}
}
