package net.minecraft.client.gui;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;

public class GuiListWorldSelection extends GuiListExtended {
	private static final Logger LOGGER = LogManager.getLogger();
	private final GuiWorldSelection worldSelectionObj;
	private final List<GuiListWorldSelectionEntry> entries = Lists.<GuiListWorldSelectionEntry> newArrayList();

	/** Index to the currently selected world */
	private int selectedIdx = -1;

	public GuiListWorldSelection(GuiWorldSelection p_i46590_1_, Minecraft clientIn, int p_i46590_3_, int p_i46590_4_, int p_i46590_5_, int p_i46590_6_, int p_i46590_7_) {
		super(clientIn, p_i46590_3_, p_i46590_4_, p_i46590_5_, p_i46590_6_, p_i46590_7_);
		this.worldSelectionObj = p_i46590_1_;
		this.refreshList();
	}

	public void refreshList() {
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		List<WorldSummary> list;

		try {
			list = isaveformat.getSaveList();
		} catch (AnvilConverterException anvilconverterexception) {
			LOGGER.error("Couldn\'t load level list", anvilconverterexception);
			this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", anvilconverterexception.getMessage()));
			return;
		}

		Collections.sort(list);

		for (WorldSummary worldsummary : list) {
			this.entries.add(new GuiListWorldSelectionEntry(this, worldsummary, this.mc.getSaveLoader()));
		}
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@Override
	public GuiListWorldSelectionEntry getListEntry(int index) {
		return this.entries.get(index);
	}

	@Override
	protected int getSize() {
		return this.entries.size();
	}

	@Override
	protected int getScrollBarX() {
		return super.getScrollBarX() + 20;
	}

	/**
	 * Gets the width of the list
	 */
	@Override
	public int getListWidth() {
		return super.getListWidth() + 50;
	}

	public void selectWorld(int idx) {
		this.selectedIdx = idx;
		this.worldSelectionObj.selectWorld(this.getSelectedWorld());
	}

	/**
	 * Returns true if the element passed in is currently selected
	 */
	@Override
	protected boolean isSelected(int slotIndex) {
		return slotIndex == this.selectedIdx;
	}

	@Nullable
	public GuiListWorldSelectionEntry getSelectedWorld() {
		return (this.selectedIdx >= 0) && (this.selectedIdx < this.getSize()) ? this.getListEntry(this.selectedIdx) : null;
	}

	public GuiWorldSelection getGuiWorldSelection() {
		return this.worldSelectionObj;
	}
}
