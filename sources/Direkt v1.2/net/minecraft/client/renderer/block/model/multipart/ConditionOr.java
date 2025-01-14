package net.minecraft.client.renderer.block.model.multipart;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class ConditionOr implements ICondition {
	final Iterable<ICondition> conditions;

	public ConditionOr(Iterable<ICondition> conditionsIn) {
		this.conditions = conditionsIn;
	}

	@Override
	public Predicate<IBlockState> getPredicate(final BlockStateContainer blockState) {
		return Predicates.or(Iterables.transform(this.conditions, new Function<ICondition, Predicate<IBlockState>>() {
			@Override
			@Nullable
			public Predicate<IBlockState> apply(@Nullable ICondition p_apply_1_) {
				return p_apply_1_ == null ? null : p_apply_1_.getPredicate(blockState);
			}
		}));
	}
}
