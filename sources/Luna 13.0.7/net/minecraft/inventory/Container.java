package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public abstract class Container
{
  public List inventoryItemStacks = Lists.newArrayList();
  public List inventorySlots = Lists.newArrayList();
  public int windowId;
  private short transactionID;
  private int dragMode = -1;
  private int dragEvent;
  private final Set dragSlots = Sets.newHashSet();
  protected List crafters = Lists.newArrayList();
  private Set playerList = Sets.newHashSet();
  private static final String __OBFID = "CL_00001730";
  
  public Container() {}
  
  protected Slot addSlotToContainer(Slot p_75146_1_)
  {
    p_75146_1_.slotNumber = this.inventorySlots.size();
    this.inventorySlots.add(p_75146_1_);
    this.inventoryItemStacks.add(null);
    return p_75146_1_;
  }
  
  public void onCraftGuiOpened(ICrafting p_75132_1_)
  {
    if (this.crafters.contains(p_75132_1_)) {
      throw new IllegalArgumentException("Listener already listening");
    }
    this.crafters.add(p_75132_1_);
    p_75132_1_.updateCraftingInventory(this, getInventory());
    detectAndSendChanges();
  }
  
  public void removeCraftingFromCrafters(ICrafting p_82847_1_)
  {
    this.crafters.remove(p_82847_1_);
  }
  
  public List getInventory()
  {
    ArrayList var1 = Lists.newArrayList();
    for (int var2 = 0; var2 < this.inventorySlots.size(); var2++) {
      var1.add(((Slot)this.inventorySlots.get(var2)).getStack());
    }
    return var1;
  }
  
  public void detectAndSendChanges()
  {
    for (int var1 = 0; var1 < this.inventorySlots.size(); var1++)
    {
      ItemStack var2 = ((Slot)this.inventorySlots.get(var1)).getStack();
      ItemStack var3 = (ItemStack)this.inventoryItemStacks.get(var1);
      if (!ItemStack.areItemStacksEqual(var3, var2))
      {
        var3 = var2 == null ? null : var2.copy();
        this.inventoryItemStacks.set(var1, var3);
        for (int var4 = 0; var4 < this.crafters.size(); var4++) {
          ((ICrafting)this.crafters.get(var4)).sendSlotContents(this, var1, var3);
        }
      }
    }
  }
  
  public boolean enchantItem(EntityPlayer playerIn, int id)
  {
    return false;
  }
  
  public Slot getSlotFromInventory(IInventory p_75147_1_, int p_75147_2_)
  {
    for (int var3 = 0; var3 < this.inventorySlots.size(); var3++)
    {
      Slot var4 = (Slot)this.inventorySlots.get(var3);
      if (var4.isHere(p_75147_1_, p_75147_2_)) {
        return var4;
      }
    }
    return null;
  }
  
  public Slot getSlot(int p_75139_1_)
  {
    return (Slot)this.inventorySlots.get(p_75139_1_);
  }
  
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
  {
    Slot var3 = (Slot)this.inventorySlots.get(index);
    return var3 != null ? var3.getStack() : null;
  }
  
  public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn)
  {
    ItemStack var5 = null;
    InventoryPlayer var6 = playerIn.inventory;
    if (mode == 5)
    {
      int var7 = this.dragEvent;
      this.dragEvent = getDragEvent(clickedButton);
      if (((var7 != 1) || (this.dragEvent != 2)) && (var7 != this.dragEvent))
      {
        resetDrag();
      }
      else if (var6.getItemStack() == null)
      {
        resetDrag();
      }
      else if (this.dragEvent == 0)
      {
        this.dragMode = extractDragMode(clickedButton);
        if (func_180610_a(this.dragMode, playerIn))
        {
          this.dragEvent = 1;
          this.dragSlots.clear();
        }
        else
        {
          resetDrag();
        }
      }
      else if (this.dragEvent == 1)
      {
        Slot var8 = (Slot)this.inventorySlots.get(slotId);
        if ((var8 != null) && (canAddItemToSlot(var8, var6.getItemStack(), true)) && (var8.isItemValid(var6.getItemStack())) && (var6.getItemStack().stackSize > this.dragSlots.size()) && (canDragIntoSlot(var8))) {
          this.dragSlots.add(var8);
        }
      }
      else if (this.dragEvent == 2)
      {
        if (!this.dragSlots.isEmpty())
        {
          ItemStack var17 = var6.getItemStack().copy();
          int var9 = var6.getItemStack().stackSize;
          Iterator var10 = this.dragSlots.iterator();
          while (var10.hasNext())
          {
            Slot var11 = (Slot)var10.next();
            if ((var11 != null) && (canAddItemToSlot(var11, var6.getItemStack(), true)) && (var11.isItemValid(var6.getItemStack())) && (var6.getItemStack().stackSize >= this.dragSlots.size()) && (canDragIntoSlot(var11)))
            {
              ItemStack var12 = var17.copy();
              int var13 = var11.getHasStack() ? var11.getStack().stackSize : 0;
              computeStackSize(this.dragSlots, this.dragMode, var12, var13);
              if (var12.stackSize > var12.getMaxStackSize()) {
                var12.stackSize = var12.getMaxStackSize();
              }
              if (var12.stackSize > var11.func_178170_b(var12)) {
                var12.stackSize = var11.func_178170_b(var12);
              }
              var9 -= var12.stackSize - var13;
              var11.putStack(var12);
            }
          }
          var17.stackSize = var9;
          if (var17.stackSize <= 0) {
            var17 = null;
          }
          var6.setItemStack(var17);
        }
        resetDrag();
      }
      else
      {
        resetDrag();
      }
    }
    else if (this.dragEvent != 0)
    {
      resetDrag();
    }
    else if (((mode == 0) || (mode == 1)) && ((clickedButton == 0) || (clickedButton == 1)))
    {
      if (slotId == 64537)
      {
        if (var6.getItemStack() != null)
        {
          if (clickedButton == 0)
          {
            playerIn.dropPlayerItemWithRandomChoice(var6.getItemStack(), true);
            var6.setItemStack((ItemStack)null);
          }
          if (clickedButton == 1)
          {
            playerIn.dropPlayerItemWithRandomChoice(var6.getItemStack().splitStack(1), true);
            if (var6.getItemStack().stackSize == 0) {
              var6.setItemStack((ItemStack)null);
            }
          }
        }
      }
      else if (mode == 1)
      {
        if (slotId < 0) {
          return null;
        }
        Slot var16 = (Slot)this.inventorySlots.get(slotId);
        if ((var16 != null) && (var16.canTakeStack(playerIn)))
        {
          ItemStack var17 = transferStackInSlot(playerIn, slotId);
          if (var17 != null)
          {
            Item var19 = var17.getItem();
            var5 = var17.copy();
            if ((var16.getStack() != null) && (var16.getStack().getItem() == var19)) {
              retrySlotClick(slotId, clickedButton, true, playerIn);
            }
          }
        }
      }
      else
      {
        if (slotId < 0) {
          return null;
        }
        Slot var16 = (Slot)this.inventorySlots.get(slotId);
        if (var16 != null)
        {
          ItemStack var17 = var16.getStack();
          ItemStack var20 = var6.getItemStack();
          if (var17 != null) {
            var5 = var17.copy();
          }
          if (var17 == null)
          {
            if ((var20 != null) && (var16.isItemValid(var20)))
            {
              int var21 = clickedButton == 0 ? var20.stackSize : 1;
              if (var21 > var16.func_178170_b(var20)) {
                var21 = var16.func_178170_b(var20);
              }
              if (var20.stackSize >= var21) {
                var16.putStack(var20.splitStack(var21));
              }
              if (var20.stackSize == 0) {
                var6.setItemStack((ItemStack)null);
              }
            }
          }
          else if (var16.canTakeStack(playerIn)) {
            if (var20 == null)
            {
              int var21 = clickedButton == 0 ? var17.stackSize : (var17.stackSize + 1) / 2;
              ItemStack var23 = var16.decrStackSize(var21);
              var6.setItemStack(var23);
              if (var17.stackSize == 0) {
                var16.putStack((ItemStack)null);
              }
              var16.onPickupFromSlot(playerIn, var6.getItemStack());
            }
            else if (var16.isItemValid(var20))
            {
              if ((var17.getItem() == var20.getItem()) && (var17.getMetadata() == var20.getMetadata()) && (ItemStack.areItemStackTagsEqual(var17, var20)))
              {
                int var21 = clickedButton == 0 ? var20.stackSize : 1;
                if (var21 > var16.func_178170_b(var20) - var17.stackSize) {
                  var21 = var16.func_178170_b(var20) - var17.stackSize;
                }
                if (var21 > var20.getMaxStackSize() - var17.stackSize) {
                  var21 = var20.getMaxStackSize() - var17.stackSize;
                }
                var20.splitStack(var21);
                if (var20.stackSize == 0) {
                  var6.setItemStack((ItemStack)null);
                }
                var17.stackSize += var21;
              }
              else if (var20.stackSize <= var16.func_178170_b(var20))
              {
                var16.putStack(var20);
                var6.setItemStack(var17);
              }
            }
            else if ((var17.getItem() == var20.getItem()) && (var20.getMaxStackSize() > 1) && ((!var17.getHasSubtypes()) || (var17.getMetadata() == var20.getMetadata())) && (ItemStack.areItemStackTagsEqual(var17, var20)))
            {
              int var21 = var17.stackSize;
              if ((var21 > 0) && (var21 + var20.stackSize <= var20.getMaxStackSize()))
              {
                var20.stackSize += var21;
                var17 = var16.decrStackSize(var21);
                if (var17.stackSize == 0) {
                  var16.putStack((ItemStack)null);
                }
                var16.onPickupFromSlot(playerIn, var6.getItemStack());
              }
            }
          }
          var16.onSlotChanged();
        }
      }
    }
    else if ((mode == 2) && (clickedButton >= 0) && (clickedButton < 9))
    {
      Slot var16 = (Slot)this.inventorySlots.get(slotId);
      if (var16.canTakeStack(playerIn))
      {
        ItemStack var17 = var6.getStackInSlot(clickedButton);
        boolean var18 = (var17 == null) || ((var16.inventory == var6) && (var16.isItemValid(var17)));
        int var21 = -1;
        if (!var18)
        {
          var21 = var6.getFirstEmptyStack();
          var18 |= var21 > -1;
        }
        if ((var16.getHasStack()) && (var18))
        {
          ItemStack var23 = var16.getStack();
          var6.setInventorySlotContents(clickedButton, var23.copy());
          if (((var16.inventory != var6) || (!var16.isItemValid(var17))) && (var17 != null))
          {
            if (var21 > -1)
            {
              var6.addItemStackToInventory(var17);
              var16.decrStackSize(var23.stackSize);
              var16.putStack((ItemStack)null);
              var16.onPickupFromSlot(playerIn, var23);
            }
          }
          else
          {
            var16.decrStackSize(var23.stackSize);
            var16.putStack(var17);
            var16.onPickupFromSlot(playerIn, var23);
          }
        }
        else if ((!var16.getHasStack()) && (var17 != null) && (var16.isItemValid(var17)))
        {
          var6.setInventorySlotContents(clickedButton, (ItemStack)null);
          var16.putStack(var17);
        }
      }
    }
    else if ((mode == 3) && (playerIn.capabilities.isCreativeMode) && (var6.getItemStack() == null) && (slotId >= 0))
    {
      Slot var16 = (Slot)this.inventorySlots.get(slotId);
      if ((var16 != null) && (var16.getHasStack()))
      {
        ItemStack var17 = var16.getStack().copy();
        var17.stackSize = var17.getMaxStackSize();
        var6.setItemStack(var17);
      }
    }
    else if ((mode == 4) && (var6.getItemStack() == null) && (slotId >= 0))
    {
      Slot var16 = (Slot)this.inventorySlots.get(slotId);
      if ((var16 != null) && (var16.getHasStack()) && (var16.canTakeStack(playerIn)))
      {
        ItemStack var17 = var16.decrStackSize(clickedButton == 0 ? 1 : var16.getStack().stackSize);
        var16.onPickupFromSlot(playerIn, var17);
        playerIn.dropPlayerItemWithRandomChoice(var17, true);
      }
    }
    else if ((mode == 6) && (slotId >= 0))
    {
      Slot var16 = (Slot)this.inventorySlots.get(slotId);
      ItemStack var17 = var6.getItemStack();
      if ((var17 != null) && ((var16 == null) || (!var16.getHasStack()) || (!var16.canTakeStack(playerIn))))
      {
        int var9 = clickedButton == 0 ? 0 : this.inventorySlots.size() - 1;
        int var21 = clickedButton == 0 ? 1 : -1;
        for (int var22 = 0; var22 < 2; var22++) {
          for (int var24 = var9; (var24 >= 0) && (var24 < this.inventorySlots.size()) && (var17.stackSize < var17.getMaxStackSize()); var24 += var21)
          {
            Slot var25 = (Slot)this.inventorySlots.get(var24);
            if ((var25.getHasStack()) && (canAddItemToSlot(var25, var17, true)) && (var25.canTakeStack(playerIn)) && (func_94530_a(var17, var25)) && ((var22 != 0) || (var25.getStack().stackSize != var25.getStack().getMaxStackSize())))
            {
              int var14 = Math.min(var17.getMaxStackSize() - var17.stackSize, var25.getStack().stackSize);
              ItemStack var15 = var25.decrStackSize(var14);
              var17.stackSize += var14;
              if (var15.stackSize <= 0) {
                var25.putStack((ItemStack)null);
              }
              var25.onPickupFromSlot(playerIn, var15);
            }
          }
        }
      }
      detectAndSendChanges();
    }
    return var5;
  }
  
  public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_)
  {
    return true;
  }
  
  protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_)
  {
    slotClick(p_75133_1_, p_75133_2_, 1, p_75133_4_);
  }
  
  public void onContainerClosed(EntityPlayer p_75134_1_)
  {
    InventoryPlayer var2 = p_75134_1_.inventory;
    if (var2.getItemStack() != null)
    {
      p_75134_1_.dropPlayerItemWithRandomChoice(var2.getItemStack(), false);
      var2.setItemStack((ItemStack)null);
    }
  }
  
  public void onCraftMatrixChanged(IInventory p_75130_1_)
  {
    detectAndSendChanges();
  }
  
  public void putStackInSlot(int p_75141_1_, ItemStack p_75141_2_)
  {
    getSlot(p_75141_1_).putStack(p_75141_2_);
  }
  
  public void putStacksInSlots(ItemStack[] p_75131_1_)
  {
    for (int var2 = 0; var2 < p_75131_1_.length; var2++) {
      getSlot(var2).putStack(p_75131_1_[var2]);
    }
  }
  
  public void updateProgressBar(int p_75137_1_, int p_75137_2_) {}
  
  public short getNextTransactionID(InventoryPlayer p_75136_1_)
  {
    this.transactionID = ((short)(this.transactionID + 1));
    return this.transactionID;
  }
  
  public boolean getCanCraft(EntityPlayer p_75129_1_)
  {
    return !this.playerList.contains(p_75129_1_);
  }
  
  public void setCanCraft(EntityPlayer p_75128_1_, boolean p_75128_2_)
  {
    if (p_75128_2_) {
      this.playerList.remove(p_75128_1_);
    } else {
      this.playerList.add(p_75128_1_);
    }
  }
  
  public abstract boolean canInteractWith(EntityPlayer paramEntityPlayer);
  
  protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean useEndIndex)
  {
    boolean var5 = false;
    int var6 = startIndex;
    if (useEndIndex) {
      var6 = endIndex - 1;
    }
    if (stack.isStackable()) {
      while ((stack.stackSize > 0) && (((!useEndIndex) && (var6 < endIndex)) || ((useEndIndex) && (var6 >= startIndex))))
      {
        Slot var7 = (Slot)this.inventorySlots.get(var6);
        ItemStack var8 = var7.getStack();
        if ((var8 != null) && (var8.getItem() == stack.getItem()) && ((!stack.getHasSubtypes()) || (stack.getMetadata() == var8.getMetadata())) && (ItemStack.areItemStackTagsEqual(stack, var8)))
        {
          int var9 = var8.stackSize + stack.stackSize;
          if (var9 <= stack.getMaxStackSize())
          {
            stack.stackSize = 0;
            var8.stackSize = var9;
            var7.onSlotChanged();
            var5 = true;
          }
          else if (var8.stackSize < stack.getMaxStackSize())
          {
            stack.stackSize -= stack.getMaxStackSize() - var8.stackSize;
            var8.stackSize = stack.getMaxStackSize();
            var7.onSlotChanged();
            var5 = true;
          }
        }
        if (useEndIndex) {
          var6--;
        } else {
          var6++;
        }
      }
    }
    if (stack.stackSize > 0)
    {
      if (useEndIndex) {
        var6 = endIndex - 1;
      } else {
        var6 = startIndex;
      }
      while (((!useEndIndex) && (var6 < endIndex)) || ((useEndIndex) && (var6 >= startIndex)))
      {
        Slot var7 = (Slot)this.inventorySlots.get(var6);
        ItemStack var8 = var7.getStack();
        if (var8 == null)
        {
          var7.putStack(stack.copy());
          var7.onSlotChanged();
          stack.stackSize = 0;
          var5 = true;
          break;
        }
        if (useEndIndex) {
          var6--;
        } else {
          var6++;
        }
      }
    }
    return var5;
  }
  
  public static int extractDragMode(int p_94529_0_)
  {
    return p_94529_0_ >> 2 & 0x3;
  }
  
  public static int getDragEvent(int p_94532_0_)
  {
    return p_94532_0_ & 0x3;
  }
  
  public static int func_94534_d(int p_94534_0_, int p_94534_1_)
  {
    return p_94534_0_ & 0x3 | (p_94534_1_ & 0x3) << 2;
  }
  
  public static boolean func_180610_a(int p_180610_0_, EntityPlayer p_180610_1_)
  {
    return p_180610_0_ == 0;
  }
  
  protected void resetDrag()
  {
    this.dragEvent = 0;
    this.dragSlots.clear();
  }
  
  public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters)
  {
    boolean var3 = (slotIn == null) || (!slotIn.getHasStack());
    if ((slotIn != null) && (slotIn.getHasStack()) && (stack != null) && (stack.isItemEqual(slotIn.getStack())) && (ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)))
    {
      int var10002 = stackSizeMatters ? 0 : stack.stackSize;
      var3 |= slotIn.getStack().stackSize + var10002 <= stack.getMaxStackSize();
    }
    return var3;
  }
  
  public static void computeStackSize(Set p_94525_0_, int p_94525_1_, ItemStack p_94525_2_, int p_94525_3_)
  {
    switch (p_94525_1_)
    {
    case 0: 
      p_94525_2_.stackSize = MathHelper.floor_float(p_94525_2_.stackSize / p_94525_0_.size());
      break;
    case 1: 
      p_94525_2_.stackSize = 1;
      break;
    case 2: 
      p_94525_2_.stackSize = p_94525_2_.getItem().getItemStackLimit();
    }
    p_94525_2_.stackSize += p_94525_3_;
  }
  
  public boolean canDragIntoSlot(Slot p_94531_1_)
  {
    return true;
  }
  
  public static int calcRedstoneFromInventory(TileEntity te)
  {
    return (te instanceof IInventory) ? calcRedstoneFromInventory((IInventory)te) : 0;
  }
  
  public static int calcRedstoneFromInventory(IInventory inv)
  {
    if (inv == null) {
      return 0;
    }
    int var1 = 0;
    float var2 = 0.0F;
    for (int var3 = 0; var3 < inv.getSizeInventory(); var3++)
    {
      ItemStack var4 = inv.getStackInSlot(var3);
      if (var4 != null)
      {
        var2 += var4.stackSize / Math.min(inv.getInventoryStackLimit(), var4.getMaxStackSize());
        var1++;
      }
    }
    var2 /= inv.getSizeInventory();
    return MathHelper.floor_float(var2 * 14.0F) + (var1 > 0 ? 1 : 0);
  }
}
