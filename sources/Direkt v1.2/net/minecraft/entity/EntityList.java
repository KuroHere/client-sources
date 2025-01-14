package net.minecraft.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class EntityList {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<String, Class<? extends Entity>> NAME_TO_CLASS = Maps.<String, Class<? extends Entity>> newHashMap();
	private static final Map<Class<? extends Entity>, String> CLASS_TO_NAME = Maps.<Class<? extends Entity>, String> newHashMap();
	private static final Map<Integer, Class<? extends Entity>> ID_TO_CLASS = Maps.<Integer, Class<? extends Entity>> newHashMap();
	private static final Map<Class<? extends Entity>, Integer> CLASS_TO_ID = Maps.<Class<? extends Entity>, Integer> newHashMap();
	private static final Map<String, Integer> NAME_TO_ID = Maps.<String, Integer> newHashMap();
	public static final Map<String, EntityList.EntityEggInfo> ENTITY_EGGS = Maps.<String, EntityList.EntityEggInfo> newLinkedHashMap();

	/**
	 * adds a mapping between Entity classes and both a string representation and an ID
	 */
	private static void addMapping(Class<? extends Entity> entityClass, String entityName, int id) {
		if (NAME_TO_CLASS.containsKey(entityName)) {
			throw new IllegalArgumentException("ID is already registered: " + entityName);
		} else if (ID_TO_CLASS.containsKey(Integer.valueOf(id))) {
			throw new IllegalArgumentException("ID is already registered: " + id);
		} else if (id == 0) {
			throw new IllegalArgumentException("Cannot register to reserved id: " + id);
		} else if (entityClass == null) {
			throw new IllegalArgumentException("Cannot register null clazz for id: " + id);
		} else {
			NAME_TO_CLASS.put(entityName, entityClass);
			CLASS_TO_NAME.put(entityClass, entityName);
			ID_TO_CLASS.put(Integer.valueOf(id), entityClass);
			CLASS_TO_ID.put(entityClass, Integer.valueOf(id));
			NAME_TO_ID.put(entityName, Integer.valueOf(id));
		}
	}

	/**
	 * Adds a entity mapping with egg info.
	 */
	private static void addMapping(Class<? extends Entity> entityClass, String entityName, int entityID, int baseColor, int spotColor) {
		addMapping(entityClass, entityName, entityID);
		ENTITY_EGGS.put(entityName, new EntityList.EntityEggInfo(entityName, baseColor, spotColor));
	}

	@Nullable

	/**
	 * Create a new instance of an entity in the world by using the entity name.
	 */
	public static Entity createEntityByName(String entityName, World worldIn) {
		Entity entity = null;

		try {
			Class<? extends Entity> oclass = NAME_TO_CLASS.get(entityName);

			if (oclass != null) {
				entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return entity;
	}

	@Nullable

	/**
	 * create a new instance of an entity from NBT store
	 */
	public static Entity createEntityFromNBT(NBTTagCompound nbt, World worldIn) {
		Entity entity = null;

		try {
			Class<? extends Entity> oclass = NAME_TO_CLASS.get(nbt.getString("id"));

			if (oclass != null) {
				entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if (entity != null) {
			entity.readFromNBT(nbt);
		} else {
			LOGGER.warn("Skipping Entity with id {}", new Object[] { nbt.getString("id") });
		}

		return entity;
	}

	@Nullable

	/**
	 * Create a new instance of an entity in the world by using an entity ID.
	 */
	public static Entity createEntityByID(int entityID, World worldIn) {
		Entity entity = null;

		try {
			Class<? extends Entity> oclass = getClassFromID(entityID);

			if (oclass != null) {
				entity = oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if (entity == null) {
			LOGGER.warn("Skipping Entity with id {}", new Object[] { Integer.valueOf(entityID) });
		}

		return entity;
	}

	@Nullable
	public static Entity createEntityByIDFromName(String name, World worldIn) {
		return createEntityByID(getIDFromString(name), worldIn);
	}

	/**
	 * gets the entityID of a specific entity
	 */
	public static int getEntityID(Entity entityIn) {
		Integer integer = CLASS_TO_ID.get(entityIn.getClass());
		return integer == null ? 0 : integer.intValue();
	}

	@Nullable
	public static Class<? extends Entity> getClassFromID(int entityID) {
		return ID_TO_CLASS.get(Integer.valueOf(entityID));
	}

	/**
	 * Gets the string representation of a specific entity.
	 */
	public static String getEntityString(Entity entityIn) {
		return getEntityStringFromClass(entityIn.getClass());
	}

	public static String getEntityStringFromClass(Class<? extends Entity> entityClass) {
		return CLASS_TO_NAME.get(entityClass);
	}

	/**
	 * Returns the ID assigned to it's string representation
	 */
	public static int getIDFromString(String entityName) {
		Integer integer = NAME_TO_ID.get(entityName);
		return integer == null ? 90 : integer.intValue();
	}

	public static void init() {
	}

	public static List<String> getEntityNameList() {
		Set<String> set = NAME_TO_CLASS.keySet();
		List<String> list = Lists.<String> newArrayList();

		for (String s : set) {
			Class<? extends Entity> oclass = NAME_TO_CLASS.get(s);

			if ((oclass.getModifiers() & 1024) != 1024) {
				list.add(s);
			}
		}

		list.add("LightningBolt");
		return list;
	}

	public static boolean isStringEntityName(Entity entityIn, String entityName) {
		String s = getEntityString(entityIn);

		if (s == null) {
			if (entityIn instanceof EntityPlayer) {
				s = "Player";
			} else {
				if (!(entityIn instanceof EntityLightningBolt)) { return false; }

				s = "LightningBolt";
			}
		}

		return entityName.equals(s);
	}

	public static boolean isStringValidEntityName(String entityName) {
		return "Player".equals(entityName) || getEntityNameList().contains(entityName);
	}

	static {
		addMapping(EntityItem.class, "Item", 1);
		addMapping(EntityXPOrb.class, "XPOrb", 2);
		addMapping(EntityAreaEffectCloud.class, "AreaEffectCloud", 3);
		addMapping(EntityEgg.class, "ThrownEgg", 7);
		addMapping(EntityLeashKnot.class, "LeashKnot", 8);
		addMapping(EntityPainting.class, "Painting", 9);
		addMapping(EntityTippedArrow.class, "Arrow", 10);
		addMapping(EntitySnowball.class, "Snowball", 11);
		addMapping(EntityLargeFireball.class, "Fireball", 12);
		addMapping(EntitySmallFireball.class, "SmallFireball", 13);
		addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
		addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
		addMapping(EntityPotion.class, "ThrownPotion", 16);
		addMapping(EntityExpBottle.class, "ThrownExpBottle", 17);
		addMapping(EntityItemFrame.class, "ItemFrame", 18);
		addMapping(EntityWitherSkull.class, "WitherSkull", 19);
		addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
		addMapping(EntityFallingBlock.class, "FallingSand", 21);
		addMapping(EntityFireworkRocket.class, "FireworksRocketEntity", 22);
		addMapping(EntitySpectralArrow.class, "SpectralArrow", 24);
		addMapping(EntityShulkerBullet.class, "ShulkerBullet", 25);
		addMapping(EntityDragonFireball.class, "DragonFireball", 26);
		addMapping(EntityArmorStand.class, "ArmorStand", 30);
		addMapping(EntityBoat.class, "Boat", 41);
		addMapping(EntityMinecartEmpty.class, EntityMinecart.Type.RIDEABLE.getName(), 42);
		addMapping(EntityMinecartChest.class, EntityMinecart.Type.CHEST.getName(), 43);
		addMapping(EntityMinecartFurnace.class, EntityMinecart.Type.FURNACE.getName(), 44);
		addMapping(EntityMinecartTNT.class, EntityMinecart.Type.TNT.getName(), 45);
		addMapping(EntityMinecartHopper.class, EntityMinecart.Type.HOPPER.getName(), 46);
		addMapping(EntityMinecartMobSpawner.class, EntityMinecart.Type.SPAWNER.getName(), 47);
		addMapping(EntityMinecartCommandBlock.class, EntityMinecart.Type.COMMAND_BLOCK.getName(), 40);
		addMapping(EntityLiving.class, "Mob", 48);
		addMapping(EntityMob.class, "Monster", 49);
		addMapping(EntityCreeper.class, "Creeper", 50, 894731, 0);
		addMapping(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
		addMapping(EntitySpider.class, "Spider", 52, 3419431, 11013646);
		addMapping(EntityGiantZombie.class, "Giant", 53);
		addMapping(EntityZombie.class, "Zombie", 54, 44975, 7969893);
		addMapping(EntitySlime.class, "Slime", 55, 5349438, 8306542);
		addMapping(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
		addMapping(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
		addMapping(EntityEnderman.class, "Enderman", 58, 1447446, 0);
		addMapping(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
		addMapping(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
		addMapping(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
		addMapping(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
		addMapping(EntityDragon.class, "EnderDragon", 63);
		addMapping(EntityWither.class, "WitherBoss", 64);
		addMapping(EntityBat.class, "Bat", 65, 4996656, 986895);
		addMapping(EntityWitch.class, "Witch", 66, 3407872, 5349438);
		addMapping(EntityEndermite.class, "Endermite", 67, 1447446, 7237230);
		addMapping(EntityGuardian.class, "Guardian", 68, 5931634, 15826224);
		addMapping(EntityShulker.class, "Shulker", 69, 9725844, 5060690);
		addMapping(EntityPig.class, "Pig", 90, 15771042, 14377823);
		addMapping(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
		addMapping(EntityCow.class, "Cow", 92, 4470310, 10592673);
		addMapping(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
		addMapping(EntitySquid.class, "Squid", 94, 2243405, 7375001);
		addMapping(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
		addMapping(EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
		addMapping(EntitySnowman.class, "SnowMan", 97);
		addMapping(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
		addMapping(EntityIronGolem.class, "VillagerGolem", 99);
		addMapping(EntityHorse.class, "EntityHorse", 100, 12623485, 15656192);
		addMapping(EntityRabbit.class, "Rabbit", 101, 10051392, 7555121);
		addMapping(EntityPolarBear.class, "PolarBear", 102, 15921906, 9803152);
		addMapping(EntityVillager.class, "Villager", 120, 5651507, 12422002);
		addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
	}

	public static class EntityEggInfo {
		public final String spawnedID;
		public final int primaryColor;
		public final int secondaryColor;
		public final StatBase killEntityStat;
		public final StatBase entityKilledByStat;

		public EntityEggInfo(String spawnedIDIn, int primColor, int secondColor) {
			this.spawnedID = spawnedIDIn;
			this.primaryColor = primColor;
			this.secondaryColor = secondColor;
			this.killEntityStat = StatList.getStatKillEntity(this);
			this.entityKilledByStat = StatList.getStatEntityKilledBy(this);
		}
	}
}
