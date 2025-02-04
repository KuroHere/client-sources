/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapData;

public class ItemEmptyMap
extends ItemMapBase {
    private static final String __OBFID = "CL_00000024";

    protected ItemEmptyMap() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        ItemStack var4 = new ItemStack(Items.filled_map, 1, worldIn.getUniqueDataId("map"));
        String var5 = "map_" + var4.getMetadata();
        MapData var6 = new MapData(var5);
        worldIn.setItemData(var5, var6);
        var6.scale = 0;
        var6.func_176054_a(playerIn.posX, playerIn.posZ, var6.scale);
        var6.dimension = (byte)worldIn.provider.getDimensionId();
        var6.markDirty();
        --itemStackIn.stackSize;
        if (itemStackIn.stackSize <= 0) {
            return var4;
        }
        if (!playerIn.inventory.addItemStackToInventory(var4.copy())) {
            playerIn.dropPlayerItemWithRandomChoice(var4, false);
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}

