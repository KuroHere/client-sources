/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityBodyHelper {
    private EntityLivingBase theLiving;
    private int rotationTickCounter;
    private float prevRenderYawHead;
    private static final String __OBFID = "CL_00001570";

    public EntityBodyHelper(EntityLivingBase p_i1611_1_) {
        this.theLiving = p_i1611_1_;
    }

    public void updateRenderAngles() {
        double var1 = this.theLiving.posX - this.theLiving.prevPosX;
        double var3 = this.theLiving.posZ - this.theLiving.prevPosZ;
        if (var1 * var1 + var3 * var3 > 2.500000277905201E-7) {
            this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
            this.prevRenderYawHead = this.theLiving.rotationYawHead = this.computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 75.0f);
            this.rotationTickCounter = 0;
        } else {
            float var5 = 75.0f;
            if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 15.0f) {
                this.rotationTickCounter = 0;
                this.prevRenderYawHead = this.theLiving.rotationYawHead;
            } else {
                ++this.rotationTickCounter;
                boolean var6 = true;
                if (this.rotationTickCounter > 10) {
                    var5 = Math.max(1.0f - (float)(this.rotationTickCounter - 10) / 10.0f, 0.0f) * 75.0f;
                }
            }
            this.theLiving.renderYawOffset = this.computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, var5);
        }
    }

    private float computeAngleWithBound(float p_75665_1_, float p_75665_2_, float p_75665_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_75665_1_ - p_75665_2_);
        if (var4 < - p_75665_3_) {
            var4 = - p_75665_3_;
        }
        if (var4 >= p_75665_3_) {
            var4 = p_75665_3_;
        }
        return p_75665_1_ - var4;
    }
}

