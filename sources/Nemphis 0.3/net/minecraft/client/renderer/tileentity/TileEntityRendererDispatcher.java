/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityPistonRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public class TileEntityRendererDispatcher {
    private Map mapSpecialRenderers = Maps.newHashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    private FontRenderer field_147557_n;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity field_147551_g;
    public float field_147562_h;
    public float field_147563_i;
    public double field_147560_j;
    public double field_147561_k;
    public double field_147558_l;
    private static final String __OBFID = "CL_00000963";

    private TileEntityRendererDispatcher() {
        this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        for (TileEntitySpecialRenderer var2 : this.mapSpecialRenderers.values()) {
            var2.setRendererDispatcher(this);
        }
    }

    public TileEntitySpecialRenderer getSpecialRendererByClass(Class p_147546_1_) {
        TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)this.mapSpecialRenderers.get(p_147546_1_);
        if (var2 == null && p_147546_1_ != TileEntity.class) {
            var2 = this.getSpecialRendererByClass(p_147546_1_.getSuperclass());
            this.mapSpecialRenderers.put(p_147546_1_, var2);
        }
        return var2;
    }

    public boolean hasSpecialRenderer(TileEntity p_147545_1_) {
        if (this.getSpecialRenderer(p_147545_1_) != null) {
            return true;
        }
        return false;
    }

    public TileEntitySpecialRenderer getSpecialRenderer(TileEntity p_147547_1_) {
        return p_147547_1_ == null ? null : this.getSpecialRendererByClass(p_147547_1_.getClass());
    }

    public void func_178470_a(World worldIn, TextureManager p_178470_2_, FontRenderer p_178470_3_, Entity p_178470_4_, float p_178470_5_) {
        if (this.worldObj != worldIn) {
            this.func_147543_a(worldIn);
        }
        this.renderEngine = p_178470_2_;
        this.field_147551_g = p_178470_4_;
        this.field_147557_n = p_178470_3_;
        this.field_147562_h = p_178470_4_.prevRotationYaw + (p_178470_4_.rotationYaw - p_178470_4_.prevRotationYaw) * p_178470_5_;
        this.field_147563_i = p_178470_4_.prevRotationPitch + (p_178470_4_.rotationPitch - p_178470_4_.prevRotationPitch) * p_178470_5_;
        this.field_147560_j = p_178470_4_.lastTickPosX + (p_178470_4_.posX - p_178470_4_.lastTickPosX) * (double)p_178470_5_;
        this.field_147561_k = p_178470_4_.lastTickPosY + (p_178470_4_.posY - p_178470_4_.lastTickPosY) * (double)p_178470_5_;
        this.field_147558_l = p_178470_4_.lastTickPosZ + (p_178470_4_.posZ - p_178470_4_.lastTickPosZ) * (double)p_178470_5_;
    }

    public void func_180546_a(TileEntity p_180546_1_, float p_180546_2_, int p_180546_3_) {
        if (p_180546_1_.getDistanceSq(this.field_147560_j, this.field_147561_k, this.field_147558_l) < p_180546_1_.getMaxRenderDistanceSquared()) {
            int var4 = this.worldObj.getCombinedLight(p_180546_1_.getPos(), 0);
            int var5 = var4 % 65536;
            int var6 = var4 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var5 / 1.0f, (float)var6 / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            BlockPos var7 = p_180546_1_.getPos();
            this.func_178469_a(p_180546_1_, (double)var7.getX() - staticPlayerX, (double)var7.getY() - staticPlayerY, (double)var7.getZ() - staticPlayerZ, p_180546_2_, p_180546_3_);
        }
    }

    public void renderTileEntityAt(TileEntity p_147549_1_, double p_147549_2_, double p_147549_4_, double p_147549_6_, float p_147549_8_) {
        this.func_178469_a(p_147549_1_, p_147549_2_, p_147549_4_, p_147549_6_, p_147549_8_, -1);
    }

    public void func_178469_a(TileEntity p_178469_1_, double p_178469_2_, double p_178469_4_, double p_178469_6_, float p_178469_8_, int p_178469_9_) {
        TileEntitySpecialRenderer var10 = this.getSpecialRenderer(p_178469_1_);
        if (var10 != null) {
            try {
                var10.renderTileEntityAt(p_178469_1_, p_178469_2_, p_178469_4_, p_178469_6_, p_178469_8_, p_178469_9_);
            }
            catch (Throwable var14) {
                CrashReport var12 = CrashReport.makeCrashReport(var14, "Rendering Block Entity");
                CrashReportCategory var13 = var12.makeCategory("Block Entity Details");
                p_178469_1_.addInfoToCrashReport(var13);
                throw new ReportedException(var12);
            }
        }
    }

    public void func_147543_a(World worldIn) {
        this.worldObj = worldIn;
    }

    public FontRenderer getFontRenderer() {
        return this.field_147557_n;
    }
}

