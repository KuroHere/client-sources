/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.client.audio;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class MovingSoundMinecart
extends MovingSound {
    private final EntityMinecart minecart;
    private float distance = 0.0f;

    public MovingSoundMinecart(EntityMinecart minecartIn) {
        super(SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.NEUTRAL);
        this.minecart = minecartIn;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public void update() {
        if (this.minecart.isDead) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float)this.minecart.posX;
            this.yPosF = (float)this.minecart.posY;
            this.zPosF = (float)this.minecart.posZ;
            float f = MathHelper.sqrt(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
            if ((double)f >= 0.01) {
                this.distance = MathHelper.clamp(this.distance + 0.0025f, 0.0f, 1.0f);
                this.volume = 0.0f + MathHelper.clamp(f, 0.0f, 0.5f) * 0.7f;
            } else {
                this.distance = 0.0f;
                this.volume = 0.0f;
            }
        }
    }
}

