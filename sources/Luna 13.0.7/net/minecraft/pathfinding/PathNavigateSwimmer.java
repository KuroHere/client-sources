package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

public class PathNavigateSwimmer
  extends PathNavigate
{
  private static final String __OBFID = "CL_00002244";
  
  public PathNavigateSwimmer(EntityLiving p_i45873_1_, World worldIn)
  {
    super(p_i45873_1_, worldIn);
  }
  
  protected PathFinder func_179679_a()
  {
    return new PathFinder(new SwimNodeProcessor());
  }
  
  protected boolean canNavigate()
  {
    return isInLiquid();
  }
  
  protected Vec3 getEntityPosition()
  {
    return new Vec3(this.theEntity.posX, this.theEntity.posY + this.theEntity.height * 0.5D, this.theEntity.posZ);
  }
  
  protected void pathFollow()
  {
    Vec3 var1 = getEntityPosition();
    float var2 = this.theEntity.width * this.theEntity.width;
    byte var3 = 6;
    if (var1.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < var2) {
      this.currentPath.incrementPathIndex();
    }
    for (int var4 = Math.min(this.currentPath.getCurrentPathIndex() + var3, this.currentPath.getCurrentPathLength() - 1); var4 > this.currentPath.getCurrentPathIndex(); var4--)
    {
      Vec3 var5 = this.currentPath.getVectorFromIndex(this.theEntity, var4);
      if ((var5.squareDistanceTo(var1) <= 36.0D) && (isDirectPathBetweenPoints(var1, var5, 0, 0, 0)))
      {
        this.currentPath.setCurrentPathIndex(var4);
        break;
      }
    }
    func_179677_a(var1);
  }
  
  protected void removeSunnyPath()
  {
    super.removeSunnyPath();
  }
  
  protected boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_)
  {
    MovingObjectPosition var6 = this.worldObj.rayTraceBlocks(p_75493_1_, new Vec3(p_75493_2_.xCoord, p_75493_2_.yCoord + this.theEntity.height * 0.5D, p_75493_2_.zCoord), false, true, false);
    return (var6 == null) || (var6.typeOfHit == MovingObjectPosition.MovingObjectType.MISS);
  }
}
