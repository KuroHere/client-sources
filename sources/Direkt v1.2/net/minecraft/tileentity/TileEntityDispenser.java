package net.minecraft.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDispenser extends TileEntityLockableLoot implements IInventory {
	private static final Random RNG = new Random();
	private ItemStack[] stacks = new ItemStack[9];
	protected String customName;

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return 9;
	}

	@Override
	@Nullable

	/**
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int index) {
		this.fillWithLoot((EntityPlayer) null);
		return this.stacks[index];
	}

	@Override
	@Nullable

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int index, int count) {
		this.fillWithLoot((EntityPlayer) null);
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);

		if (itemstack != null) {
			this.markDirty();
		}

		return itemstack;
	}

	@Override
	@Nullable

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int index) {
		this.fillWithLoot((EntityPlayer) null);
		return ItemStackHelper.getAndRemove(this.stacks, index);
	}

	public int getDispenseSlot() {
		this.fillWithLoot((EntityPlayer) null);
		int i = -1;
		int j = 1;

		for (int k = 0; k < this.stacks.length; ++k) {
			if ((this.stacks[k] != null) && (RNG.nextInt(j++) == 0)) {
				i = k;
			}
		}

		return i;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		this.fillWithLoot((EntityPlayer) null);
		this.stacks[index] = stack;

		if ((stack != null) && (stack.stackSize > this.getInventoryStackLimit())) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	/**
	 * Add the given ItemStack to this Dispenser. Return the Slot the Item was placed in or -1 if no free slot is available.
	 */
	public int addItemStack(ItemStack stack) {
		for (int i = 0; i < this.stacks.length; ++i) {
			if ((this.stacks[i] == null) || (this.stacks[i].getItem() == null)) {
				this.setInventorySlotContents(i, stack);
				return i;
			}
		}

		return -1;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.dispenser";
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return this.customName != null;
	}

	public static void func_189678_a(DataFixer p_189678_0_) {
		p_189678_0_.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists("Trap", new String[] { "Items" }));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (!this.checkLootAndRead(compound)) {
			NBTTagList nbttaglist = compound.getTagList("Items", 10);
			this.stacks = new ItemStack[this.getSizeInventory()];

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				int j = nbttagcompound.getByte("Slot") & 255;

				if ((j >= 0) && (j < this.stacks.length)) {
					this.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
				}
			}
		}

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
	}

	@Override
	public NBTTagCompound func_189515_b(NBTTagCompound p_189515_1_) {
		super.func_189515_b(p_189515_1_);

		if (!this.checkLootAndWrite(p_189515_1_)) {
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < this.stacks.length; ++i) {
				if (this.stacks[i] != null) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setByte("Slot", (byte) i);
					this.stacks[i].writeToNBT(nbttagcompound);
					nbttaglist.appendTag(nbttagcompound);
				}
			}

			p_189515_1_.setTag("Items", nbttaglist);
		}

		if (this.hasCustomName()) {
			p_189515_1_.setString("CustomName", this.customName);
		}

		return p_189515_1_;
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public String getGuiID() {
		return "minecraft:dispenser";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		this.fillWithLoot(playerIn);
		return new ContainerDispenser(playerInventory, this);
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
	public void clear() {
		this.fillWithLoot((EntityPlayer) null);

		for (int i = 0; i < this.stacks.length; ++i) {
			this.stacks[i] = null;
		}
	}
}
