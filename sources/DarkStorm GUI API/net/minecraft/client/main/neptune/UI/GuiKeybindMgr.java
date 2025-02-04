package net.minecraft.client.main.neptune.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.darkstorm.minecraft.gui.AbstractGuiManager;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager.HorizontalGridConstraint;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.theme.Theme;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.neptune.Neptune;
import net.minecraft.client.main.neptune.Mod.Category;
import net.minecraft.client.main.neptune.Mod.Mod;

public final class GuiKeybindMgr extends AbstractGuiManager {
	private class ModuleFrame extends BasicFrame {
		private ModuleFrame() {
		}

		private ModuleFrame(String title) {
			super(title);
		}
	}

	private final AtomicBoolean setup;

	public GuiKeybindMgr() {
		setup = new AtomicBoolean();
	}

	@Override
	public void setup() {
		if (!setup.compareAndSet(false, true))
			return;
		final Map<Category, ModuleFrame> categoryFrames = new HashMap<Category, ModuleFrame>();
		for (Mod mod : Neptune.getWinter().theMods.getMods()) {
			ModuleFrame frame = categoryFrames.get(mod.getCategory());
			if (frame == null) {
				String name = mod.getCategory().name().toLowerCase();
				name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
				frame = new ModuleFrame(name);
				frame.setTheme(theme);
				frame.setLayoutManager(new GridLayoutManager(1, 0));
				frame.setVisible(true);
				frame.setClosable(false);
				frame.setMinimized(true);
				frame.setPinnable(false);
				addFrame(frame);
				categoryFrames.put(mod.getCategory(), frame);
			}
			final Mod updateMod = mod;
			Button button = new BasicButton(
					String.format("%s: %s", mod.getModName(), Keyboard.getKeyName(mod.getBind()))) {
				@Override
				public void update() {
					Color gray = new Color(0.3f, 0.3f, 0.3f, 1.0f);
					this.setBackgroundColor(gray);
					this.setText(
							String.format("%s: %s", updateMod.getModName(), Keyboard.getKeyName(updateMod.getBind())));
				}
			};
			button.addButtonListener(new ButtonListener() {
				@Override
				public void onButtonPress(Button button) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiKeybindChanger(updateMod));
				}
			});
			frame.add(button, HorizontalGridConstraint.FILL);
		}

		resizeComponents();
		Minecraft minecraft = Minecraft.getMinecraft();
		Dimension maxSize = recalculateSizes();
		int offsetX = 5, offsetY = 5;
		int scale = minecraft.gameSettings.guiScale;
		if (scale == 0)
			scale = 1000;
		int scaleFactor = 0;
		while (scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320
				&& minecraft.displayHeight / (scaleFactor + 1) >= 240)
			scaleFactor++;
		for (Frame frame : getFrames()) {
			frame.setX(offsetX);
			frame.setY(offsetY);
			offsetX += maxSize.width + 5;
			if (offsetX + maxSize.width + 5 > minecraft.displayWidth / scaleFactor) {
				offsetX = 5;
				offsetY += maxSize.height + 5;
			}
		}
	}

	@Override
	protected void resizeComponents() {
		Theme theme = getTheme();
		Frame[] frames = getFrames();
		Button enable = new BasicButton("Enable");
		Button disable = new BasicButton("Disable");
		Dimension enableSize = theme.getUIForComponent(enable).getDefaultSize(enable);
		Dimension disableSize = theme.getUIForComponent(disable).getDefaultSize(disable);
		int buttonWidth = Math.max(enableSize.width, disableSize.width);
		int buttonHeight = Math.max(enableSize.height, disableSize.height);
		for (Frame frame : frames) {
			if (frame instanceof ModuleFrame) {
				for (Component component : frame.getChildren()) {
					if (component instanceof Button) {
						component.setWidth(buttonWidth);
						component.setHeight(buttonHeight);
					}
				}
			}
		}
		recalculateSizes();
	}

	private Dimension recalculateSizes() {
		Frame[] frames = getFrames();
		int maxWidth = 0, maxHeight = 0;
		for (Frame frame : frames) {
			Dimension defaultDimension = frame.getTheme().getUIForComponent(frame).getDefaultSize(frame);
			maxWidth = Math.max(maxWidth, defaultDimension.width);
			frame.setHeight(defaultDimension.height);
			if (frame.isMinimized()) {
				for (Rectangle area : frame.getTheme().getUIForComponent(frame).getInteractableRegions(frame))
					maxHeight = Math.max(maxHeight, area.height);
			} else
				maxHeight = Math.max(maxHeight, defaultDimension.height);
		}
		for (Frame frame : frames) {
			frame.setWidth(maxWidth);
			frame.layoutChildren();
		}
		return new Dimension(maxWidth, maxHeight);
	}
}