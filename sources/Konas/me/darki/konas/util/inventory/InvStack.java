package me.darki.konas.util.inventory;

import net.minecraft.item.ItemStack;

public class InvStack {

    public final int slot;
    public final ItemStack stack;

    public InvStack(int slot, ItemStack stack) {
        this.slot = slot;
        this.stack = stack;
    }

}
