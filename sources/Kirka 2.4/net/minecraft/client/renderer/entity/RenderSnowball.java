/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderSnowball
extends Render {
    protected final Item field_177084_a;
    private final RenderItem field_177083_e;
    private static final String __OBFID = "CL_00001008";

    public RenderSnowball(RenderManager p_i46137_1_, Item p_i46137_2_, RenderItem p_i46137_3_) {
        super(p_i46137_1_);
        this.field_177084_a = p_i46137_2_;
        this.field_177083_e = p_i46137_3_;
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        this.bindTexture(TextureMap.locationBlocksTexture);
        this.field_177083_e.func_175043_b(this.func_177082_d(p_76986_1_));
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public ItemStack func_177082_d(Entity p_177082_1_) {
        return new ItemStack(this.field_177084_a, 1, 0);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return TextureMap.locationBlocksTexture;
    }
}

