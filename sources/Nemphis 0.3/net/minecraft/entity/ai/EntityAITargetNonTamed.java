/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed
extends EntityAINearestAttackableTarget {
    private EntityTameable theTameable;
    private static final String __OBFID = "CL_00001623";

    public EntityAITargetNonTamed(EntityTameable p_i45876_1_, Class p_i45876_2_, boolean p_i45876_3_, Predicate p_i45876_4_) {
        super(p_i45876_1_, p_i45876_2_, 10, p_i45876_3_, false, p_i45876_4_);
        this.theTameable = p_i45876_1_;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.theTameable.isTamed() && super.shouldExecute()) {
            return true;
        }
        return false;
    }
}

