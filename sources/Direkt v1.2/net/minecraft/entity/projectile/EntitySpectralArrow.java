package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntitySpectralArrow extends EntityArrow {
	private int duration = 200;

	public EntitySpectralArrow(World worldIn) {
		super(worldIn);
	}

	public EntitySpectralArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntitySpectralArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.worldObj.isRemote && !this.inGround) {
			this.worldObj.spawnParticle(EnumParticleTypes.SPELL_INSTANT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(Items.SPECTRAL_ARROW);
	}

	@Override
	protected void arrowHit(EntityLivingBase living) {
		super.arrowHit(living);
		PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, this.duration, 0);
		living.addPotionEffect(potioneffect);
	}

	public static void func_189659_b(DataFixer p_189659_0_) {
		EntityArrow.func_189657_a(p_189659_0_, "SpectralArrow");
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if (compound.hasKey("Duration")) {
			this.duration = compound.getInteger("Duration");
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Duration", this.duration);
	}
}
