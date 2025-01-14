package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiDispenser extends GuiContainer {
    private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");

    /**
     * The player inventory bound to this GUI.
     */
    private final InventoryPlayer playerInventory;

    /**
     * The inventory contained within the corresponding Dispenser.
     */
    public IInventory dispenserInventory;

    public GuiDispenser(final InventoryPlayer playerInv, final IInventory dispenserInv) {
        super(new ContainerDispenser(playerInv, dispenserInv));
        this.playerInventory = playerInv;
        this.dispenserInventory = dispenserInv;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String s = this.dispenserInventory.getDisplayName().getUnformattedText();
        this.fontRendererObj.draw(s, this.xSize / 2 - this.fontRendererObj.width(s) / 2, 6, 4210752);
        this.fontRendererObj.draw(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(dispenserGuiTextures);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
