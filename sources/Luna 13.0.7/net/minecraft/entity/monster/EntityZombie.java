package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityZombie
  extends EntityMob
{
  protected static final IAttribute field_110186_bp = new RangedAttribute((IAttribute)null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D).setDescription("Spawn Reinforcements Chance");
  private static final UUID babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
  private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost", 0.5D, 1);
  private final EntityAIBreakDoor field_146075_bs = new EntityAIBreakDoor(this);
  private int conversionTime;
  private boolean field_146076_bu = false;
  private float field_146074_bv = -1.0F;
  private float field_146073_bw;
  private static final String __OBFID = "CL_00001702";
  
  public EntityZombie(World worldIn)
  {
    super(worldIn);
    ((PathNavigateGround)getNavigator()).func_179688_b(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
    this.tasks.addTask(2, this.field_175455_a);
    this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, new EntityAILookIdle(this));
    func_175456_n();
    setSize(0.6F, 1.95F);
  }
  
  protected void func_175456_n()
  {
    this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
    this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0D, true));
    this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
  }
  
  protected void applyEntityAttributes()
  {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    getAttributeMap().registerAttribute(field_110186_bp).setBaseValue(this.rand.nextDouble() * 0.10000000149011612D);
  }
  
  protected void entityInit()
  {
    super.entityInit();
    getDataWatcher().addObject(12, Byte.valueOf((byte)0));
    getDataWatcher().addObject(13, Byte.valueOf((byte)0));
    getDataWatcher().addObject(14, Byte.valueOf((byte)0));
  }
  
  public int getTotalArmorValue()
  {
    int var1 = super.getTotalArmorValue() + 2;
    if (var1 > 20) {
      var1 = 20;
    }
    return var1;
  }
  
  public boolean func_146072_bX()
  {
    return this.field_146076_bu;
  }
  
  public void func_146070_a(boolean p_146070_1_)
  {
    if (this.field_146076_bu != p_146070_1_)
    {
      this.field_146076_bu = p_146070_1_;
      if (p_146070_1_) {
        this.tasks.addTask(1, this.field_146075_bs);
      } else {
        this.tasks.removeTask(this.field_146075_bs);
      }
    }
  }
  
  public boolean isChild()
  {
    return getDataWatcher().getWatchableObjectByte(12) == 1;
  }
  
  protected int getExperiencePoints(EntityPlayer p_70693_1_)
  {
    if (isChild()) {
      this.experienceValue = ((int)(this.experienceValue * 2.5F));
    }
    return super.getExperiencePoints(p_70693_1_);
  }
  
  public void setChild(boolean p_82227_1_)
  {
    getDataWatcher().updateObject(12, Byte.valueOf((byte)(p_82227_1_ ? 1 : 0)));
    if ((this.worldObj != null) && (!this.worldObj.isRemote))
    {
      IAttributeInstance var2 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      var2.removeModifier(babySpeedBoostModifier);
      if (p_82227_1_) {
        var2.applyModifier(babySpeedBoostModifier);
      }
    }
    func_146071_k(p_82227_1_);
  }
  
  public boolean isVillager()
  {
    return getDataWatcher().getWatchableObjectByte(13) == 1;
  }
  
  public void setVillager(boolean p_82229_1_)
  {
    getDataWatcher().updateObject(13, Byte.valueOf((byte)(p_82229_1_ ? 1 : 0)));
  }
  
  public void onLivingUpdate()
  {
    if ((this.worldObj.isDaytime()) && (!this.worldObj.isRemote) && (!isChild()))
    {
      float var1 = getBrightness(1.0F);
      BlockPos var2 = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
      if ((var1 > 0.5F) && (this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) && (this.worldObj.isAgainstSky(var2)))
      {
        boolean var3 = true;
        ItemStack var4 = getEquipmentInSlot(4);
        if (var4 != null)
        {
          if (var4.isItemStackDamageable())
          {
            var4.setItemDamage(var4.getItemDamage() + this.rand.nextInt(2));
            if (var4.getItemDamage() >= var4.getMaxDamage())
            {
              renderBrokenItemStack(var4);
              setCurrentItemOrArmor(4, (ItemStack)null);
            }
          }
          var3 = false;
        }
        if (var3) {
          setFire(8);
        }
      }
    }
    if ((isRiding()) && (getAttackTarget() != null) && ((this.ridingEntity instanceof EntityChicken))) {
      ((EntityLiving)this.ridingEntity).getNavigator().setPath(getNavigator().getPath(), 1.5D);
    }
    super.onLivingUpdate();
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (super.attackEntityFrom(source, amount))
    {
      EntityLivingBase var3 = getAttackTarget();
      if ((var3 == null) && ((source.getEntity() instanceof EntityLivingBase))) {
        var3 = (EntityLivingBase)source.getEntity();
      }
      if ((var3 != null) && (this.worldObj.getDifficulty() == EnumDifficulty.HARD) && (this.rand.nextFloat() < getEntityAttribute(field_110186_bp).getAttributeValue()))
      {
        int var4 = MathHelper.floor_double(this.posX);
        int var5 = MathHelper.floor_double(this.posY);
        int var6 = MathHelper.floor_double(this.posZ);
        EntityZombie var7 = new EntityZombie(this.worldObj);
        for (int var8 = 0; var8 < 50; var8++)
        {
          int var9 = var4 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
          int var10 = var5 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
          int var11 = var6 + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
          if ((World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var9, var10 - 1, var11))) && (this.worldObj.getLightFromNeighbors(new BlockPos(var9, var10, var11)) < 10))
          {
            var7.setPosition(var9, var10, var11);
            if ((!this.worldObj.func_175636_b(var9, var10, var11, 7.0D)) && (this.worldObj.checkNoEntityCollision(var7.getEntityBoundingBox(), var7)) && (this.worldObj.getCollidingBoundingBoxes(var7, var7.getEntityBoundingBox()).isEmpty()) && (!this.worldObj.isAnyLiquid(var7.getEntityBoundingBox())))
            {
              this.worldObj.spawnEntityInWorld(var7);
              var7.setAttackTarget(var3);
              var7.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var7)), (IEntityLivingData)null);
              getEntityAttribute(field_110186_bp).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
              var7.getEntityAttribute(field_110186_bp).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
              break;
            }
          }
        }
      }
      return true;
    }
    return false;
  }
  
  public void onUpdate()
  {
    if ((!this.worldObj.isRemote) && (isConverting()))
    {
      int var1 = getConversionTimeBoost();
      this.conversionTime -= var1;
      if (this.conversionTime <= 0) {
        convertToVillager();
      }
    }
    super.onUpdate();
  }
  
  public boolean attackEntityAsMob(Entity p_70652_1_)
  {
    boolean var2 = super.attackEntityAsMob(p_70652_1_);
    if (var2)
    {
      int var3 = this.worldObj.getDifficulty().getDifficultyId();
      if ((getHeldItem() == null) && (isBurning()) && (this.rand.nextFloat() < var3 * 0.3F)) {
        p_70652_1_.setFire(2 * var3);
      }
    }
    return var2;
  }
  
  protected String getLivingSound()
  {
    return "mob.zombie.say";
  }
  
  protected String getHurtSound()
  {
    return "mob.zombie.hurt";
  }
  
  protected String getDeathSound()
  {
    return "mob.zombie.death";
  }
  
  protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_)
  {
    playSound("mob.zombie.step", 0.15F, 1.0F);
  }
  
  protected Item getDropItem()
  {
    return Items.rotten_flesh;
  }
  
  public EnumCreatureAttribute getCreatureAttribute()
  {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  protected void addRandomArmor()
  {
    switch (this.rand.nextInt(3))
    {
    case 0: 
      dropItem(Items.iron_ingot, 1);
      break;
    case 1: 
      dropItem(Items.carrot, 1);
      break;
    case 2: 
      dropItem(Items.potato, 1);
    }
  }
  
  protected void func_180481_a(DifficultyInstance p_180481_1_)
  {
    super.func_180481_a(p_180481_1_);
    if (this.rand.nextFloat() < (this.worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F))
    {
      int var2 = this.rand.nextInt(3);
      if (var2 == 0) {
        setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
      } else {
        setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
      }
    }
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    super.writeEntityToNBT(tagCompound);
    if (isChild()) {
      tagCompound.setBoolean("IsBaby", true);
    }
    if (isVillager()) {
      tagCompound.setBoolean("IsVillager", true);
    }
    tagCompound.setInteger("ConversionTime", isConverting() ? this.conversionTime : -1);
    tagCompound.setBoolean("CanBreakDoors", func_146072_bX());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    super.readEntityFromNBT(tagCompund);
    if (tagCompund.getBoolean("IsBaby")) {
      setChild(true);
    }
    if (tagCompund.getBoolean("IsVillager")) {
      setVillager(true);
    }
    if ((tagCompund.hasKey("ConversionTime", 99)) && (tagCompund.getInteger("ConversionTime") > -1)) {
      startConversion(tagCompund.getInteger("ConversionTime"));
    }
    func_146070_a(tagCompund.getBoolean("CanBreakDoors"));
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn)
  {
    super.onKillEntity(entityLivingIn);
    if (((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) || (this.worldObj.getDifficulty() == EnumDifficulty.HARD)) && ((entityLivingIn instanceof EntityVillager)))
    {
      if ((this.worldObj.getDifficulty() != EnumDifficulty.HARD) && (this.rand.nextBoolean())) {
        return;
      }
      EntityZombie var2 = new EntityZombie(this.worldObj);
      var2.copyLocationAndAnglesFrom(entityLivingIn);
      this.worldObj.removeEntity(entityLivingIn);
      var2.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var2)), (IEntityLivingData)null);
      var2.setVillager(true);
      if (entityLivingIn.isChild()) {
        var2.setChild(true);
      }
      this.worldObj.spawnEntityInWorld(var2);
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }
  }
  
  public float getEyeHeight()
  {
    float var1 = 1.74F;
    if (isChild()) {
      var1 = (float)(var1 - 0.81D);
    }
    return var1;
  }
  
  protected boolean func_175448_a(ItemStack p_175448_1_)
  {
    return (p_175448_1_.getItem() == Items.egg) && (isChild()) && (isRiding()) ? false : super.func_175448_a(p_175448_1_);
  }
  
  public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
  {
    Object p_180482_2_1 = super.func_180482_a(p_180482_1_, p_180482_2_);
    float var3 = p_180482_1_.func_180170_c();
    setCanPickUpLoot(this.rand.nextFloat() < 0.55F * var3);
    if (p_180482_2_1 == null) {
      p_180482_2_1 = new GroupData(this.worldObj.rand.nextFloat() < 0.05F, this.worldObj.rand.nextFloat() < 0.05F, null);
    }
    if ((p_180482_2_1 instanceof GroupData))
    {
      GroupData var4 = (GroupData)p_180482_2_1;
      if (var4.field_142046_b) {
        setVillager(true);
      }
      if (var4.field_142048_a)
      {
        setChild(true);
        if (this.worldObj.rand.nextFloat() < 0.05D)
        {
          List var5 = this.worldObj.func_175647_a(EntityChicken.class, getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), IEntitySelector.field_152785_b);
          if (!var5.isEmpty())
          {
            EntityChicken var6 = (EntityChicken)var5.get(0);
            var6.func_152117_i(true);
            mountEntity(var6);
          }
        }
        else if (this.worldObj.rand.nextFloat() < 0.05D)
        {
          EntityChicken var10 = new EntityChicken(this.worldObj);
          var10.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
          var10.func_180482_a(p_180482_1_, (IEntityLivingData)null);
          var10.func_152117_i(true);
          this.worldObj.spawnEntityInWorld(var10);
          mountEntity(var10);
        }
      }
    }
    func_146070_a(this.rand.nextFloat() < var3 * 0.1F);
    func_180481_a(p_180482_1_);
    func_180483_b(p_180482_1_);
    if (getEquipmentInSlot(4) == null)
    {
      Calendar var8 = this.worldObj.getCurrentDate();
      if ((var8.get(2) + 1 == 10) && (var8.get(5) == 31) && (this.rand.nextFloat() < 0.25F))
      {
        setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
        this.equipmentDropChances[4] = 0.0F;
      }
    }
    getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
    double var9 = this.rand.nextDouble() * 1.5D * var3;
    if (var9 > 1.0D) {
      getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", var9, 2));
    }
    if (this.rand.nextFloat() < var3 * 0.05F)
    {
      getEntityAttribute(field_110186_bp).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
      getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
      func_146070_a(true);
    }
    return (IEntityLivingData)p_180482_2_1;
  }
  
  public boolean interact(EntityPlayer p_70085_1_)
  {
    ItemStack var2 = p_70085_1_.getCurrentEquippedItem();
    if ((var2 != null) && (var2.getItem() == Items.golden_apple) && (var2.getMetadata() == 0) && (isVillager()) && (isPotionActive(Potion.weakness)))
    {
      if (!p_70085_1_.capabilities.isCreativeMode) {
        var2.stackSize -= 1;
      }
      if (var2.stackSize <= 0) {
        p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack)null);
      }
      if (!this.worldObj.isRemote) {
        startConversion(this.rand.nextInt(2401) + 3600);
      }
      return true;
    }
    return false;
  }
  
  protected void startConversion(int p_82228_1_)
  {
    this.conversionTime = p_82228_1_;
    getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
    removePotionEffect(Potion.weakness.id);
    addPotionEffect(new PotionEffect(Potion.damageBoost.id, p_82228_1_, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
    this.worldObj.setEntityState(this, (byte)16);
  }
  
  public void handleHealthUpdate(byte p_70103_1_)
  {
    if (p_70103_1_ == 16)
    {
      if (!isSlient()) {
        this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
      }
    }
    else {
      super.handleHealthUpdate(p_70103_1_);
    }
  }
  
  protected boolean canDespawn()
  {
    return !isConverting();
  }
  
  public boolean isConverting()
  {
    return getDataWatcher().getWatchableObjectByte(14) == 1;
  }
  
  protected void convertToVillager()
  {
    EntityVillager var1 = new EntityVillager(this.worldObj);
    var1.copyLocationAndAnglesFrom(this);
    var1.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(var1)), (IEntityLivingData)null);
    var1.setLookingForHome();
    if (isChild()) {
      var1.setGrowingAge(41536);
    }
    this.worldObj.removeEntity(this);
    this.worldObj.spawnEntityInWorld(var1);
    var1.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
  }
  
  protected int getConversionTimeBoost()
  {
    int var1 = 1;
    if (this.rand.nextFloat() < 0.01F)
    {
      int var2 = 0;
      for (int var3 = (int)this.posX - 4; (var3 < (int)this.posX + 4) && (var2 < 14); var3++) {
        for (int var4 = (int)this.posY - 4; (var4 < (int)this.posY + 4) && (var2 < 14); var4++) {
          for (int var5 = (int)this.posZ - 4; (var5 < (int)this.posZ + 4) && (var2 < 14); var5++)
          {
            Block var6 = this.worldObj.getBlockState(new BlockPos(var3, var4, var5)).getBlock();
            if ((var6 == Blocks.iron_bars) || (var6 == Blocks.bed))
            {
              if (this.rand.nextFloat() < 0.3F) {
                var1++;
              }
              var2++;
            }
          }
        }
      }
    }
    return var1;
  }
  
  public void func_146071_k(boolean p_146071_1_)
  {
    func_146069_a(p_146071_1_ ? 0.5F : 1.0F);
  }
  
  protected final void setSize(float width, float height)
  {
    boolean var3 = (this.field_146074_bv > 0.0F) && (this.field_146073_bw > 0.0F);
    this.field_146074_bv = width;
    this.field_146073_bw = height;
    if (!var3) {
      func_146069_a(1.0F);
    }
  }
  
  protected final void func_146069_a(float p_146069_1_)
  {
    super.setSize(this.field_146074_bv * p_146069_1_, this.field_146073_bw * p_146069_1_);
  }
  
  public double getYOffset()
  {
    return super.getYOffset() - 0.5D;
  }
  
  public void onDeath(DamageSource cause)
  {
    super.onDeath(cause);
    if (((cause.getEntity() instanceof EntityCreeper)) && (!(this instanceof EntityPigZombie)) && (((EntityCreeper)cause.getEntity()).getPowered()) && (((EntityCreeper)cause.getEntity()).isAIEnabled()))
    {
      ((EntityCreeper)cause.getEntity()).func_175493_co();
      entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0F);
    }
  }
  
  class GroupData
    implements IEntityLivingData
  {
    public boolean field_142048_a;
    public boolean field_142046_b;
    private static final String __OBFID = "CL_00001704";
    
    private GroupData(boolean p_i2348_2_, boolean p_i2348_3_)
    {
      this.field_142048_a = false;
      this.field_142046_b = false;
      this.field_142048_a = p_i2348_2_;
      this.field_142046_b = p_i2348_3_;
    }
    
    GroupData(boolean p_i2349_2_, boolean p_i2349_3_, Object p_i2349_4_)
    {
      this(p_i2349_2_, p_i2349_3_);
    }
  }
}
