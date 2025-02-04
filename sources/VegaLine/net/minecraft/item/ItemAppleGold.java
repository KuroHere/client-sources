/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import ru.govno.client.module.modules.AutoApple;
import ru.govno.client.module.modules.PlayerHelper;

public class ItemAppleGold
extends ItemFood {
    public ItemAppleGold(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack) || stack.getMetadata() > 0;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return stack.getMetadata() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        if (stack != null && stack.getItem() instanceof ItemAppleGold) {
            if (PlayerHelper.get.actived && PlayerHelper.get.AppleEatCooldown.bValue && !PlayerHelper.checkApple && PlayerHelper.canCooldown()) {
                PlayerHelper.trackApple();
            }
            AutoApple.get.onEatenSucessfully(stack);
        }
        if (!worldIn.isRemote) {
            if (stack.getMetadata() > 0) {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
            } else {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
            }
        }
    }

    @Override
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            tab.add(new ItemStack(this));
            tab.add(new ItemStack(this, 1, 1));
        }
    }
}

