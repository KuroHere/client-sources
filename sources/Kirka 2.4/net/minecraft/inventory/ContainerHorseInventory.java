/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.inventory;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory
extends Container {
    private IInventory field_111243_a;
    private EntityHorse theHorse;
    private static final String __OBFID = "CL_00001751";

    public ContainerHorseInventory(IInventory p_i45791_1_, IInventory p_i45791_2_, final EntityHorse p_i45791_3_, EntityPlayer p_i45791_4_) {
        int var7;
        int var8;
        this.field_111243_a = p_i45791_2_;
        this.theHorse = p_i45791_3_;
        int var5 = 3;
        p_i45791_2_.openInventory(p_i45791_4_);
        int var6 = (var5 - 4) * 18;
        this.addSlotToContainer(new Slot(p_i45791_2_, 0, 8, 18){
            private static final String __OBFID = "CL_00001752";

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && stack.getItem() == Items.saddle && !this.getHasStack();
            }
        });
        this.addSlotToContainer(new Slot(p_i45791_2_, 1, 8, 36){
            private static final String __OBFID = "CL_00001753";

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && p_i45791_3_.canWearArmor() && EntityHorse.func_146085_a(stack.getItem());
            }

            @Override
            public boolean canBeHovered() {
                return p_i45791_3_.canWearArmor();
            }
        });
        if (p_i45791_3_.isChested()) {
            for (var7 = 0; var7 < var5; ++var7) {
                for (var8 = 0; var8 < 5; ++var8) {
                    this.addSlotToContainer(new Slot(p_i45791_2_, 2 + var8 + var7 * 5, 80 + var8 * 18, 18 + var7 * 18));
                }
            }
        }
        for (var7 = 0; var7 < 3; ++var7) {
            for (var8 = 0; var8 < 9; ++var8) {
                this.addSlotToContainer(new Slot(p_i45791_1_, var8 + var7 * 9 + 9, 8 + var8 * 18, 102 + var7 * 18 + var6));
            }
        }
        for (var7 = 0; var7 < 9; ++var7) {
            this.addSlotToContainer(new Slot(p_i45791_1_, var7, 8 + var7 * 18, 160 + var6));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.field_111243_a.isUseableByPlayer(playerIn) && this.theHorse.isEntityAlive() && this.theHorse.getDistanceToEntity(playerIn) < 8.0f;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index < this.field_111243_a.getSizeInventory() ? !this.mergeItemStack(var5, this.field_111243_a.getSizeInventory(), this.inventorySlots.size(), true) : (this.getSlot(1).isItemValid(var5) && !this.getSlot(1).getHasStack() ? !this.mergeItemStack(var5, 1, 2, false) : (this.getSlot(0).isItemValid(var5) ? !this.mergeItemStack(var5, 0, 1, false) : this.field_111243_a.getSizeInventory() <= 2 || !this.mergeItemStack(var5, 2, this.field_111243_a.getSizeInventory(), false)))) {
                return null;
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            } else {
                var4.onSlotChanged();
            }
        }
        return var3;
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.field_111243_a.closeInventory(p_75134_1_);
    }

}

