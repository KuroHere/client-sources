/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderFish
extends Render<EntityFishHook> {
    private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

    public RenderFish(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
        block6: {
            entityplayer = entity.func_190619_l();
            if (entityplayer == null || this.renderOutlines) break block6;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y, (float)z);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            this.bindEntityTexture(entity);
            tessellator = Tessellator.getInstance();
            bufferbuilder = tessellator.getBuffer();
            i = true;
            j = 2;
            f = 0.0625f;
            f1 = 0.125f;
            f2 = 0.125f;
            f3 = 0.1875f;
            f4 = 1.0f;
            f5 = 0.5f;
            f6 = 0.5f;
            GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
            bufferbuilder.pos(-0.5, -0.5, 0.0).tex(0.0625, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(0.5, -0.5, 0.0).tex(0.125, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(0.5, 0.5, 0.0).tex(0.125, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(-0.5, 0.5, 0.0).tex(0.0625, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
            tessellator.draw();
            if (this.renderOutlines) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            k = entityplayer.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
            itemstack = entityplayer.getHeldItemMainhand();
            if (itemstack.getItem() != Items.FISHING_ROD) {
                k = -k;
            }
            f7 = entityplayer.getSwingProgress(partialTicks);
            f8 = MathHelper.sin(MathHelper.sqrt(f7) * 3.1415927f);
            f9 = (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292f;
            d0 = MathHelper.sin(f9);
            d1 = MathHelper.cos(f9);
            d2 = (double)k * 0.35;
            d3 = 0.8;
            if (this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0) ** GOTO lbl-1000
            Minecraft.getMinecraft();
            if (entityplayer == Minecraft.player) {
                f10 = this.renderManager.options.fovSetting;
                vec3d = new Vec3d((double)k * -0.36 * (double)(f10 /= 100.0f), -0.045 * (double)f10, 0.4);
                vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292f);
                vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292f);
                vec3d = vec3d.rotateYaw(f8 * 0.5f);
                vec3d = vec3d.rotatePitch(-f8 * 0.7f);
                d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)partialTicks + vec3d.xCoord;
                d5 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)partialTicks + vec3d.yCoord;
                d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)partialTicks + vec3d.zCoord;
                d7 = entityplayer.getEyeHeight();
            } else lbl-1000:
            // 2 sources

            {
                d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)partialTicks - d1 * d2 - d0 * 0.8;
                d5 = entityplayer.prevPosY + (double)entityplayer.getEyeHeight() + (entityplayer.posY - entityplayer.prevPosY) * (double)partialTicks - 0.45;
                d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)partialTicks - d0 * d2 + d1 * 0.8;
                d7 = entityplayer.isSneaking() != false ? -0.1875 : 0.0;
            }
            d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
            d8 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + 0.25;
            d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
            d10 = (float)(d4 - d13);
            d11 = (double)((float)(d5 - d8)) + d7;
            d12 = (float)(d6 - d9);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            l = 16;
            for (i1 = 0; i1 <= 16; ++i1) {
                f11 = (float)i1 / 16.0f;
                bufferbuilder.pos(x + d10 * (double)f11, y + d11 * (double)(f11 * f11 + f11) * 0.5 + 0.25, z + d12 * (double)f11).color(0, 0, 0, 255).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFishHook entity) {
        return FISH_PARTICLES;
    }
}

