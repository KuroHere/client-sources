/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class EntityMoveHelper {
    protected EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected boolean update;
    private static final String __OBFID = "CL_00001573";

    public EntityMoveHelper(EntityLiving p_i1614_1_) {
        this.entity = p_i1614_1_;
        this.posX = p_i1614_1_.posX;
        this.posY = p_i1614_1_.posY;
        this.posZ = p_i1614_1_.posZ;
    }

    public boolean isUpdating() {
        return this.update;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setMoveTo(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_) {
        this.posX = p_75642_1_;
        this.posY = p_75642_3_;
        this.posZ = p_75642_5_;
        this.speed = p_75642_7_;
        this.update = true;
    }

    public void onUpdateMoveHelper() {
        this.entity.setMoveForward(0.0f);
        if (this.update) {
            double var4;
            this.update = false;
            double var2 = this.posX - this.entity.posX;
            int var1 = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5);
            double var6 = this.posY - (double)var1;
            double var8 = var2 * var2 + var6 * var6 + (var4 = this.posZ - this.entity.posZ) * var4;
            if (var8 >= 2.500000277905201E-7) {
                float var10 = (float)(Math.atan2(var4, var2) * 180.0 / 3.141592653589793) - 90.0f;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, var10, 30.0f);
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                if (var6 > 0.0 && var2 * var2 + var4 * var4 < 1.0) {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }

    protected float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float var5;
        float var4 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
        if (var4 > p_75639_3_) {
            var4 = p_75639_3_;
        }
        if (var4 < -p_75639_3_) {
            var4 = -p_75639_3_;
        }
        if ((var5 = p_75639_1_ + var4) < 0.0f) {
            var5 += 360.0f;
        } else if (var5 > 360.0f) {
            var5 -= 360.0f;
        }
        return var5;
    }

    public double func_179917_d() {
        return this.posX;
    }

    public double func_179919_e() {
        return this.posY;
    }

    public double func_179918_f() {
        return this.posZ;
    }
}

