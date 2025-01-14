package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
	private static final DataParameter<Byte> PUMPKIN_EQUIPPED = EntityDataManager.<Byte> createKey(EntitySnowman.class, DataSerializers.BYTE);

	public EntitySnowman(World worldIn) {
		super(worldIn);
		this.setSize(0.7F, 1.9F);
	}

	public static void func_189783_b(DataFixer p_189783_0_) {
		EntityLiving.func_189752_a(p_189783_0_, "SnowMan");
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PUMPKIN_EQUIPPED, Byte.valueOf((byte) 0));
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.worldObj.isRemote) {
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY);
			int k = MathHelper.floor_double(this.posZ);

			if (this.isWet()) {
				this.attackEntityFrom(DamageSource.drown, 1.0F);
			}

			if (this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(new BlockPos(i, j, k)) > 1.0F) {
				this.attackEntityFrom(DamageSource.onFire, 1.0F);
			}

			if (!this.worldObj.getGameRules().getBoolean("mobGriefing")) { return; }

			for (int l = 0; l < 4; ++l) {
				i = MathHelper.floor_double(this.posX + (((l % 2) * 2) - 1) * 0.25F);
				j = MathHelper.floor_double(this.posY);
				k = MathHelper.floor_double(this.posZ + ((((l / 2) % 2) * 2) - 1) * 0.25F);
				BlockPos blockpos = new BlockPos(i, j, k);

				if ((this.worldObj.getBlockState(blockpos).getMaterial() == Material.AIR) && (this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(blockpos) < 0.8F)
						&& Blocks.SNOW_LAYER.canPlaceBlockAt(this.worldObj, blockpos)) {
					this.worldObj.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
				}
			}
		}
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_SNOWMAN;
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
		EntitySnowball entitysnowball = new EntitySnowball(this.worldObj, this);
		double d0 = (target.posY + target.getEyeHeight()) - 1.100000023841858D;
		double d1 = target.posX - this.posX;
		double d2 = d0 - entitysnowball.posY;
		double d3 = target.posZ - this.posZ;
		float f = MathHelper.sqrt_double((d1 * d1) + (d3 * d3)) * 0.2F;
		entitysnowball.setThrowableHeading(d1, d2 + f, d3, 1.6F, 12.0F);
		this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / ((this.getRNG().nextFloat() * 0.4F) + 0.8F));
		this.worldObj.spawnEntityInWorld(entitysnowball);
	}

	@Override
	public float getEyeHeight() {
		return 1.7F;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		if ((stack != null) && (stack.getItem() == Items.SHEARS) && !this.isPumpkinEquipped() && !this.worldObj.isRemote) {
			this.setPumpkinEquipped(true);
			stack.damageItem(1, player);
		}

		return super.processInteract(player, hand, stack);
	}

	public boolean isPumpkinEquipped() {
		return (this.dataManager.get(PUMPKIN_EQUIPPED).byteValue() & 16) != 0;
	}

	public void setPumpkinEquipped(boolean pumpkinEquipped) {
		byte b0 = this.dataManager.get(PUMPKIN_EQUIPPED).byteValue();

		if (pumpkinEquipped) {
			this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte) (b0 | 16)));
		} else {
			this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte) (b0 & -17)));
		}
	}

	@Override
	@Nullable
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_SNOWMAN_HURT;
	}

	@Override
	@Nullable
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SNOWMAN_DEATH;
	}
}
