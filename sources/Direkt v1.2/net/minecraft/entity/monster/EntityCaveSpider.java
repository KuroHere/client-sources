package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.*;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityCaveSpider extends EntitySpider {
	public EntityCaveSpider(World worldIn) {
		super(worldIn);
		this.setSize(0.7F, 0.5F);
	}

	public static void func_189775_b(DataFixer p_189775_0_) {
		EntityLiving.func_189752_a(p_189775_0_, "CaveSpider");
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (super.attackEntityAsMob(entityIn)) {
			if (entityIn instanceof EntityLivingBase) {
				int i = 0;

				if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
					i = 7;
				} else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
					i = 15;
				}

				if (i > 0) {
					((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, i * 20, 0));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	@Nullable

	/**
	 * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	 */
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		return livingdata;
	}

	@Override
	public float getEyeHeight() {
		return 0.45F;
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_CAVE_SPIDER;
	}
}
