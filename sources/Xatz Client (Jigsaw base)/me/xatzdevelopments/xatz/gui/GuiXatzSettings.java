package me.xatzdevelopments.xatz.gui;

import java.io.IOException;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.cracker.gui.GuiXatzAccHacker;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

public class GuiXatzSettings extends GuiScreen {
	private int offset = 44;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiXatzSettings(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 - 80, this.height / 2 - offset, 160, 20, "Reset all Keybinds"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 80, this.height / 2 + 22 - offset, 160, 20, "Reset all Friends"));

		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, height - 50, 200, 20, "Done"));

		this.buttonList.add(new GuiButton(4, this.width / 2 - 80, this.height / 2 + 44 - offset, 160, 20, "Change ClickGUI Keybind"));
		this.buttonList.add(new GuiButton(5, this.width /2 - 80, this.height /2 + 66 - offset, 160, 20, "Main Menu Options"));
		
		//this.buttonList.add(new GuiButton(5, this.width / 2 - 80, this.height / 2 + 66 - offset, 160, 20, ClientSettings.bigWaterMark ? "Watermark: Big" : "Watermark: Small"));
		
		//this.buttonList.add(new GuiButton(6, this.width / 2 - 80, this.height / 2 + 88 - offset, 160, 20, ClientSettings.tabGui ? "TabGui: Enabled" : "TabGui: Disabled"));

		 //this.buttonList.add(new GuiButton(8, this.width / 2 + 2,
		 //94, 198, 20, "Account Hacker"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			for (Module module : Xatz.getModules()) {
				module.setKeyCode(module.getDefaultKeyboardKey());
			}
		}
		if (button.id == 1) {
			Xatz.getFriendsMananger().getFriends().clear();
			Xatz.getFileMananger().friendsDir.delete();
			this.onGuiClosed();
		}
		if (button.id == 2) {
			for (EntityPlayer en : mc.theWorld.playerEntities) {
				if (mc.thePlayer.getDistanceToEntity(en) <= 24) {
					Xatz.getFriendsMananger().getFriends().add(en.getName());
				}
			}
		}
		if (button.id == 3) {
			mc.displayGuiScreen(before);
		}
		if (button.id == 4) {
			mc.displayGuiScreen(new GuiXatzKeyBind(Xatz.getModuleByName("ClickGUI"), this));
		}
		if (button.id == 8) {
			mc.displayGuiScreen(new GuiXatzAccHacker(this));
		}
		if (button.id == 5) {
			//ClientSettings.bigWaterMark = !ClientSettings.bigWaterMark;
			mc.displayGuiScreen(new GuiXatzSettings(before));
		}
		if (button.id == 6) {
			//ClientSettings.tabGui = !ClientSettings.tabGui;
			mc.displayGuiScreen(new GuiXatzSettings(before));
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void onGuiClosed() {
		if (closed = false) {
			mc.displayGuiScreen(before);
			closed = true;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, Xatz.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - offset) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Settings", this.width / 2 / 2, (this.height / 2 - 25 - offset) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - offset) / 1, 0xffaaaaaa);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
