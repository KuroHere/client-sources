package net.minecraft.entity.passive;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.datafix.*;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityVillager extends EntityAgeable implements IMerchant, INpc {
	private static final DataParameter<Integer> PROFESSION = EntityDataManager.<Integer> createKey(EntityVillager.class, DataSerializers.VARINT);
	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;

	/** This villager's current customer. */
	private EntityPlayer buyingPlayer;

	/** Initialises the MerchantRecipeList.java */
	private MerchantRecipeList buyingList;
	private int timeUntilReset;

	/** addDefaultEquipmentAndRecipies is called if this is true */
	private boolean needsInitilization;
	private boolean isWillingToMate;
	private int wealth;

	/** Last player to trade with this villager, used for aggressivity. */
	private String lastBuyingPlayer;
	private int careerId;

	/** This is the EntityVillager's career level value */
	private int careerLevel;
	private boolean isLookingForHome;
	private boolean areAdditionalTasksSet;
	private final InventoryBasic villagerInventory;

	/**
	 * A multi-dimensional array mapping the various professions, careers and career levels that a Villager may offer
	 */
	private static final EntityVillager.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new EntityVillager.ITradeList[][][][] {
			{ { { new EntityVillager.EmeraldForItems(Items.WHEAT, new EntityVillager.PriceInfo(18, 22)),
					new EntityVillager.EmeraldForItems(Items.POTATO, new EntityVillager.PriceInfo(15, 19)), new EntityVillager.EmeraldForItems(Items.CARROT, new EntityVillager.PriceInfo(15, 19)),
					new EntityVillager.ListItemForEmeralds(Items.BREAD, new EntityVillager.PriceInfo(-4, -2)) },
					{
							new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new EntityVillager.PriceInfo(8, 13)),
							new EntityVillager.ListItemForEmeralds(Items.PUMPKIN_PIE, new EntityVillager.PriceInfo(-3, -2)) },
					{ new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new EntityVillager.PriceInfo(7, 12)),
							new EntityVillager.ListItemForEmeralds(Items.APPLE, new EntityVillager.PriceInfo(-5, -7)) },
					{ new EntityVillager.ListItemForEmeralds(Items.COOKIE, new EntityVillager.PriceInfo(-6, -10)),
							new EntityVillager.ListItemForEmeralds(Items.CAKE, new EntityVillager.PriceInfo(1, 1)) } },
					{ { new EntityVillager.EmeraldForItems(Items.STRING, new EntityVillager.PriceInfo(15, 20)), new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
							new EntityVillager.ItemAndEmeraldToItem(Items.FISH, new EntityVillager.PriceInfo(6, 6), Items.COOKED_FISH, new EntityVillager.PriceInfo(6, 6)) },
							{ new EntityVillager.ListEnchantedItemForEmeralds(Items.FISHING_ROD, new EntityVillager.PriceInfo(7, 8)) } },
					{ { new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new EntityVillager.PriceInfo(16, 22)),
							new EntityVillager.ListItemForEmeralds(Items.SHEARS, new EntityVillager.PriceInfo(3, 4)) },
							{ new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new EntityVillager.PriceInfo(1, 2)),
									new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new EntityVillager.PriceInfo(1, 2)) } },
					{ { new EntityVillager.EmeraldForItems(Items.STRING, new EntityVillager.PriceInfo(15, 20)),
							new EntityVillager.ListItemForEmeralds(Items.ARROW, new EntityVillager.PriceInfo(-12, -8)) },
							{ new EntityVillager.ListItemForEmeralds(Items.BOW, new EntityVillager.PriceInfo(2, 3)),
									new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new EntityVillager.PriceInfo(10, 10), Items.FLINT,
											new EntityVillager.PriceInfo(6, 10)) } } },
			{ { { new EntityVillager.EmeraldForItems(Items.PAPER, new EntityVillager.PriceInfo(24, 36)), new EntityVillager.ListEnchantedBookForEmeralds() },
					{ new EntityVillager.EmeraldForItems(Items.BOOK, new EntityVillager.PriceInfo(8, 10)), new EntityVillager.ListItemForEmeralds(Items.COMPASS, new EntityVillager.PriceInfo(10, 12)),
							new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new EntityVillager.PriceInfo(3, 4)) },
					{ new EntityVillager.EmeraldForItems(Items.WRITTEN_BOOK, new EntityVillager.PriceInfo(2, 2)),
							new EntityVillager.ListItemForEmeralds(Items.CLOCK, new EntityVillager.PriceInfo(10, 12)),
							new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new EntityVillager.PriceInfo(-5, -3)) },
					{ new EntityVillager.ListEnchantedBookForEmeralds() }, { new EntityVillager.ListEnchantedBookForEmeralds() },
					{ new EntityVillager.ListItemForEmeralds(Items.NAME_TAG, new EntityVillager.PriceInfo(20, 22)) } } },
			{ { { new EntityVillager.EmeraldForItems(Items.ROTTEN_FLESH, new EntityVillager.PriceInfo(36, 40)),
					new EntityVillager.EmeraldForItems(Items.GOLD_INGOT, new EntityVillager.PriceInfo(8, 10)) },
					{ new EntityVillager.ListItemForEmeralds(Items.REDSTONE, new EntityVillager.PriceInfo(-4, -1)),
							new EntityVillager.ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new EntityVillager.PriceInfo(-2, -1)) },
					{ new EntityVillager.ListItemForEmeralds(Items.ENDER_PEARL, new EntityVillager.PriceInfo(4, 7)),
							new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new EntityVillager.PriceInfo(-3, -1)) },
					{ new EntityVillager.ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new EntityVillager.PriceInfo(3, 11)) } } },
			{ { { new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.IRON_HELMET, new EntityVillager.PriceInfo(4, 6)) },
					{ new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)),
							new EntityVillager.ListItemForEmeralds(Items.IRON_CHESTPLATE, new EntityVillager.PriceInfo(10, 14)) },
					{ new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)),
							new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new EntityVillager.PriceInfo(16, 19)) },
					{ new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new EntityVillager.PriceInfo(5, 7)),
							new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new EntityVillager.PriceInfo(9, 11)),
							new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_HELMET, new EntityVillager.PriceInfo(5, 7)),
							new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new EntityVillager.PriceInfo(11, 15)) } },
					{ { new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
							new EntityVillager.ListItemForEmeralds(Items.IRON_AXE, new EntityVillager.PriceInfo(6, 8)) },
							{ new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)),
									new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_SWORD, new EntityVillager.PriceInfo(9, 10)) },
							{ new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)),
									new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new EntityVillager.PriceInfo(12, 15)),
									new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new EntityVillager.PriceInfo(9, 12)) } },
					{ { new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
							new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new EntityVillager.PriceInfo(5, 7)) },
							{ new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)),
									new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new EntityVillager.PriceInfo(9, 11)) },
							{ new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)),
									new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new EntityVillager.PriceInfo(12, 15)) } } },
			{ { { new EntityVillager.EmeraldForItems(Items.PORKCHOP, new EntityVillager.PriceInfo(14, 18)), new EntityVillager.EmeraldForItems(Items.CHICKEN, new EntityVillager.PriceInfo(14, 18)) },
					{ new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)),
							new EntityVillager.ListItemForEmeralds(Items.COOKED_PORKCHOP, new EntityVillager.PriceInfo(-7, -5)),
							new EntityVillager.ListItemForEmeralds(Items.COOKED_CHICKEN, new EntityVillager.PriceInfo(-8, -6)) } },
					{ { new EntityVillager.EmeraldForItems(Items.LEATHER, new EntityVillager.PriceInfo(9, 12)),
							new EntityVillager.ListItemForEmeralds(Items.LEATHER_LEGGINGS, new EntityVillager.PriceInfo(2, 4)) },
							{ new EntityVillager.ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new EntityVillager.PriceInfo(7, 12)) },
							{ new EntityVillager.ListItemForEmeralds(Items.SADDLE, new EntityVillager.PriceInfo(8, 10)) } } } };

	public EntityVillager(World worldIn) {
		this(worldIn, 0);
	}

	public EntityVillager(World worldIn, int professionId) {
		super(worldIn);
		this.villagerInventory = new InventoryBasic("Items", false, 8);
		this.setProfession(professionId);
		this.setSize(0.6F, 1.95F);
		((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
		this.setCanPickUpLoot(true);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
		this.tasks.addTask(1, new EntityAITradePlayer(this));
		this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		this.tasks.addTask(6, new EntityAIVillagerMate(this));
		this.tasks.addTask(7, new EntityAIFollowGolem(this));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIVillagerInteract(this));
		this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	}

	private void setAdditionalAItasks() {
		if (!this.areAdditionalTasksSet) {
			this.areAdditionalTasksSet = true;

			if (this.isChild()) {
				this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
			} else if (this.getProfession() == 0) {
				this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
			}
		}
	}

	/**
	 * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as an adult)
	 */
	@Override
	protected void onGrowingAdult() {
		if (this.getProfession() == 0) {
			this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
		}

		super.onGrowingAdult();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
	}

	@Override
	protected void updateAITasks() {
		if (--this.randomTickDivider <= 0) {
			BlockPos blockpos = new BlockPos(this);
			this.worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
			this.randomTickDivider = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(blockpos, 32);

			if (this.villageObj == null) {
				this.detachHome();
			} else {
				BlockPos blockpos1 = this.villageObj.getCenter();
				this.setHomePosAndDistance(blockpos1, this.villageObj.getVillageRadius());

				if (this.isLookingForHome) {
					this.isLookingForHome = false;
					this.villageObj.setDefaultPlayerReputation(5);
				}
			}
		}

		if (!this.isTrading() && (this.timeUntilReset > 0)) {
			--this.timeUntilReset;

			if (this.timeUntilReset <= 0) {
				if (this.needsInitilization) {
					for (MerchantRecipe merchantrecipe : this.buyingList) {
						if (merchantrecipe.isRecipeDisabled()) {
							merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
						}
					}

					this.populateBuyingList();
					this.needsInitilization = false;

					if ((this.villageObj != null) && (this.lastBuyingPlayer != null)) {
						this.worldObj.setEntityState(this, (byte) 14);
						this.villageObj.modifyPlayerReputation(this.lastBuyingPlayer, 1);
					}
				}

				this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
			}
		}

		super.updateAITasks();
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		boolean flag = (stack != null) && (stack.getItem() == Items.SPAWN_EGG);

		if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
			if (!this.worldObj.isRemote && ((this.buyingList == null) || !this.buyingList.isEmpty())) {
				this.setCustomer(player);
				player.displayVillagerTradeGui(this);
			}

			player.addStat(StatList.TALKED_TO_VILLAGER);
			return true;
		} else {
			return super.processInteract(player, hand, stack);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PROFESSION, Integer.valueOf(0));
	}

	public static void func_189785_b(DataFixer p_189785_0_) {
		EntityLiving.func_189752_a(p_189785_0_, "Villager");
		p_189785_0_.registerWalker(FixTypes.ENTITY, new ItemStackDataLists("Villager", new String[] { "Inventory" }));
		p_189785_0_.registerWalker(FixTypes.ENTITY, new IDataWalker() {
			@Override
			public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
				if ("Villager".equals(compound.getString("id")) && compound.hasKey("Offers", 10)) {
					NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");

					if (nbttagcompound.hasKey("Recipes", 9)) {
						NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 10);

						for (int i = 0; i < nbttaglist.tagCount(); ++i) {
							NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
							DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buy");
							DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buyB");
							DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "sell");
							nbttaglist.set(i, nbttagcompound1);
						}
					}
				}

				return compound;
			}
		});
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Profession", this.getProfession());
		compound.setInteger("Riches", this.wealth);
		compound.setInteger("Career", this.careerId);
		compound.setInteger("CareerLevel", this.careerLevel);
		compound.setBoolean("Willing", this.isWillingToMate);

		if (this.buyingList != null) {
			compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
		}

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

			if (itemstack != null) {
				nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
			}
		}

		compound.setTag("Inventory", nbttaglist);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setProfession(compound.getInteger("Profession"));
		this.wealth = compound.getInteger("Riches");
		this.careerId = compound.getInteger("Career");
		this.careerLevel = compound.getInteger("CareerLevel");
		this.isWillingToMate = compound.getBoolean("Willing");

		if (compound.hasKey("Offers", 10)) {
			NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
			this.buyingList = new MerchantRecipeList(nbttagcompound);
		}

		NBTTagList nbttaglist = compound.getTagList("Inventory", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));

			if (itemstack != null) {
				this.villagerInventory.addItem(itemstack);
			}
		}

		this.setCanPickUpLoot(true);
		this.setAdditionalAItasks();
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_VILLAGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}

	public void setProfession(int professionId) {
		this.dataManager.set(PROFESSION, Integer.valueOf(professionId));
	}

	public int getProfession() {
		return Math.max(this.dataManager.get(PROFESSION).intValue() % 5, 0);
	}

	public boolean isMating() {
		return this.isMating;
	}

	public void setMating(boolean mating) {
		this.isMating = mating;
	}

	public void setPlaying(boolean playing) {
		this.isPlaying = playing;
	}

	public boolean isPlaying() {
		return this.isPlaying;
	}

	@Override
	public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
		super.setRevengeTarget(livingBase);

		if ((this.villageObj != null) && (livingBase != null)) {
			this.villageObj.addOrRenewAgressor(livingBase);

			if (livingBase instanceof EntityPlayer) {
				int i = -1;

				if (this.isChild()) {
					i = -3;
				}

				this.villageObj.modifyPlayerReputation(livingBase.getName(), i);

				if (this.isEntityAlive()) {
					this.worldObj.setEntityState(this, (byte) 13);
				}
			}
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource cause) {
		if (this.villageObj != null) {
			Entity entity = cause.getEntity();

			if (entity != null) {
				if (entity instanceof EntityPlayer) {
					this.villageObj.modifyPlayerReputation(entity.getName(), -2);
				} else if (entity instanceof IMob) {
					this.villageObj.endMatingSeason();
				}
			} else {
				EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

				if (entityplayer != null) {
					this.villageObj.endMatingSeason();
				}
			}
		}

		super.onDeath(cause);
	}

	@Override
	public void setCustomer(EntityPlayer player) {
		this.buyingPlayer = player;
	}

	@Override
	public EntityPlayer getCustomer() {
		return this.buyingPlayer;
	}

	public boolean isTrading() {
		return this.buyingPlayer != null;
	}

	/**
	 * Returns current or updated value of {@link #isWillingToMate}
	 */
	public boolean getIsWillingToMate(boolean updateFirst) {
		if (!this.isWillingToMate && updateFirst && this.hasEnoughFoodToBreed()) {
			boolean flag = false;

			for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
				ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

				if (itemstack != null) {
					if ((itemstack.getItem() == Items.BREAD) && (itemstack.stackSize >= 3)) {
						flag = true;
						this.villagerInventory.decrStackSize(i, 3);
					} else if (((itemstack.getItem() == Items.POTATO) || (itemstack.getItem() == Items.CARROT)) && (itemstack.stackSize >= 12)) {
						flag = true;
						this.villagerInventory.decrStackSize(i, 12);
					}
				}

				if (flag) {
					this.worldObj.setEntityState(this, (byte) 18);
					this.isWillingToMate = true;
					break;
				}
			}
		}

		return this.isWillingToMate;
	}

	public void setIsWillingToMate(boolean willingToTrade) {
		this.isWillingToMate = willingToTrade;
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		recipe.incrementToolUses();
		this.livingSoundTime = -this.getTalkInterval();
		this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
		int i = 3 + this.rand.nextInt(4);

		if ((recipe.getToolUses() == 1) || (this.rand.nextInt(5) == 0)) {
			this.timeUntilReset = 40;
			this.needsInitilization = true;
			this.isWillingToMate = true;

			if (this.buyingPlayer != null) {
				this.lastBuyingPlayer = this.buyingPlayer.getName();
			} else {
				this.lastBuyingPlayer = null;
			}

			i += 5;
		}

		if (recipe.getItemToBuy().getItem() == Items.EMERALD) {
			this.wealth += recipe.getItemToBuy().stackSize;
		}

		if (recipe.getRewardsExp()) {
			this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, i));
		}
	}

	/**
	 * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte being played depending if the suggested itemstack is not null.
	 */
	@Override
	public void verifySellingItem(ItemStack stack) {
		if (!this.worldObj.isRemote && (this.livingSoundTime > (-this.getTalkInterval() + 20))) {
			this.livingSoundTime = -this.getTalkInterval();

			if (stack != null) {
				this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
			} else {
				this.playSound(SoundEvents.ENTITY_VILLAGER_NO, this.getSoundVolume(), this.getSoundPitch());
			}
		}
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		if (this.buyingList == null) {
			this.populateBuyingList();
		}

		return this.buyingList;
	}

	private void populateBuyingList() {
		EntityVillager.ITradeList[][][] aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP[this.getProfession()];

		if ((this.careerId != 0) && (this.careerLevel != 0)) {
			++this.careerLevel;
		} else {
			this.careerId = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
			this.careerLevel = 1;
		}

		if (this.buyingList == null) {
			this.buyingList = new MerchantRecipeList();
		}

		int i = this.careerId - 1;
		int j = this.careerLevel - 1;
		EntityVillager.ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];

		if ((j >= 0) && (j < aentityvillager$itradelist1.length)) {
			EntityVillager.ITradeList[] aentityvillager$itradelist2 = aentityvillager$itradelist1[j];

			for (EntityVillager.ITradeList entityvillager$itradelist : aentityvillager$itradelist2) {
				entityvillager$itradelist.modifyMerchantRecipeList(this.buyingList, this.rand);
			}
		}
	}

	@Override
	public void setRecipes(MerchantRecipeList recipeList) {
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName() {
		Team team = this.getTeam();
		String s = this.getCustomNameTag();

		if ((s != null) && !s.isEmpty()) {
			TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, s));
			textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
			textcomponentstring.getStyle().setInsertion(this.func_189512_bd());
			return textcomponentstring;
		} else {
			if (this.buyingList == null) {
				this.populateBuyingList();
			}

			String s1 = null;

			switch (this.getProfession()) {
			case 0:
				if (this.careerId == 1) {
					s1 = "farmer";
				} else if (this.careerId == 2) {
					s1 = "fisherman";
				} else if (this.careerId == 3) {
					s1 = "shepherd";
				} else if (this.careerId == 4) {
					s1 = "fletcher";
				}

				break;

			case 1:
				s1 = "librarian";
				break;

			case 2:
				s1 = "cleric";
				break;

			case 3:
				if (this.careerId == 1) {
					s1 = "armor";
				} else if (this.careerId == 2) {
					s1 = "weapon";
				} else if (this.careerId == 3) {
					s1 = "tool";
				}

				break;

			case 4:
				if (this.careerId == 1) {
					s1 = "butcher";
				} else if (this.careerId == 2) {
					s1 = "leather";
				}
			}

			if (s1 != null) {
				TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("entity.Villager." + s1, new Object[0]);
				textcomponenttranslation.getStyle().setHoverEvent(this.getHoverEvent());
				textcomponenttranslation.getStyle().setInsertion(this.func_189512_bd());

				if (team != null) {
					textcomponenttranslation.getStyle().setColor(team.getChatFormat());
				}

				return textcomponenttranslation;
			} else {
				return super.getDisplayName();
			}
		}
	}

	@Override
	public float getEyeHeight() {
		return this.isChild() ? 0.81F : 1.62F;
	}

	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 12) {
			this.spawnParticles(EnumParticleTypes.HEART);
		} else if (id == 13) {
			this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
		} else if (id == 14) {
			this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
		} else {
			super.handleStatusUpdate(id);
		}
	}

	private void spawnParticles(EnumParticleTypes particleType) {
		for (int i = 0; i < 5; ++i) {
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(particleType, (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 1.0D + this.rand.nextFloat() * this.height,
					(this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
		}
	}

	@Override
	@Nullable

	/**
	 * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	 */
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setProfession(this.worldObj.rand.nextInt(5));
		this.setAdditionalAItasks();
		return livingdata;
	}

	public void setLookingForHome() {
		this.isLookingForHome = true;
	}

	@Override
	public EntityVillager createChild(EntityAgeable ageable) {
		EntityVillager entityvillager = new EntityVillager(this.worldObj);
		entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData) null);
		return entityvillager;
	}

	@Override
	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		if (!this.worldObj.isRemote && !this.isDead) {
			EntityWitch entitywitch = new EntityWitch(this.worldObj);
			entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			entitywitch.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entitywitch)), (IEntityLivingData) null);
			entitywitch.setNoAI(this.isAIDisabled());

			if (this.hasCustomName()) {
				entitywitch.setCustomNameTag(this.getCustomNameTag());
				entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
			}

			this.worldObj.spawnEntityInWorld(entitywitch);
			this.setDead();
		}
	}

	public InventoryBasic getVillagerInventory() {
		return this.villagerInventory;
	}

	/**
	 * Tests if this entity should pickup a weapon or an armor. Entity drops current weapon or armor if the new one is better.
	 */
	@Override
	protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
		ItemStack itemstack = itemEntity.getEntityItem();
		Item item = itemstack.getItem();

		if (this.canVillagerPickupItem(item)) {
			ItemStack itemstack1 = this.villagerInventory.addItem(itemstack);

			if (itemstack1 == null) {
				itemEntity.setDead();
			} else {
				itemstack.stackSize = itemstack1.stackSize;
			}
		}
	}

	private boolean canVillagerPickupItem(Item itemIn) {
		return (itemIn == Items.BREAD) || (itemIn == Items.POTATO) || (itemIn == Items.CARROT) || (itemIn == Items.WHEAT) || (itemIn == Items.WHEAT_SEEDS) || (itemIn == Items.BEETROOT)
				|| (itemIn == Items.BEETROOT_SEEDS);
	}

	public boolean hasEnoughFoodToBreed() {
		return this.hasEnoughItems(1);
	}

	/**
	 * Used by {@link net.minecraft.entity.ai.EntityAIVillagerInteract EntityAIVillagerInteract} to check if the villager can give some items from an inventory to another villager.
	 */
	public boolean canAbondonItems() {
		return this.hasEnoughItems(2);
	}

	public boolean wantsMoreFood() {
		boolean flag = this.getProfession() == 0;
		return flag ? !this.hasEnoughItems(5) : !this.hasEnoughItems(1);
	}

	/**
	 * Returns true if villager has enough items in inventory
	 */
	private boolean hasEnoughItems(int multiplier) {
		boolean flag = this.getProfession() == 0;

		for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

			if (itemstack != null) {
				if (((itemstack.getItem() == Items.BREAD) && (itemstack.stackSize >= (3 * multiplier))) || ((itemstack.getItem() == Items.POTATO) && (itemstack.stackSize >= (12 * multiplier)))
						|| ((itemstack.getItem() == Items.CARROT) && (itemstack.stackSize >= (12 * multiplier)))
						|| ((itemstack.getItem() == Items.BEETROOT) && (itemstack.stackSize >= (12 * multiplier)))) { return true; }

				if (flag && (itemstack.getItem() == Items.WHEAT) && (itemstack.stackSize >= (9 * multiplier))) { return true; }
			}
		}

		return false;
	}

	/**
	 * Returns true if villager has seeds, potatoes or carrots in inventory
	 */
	public boolean isFarmItemInInventory() {
		for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

			if ((itemstack != null) && ((itemstack.getItem() == Items.WHEAT_SEEDS) || (itemstack.getItem() == Items.POTATO) || (itemstack.getItem() == Items.CARROT)
					|| (itemstack.getItem() == Items.BEETROOT_SEEDS))) { return true; }
		}

		return false;
	}

	@Override
	public boolean replaceItemInInventory(int inventorySlot, @Nullable ItemStack itemStackIn) {
		if (super.replaceItemInInventory(inventorySlot, itemStackIn)) {
			return true;
		} else {
			int i = inventorySlot - 300;

			if ((i >= 0) && (i < this.villagerInventory.getSizeInventory())) {
				this.villagerInventory.setInventorySlotContents(i, itemStackIn);
				return true;
			} else {
				return false;
			}
		}
	}

	static class EmeraldForItems implements EntityVillager.ITradeList {
		public Item buyingItem;
		public EntityVillager.PriceInfo price;

		public EmeraldForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
			this.buyingItem = itemIn;
			this.price = priceIn;
		}

		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (this.price != null) {
				i = this.price.getPrice(random);
			}

			recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), Items.EMERALD));
		}
	}

	interface ITradeList {
		void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random);
	}

	static class ItemAndEmeraldToItem implements EntityVillager.ITradeList {
		public ItemStack buyingItemStack;
		public EntityVillager.PriceInfo buyingPriceInfo;
		public ItemStack sellingItemstack;
		public EntityVillager.PriceInfo sellingPriceInfo;

		public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_) {
			this.buyingItemStack = new ItemStack(p_i45813_1_);
			this.buyingPriceInfo = p_i45813_2_;
			this.sellingItemstack = new ItemStack(p_i45813_3_);
			this.sellingPriceInfo = p_i45813_4_;
		}

		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (this.buyingPriceInfo != null) {
				i = this.buyingPriceInfo.getPrice(random);
			}

			int j = 1;

			if (this.sellingPriceInfo != null) {
				j = this.sellingPriceInfo.getPrice(random);
			}

			recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD),
					new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
		}
	}

	static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList {
		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			Enchantment enchantment = Enchantment.REGISTRY.getRandomObject(random);
			int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(enchantment, i));
			int j = 2 + random.nextInt(5 + (i * 10)) + (3 * i);

			if (enchantment.isTreasureEnchantment()) {
				j *= 2;
			}

			if (j > 64) {
				j = 64;
			}

			recipeList.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
		}
	}

	static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList {
		public ItemStack enchantedItemStack;
		public EntityVillager.PriceInfo priceInfo;

		public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_) {
			this.enchantedItemStack = new ItemStack(p_i45814_1_);
			this.priceInfo = p_i45814_2_;
		}

		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (this.priceInfo != null) {
				i = this.priceInfo.getPrice(random);
			}

			ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
			ItemStack itemstack1 = new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata());
			itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15), false);
			recipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	static class ListItemForEmeralds implements EntityVillager.ITradeList {
		public ItemStack itemToBuy;
		public EntityVillager.PriceInfo priceInfo;

		public ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo) {
			this.itemToBuy = new ItemStack(par1Item);
			this.priceInfo = priceInfo;
		}

		public ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
			this.itemToBuy = stack;
			this.priceInfo = priceInfo;
		}

		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (this.priceInfo != null) {
				i = this.priceInfo.getPrice(random);
			}

			ItemStack itemstack;
			ItemStack itemstack1;

			if (i < 0) {
				itemstack = new ItemStack(Items.EMERALD);
				itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
			} else {
				itemstack = new ItemStack(Items.EMERALD, i, 0);
				itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
			}

			recipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	static class PriceInfo extends Tuple<Integer, Integer> {
		public PriceInfo(int p_i45810_1_, int p_i45810_2_) {
			super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
		}

		public int getPrice(Random rand) {
			return this.getFirst().intValue() >= this.getSecond().intValue() ? this.getFirst().intValue()
					: this.getFirst().intValue() + rand.nextInt((this.getSecond().intValue() - this.getFirst().intValue()) + 1);
		}
	}
}
