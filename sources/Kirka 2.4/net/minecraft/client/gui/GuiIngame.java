/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import me.thekirkayt.client.module.Module;
import me.thekirkayt.client.module.modules.render.HudKirkaB11;
import me.thekirkayt.event.Event;
import me.thekirkayt.event.events.Render2DEvent;
import me.thekirkayt.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.GuiStreamIndicator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;

public class GuiIngame
extends Gui {
    private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    private final Random rand = new Random();
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    public GuiNewChat persistantChatGUI;
    private final GuiStreamIndicator streamIndicator;
    private int updateCounter;
    private String recordPlaying = "";
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness = 1.0f;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSpectator field_175197_u;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private int field_175195_w;
    private String field_175201_x = "";
    private String field_175200_y = "";
    private int field_175199_z;
    private int field_175192_A;
    private int field_175193_B;
    private int field_175194_C = 0;
    private int field_175189_D = 0;
    private long field_175190_E = 0L;
    private long field_175191_F = 0L;
    private static final String __OBFID = "CL_00000661";

    public GuiIngame(Minecraft mcIn) {
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.field_175197_u = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.streamIndicator = new GuiStreamIndicator(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.func_175177_a();
    }

    public void func_175177_a() {
        this.field_175199_z = 10;
        this.field_175192_A = 70;
        this.field_175193_B = 20;
    }

    public void func_175180_a(float p_175180_1_) {
        ScoreObjective var16;
        float var7;
        int var10;
        float var6;
        int var8;
        int var9;
        int var11;
        ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int var3 = ScaledResolution.getScaledWidth();
        int var4 = ScaledResolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.func_180480_a(Minecraft.thePlayer.getBrightness(p_175180_1_), var2);
        } else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        ItemStack var5 = Minecraft.thePlayer.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && var5 != null && var5.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.func_180476_e(var2);
        }
        if (!Minecraft.thePlayer.isPotionActive(Potion.confusion) && (var6 = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * p_175180_1_) > 0.0f) {
            this.func_180474_b(var6, var2);
        }
        if (Minecraft.playerController.enableEverythingIsScrewedUpMode()) {
            this.field_175197_u.func_175264_a(var2, p_175180_1_);
        } else {
            this.func_180479_a(var2, p_175180_1_);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(icons);
        GlStateManager.enableBlend();
        if (this.func_175183_b()) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(var3 / 2 - 7, var4 / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();
        if (Minecraft.playerController.shouldDrawHUD()) {
            this.func_180477_d(var2);
        }
        GlStateManager.disableBlend();
        if (Minecraft.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            var11 = Minecraft.thePlayer.getSleepTimer();
            var7 = (float)var11 / 100.0f;
            if (var7 > 1.0f) {
                var7 = 1.0f - (float)(var11 - 100) / 10.0f;
            }
            var8 = (int)(220.0f * var7) << 24 | 1052704;
            GuiIngame.drawRect(0, 0, var3, var4, var8);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        var11 = var3 / 2 - 91;
        if (Minecraft.thePlayer.isRidingHorse()) {
            this.func_175186_a(var2, var11);
        } else if (Minecraft.playerController.gameIsSurvivalOrAdventure()) {
            this.func_175176_b(var2, var11);
        }
        if (this.mc.gameSettings.heldItemTooltips && !Minecraft.playerController.enableEverythingIsScrewedUpMode()) {
            this.func_175182_a(var2);
        } else if (Minecraft.thePlayer.func_175149_v()) {
            this.field_175197_u.func_175263_a(var2);
        }
        if (this.mc.isDemo()) {
            this.func_175185_b(var2);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.func_175237_a(var2);
        }
        new Render2DEvent(var3, var4).call();
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            var7 = (float)this.recordPlayingUpFor - p_175180_1_;
            var8 = (int)(var7 * 255.0f / 20.0f);
            if (var8 > 255) {
                var8 = 255;
            }
            if (var8 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(var3 / 2, var4 - 68, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                var9 = 16777215;
                if (this.recordIsPlaying) {
                    var9 = Color.HSBtoRGB(var7 / 50.0f, 0.7f, 0.6f) & 16777215;
                }
                this.func_175179_f().drawString(this.recordPlaying, -this.func_175179_f().getStringWidth(this.recordPlaying) / 2, -4, var9 + (var8 << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            var7 = (float)this.field_175195_w - p_175180_1_;
            var8 = 255;
            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                float var14 = (float)(this.field_175199_z + this.field_175192_A + this.field_175193_B) - var7;
                var8 = (int)(var14 * 255.0f / (float)this.field_175199_z);
            }
            if (this.field_175195_w <= this.field_175193_B) {
                var8 = (int)(var7 * 255.0f / (float)this.field_175193_B);
            }
            if ((var8 = MathHelper.clamp_int(var8, 0, 255)) > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(var3 / 2, var4 / 2, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                var9 = var8 << 24 & -16777216;
                this.func_175179_f().func_175065_a(this.field_175201_x, -this.func_175179_f().getStringWidth(this.field_175201_x) / 2, -10.0f, 16777215 | var9, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.func_175179_f().func_175065_a(this.field_175200_y, -this.func_175179_f().getStringWidth(this.field_175200_y) / 2, 5.0f, 16777215 | var9, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        Scoreboard var12 = Minecraft.theWorld.getScoreboard();
        ScoreObjective var13 = null;
        ScorePlayerTeam var15 = var12.getPlayersTeam(Minecraft.thePlayer.getName());
        if (var15 != null && (var10 = var15.func_178775_l().func_175746_b()) >= 0) {
            var13 = var12.getObjectiveInDisplaySlot(3 + var10);
        }
        ScoreObjective scoreObjective = var16 = var13 != null ? var13 : var12.getObjectiveInDisplaySlot(1);
        if (var16 != null) {
            this.func_180475_a(var16, var2);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, var4 - 48, 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        var16 = var12.getObjectiveInDisplaySlot(0);
        if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning() || Minecraft.thePlayer.sendQueue.func_175106_d().size() > 1 || var16 != null)) {
            this.overlayPlayerList.func_175246_a(true);
            this.overlayPlayerList.func_175249_a(var3, var12, var16);
        } else {
            this.overlayPlayerList.func_175246_a(false);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }

    protected void func_180479_a(ScaledResolution p_180479_1_, float p_180479_2_) {
        HudKirkaB11 hudkirkab11 = (HudKirkaB11)Module.getModByName("hudkirkab11");
        if (hudkirkab11.isEnabled()) {
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.drawRectNoBlend(0.0f, ScaledResolution.getScaledHeight() - 22, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), Integer.MIN_VALUE);
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.drawRectNoBlend(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight() - 22, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), -862348903);
            int item = Minecraft.thePlayer.inventory.currentItem + 1;
            float xPos = 21.0f;
            switch (item) {
                case 1: {
                    xPos = 21.5f;
                    break;
                }
                case 2: {
                    xPos = 41.5f;
                    break;
                }
                case 3: {
                    xPos = 61.5f;
                    break;
                }
                case 4: {
                    xPos = 81.5f;
                    break;
                }
                case 5: {
                    xPos = 101.5f;
                    break;
                }
                case 6: {
                    xPos = 121.5f;
                    break;
                }
                case 7: {
                    xPos = 141.5f;
                    break;
                }
                case 8: {
                    xPos = 161.5f;
                    break;
                }
                case 9: {
                    xPos = 181.5f;
                }
            }
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.getScaledRes();
            RenderUtils.drawRectNoBlend((float)(ScaledResolution.getScaledWidth() / 2 - 91) + xPos - 21.0f, ScaledResolution.getScaledHeight() - 22, (float)(ScaledResolution.getScaledWidth() / 2 - 91) + xPos, ScaledResolution.getScaledHeight() - 2, -2130706433);
        }
        if (this.mc.func_175606_aa() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer var3 = (EntityPlayer)this.mc.func_175606_aa();
            int var4 = ScaledResolution.getScaledWidth() / 2;
            float var5 = this.zLevel;
            this.zLevel = -90.0f;
            if (!hudkirkab11.isEnabled()) {
                this.drawTexturedModalRect(var4 - 91, ScaledResolution.getScaledHeight() - 22, 0, 0, 182, 22);
                this.drawTexturedModalRect(var4 - 91 - 1 + var3.inventory.currentItem * 20, ScaledResolution.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            }
            this.zLevel = var5;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int var6 = 0; var6 < 9; ++var6) {
                int var7 = ScaledResolution.getScaledWidth() / 2 - 90 + var6 * 20 + 2;
                int var8 = ScaledResolution.getScaledHeight() - 16 - 3;
                this.func_175184_a(var6, var7, var8, p_180479_2_, var3);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public void func_175186_a(ScaledResolution p_175186_1_, int p_175186_2_) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        float var3 = Minecraft.thePlayer.getHorseJumpPower();
        int var4 = 182;
        int var5 = (int)(var3 * (float)(var4 + 1));
        int var6 = ScaledResolution.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(p_175186_2_, var6, 0, 84, var4, 5);
        if (var5 > 0) {
            this.drawTexturedModalRect(p_175186_2_, var6, 0, 89, var5, 5);
        }
        this.mc.mcProfiler.endSection();
    }

    public void func_175176_b(ScaledResolution p_175176_1_, int p_175176_2_) {
        int var6;
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int var3 = Minecraft.thePlayer.xpBarCap();
        if (var3 > 0) {
            int var4 = 182;
            int var5 = (int)(Minecraft.thePlayer.experience * (float)(var4 + 1));
            var6 = ScaledResolution.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(p_175176_2_, var6, 0, 64, var4, 5);
            if (var5 > 0) {
                this.drawTexturedModalRect(p_175176_2_, var6, 0, 69, var5, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (Minecraft.thePlayer.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            int var9 = 8453920;
            String var10 = "" + Minecraft.thePlayer.experienceLevel;
            var6 = (ScaledResolution.getScaledWidth() - this.func_175179_f().getStringWidth(var10)) / 2;
            int var7 = ScaledResolution.getScaledHeight() - 31 - 4;
            boolean var8 = false;
            this.func_175179_f().drawString(var10, var6 + 1, var7, 0);
            this.func_175179_f().drawString(var10, var6 - 1, var7, 0);
            this.func_175179_f().drawString(var10, var6, var7 + 1, 0);
            this.func_175179_f().drawString(var10, var6, var7 - 1, 0);
            this.func_175179_f().drawString(var10, var6, var7, var9);
            this.mc.mcProfiler.endSection();
        }
    }

    public void func_175182_a(ScaledResolution p_175182_1_) {
        this.mc.mcProfiler.startSection("toolHighlight");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            int var5;
            String var2 = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                var2 = (Object)((Object)EnumChatFormatting.ITALIC) + var2;
            }
            int var3 = (ScaledResolution.getScaledWidth() - this.func_175179_f().getStringWidth(var2)) / 2;
            int var4 = ScaledResolution.getScaledHeight() - 59;
            if (!Minecraft.playerController.shouldDrawHUD()) {
                var4 += 14;
            }
            if ((var5 = (int)((float)this.remainingHighlightTicks * 256.0f / 10.0f)) > 255) {
                var5 = 255;
            }
            if (var5 > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.func_175179_f().func_175063_a(var2, var3, var4, 16777215 + (var5 << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }

    public void func_175185_b(ScaledResolution p_175185_1_) {
        this.mc.mcProfiler.startSection("demo");
        String var2 = "";
        var2 = Minecraft.theWorld.getTotalWorldTime() >= 120500L ? I18n.format("demo.demoExpired", new Object[0]) : I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - Minecraft.theWorld.getTotalWorldTime())));
        int var3 = this.func_175179_f().getStringWidth(var2);
        this.func_175179_f().func_175063_a(var2, ScaledResolution.getScaledWidth() - var3 - 10, 5.0f, 16777215);
        this.mc.mcProfiler.endSection();
    }

    protected boolean func_175183_b() {
        if (this.mc.gameSettings.showDebugInfo && !Minecraft.thePlayer.func_175140_cp() && !this.mc.gameSettings.field_178879_v) {
            return false;
        }
        if (Minecraft.playerController.enableEverythingIsScrewedUpMode()) {
            BlockPos var1;
            if (this.mc.pointedEntity != null) {
                return true;
            }
            return this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && Minecraft.theWorld.getTileEntity(var1 = this.mc.objectMouseOver.func_178782_a()) instanceof IInventory;
        }
        return true;
    }

    public void func_180478_c(ScaledResolution p_180478_1_) {
        this.streamIndicator.render(ScaledResolution.getScaledWidth() - 10, 10);
    }

    private void func_180475_a(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_) {
        Scoreboard var3 = p_180475_1_.getScoreboard();
        Collection var4 = var3.getSortedScores(p_180475_1_);
        ArrayList var5 = Lists.newArrayList((Iterable)Iterables.filter((Iterable)var4, (Predicate)new Predicate(){
            private static final String __OBFID = "CL_00001958";

            public boolean func_178903_a(Score p_178903_1_) {
                return p_178903_1_.getPlayerName() != null && !p_178903_1_.getPlayerName().startsWith("#");
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_178903_a((Score)p_apply_1_);
            }
        }));
        ArrayList var21 = var5.size() > 15 ? Lists.newArrayList((Iterable)Iterables.skip((Iterable)var5, (int)(var4.size() - 15))) : var5;
        int var6 = this.func_175179_f().getStringWidth(p_180475_1_.getDisplayName());
        for (Score var8 : var21) {
            ScorePlayerTeam var9 = var3.getPlayersTeam(var8.getPlayerName());
            String var10 = String.valueOf(ScorePlayerTeam.formatPlayerName(var9, var8.getPlayerName())) + ": " + (Object)((Object)EnumChatFormatting.RED) + var8.getScorePoints();
            var6 = Math.max(var6, this.func_175179_f().getStringWidth(var10));
        }
        int var22 = var21.size() * this.func_175179_f().FONT_HEIGHT;
        int var23 = ScaledResolution.getScaledHeight() / 2 + var22 / 3;
        int var24 = 3;
        int var25 = ScaledResolution.getScaledWidth() - var6 - var24;
        int var11 = 0;
        for (Score var13 : var21) {
            ScorePlayerTeam var14 = var3.getPlayersTeam(var13.getPlayerName());
            String var15 = ScorePlayerTeam.formatPlayerName(var14, var13.getPlayerName());
            String var16 = "" + (Object)((Object)EnumChatFormatting.RED) + var13.getScorePoints();
            int var18 = var23 - ++var11 * this.func_175179_f().FONT_HEIGHT;
            int var19 = ScaledResolution.getScaledWidth() - var24 + 2;
            GuiIngame.drawRect(var25 - 2, var18, var19, var18 + this.func_175179_f().FONT_HEIGHT, 1342177280);
            this.func_175179_f().drawString(var15, var25, var18, 553648127);
            this.func_175179_f().drawString(var16, var19 - this.func_175179_f().getStringWidth(var16), var18, 553648127);
            if (var11 != var21.size()) continue;
            String var20 = p_180475_1_.getDisplayName();
            GuiIngame.drawRect(var25 - 2, var18 - this.func_175179_f().FONT_HEIGHT - 1, var19, var18 - 1, 1610612736);
            GuiIngame.drawRect(var25 - 2, var18 - 1, var19, var18, 1342177280);
            this.func_175179_f().drawString(var20, var25 + var6 / 2 - this.func_175179_f().getStringWidth(var20) / 2, var18 - this.func_175179_f().FONT_HEIGHT, 553648127);
        }
    }

    private void func_180477_d(ScaledResolution p_180477_1_) {
        if (this.mc.func_175606_aa() instanceof EntityPlayer) {
            int var23;
            int var22;
            int var36;
            int var25;
            int var26;
            int var27;
            boolean var4;
            EntityPlayer var2 = (EntityPlayer)this.mc.func_175606_aa();
            int var3 = MathHelper.ceiling_float_int(var2.getHealth());
            boolean bl = var4 = this.field_175191_F > (long)this.updateCounter && (this.field_175191_F - (long)this.updateCounter) / 3L % 2L == 1L;
            if (var3 < this.field_175194_C && var2.hurtResistantTime > 0) {
                this.field_175190_E = Minecraft.getSystemTime();
                this.field_175191_F = this.updateCounter + 20;
            } else if (var3 > this.field_175194_C && var2.hurtResistantTime > 0) {
                this.field_175190_E = Minecraft.getSystemTime();
                this.field_175191_F = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.field_175190_E > 1000L) {
                this.field_175194_C = var3;
                this.field_175189_D = var3;
                this.field_175190_E = Minecraft.getSystemTime();
            }
            this.field_175194_C = var3;
            int var5 = this.field_175189_D;
            this.rand.setSeed(this.updateCounter * 312871);
            boolean var6 = false;
            FoodStats var7 = var2.getFoodStats();
            int var8 = var7.getFoodLevel();
            int var9 = var7.getPrevFoodLevel();
            IAttributeInstance var10 = var2.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int var11 = ScaledResolution.getScaledWidth() / 2 - 91;
            int var12 = ScaledResolution.getScaledWidth() / 2 + 91;
            int var13 = ScaledResolution.getScaledHeight() - 39;
            float var14 = (float)var10.getAttributeValue();
            float var15 = var2.getAbsorptionAmount();
            int var16 = MathHelper.ceiling_float_int((var14 + var15) / 2.0f / 10.0f);
            int var17 = Math.max(10 - (var16 - 2), 3);
            int var18 = var13 - (var16 - 1) * var17 - 10;
            float var19 = var15;
            int var20 = var2.getTotalArmorValue();
            int var21 = -1;
            if (var2.isPotionActive(Potion.regeneration)) {
                var21 = this.updateCounter % MathHelper.ceiling_float_int(var14 + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            for (var22 = 0; var22 < 10; ++var22) {
                if (var20 <= 0) continue;
                var23 = var11 + var22 * 8;
                if (var22 * 2 + 1 < var20) {
                    this.drawTexturedModalRect(var23, var18, 34, 9, 9, 9);
                }
                if (var22 * 2 + 1 == var20) {
                    this.drawTexturedModalRect(var23, var18, 25, 9, 9, 9);
                }
                if (var22 * 2 + 1 <= var20) continue;
                this.drawTexturedModalRect(var23, var18, 16, 9, 9, 9);
            }
            this.mc.mcProfiler.endStartSection("health");
            for (var22 = MathHelper.ceiling_float_int((float)((var14 + var15) / 2.0f)) - 1; var22 >= 0; --var22) {
                var23 = 16;
                if (var2.isPotionActive(Potion.poison)) {
                    var23 += 36;
                } else if (var2.isPotionActive(Potion.wither)) {
                    var23 += 72;
                }
                int var24 = 0;
                if (var4) {
                    var24 = 1;
                }
                var25 = MathHelper.ceiling_float_int((float)(var22 + 1) / 10.0f) - 1;
                var26 = var11 + var22 % 10 * 8;
                var27 = var13 - var25 * var17;
                if (var3 <= 4) {
                    var27 += this.rand.nextInt(2);
                }
                if (var22 == var21) {
                    var27 -= 2;
                }
                int var28 = 0;
                if (var2.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    var28 = 5;
                }
                this.drawTexturedModalRect(var26, var27, 16 + var24 * 9, 9 * var28, 9, 9);
                if (var4) {
                    if (var22 * 2 + 1 < var5) {
                        this.drawTexturedModalRect(var26, var27, var23 + 54, 9 * var28, 9, 9);
                    }
                    if (var22 * 2 + 1 == var5) {
                        this.drawTexturedModalRect(var26, var27, var23 + 63, 9 * var28, 9, 9);
                    }
                }
                if (var19 > 0.0f) {
                    if (var19 == var15 && var15 % 2.0f == 1.0f) {
                        this.drawTexturedModalRect(var26, var27, var23 + 153, 9 * var28, 9, 9);
                    } else {
                        this.drawTexturedModalRect(var26, var27, var23 + 144, 9 * var28, 9, 9);
                    }
                    var19 -= 2.0f;
                    continue;
                }
                if (var22 * 2 + 1 < var3) {
                    this.drawTexturedModalRect(var26, var27, var23 + 36, 9 * var28, 9, 9);
                }
                if (var22 * 2 + 1 != var3) continue;
                this.drawTexturedModalRect(var26, var27, var23 + 45, 9 * var28, 9, 9);
            }
            Entity var34 = var2.ridingEntity;
            if (var34 == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (var23 = 0; var23 < 10; ++var23) {
                    var36 = var13;
                    var25 = 16;
                    int var38 = 0;
                    if (var2.isPotionActive(Potion.hunger)) {
                        var25 += 36;
                        var38 = 13;
                    }
                    if (var2.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (var8 * 3 + 1) == 0) {
                        var36 = var13 + (this.rand.nextInt(3) - 1);
                    }
                    if (var6) {
                        var38 = 1;
                    }
                    var27 = var12 - var23 * 8 - 9;
                    this.drawTexturedModalRect(var27, var36, 16 + var38 * 9, 27, 9, 9);
                    if (var6) {
                        if (var23 * 2 + 1 < var9) {
                            this.drawTexturedModalRect(var27, var36, var25 + 54, 27, 9, 9);
                        }
                        if (var23 * 2 + 1 == var9) {
                            this.drawTexturedModalRect(var27, var36, var25 + 63, 27, 9, 9);
                        }
                    }
                    if (var23 * 2 + 1 < var8) {
                        this.drawTexturedModalRect(var27, var36, var25 + 36, 27, 9, 9);
                    }
                    if (var23 * 2 + 1 != var8) continue;
                    this.drawTexturedModalRect(var27, var36, var25 + 45, 27, 9, 9);
                }
            } else if (var34 instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase var35 = (EntityLivingBase)var34;
                var36 = (int)Math.ceil(var35.getHealth());
                float var37 = var35.getMaxHealth();
                var26 = (int)(var37 + 0.5f) / 2;
                if (var26 > 30) {
                    var26 = 30;
                }
                var27 = var13;
                int var39 = 0;
                while (var26 > 0) {
                    int var29 = Math.min(var26, 10);
                    var26 -= var29;
                    for (int var30 = 0; var30 < var29; ++var30) {
                        int var31 = 52;
                        int var32 = 0;
                        if (var6) {
                            var32 = 1;
                        }
                        int var33 = var12 - var30 * 8 - 9;
                        this.drawTexturedModalRect(var33, var27, var31 + var32 * 9, 9, 9, 9);
                        if (var30 * 2 + 1 + var39 < var36) {
                            this.drawTexturedModalRect(var33, var27, var31 + 36, 9, 9, 9);
                        }
                        if (var30 * 2 + 1 + var39 != var36) continue;
                        this.drawTexturedModalRect(var33, var27, var31 + 45, 9, 9, 9);
                    }
                    var27 -= 10;
                    var39 += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (var2.isInsideOfMaterial(Material.water)) {
                var23 = Minecraft.thePlayer.getAir();
                var36 = MathHelper.ceiling_double_int((double)(var23 - 2) * 10.0 / 300.0);
                var25 = MathHelper.ceiling_double_int((double)var23 * 10.0 / 300.0) - var36;
                for (var26 = 0; var26 < var36 + var25; ++var26) {
                    if (var26 < var36) {
                        this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 16, 18, 9, 9);
                        continue;
                    }
                    this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 25, 18, 9, 9);
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            FontRenderer var1 = this.mc.fontRendererObj;
            ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int var3 = ScaledResolution.getScaledWidth();
            int var4 = 182;
            int var5 = var3 / 2 - var4 / 2;
            int var6 = (int)(BossStatus.healthScale * (float)(var4 + 1));
            int var7 = 12;
            this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
            this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
            if (var6 > 0) {
                this.drawTexturedModalRect(var5, var7, 0, 79, var6, 5);
            }
            String var8 = BossStatus.bossName;
            this.func_175179_f().func_175063_a(var8, var3 / 2 - this.func_175179_f().getStringWidth(var8) / 2, var7 - 10, 16777215);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    private void func_180476_e(ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0, ScaledResolution.getScaledHeight(), -90.0, 0.0, 1.0);
        var3.addVertexWithUV(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), -90.0, 1.0, 1.0);
        var3.addVertexWithUV(ScaledResolution.getScaledWidth(), 0.0, -90.0, 1.0, 0.0);
        var3.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
        var2.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void func_180480_a(float p_180480_1_, ScaledResolution p_180480_2_) {
        p_180480_1_ = 1.0f - p_180480_1_;
        p_180480_1_ = MathHelper.clamp_float(p_180480_1_, 0.0f, 1.0f);
        WorldBorder var3 = Minecraft.theWorld.getWorldBorder();
        float var4 = (float)var3.getClosestDistance(Minecraft.thePlayer);
        double var5 = Math.min(var3.func_177749_o() * (double)var3.getWarningTime() * 1000.0, Math.abs(var3.getTargetSize() - var3.getDiameter()));
        double var7 = Math.max((double)var3.getWarningDistance(), var5);
        var4 = (double)var4 < var7 ? 1.0f - (float)((double)var4 / var7) : 0.0f;
        this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(p_180480_1_ - this.prevVignetteBrightness) * 0.01);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
        if (var4 > 0.0f) {
            GlStateManager.color(0.0f, var4, var4, 1.0f);
        } else {
            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
        }
        this.mc.getTextureManager().bindTexture(vignetteTexPath);
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(0.0, ScaledResolution.getScaledHeight(), -90.0, 0.0, 1.0);
        var10.addVertexWithUV(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), -90.0, 1.0, 1.0);
        var10.addVertexWithUV(ScaledResolution.getScaledWidth(), 0.0, -90.0, 1.0, 0.0);
        var10.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
        var9.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    private void func_180474_b(float p_180474_1_, ScaledResolution p_180474_2_) {
        if (p_180474_1_ < 1.0f) {
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ = p_180474_1_ * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_180474_1_);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite var3 = this.mc.getBlockRendererDispatcher().func_175023_a().func_178122_a(Blocks.portal.getDefaultState());
        float var4 = var3.getMinU();
        float var5 = var3.getMinV();
        float var6 = var3.getMaxU();
        float var7 = var3.getMaxV();
        Tessellator var8 = Tessellator.getInstance();
        WorldRenderer var9 = var8.getWorldRenderer();
        var9.startDrawingQuads();
        var9.addVertexWithUV(0.0, ScaledResolution.getScaledHeight(), -90.0, var4, var7);
        var9.addVertexWithUV(ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), -90.0, var6, var7);
        var9.addVertexWithUV(ScaledResolution.getScaledWidth(), 0.0, -90.0, var6, var5);
        var9.addVertexWithUV(0.0, 0.0, -90.0, var4, var5);
        var8.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void func_175184_a(int p_175184_1_, int p_175184_2_, int p_175184_3_, float p_175184_4_, EntityPlayer p_175184_5_) {
        ItemStack var6 = p_175184_5_.inventory.mainInventory[p_175184_1_];
        if (var6 != null) {
            float var7 = (float)var6.animationsToGo - p_175184_4_;
            if (var7 > 0.0f) {
                GlStateManager.pushMatrix();
                float var8 = 1.0f + var7 / 5.0f;
                GlStateManager.translate(p_175184_2_ + 8, p_175184_3_ + 12, 0.0f);
                GlStateManager.scale(1.0f / var8, (var8 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate(-(p_175184_2_ + 8), -(p_175184_3_ + 12), 0.0f);
            }
            this.itemRenderer.func_180450_b(var6, p_175184_2_, p_175184_3_);
            if (var7 > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.func_175030_a(this.mc.fontRendererObj, var6, p_175184_2_, p_175184_3_);
        }
    }

    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }
        if (this.field_175195_w > 0) {
            --this.field_175195_w;
            if (this.field_175195_w <= 0) {
                this.field_175201_x = "";
                this.field_175200_y = "";
            }
        }
        ++this.updateCounter;
        this.streamIndicator.func_152439_a();
        if (Minecraft.thePlayer != null) {
            ItemStack var1 = Minecraft.thePlayer.inventory.getCurrentItem();
            if (var1 == null) {
                this.remainingHighlightTicks = 0;
            } else if (this.highlightingItemStack != null && var1.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(var1, this.highlightingItemStack) && (var1.isItemStackDamageable() || var1.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            } else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = var1;
        }
    }

    public void setRecordPlayingMessage(String p_73833_1_) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", p_73833_1_), true);
    }

    public void setRecordPlaying(String p_110326_1_, boolean p_110326_2_) {
        this.recordPlaying = p_110326_1_;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = p_110326_2_;
    }

    public void func_175178_a(String p_175178_1_, String p_175178_2_, int p_175178_3_, int p_175178_4_, int p_175178_5_) {
        if (p_175178_1_ == null && p_175178_2_ == null && p_175178_3_ < 0 && p_175178_4_ < 0 && p_175178_5_ < 0) {
            this.field_175201_x = "";
            this.field_175200_y = "";
            this.field_175195_w = 0;
        } else if (p_175178_1_ != null) {
            this.field_175201_x = p_175178_1_;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        } else if (p_175178_2_ != null) {
            this.field_175200_y = p_175178_2_;
        } else {
            if (p_175178_3_ >= 0) {
                this.field_175199_z = p_175178_3_;
            }
            if (p_175178_4_ >= 0) {
                this.field_175192_A = p_175178_4_;
            }
            if (p_175178_5_ >= 0) {
                this.field_175193_B = p_175178_5_;
            }
            if (this.field_175195_w > 0) {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }

    public void func_175188_a(IChatComponent p_175188_1_, boolean p_175188_2_) {
        this.setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
    }

    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    public FontRenderer func_175179_f() {
        return this.mc.fontRendererObj;
    }

    public GuiSpectator func_175187_g() {
        return this.field_175197_u;
    }

    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }

}

