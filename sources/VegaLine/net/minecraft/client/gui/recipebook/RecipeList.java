/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class RecipeList {
    private List<IRecipe> field_192713_b = Lists.newArrayList();
    private final BitSet field_194215_b = new BitSet();
    private final BitSet field_194216_c = new BitSet();
    private final BitSet field_194217_d = new BitSet();
    private boolean field_194218_e = true;

    public boolean func_194209_a() {
        return !this.field_194217_d.isEmpty();
    }

    public void func_194214_a(RecipeBook p_194214_1_) {
        for (int i = 0; i < this.field_192713_b.size(); ++i) {
            this.field_194217_d.set(i, p_194214_1_.func_193830_f(this.field_192713_b.get(i)));
        }
    }

    public void func_194210_a(RecipeItemHelper p_194210_1_, int p_194210_2_, int p_194210_3_, RecipeBook p_194210_4_) {
        for (int i = 0; i < this.field_192713_b.size(); ++i) {
            IRecipe irecipe = this.field_192713_b.get(i);
            boolean flag = irecipe.func_194133_a(p_194210_2_, p_194210_3_) && p_194210_4_.func_193830_f(irecipe);
            this.field_194216_c.set(i, flag);
            this.field_194215_b.set(i, flag && p_194210_1_.func_194116_a(irecipe, null));
        }
    }

    public boolean func_194213_a(IRecipe p_194213_1_) {
        return this.field_194215_b.get(this.field_192713_b.indexOf(p_194213_1_));
    }

    public boolean func_192708_c() {
        return !this.field_194215_b.isEmpty();
    }

    public boolean func_194212_c() {
        return !this.field_194216_c.isEmpty();
    }

    public List<IRecipe> func_192711_b() {
        return this.field_192713_b;
    }

    public List<IRecipe> func_194208_a(boolean p_194208_1_) {
        ArrayList<IRecipe> list = Lists.newArrayList();
        int i = this.field_194217_d.nextSetBit(0);
        while (i >= 0) {
            if ((p_194208_1_ ? this.field_194215_b : this.field_194216_c).get(i)) {
                list.add(this.field_192713_b.get(i));
            }
            i = this.field_194217_d.nextSetBit(i + 1);
        }
        return list;
    }

    public List<IRecipe> func_194207_b(boolean p_194207_1_) {
        ArrayList<IRecipe> list = Lists.newArrayList();
        int i = this.field_194217_d.nextSetBit(0);
        while (i >= 0) {
            if (this.field_194216_c.get(i) && this.field_194215_b.get(i) == p_194207_1_) {
                list.add(this.field_192713_b.get(i));
            }
            i = this.field_194217_d.nextSetBit(i + 1);
        }
        return list;
    }

    public void func_192709_a(IRecipe p_192709_1_) {
        this.field_192713_b.add(p_192709_1_);
        if (this.field_194218_e) {
            ItemStack itemstack1;
            ItemStack itemstack = this.field_192713_b.get(0).getRecipeOutput();
            this.field_194218_e = ItemStack.areItemsEqual(itemstack, itemstack1 = p_192709_1_.getRecipeOutput()) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1);
        }
    }

    public boolean func_194211_e() {
        return this.field_194218_e;
    }
}

