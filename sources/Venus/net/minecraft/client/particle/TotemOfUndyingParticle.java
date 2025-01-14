/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;

public class TotemOfUndyingParticle
extends SimpleAnimatedParticle {
    private TotemOfUndyingParticle(ClientWorld clientWorld, double d, double d2, double d3, double d4, double d5, double d6, IAnimatedSprite iAnimatedSprite) {
        super(clientWorld, d, d2, d3, iAnimatedSprite, -0.05f);
        this.motionX = d4;
        this.motionY = d5;
        this.motionZ = d6;
        this.particleScale *= 0.75f;
        this.maxAge = 60 + this.rand.nextInt(12);
        this.selectSpriteWithAge(iAnimatedSprite);
        if (this.rand.nextInt(4) == 0) {
            this.setColor(0.6f + this.rand.nextFloat() * 0.2f, 0.6f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        } else {
            this.setColor(0.1f + this.rand.nextFloat() * 0.2f, 0.4f + this.rand.nextFloat() * 0.3f, this.rand.nextFloat() * 0.2f);
        }
        this.setBaseAirFriction(0.6f);
    }

    public static class Factory
    implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite iAnimatedSprite) {
            this.spriteSet = iAnimatedSprite;
        }

        @Override
        public Particle makeParticle(BasicParticleType basicParticleType, ClientWorld clientWorld, double d, double d2, double d3, double d4, double d5, double d6) {
            return new TotemOfUndyingParticle(clientWorld, d, d2, d3, d4, d5, d6, this.spriteSet);
        }

        @Override
        public Particle makeParticle(IParticleData iParticleData, ClientWorld clientWorld, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.makeParticle((BasicParticleType)iParticleData, clientWorld, d, d2, d3, d4, d5, d6);
        }
    }
}

