package net.minecraft.block;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOldLeaf extends BlockLeaves {
	public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.<BlockPlanks.EnumType> create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
		@Override
		public boolean apply(@Nullable BlockPlanks.EnumType p_apply_1_) {
			return p_apply_1_.getMetadata() < 4;
		}
	});

	public BlockOldLeaf() {
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
	}

	@Override
	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
		if ((state.getValue(VARIANT) == BlockPlanks.EnumType.OAK) && (worldIn.rand.nextInt(chance) == 0)) {
			spawnAsEntity(worldIn, pos, new ItemStack(Items.APPLE));
		}
	}

	@Override
	protected int getSaplingDropChance(IBlockState state) {
		return state.getValue(VARIANT) == BlockPlanks.EnumType.JUNGLE ? 40 : super.getSaplingDropChance(state);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.getMetadata()));
		list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
		list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
		list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
	}

	@Override
	protected ItemStack createStackedBlock(IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).getMetadata());
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, this.getWoodType(meta)).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY,
				Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(VARIANT).getMetadata();

		if (!state.getValue(DECAYABLE).booleanValue()) {
			i |= 4;
		}

		if (state.getValue(CHECK_DECAY).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return BlockPlanks.EnumType.byMetadata((meta & 3) % 4);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT, CHECK_DECAY, DECAYABLE });
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).getMetadata();
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
		if (!worldIn.isRemote && (stack != null) && (stack.getItem() == Items.SHEARS)) {
			player.addStat(StatList.getBlockStats(this));
			spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).getMetadata()));
		} else {
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}
}
