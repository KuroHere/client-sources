package net.minecraft.item.crafting;

import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

public class ShieldRecipes {
	public void addRecipes(CraftingManager manager) {
		manager.addRecipe(new ItemStack(Items.SHIELD), new Object[] { "WoW", "WWW", " W ", 'W', Blocks.PLANKS, 'o', Items.IRON_INGOT });
		manager.addRecipe(new ShieldRecipes.Decoration());
	}

	static class Decoration implements IRecipe {
		private Decoration() {
		}

		@Override
		public boolean matches(InventoryCrafting inv, World worldIn) {
			ItemStack itemstack = null;
			ItemStack itemstack1 = null;

			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack itemstack2 = inv.getStackInSlot(i);

				if (itemstack2 != null) {
					if (itemstack2.getItem() == Items.BANNER) {
						if (itemstack1 != null) { return false; }

						itemstack1 = itemstack2;
					} else {
						if (itemstack2.getItem() != Items.SHIELD) { return false; }

						if (itemstack != null) { return false; }

						if (itemstack2.getSubCompound("BlockEntityTag", false) != null) { return false; }

						itemstack = itemstack2;
					}
				}
			}

			if ((itemstack != null) && (itemstack1 != null)) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		@Nullable
		public ItemStack getCraftingResult(InventoryCrafting inv) {
			ItemStack itemstack = null;

			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack itemstack1 = inv.getStackInSlot(i);

				if ((itemstack1 != null) && (itemstack1.getItem() == Items.BANNER)) {
					itemstack = itemstack1;
				}
			}

			ItemStack itemstack2 = new ItemStack(Items.SHIELD, 1, 0);
			EnumDyeColor enumdyecolor;
			NBTTagCompound nbttagcompound;

			if (itemstack.hasTagCompound()) {
				nbttagcompound = itemstack.getTagCompound().copy();
				enumdyecolor = EnumDyeColor.byDyeDamage(TileEntityBanner.getBaseColor(itemstack));
			} else {
				nbttagcompound = new NBTTagCompound();
				enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getItemDamage());
			}

			itemstack2.setTagCompound(nbttagcompound);
			TileEntityBanner.addBaseColorTag(itemstack2, enumdyecolor);
			return itemstack2;
		}

		@Override
		public int getRecipeSize() {
			return 2;
		}

		@Override
		@Nullable
		public ItemStack getRecipeOutput() {
			return null;
		}

		@Override
		public ItemStack[] getRemainingItems(InventoryCrafting inv) {
			ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

			for (int i = 0; i < aitemstack.length; ++i) {
				ItemStack itemstack = inv.getStackInSlot(i);

				if ((itemstack != null) && itemstack.getItem().hasContainerItem()) {
					aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
				}
			}

			return aitemstack;
		}
	}
}
