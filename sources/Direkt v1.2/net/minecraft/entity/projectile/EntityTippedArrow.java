package net.minecraft.entity.projectile;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityTippedArrow extends EntityArrow {
	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer> createKey(EntityTippedArrow.class, DataSerializers.VARINT);
	private PotionType potion = PotionTypes.EMPTY;
	private final Set<PotionEffect> customPotionEffects = Sets.<PotionEffect> newHashSet();

	public EntityTippedArrow(World worldIn) {
		super(worldIn);
	}

	public EntityTippedArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityTippedArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public void setPotionEffect(ItemStack stack) {
		if (stack.getItem() == Items.TIPPED_ARROW) {
			this.potion = PotionUtils.getPotionTypeFromNBT(stack.getTagCompound());
			Collection<PotionEffect> collection = PotionUtils.getFullEffectsFromItem(stack);

			if (!collection.isEmpty()) {
				for (PotionEffect potioneffect : collection) {
					this.customPotionEffects.add(new PotionEffect(potioneffect));
				}
			}

			this.dataManager.set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, collection))));
		} else if (stack.getItem() == Items.ARROW) {
			this.potion = PotionTypes.EMPTY;
			this.customPotionEffects.clear();
			this.dataManager.set(COLOR, Integer.valueOf(0));
		}
	}

	public void addEffect(PotionEffect effect) {
		this.customPotionEffects.add(effect);
		this.getDataManager().set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects))));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(COLOR, Integer.valueOf(0));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.worldObj.isRemote) {
			if (this.inGround) {
				if ((this.timeInGround % 5) == 0) {
					this.spawnPotionParticles(1);
				}
			} else {
				this.spawnPotionParticles(2);
			}
		} else if (this.inGround && (this.timeInGround != 0) && !this.customPotionEffects.isEmpty() && (this.timeInGround >= 600)) {
			this.worldObj.setEntityState(this, (byte) 0);
			this.potion = PotionTypes.EMPTY;
			this.customPotionEffects.clear();
			this.dataManager.set(COLOR, Integer.valueOf(0));
		}
	}

	private void spawnPotionParticles(int particleCount) {
		int i = this.getColor();

		if ((i != 0) && (particleCount > 0)) {
			double d0 = ((i >> 16) & 255) / 255.0D;
			double d1 = ((i >> 8) & 255) / 255.0D;
			double d2 = ((i >> 0) & 255) / 255.0D;

			for (int j = 0; j < particleCount; ++j) {
				this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + ((this.rand.nextDouble() - 0.5D) * this.width), this.posY + (this.rand.nextDouble() * this.height),
						this.posZ + ((this.rand.nextDouble() - 0.5D) * this.width), d0, d1, d2, new int[0]);
			}
		}
	}

	public int getColor() {
		return this.dataManager.get(COLOR).intValue();
	}

	public static void func_189660_b(DataFixer p_189660_0_) {
		EntityArrow.func_189657_a(p_189660_0_, "TippedArrow");
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		if ((this.potion != PotionTypes.EMPTY) && (this.potion != null)) {
			compound.setString("Potion", PotionType.REGISTRY.getNameForObject(this.potion).toString());
		}

		if (!this.customPotionEffects.isEmpty()) {
			NBTTagList nbttaglist = new NBTTagList();

			for (PotionEffect potioneffect : this.customPotionEffects) {
				nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
			}

			compound.setTag("CustomPotionEffects", nbttaglist);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if (compound.hasKey("Potion", 8)) {
			this.potion = PotionUtils.getPotionTypeFromNBT(compound);
		}

		for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromTag(compound)) {
			this.addEffect(potioneffect);
		}

		if ((this.potion != PotionTypes.EMPTY) || !this.customPotionEffects.isEmpty()) {
			this.dataManager.set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects))));
		}
	}

	@Override
	protected void arrowHit(EntityLivingBase living) {
		super.arrowHit(living);

		for (PotionEffect potioneffect : this.potion.getEffects()) {
			living.addPotionEffect(
					new PotionEffect(potioneffect.getPotion(), potioneffect.getDuration() / 8, potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
		}

		if (!this.customPotionEffects.isEmpty()) {
			for (PotionEffect potioneffect1 : this.customPotionEffects) {
				living.addPotionEffect(potioneffect1);
			}
		}
	}

	@Override
	protected ItemStack getArrowStack() {
		if (this.customPotionEffects.isEmpty() && (this.potion == PotionTypes.EMPTY)) {
			return new ItemStack(Items.ARROW);
		} else {
			ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
			PotionUtils.addPotionToItemStack(itemstack, this.potion);
			PotionUtils.appendEffects(itemstack, this.customPotionEffects);
			return itemstack;
		}
	}

	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 0) {
			int i = this.getColor();

			if (i > 0) {
				double d0 = ((i >> 16) & 255) / 255.0D;
				double d1 = ((i >> 8) & 255) / 255.0D;
				double d2 = ((i >> 0) & 255) / 255.0D;

				for (int j = 0; j < 20; ++j) {
					this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + ((this.rand.nextDouble() - 0.5D) * this.width), this.posY + (this.rand.nextDouble() * this.height),
							this.posZ + ((this.rand.nextDouble() - 0.5D) * this.width), d0, d1, d2, new int[0]);
				}
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}
}
