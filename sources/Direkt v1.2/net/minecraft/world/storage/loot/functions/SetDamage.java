package net.minecraft.world.storage.loot.functions;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetDamage extends LootFunction {
	private static final Logger LOGGER = LogManager.getLogger();
	private final RandomValueRange damageRange;

	public SetDamage(LootCondition[] conditionsIn, RandomValueRange damageRangeIn) {
		super(conditionsIn);
		this.damageRange = damageRangeIn;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		if (stack.isItemStackDamageable()) {
			float f = 1.0F - this.damageRange.generateFloat(rand);
			stack.setItemDamage(MathHelper.floor_float(f * stack.getMaxDamage()));
		} else {
			LOGGER.warn("Couldn\'t set damage of loot item {}", new Object[] { stack });
		}

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<SetDamage> {
		protected Serializer() {
			super(new ResourceLocation("set_damage"), SetDamage.class);
		}

		@Override
		public void serialize(JsonObject object, SetDamage functionClazz, JsonSerializationContext serializationContext) {
			object.add("damage", serializationContext.serialize(functionClazz.damageRange));
		}

		@Override
		public SetDamage deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
			return new SetDamage(conditionsIn, JsonUtils.deserializeClass(object, "damage", deserializationContext, RandomValueRange.class));
		}
	}
}
