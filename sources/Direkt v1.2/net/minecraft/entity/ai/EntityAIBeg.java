package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIBeg extends EntityAIBase {
	private final EntityWolf theWolf;
	private EntityPlayer thePlayer;
	private final World worldObject;
	private final float minPlayerDistance;
	private int timeoutCounter;

	public EntityAIBeg(EntityWolf wolf, float minDistance) {
		this.theWolf = wolf;
		this.worldObject = wolf.worldObj;
		this.minPlayerDistance = minDistance;
		this.setMutexBits(2);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theWolf, this.minPlayerDistance);
		return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !this.thePlayer.isEntityAlive() ? false
				: (this.theWolf.getDistanceSqToEntity(this.thePlayer) > this.minPlayerDistance * this.minPlayerDistance ? false
						: (this.timeoutCounter > 0) && this.hasPlayerGotBoneInHand(this.thePlayer));
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.theWolf.setBegging(true);
		this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		this.theWolf.setBegging(false);
		this.thePlayer = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, this.theWolf.getVerticalFaceSpeed());
		--this.timeoutCounter;
	}

	/**
	 * Gets if the Player has the Bone in the hand.
	 */
	private boolean hasPlayerGotBoneInHand(EntityPlayer player) {
		for (EnumHand enumhand : EnumHand.values()) {
			ItemStack itemstack = player.getHeldItem(enumhand);

			if (itemstack != null) {
				if (this.theWolf.isTamed() && (itemstack.getItem() == Items.BONE)) { return true; }

				if (this.theWolf.isBreedingItem(itemstack)) { return true; }
			}
		}

		return false;
	}
}
