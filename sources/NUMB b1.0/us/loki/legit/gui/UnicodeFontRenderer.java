package us.loki.legit.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;

public class UnicodeFontRenderer extends FontRenderer {
	private final UnicodeFont font;

	@SuppressWarnings("unchecked")
	public UnicodeFontRenderer(Font awtFont) {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
				Minecraft.getMinecraft().getTextureManager(), false);

		font = new UnicodeFont(awtFont);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(Color.WHITE));
		try {
			font.loadGlyphs();
		} catch (SlickException exception) {
			throw new RuntimeException(exception);
		}
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		FONT_HEIGHT = font.getHeight(alphabet) / 2;
	}

	@Override
	public int drawString(String string, int x, int y, int color) {
		if (string == null)
			return 0;

		glPushMatrix();
		glScaled(0.5, 0.5, 0.5);

		boolean blend = glIsEnabled(GL_BLEND);
		boolean lighting = glIsEnabled(GL_LIGHTING);
		boolean texture = glIsEnabled(GL_TEXTURE_2D);
		if (!blend)
			glEnable(GL_BLEND);
		if (lighting)
			glDisable(GL_LIGHTING);
		if (texture)
			glDisable(GL_TEXTURE_2D);
		x *= 2;
		y *= 2;

		font.drawString(x, y, string, new org.newdawn.slick.Color(color));

		if (texture)
			glEnable(GL_TEXTURE_2D);
		if (lighting)
			glEnable(GL_LIGHTING);
		if (!blend)
			glDisable(GL_BLEND);
		glPopMatrix();
		return x;
	}

	@Override
	public int getCharWidth(char c) {
		return getStringWidth(Character.toString(c));
	}

	@Override
	public int getStringWidth(String string) {
		return font.getWidth(string) / 2;
	}

	public int getStringHeight(String string) {
		return font.getHeight(string) / 2;
	}

	public int drawCenteredString(String text, int x, int y, int color) {
		return this.drawString(text, (x - getStringWidth(text) / 2 + 2), y, color);
	}

	public int getRealStringWidth(final String string) {
		return this.font.getWidth(StringUtils.stripControlCodes(string)) / 2;
	}
}