package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowInfinite extends Enchantment {
	public EnchantmentArrowInfinite(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
		super(rarityIn, EnumEnchantmentType.BOW, slots);
		this.setName("arrowInfinite");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level passed.
	 */
	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return 20;
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment level passed.
	 */
	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return 50;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel() {
		return 1;
	}
}
