/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.client.gui.recipebook;

import java.util.Set;
import net.minecraft.client.gui.recipebook.AbstractRecipeBookGui;
import net.minecraft.item.Item;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class FurnaceRecipeGui
extends AbstractRecipeBookGui {
    private static final ITextComponent field_243414_i = new TranslationTextComponent("gui.recipebook.toggleRecipes.smeltable");

    @Override
    protected ITextComponent func_230479_g_() {
        return field_243414_i;
    }

    @Override
    protected Set<Item> func_212958_h() {
        return AbstractFurnaceTileEntity.getBurnTimes().keySet();
    }
}

