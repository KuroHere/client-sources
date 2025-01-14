/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.item;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemFishingRod
extends Item {
    private static final String __OBFID = "CL_00000034";

    public ItemFishingRod() {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (playerIn.fishEntity != null) {
            int var4 = playerIn.fishEntity.handleHookRetraction();
            itemStackIn.damageItem(var4, playerIn);
            playerIn.swingItem();
        } else {
            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(new EntityFishHook(worldIn, playerIn));
            }
            playerIn.swingItem();
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStackIn;
    }

    @Override
    public boolean isItemTool(ItemStack stack) {
        return super.isItemTool(stack);
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}

