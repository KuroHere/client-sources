package net.minecraft.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class EntitySpawnPlacementRegistry
{
    private static final Map < EntityType<?>, EntitySpawnPlacementRegistry.Entry > REGISTRY = Maps.newHashMap();

    private static <T extends MobEntity> void register(EntityType<T> entityTypeIn, EntitySpawnPlacementRegistry.PlacementType placementType, Heightmap.Type heightMapType, EntitySpawnPlacementRegistry.IPlacementPredicate<T> placementPredicate)
    {
        EntitySpawnPlacementRegistry.Entry entityspawnplacementregistry$entry = REGISTRY.put(entityTypeIn, new EntitySpawnPlacementRegistry.Entry(heightMapType, placementType, placementPredicate));

        if (entityspawnplacementregistry$entry != null)
        {
            throw new IllegalStateException("Duplicate registration for type " + Registry.ENTITY_TYPE.getKey(entityTypeIn));
        }
    }

    public static EntitySpawnPlacementRegistry.PlacementType getPlacementType(EntityType<?> entityTypeIn)
    {
        EntitySpawnPlacementRegistry.Entry entityspawnplacementregistry$entry = REGISTRY.get(entityTypeIn);
        return entityspawnplacementregistry$entry == null ? EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS : entityspawnplacementregistry$entry.placementType;
    }

    public static Heightmap.Type func_209342_b(@Nullable EntityType<?> entityTypeIn)
    {
        EntitySpawnPlacementRegistry.Entry entityspawnplacementregistry$entry = REGISTRY.get(entityTypeIn);
        return entityspawnplacementregistry$entry == null ? Heightmap.Type.MOTION_BLOCKING_NO_LEAVES : entityspawnplacementregistry$entry.type;
    }

    public static <T extends Entity> boolean canSpawnEntity(EntityType<T> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random rand)
    {
        EntitySpawnPlacementRegistry.Entry entityspawnplacementregistry$entry = REGISTRY.get(entityType);
        return entityspawnplacementregistry$entry == null || entityspawnplacementregistry$entry.placementPredicate.test((EntityType)entityType, world, reason, pos, rand);
    }

    static
    {
        register(EntityType.COD, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.DOLPHIN, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DolphinEntity::func_223364_b);
        register(EntityType.DROWNED, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownedEntity::func_223332_b);
        register(EntityType.GUARDIAN, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GuardianEntity::func_223329_b);
        register(EntityType.PUFFERFISH, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.SALMON, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.SQUID, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SquidEntity::func_223365_b);
        register(EntityType.TROPICAL_FISH, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
        register(EntityType.BAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BatEntity::canSpawn);
        register(EntityType.BLAZE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        register(EntityType.CAVE_SPIDER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.CHICKEN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.COW, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.CREEPER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.DONKEY, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ENDERMAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.ENDERMITE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndermiteEntity::func_223328_b);
        register(EntityType.ENDER_DRAGON, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.GHAST, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhastEntity::func_223368_b);
        register(EntityType.GIANT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.HORSE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.HUSK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HuskEntity::func_223334_b);
        register(EntityType.IRON_GOLEM, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.LLAMA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.MAGMA_CUBE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MagmaCubeEntity::func_223367_b);
        register(EntityType.MOOSHROOM, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MooshroomEntity::func_223318_c);
        register(EntityType.MULE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.OCELOT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, OcelotEntity::func_223319_c);
        register(EntityType.PARROT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, ParrotEntity::func_223317_c);
        register(EntityType.PIG, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.HOGLIN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HoglinEntity::func_234361_c_);
        register(EntityType.PIGLIN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PiglinEntity::func_234418_b_);
        register(EntityType.PILLAGER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PatrollerEntity::func_223330_b);
        register(EntityType.POLAR_BEAR, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PolarBearEntity::func_223320_c);
        register(EntityType.RABBIT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RabbitEntity::func_223321_c);
        register(EntityType.SHEEP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.SILVERFISH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SilverfishEntity::func_223331_b);
        register(EntityType.SKELETON, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.SKELETON_HORSE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.SLIME, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SlimeEntity::func_223366_c);
        register(EntityType.SNOW_GOLEM, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.SPIDER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.STRAY, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StrayEntity::func_223327_b);
        register(EntityType.STRIDER, EntitySpawnPlacementRegistry.PlacementType.IN_LAVA, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StriderEntity::func_234314_c_);
        register(EntityType.TURTLE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TurtleEntity::func_223322_c);
        register(EntityType.VILLAGER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.WITCH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WITHER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WITHER_SKELETON, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WOLF, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ZOMBIE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.ZOMBIE_HORSE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ZOMBIFIED_PIGLIN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ZombifiedPiglinEntity::func_234351_b_);
        register(EntityType.ZOMBIE_VILLAGER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.CAT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ELDER_GUARDIAN, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GuardianEntity::func_223329_b);
        register(EntityType.EVOKER, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.FOX, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.ILLUSIONER, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.PANDA, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.PHANTOM, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.RAVAGER, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.SHULKER, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        register(EntityType.TRADER_LLAMA, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        register(EntityType.VEX, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.VINDICATOR, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        register(EntityType.WANDERING_TRADER, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
    }

    static class Entry
    {
        private final Heightmap.Type type;
        private final EntitySpawnPlacementRegistry.PlacementType placementType;
        private final EntitySpawnPlacementRegistry.IPlacementPredicate<?> placementPredicate;

        public Entry(Heightmap.Type typeIn, EntitySpawnPlacementRegistry.PlacementType placementTypeIn, EntitySpawnPlacementRegistry.IPlacementPredicate<?> placementPredicateIn)
        {
            this.type = typeIn;
            this.placementType = placementTypeIn;
            this.placementPredicate = placementPredicateIn;
        }
    }

    @FunctionalInterface
    public interface IPlacementPredicate<T extends Entity>
    {
        boolean test(EntityType<T> p_test_1_, IServerWorld p_test_2_, SpawnReason p_test_3_, BlockPos p_test_4_, Random p_test_5_);
    }

    public static enum PlacementType
    {
        ON_GROUND,
        IN_WATER,
        NO_RESTRICTIONS,
        IN_LAVA;
    }
}
