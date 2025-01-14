/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityPotion
extends EntityThrowable {
    private ItemStack potionDamage;
    private static final String __OBFID = "CL_00001727";

    public EntityPotion(World worldIn) {
        super(worldIn);
    }

    public EntityPotion(World worldIn, EntityLivingBase p_i1789_2_, int p_i1789_3_) {
        this(worldIn, p_i1789_2_, new ItemStack(Items.potionitem, 1, p_i1789_3_));
    }

    public EntityPotion(World worldIn, EntityLivingBase p_i1790_2_, ItemStack p_i1790_3_) {
        super(worldIn, p_i1790_2_);
        this.potionDamage = p_i1790_3_;
    }

    public EntityPotion(World worldIn, double p_i1791_2_, double p_i1791_4_, double p_i1791_6_, int p_i1791_8_) {
        this(worldIn, p_i1791_2_, p_i1791_4_, p_i1791_6_, new ItemStack(Items.potionitem, 1, p_i1791_8_));
    }

    public EntityPotion(World worldIn, double p_i1792_2_, double p_i1792_4_, double p_i1792_6_, ItemStack p_i1792_8_) {
        super(worldIn, p_i1792_2_, p_i1792_4_, p_i1792_6_);
        this.potionDamage = p_i1792_8_;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05f;
    }

    @Override
    protected float func_70182_d() {
        return 0.5f;
    }

    @Override
    protected float func_70183_g() {
        return -20.0f;
    }

    public void setPotionDamage(int p_82340_1_) {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        this.potionDamage.setItemDamage(p_82340_1_);
    }

    public int getPotionDamage() {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        return this.potionDamage.getMetadata();
    }

    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (!this.worldObj.isRemote) {
            List var4;
            AxisAlignedBB var3;
            List var2 = Items.potionitem.getEffects(this.potionDamage);
            if (var2 != null && !var2.isEmpty() && !(var4 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, var3 = this.getEntityBoundingBox().expand(4.0, 2.0, 4.0))).isEmpty()) {
                for (EntityLivingBase var6 : var4) {
                    double var7 = this.getDistanceSqToEntity(var6);
                    if (!(var7 < 16.0)) continue;
                    double var9 = 1.0 - Math.sqrt(var7) / 4.0;
                    if (var6 == p_70184_1_.entityHit) {
                        var9 = 1.0;
                    }
                    for (PotionEffect var12 : var2) {
                        int var13 = var12.getPotionID();
                        if (Potion.potionTypes[var13].isInstant()) {
                            Potion.potionTypes[var13].func_180793_a(this, this.getThrower(), var6, var12.getAmplifier(), var9);
                            continue;
                        }
                        int var14 = (int)(var9 * (double)var12.getDuration() + 0.5);
                        if (var14 <= 20) continue;
                        var6.addPotionEffect(new PotionEffect(var13, var14, var12.getAmplifier()));
                    }
                }
            }
            this.worldObj.playAuxSFX(2002, new BlockPos(this), this.getPotionDamage());
            this.setDead();
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("Potion", 10)) {
            this.potionDamage = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("Potion"));
        } else {
            this.setPotionDamage(tagCompund.getInteger("potionValue"));
        }
        if (this.potionDamage == null) {
            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.potionDamage != null) {
            tagCompound.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
        }
    }
}

