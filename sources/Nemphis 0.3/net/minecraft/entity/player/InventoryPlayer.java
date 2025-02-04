/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity.player;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public class InventoryPlayer
implements IInventory {
    public ItemStack[] mainInventory = new ItemStack[36];
    public ItemStack[] armorInventory = new ItemStack[4];
    public int currentItem;
    public EntityPlayer player;
    private ItemStack itemStack;
    public boolean inventoryChanged;
    private static final String __OBFID = "CL_00001709";

    public InventoryPlayer(EntityPlayer p_i1750_1_) {
        this.player = p_i1750_1_;
    }

    public ItemStack getCurrentItem() {
        return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
    }

    public static int getHotbarSize() {
        return 9;
    }

    private int getInventorySlotContainItem(Item itemIn) {
        int var2 = 0;
        while (var2 < this.mainInventory.length) {
            if (this.mainInventory[var2] != null && this.mainInventory[var2].getItem() == itemIn) {
                return var2;
            }
            ++var2;
        }
        return -1;
    }

    private int getInventorySlotContainItemAndDamage(Item p_146024_1_, int p_146024_2_) {
        int var3 = 0;
        while (var3 < this.mainInventory.length) {
            if (this.mainInventory[var3] != null && this.mainInventory[var3].getItem() == p_146024_1_ && this.mainInventory[var3].getMetadata() == p_146024_2_) {
                return var3;
            }
            ++var3;
        }
        return -1;
    }

    private int storeItemStack(ItemStack p_70432_1_) {
        int var2 = 0;
        while (var2 < this.mainInventory.length) {
            if (this.mainInventory[var2] != null && this.mainInventory[var2].getItem() == p_70432_1_.getItem() && this.mainInventory[var2].isStackable() && this.mainInventory[var2].stackSize < this.mainInventory[var2].getMaxStackSize() && this.mainInventory[var2].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[var2].getHasSubtypes() || this.mainInventory[var2].getMetadata() == p_70432_1_.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[var2], p_70432_1_)) {
                return var2;
            }
            ++var2;
        }
        return -1;
    }

    public int getFirstEmptyStack() {
        int var1 = 0;
        while (var1 < this.mainInventory.length) {
            if (this.mainInventory[var1] == null) {
                return var1;
            }
            ++var1;
        }
        return -1;
    }

    public void setCurrentItem(Item p_146030_1_, int p_146030_2_, boolean p_146030_3_, boolean p_146030_4_) {
        int var6;
        ItemStack var5 = this.getCurrentItem();
        int n = var6 = p_146030_3_ ? this.getInventorySlotContainItemAndDamage(p_146030_1_, p_146030_2_) : this.getInventorySlotContainItem(p_146030_1_);
        if (var6 >= 0 && var6 < 9) {
            this.currentItem = var6;
        } else if (p_146030_4_ && p_146030_1_ != null) {
            int var7 = this.getFirstEmptyStack();
            if (var7 >= 0 && var7 < 9) {
                this.currentItem = var7;
            }
            if (var5 == null || !var5.isItemEnchantable() || this.getInventorySlotContainItemAndDamage(var5.getItem(), var5.getItemDamage()) != this.currentItem) {
                int var9;
                int var8 = this.getInventorySlotContainItemAndDamage(p_146030_1_, p_146030_2_);
                if (var8 >= 0) {
                    var9 = this.mainInventory[var8].stackSize;
                    this.mainInventory[var8] = this.mainInventory[this.currentItem];
                } else {
                    var9 = 1;
                }
                this.mainInventory[this.currentItem] = new ItemStack(p_146030_1_, var9, p_146030_2_);
            }
        }
    }

    public void changeCurrentItem(int p_70453_1_) {
        if (p_70453_1_ > 0) {
            p_70453_1_ = 1;
        }
        if (p_70453_1_ < 0) {
            p_70453_1_ = -1;
        }
        this.currentItem -= p_70453_1_;
        while (this.currentItem < 0) {
            this.currentItem += 9;
        }
        while (this.currentItem >= 9) {
            this.currentItem -= 9;
        }
    }

    public int func_174925_a(Item p_174925_1_, int p_174925_2_, int p_174925_3_, NBTTagCompound p_174925_4_) {
        ItemStack var7;
        int var8;
        int var5 = 0;
        int var6 = 0;
        while (var6 < this.mainInventory.length) {
            var7 = this.mainInventory[var6];
            if (!(var7 == null || p_174925_1_ != null && var7.getItem() != p_174925_1_ || p_174925_2_ > -1 && var7.getMetadata() != p_174925_2_ || p_174925_4_ != null && !CommandTestForBlock.func_175775_a(p_174925_4_, var7.getTagCompound(), true))) {
                var8 = p_174925_3_ <= 0 ? var7.stackSize : Math.min(p_174925_3_ - var5, var7.stackSize);
                var5 += var8;
                if (p_174925_3_ != 0) {
                    this.mainInventory[var6].stackSize -= var8;
                    if (this.mainInventory[var6].stackSize == 0) {
                        this.mainInventory[var6] = null;
                    }
                    if (p_174925_3_ > 0 && var5 >= p_174925_3_) {
                        return var5;
                    }
                }
            }
            ++var6;
        }
        var6 = 0;
        while (var6 < this.armorInventory.length) {
            var7 = this.armorInventory[var6];
            if (!(var7 == null || p_174925_1_ != null && var7.getItem() != p_174925_1_ || p_174925_2_ > -1 && var7.getMetadata() != p_174925_2_ || p_174925_4_ != null && !CommandTestForBlock.func_175775_a(p_174925_4_, var7.getTagCompound(), false))) {
                var8 = p_174925_3_ <= 0 ? var7.stackSize : Math.min(p_174925_3_ - var5, var7.stackSize);
                var5 += var8;
                if (p_174925_3_ != 0) {
                    this.armorInventory[var6].stackSize -= var8;
                    if (this.armorInventory[var6].stackSize == 0) {
                        this.armorInventory[var6] = null;
                    }
                    if (p_174925_3_ > 0 && var5 >= p_174925_3_) {
                        return var5;
                    }
                }
            }
            ++var6;
        }
        if (this.itemStack != null) {
            if (p_174925_1_ != null && this.itemStack.getItem() != p_174925_1_) {
                return var5;
            }
            if (p_174925_2_ > -1 && this.itemStack.getMetadata() != p_174925_2_) {
                return var5;
            }
            if (p_174925_4_ != null && !CommandTestForBlock.func_175775_a(p_174925_4_, this.itemStack.getTagCompound(), false)) {
                return var5;
            }
            var6 = p_174925_3_ <= 0 ? this.itemStack.stackSize : Math.min(p_174925_3_ - var5, this.itemStack.stackSize);
            var5 += var6;
            if (p_174925_3_ != 0) {
                this.itemStack.stackSize -= var6;
                if (this.itemStack.stackSize == 0) {
                    this.itemStack = null;
                }
                if (p_174925_3_ > 0 && var5 >= p_174925_3_) {
                    return var5;
                }
            }
        }
        return var5;
    }

    private int storePartialItemStack(ItemStack p_70452_1_) {
        Item var2 = p_70452_1_.getItem();
        int var3 = p_70452_1_.stackSize;
        int var4 = this.storeItemStack(p_70452_1_);
        if (var4 < 0) {
            var4 = this.getFirstEmptyStack();
        }
        if (var4 < 0) {
            return var3;
        }
        if (this.mainInventory[var4] == null) {
            this.mainInventory[var4] = new ItemStack(var2, 0, p_70452_1_.getMetadata());
            if (p_70452_1_.hasTagCompound()) {
                this.mainInventory[var4].setTagCompound((NBTTagCompound)p_70452_1_.getTagCompound().copy());
            }
        }
        int var5 = var3;
        if (var3 > this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize) {
            var5 = this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize;
        }
        if (var5 > this.getInventoryStackLimit() - this.mainInventory[var4].stackSize) {
            var5 = this.getInventoryStackLimit() - this.mainInventory[var4].stackSize;
        }
        if (var5 == 0) {
            return var3;
        }
        this.mainInventory[var4].stackSize += var5;
        this.mainInventory[var4].animationsToGo = 5;
        return var3 -= var5;
    }

    public void decrementAnimations() {
        int var1 = 0;
        while (var1 < this.mainInventory.length) {
            if (this.mainInventory[var1] != null) {
                this.mainInventory[var1].updateAnimation(this.player.worldObj, this.player, var1, this.currentItem == var1);
            }
            ++var1;
        }
    }

    public boolean consumeInventoryItem(Item p_146026_1_) {
        int var2 = this.getInventorySlotContainItem(p_146026_1_);
        if (var2 < 0) {
            return false;
        }
        if (--this.mainInventory[var2].stackSize <= 0) {
            this.mainInventory[var2] = null;
        }
        return true;
    }

    public boolean hasItem(Item p_146028_1_) {
        int var2 = this.getInventorySlotContainItem(p_146028_1_);
        if (var2 >= 0) {
            return true;
        }
        return false;
    }

    public boolean addItemStackToInventory(final ItemStack p_70441_1_) {
        if (p_70441_1_ != null && p_70441_1_.stackSize != 0 && p_70441_1_.getItem() != null) {
            int var2;
            block11 : {
                block9 : {
                    block10 : {
                        if (!p_70441_1_.isItemDamaged()) break block9;
                        var2 = this.getFirstEmptyStack();
                        if (var2 < 0) break block10;
                        this.mainInventory[var2] = ItemStack.copyItemStack(p_70441_1_);
                        this.mainInventory[var2].animationsToGo = 5;
                        p_70441_1_.stackSize = 0;
                        return true;
                    }
                    if (this.player.capabilities.isCreativeMode) {
                        p_70441_1_.stackSize = 0;
                        return true;
                    }
                    return false;
                }
                try {
                    do {
                        var2 = p_70441_1_.stackSize;
                        p_70441_1_.stackSize = this.storePartialItemStack(p_70441_1_);
                    } while (p_70441_1_.stackSize > 0 && p_70441_1_.stackSize < var2);
                    if (p_70441_1_.stackSize != var2 || !this.player.capabilities.isCreativeMode) break block11;
                    p_70441_1_.stackSize = 0;
                    return true;
                }
                catch (Throwable var5) {
                    CrashReport var3 = CrashReport.makeCrashReport(var5, "Adding item to inventory");
                    CrashReportCategory var4 = var3.makeCategory("Item being added");
                    var4.addCrashSection("Item ID", Item.getIdFromItem(p_70441_1_.getItem()));
                    var4.addCrashSection("Item data", p_70441_1_.getMetadata());
                    var4.addCrashSectionCallable("Item name", new Callable(){
                        private static final String __OBFID = "CL_00001710";

                        public String call() {
                            return p_70441_1_.getDisplayName();
                        }
                    });
                    throw new ReportedException(var3);
                }
            }
            if (p_70441_1_.stackSize < var2) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack[] var3 = this.mainInventory;
        if (index >= this.mainInventory.length) {
            var3 = this.armorInventory;
            index -= this.mainInventory.length;
        }
        if (var3[index] != null) {
            if (var3[index].stackSize <= count) {
                ItemStack var4 = var3[index];
                var3[index] = null;
                return var4;
            }
            ItemStack var4 = var3[index].splitStack(count);
            if (var3[index].stackSize == 0) {
                var3[index] = null;
            }
            return var4;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack[] var2 = this.mainInventory;
        if (index >= this.mainInventory.length) {
            var2 = this.armorInventory;
            index -= this.mainInventory.length;
        }
        if (var2[index] != null) {
            ItemStack var3 = var2[index];
            var2[index] = null;
            return var3;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack[] var3 = this.mainInventory;
        if (index >= var3.length) {
            index -= var3.length;
            var3 = this.armorInventory;
        }
        var3[index] = stack;
    }

    public float getStrVsBlock(Block p_146023_1_) {
        float var2 = 1.0f;
        if (this.mainInventory[this.currentItem] != null) {
            var2 *= this.mainInventory[this.currentItem].getStrVsBlock(p_146023_1_);
        }
        return var2;
    }

    public NBTTagList writeToNBT(NBTTagList p_70442_1_) {
        NBTTagCompound var3;
        int var2 = 0;
        while (var2 < this.mainInventory.length) {
            if (this.mainInventory[var2] != null) {
                var3 = new NBTTagCompound();
                var3.setByte("Slot", (byte)var2);
                this.mainInventory[var2].writeToNBT(var3);
                p_70442_1_.appendTag(var3);
            }
            ++var2;
        }
        var2 = 0;
        while (var2 < this.armorInventory.length) {
            if (this.armorInventory[var2] != null) {
                var3 = new NBTTagCompound();
                var3.setByte("Slot", (byte)(var2 + 100));
                this.armorInventory[var2].writeToNBT(var3);
                p_70442_1_.appendTag(var3);
            }
            ++var2;
        }
        return p_70442_1_;
    }

    public void readFromNBT(NBTTagList p_70443_1_) {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
        int var2 = 0;
        while (var2 < p_70443_1_.tagCount()) {
            NBTTagCompound var3 = p_70443_1_.getCompoundTagAt(var2);
            int var4 = var3.getByte("Slot") & 255;
            ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);
            if (var5 != null) {
                if (var4 >= 0 && var4 < this.mainInventory.length) {
                    this.mainInventory[var4] = var5;
                }
                if (var4 >= 100 && var4 < this.armorInventory.length + 100) {
                    this.armorInventory[var4 - 100] = var5;
                }
            }
            ++var2;
        }
    }

    @Override
    public int getSizeInventory() {
        return this.mainInventory.length + 4;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        ItemStack[] var2 = this.mainInventory;
        if (slotIn >= var2.length) {
            slotIn -= var2.length;
            var2 = this.armorInventory;
        }
        return var2[slotIn];
    }

    @Override
    public String getName() {
        return "container.inventory";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean func_146025_b(Block p_146025_1_) {
        if (p_146025_1_.getMaterial().isToolNotRequired()) {
            return true;
        }
        ItemStack var2 = this.getStackInSlot(this.currentItem);
        return var2 != null ? var2.canHarvestBlock(p_146025_1_) : false;
    }

    public ItemStack armorItemInSlot(int p_70440_1_) {
        return this.armorInventory[p_70440_1_];
    }

    public int getTotalArmorValue() {
        int var1 = 0;
        int var2 = 0;
        while (var2 < this.armorInventory.length) {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
                int var3 = ((ItemArmor)this.armorInventory[var2].getItem()).damageReduceAmount;
                var1 += var3;
            }
            ++var2;
        }
        return var1;
    }

    public void damageArmor(float p_70449_1_) {
        if ((p_70449_1_ /= 4.0f) < 1.0f) {
            p_70449_1_ = 1.0f;
        }
        int var2 = 0;
        while (var2 < this.armorInventory.length) {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
                this.armorInventory[var2].damageItem((int)p_70449_1_, this.player);
                if (this.armorInventory[var2].stackSize == 0) {
                    this.armorInventory[var2] = null;
                }
            }
            ++var2;
        }
    }

    public void dropAllItems() {
        int var1 = 0;
        while (var1 < this.mainInventory.length) {
            if (this.mainInventory[var1] != null) {
                this.player.func_146097_a(this.mainInventory[var1], true, false);
                this.mainInventory[var1] = null;
            }
            ++var1;
        }
        var1 = 0;
        while (var1 < this.armorInventory.length) {
            if (this.armorInventory[var1] != null) {
                this.player.func_146097_a(this.armorInventory[var1], true, false);
                this.armorInventory[var1] = null;
            }
            ++var1;
        }
    }

    @Override
    public void markDirty() {
        this.inventoryChanged = true;
    }

    public void setItemStack(ItemStack p_70437_1_) {
        this.itemStack = p_70437_1_;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.player.isDead ? false : playerIn.getDistanceSqToEntity(this.player) <= 64.0;
    }

    public boolean hasItemStack(ItemStack p_70431_1_) {
        int var2 = 0;
        while (var2 < this.armorInventory.length) {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].isItemEqual(p_70431_1_)) {
                return true;
            }
            ++var2;
        }
        var2 = 0;
        while (var2 < this.mainInventory.length) {
            if (this.mainInventory[var2] != null && this.mainInventory[var2].isItemEqual(p_70431_1_)) {
                return true;
            }
            ++var2;
        }
        return false;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    public void copyInventory(InventoryPlayer p_70455_1_) {
        int var2 = 0;
        while (var2 < this.mainInventory.length) {
            this.mainInventory[var2] = ItemStack.copyItemStack(p_70455_1_.mainInventory[var2]);
            ++var2;
        }
        var2 = 0;
        while (var2 < this.armorInventory.length) {
            this.armorInventory[var2] = ItemStack.copyItemStack(p_70455_1_.armorInventory[var2]);
            ++var2;
        }
        this.currentItem = p_70455_1_.currentItem;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clearInventory() {
        int var1 = 0;
        while (var1 < this.mainInventory.length) {
            this.mainInventory[var1] = null;
            ++var1;
        }
        var1 = 0;
        while (var1 < this.armorInventory.length) {
            this.armorInventory[var1] = null;
            ++var1;
        }
    }

}

