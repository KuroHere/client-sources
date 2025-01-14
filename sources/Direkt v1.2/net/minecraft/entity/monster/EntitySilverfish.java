package net.minecraft.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySilverfish extends EntityMob {
	private EntitySilverfish.AISummonSilverfish summonSilverfish;

	public EntitySilverfish(World worldIn) {
		super(worldIn);
		this.setSize(0.4F, 0.3F);
	}

	public static void func_189767_b(DataFixer p_189767_0_) {
		EntityLiving.func_189752_a(p_189767_0_, "Silverfish");
	}

	@Override
	protected void initEntityAI() {
		this.summonSilverfish = new EntitySilverfish.AISummonSilverfish(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, this.summonSilverfish);
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntitySilverfish.AIHideInStone(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset() {
		return 0.2D;
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
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
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
		return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_SILVERFISH_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SILVERFISH_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else {
			if (((source instanceof EntityDamageSource) || (source == DamageSource.magic)) && (this.summonSilverfish != null)) {
				this.summonSilverfish.notifyHurt();
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_SILVERFISH;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	@Override
	public float getBlockPathWeight(BlockPos pos) {
		return this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.STONE ? 10.0F : super.getBlockPathWeight(pos);
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
			EntityPlayer entityplayer = this.worldObj.getNearestPlayerNotCreative(this, 5.0D);
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

	static class AIHideInStone extends EntityAIWander {
		private final EntitySilverfish silverfish;
		private EnumFacing facing;
		private boolean doMerge;

		public AIHideInStone(EntitySilverfish silverfishIn) {
			super(silverfishIn, 1.0D, 10);
			this.silverfish = silverfishIn;
			this.setMutexBits(1);
		}

		@Override
		public boolean shouldExecute() {
			if (!this.silverfish.worldObj.getGameRules().getBoolean("mobGriefing")) {
				return false;
			} else if (this.silverfish.getAttackTarget() != null) {
				return false;
			} else if (!this.silverfish.getNavigator().noPath()) {
				return false;
			} else {
				Random random = this.silverfish.getRNG();

				if (random.nextInt(10) == 0) {
					this.facing = EnumFacing.random(random);
					BlockPos blockpos = (new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5D, this.silverfish.posZ)).offset(this.facing);
					IBlockState iblockstate = this.silverfish.worldObj.getBlockState(blockpos);

					if (BlockSilverfish.canContainSilverfish(iblockstate)) {
						this.doMerge = true;
						return true;
					}
				}

				this.doMerge = false;
				return super.shouldExecute();
			}
		}

		@Override
		public boolean continueExecuting() {
			return this.doMerge ? false : super.continueExecuting();
		}

		@Override
		public void startExecuting() {
			if (!this.doMerge) {
				super.startExecuting();
			} else {
				World world = this.silverfish.worldObj;
				BlockPos blockpos = (new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5D, this.silverfish.posZ)).offset(this.facing);
				IBlockState iblockstate = world.getBlockState(blockpos);

				if (BlockSilverfish.canContainSilverfish(iblockstate)) {
					world.setBlockState(blockpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
					this.silverfish.spawnExplosionParticle();
					this.silverfish.setDead();
				}
			}
		}
	}

	static class AISummonSilverfish extends EntityAIBase {
		private final EntitySilverfish silverfish;
		private int lookForFriends;

		public AISummonSilverfish(EntitySilverfish silverfishIn) {
			this.silverfish = silverfishIn;
		}

		public void notifyHurt() {
			if (this.lookForFriends == 0) {
				this.lookForFriends = 20;
			}
		}

		@Override
		public boolean shouldExecute() {
			return this.lookForFriends > 0;
		}

		@Override
		public void updateTask() {
			--this.lookForFriends;

			if (this.lookForFriends <= 0) {
				World world = this.silverfish.worldObj;
				Random random = this.silverfish.getRNG();
				BlockPos blockpos = new BlockPos(this.silverfish);

				for (int i = 0; (i <= 5) && (i >= -5); i = i <= 0 ? 1 - i : 0 - i) {
					for (int j = 0; (j <= 10) && (j >= -10); j = j <= 0 ? 1 - j : 0 - j) {
						for (int k = 0; (k <= 10) && (k >= -10); k = k <= 0 ? 1 - k : 0 - k) {
							BlockPos blockpos1 = blockpos.add(j, i, k);
							IBlockState iblockstate = world.getBlockState(blockpos1);

							if (iblockstate.getBlock() == Blocks.MONSTER_EGG) {
								if (world.getGameRules().getBoolean("mobGriefing")) {
									world.destroyBlock(blockpos1, true);
								} else {
									world.setBlockState(blockpos1, iblockstate.getValue(BlockSilverfish.VARIANT).getModelBlock(), 3);
								}

								if (random.nextBoolean()) { return; }
							}
						}
					}
				}
			}
		}
	}
}
