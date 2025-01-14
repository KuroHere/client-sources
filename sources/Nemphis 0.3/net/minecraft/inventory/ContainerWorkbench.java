/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.inventory;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerWorkbench
extends Container {
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private World worldObj;
    private BlockPos field_178145_h;
    private static final String __OBFID = "CL_00001744";

    public ContainerWorkbench(InventoryPlayer p_i45800_1_, World worldIn, BlockPos p_i45800_3_) {
        int var5;
        this.craftMatrix = new InventoryCrafting(this, 3, 3);
        this.craftResult = new InventoryCraftResult();
        this.worldObj = worldIn;
        this.field_178145_h = p_i45800_3_;
        this.addSlotToContainer(new SlotCrafting(p_i45800_1_.player, this.craftMatrix, this.craftResult, 0, 124, 35));
        int var4 = 0;
        while (var4 < 3) {
            var5 = 0;
            while (var5 < 3) {
                this.addSlotToContainer(new Slot(this.craftMatrix, var5 + var4 * 3, 30 + var5 * 18, 17 + var4 * 18));
                ++var5;
            }
            ++var4;
        }
        var4 = 0;
        while (var4 < 3) {
            var5 = 0;
            while (var5 < 9) {
                this.addSlotToContainer(new Slot(p_i45800_1_, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
                ++var5;
            }
            ++var4;
        }
        var4 = 0;
        while (var4 < 9) {
            this.addSlotToContainer(new Slot(p_i45800_1_, var4, 8 + var4 * 18, 142));
            ++var4;
        }
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.worldObj.isRemote) {
            int var2 = 0;
            while (var2 < 9) {
                ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);
                if (var3 != null) {
                    p_75134_1_.dropPlayerItemWithRandomChoice(var3, false);
                }
                ++var2;
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.worldObj.getBlockState(this.field_178145_h).getBlock() != Blocks.crafting_table ? false : playerIn.getDistanceSq((double)this.field_178145_h.getX() + 0.5, (double)this.field_178145_h.getY() + 0.5, (double)this.field_178145_h.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index == 0) {
                if (!this.mergeItemStack(var5, 10, 46, true)) {
                    return null;
                }
                var4.onSlotChange(var5, var3);
            } else if (index >= 10 && index < 37 ? !this.mergeItemStack(var5, 37, 46, false) : (index >= 37 && index < 46 ? !this.mergeItemStack(var5, 10, 37, false) : !this.mergeItemStack(var5, 10, 46, false))) {
                return null;
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            } else {
                var4.onSlotChanged();
            }
            if (var5.stackSize == var3.stackSize) {
                return null;
            }
            var4.onPickupFromSlot(playerIn, var5);
        }
        return var3;
    }

    @Override
    public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
        if (p_94530_2_.inventory != this.craftResult && super.func_94530_a(p_94530_1_, p_94530_2_)) {
            return true;
        }
        return false;
    }
}

