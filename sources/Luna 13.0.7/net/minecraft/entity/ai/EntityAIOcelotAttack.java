package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityAIOcelotAttack
  extends EntityAIBase
{
  World theWorld;
  EntityLiving theEntity;
  EntityLivingBase theVictim;
  int attackCountdown;
  private static final String __OBFID = "CL_00001600";
  
  public EntityAIOcelotAttack(EntityLiving p_i1641_1_)
  {
    this.theEntity = p_i1641_1_;
    this.theWorld = p_i1641_1_.worldObj;
    setMutexBits(3);
  }
  
  public boolean shouldExecute()
  {
    EntityLivingBase var1 = this.theEntity.getAttackTarget();
    if (var1 == null) {
      return false;
    }
    this.theVictim = var1;
    return true;
  }
  
  public boolean continueExecuting()
  {
    return this.theVictim.isEntityAlive();
  }
  
  public void resetTask()
  {
    this.theVictim = null;
    this.theEntity.getNavigator().clearPathEntity();
  }
  
  public void updateTask()
  {
    this.theEntity.getLookHelper().setLookPositionWithEntity(this.theVictim, 30.0F, 30.0F);
    double var1 = this.theEntity.width * 2.0F * this.theEntity.width * 2.0F;
    double var3 = this.theEntity.getDistanceSq(this.theVictim.posX, this.theVictim.getEntityBoundingBox().minY, this.theVictim.posZ);
    double var5 = 0.8D;
    if ((var3 > var1) && (var3 < 16.0D)) {
      var5 = 1.33D;
    } else if (var3 < 225.0D) {
      var5 = 0.6D;
    }
    this.theEntity.getNavigator().tryMoveToEntityLiving(this.theVictim, var5);
    this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
    if (var3 <= var1) {
      if (this.attackCountdown <= 0)
      {
        this.attackCountdown = 20;
        this.theEntity.attackEntityAsMob(this.theVictim);
      }
    }
  }
}
