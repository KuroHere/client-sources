package net.minecraft.entity.item;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer, ILootContainer {
	private ItemStack[] minecartContainerItems = new ItemStack[36];

	/**
	 * When set to true, the minecart will drop all items when setDead() is called. When false (such as when travelling dimensions) it preserves its contents.
	 */
	private boolean dropContentsWhenDead = true;
	private ResourceLocation lootTable;
	private long lootTableSeed;

	public EntityMinecartContainer(World worldIn) {
		super(worldIn);
	}

	public EntityMinecartContainer(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public void killMinecart(DamageSource source) {
		super.killMinecart(source);

		if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
			InventoryHelper.dropInventoryItems(this.worldObj, this, this);
		}
	}

	@Override
	@Nullable

	/**
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int index) {
		this.addLoot((EntityPlayer) null);
		return this.minecartContainerItems[index];
	}

	@Override
	@Nullable

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int index, int count) {
		this.addLoot((EntityPlayer) null);
		return ItemStackHelper.getAndSplit(this.minecartContainerItems, index, count);
	}

	@Override
	@Nullable

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int index) {
		this.addLoot((EntityPlayer) null);

		if (this.minecartContainerItems[index] != null) {
			ItemStack itemstack = this.minecartContainerItems[index];
			this.minecartContainerItems[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		this.addLoot((EntityPlayer) null);
		this.minecartContainerItems[index] = stack;

		if ((stack != null) && (stack.stackSize > this.getInventoryStackLimit())) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it hasn't changed and skip it.
	 */
	@Override
	public void markDirty() {
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.isDead ? false : player.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	@Nullable
	public Entity changeDimension(int dimensionIn) {
		this.dropContentsWhenDead = false;
		return super.changeDimension(dimensionIn);
	}

	/**
	 * Will get destroyed next tick.
	 */
	@Override
	public void setDead() {
		if (this.dropContentsWhenDead) {
			InventoryHelper.dropInventoryItems(this.worldObj, this, this);
		}

		super.setDead();
	}

	/**
	 * Sets whether this entity should drop its items when setDead() is called. This applies to container minecarts.
	 */
	@Override
	public void setDropItemsWhenDead(boolean dropWhenDead) {
		this.dropContentsWhenDead = dropWhenDead;
	}

	public static void func_189680_b(DataFixer p_189680_0_, String p_189680_1_) {
		EntityMinecart.func_189669_a(p_189680_0_, p_189680_1_);
		p_189680_0_.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(p_189680_1_, new String[] { "Items" }));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		if (this.lootTable != null) {
			compound.setString("LootTable", this.lootTable.toString());

			if (this.lootTableSeed != 0L) {
				compound.setLong("LootTableSeed", this.lootTableSeed);
			}
		} else {
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < this.minecartContainerItems.length; ++i) {
				if (this.minecartContainerItems[i] != null) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setByte("Slot", (byte) i);
					this.minecartContainerItems[i].writeToNBT(nbttagcompound);
					nbttaglist.appendTag(nbttagcompound);
				}
			}

			compound.setTag("Items", nbttaglist);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.minecartContainerItems = new ItemStack[this.getSizeInventory()];

		if (compound.hasKey("LootTable", 8)) {
			this.lootTable = new ResourceLocation(compound.getString("LootTable"));
			this.lootTableSeed = compound.getLong("LootTableSeed");
		} else {
			NBTTagList nbttaglist = compound.getTagList("Items", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				int j = nbttagcompound.getByte("Slot") & 255;

				if ((j >= 0) && (j < this.minecartContainerItems.length)) {
					this.minecartContainerItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
				}
			}
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
		if (!this.worldObj.isRemote) {
			player.displayGUIChest(this);
		}

		return true;
	}

	@Override
	protected void applyDrag() {
		float f = 0.98F;

		if (this.lootTable == null) {
			int i = 15 - Container.calcRedstoneFromInventory(this);
			f += i * 0.001F;
		}

		this.motionX *= f;
		this.motionY *= 0.0D;
		this.motionZ *= f;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public boolean isLocked() {
		return false;
	}

	@Override
	public void setLockCode(LockCode code) {
	}

	@Override
	public LockCode getLockCode() {
		return LockCode.EMPTY_CODE;
	}

	/**
	 * Adds loot to the minecart's contents.
	 */
	public void addLoot(@Nullable EntityPlayer player) {
		if (this.lootTable != null) {
			LootTable loottable = this.worldObj.getLootTableManager().getLootTableFromLocation(this.lootTable);
			this.lootTable = null;
			Random random;

			if (this.lootTableSeed == 0L) {
				random = new Random();
			} else {
				random = new Random(this.lootTableSeed);
			}

			LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) this.worldObj);

			if (player != null) {
				lootcontext$builder.withLuck(player.getLuck());
			}

			loottable.fillInventory(this, random, lootcontext$builder.build());
		}
	}

	@Override
	public void clear() {
		this.addLoot((EntityPlayer) null);

		for (int i = 0; i < this.minecartContainerItems.length; ++i) {
			this.minecartContainerItems[i] = null;
		}
	}

	public void setLootTable(ResourceLocation lootTableIn, long lootTableSeedIn) {
		this.lootTable = lootTableIn;
		this.lootTableSeed = lootTableSeedIn;
	}

	@Override
	public ResourceLocation getLootTable() {
		return this.lootTable;
	}
}
