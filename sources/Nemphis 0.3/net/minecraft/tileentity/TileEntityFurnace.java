/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityFurnace
extends TileEntityLockable
implements IUpdatePlayerListBox,
ISidedInventory {
    private static final int[] slotsTop = new int[1];
    private static final int[] slotsBottom = new int[]{2, 1};
    private static final int[] slotsSides = new int[]{1};
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int field_174906_k;
    private int field_174905_l;
    private String furnaceCustomName;
    private static final String __OBFID = "CL_00000357";

    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return this.furnaceItemStacks[slotIn];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.furnaceItemStacks[index] != null) {
            if (this.furnaceItemStacks[index].stackSize <= count) {
                ItemStack var3 = this.furnaceItemStacks[index];
                this.furnaceItemStacks[index] = null;
                return var3;
            }
            ItemStack var3 = this.furnaceItemStacks[index].splitStack(count);
            if (this.furnaceItemStacks[index].stackSize == 0) {
                this.furnaceItemStacks[index] = null;
            }
            return var3;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.furnaceItemStacks[index] != null) {
            ItemStack var2 = this.furnaceItemStacks[index];
            this.furnaceItemStacks[index] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        boolean var3 = stack != null && stack.isItemEqual(this.furnaceItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks[index]);
        this.furnaceItemStacks[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        if (index == 0 && !var3) {
            this.field_174905_l = this.func_174904_a(stack);
            this.field_174906_k = 0;
            this.markDirty();
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }

    @Override
    public boolean hasCustomName() {
        if (this.furnaceCustomName != null && this.furnaceCustomName.length() > 0) {
            return true;
        }
        return false;
    }

    public void setCustomInventoryName(String p_145951_1_) {
        this.furnaceCustomName = p_145951_1_;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
        int var3 = 0;
        while (var3 < var2.tagCount()) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");
            if (var5 >= 0 && var5 < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
            ++var3;
        }
        this.furnaceBurnTime = compound.getShort("BurnTime");
        this.field_174906_k = compound.getShort("CookTime");
        this.field_174905_l = compound.getShort("CookTimeTotal");
        this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
        if (compound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)this.furnaceBurnTime);
        compound.setShort("CookTime", (short)this.field_174906_k);
        compound.setShort("CookTimeTotal", (short)this.field_174905_l);
        NBTTagList var2 = new NBTTagList();
        int var3 = 0;
        while (var3 < this.furnaceItemStacks.length) {
            if (this.furnaceItemStacks[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.furnaceItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
            ++var3;
        }
        compound.setTag("Items", var2);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.furnaceCustomName);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isBurning() {
        if (this.furnaceBurnTime > 0) {
            return true;
        }
        return false;
    }

    public static boolean func_174903_a(IInventory p_174903_0_) {
        if (p_174903_0_.getField(0) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        boolean var1 = this.isBurning();
        boolean var2 = false;
        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }
        if (!this.worldObj.isRemote) {
            if (!(this.isBurning() || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null)) {
                if (!this.isBurning() && this.field_174906_k > 0) {
                    this.field_174906_k = MathHelper.clamp_int(this.field_174906_k - 2, 0, this.field_174905_l);
                }
            } else {
                if (!this.isBurning() && this.canSmelt()) {
                    this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
                    if (this.isBurning()) {
                        var2 = true;
                        if (this.furnaceItemStacks[1] != null) {
                            --this.furnaceItemStacks[1].stackSize;
                            if (this.furnaceItemStacks[1].stackSize == 0) {
                                Item var3 = this.furnaceItemStacks[1].getItem().getContainerItem();
                                ItemStack itemStack = this.furnaceItemStacks[1] = var3 != null ? new ItemStack(var3) : null;
                            }
                        }
                    }
                }
                if (this.isBurning() && this.canSmelt()) {
                    ++this.field_174906_k;
                    if (this.field_174906_k == this.field_174905_l) {
                        this.field_174906_k = 0;
                        this.field_174905_l = this.func_174904_a(this.furnaceItemStacks[0]);
                        this.smeltItem();
                        var2 = true;
                    }
                } else {
                    this.field_174906_k = 0;
                }
            }
            if (var1 != this.isBurning()) {
                var2 = true;
                BlockFurnace.func_176446_a(this.isBurning(), this.worldObj, this.pos);
            }
        }
        if (var2) {
            this.markDirty();
        }
    }

    public int func_174904_a(ItemStack p_174904_1_) {
        return 200;
    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        }
        ItemStack var1 = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
        return var1 == null ? false : (this.furnaceItemStacks[2] == null ? true : (!this.furnaceItemStacks[2].isItemEqual(var1) ? false : (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit() && this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2].getMaxStackSize() ? true : this.furnaceItemStacks[2].stackSize < var1.getMaxStackSize())));
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack var1 = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
            if (this.furnaceItemStacks[2] == null) {
                this.furnaceItemStacks[2] = var1.copy();
            } else if (this.furnaceItemStacks[2].getItem() == var1.getItem()) {
                ++this.furnaceItemStacks[2].stackSize;
            }
            if (this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && this.furnaceItemStacks[0].getMetadata() == 1 && this.furnaceItemStacks[1] != null && this.furnaceItemStacks[1].getItem() == Items.bucket) {
                this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
            }
            --this.furnaceItemStacks[0].stackSize;
            if (this.furnaceItemStacks[0].stackSize <= 0) {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

    public static int getItemBurnTime(ItemStack p_145952_0_) {
        if (p_145952_0_ == null) {
            return 0;
        }
        Item var1 = p_145952_0_.getItem();
        if (var1 instanceof ItemBlock && Block.getBlockFromItem(var1) != Blocks.air) {
            Block var2 = Block.getBlockFromItem(var1);
            if (var2 == Blocks.wooden_slab) {
                return 150;
            }
            if (var2.getMaterial() == Material.wood) {
                return 300;
            }
            if (var2 == Blocks.coal_block) {
                return 16000;
            }
        }
        return var1 instanceof ItemTool && ((ItemTool)var1).getToolMaterialName().equals("WOOD") ? 200 : (var1 instanceof ItemSword && ((ItemSword)var1).getToolMaterialName().equals("WOOD") ? 200 : (var1 instanceof ItemHoe && ((ItemHoe)var1).getMaterialName().equals("WOOD") ? 200 : (var1 == Items.stick ? 100 : (var1 == Items.coal ? 1600 : (var1 == Items.lava_bucket ? 20000 : (var1 == Item.getItemFromBlock(Blocks.sapling) ? 100 : (var1 == Items.blaze_rod ? 2400 : 0)))))));
    }

    public static boolean isItemFuel(ItemStack p_145954_0_) {
        if (TileEntityFurnace.getItemBurnTime(p_145954_0_) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : playerIn.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 2 ? false : (index != 1 ? true : TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.func_178173_c_(stack));
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
    }

    @Override
    public boolean canInsertItem(int slotIn, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(slotIn, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int slotId, ItemStack stack, EnumFacing direction) {
        Item var4;
        if (direction == EnumFacing.DOWN && slotId == 1 && (var4 = stack.getItem()) != Items.water_bucket && var4 != Items.bucket) {
            return false;
        }
        return true;
    }

    @Override
    public String getGuiID() {
        return "minecraft:furnace";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerFurnace(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: {
                return this.furnaceBurnTime;
            }
            case 1: {
                return this.currentItemBurnTime;
            }
            case 2: {
                return this.field_174906_k;
            }
            case 3: {
                return this.field_174905_l;
            }
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: {
                this.furnaceBurnTime = value;
                break;
            }
            case 1: {
                this.currentItemBurnTime = value;
                break;
            }
            case 2: {
                this.field_174906_k = value;
                break;
            }
            case 3: {
                this.field_174905_l = value;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clearInventory() {
        int var1 = 0;
        while (var1 < this.furnaceItemStacks.length) {
            this.furnaceItemStacks[var1] = null;
            ++var1;
        }
    }
}

