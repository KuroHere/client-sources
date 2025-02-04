package net.minecraft.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable {
	/**
	 * Returns the number of slots in the inventory.
	 */
	int getSizeInventory();

	@Nullable

	/**
	 * Returns the stack in the given slot.
	 */
	ItemStack getStackInSlot(int index);

	@Nullable

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	ItemStack decrStackSize(int index, int count);

	@Nullable

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	ItemStack removeStackFromSlot(int index);

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	void setInventorySlotContents(int index, ItemStack stack);

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	int getInventoryStackLimit();

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it hasn't changed and skip it.
	 */
	void markDirty();

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	boolean isUseableByPlayer(EntityPlayer player);

	void openInventory(EntityPlayer player);

	void closeInventory(EntityPlayer player);

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	boolean isItemValidForSlot(int index, ItemStack stack);

	int getField(int id);

	void setField(int id, int value);

	int getFieldCount();

	void clear();
}
