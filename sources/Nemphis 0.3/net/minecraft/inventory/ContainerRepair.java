/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.inventory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair
extends Container {
    private static final Logger logger = LogManager.getLogger();
    private IInventory outputSlot = new InventoryCraftResult();
    private IInventory inputSlots;
    private World theWorld;
    private BlockPos field_178156_j;
    public int maximumCost;
    private int materialCost;
    private String repairedItemName;
    private final EntityPlayer thePlayer;
    private static final String __OBFID = "CL_00001732";

    public ContainerRepair(InventoryPlayer p_i45806_1_, World worldIn, EntityPlayer p_i45806_3_) {
        this(p_i45806_1_, worldIn, BlockPos.ORIGIN, p_i45806_3_);
    }

    public ContainerRepair(InventoryPlayer p_i45807_1_, final World worldIn, final BlockPos p_i45807_3_, EntityPlayer p_i45807_4_) {
        this.inputSlots = new InventoryBasic("Repair", true, 2){
            private static final String __OBFID = "CL_00001733";

            @Override
            public void markDirty() {
                super.markDirty();
                ContainerRepair.this.onCraftMatrixChanged(this);
            }
        };
        this.field_178156_j = p_i45807_3_;
        this.theWorld = worldIn;
        this.thePlayer = p_i45807_4_;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47){
            private static final String __OBFID = "CL_00001734";

            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeStack(EntityPlayer p_82869_1_) {
                if ((p_82869_1_.capabilities.isCreativeMode || p_82869_1_.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack()) {
                    return true;
                }
                return false;
            }

            @Override
            public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.addExperienceLevel(- ContainerRepair.this.maximumCost);
                }
                ContainerRepair.this.inputSlots.setInventorySlotContents(0, null);
                if (ContainerRepair.this.materialCost > 0) {
                    ItemStack var3 = ContainerRepair.this.inputSlots.getStackInSlot(1);
                    if (var3 != null && var3.stackSize > ContainerRepair.this.materialCost) {
                        var3.stackSize -= ContainerRepair.this.materialCost;
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, var3);
                    } else {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                    }
                } else {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                }
                ContainerRepair.this.maximumCost = 0;
                IBlockState var5 = worldIn.getBlockState(p_i45807_3_);
                if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && var5.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12f) {
                    int var4 = (Integer)var5.getValue(BlockAnvil.DAMAGE);
                    if (++var4 > 2) {
                        worldIn.setBlockToAir(p_i45807_3_);
                        worldIn.playAuxSFX(1020, p_i45807_3_, 0);
                    } else {
                        worldIn.setBlockState(p_i45807_3_, var5.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(var4)), 2);
                        worldIn.playAuxSFX(1021, p_i45807_3_, 0);
                    }
                } else if (!worldIn.isRemote) {
                    worldIn.playAuxSFX(1021, p_i45807_3_, 0);
                }
            }
        });
        int var5 = 0;
        while (var5 < 3) {
            int var6 = 0;
            while (var6 < 9) {
                this.addSlotToContainer(new Slot(p_i45807_1_, var6 + var5 * 9 + 9, 8 + var6 * 18, 84 + var5 * 18));
                ++var6;
            }
            ++var5;
        }
        var5 = 0;
        while (var5 < 9) {
            this.addSlotToContainer(new Slot(p_i45807_1_, var5, 8 + var5 * 18, 142));
            ++var5;
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        super.onCraftMatrixChanged(p_75130_1_);
        if (p_75130_1_ == this.inputSlots) {
            this.updateRepairOutput();
        }
    }

    public void updateRepairOutput() {
        boolean var1 = false;
        boolean var2 = true;
        boolean var3 = true;
        boolean var4 = true;
        boolean var5 = true;
        boolean var6 = true;
        boolean var7 = true;
        ItemStack var8 = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int var9 = 0;
        int var10 = 0;
        int var11 = 0;
        if (var8 == null) {
            this.outputSlot.setInventorySlotContents(0, null);
            this.maximumCost = 0;
        } else {
            int var16;
            ItemStack var12 = var8.copy();
            ItemStack var13 = this.inputSlots.getStackInSlot(1);
            Map var14 = EnchantmentHelper.getEnchantments(var12);
            boolean var15 = false;
            int var25 = var10 + var8.getRepairCost() + (var13 == null ? 0 : var13.getRepairCost());
            this.materialCost = 0;
            if (var13 != null) {
                boolean bl = var15 = var13.getItem() == Items.enchanted_book && Items.enchanted_book.func_92110_g(var13).tagCount() > 0;
                if (var12.isItemStackDamageable() && var12.getItem().getIsRepairable(var8, var13)) {
                    var16 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4);
                    if (var16 <= 0) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    int var17 = 0;
                    while (var16 > 0 && var17 < var13.stackSize) {
                        int var18 = var12.getItemDamage() - var16;
                        var12.setItemDamage(var18);
                        ++var9;
                        var16 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4);
                        ++var17;
                    }
                    this.materialCost = var17;
                } else {
                    int var18;
                    int var20;
                    if (!(var15 || var12.getItem() == var13.getItem() && var12.isItemStackDamageable())) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    if (var12.isItemStackDamageable() && !var15) {
                        var16 = var8.getMaxDamage() - var8.getItemDamage();
                        int var17 = var13.getMaxDamage() - var13.getItemDamage();
                        var18 = var17 + var12.getMaxDamage() * 12 / 100;
                        int var19 = var16 + var18;
                        var20 = var12.getMaxDamage() - var19;
                        if (var20 < 0) {
                            var20 = 0;
                        }
                        if (var20 < var12.getMetadata()) {
                            var12.setItemDamage(var20);
                            var9 += 2;
                        }
                    }
                    Map var26 = EnchantmentHelper.getEnchantments(var13);
                    Iterator var27 = var26.keySet().iterator();
                    while (var27.hasNext()) {
                        var18 = (Integer)var27.next();
                        Enchantment var28 = Enchantment.func_180306_c(var18);
                        if (var28 == null) continue;
                        var20 = var14.containsKey(var18) ? (Integer)var14.get(var18) : 0;
                        int var21 = (Integer)var26.get(var18);
                        int var10000 = var20 == var21 ? ++var21 : Math.max(var21, var20);
                        var21 = var10000;
                        boolean var22 = var28.canApply(var8);
                        if (this.thePlayer.capabilities.isCreativeMode || var8.getItem() == Items.enchanted_book) {
                            var22 = true;
                        }
                        Iterator var23 = var14.keySet().iterator();
                        while (var23.hasNext()) {
                            int var24 = (Integer)var23.next();
                            if (var24 == var18 || var28.canApplyTogether(Enchantment.func_180306_c(var24))) continue;
                            var22 = false;
                            ++var9;
                        }
                        if (!var22) continue;
                        if (var21 > var28.getMaxLevel()) {
                            var21 = var28.getMaxLevel();
                        }
                        var14.put(var18, var21);
                        int var29 = 0;
                        switch (var28.getWeight()) {
                            case 1: {
                                var29 = 8;
                                break;
                            }
                            case 2: {
                                var29 = 4;
                            }
                            default: {
                                break;
                            }
                            case 5: {
                                var29 = 2;
                                break;
                            }
                            case 10: {
                                var29 = 1;
                            }
                        }
                        if (var15) {
                            var29 = Math.max(1, var29 / 2);
                        }
                        var9 += var29 * var21;
                    }
                }
            }
            if (StringUtils.isBlank((CharSequence)this.repairedItemName)) {
                if (var8.hasDisplayName()) {
                    var11 = 1;
                    var9 += var11;
                    var12.clearCustomName();
                }
            } else if (!this.repairedItemName.equals(var8.getDisplayName())) {
                var11 = 1;
                var9 += var11;
                var12.setStackDisplayName(this.repairedItemName);
            }
            this.maximumCost = var25 + var9;
            if (var9 <= 0) {
                var12 = null;
            }
            if (var11 == var9 && var11 > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }
            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
                var12 = null;
            }
            if (var12 != null) {
                var16 = var12.getRepairCost();
                if (var13 != null && var16 < var13.getRepairCost()) {
                    var16 = var13.getRepairCost();
                }
                var16 = var16 * 2 + 1;
                var12.setRepairCost(var16);
                EnchantmentHelper.setEnchantments(var14, var12);
            }
            this.outputSlot.setInventorySlotContents(0, var12);
            this.detectAndSendChanges();
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    @Override
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        if (p_75137_1_ == 0) {
            this.maximumCost = p_75137_2_;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.theWorld.isRemote) {
            int var2 = 0;
            while (var2 < this.inputSlots.getSizeInventory()) {
                ItemStack var3 = this.inputSlots.getStackInSlotOnClosing(var2);
                if (var3 != null) {
                    p_75134_1_.dropPlayerItemWithRandomChoice(var3, false);
                }
                ++var2;
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.theWorld.getBlockState(this.field_178156_j).getBlock() != Blocks.anvil ? false : playerIn.getDistanceSq((double)this.field_178156_j.getX() + 0.5, (double)this.field_178156_j.getY() + 0.5, (double)this.field_178156_j.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index == 2) {
                if (!this.mergeItemStack(var5, 3, 39, true)) {
                    return null;
                }
                var4.onSlotChange(var5, var3);
            } else if (index != 0 && index != 1 ? index >= 3 && index < 39 && !this.mergeItemStack(var5, 0, 2, false) : !this.mergeItemStack(var5, 3, 39, false)) {
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

    public void updateItemName(String p_82850_1_) {
        this.repairedItemName = p_82850_1_;
        if (this.getSlot(2).getHasStack()) {
            ItemStack var2 = this.getSlot(2).getStack();
            if (StringUtils.isBlank((CharSequence)p_82850_1_)) {
                var2.clearCustomName();
            } else {
                var2.setStackDisplayName(this.repairedItemName);
            }
        }
        this.updateRepairOutput();
    }

}

