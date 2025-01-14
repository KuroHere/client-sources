package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public abstract interface IMerchant
{
  public abstract void setCustomer(EntityPlayer paramEntityPlayer);
  
  public abstract EntityPlayer getCustomer();
  
  public abstract MerchantRecipeList getRecipes(EntityPlayer paramEntityPlayer);
  
  public abstract void setRecipes(MerchantRecipeList paramMerchantRecipeList);
  
  public abstract void useRecipe(MerchantRecipe paramMerchantRecipe);
  
  public abstract void func_110297_a_(ItemStack paramItemStack);
}


/* Location:           C:\Users\D\AppData\Roaming\.minecraft\versions\Nodus_2.0-1.7.x\Nodus_2.0-1.7.x.jar
 * Qualified Name:     net.minecraft.entity.IMerchant
 * JD-Core Version:    0.7.0.1
 */