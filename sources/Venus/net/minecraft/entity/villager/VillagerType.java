/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.entity.villager;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

public final class VillagerType {
    public static final VillagerType DESERT = VillagerType.register("desert");
    public static final VillagerType JUNGLE = VillagerType.register("jungle");
    public static final VillagerType PLAINS = VillagerType.register("plains");
    public static final VillagerType SAVANNA = VillagerType.register("savanna");
    public static final VillagerType SNOW = VillagerType.register("snow");
    public static final VillagerType SWAMP = VillagerType.register("swamp");
    public static final VillagerType TAIGA = VillagerType.register("taiga");
    private final String field_242370_h;
    private static final Map<RegistryKey<Biome>, VillagerType> BY_BIOME = Util.make(Maps.newHashMap(), VillagerType::lambda$static$0);

    private VillagerType(String string) {
        this.field_242370_h = string;
    }

    public String toString() {
        return this.field_242370_h;
    }

    private static VillagerType register(String string) {
        return Registry.register(Registry.VILLAGER_TYPE, new ResourceLocation(string), new VillagerType(string));
    }

    public static VillagerType func_242371_a(Optional<RegistryKey<Biome>> optional) {
        return optional.flatMap(VillagerType::lambda$func_242371_a$1).orElse(PLAINS);
    }

    private static Optional lambda$func_242371_a$1(RegistryKey registryKey) {
        return Optional.ofNullable(BY_BIOME.get(registryKey));
    }

    private static void lambda$static$0(HashMap hashMap) {
        hashMap.put(Biomes.BADLANDS, DESERT);
        hashMap.put(Biomes.BADLANDS_PLATEAU, DESERT);
        hashMap.put(Biomes.DESERT, DESERT);
        hashMap.put(Biomes.DESERT_HILLS, DESERT);
        hashMap.put(Biomes.DESERT_LAKES, DESERT);
        hashMap.put(Biomes.ERODED_BADLANDS, DESERT);
        hashMap.put(Biomes.MODIFIED_BADLANDS_PLATEAU, DESERT);
        hashMap.put(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, DESERT);
        hashMap.put(Biomes.WOODED_BADLANDS_PLATEAU, DESERT);
        hashMap.put(Biomes.BAMBOO_JUNGLE, JUNGLE);
        hashMap.put(Biomes.BAMBOO_JUNGLE_HILLS, JUNGLE);
        hashMap.put(Biomes.JUNGLE, JUNGLE);
        hashMap.put(Biomes.JUNGLE_EDGE, JUNGLE);
        hashMap.put(Biomes.JUNGLE_HILLS, JUNGLE);
        hashMap.put(Biomes.MODIFIED_JUNGLE, JUNGLE);
        hashMap.put(Biomes.MODIFIED_JUNGLE_EDGE, JUNGLE);
        hashMap.put(Biomes.SAVANNA_PLATEAU, SAVANNA);
        hashMap.put(Biomes.SAVANNA, SAVANNA);
        hashMap.put(Biomes.SHATTERED_SAVANNA, SAVANNA);
        hashMap.put(Biomes.SHATTERED_SAVANNA_PLATEAU, SAVANNA);
        hashMap.put(Biomes.DEEP_FROZEN_OCEAN, SNOW);
        hashMap.put(Biomes.FROZEN_OCEAN, SNOW);
        hashMap.put(Biomes.FROZEN_RIVER, SNOW);
        hashMap.put(Biomes.ICE_SPIKES, SNOW);
        hashMap.put(Biomes.SNOWY_BEACH, SNOW);
        hashMap.put(Biomes.SNOWY_MOUNTAINS, SNOW);
        hashMap.put(Biomes.SNOWY_TAIGA, SNOW);
        hashMap.put(Biomes.SNOWY_TAIGA_HILLS, SNOW);
        hashMap.put(Biomes.SNOWY_TAIGA_MOUNTAINS, SNOW);
        hashMap.put(Biomes.SNOWY_TUNDRA, SNOW);
        hashMap.put(Biomes.SWAMP, SWAMP);
        hashMap.put(Biomes.SWAMP_HILLS, SWAMP);
        hashMap.put(Biomes.GIANT_SPRUCE_TAIGA, TAIGA);
        hashMap.put(Biomes.GIANT_SPRUCE_TAIGA_HILLS, TAIGA);
        hashMap.put(Biomes.GIANT_TREE_TAIGA, TAIGA);
        hashMap.put(Biomes.GIANT_TREE_TAIGA_HILLS, TAIGA);
        hashMap.put(Biomes.GRAVELLY_MOUNTAINS, TAIGA);
        hashMap.put(Biomes.MODIFIED_GRAVELLY_MOUNTAINS, TAIGA);
        hashMap.put(Biomes.MOUNTAIN_EDGE, TAIGA);
        hashMap.put(Biomes.MOUNTAINS, TAIGA);
        hashMap.put(Biomes.TAIGA, TAIGA);
        hashMap.put(Biomes.TAIGA_HILLS, TAIGA);
        hashMap.put(Biomes.TAIGA_MOUNTAINS, TAIGA);
        hashMap.put(Biomes.WOODED_MOUNTAINS, TAIGA);
    }
}

