/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWorker
extends Enchantment {
    private static final String __OBFID = "CL_00000124";

    public EnchantmentWaterWorker(int p_i45761_1_, ResourceLocation p_i45761_2_, int p_i45761_3_) {
        super(p_i45761_1_, p_i45761_2_, p_i45761_3_, EnumEnchantmentType.ARMOR_HEAD);
        this.setName("waterWorker");
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 1;
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 40;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}

