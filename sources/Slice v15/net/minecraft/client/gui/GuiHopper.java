package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class GuiHopper extends GuiContainer
{
  private static final ResourceLocation field_147085_u = new ResourceLocation("textures/gui/container/hopper.png");
  private IInventory field_147084_v;
  private IInventory field_147083_w;
  private static final String __OBFID = "CL_00000759";
  
  public GuiHopper(InventoryPlayer p_i1092_1_, IInventory p_i1092_2_)
  {
    super(new ContainerHopper(p_i1092_1_, p_i1092_2_, Minecraft.thePlayer));
    field_147084_v = p_i1092_1_;
    field_147083_w = p_i1092_2_;
    allowUserInput = false;
    ySize = 133;
  }
  



  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
  {
    fontRendererObj.drawString(field_147083_w.getDisplayName().getUnformattedText(), 8, 6, 4210752);
    fontRendererObj.drawString(field_147084_v.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
  }
  



  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    mc.getTextureManager().bindTexture(field_147085_u);
    int var4 = (width - xSize) / 2;
    int var5 = (height - ySize) / 2;
    drawTexturedModalRect(var4, var5, 0, 0, xSize, ySize);
  }
}
