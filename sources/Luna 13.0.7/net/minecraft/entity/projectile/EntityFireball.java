package net.minecraft.entity.projectile;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityFireball
  extends Entity
{
  private int field_145795_e = -1;
  private int field_145793_f = -1;
  private int field_145794_g = -1;
  private Block field_145796_h;
  private boolean inGround;
  public EntityLivingBase shootingEntity;
  private int ticksAlive;
  private int ticksInAir;
  public double accelerationX;
  public double accelerationY;
  public double accelerationZ;
  private static final String __OBFID = "CL_00001717";
  
  public EntityFireball(World worldIn)
  {
    super(worldIn);
    setSize(1.0F, 1.0F);
  }
  
  protected void entityInit() {}
  
  public boolean isInRangeToRenderDist(double distance)
  {
    double var3 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
    var3 *= 64.0D;
    return distance < var3 * var3;
  }
  
  public EntityFireball(World worldIn, double p_i1760_2_, double p_i1760_4_, double p_i1760_6_, double p_i1760_8_, double p_i1760_10_, double p_i1760_12_)
  {
    super(worldIn);
    setSize(1.0F, 1.0F);
    setLocationAndAngles(p_i1760_2_, p_i1760_4_, p_i1760_6_, this.rotationYaw, this.rotationPitch);
    setPosition(p_i1760_2_, p_i1760_4_, p_i1760_6_);
    double var14 = MathHelper.sqrt_double(p_i1760_8_ * p_i1760_8_ + p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
    this.accelerationX = (p_i1760_8_ / var14 * 0.1D);
    this.accelerationY = (p_i1760_10_ / var14 * 0.1D);
    this.accelerationZ = (p_i1760_12_ / var14 * 0.1D);
  }
  
  public EntityFireball(World worldIn, EntityLivingBase p_i1761_2_, double p_i1761_3_, double p_i1761_5_, double p_i1761_7_)
  {
    super(worldIn);
    this.shootingEntity = p_i1761_2_;
    setSize(1.0F, 1.0F);
    setLocationAndAngles(p_i1761_2_.posX, p_i1761_2_.posY, p_i1761_2_.posZ, p_i1761_2_.rotationYaw, p_i1761_2_.rotationPitch);
    setPosition(this.posX, this.posY, this.posZ);
    this.motionX = (this.motionY = this.motionZ = 0.0D);
    p_i1761_3_ += this.rand.nextGaussian() * 0.4D;
    p_i1761_5_ += this.rand.nextGaussian() * 0.4D;
    p_i1761_7_ += this.rand.nextGaussian() * 0.4D;
    double var9 = MathHelper.sqrt_double(p_i1761_3_ * p_i1761_3_ + p_i1761_5_ * p_i1761_5_ + p_i1761_7_ * p_i1761_7_);
    this.accelerationX = (p_i1761_3_ / var9 * 0.1D);
    this.accelerationY = (p_i1761_5_ / var9 * 0.1D);
    this.accelerationZ = (p_i1761_7_ / var9 * 0.1D);
  }
  
  public void onUpdate()
  {
    if ((!this.worldObj.isRemote) && (((this.shootingEntity != null) && (this.shootingEntity.isDead)) || (!this.worldObj.isBlockLoaded(new BlockPos(this)))))
    {
      setDead();
    }
    else
    {
      super.onUpdate();
      setFire(1);
      if (this.inGround)
      {
        if (this.worldObj.getBlockState(new BlockPos(this.field_145795_e, this.field_145793_f, this.field_145794_g)).getBlock() == this.field_145796_h)
        {
          this.ticksAlive += 1;
          if (this.ticksAlive == 600) {
            setDead();
          }
          return;
        }
        this.inGround = false;
        this.motionX *= this.rand.nextFloat() * 0.2F;
        this.motionY *= this.rand.nextFloat() * 0.2F;
        this.motionZ *= this.rand.nextFloat() * 0.2F;
        this.ticksAlive = 0;
        this.ticksInAir = 0;
      }
      else
      {
        this.ticksInAir += 1;
      }
      Vec3 var1 = new Vec3(this.posX, this.posY, this.posZ);
      Vec3 var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var1, var2);
      var1 = new Vec3(this.posX, this.posY, this.posZ);
      var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (var3 != null) {
        var2 = new Vec3(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
      }
      Entity var4 = null;
      List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
      double var6 = 0.0D;
      for (int var8 = 0; var8 < var5.size(); var8++)
      {
        Entity var9 = (Entity)var5.get(var8);
        if ((var9.canBeCollidedWith()) && ((!var9.isEntityEqual(this.shootingEntity)) || (this.ticksInAir >= 25)))
        {
          float var10 = 0.3F;
          AxisAlignedBB var11 = var9.getEntityBoundingBox().expand(var10, var10, var10);
          MovingObjectPosition var12 = var11.calculateIntercept(var1, var2);
          if (var12 != null)
          {
            double var13 = var1.distanceTo(var12.hitVec);
            if ((var13 < var6) || (var6 == 0.0D))
            {
              var4 = var9;
              var6 = var13;
            }
          }
        }
      }
      if (var4 != null) {
        var3 = new MovingObjectPosition(var4);
      }
      if (var3 != null) {
        onImpact(var3);
      }
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      float var15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = ((float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / 3.141592653589793D) + 90.0F);
      for (this.rotationPitch = ((float)(Math.atan2(var15, this.motionY) * 180.0D / 3.141592653589793D) - 90.0F); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
      while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
        this.prevRotationPitch += 360.0F;
      }
      while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
        this.prevRotationYaw -= 360.0F;
      }
      while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
        this.prevRotationYaw += 360.0F;
      }
      this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
      this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
      float var16 = getMotionFactor();
      if (isInWater())
      {
        for (int var17 = 0; var17 < 4; var17++)
        {
          float var18 = 0.25F;
          this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * var18, this.posY - this.motionY * var18, this.posZ - this.motionZ * var18, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
        var16 = 0.8F;
      }
      this.motionX += this.accelerationX;
      this.motionY += this.accelerationY;
      this.motionZ += this.accelerationZ;
      this.motionX *= var16;
      this.motionY *= var16;
      this.motionZ *= var16;
      this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      setPosition(this.posX, this.posY, this.posZ);
    }
  }
  
  protected float getMotionFactor()
  {
    return 0.95F;
  }
  
  protected abstract void onImpact(MovingObjectPosition paramMovingObjectPosition);
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    tagCompound.setShort("xTile", (short)this.field_145795_e);
    tagCompound.setShort("yTile", (short)this.field_145793_f);
    tagCompound.setShort("zTile", (short)this.field_145794_g);
    ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.field_145796_h);
    tagCompound.setString("inTile", var2 == null ? "" : var2.toString());
    tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    tagCompound.setTag("direction", newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    this.field_145795_e = tagCompund.getShort("xTile");
    this.field_145793_f = tagCompund.getShort("yTile");
    this.field_145794_g = tagCompund.getShort("zTile");
    if (tagCompund.hasKey("inTile", 8)) {
      this.field_145796_h = Block.getBlockFromName(tagCompund.getString("inTile"));
    } else {
      this.field_145796_h = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
    }
    this.inGround = (tagCompund.getByte("inGround") == 1);
    if (tagCompund.hasKey("direction", 9))
    {
      NBTTagList var2 = tagCompund.getTagList("direction", 6);
      this.motionX = var2.getDouble(0);
      this.motionY = var2.getDouble(1);
      this.motionZ = var2.getDouble(2);
    }
    else
    {
      setDead();
    }
  }
  
  public boolean canBeCollidedWith()
  {
    return true;
  }
  
  public float getCollisionBorderSize()
  {
    return 1.0F;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    setBeenAttacked();
    if (source.getEntity() != null)
    {
      Vec3 var3 = source.getEntity().getLookVec();
      if (var3 != null)
      {
        this.motionX = var3.xCoord;
        this.motionY = var3.yCoord;
        this.motionZ = var3.zCoord;
        this.accelerationX = (this.motionX * 0.1D);
        this.accelerationY = (this.motionY * 0.1D);
        this.accelerationZ = (this.motionZ * 0.1D);
      }
      if ((source.getEntity() instanceof EntityLivingBase)) {
        this.shootingEntity = ((EntityLivingBase)source.getEntity());
      }
      return true;
    }
    return false;
  }
  
  public float getBrightness(float p_70013_1_)
  {
    return 1.0F;
  }
  
  public int getBrightnessForRender(float p_70070_1_)
  {
    return 15728880;
  }
}
