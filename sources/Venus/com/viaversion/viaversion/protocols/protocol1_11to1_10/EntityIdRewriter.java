/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.protocols.protocol1_11to1_10;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;

public class EntityIdRewriter {
    private static final BiMap<String, String> oldToNewNames = HashBiMap.create();

    public static void toClient(CompoundTag compoundTag) {
        EntityIdRewriter.toClient(compoundTag, false);
    }

    public static void toClient(CompoundTag compoundTag, boolean bl) {
        Object t = compoundTag.get("id");
        if (t instanceof StringTag) {
            String string;
            StringTag stringTag = (StringTag)t;
            String string2 = string = bl ? (String)oldToNewNames.inverse().get(stringTag.getValue()) : (String)oldToNewNames.get(stringTag.getValue());
            if (string != null) {
                stringTag.setValue(string);
            }
        }
    }

    public static void toClientSpawner(CompoundTag compoundTag) {
        EntityIdRewriter.toClientSpawner(compoundTag, false);
    }

    public static void toClientSpawner(CompoundTag compoundTag, boolean bl) {
        if (compoundTag == null) {
            return;
        }
        Object t = compoundTag.get("SpawnData");
        if (t != null) {
            EntityIdRewriter.toClient((CompoundTag)t, bl);
        }
    }

    public static void toClientItem(Item item) {
        EntityIdRewriter.toClientItem(item, false);
    }

    public static void toClientItem(Item item, boolean bl) {
        if (EntityIdRewriter.hasEntityTag(item)) {
            EntityIdRewriter.toClient((CompoundTag)item.tag().get("EntityTag"), bl);
        }
        if (item != null && item.amount() <= 0) {
            item.setAmount(1);
        }
    }

    public static void toServerItem(Item item) {
        EntityIdRewriter.toServerItem(item, false);
    }

    public static void toServerItem(Item item, boolean bl) {
        if (!EntityIdRewriter.hasEntityTag(item)) {
            return;
        }
        CompoundTag compoundTag = (CompoundTag)item.tag().get("EntityTag");
        Object t = compoundTag.get("id");
        if (t instanceof StringTag) {
            String string;
            StringTag stringTag = (StringTag)t;
            String string2 = string = bl ? (String)oldToNewNames.get(stringTag.getValue()) : (String)oldToNewNames.inverse().get(stringTag.getValue());
            if (string != null) {
                stringTag.setValue(string);
            }
        }
    }

    private static boolean hasEntityTag(Item item) {
        if (item == null || item.identifier() != 383) {
            return true;
        }
        CompoundTag compoundTag = item.tag();
        if (compoundTag == null) {
            return true;
        }
        Object t = compoundTag.get("EntityTag");
        return t instanceof CompoundTag && ((CompoundTag)t).get("id") instanceof StringTag;
    }

    static {
        oldToNewNames.put("AreaEffectCloud", "minecraft:area_effect_cloud");
        oldToNewNames.put("ArmorStand", "minecraft:armor_stand");
        oldToNewNames.put("Arrow", "minecraft:arrow");
        oldToNewNames.put("Bat", "minecraft:bat");
        oldToNewNames.put("Blaze", "minecraft:blaze");
        oldToNewNames.put("Boat", "minecraft:boat");
        oldToNewNames.put("CaveSpider", "minecraft:cave_spider");
        oldToNewNames.put("Chicken", "minecraft:chicken");
        oldToNewNames.put("Cow", "minecraft:cow");
        oldToNewNames.put("Creeper", "minecraft:creeper");
        oldToNewNames.put("Donkey", "minecraft:donkey");
        oldToNewNames.put("DragonFireball", "minecraft:dragon_fireball");
        oldToNewNames.put("ElderGuardian", "minecraft:elder_guardian");
        oldToNewNames.put("EnderCrystal", "minecraft:ender_crystal");
        oldToNewNames.put("EnderDragon", "minecraft:ender_dragon");
        oldToNewNames.put("Enderman", "minecraft:enderman");
        oldToNewNames.put("Endermite", "minecraft:endermite");
        oldToNewNames.put("EntityHorse", "minecraft:horse");
        oldToNewNames.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
        oldToNewNames.put("FallingSand", "minecraft:falling_block");
        oldToNewNames.put("Fireball", "minecraft:fireball");
        oldToNewNames.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
        oldToNewNames.put("Ghast", "minecraft:ghast");
        oldToNewNames.put("Giant", "minecraft:giant");
        oldToNewNames.put("Guardian", "minecraft:guardian");
        oldToNewNames.put("Husk", "minecraft:husk");
        oldToNewNames.put("Item", "minecraft:item");
        oldToNewNames.put("ItemFrame", "minecraft:item_frame");
        oldToNewNames.put("LavaSlime", "minecraft:magma_cube");
        oldToNewNames.put("LeashKnot", "minecraft:leash_knot");
        oldToNewNames.put("MinecartChest", "minecraft:chest_minecart");
        oldToNewNames.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
        oldToNewNames.put("MinecartFurnace", "minecraft:furnace_minecart");
        oldToNewNames.put("MinecartHopper", "minecraft:hopper_minecart");
        oldToNewNames.put("MinecartRideable", "minecraft:minecart");
        oldToNewNames.put("MinecartSpawner", "minecraft:spawner_minecart");
        oldToNewNames.put("MinecartTNT", "minecraft:tnt_minecart");
        oldToNewNames.put("Mule", "minecraft:mule");
        oldToNewNames.put("MushroomCow", "minecraft:mooshroom");
        oldToNewNames.put("Ozelot", "minecraft:ocelot");
        oldToNewNames.put("Painting", "minecraft:painting");
        oldToNewNames.put("Pig", "minecraft:pig");
        oldToNewNames.put("PigZombie", "minecraft:zombie_pigman");
        oldToNewNames.put("PolarBear", "minecraft:polar_bear");
        oldToNewNames.put("PrimedTnt", "minecraft:tnt");
        oldToNewNames.put("Rabbit", "minecraft:rabbit");
        oldToNewNames.put("Sheep", "minecraft:sheep");
        oldToNewNames.put("Shulker", "minecraft:shulker");
        oldToNewNames.put("ShulkerBullet", "minecraft:shulker_bullet");
        oldToNewNames.put("Silverfish", "minecraft:silverfish");
        oldToNewNames.put("Skeleton", "minecraft:skeleton");
        oldToNewNames.put("SkeletonHorse", "minecraft:skeleton_horse");
        oldToNewNames.put("Slime", "minecraft:slime");
        oldToNewNames.put("SmallFireball", "minecraft:small_fireball");
        oldToNewNames.put("Snowball", "minecraft:snowball");
        oldToNewNames.put("SnowMan", "minecraft:snowman");
        oldToNewNames.put("SpectralArrow", "minecraft:spectral_arrow");
        oldToNewNames.put("Spider", "minecraft:spider");
        oldToNewNames.put("Squid", "minecraft:squid");
        oldToNewNames.put("Stray", "minecraft:stray");
        oldToNewNames.put("ThrownEgg", "minecraft:egg");
        oldToNewNames.put("ThrownEnderpearl", "minecraft:ender_pearl");
        oldToNewNames.put("ThrownExpBottle", "minecraft:xp_bottle");
        oldToNewNames.put("ThrownPotion", "minecraft:potion");
        oldToNewNames.put("Villager", "minecraft:villager");
        oldToNewNames.put("VillagerGolem", "minecraft:villager_golem");
        oldToNewNames.put("Witch", "minecraft:witch");
        oldToNewNames.put("WitherBoss", "minecraft:wither");
        oldToNewNames.put("WitherSkeleton", "minecraft:wither_skeleton");
        oldToNewNames.put("WitherSkull", "minecraft:wither_skull");
        oldToNewNames.put("Wolf", "minecraft:wolf");
        oldToNewNames.put("XPOrb", "minecraft:xp_orb");
        oldToNewNames.put("Zombie", "minecraft:zombie");
        oldToNewNames.put("ZombieHorse", "minecraft:zombie_horse");
        oldToNewNames.put("ZombieVillager", "minecraft:zombie_villager");
    }
}

