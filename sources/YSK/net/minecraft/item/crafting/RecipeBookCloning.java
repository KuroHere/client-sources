package net.minecraft.item.crafting;

import net.minecraft.inventory.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;

public class RecipeBookCloning implements IRecipe
{
    private static final String[] I;
    
    @Override
    public boolean matches(final InventoryCrafting inventoryCrafting, final World world) {
        int length = "".length();
        ItemStack itemStack = null;
        int i = "".length();
        "".length();
        if (-1 == 1) {
            throw null;
        }
        while (i < inventoryCrafting.getSizeInventory()) {
            final ItemStack stackInSlot = inventoryCrafting.getStackInSlot(i);
            if (stackInSlot != null) {
                if (stackInSlot.getItem() == Items.written_book) {
                    if (itemStack != null) {
                        return "".length() != 0;
                    }
                    itemStack = stackInSlot;
                    "".length();
                    if (3 >= 4) {
                        throw null;
                    }
                }
                else {
                    if (stackInSlot.getItem() != Items.writable_book) {
                        return "".length() != 0;
                    }
                    ++length;
                }
            }
            ++i;
        }
        if (itemStack != null && length > 0) {
            return " ".length() != 0;
        }
        return "".length() != 0;
    }
    
    @Override
    public ItemStack[] getRemainingItems(final InventoryCrafting inventoryCrafting) {
        final ItemStack[] array = new ItemStack[inventoryCrafting.getSizeInventory()];
        int i = "".length();
        "".length();
        if (4 < 0) {
            throw null;
        }
        while (i < array.length) {
            final ItemStack stackInSlot = inventoryCrafting.getStackInSlot(i);
            if (stackInSlot != null && stackInSlot.getItem() instanceof ItemEditableBook) {
                array[i] = stackInSlot;
                "".length();
                if (3 < 2) {
                    throw null;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return array;
    }
    
    @Override
    public int getRecipeSize() {
        return 0x39 ^ 0x30;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inventoryCrafting) {
        int length = "".length();
        ItemStack itemStack = null;
        int i = "".length();
        "".length();
        if (1 == 2) {
            throw null;
        }
        while (i < inventoryCrafting.getSizeInventory()) {
            final ItemStack stackInSlot = inventoryCrafting.getStackInSlot(i);
            if (stackInSlot != null) {
                if (stackInSlot.getItem() == Items.written_book) {
                    if (itemStack != null) {
                        return null;
                    }
                    itemStack = stackInSlot;
                    "".length();
                    if (-1 >= 1) {
                        throw null;
                    }
                }
                else {
                    if (stackInSlot.getItem() != Items.writable_book) {
                        return null;
                    }
                    ++length;
                }
            }
            ++i;
        }
        if (itemStack != null && length >= " ".length() && ItemEditableBook.getGeneration(itemStack) < "  ".length()) {
            final ItemStack itemStack2 = new ItemStack(Items.written_book, length);
            itemStack2.setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            itemStack2.getTagCompound().setInteger(RecipeBookCloning.I["".length()], ItemEditableBook.getGeneration(itemStack) + " ".length());
            if (itemStack.hasDisplayName()) {
                itemStack2.setStackDisplayName(itemStack.getDisplayName());
            }
            return itemStack2;
        }
        return null;
    }
    
    private static void I() {
        (I = new String[" ".length()])["".length()] = I("$1&'3\" !-/", "CTHBA");
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
    
    static {
        I();
    }
    
    private static String I(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        final char[] charArray = s2.toCharArray();
        int length = "".length();
        final char[] charArray2 = s.toCharArray();
        final int length2 = charArray2.length;
        int i = "".length();
        while (i < length2) {
            sb.append((char)(charArray2[i] ^ charArray[length % charArray.length]));
            ++length;
            ++i;
            "".length();
            if (2 < 0) {
                throw null;
            }
        }
        return sb.toString();
    }
}
