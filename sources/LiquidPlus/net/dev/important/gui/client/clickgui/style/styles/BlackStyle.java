/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.StringUtils
 *  org.lwjgl.input.Mouse
 */
package net.dev.important.gui.client.clickgui.style.styles;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import net.dev.important.gui.client.clickgui.Panel;
import net.dev.important.gui.client.clickgui.elements.ButtonElement;
import net.dev.important.gui.client.clickgui.elements.ModuleElement;
import net.dev.important.gui.client.clickgui.style.Style;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BlockValue;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.dev.important.value.Value;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Mouse;

public class BlackStyle
extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() - 3.0f, (float)panel.getX() + (float)panel.getWidth(), (float)panel.getY() + 17.0f, 3.0f, new Color(20, 20, 20).getRGB(), new Color(20, 20, 20).getRGB());
        if (panel.getFade() > 0) {
            RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() + 17.0f, (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade(), 3.0f, new Color(40, 40, 40).getRGB(), new Color(40, 40, 40).getRGB());
            RenderUtils.drawBorderedRect(panel.getX(), panel.getY() + 17 + panel.getFade(), (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade() + 5, 3.0f, new Color(20, 20, 20).getRGB(), new Color(20, 20, 20).getRGB());
        }
        GlStateManager.func_179117_G();
        float textWidth = Fonts.Poppins.func_78256_a("\u00a7f" + StringUtils.func_76338_a((String)panel.getName()));
        Fonts.Poppins.func_78276_b(panel.getName(), (int)((float)panel.getX() - (textWidth - 100.0f) / 2.0f), panel.getY() + 7 - 3, Color.WHITE.getRGB());
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = Fonts.Poppins.func_78256_a(text);
        RenderUtils.drawBorderedRect(mouseX + 9, mouseY, mouseX + textWidth + 14, mouseY + Fonts.Poppins.field_78288_b + 3, 3.0f, new Color(40, 40, 40).getRGB(), new Color(40, 40, 40).getRGB());
        GlStateManager.func_179117_G();
        Fonts.Poppins.func_78276_b(text, mouseX + 12, mouseY + Fonts.Poppins.field_78288_b / 2, Color.WHITE.getRGB());
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        Gui.func_73734_a((int)(buttonElement.getX() - 1), (int)(buttonElement.getY() - 1), (int)(buttonElement.getX() + buttonElement.getWidth() + 1), (int)(buttonElement.getY() + buttonElement.getHeight() + 1), (int)this.hoverColor(buttonElement.getColor() != Integer.MAX_VALUE ? new Color(20, 20, 20) : new Color(40, 40, 40), buttonElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.Poppins.func_78276_b(buttonElement.getDisplayName(), buttonElement.getX() + 5, buttonElement.getY() + 5, Color.WHITE.getRGB());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        Gui.func_73734_a((int)(moduleElement.getX() - 1), (int)(moduleElement.getY() - 1), (int)(moduleElement.getX() + moduleElement.getWidth() + 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 1), (int)this.hoverColor(new Color(40, 40, 40), moduleElement.hoverTime).getRGB());
        Gui.func_73734_a((int)(moduleElement.getX() - 1), (int)(moduleElement.getY() - 1), (int)(moduleElement.getX() + moduleElement.getWidth() + 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 1), (int)this.hoverColor(new Color(20, 20, 20, moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.Poppins.func_78276_b(moduleElement.getDisplayName(), moduleElement.getX() + 5, moduleElement.getY() + 5, Color.WHITE.getRGB());
        List<Value<?>> moduleValues = moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.Poppins.func_78276_b(">", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + 5, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                if (moduleElement.getSettingsWidth() > 0.0f && moduleElement.slowlySettingsYPos > moduleElement.getY() + 6) {
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), moduleElement.slowlySettingsYPos + 2, 3.0f, new Color(40, 40, 40).getRGB(), new Color(40, 40, 40).getRGB());
                }
                moduleElement.slowlySettingsYPos = moduleElement.getY() + 6;
                for (Value<?> value : moduleValues) {
                    float textWidth;
                    String text;
                    if (!value.getCanDisplay().invoke().booleanValue()) continue;
                    if (value instanceof BoolValue) {
                        text = value.getName();
                        textWidth = Fonts.Poppins.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + 12 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            BoolValue boolValue;
                            boolValue.set((Boolean)(boolValue = (BoolValue)value).get() == false);
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                        }
                        Fonts.Poppins.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, (Boolean)((BoolValue)value).get() != false ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                        moduleElement.slowlySettingsYPos += 11;
                        continue;
                    }
                    if (value instanceof ListValue) {
                        ListValue listValue = (ListValue)value;
                        String text2 = value.getName();
                        float textWidth2 = Fonts.Poppins.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        Fonts.Poppins.func_78276_b(text2, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, 0xFFFFFF);
                        Fonts.Poppins.func_78276_b(listValue.openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(listValue.openList ? 5 : 6)), moduleElement.slowlySettingsYPos + 2, 0xFFFFFF);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + Fonts.Poppins.field_78288_b && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            listValue.openList = !listValue.openList;
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                        }
                        moduleElement.slowlySettingsYPos += Fonts.Poppins.field_78288_b + 1;
                        for (String valueOfList : listValue.getValues()) {
                            float textWidth22 = Fonts.Poppins.func_78256_a("> " + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth22 + 12.0f) {
                                moduleElement.setSettingsWidth(textWidth22 + 12.0f);
                            }
                            if (!listValue.openList) continue;
                            if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos + 2 && mouseY <= moduleElement.slowlySettingsYPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                listValue.set(valueOfList);
                                mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                            }
                            GlStateManager.func_179117_G();
                            Fonts.Poppins.func_78276_b("> " + valueOfList, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, listValue.get() != null && ((String)listValue.get()).equalsIgnoreCase(valueOfList) ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                            moduleElement.slowlySettingsYPos += Fonts.Poppins.field_78288_b + 1;
                        }
                        if (listValue.openList) continue;
                        ++moduleElement.slowlySettingsYPos;
                        continue;
                    }
                    if (value instanceof FloatValue) {
                        float valueOfSlide;
                        FloatValue floatValue = (FloatValue)value;
                        String text3 = value.getName() + "\u00a7f: " + this.round(((Float)floatValue.get()).floatValue()) + floatValue.getSuffix();
                        float textWidth3 = Fonts.Poppins.func_78256_a(text3);
                        if (moduleElement.getSettingsWidth() < textWidth3 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth3 + 8.0f);
                        }
                        if ((valueOfSlide = BlackStyle.drawSlider(((Float)floatValue.get()).floatValue(), floatValue.getMinimum(), floatValue.getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, mouseX, mouseY, new Color(20, 20, 20))) != ((Float)floatValue.get()).floatValue()) {
                            floatValue.set(Float.valueOf(valueOfSlide));
                        }
                        Fonts.Poppins.func_78276_b(text3, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 19;
                        continue;
                    }
                    if (value instanceof IntegerValue) {
                        float valueOfSlide;
                        IntegerValue integerValue = (IntegerValue)value;
                        String text4 = value.getName() + "\u00a7f: " + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)integerValue.get()) + " (" + integerValue.get() + ")" : integerValue.get() + integerValue.getSuffix());
                        float textWidth4 = Fonts.Poppins.func_78256_a(text4);
                        if (moduleElement.getSettingsWidth() < textWidth4 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth4 + 8.0f);
                        }
                        if ((valueOfSlide = BlackStyle.drawSlider(((Integer)integerValue.get()).intValue(), integerValue.getMinimum(), integerValue.getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, mouseX, mouseY, new Color(20, 20, 20))) != (float)((Integer)integerValue.get()).intValue()) {
                            integerValue.set((int)valueOfSlide);
                        }
                        Fonts.Poppins.func_78276_b(text4, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 19;
                        continue;
                    }
                    if (value instanceof FontValue) {
                        FontValue fontValue = (FontValue)value;
                        FontRenderer fontRenderer = (FontRenderer)fontValue.get();
                        String displayString = "Font: Unknown";
                        if (fontRenderer instanceof GameFontRenderer) {
                            GameFontRenderer liquidFontRenderer = (GameFontRenderer)fontRenderer;
                            displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " + liquidFontRenderer.getDefaultFont().getFont().getSize();
                        } else if (fontRenderer == Fonts.minecraftFont) {
                            displayString = "Font: Minecraft";
                        } else {
                            Object[] objects = Fonts.getFontDetails(fontRenderer);
                            if (objects != null) {
                                displayString = objects[0] + ((Integer)objects[1] != -1 ? " - " + objects[1] : "");
                            }
                        }
                        Fonts.Poppins.func_78276_b(displayString, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, Color.WHITE.getRGB());
                        int stringWidth = Fonts.Poppins.func_78256_a(displayString);
                        if (moduleElement.getSettingsWidth() < (float)(stringWidth + 8)) {
                            moduleElement.setSettingsWidth(stringWidth + 8);
                        }
                        if ((Mouse.isButtonDown((int)0) && !this.mouseDown || Mouse.isButtonDown((int)1) && !this.rightMouseDown) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + 12) {
                            FontRenderer font;
                            int i;
                            List<FontRenderer> fonts = Fonts.getFonts();
                            if (Mouse.isButtonDown((int)0)) {
                                for (i = 0; i < fonts.size(); ++i) {
                                    font = fonts.get(i);
                                    if (font != fontRenderer) continue;
                                    if (++i >= fonts.size()) {
                                        i = 0;
                                    }
                                    fontValue.set(fonts.get(i));
                                    break;
                                }
                            } else {
                                for (i = fonts.size() - 1; i >= 0; --i) {
                                    font = fonts.get(i);
                                    if (font != fontRenderer) continue;
                                    if (--i >= fonts.size()) {
                                        i = 0;
                                    }
                                    if (i < 0) {
                                        i = fonts.size() - 1;
                                    }
                                    fontValue.set(fonts.get(i));
                                    break;
                                }
                            }
                        }
                        moduleElement.slowlySettingsYPos += 11;
                        continue;
                    }
                    text = value.getName() + "\u00a7f: " + value.get();
                    textWidth = Fonts.Poppins.func_78256_a(text);
                    if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                        moduleElement.setSettingsWidth(textWidth + 8.0f);
                    }
                    GlStateManager.func_179117_G();
                    Fonts.Poppins.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 4, 0xFFFFFF);
                    moduleElement.slowlySettingsYPos += 12;
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown((int)0);
                this.rightMouseDown = Mouse.isButtonDown((int)1);
            }
        }
    }

    private BigDecimal round(float v) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(v));
        bigDecimal = bigDecimal.setScale(2, 4);
        return bigDecimal;
    }

    private Color hoverColor(Color color, int hover) {
        int r = color.getRed() - hover * 2;
        int g = color.getGreen() - hover * 2;
        int b = color.getBlue() - hover * 2;
        return new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0), color.getAlpha());
    }

    public static float drawSlider(float value, float min, float max, int x, int y, int width, int mouseX, int mouseY, Color color) {
        float displayValue = Math.max(min, Math.min(value, max));
        float sliderValue = (float)x + (float)width * (displayValue - min) / (max - min);
        RenderUtils.drawRect((float)x, (float)y, (float)(x + width), (float)(y + 2), Integer.MAX_VALUE);
        RenderUtils.drawRect((float)x, (float)y, sliderValue, (float)(y + 2), color);
        RenderUtils.drawFilledCircle((int)sliderValue, y + 1, 3.0f, color);
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 3 && Mouse.isButtonDown((int)0)) {
            double i = MathHelper.func_151237_a((double)(((double)mouseX - (double)x) / ((double)width - 3.0)), (double)0.0, (double)1.0);
            BigDecimal bigDecimal = new BigDecimal(Double.toString((double)min + (double)(max - min) * i));
            bigDecimal = bigDecimal.setScale(2, 4);
            return bigDecimal.floatValue();
        }
        return value;
    }
}

