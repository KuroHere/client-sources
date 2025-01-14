package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySquid extends EntityWaterMob {
	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;

	/**
	 * appears to be rotation in radians; we already have pitch & yaw, so this completes the triumvirate.
	 */
	public float squidRotation;

	/** previous squidRotation in radians */
	public float prevSquidRotation;

	/** angle of the tentacles in radians */
	public float tentacleAngle;

	/** the last calculated angle of the tentacles in radians */
	public float lastTentacleAngle;
	private float randomMotionSpeed;

	/** change in squidRotation in radians. */
	private float rotationVelocity;
	private float rotateSpeed;
	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;

	public EntitySquid(World worldIn) {
		super(worldIn);
		this.setSize(0.8F, 0.8F);
		this.rand.setSeed(1 + this.getEntityId());
		this.rotationVelocity = (1.0F / (this.rand.nextFloat() + 1.0F)) * 0.2F;
	}

	public static void func_189804_b(DataFixer p_189804_0_) {
		EntityLiving.func_189752_a(p_189804_0_, "Squid");
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntitySquid.AIMoveRandom(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.5F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SQUID_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_SQUID_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SQUID_DEATH;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_SQUID;
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning true)
	 */
	@Override
	public boolean isInWater() {
		return super.isInWater();
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.prevSquidPitch = this.squidPitch;
		this.prevSquidYaw = this.squidYaw;
		this.prevSquidRotation = this.squidRotation;
		this.lastTentacleAngle = this.tentacleAngle;
		this.squidRotation += this.rotationVelocity;

		if (this.squidRotation > (Math.PI * 2D)) {
			if (this.worldObj.isRemote) {
				this.squidRotation = ((float) Math.PI * 2F);
			} else {
				this.squidRotation = (float) (this.squidRotation - (Math.PI * 2D));

				if (this.rand.nextInt(10) == 0) {
					this.rotationVelocity = (1.0F / (this.rand.nextFloat() + 1.0F)) * 0.2F;
				}

				this.worldObj.setEntityState(this, (byte) 19);
			}
		}

		if (this.inWater) {
			if (this.squidRotation < (float) Math.PI) {
				float f = this.squidRotation / (float) Math.PI;
				this.tentacleAngle = MathHelper.sin(f * f * (float) Math.PI) * (float) Math.PI * 0.25F;

				if (f > 0.75D) {
					this.randomMotionSpeed = 1.0F;
					this.rotateSpeed = 1.0F;
				} else {
					this.rotateSpeed *= 0.8F;
				}
			} else {
				this.tentacleAngle = 0.0F;
				this.randomMotionSpeed *= 0.9F;
				this.rotateSpeed *= 0.99F;
			}

			if (!this.worldObj.isRemote) {
				this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
				this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
				this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
			}

			float f1 = MathHelper.sqrt_double((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
			this.renderYawOffset += ((-((float) MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float) Math.PI)) - this.renderYawOffset) * 0.1F;
			this.rotationYaw = this.renderYawOffset;
			this.squidYaw = (float) (this.squidYaw + (Math.PI * this.rotateSpeed * 1.5D));
			this.squidPitch += ((-((float) MathHelper.atan2(f1, this.motionY)) * (180F / (float) Math.PI)) - this.squidPitch) * 0.1F;
		} else {
			this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float) Math.PI * 0.25F;

			if (!this.worldObj.isRemote) {
				this.motionX = 0.0D;
				this.motionZ = 0.0D;

				if (this.isPotionActive(MobEffects.LEVITATION)) {
					this.motionY += (0.05D * (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1)) - this.motionY;
				} else if (!this.func_189652_ae()) {
					this.motionY -= 0.08D;
				}

				this.motionY *= 0.9800000190734863D;
			}

			this.squidPitch = (float) (this.squidPitch + ((-90.0F - this.squidPitch) * 0.02D));
		}
	}

	/**
	 * Moves the entity based on the specified heading.
	 */
	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return (this.posY > 45.0D) && (this.posY < this.worldObj.getSeaLevel()) && super.getCanSpawnHere();
	}

	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 19) {
			this.squidRotation = 0.0F;
		} else {
			super.handleStatusUpdate(id);
		}
	}

	public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
		this.randomMotionVecX = randomMotionVecXIn;
		this.randomMotionVecY = randomMotionVecYIn;
		this.randomMotionVecZ = randomMotionVecZIn;
	}

	public boolean hasMovementVector() {
		return (this.randomMotionVecX != 0.0F) || (this.randomMotionVecY != 0.0F) || (this.randomMotionVecZ != 0.0F);
	}

	static class AIMoveRandom extends EntityAIBase {
		private final EntitySquid squid;

		public AIMoveRandom(EntitySquid p_i45859_1_) {
			this.squid = p_i45859_1_;
		}

		@Override
		public boolean shouldExecute() {
			return true;
		}

		@Override
		public void updateTask() {
			int i = this.squid.getAge();

			if (i > 100) {
				this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
			} else if ((this.squid.getRNG().nextInt(50) == 0) || !this.squid.inWater || !this.squid.hasMovementVector()) {
				float f = this.squid.getRNG().nextFloat() * ((float) Math.PI * 2F);
				float f1 = MathHelper.cos(f) * 0.2F;
				float f2 = -0.1F + (this.squid.getRNG().nextFloat() * 0.2F);
				float f3 = MathHelper.sin(f) * 0.2F;
				this.squid.setMovementVector(f1, f2, f3);
			}
		}
	}
}
