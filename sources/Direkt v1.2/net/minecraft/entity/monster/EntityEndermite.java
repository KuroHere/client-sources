package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEndermite extends EntityMob {
	private int lifetime;
	private boolean playerSpawned;

	public EntityEndermite(World worldIn) {
		super(worldIn);
		this.experienceValue = 3;
		this.setSize(0.4F, 0.3F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	public float getEyeHeight() {
		return 0.1F;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_ENDERMITE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMITE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_ENDERMITE;
	}

	public static void func_189764_b(DataFixer p_189764_0_) {
		EntityLiving.func_189752_a(p_189764_0_, "Endermite");
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.lifetime = compound.getInteger("Lifetime");
		this.playerSpawned = compound.getBoolean("PlayerSpawned");
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Lifetime", this.lifetime);
		compound.setBoolean("PlayerSpawned", this.playerSpawned);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset() {
		return 0.3D;
	}

	public boolean isSpawnedByPlayer() {
		return this.playerSpawned;
	}

	/**
	 * Sets if this mob was spawned by a player or not.
	 */
	public void setSpawnedByPlayer(boolean spawnedByPlayer) {
		this.playerSpawned = spawnedByPlayer;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.worldObj.isRemote) {
			for (int i = 0; i < 2; ++i) {
				this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + ((this.rand.nextDouble() - 0.5D) * this.width), this.posY + (this.rand.nextDouble() * this.height),
						this.posZ + ((this.rand.nextDouble() - 0.5D) * this.width), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D,
						new int[0]);
			}
		} else {
			if (!this.isNoDespawnRequired()) {
				++this.lifetime;
			}

			if (this.lifetime >= 2400) {
				this.setDead();
			}
		}
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
			return entityplayer == null;
		} else {
			return false;
		}
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}
}
