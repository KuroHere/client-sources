package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityWither
  extends EntityMob
  implements IBossDisplayData, IRangedAttackMob
{
  private float[] field_82220_d = new float[2];
  private float[] field_82221_e = new float[2];
  private float[] field_82217_f = new float[2];
  private float[] field_82218_g = new float[2];
  private int[] field_82223_h = new int[2];
  private int[] field_82224_i = new int[2];
  private int field_82222_j;
  private static final Predicate attackEntitySelector = new Predicate()
  {
    private static final String __OBFID = "CL_00001662";
    
    public boolean func_180027_a(Entity p_180027_1_)
    {
      return ((p_180027_1_ instanceof EntityLivingBase)) && (((EntityLivingBase)p_180027_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD);
    }
    
    public boolean apply(Object p_apply_1_)
    {
      return func_180027_a((Entity)p_apply_1_);
    }
  };
  private static final String __OBFID = "CL_00001661";
  
  public EntityWither(World worldIn)
  {
    super(worldIn);
    setHealth(getMaxHealth());
    setSize(0.9F, 3.5F);
    this.isImmuneToFire = true;
    ((PathNavigateGround)getNavigator()).func_179693_d(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
    this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(7, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
    this.experienceValue = 50;
  }
  
  protected void entityInit()
  {
    super.entityInit();
    this.dataWatcher.addObject(17, new Integer(0));
    this.dataWatcher.addObject(18, new Integer(0));
    this.dataWatcher.addObject(19, new Integer(0));
    this.dataWatcher.addObject(20, new Integer(0));
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("Invul", getInvulTime());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    super.readEntityFromNBT(tagCompund);
    setInvulTime(tagCompund.getInteger("Invul"));
  }
  
  protected String getLivingSound()
  {
    return "mob.wither.idle";
  }
  
  protected String getHurtSound()
  {
    return "mob.wither.hurt";
  }
  
  protected String getDeathSound()
  {
    return "mob.wither.death";
  }
  
  public void onLivingUpdate()
  {
    this.motionY *= 0.6000000238418579D;
    if ((!this.worldObj.isRemote) && (getWatchedTargetId(0) > 0))
    {
      Entity var1 = this.worldObj.getEntityByID(getWatchedTargetId(0));
      if (var1 != null)
      {
        if ((this.posY < var1.posY) || ((!isArmored()) && (this.posY < var1.posY + 5.0D)))
        {
          if (this.motionY < 0.0D) {
            this.motionY = 0.0D;
          }
          this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
        }
        double var2 = var1.posX - this.posX;
        double var4 = var1.posZ - this.posZ;
        double var6 = var2 * var2 + var4 * var4;
        if (var6 > 9.0D)
        {
          double var8 = MathHelper.sqrt_double(var6);
          this.motionX += (var2 / var8 * 0.5D - this.motionX) * 0.6000000238418579D;
          this.motionZ += (var4 / var8 * 0.5D - this.motionZ) * 0.6000000238418579D;
        }
      }
    }
    if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D) {
      this.rotationYaw = ((float)Math.atan2(this.motionZ, this.motionX) * 57.295776F - 90.0F);
    }
    super.onLivingUpdate();
    for (int var20 = 0; var20 < 2; var20++)
    {
      this.field_82218_g[var20] = this.field_82221_e[var20];
      this.field_82217_f[var20] = this.field_82220_d[var20];
    }
    for (var20 = 0; var20 < 2; var20++)
    {
      int var22 = getWatchedTargetId(var20 + 1);
      Entity var3 = null;
      if (var22 > 0) {
        var3 = this.worldObj.getEntityByID(var22);
      }
      if (var3 != null)
      {
        double var4 = func_82214_u(var20 + 1);
        double var6 = func_82208_v(var20 + 1);
        double var8 = func_82213_w(var20 + 1);
        double var10 = var3.posX - var4;
        double var12 = var3.posY + var3.getEyeHeight() - var6;
        double var14 = var3.posZ - var8;
        double var16 = MathHelper.sqrt_double(var10 * var10 + var14 * var14);
        float var18 = (float)(Math.atan2(var14, var10) * 180.0D / 3.141592653589793D) - 90.0F;
        float var19 = (float)-(Math.atan2(var12, var16) * 180.0D / 3.141592653589793D);
        this.field_82220_d[var20] = func_82204_b(this.field_82220_d[var20], var19, 40.0F);
        this.field_82221_e[var20] = func_82204_b(this.field_82221_e[var20], var18, 10.0F);
      }
      else
      {
        this.field_82221_e[var20] = func_82204_b(this.field_82221_e[var20], this.renderYawOffset, 10.0F);
      }
    }
    boolean var21 = isArmored();
    for (int var22 = 0; var22 < 3; var22++)
    {
      double var23 = func_82214_u(var22);
      double var5 = func_82208_v(var22);
      double var7 = func_82213_w(var22);
      this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var23 + this.rand.nextGaussian() * 0.30000001192092896D, var5 + this.rand.nextGaussian() * 0.30000001192092896D, var7 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
      if ((var21) && (this.worldObj.rand.nextInt(4) == 0)) {
        this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, var23 + this.rand.nextGaussian() * 0.30000001192092896D, var5 + this.rand.nextGaussian() * 0.30000001192092896D, var7 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
      }
    }
    if (getInvulTime() > 0) {
      for (var22 = 0; var22 < 3; var22++) {
        this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + this.rand.nextFloat() * 3.3F, this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
      }
    }
  }
  
  protected void updateAITasks()
  {
    if (getInvulTime() > 0)
    {
      int var1 = getInvulTime() - 1;
      if (var1 <= 0)
      {
        this.worldObj.newExplosion(this, this.posX, this.posY + getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
        this.worldObj.func_175669_a(1013, new BlockPos(this), 0);
      }
      setInvulTime(var1);
      if (this.ticksExisted % 10 == 0) {
        heal(10.0F);
      }
    }
    else
    {
      super.updateAITasks();
      for (int var1 = 1; var1 < 3; var1++) {
        if (this.ticksExisted >= this.field_82223_h[(var1 - 1)])
        {
          this.field_82223_h[(var1 - 1)] = (this.ticksExisted + 10 + this.rand.nextInt(10));
          if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) || (this.worldObj.getDifficulty() == EnumDifficulty.HARD))
          {
            int var10001 = var1 - 1;
            int var10003 = this.field_82224_i[(var1 - 1)];
            this.field_82224_i[var10001] = (this.field_82224_i[(var1 - 1)] + 1);
            if (var10003 > 15)
            {
              float var2 = 10.0F;
              float var3 = 5.0F;
              double var4 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - var2, this.posX + var2);
              double var6 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - var3, this.posY + var3);
              double var8 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - var2, this.posZ + var2);
              launchWitherSkullToCoords(var1 + 1, var4, var6, var8, true);
              this.field_82224_i[(var1 - 1)] = 0;
            }
          }
          int var12 = getWatchedTargetId(var1);
          if (var12 > 0)
          {
            Entity var14 = this.worldObj.getEntityByID(var12);
            if ((var14 != null) && (var14.isEntityAlive()) && (getDistanceSqToEntity(var14) <= 900.0D) && (canEntityBeSeen(var14)))
            {
              launchWitherSkullToEntity(var1 + 1, (EntityLivingBase)var14);
              this.field_82223_h[(var1 - 1)] = (this.ticksExisted + 40 + this.rand.nextInt(20));
              this.field_82224_i[(var1 - 1)] = 0;
            }
            else
            {
              func_82211_c(var1, 0);
            }
          }
          else
          {
            List var13 = this.worldObj.func_175647_a(EntityLivingBase.class, getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.and(attackEntitySelector, IEntitySelector.field_180132_d));
            for (int var16 = 0; (var16 < 10) && (!var13.isEmpty()); var16++)
            {
              EntityLivingBase var5 = (EntityLivingBase)var13.get(this.rand.nextInt(var13.size()));
              if ((var5 != this) && (var5.isEntityAlive()) && (canEntityBeSeen(var5)))
              {
                if ((var5 instanceof EntityPlayer))
                {
                  if (((EntityPlayer)var5).capabilities.disableDamage) {
                    break;
                  }
                  func_82211_c(var1, var5.getEntityId()); break;
                }
                func_82211_c(var1, var5.getEntityId());
                
                break;
              }
              var13.remove(var5);
            }
          }
        }
      }
      if (getAttackTarget() != null) {
        func_82211_c(0, getAttackTarget().getEntityId());
      } else {
        func_82211_c(0, 0);
      }
      if (this.field_82222_j > 0)
      {
        this.field_82222_j -= 1;
        if ((this.field_82222_j == 0) && (this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")))
        {
          var1 = MathHelper.floor_double(this.posY);
          int var12 = MathHelper.floor_double(this.posX);
          int var15 = MathHelper.floor_double(this.posZ);
          boolean var17 = false;
          for (int var18 = -1; var18 <= 1; var18++) {
            for (int var19 = -1; var19 <= 1; var19++) {
              for (int var7 = 0; var7 <= 3; var7++)
              {
                int var20 = var12 + var18;
                int var9 = var1 + var7;
                int var10 = var15 + var19;
                Block var11 = this.worldObj.getBlockState(new BlockPos(var20, var9, var10)).getBlock();
                if ((var11.getMaterial() != Material.air) && (var11 != Blocks.bedrock) && (var11 != Blocks.end_portal) && (var11 != Blocks.end_portal_frame) && (var11 != Blocks.command_block) && (var11 != Blocks.barrier)) {
                  var17 = (this.worldObj.destroyBlock(new BlockPos(var20, var9, var10), true)) || (var17);
                }
              }
            }
          }
          if (var17) {
            this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, new BlockPos(this), 0);
          }
        }
      }
      if (this.ticksExisted % 20 == 0) {
        heal(1.0F);
      }
    }
  }
  
  public void func_82206_m()
  {
    setInvulTime(220);
    setHealth(getMaxHealth() / 3.0F);
  }
  
  public void setInWeb() {}
  
  public int getTotalArmorValue()
  {
    return 4;
  }
  
  private double func_82214_u(int p_82214_1_)
  {
    if (p_82214_1_ <= 0) {
      return this.posX;
    }
    float var2 = (this.renderYawOffset + 180 * (p_82214_1_ - 1)) / 180.0F * 3.1415927F;
    float var3 = MathHelper.cos(var2);
    return this.posX + var3 * 1.3D;
  }
  
  private double func_82208_v(int p_82208_1_)
  {
    return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
  }
  
  private double func_82213_w(int p_82213_1_)
  {
    if (p_82213_1_ <= 0) {
      return this.posZ;
    }
    float var2 = (this.renderYawOffset + 180 * (p_82213_1_ - 1)) / 180.0F * 3.1415927F;
    float var3 = MathHelper.sin(var2);
    return this.posZ + var3 * 1.3D;
  }
  
  private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_)
  {
    float var4 = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);
    if (var4 > p_82204_3_) {
      var4 = p_82204_3_;
    }
    if (var4 < -p_82204_3_) {
      var4 = -p_82204_3_;
    }
    return p_82204_1_ + var4;
  }
  
  private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_)
  {
    launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, (p_82216_1_ == 0) && (this.rand.nextFloat() < 0.001F));
  }
  
  private void launchWitherSkullToCoords(int p_82209_1_, double p_82209_2_, double p_82209_4_, double p_82209_6_, boolean p_82209_8_)
  {
    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, new BlockPos(this), 0);
    double var9 = func_82214_u(p_82209_1_);
    double var11 = func_82208_v(p_82209_1_);
    double var13 = func_82213_w(p_82209_1_);
    double var15 = p_82209_2_ - var9;
    double var17 = p_82209_4_ - var11;
    double var19 = p_82209_6_ - var13;
    EntityWitherSkull var21 = new EntityWitherSkull(this.worldObj, this, var15, var17, var19);
    if (p_82209_8_) {
      var21.setInvulnerable(true);
    }
    var21.posY = var11;
    var21.posX = var9;
    var21.posZ = var13;
    this.worldObj.spawnEntityInWorld(var21);
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
  {
    launchWitherSkullToEntity(0, p_82196_1_);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    if ((source != DamageSource.drown) && (!(source.getEntity() instanceof EntityWither)))
    {
      if ((getInvulTime() > 0) && (source != DamageSource.outOfWorld)) {
        return false;
      }
      if (isArmored())
      {
        Entity var3 = source.getSourceOfDamage();
        if ((var3 instanceof EntityArrow)) {
          return false;
        }
      }
      Entity var3 = source.getEntity();
      if ((var3 != null) && (!(var3 instanceof EntityPlayer)) && ((var3 instanceof EntityLivingBase)) && (((EntityLivingBase)var3).getCreatureAttribute() == getCreatureAttribute())) {
        return false;
      }
      if (this.field_82222_j <= 0) {
        this.field_82222_j = 20;
      }
      for (int var4 = 0; var4 < this.field_82224_i.length; var4++) {
        this.field_82224_i[var4] += 3;
      }
      return super.attackEntityFrom(source, amount);
    }
    return false;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
  {
    EntityItem var3 = dropItem(Items.nether_star, 1);
    if (var3 != null) {
      var3.func_174873_u();
    }
    if (!this.worldObj.isRemote)
    {
      Iterator var4 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();
      while (var4.hasNext())
      {
        EntityPlayer var5 = (EntityPlayer)var4.next();
        var5.triggerAchievement(AchievementList.killWither);
      }
    }
  }
  
  protected void despawnEntity()
  {
    this.entityAge = 0;
  }
  
  public int getBrightnessForRender(float p_70070_1_)
  {
    return 15728880;
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  public void addPotionEffect(PotionEffect p_70690_1_) {}
  
  protected void applyEntityAttributes()
  {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
  }
  
  public float func_82207_a(int p_82207_1_)
  {
    return this.field_82221_e[p_82207_1_];
  }
  
  public float func_82210_r(int p_82210_1_)
  {
    return this.field_82220_d[p_82210_1_];
  }
  
  public int getInvulTime()
  {
    return this.dataWatcher.getWatchableObjectInt(20);
  }
  
  public void setInvulTime(int p_82215_1_)
  {
    this.dataWatcher.updateObject(20, Integer.valueOf(p_82215_1_));
  }
  
  public int getWatchedTargetId(int p_82203_1_)
  {
    return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
  }
  
  public void func_82211_c(int p_82211_1_, int p_82211_2_)
  {
    this.dataWatcher.updateObject(17 + p_82211_1_, Integer.valueOf(p_82211_2_));
  }
  
  public boolean isArmored()
  {
    return getHealth() <= getMaxHealth() / 2.0F;
  }
  
  public EnumCreatureAttribute getCreatureAttribute()
  {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  public void mountEntity(Entity entityIn)
  {
    this.ridingEntity = null;
  }
}
