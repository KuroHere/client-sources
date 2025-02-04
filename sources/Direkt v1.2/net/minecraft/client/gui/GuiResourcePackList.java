package net.minecraft.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.text.TextFormatting;

public abstract class GuiResourcePackList extends GuiListExtended {
	protected final Minecraft mc;
	protected final List<ResourcePackListEntry> resourcePackEntries;

	public GuiResourcePackList(Minecraft mcIn, int p_i45055_2_, int p_i45055_3_, List<ResourcePackListEntry> p_i45055_4_) {
		super(mcIn, p_i45055_2_, p_i45055_3_, 32, (p_i45055_3_ - 55) + 4, 36);
		this.mc = mcIn;
		this.resourcePackEntries = p_i45055_4_;
		this.centerListVertically = false;
		this.setHasListHeader(true, (int) (mcIn.fontRendererObj.FONT_HEIGHT * 1.5F));
	}

	/**
	 * Handles drawing a list's header row.
	 */
	@Override
	protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn) {
		String s = TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + this.getListHeader();
		this.mc.fontRendererObj.drawString(s, (insideLeft + (this.width / 2)) - (this.mc.fontRendererObj.getStringWidth(s) / 2), Math.min(this.top + 3, insideTop), 16777215);
	}

	protected abstract String getListHeader();

	public List<ResourcePackListEntry> getList() {
		return this.resourcePackEntries;
	}

	@Override
	protected int getSize() {
		return this.getList().size();
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@Override
	public ResourcePackListEntry getListEntry(int index) {
		return this.getList().get(index);
	}

	/**
	 * Gets the width of the list
	 */
	@Override
	public int getListWidth() {
		return this.width;
	}

	@Override
	protected int getScrollBarX() {
		return this.right - 6;
	}
}
