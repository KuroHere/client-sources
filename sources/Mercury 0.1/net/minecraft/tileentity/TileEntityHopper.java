/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.tileentity;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class TileEntityHopper
extends TileEntityLockable
implements IHopper,
IUpdatePlayerListBox {
    private ItemStack[] inventory = new ItemStack[5];
    private String customName;
    private int transferCooldown = -1;
    private static final String __OBFID = "CL_00000359";

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.transferCooldown = compound.getInteger("TransferCooldown");
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");
            if (var5 < 0 || var5 >= this.inventory.length) continue;
            this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.inventory.length; ++var3) {
            if (this.inventory[var3] == null) continue;
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.inventory[var3].writeToNBT(var4);
            var2.appendTag(var4);
        }
        compound.setTag("Items", var2);
        compound.setInteger("TransferCooldown", this.transferCooldown);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return this.inventory[slotIn];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventory[index] != null) {
            if (this.inventory[index].stackSize <= count) {
                ItemStack var3 = this.inventory[index];
                this.inventory[index] = null;
                return var3;
            }
            ItemStack var3 = this.inventory[index].splitStack(count);
            if (this.inventory[index].stackSize == 0) {
                this.inventory[index] = null;
            }
            return var3;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.inventory[index] != null) {
            ItemStack var2 = this.inventory[index];
            this.inventory[index] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.hopper";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String customNameIn) {
        this.customName = customNameIn;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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
        return true;
    }

    @Override
    public void update() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            --this.transferCooldown;
            if (!this.isOnTransferCooldown()) {
                this.setTransferCooldown(0);
                this.func_145887_i();
            }
        }
    }

    public boolean func_145887_i() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (!this.isOnTransferCooldown() && BlockHopper.getActiveStateFromMetadata(this.getBlockMetadata())) {
                boolean var1 = false;
                if (!this.func_152104_k()) {
                    var1 = this.func_145883_k();
                }
                if (!this.func_152105_l()) {
                    boolean bl2 = var1 = TileEntityHopper.func_145891_a(this) || var1;
                }
                if (var1) {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean func_152104_k() {
        for (ItemStack var4 : this.inventory) {
            if (var4 == null) continue;
            return false;
        }
        return true;
    }

    private boolean func_152105_l() {
        for (ItemStack var4 : this.inventory) {
            if (var4 != null && var4.stackSize == var4.getMaxStackSize()) continue;
            return false;
        }
        return true;
    }

    private boolean func_145883_k() {
        IInventory var1 = this.func_145895_l();
        if (var1 == null) {
            return false;
        }
        EnumFacing var2 = BlockHopper.func_176428_b(this.getBlockMetadata()).getOpposite();
        if (this.func_174919_a(var1, var2)) {
            return false;
        }
        for (int var3 = 0; var3 < this.getSizeInventory(); ++var3) {
            if (this.getStackInSlot(var3) == null) continue;
            ItemStack var4 = this.getStackInSlot(var3).copy();
            ItemStack var5 = TileEntityHopper.func_174918_a(var1, this.decrStackSize(var3, 1), var2);
            if (var5 == null || var5.stackSize == 0) {
                var1.markDirty();
                return true;
            }
            this.setInventorySlotContents(var3, var4);
        }
        return false;
    }

    private boolean func_174919_a(IInventory p_174919_1_, EnumFacing p_174919_2_) {
        if (p_174919_1_ instanceof ISidedInventory) {
            ISidedInventory var3 = (ISidedInventory)p_174919_1_;
            int[] var4 = var3.getSlotsForFace(p_174919_2_);
            for (int var5 = 0; var5 < var4.length; ++var5) {
                ItemStack var6 = var3.getStackInSlot(var4[var5]);
                if (var6 != null && var6.stackSize == var6.getMaxStackSize()) continue;
                return false;
            }
        } else {
            int var7 = p_174919_1_.getSizeInventory();
            for (int var8 = 0; var8 < var7; ++var8) {
                ItemStack var9 = p_174919_1_.getStackInSlot(var8);
                if (var9 != null && var9.stackSize == var9.getMaxStackSize()) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean func_174917_b(IInventory p_174917_0_, EnumFacing p_174917_1_) {
        if (p_174917_0_ instanceof ISidedInventory) {
            ISidedInventory var2 = (ISidedInventory)p_174917_0_;
            int[] var3 = var2.getSlotsForFace(p_174917_1_);
            for (int var4 = 0; var4 < var3.length; ++var4) {
                if (var2.getStackInSlot(var3[var4]) == null) continue;
                return false;
            }
        } else {
            int var5 = p_174917_0_.getSizeInventory();
            for (int var6 = 0; var6 < var5; ++var6) {
                if (p_174917_0_.getStackInSlot(var6) == null) continue;
                return false;
            }
        }
        return true;
    }

    public static boolean func_145891_a(IHopper p_145891_0_) {
        IInventory var1 = TileEntityHopper.func_145884_b(p_145891_0_);
        if (var1 != null) {
            EnumFacing var2 = EnumFacing.DOWN;
            if (TileEntityHopper.func_174917_b(var1, var2)) {
                return false;
            }
            if (var1 instanceof ISidedInventory) {
                ISidedInventory var3 = (ISidedInventory)var1;
                int[] var4 = var3.getSlotsForFace(var2);
                for (int var5 = 0; var5 < var4.length; ++var5) {
                    if (!TileEntityHopper.func_174915_a(p_145891_0_, var1, var4[var5], var2)) continue;
                    return true;
                }
            } else {
                int var7 = var1.getSizeInventory();
                for (int var8 = 0; var8 < var7; ++var8) {
                    if (!TileEntityHopper.func_174915_a(p_145891_0_, var1, var8, var2)) continue;
                    return true;
                }
            }
        } else {
            EntityItem var6 = TileEntityHopper.func_145897_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0, p_145891_0_.getZPos());
            if (var6 != null) {
                return TileEntityHopper.func_145898_a(p_145891_0_, var6);
            }
        }
        return false;
    }

    private static boolean func_174915_a(IHopper p_174915_0_, IInventory p_174915_1_, int p_174915_2_, EnumFacing p_174915_3_) {
        ItemStack var4 = p_174915_1_.getStackInSlot(p_174915_2_);
        if (var4 != null && TileEntityHopper.func_174921_b(p_174915_1_, var4, p_174915_2_, p_174915_3_)) {
            ItemStack var5 = var4.copy();
            ItemStack var6 = TileEntityHopper.func_174918_a(p_174915_0_, p_174915_1_.decrStackSize(p_174915_2_, 1), null);
            if (var6 == null || var6.stackSize == 0) {
                p_174915_1_.markDirty();
                return true;
            }
            p_174915_1_.setInventorySlotContents(p_174915_2_, var5);
        }
        return false;
    }

    public static boolean func_145898_a(IInventory p_145898_0_, EntityItem p_145898_1_) {
        boolean var2 = false;
        if (p_145898_1_ == null) {
            return false;
        }
        ItemStack var3 = p_145898_1_.getEntityItem().copy();
        ItemStack var4 = TileEntityHopper.func_174918_a(p_145898_0_, var3, null);
        if (var4 != null && var4.stackSize != 0) {
            p_145898_1_.setEntityItemStack(var4);
        } else {
            var2 = true;
            p_145898_1_.setDead();
        }
        return var2;
    }

    public static ItemStack func_174918_a(IInventory p_174918_0_, ItemStack p_174918_1_, EnumFacing p_174918_2_) {
        if (p_174918_0_ instanceof ISidedInventory && p_174918_2_ != null) {
            ISidedInventory var6 = (ISidedInventory)p_174918_0_;
            int[] var7 = var6.getSlotsForFace(p_174918_2_);
            for (int var5 = 0; var5 < var7.length && p_174918_1_ != null && p_174918_1_.stackSize > 0; ++var5) {
                p_174918_1_ = TileEntityHopper.func_174916_c(p_174918_0_, p_174918_1_, var7[var5], p_174918_2_);
            }
        } else {
            int var3 = p_174918_0_.getSizeInventory();
            for (int var4 = 0; var4 < var3 && p_174918_1_ != null && p_174918_1_.stackSize > 0; ++var4) {
                p_174918_1_ = TileEntityHopper.func_174916_c(p_174918_0_, p_174918_1_, var4, p_174918_2_);
            }
        }
        if (p_174918_1_ != null && p_174918_1_.stackSize == 0) {
            p_174918_1_ = null;
        }
        return p_174918_1_;
    }

    private static boolean func_174920_a(IInventory p_174920_0_, ItemStack p_174920_1_, int p_174920_2_, EnumFacing p_174920_3_) {
        return !p_174920_0_.isItemValidForSlot(p_174920_2_, p_174920_1_) ? false : !(p_174920_0_ instanceof ISidedInventory) || ((ISidedInventory)p_174920_0_).canInsertItem(p_174920_2_, p_174920_1_, p_174920_3_);
    }

    private static boolean func_174921_b(IInventory p_174921_0_, ItemStack p_174921_1_, int p_174921_2_, EnumFacing p_174921_3_) {
        return !(p_174921_0_ instanceof ISidedInventory) || ((ISidedInventory)p_174921_0_).canExtractItem(p_174921_2_, p_174921_1_, p_174921_3_);
    }

    private static ItemStack func_174916_c(IInventory p_174916_0_, ItemStack p_174916_1_, int p_174916_2_, EnumFacing p_174916_3_) {
        ItemStack var4 = p_174916_0_.getStackInSlot(p_174916_2_);
        if (TileEntityHopper.func_174920_a(p_174916_0_, p_174916_1_, p_174916_2_, p_174916_3_)) {
            boolean var5 = false;
            if (var4 == null) {
                p_174916_0_.setInventorySlotContents(p_174916_2_, p_174916_1_);
                p_174916_1_ = null;
                var5 = true;
            } else if (TileEntityHopper.canCombine(var4, p_174916_1_)) {
                int var6 = p_174916_1_.getMaxStackSize() - var4.stackSize;
                int var7 = Math.min(p_174916_1_.stackSize, var6);
                p_174916_1_.stackSize -= var7;
                var4.stackSize += var7;
                boolean bl2 = var5 = var7 > 0;
            }
            if (var5) {
                if (p_174916_0_ instanceof TileEntityHopper) {
                    TileEntityHopper var8 = (TileEntityHopper)p_174916_0_;
                    if (var8.mayTransfer()) {
                        var8.setTransferCooldown(8);
                    }
                    p_174916_0_.markDirty();
                }
                p_174916_0_.markDirty();
            }
        }
        return p_174916_1_;
    }

    private IInventory func_145895_l() {
        EnumFacing var1 = BlockHopper.func_176428_b(this.getBlockMetadata());
        return TileEntityHopper.func_145893_b(this.getWorld(), this.pos.getX() + var1.getFrontOffsetX(), this.pos.getY() + var1.getFrontOffsetY(), this.pos.getZ() + var1.getFrontOffsetZ());
    }

    public static IInventory func_145884_b(IHopper p_145884_0_) {
        return TileEntityHopper.func_145893_b(p_145884_0_.getWorld(), p_145884_0_.getXPos(), p_145884_0_.getYPos() + 1.0, p_145884_0_.getZPos());
    }

    public static EntityItem func_145897_a(World worldIn, double p_145897_1_, double p_145897_3_, double p_145897_5_) {
        List var7 = worldIn.func_175647_a(EntityItem.class, new AxisAlignedBB(p_145897_1_, p_145897_3_, p_145897_5_, p_145897_1_ + 1.0, p_145897_3_ + 1.0, p_145897_5_ + 1.0), IEntitySelector.selectAnything);
        return var7.size() > 0 ? (EntityItem)var7.get(0) : null;
    }

    public static IInventory func_145893_b(World worldIn, double p_145893_1_, double p_145893_3_, double p_145893_5_) {
        List var14;
        Block var13;
        IInventory var7 = null;
        int var8 = MathHelper.floor_double(p_145893_1_);
        int var9 = MathHelper.floor_double(p_145893_3_);
        int var10 = MathHelper.floor_double(p_145893_5_);
        BlockPos var11 = new BlockPos(var8, var9, var10);
        TileEntity var12 = worldIn.getTileEntity(new BlockPos(var8, var9, var10));
        if (var12 instanceof IInventory && (var7 = (IInventory)((Object)var12)) instanceof TileEntityChest && (var13 = worldIn.getBlockState(new BlockPos(var8, var9, var10)).getBlock()) instanceof BlockChest) {
            var7 = ((BlockChest)var13).getLockableContainer(worldIn, var11);
        }
        if (var7 == null && (var14 = worldIn.func_175674_a(null, new AxisAlignedBB(p_145893_1_, p_145893_3_, p_145893_5_, p_145893_1_ + 1.0, p_145893_3_ + 1.0, p_145893_5_ + 1.0), IEntitySelector.selectInventories)).size() > 0) {
            var7 = (IInventory)var14.get(worldIn.rand.nextInt(var14.size()));
        }
        return var7;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.stackSize > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }

    @Override
    public double getXPos() {
        return this.pos.getX();
    }

    @Override
    public double getYPos() {
        return this.pos.getY();
    }

    @Override
    public double getZPos() {
        return this.pos.getZ();
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown <= 1;
    }

    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerHopper(playerInventory, this, playerIn);
    }

    @Override
    public int getField(int id2) {
        return 0;
    }

    @Override
    public void setField(int id2, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clearInventory() {
        for (int var1 = 0; var1 < this.inventory.length; ++var1) {
            this.inventory[var1] = null;
        }
    }
}

