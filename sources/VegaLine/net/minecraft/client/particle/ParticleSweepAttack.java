/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleSweepAttack
extends Particle {
    private static final ResourceLocation SWEEP_TEXTURE = new ResourceLocation("textures/entity/sweep.png");
    private static final VertexFormat VERTEX_FORMAT = new VertexFormat().addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
    private int life;
    private final int lifeTime;
    private final TextureManager textureManager;
    private final float size;

    protected ParticleSweepAttack(TextureManager textureManagerIn, World worldIn, double x, double y, double z, double p_i46582_9_, double p_i46582_11_, double p_i46582_13_) {
        super(worldIn, x, y, z, 0.0, 0.0, 0.0);
        float f;
        this.textureManager = textureManagerIn;
        this.lifeTime = 4;
        this.particleRed = f = this.rand.nextFloat() * 0.6f + 0.4f;
        this.particleGreen = f;
        this.particleBlue = f;
        this.size = 1.0f - (float)p_i46582_9_ * 0.5f;
    }

    @Override
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        int i = (int)(((float)this.life + partialTicks) * 3.0f / (float)this.lifeTime);
        if (i <= 7) {
            this.textureManager.bindTexture(SWEEP_TEXTURE);
            float f = (float)(i % 4) / 4.0f;
            float f1 = f + 0.24975f;
            float f2 = (float)(i / 2) / 2.0f;
            float f3 = f2 + 0.4995f;
            float f4 = 1.0f * this.size;
            float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
            float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
            float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            RenderHelper.disableStandardItemLighting();
            worldRendererIn.begin(7, VERTEX_FORMAT);
            worldRendererIn.pos(f5 - rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4 * 0.5f, f7 - rotationYZ * f4 - rotationXZ * f4).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRendererIn.pos(f5 - rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4 * 0.5f, f7 - rotationYZ * f4 + rotationXZ * f4).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRendererIn.pos(f5 + rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4 * 0.5f, f7 + rotationYZ * f4 + rotationXZ * f4).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            worldRendererIn.pos(f5 + rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4 * 0.5f, f7 + rotationYZ * f4 - rotationXZ * f4).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0f).lightmap(0, 240).normal(0.0f, 1.0f, 0.0f).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.enableLighting();
        }
    }

    @Override
    public int getBrightnessForRender(float p_189214_1_) {
        return 61680;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.life;
        if (this.life == this.lifeTime) {
            this.setExpired();
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new ParticleSweepAttack(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}

