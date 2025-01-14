package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {
	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		IPosition iposition = BlockDispenser.getDispensePosition(source);
		EnumFacing enumfacing = source.func_189992_e().getValue(BlockDispenser.FACING);
		IProjectile iprojectile = this.getProjectileEntity(world, iposition, stack);
		iprojectile.setThrowableHeading(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY() + 0.1F, enumfacing.getFrontOffsetZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
		world.spawnEntityInWorld((Entity) iprojectile);
		stack.splitStack(1);
		return stack;
	}

	/**
	 * Play the dispense sound from the specified block.
	 */
	@Override
	protected void playDispenseSound(IBlockSource source) {
		source.getWorld().playEvent(1002, source.getBlockPos(), 0);
	}

	/**
	 * Return the projectile entity spawned by this dispense behavior.
	 */
	protected abstract IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn);

	protected float getProjectileInaccuracy() {
		return 6.0F;
	}

	protected float getProjectileVelocity() {
		return 1.1F;
	}
}
