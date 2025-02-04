package net.minecraft.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.math.MathHelper;

public class SlotFurnaceOutput extends Slot {
	/** The player that is using the GUI where this slot resides. */
	private final EntityPlayer thePlayer;
	private int removeCount;

	public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.thePlayer = player;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(@Nullable ItemStack stack) {
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.removeCount += Math.min(amount, this.getStack().stackSize);
		}

		return super.decrStackSize(amount);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
		this.onCrafting(stack);
		super.onPickupFromSlot(playerIn, stack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an internal count then calls onCrafting(item).
	 */
	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafting(stack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.removeCount);

		if (!this.thePlayer.worldObj.isRemote) {
			int i = this.removeCount;
			float f = FurnaceRecipes.instance().getSmeltingExperience(stack);

			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				int j = MathHelper.floor_float(i * f);

				if ((j < MathHelper.ceiling_float_int(i * f)) && (Math.random() < (i * f) - j)) {
					++j;
				}

				i = j;
			}

			while (i > 0) {
				int k = EntityXPOrb.getXPSplit(i);
				i -= k;
				this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, k));
			}
		}

		this.removeCount = 0;

		if (stack.getItem() == Items.IRON_INGOT) {
			this.thePlayer.addStat(AchievementList.ACQUIRE_IRON);
		}

		if (stack.getItem() == Items.COOKED_FISH) {
			this.thePlayer.addStat(AchievementList.COOK_FISH);
		}
	}
}
