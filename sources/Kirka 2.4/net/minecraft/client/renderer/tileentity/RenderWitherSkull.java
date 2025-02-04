/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkull
extends Render {
    private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();
    private static final String __OBFID = "CL_00001035";

    public RenderWitherSkull(RenderManager p_i46129_1_) {
        super(p_i46129_1_);
    }

    private float func_82400_a(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
        float var4;
        for (var4 = p_82400_2_ - p_82400_1_; var4 < -180.0f; var4 += 360.0f) {
        }
        while (var4 >= 180.0f) {
            var4 -= 360.0f;
        }
        return p_82400_1_ + p_82400_3_ * var4;
    }

    public void doRender(EntityWitherSkull p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        float var10 = this.func_82400_a(p_76986_1_.prevRotationYaw, p_76986_1_.rotationYaw, p_76986_9_);
        float var11 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        float var12 = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(p_76986_1_);
        this.skeletonHeadModel.render(p_76986_1_, 0.0f, 0.0f, 0.0f, var10, var11, var12);
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected ResourceLocation func_180564_a(EntityWitherSkull p_180564_1_) {
        return p_180564_1_.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_180564_a((EntityWitherSkull)p_110775_1_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityWitherSkull)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}

