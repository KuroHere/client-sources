/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BookItem
extends Item {
    public BookItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return itemStack.getCount() == 1;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }
}

