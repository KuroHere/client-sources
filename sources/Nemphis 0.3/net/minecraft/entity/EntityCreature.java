/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public abstract class EntityCreature
extends EntityLiving {
    public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier field_110181_i = new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2.0, 2).setSaved(false);
    private BlockPos homePosition = BlockPos.ORIGIN;
    private float maximumHomeDistance = -1.0f;
    private EntityAIBase aiBase;
    private boolean field_110180_bt;
    private static final String __OBFID = "CL_00001558";

    public EntityCreature(World worldIn) {
        super(worldIn);
        this.aiBase = new EntityAIMoveTowardsRestriction(this, 1.0);
    }

    public float func_180484_a(BlockPos p_180484_1_) {
        return 0.0f;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere() && this.func_180484_a(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0f) {
            return true;
        }
        return false;
    }

    public boolean hasPath() {
        return !this.navigator.noPath();
    }

    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.func_180485_d(new BlockPos(this));
    }

    public boolean func_180485_d(BlockPos p_180485_1_) {
        return this.maximumHomeDistance == -1.0f ? true : this.homePosition.distanceSq(p_180485_1_) < (double)(this.maximumHomeDistance * this.maximumHomeDistance);
    }

    public void func_175449_a(BlockPos p_175449_1_, int p_175449_2_) {
        this.homePosition = p_175449_1_;
        this.maximumHomeDistance = p_175449_2_;
    }

    public BlockPos func_180486_cf() {
        return this.homePosition;
    }

    public float getMaximumHomeDistance() {
        return this.maximumHomeDistance;
    }

    public void detachHome() {
        this.maximumHomeDistance = -1.0f;
    }

    public boolean hasHome() {
        if (this.maximumHomeDistance != -1.0f) {
            return true;
        }
        return false;
    }

    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj) {
            Entity var1 = this.getLeashedToEntity();
            this.func_175449_a(new BlockPos((int)var1.posX, (int)var1.posY, (int)var1.posZ), 5);
            float var2 = this.getDistanceToEntity(var1);
            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
                if (var2 > 10.0f) {
                    this.clearLeashed(true, true);
                }
                return;
            }
            if (!this.field_110180_bt) {
                this.tasks.addTask(2, this.aiBase);
                if (this.getNavigator() instanceof PathNavigateGround) {
                    ((PathNavigateGround)this.getNavigator()).func_179690_a(false);
                }
                this.field_110180_bt = true;
            }
            this.func_142017_o(var2);
            if (var2 > 4.0f) {
                this.getNavigator().tryMoveToEntityLiving(var1, 1.0);
            }
            if (var2 > 6.0f) {
                double var3 = (var1.posX - this.posX) / (double)var2;
                double var5 = (var1.posY - this.posY) / (double)var2;
                double var7 = (var1.posZ - this.posZ) / (double)var2;
                this.motionX += var3 * Math.abs(var3) * 0.4;
                this.motionY += var5 * Math.abs(var5) * 0.4;
                this.motionZ += var7 * Math.abs(var7) * 0.4;
            }
            if (var2 > 10.0f) {
                this.clearLeashed(true, true);
            }
        } else if (!this.getLeashed() && this.field_110180_bt) {
            this.field_110180_bt = false;
            this.tasks.removeTask(this.aiBase);
            if (this.getNavigator() instanceof PathNavigateGround) {
                ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
            }
            this.detachHome();
        }
    }

    protected void func_142017_o(float p_142017_1_) {
    }
}

