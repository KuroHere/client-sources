/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuReciepient;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiSpectator
extends Gui
implements ISpectatorMenuReciepient {
    private static final ResourceLocation field_175267_f = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation field_175269_a = new ResourceLocation("textures/gui/spectator_widgets.png");
    private final Minecraft field_175268_g;
    private long field_175270_h;
    private SpectatorMenu field_175271_i;
    private static final String __OBFID = "CL_00001940";

    public GuiSpectator(Minecraft mcIn) {
        this.field_175268_g = mcIn;
    }

    public void func_175260_a(int p_175260_1_) {
        this.field_175270_h = Minecraft.getSystemTime();
        if (this.field_175271_i != null) {
            this.field_175271_i.func_178644_b(p_175260_1_);
        } else {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }

    private float func_175265_c() {
        long var1 = this.field_175270_h - Minecraft.getSystemTime() + 5000L;
        return MathHelper.clamp_float((float)var1 / 2000.0f, 0.0f, 1.0f);
    }

    public void func_175264_a(ScaledResolution p_175264_1_, float p_175264_2_) {
        if (this.field_175271_i != null) {
            float var3 = this.func_175265_c();
            if (var3 <= 0.0f) {
                this.field_175271_i.func_178641_d();
            } else {
                int var4 = ScaledResolution.getScaledWidth() / 2;
                float var5 = this.zLevel;
                this.zLevel = -90.0f;
                float var6 = (float)ScaledResolution.getScaledHeight() - 22.0f * var3;
                SpectatorDetails var7 = this.field_175271_i.func_178646_f();
                this.func_175258_a(p_175264_1_, var3, var4, var6, var7);
                this.zLevel = var5;
            }
        }
    }

    protected void func_175258_a(ScaledResolution p_175258_1_, float p_175258_2_, int p_175258_3_, float p_175258_4_, SpectatorDetails p_175258_5_) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_175258_2_);
        this.field_175268_g.getTextureManager().bindTexture(field_175267_f);
        this.func_175174_a(p_175258_3_ - 91, p_175258_4_, 0, 0, 182, 22);
        if (p_175258_5_.func_178681_b() >= 0) {
            this.func_175174_a(p_175258_3_ - 91 - 1 + p_175258_5_.func_178681_b() * 20, p_175258_4_ - 1.0f, 0, 22, 24, 22);
        }
        RenderHelper.enableGUIStandardItemLighting();
        for (int var6 = 0; var6 < 9; ++var6) {
            this.func_175266_a(var6, ScaledResolution.getScaledWidth() / 2 - 90 + var6 * 20 + 2, p_175258_4_ + 3.0f, p_175258_2_, p_175258_5_.func_178680_a(var6));
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }

    private void func_175266_a(int p_175266_1_, int p_175266_2_, float p_175266_3_, float p_175266_4_, ISpectatorMenuObject p_175266_5_) {
        this.field_175268_g.getTextureManager().bindTexture(field_175269_a);
        if (p_175266_5_ != SpectatorMenu.field_178657_a) {
            int var6 = (int)(p_175266_4_ * 255.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(p_175266_2_, p_175266_3_, 0.0f);
            float var7 = p_175266_5_.func_178662_A_() ? 1.0f : 0.25f;
            GlStateManager.color(var7, var7, var7, p_175266_4_);
            p_175266_5_.func_178663_a(var7, var6);
            GlStateManager.popMatrix();
            String var8 = String.valueOf(GameSettings.getKeyDisplayString(this.field_175268_g.gameSettings.keyBindsHotbar[p_175266_1_].getKeyCode()));
            if (var6 > 3 && p_175266_5_.func_178662_A_()) {
                this.field_175268_g.fontRendererObj.func_175063_a(var8, p_175266_2_ + 19 - 2 - this.field_175268_g.fontRendererObj.getStringWidth(var8), p_175266_3_ + 6.0f + 3.0f, 16777215 + (var6 << 24));
            }
        }
    }

    public void func_175263_a(ScaledResolution p_175263_1_) {
        int var2 = (int)(this.func_175265_c() * 255.0f);
        if (var2 > 3 && this.field_175271_i != null) {
            String var4;
            ISpectatorMenuObject var3 = this.field_175271_i.func_178645_b();
            String string = var4 = var3 != SpectatorMenu.field_178657_a ? var3.func_178664_z_().getFormattedText() : this.field_175271_i.func_178650_c().func_178670_b().getFormattedText();
            if (var4 != null) {
                int var5 = (ScaledResolution.getScaledWidth() - this.field_175268_g.fontRendererObj.getStringWidth(var4)) / 2;
                int var6 = ScaledResolution.getScaledHeight() - 35;
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.field_175268_g.fontRendererObj.func_175063_a(var4, var5, var6, 16777215 + (var2 << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public void func_175257_a(SpectatorMenu p_175257_1_) {
        this.field_175271_i = null;
        this.field_175270_h = 0L;
    }

    public boolean func_175262_a() {
        return this.field_175271_i != null;
    }

    public void func_175259_b(int p_175259_1_) {
        int var2;
        for (var2 = this.field_175271_i.func_178648_e() + p_175259_1_; !(var2 < 0 || var2 > 8 || this.field_175271_i.func_178643_a(var2) != SpectatorMenu.field_178657_a && this.field_175271_i.func_178643_a(var2).func_178662_A_()); var2 += p_175259_1_) {
        }
        if (var2 >= 0 && var2 <= 8) {
            this.field_175271_i.func_178644_b(var2);
            this.field_175270_h = Minecraft.getSystemTime();
        }
    }

    public void func_175261_b() {
        this.field_175270_h = Minecraft.getSystemTime();
        if (this.func_175262_a()) {
            int var1 = this.field_175271_i.func_178648_e();
            if (var1 != -1) {
                this.field_175271_i.func_178644_b(var1);
            }
        } else {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }
}

