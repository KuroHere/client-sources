package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable {
	public EntityEgg(World worldIn) {
		super(worldIn);
	}

	public EntityEgg(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityEgg(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static void func_189664_a(DataFixer p_189664_0_) {
		EntityThrowable.func_189661_a(p_189664_0_, "ThrownEgg");
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.entityHit != null) {
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
		}

		if (!this.worldObj.isRemote && (this.rand.nextInt(8) == 0)) {
			int i = 1;

			if (this.rand.nextInt(32) == 0) {
				i = 4;
			}

			for (int j = 0; j < i; ++j) {
				EntityChicken entitychicken = new EntityChicken(this.worldObj);
				entitychicken.setGrowingAge(-24000);
				entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				this.worldObj.spawnEntityInWorld(entitychicken);
			}
		}

		double d0 = 0.08D;

		for (int k = 0; k < 8; ++k) {
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D,
					(this.rand.nextFloat() - 0.5D) * 0.08D, new int[] { Item.getIdFromItem(Items.EGG) });
		}

		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
}
