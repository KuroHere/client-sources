package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public abstract class EntityAITarget
  extends EntityAIBase
{
  protected final EntityCreature taskOwner;
  protected boolean shouldCheckSight;
  private boolean nearbyOnly;
  private int targetSearchStatus;
  private int targetSearchDelay;
  private int targetUnseenTicks;
  private static final String __OBFID = "CL_00001626";
  
  public EntityAITarget(EntityCreature p_i1669_1_, boolean p_i1669_2_)
  {
    this(p_i1669_1_, p_i1669_2_, false);
  }
  
  public EntityAITarget(EntityCreature p_i1670_1_, boolean p_i1670_2_, boolean p_i1670_3_)
  {
    this.taskOwner = p_i1670_1_;
    this.shouldCheckSight = p_i1670_2_;
    this.nearbyOnly = p_i1670_3_;
  }
  
  public boolean continueExecuting()
  {
    EntityLivingBase var1 = this.taskOwner.getAttackTarget();
    if (var1 == null) {
      return false;
    }
    if (!var1.isEntityAlive()) {
      return false;
    }
    Team var2 = this.taskOwner.getTeam();
    Team var3 = var1.getTeam();
    if ((var2 != null) && (var3 == var2)) {
      return false;
    }
    double var4 = getTargetDistance();
    if (this.taskOwner.getDistanceSqToEntity(var1) > var4 * var4) {
      return false;
    }
    if (this.shouldCheckSight) {
      if (this.taskOwner.getEntitySenses().canSee(var1)) {
        this.targetUnseenTicks = 0;
      } else if (++this.targetUnseenTicks > 60) {
        return false;
      }
    }
    return (!(var1 instanceof EntityPlayer)) || (!((EntityPlayer)var1).capabilities.disableDamage);
  }
  
  protected double getTargetDistance()
  {
    IAttributeInstance var1 = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
    return var1 == null ? 16.0D : var1.getAttributeValue();
  }
  
  public void startExecuting()
  {
    this.targetSearchStatus = 0;
    this.targetSearchDelay = 0;
    this.targetUnseenTicks = 0;
  }
  
  public void resetTask()
  {
    this.taskOwner.setAttackTarget((EntityLivingBase)null);
  }
  
  public static boolean func_179445_a(EntityLiving p_179445_0_, EntityLivingBase p_179445_1_, boolean p_179445_2_, boolean p_179445_3_)
  {
    if (p_179445_1_ == null) {
      return false;
    }
    if (p_179445_1_ == p_179445_0_) {
      return false;
    }
    if (!p_179445_1_.isEntityAlive()) {
      return false;
    }
    if (!p_179445_0_.canAttackClass(p_179445_1_.getClass())) {
      return false;
    }
    Team var4 = p_179445_0_.getTeam();
    Team var5 = p_179445_1_.getTeam();
    if ((var4 != null) && (var5 == var4)) {
      return false;
    }
    if (((p_179445_0_ instanceof IEntityOwnable)) && (StringUtils.isNotEmpty(((IEntityOwnable)p_179445_0_).func_152113_b())))
    {
      if (((p_179445_1_ instanceof IEntityOwnable)) && (((IEntityOwnable)p_179445_0_).func_152113_b().equals(((IEntityOwnable)p_179445_1_).func_152113_b()))) {
        return false;
      }
      if (p_179445_1_ == ((IEntityOwnable)p_179445_0_).getOwner()) {
        return false;
      }
    }
    else if (((p_179445_1_ instanceof EntityPlayer)) && (!p_179445_2_) && (((EntityPlayer)p_179445_1_).capabilities.disableDamage))
    {
      return false;
    }
    return (!p_179445_3_) || (p_179445_0_.getEntitySenses().canSee(p_179445_1_));
  }
  
  protected boolean isSuitableTarget(EntityLivingBase p_75296_1_, boolean p_75296_2_)
  {
    if (!func_179445_a(this.taskOwner, p_75296_1_, p_75296_2_, this.shouldCheckSight)) {
      return false;
    }
    if (!this.taskOwner.func_180485_d(new BlockPos(p_75296_1_))) {
      return false;
    }
    if (this.nearbyOnly)
    {
      if (--this.targetSearchDelay <= 0) {
        this.targetSearchStatus = 0;
      }
      if (this.targetSearchStatus == 0) {
        this.targetSearchStatus = (canEasilyReach(p_75296_1_) ? 1 : 2);
      }
      if (this.targetSearchStatus == 2) {
        return false;
      }
    }
    return true;
  }
  
  private boolean canEasilyReach(EntityLivingBase p_75295_1_)
  {
    this.targetSearchDelay = (10 + this.taskOwner.getRNG().nextInt(5));
    PathEntity var2 = this.taskOwner.getNavigator().getPathToEntityLiving(p_75295_1_);
    if (var2 == null) {
      return false;
    }
    PathPoint var3 = var2.getFinalPathPoint();
    if (var3 == null) {
      return false;
    }
    int var4 = var3.xCoord - MathHelper.floor_double(p_75295_1_.posX);
    int var5 = var3.zCoord - MathHelper.floor_double(p_75295_1_.posZ);
    return var4 * var4 + var5 * var5 <= 2.25D;
  }
}
