/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity.passive;

import java.util.Calendar;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBat
extends EntityAmbientCreature {
    private BlockPos spawnPosition;
    private static final String __OBFID = "CL_00001637";

    public EntityBat(World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.9f);
        this.setIsBatHanging(true);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    @Override
    protected float getSoundVolume() {
        return 0.1f;
    }

    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95f;
    }

    @Override
    protected String getLivingSound() {
        return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    @Override
    protected String getHurtSound() {
        return "mob.bat.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.bat.death";
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity p_82167_1_) {
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0);
    }

    public boolean getIsBatHanging() {
        if ((this.dataWatcher.getWatchableObjectByte(16) & 1) != 0) {
            return true;
        }
        return false;
    }

    public void setIsBatHanging(boolean p_82236_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_82236_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -2)));
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getIsBatHanging()) {
            this.motionZ = 0.0;
            this.motionY = 0.0;
            this.motionX = 0.0;
            this.posY = (double)MathHelper.floor_double(this.posY) + 1.0 - (double)this.height;
        } else {
            this.motionY *= 0.6000000238418579;
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        BlockPos var1 = new BlockPos(this);
        BlockPos var2 = var1.offsetUp();
        if (this.getIsBatHanging()) {
            if (!this.worldObj.getBlockState(var2).getBlock().isNormalCube()) {
                this.setIsBatHanging(false);
                this.worldObj.playAuxSFXAtEntity(null, 1015, var1, 0);
            } else {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = this.rand.nextInt(360);
                }
                if (this.worldObj.getClosestPlayerToEntity(this, 4.0) != null) {
                    this.setIsBatHanging(false);
                    this.worldObj.playAuxSFXAtEntity(null, 1015, var1, 0);
                }
            }
        } else {
            if (!(this.spawnPosition == null || this.worldObj.isAirBlock(this.spawnPosition) && this.spawnPosition.getY() >= 1)) {
                this.spawnPosition = null;
            }
            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0) {
                this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }
            double var3 = (double)this.spawnPosition.getX() + 0.5 - this.posX;
            double var5 = (double)this.spawnPosition.getY() + 0.1 - this.posY;
            double var7 = (double)this.spawnPosition.getZ() + 0.5 - this.posZ;
            this.motionX += (Math.signum(var3) * 0.5 - this.motionX) * 0.10000000149011612;
            this.motionY += (Math.signum(var5) * 0.699999988079071 - this.motionY) * 0.10000000149011612;
            this.motionZ += (Math.signum(var7) * 0.5 - this.motionZ) * 0.10000000149011612;
            float var9 = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0 / 3.141592653589793) - 90.0f;
            float var10 = MathHelper.wrapAngleTo180_float(var9 - this.rotationYaw);
            this.moveForward = 0.5f;
            this.rotationYaw += var10;
            if (this.rand.nextInt(100) == 0 && this.worldObj.getBlockState(var2).getBlock().isNormalCube()) {
                this.setIsBatHanging(true);
            }
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) {
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (!this.worldObj.isRemote && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.dataWatcher.updateObject(16, Byte.valueOf(tagCompund.getByte("BatFlags")));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos var1 = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (var1.getY() >= 63) {
            return false;
        }
        int var2 = this.worldObj.getLightFromNeighbors(var1);
        int var3 = 4;
        if (this.func_175569_a(this.worldObj.getCurrentDate())) {
            var3 = 7;
        } else if (this.rand.nextBoolean()) {
            return false;
        }
        return var2 > this.rand.nextInt(var3) ? false : super.getCanSpawnHere();
    }

    private boolean func_175569_a(Calendar p_175569_1_) {
        if (!(p_175569_1_.get(2) + 1 == 10 && p_175569_1_.get(5) >= 20 || p_175569_1_.get(2) + 1 == 11 && p_175569_1_.get(5) <= 3)) {
            return false;
        }
        return true;
    }

    @Override
    public float getEyeHeight() {
        return this.height / 2.0f;
    }
}

