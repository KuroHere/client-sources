package net.minecraft.block.state.pattern;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockMatcher implements Predicate<IBlockState> {
	private final Block block;

	private BlockMatcher(Block blockType) {
		this.block = blockType;
	}

	public static BlockMatcher forBlock(Block blockType) {
		return new BlockMatcher(blockType);
	}

	@Override
	public boolean apply(@Nullable IBlockState p_apply_1_) {
		return (p_apply_1_ != null) && (p_apply_1_.getBlock() == this.block);
	}
}
