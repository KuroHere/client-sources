package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
	protected Block spawnableBlock = Blocks.GRASS;
	private int inLove;
	private EntityPlayer playerInLove;

	public EntityAnimal(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void updateAITasks() {
		if (this.getGrowingAge() != 0) {
			this.inLove = 0;
		}

		super.updateAITasks();
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.getGrowingAge() != 0) {
			this.inLove = 0;
		}

		if (this.inLove > 0) {
			--this.inLove;

			if ((this.inLove % 10) == 0) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle(EnumParticleTypes.HEART, (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height,
						(this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else {
			this.inLove = 0;
			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	public float getBlockPathWeight(BlockPos pos) {
		return this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.GRASS ? 10.0F : this.worldObj.getLightBrightness(pos) - 0.5F;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("InLove", this.inLove);
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset() {
		return 0.29D;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.inLove = compound.getInteger("InLove");
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.getEntityBoundingBox().minY);
		int k = MathHelper.floor_double(this.posZ);
		BlockPos blockpos = new BlockPos(i, j, k);
		return (this.worldObj.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock) && (this.worldObj.getLight(blockpos) > 8) && super.getCanSpawnHere();
	}

	/**
	 * Get number of ticks, at least during which the living entity will be silent.
	 */
	@Override
	public int getTalkInterval() {
		return 120;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return false;
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 1 + this.worldObj.rand.nextInt(3);
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(@Nullable ItemStack stack) {
		return stack == null ? false : stack.getItem() == Items.WHEAT;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		if (stack != null) {
			if (this.isBreedingItem(stack) && (this.getGrowingAge() == 0) && (this.inLove <= 0)) {
				this.consumeItemFromStack(player, stack);
				this.setInLove(player);
				return true;
			}

			if (this.isChild() && this.isBreedingItem(stack)) {
				this.consumeItemFromStack(player, stack);
				this.ageUp((int) (-this.getGrowingAge() / 20 * 0.1F), true);
				return true;
			}
		}

		return super.processInteract(player, hand, stack);
	}

	/**
	 * Decreases ItemStack size by one
	 */
	protected void consumeItemFromStack(EntityPlayer player, ItemStack stack) {
		if (!player.capabilities.isCreativeMode) {
			--stack.stackSize;
		}
	}

	public void setInLove(EntityPlayer player) {
		this.inLove = 600;
		this.playerInLove = player;
		this.worldObj.setEntityState(this, (byte) 18);
	}

	public EntityPlayer getPlayerInLove() {
		return this.playerInLove;
	}

	/**
	 * Returns if the entity is currently in 'love mode'.
	 */
	public boolean isInLove() {
		return this.inLove > 0;
	}

	public void resetInLove() {
		this.inLove = 0;
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	public boolean canMateWith(EntityAnimal otherAnimal) {
		return otherAnimal == this ? false : (otherAnimal.getClass() != this.getClass() ? false : this.isInLove() && otherAnimal.isInLove());
	}

	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 18) {
			for (int i = 0; i < 7; ++i) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle(EnumParticleTypes.HEART, (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height,
						(this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}
}
