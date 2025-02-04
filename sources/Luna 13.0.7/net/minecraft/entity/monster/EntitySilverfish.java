package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockSilverfish.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntitySilverfish
  extends EntityMob
{
  private AISummonSilverfish field_175460_b;
  private static final String __OBFID = "CL_00001696";
  
  public EntitySilverfish(World worldIn)
  {
    super(worldIn);
    setSize(0.4F, 0.3F);
    this.tasks.addTask(1, new EntityAISwimming(this));
    this.tasks.addTask(3, this.field_175460_b = new AISummonSilverfish());
    this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
    this.tasks.addTask(5, new AIHideInStone());
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  public float getEyeHeight()
  {
    return 0.1F;
  }
  
  protected void applyEntityAttributes()
  {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
  }
  
  protected boolean canTriggerWalking()
  {
    return false;
  }
  
  protected String getLivingSound()
  {
    return "mob.silverfish.say";
  }
  
  protected String getHurtSound()
  {
    return "mob.silverfish.hit";
  }
  
  protected String getDeathSound()
  {
    return "mob.silverfish.kill";
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    if (((source instanceof EntityDamageSource)) || (source == DamageSource.magic)) {
      this.field_175460_b.func_179462_f();
    }
    return super.attackEntityFrom(source, amount);
  }
  
  protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_)
  {
    playSound("mob.silverfish.step", 0.15F, 1.0F);
  }
  
  protected Item getDropItem()
  {
    return null;
  }
  
  public void onUpdate()
  {
    this.renderYawOffset = this.rotationYaw;
    super.onUpdate();
  }
  
  public float func_180484_a(BlockPos p_180484_1_)
  {
    return this.worldObj.getBlockState(p_180484_1_.offsetDown()).getBlock() == Blocks.stone ? 10.0F : super.func_180484_a(p_180484_1_);
  }
  
  protected boolean isValidLightLevel()
  {
    return true;
  }
  
  public boolean getCanSpawnHere()
  {
    if (super.getCanSpawnHere())
    {
      EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
      return var1 == null;
    }
    return false;
  }
  
  public EnumCreatureAttribute getCreatureAttribute()
  {
    return EnumCreatureAttribute.ARTHROPOD;
  }
  
  class AIHideInStone
    extends EntityAIWander
  {
    private EnumFacing field_179483_b;
    private boolean field_179484_c;
    private static final String __OBFID = "CL_00002205";
    
    public AIHideInStone()
    {
      super(1.0D, 10);
      setMutexBits(1);
    }
    
    public boolean shouldExecute()
    {
      if (EntitySilverfish.this.getAttackTarget() != null) {
        return false;
      }
      if (!EntitySilverfish.this.getNavigator().noPath()) {
        return false;
      }
      Random var1 = EntitySilverfish.this.getRNG();
      if (var1.nextInt(10) == 0)
      {
        this.field_179483_b = EnumFacing.random(var1);
        BlockPos var2 = new BlockPos(EntitySilverfish.this.posX, EntitySilverfish.this.posY + 0.5D, EntitySilverfish.this.posZ).offset(this.field_179483_b);
        IBlockState var3 = EntitySilverfish.this.worldObj.getBlockState(var2);
        if (BlockSilverfish.func_176377_d(var3))
        {
          this.field_179484_c = true;
          return true;
        }
      }
      this.field_179484_c = false;
      return super.shouldExecute();
    }
    
    public boolean continueExecuting()
    {
      return this.field_179484_c ? false : super.continueExecuting();
    }
    
    public void startExecuting()
    {
      if (!this.field_179484_c)
      {
        super.startExecuting();
      }
      else
      {
        World var1 = EntitySilverfish.this.worldObj;
        BlockPos var2 = new BlockPos(EntitySilverfish.this.posX, EntitySilverfish.this.posY + 0.5D, EntitySilverfish.this.posZ).offset(this.field_179483_b);
        IBlockState var3 = var1.getBlockState(var2);
        if (BlockSilverfish.func_176377_d(var3))
        {
          var1.setBlockState(var2, Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT_PROP, BlockSilverfish.EnumType.func_176878_a(var3)), 3);
          EntitySilverfish.this.spawnExplosionParticle();
          EntitySilverfish.this.setDead();
        }
      }
    }
  }
  
  class AISummonSilverfish
    extends EntityAIBase
  {
    private EntitySilverfish field_179464_a = EntitySilverfish.this;
    private int field_179463_b;
    private static final String __OBFID = "CL_00002204";
    
    AISummonSilverfish() {}
    
    public void func_179462_f()
    {
      if (this.field_179463_b == 0) {
        this.field_179463_b = 20;
      }
    }
    
    public boolean shouldExecute()
    {
      return this.field_179463_b > 0;
    }
    
    public void updateTask()
    {
      this.field_179463_b -= 1;
      if (this.field_179463_b <= 0)
      {
        World var1 = this.field_179464_a.worldObj;
        Random var2 = this.field_179464_a.getRNG();
        BlockPos var3 = new BlockPos(this.field_179464_a);
        for (int var4 = 0; (var4 <= 5) && (var4 >= -5); var4 = var4 <= 0 ? 1 - var4 : 0 - var4) {
          for (int var5 = 0; (var5 <= 10) && (var5 >= -10); var5 = var5 <= 0 ? 1 - var5 : 0 - var5) {
            for (int var6 = 0; (var6 <= 10) && (var6 >= -10); var6 = var6 <= 0 ? 1 - var6 : 0 - var6)
            {
              BlockPos var7 = var3.add(var5, var4, var6);
              IBlockState var8 = var1.getBlockState(var7);
              if (var8.getBlock() == Blocks.monster_egg)
              {
                if (var1.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                  var1.destroyBlock(var7, true);
                } else {
                  var1.setBlockState(var7, ((BlockSilverfish.EnumType)var8.getValue(BlockSilverfish.VARIANT_PROP)).func_176883_d(), 3);
                }
                if (var2.nextBoolean()) {
                  return;
                }
              }
            }
          }
        }
      }
    }
  }
}
