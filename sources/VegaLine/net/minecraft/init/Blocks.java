/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.init;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;

public class Blocks {
    private static final Set<Block> CACHE;
    public static final Block AIR;
    public static final Block STONE;
    public static final BlockGrass GRASS;
    public static final Block DIRT;
    public static final Block COBBLESTONE;
    public static final Block PLANKS;
    public static final Block SAPLING;
    public static final Block BEDROCK;
    public static final BlockDynamicLiquid FLOWING_WATER;
    public static final BlockStaticLiquid WATER;
    public static final BlockDynamicLiquid FLOWING_LAVA;
    public static final BlockStaticLiquid LAVA;
    public static final BlockSand SAND;
    public static final Block GRAVEL;
    public static final Block GOLD_ORE;
    public static final Block IRON_ORE;
    public static final Block COAL_ORE;
    public static final Block LOG;
    public static final Block LOG2;
    public static final BlockLeaves LEAVES;
    public static final BlockLeaves LEAVES2;
    public static final Block SPONGE;
    public static final Block GLASS;
    public static final Block LAPIS_ORE;
    public static final Block LAPIS_BLOCK;
    public static final Block DISPENSER;
    public static final Block SANDSTONE;
    public static final Block NOTEBLOCK;
    public static final Block BED;
    public static final Block GOLDEN_RAIL;
    public static final Block DETECTOR_RAIL;
    public static final BlockPistonBase STICKY_PISTON;
    public static final Block WEB;
    public static final BlockTallGrass TALLGRASS;
    public static final BlockDeadBush DEADBUSH;
    public static final BlockPistonBase PISTON;
    public static final BlockPistonExtension PISTON_HEAD;
    public static final Block WOOL;
    public static final BlockPistonMoving PISTON_EXTENSION;
    public static final BlockFlower YELLOW_FLOWER;
    public static final BlockFlower RED_FLOWER;
    public static final BlockBush BROWN_MUSHROOM;
    public static final BlockBush RED_MUSHROOM;
    public static final Block GOLD_BLOCK;
    public static final Block IRON_BLOCK;
    public static final BlockSlab DOUBLE_STONE_SLAB;
    public static final BlockSlab STONE_SLAB;
    public static final Block BRICK_BLOCK;
    public static final Block TNT;
    public static final Block BOOKSHELF;
    public static final Block MOSSY_COBBLESTONE;
    public static final Block OBSIDIAN;
    public static final Block TORCH;
    public static final BlockFire FIRE;
    public static final Block MOB_SPAWNER;
    public static final Block OAK_STAIRS;
    public static final BlockChest CHEST;
    public static final BlockRedstoneWire REDSTONE_WIRE;
    public static final Block DIAMOND_ORE;
    public static final Block DIAMOND_BLOCK;
    public static final Block CRAFTING_TABLE;
    public static final Block WHEAT;
    public static final Block FARMLAND;
    public static final Block FURNACE;
    public static final Block LIT_FURNACE;
    public static final Block STANDING_SIGN;
    public static final BlockDoor OAK_DOOR;
    public static final BlockDoor SPRUCE_DOOR;
    public static final BlockDoor BIRCH_DOOR;
    public static final BlockDoor JUNGLE_DOOR;
    public static final BlockDoor ACACIA_DOOR;
    public static final BlockDoor DARK_OAK_DOOR;
    public static final Block LADDER;
    public static final Block RAIL;
    public static final Block STONE_STAIRS;
    public static final Block WALL_SIGN;
    public static final Block LEVER;
    public static final Block STONE_PRESSURE_PLATE;
    public static final BlockDoor IRON_DOOR;
    public static final Block WOODEN_PRESSURE_PLATE;
    public static final Block REDSTONE_ORE;
    public static final Block LIT_REDSTONE_ORE;
    public static final Block UNLIT_REDSTONE_TORCH;
    public static final Block REDSTONE_TORCH;
    public static final Block STONE_BUTTON;
    public static final Block SNOW_LAYER;
    public static final Block ICE;
    public static final Block SNOW;
    public static final BlockCactus CACTUS;
    public static final Block CLAY;
    public static final BlockReed REEDS;
    public static final Block JUKEBOX;
    public static final Block OAK_FENCE;
    public static final Block SPRUCE_FENCE;
    public static final Block BIRCH_FENCE;
    public static final Block JUNGLE_FENCE;
    public static final Block DARK_OAK_FENCE;
    public static final Block ACACIA_FENCE;
    public static final Block PUMPKIN;
    public static final Block NETHERRACK;
    public static final Block SOUL_SAND;
    public static final Block GLOWSTONE;
    public static final BlockPortal PORTAL;
    public static final Block LIT_PUMPKIN;
    public static final Block CAKE;
    public static final BlockRedstoneRepeater UNPOWERED_REPEATER;
    public static final BlockRedstoneRepeater POWERED_REPEATER;
    public static final Block TRAPDOOR;
    public static final Block MONSTER_EGG;
    public static final Block STONEBRICK;
    public static final Block BROWN_MUSHROOM_BLOCK;
    public static final Block RED_MUSHROOM_BLOCK;
    public static final Block IRON_BARS;
    public static final Block GLASS_PANE;
    public static final Block MELON_BLOCK;
    public static final Block PUMPKIN_STEM;
    public static final Block MELON_STEM;
    public static final Block VINE;
    public static final Block OAK_FENCE_GATE;
    public static final Block SPRUCE_FENCE_GATE;
    public static final Block BIRCH_FENCE_GATE;
    public static final Block JUNGLE_FENCE_GATE;
    public static final Block DARK_OAK_FENCE_GATE;
    public static final Block ACACIA_FENCE_GATE;
    public static final Block BRICK_STAIRS;
    public static final Block STONE_BRICK_STAIRS;
    public static final BlockMycelium MYCELIUM;
    public static final Block WATERLILY;
    public static final Block NETHER_BRICK;
    public static final Block NETHER_BRICK_FENCE;
    public static final Block NETHER_BRICK_STAIRS;
    public static final Block NETHER_WART;
    public static final Block ENCHANTING_TABLE;
    public static final Block BREWING_STAND;
    public static final BlockCauldron CAULDRON;
    public static final Block END_PORTAL;
    public static final Block END_PORTAL_FRAME;
    public static final Block END_STONE;
    public static final Block DRAGON_EGG;
    public static final Block REDSTONE_LAMP;
    public static final Block LIT_REDSTONE_LAMP;
    public static final BlockSlab DOUBLE_WOODEN_SLAB;
    public static final BlockSlab WOODEN_SLAB;
    public static final Block COCOA;
    public static final Block SANDSTONE_STAIRS;
    public static final Block EMERALD_ORE;
    public static final Block ENDER_CHEST;
    public static final BlockTripWireHook TRIPWIRE_HOOK;
    public static final Block TRIPWIRE;
    public static final Block EMERALD_BLOCK;
    public static final Block SPRUCE_STAIRS;
    public static final Block BIRCH_STAIRS;
    public static final Block JUNGLE_STAIRS;
    public static final Block COMMAND_BLOCK;
    public static final BlockBeacon BEACON;
    public static final Block COBBLESTONE_WALL;
    public static final Block FLOWER_POT;
    public static final Block CARROTS;
    public static final Block POTATOES;
    public static final Block WOODEN_BUTTON;
    public static final BlockSkull SKULL;
    public static final Block ANVIL;
    public static final Block TRAPPED_CHEST;
    public static final Block LIGHT_WEIGHTED_PRESSURE_PLATE;
    public static final Block HEAVY_WEIGHTED_PRESSURE_PLATE;
    public static final BlockRedstoneComparator UNPOWERED_COMPARATOR;
    public static final BlockRedstoneComparator POWERED_COMPARATOR;
    public static final BlockDaylightDetector DAYLIGHT_DETECTOR;
    public static final BlockDaylightDetector DAYLIGHT_DETECTOR_INVERTED;
    public static final Block REDSTONE_BLOCK;
    public static final Block QUARTZ_ORE;
    public static final BlockHopper HOPPER;
    public static final Block QUARTZ_BLOCK;
    public static final Block QUARTZ_STAIRS;
    public static final Block ACTIVATOR_RAIL;
    public static final Block DROPPER;
    public static final Block STAINED_HARDENED_CLAY;
    public static final Block BARRIER;
    public static final Block IRON_TRAPDOOR;
    public static final Block HAY_BLOCK;
    public static final Block CARPET;
    public static final Block HARDENED_CLAY;
    public static final Block COAL_BLOCK;
    public static final Block PACKED_ICE;
    public static final Block ACACIA_STAIRS;
    public static final Block DARK_OAK_STAIRS;
    public static final Block SLIME_BLOCK;
    public static final BlockDoublePlant DOUBLE_PLANT;
    public static final BlockStainedGlass STAINED_GLASS;
    public static final BlockStainedGlassPane STAINED_GLASS_PANE;
    public static final Block PRISMARINE;
    public static final Block SEA_LANTERN;
    public static final Block STANDING_BANNER;
    public static final Block WALL_BANNER;
    public static final Block RED_SANDSTONE;
    public static final Block RED_SANDSTONE_STAIRS;
    public static final BlockSlab DOUBLE_STONE_SLAB2;
    public static final BlockSlab STONE_SLAB2;
    public static final Block END_ROD;
    public static final Block CHORUS_PLANT;
    public static final Block CHORUS_FLOWER;
    public static final Block PURPUR_BLOCK;
    public static final Block PURPUR_PILLAR;
    public static final Block PURPUR_STAIRS;
    public static final BlockSlab PURPUR_DOUBLE_SLAB;
    public static final BlockSlab PURPUR_SLAB;
    public static final Block END_BRICKS;
    public static final Block BEETROOTS;
    public static final Block GRASS_PATH;
    public static final Block END_GATEWAY;
    public static final Block REPEATING_COMMAND_BLOCK;
    public static final Block CHAIN_COMMAND_BLOCK;
    public static final Block FROSTED_ICE;
    public static final Block MAGMA;
    public static final Block NETHER_WART_BLOCK;
    public static final Block RED_NETHER_BRICK;
    public static final Block BONE_BLOCK;
    public static final Block STRUCTURE_VOID;
    public static final Block field_190976_dk;
    public static final Block field_190977_dl;
    public static final Block field_190978_dm;
    public static final Block field_190979_dn;
    public static final Block field_190980_do;
    public static final Block field_190981_dp;
    public static final Block field_190982_dq;
    public static final Block field_190983_dr;
    public static final Block field_190984_ds;
    public static final Block field_190985_dt;
    public static final Block field_190986_du;
    public static final Block field_190987_dv;
    public static final Block field_190988_dw;
    public static final Block field_190989_dx;
    public static final Block field_190990_dy;
    public static final Block field_190991_dz;
    public static final Block field_190975_dA;
    public static final Block field_192427_dB;
    public static final Block field_192428_dC;
    public static final Block field_192429_dD;
    public static final Block field_192430_dE;
    public static final Block field_192431_dF;
    public static final Block field_192432_dG;
    public static final Block field_192433_dH;
    public static final Block field_192434_dI;
    public static final Block field_192435_dJ;
    public static final Block field_192436_dK;
    public static final Block field_192437_dL;
    public static final Block field_192438_dM;
    public static final Block field_192439_dN;
    public static final Block field_192440_dO;
    public static final Block field_192441_dP;
    public static final Block field_192442_dQ;
    public static final Block field_192443_dR;
    public static final Block field_192444_dS;
    public static final Block STRUCTURE_BLOCK;

    @Nullable
    private static Block getRegisteredBlock(String blockName) {
        Block block = Block.REGISTRY.getObject(new ResourceLocation(blockName));
        if (!CACHE.add(block)) {
            throw new IllegalStateException("Invalid Block requested: " + blockName);
        }
        return block;
    }

    static {
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed Blocks before Bootstrap!");
        }
        CACHE = Sets.newHashSet();
        AIR = Blocks.getRegisteredBlock("air");
        STONE = Blocks.getRegisteredBlock("stone");
        GRASS = (BlockGrass)Blocks.getRegisteredBlock("grass");
        DIRT = Blocks.getRegisteredBlock("dirt");
        COBBLESTONE = Blocks.getRegisteredBlock("cobblestone");
        PLANKS = Blocks.getRegisteredBlock("planks");
        SAPLING = Blocks.getRegisteredBlock("sapling");
        BEDROCK = Blocks.getRegisteredBlock("bedrock");
        FLOWING_WATER = (BlockDynamicLiquid)Blocks.getRegisteredBlock("flowing_water");
        WATER = (BlockStaticLiquid)Blocks.getRegisteredBlock("water");
        FLOWING_LAVA = (BlockDynamicLiquid)Blocks.getRegisteredBlock("flowing_lava");
        LAVA = (BlockStaticLiquid)Blocks.getRegisteredBlock("lava");
        SAND = (BlockSand)Blocks.getRegisteredBlock("sand");
        GRAVEL = Blocks.getRegisteredBlock("gravel");
        GOLD_ORE = Blocks.getRegisteredBlock("gold_ore");
        IRON_ORE = Blocks.getRegisteredBlock("iron_ore");
        COAL_ORE = Blocks.getRegisteredBlock("coal_ore");
        LOG = Blocks.getRegisteredBlock("log");
        LOG2 = Blocks.getRegisteredBlock("log2");
        LEAVES = (BlockLeaves)Blocks.getRegisteredBlock("leaves");
        LEAVES2 = (BlockLeaves)Blocks.getRegisteredBlock("leaves2");
        SPONGE = Blocks.getRegisteredBlock("sponge");
        GLASS = Blocks.getRegisteredBlock("glass");
        LAPIS_ORE = Blocks.getRegisteredBlock("lapis_ore");
        LAPIS_BLOCK = Blocks.getRegisteredBlock("lapis_block");
        DISPENSER = Blocks.getRegisteredBlock("dispenser");
        SANDSTONE = Blocks.getRegisteredBlock("sandstone");
        NOTEBLOCK = Blocks.getRegisteredBlock("noteblock");
        BED = Blocks.getRegisteredBlock("bed");
        GOLDEN_RAIL = Blocks.getRegisteredBlock("golden_rail");
        DETECTOR_RAIL = Blocks.getRegisteredBlock("detector_rail");
        STICKY_PISTON = (BlockPistonBase)Blocks.getRegisteredBlock("sticky_piston");
        WEB = Blocks.getRegisteredBlock("web");
        TALLGRASS = (BlockTallGrass)Blocks.getRegisteredBlock("tallgrass");
        DEADBUSH = (BlockDeadBush)Blocks.getRegisteredBlock("deadbush");
        PISTON = (BlockPistonBase)Blocks.getRegisteredBlock("piston");
        PISTON_HEAD = (BlockPistonExtension)Blocks.getRegisteredBlock("piston_head");
        WOOL = Blocks.getRegisteredBlock("wool");
        PISTON_EXTENSION = (BlockPistonMoving)Blocks.getRegisteredBlock("piston_extension");
        YELLOW_FLOWER = (BlockFlower)Blocks.getRegisteredBlock("yellow_flower");
        RED_FLOWER = (BlockFlower)Blocks.getRegisteredBlock("red_flower");
        BROWN_MUSHROOM = (BlockBush)Blocks.getRegisteredBlock("brown_mushroom");
        RED_MUSHROOM = (BlockBush)Blocks.getRegisteredBlock("red_mushroom");
        GOLD_BLOCK = Blocks.getRegisteredBlock("gold_block");
        IRON_BLOCK = Blocks.getRegisteredBlock("iron_block");
        DOUBLE_STONE_SLAB = (BlockSlab)Blocks.getRegisteredBlock("double_stone_slab");
        STONE_SLAB = (BlockSlab)Blocks.getRegisteredBlock("stone_slab");
        BRICK_BLOCK = Blocks.getRegisteredBlock("brick_block");
        TNT = Blocks.getRegisteredBlock("tnt");
        BOOKSHELF = Blocks.getRegisteredBlock("bookshelf");
        MOSSY_COBBLESTONE = Blocks.getRegisteredBlock("mossy_cobblestone");
        OBSIDIAN = Blocks.getRegisteredBlock("obsidian");
        TORCH = Blocks.getRegisteredBlock("torch");
        FIRE = (BlockFire)Blocks.getRegisteredBlock("fire");
        MOB_SPAWNER = Blocks.getRegisteredBlock("mob_spawner");
        OAK_STAIRS = Blocks.getRegisteredBlock("oak_stairs");
        CHEST = (BlockChest)Blocks.getRegisteredBlock("chest");
        REDSTONE_WIRE = (BlockRedstoneWire)Blocks.getRegisteredBlock("redstone_wire");
        DIAMOND_ORE = Blocks.getRegisteredBlock("diamond_ore");
        DIAMOND_BLOCK = Blocks.getRegisteredBlock("diamond_block");
        CRAFTING_TABLE = Blocks.getRegisteredBlock("crafting_table");
        WHEAT = Blocks.getRegisteredBlock("wheat");
        FARMLAND = Blocks.getRegisteredBlock("farmland");
        FURNACE = Blocks.getRegisteredBlock("furnace");
        LIT_FURNACE = Blocks.getRegisteredBlock("lit_furnace");
        STANDING_SIGN = Blocks.getRegisteredBlock("standing_sign");
        OAK_DOOR = (BlockDoor)Blocks.getRegisteredBlock("wooden_door");
        SPRUCE_DOOR = (BlockDoor)Blocks.getRegisteredBlock("spruce_door");
        BIRCH_DOOR = (BlockDoor)Blocks.getRegisteredBlock("birch_door");
        JUNGLE_DOOR = (BlockDoor)Blocks.getRegisteredBlock("jungle_door");
        ACACIA_DOOR = (BlockDoor)Blocks.getRegisteredBlock("acacia_door");
        DARK_OAK_DOOR = (BlockDoor)Blocks.getRegisteredBlock("dark_oak_door");
        LADDER = Blocks.getRegisteredBlock("ladder");
        RAIL = Blocks.getRegisteredBlock("rail");
        STONE_STAIRS = Blocks.getRegisteredBlock("stone_stairs");
        WALL_SIGN = Blocks.getRegisteredBlock("wall_sign");
        LEVER = Blocks.getRegisteredBlock("lever");
        STONE_PRESSURE_PLATE = Blocks.getRegisteredBlock("stone_pressure_plate");
        IRON_DOOR = (BlockDoor)Blocks.getRegisteredBlock("iron_door");
        WOODEN_PRESSURE_PLATE = Blocks.getRegisteredBlock("wooden_pressure_plate");
        REDSTONE_ORE = Blocks.getRegisteredBlock("redstone_ore");
        LIT_REDSTONE_ORE = Blocks.getRegisteredBlock("lit_redstone_ore");
        UNLIT_REDSTONE_TORCH = Blocks.getRegisteredBlock("unlit_redstone_torch");
        REDSTONE_TORCH = Blocks.getRegisteredBlock("redstone_torch");
        STONE_BUTTON = Blocks.getRegisteredBlock("stone_button");
        SNOW_LAYER = Blocks.getRegisteredBlock("snow_layer");
        ICE = Blocks.getRegisteredBlock("ice");
        SNOW = Blocks.getRegisteredBlock("snow");
        CACTUS = (BlockCactus)Blocks.getRegisteredBlock("cactus");
        CLAY = Blocks.getRegisteredBlock("clay");
        REEDS = (BlockReed)Blocks.getRegisteredBlock("reeds");
        JUKEBOX = Blocks.getRegisteredBlock("jukebox");
        OAK_FENCE = Blocks.getRegisteredBlock("fence");
        SPRUCE_FENCE = Blocks.getRegisteredBlock("spruce_fence");
        BIRCH_FENCE = Blocks.getRegisteredBlock("birch_fence");
        JUNGLE_FENCE = Blocks.getRegisteredBlock("jungle_fence");
        DARK_OAK_FENCE = Blocks.getRegisteredBlock("dark_oak_fence");
        ACACIA_FENCE = Blocks.getRegisteredBlock("acacia_fence");
        PUMPKIN = Blocks.getRegisteredBlock("pumpkin");
        NETHERRACK = Blocks.getRegisteredBlock("netherrack");
        SOUL_SAND = Blocks.getRegisteredBlock("soul_sand");
        GLOWSTONE = Blocks.getRegisteredBlock("glowstone");
        PORTAL = (BlockPortal)Blocks.getRegisteredBlock("portal");
        LIT_PUMPKIN = Blocks.getRegisteredBlock("lit_pumpkin");
        CAKE = Blocks.getRegisteredBlock("cake");
        UNPOWERED_REPEATER = (BlockRedstoneRepeater)Blocks.getRegisteredBlock("unpowered_repeater");
        POWERED_REPEATER = (BlockRedstoneRepeater)Blocks.getRegisteredBlock("powered_repeater");
        TRAPDOOR = Blocks.getRegisteredBlock("trapdoor");
        MONSTER_EGG = Blocks.getRegisteredBlock("monster_egg");
        STONEBRICK = Blocks.getRegisteredBlock("stonebrick");
        BROWN_MUSHROOM_BLOCK = Blocks.getRegisteredBlock("brown_mushroom_block");
        RED_MUSHROOM_BLOCK = Blocks.getRegisteredBlock("red_mushroom_block");
        IRON_BARS = Blocks.getRegisteredBlock("iron_bars");
        GLASS_PANE = Blocks.getRegisteredBlock("glass_pane");
        MELON_BLOCK = Blocks.getRegisteredBlock("melon_block");
        PUMPKIN_STEM = Blocks.getRegisteredBlock("pumpkin_stem");
        MELON_STEM = Blocks.getRegisteredBlock("melon_stem");
        VINE = Blocks.getRegisteredBlock("vine");
        OAK_FENCE_GATE = Blocks.getRegisteredBlock("fence_gate");
        SPRUCE_FENCE_GATE = Blocks.getRegisteredBlock("spruce_fence_gate");
        BIRCH_FENCE_GATE = Blocks.getRegisteredBlock("birch_fence_gate");
        JUNGLE_FENCE_GATE = Blocks.getRegisteredBlock("jungle_fence_gate");
        DARK_OAK_FENCE_GATE = Blocks.getRegisteredBlock("dark_oak_fence_gate");
        ACACIA_FENCE_GATE = Blocks.getRegisteredBlock("acacia_fence_gate");
        BRICK_STAIRS = Blocks.getRegisteredBlock("brick_stairs");
        STONE_BRICK_STAIRS = Blocks.getRegisteredBlock("stone_brick_stairs");
        MYCELIUM = (BlockMycelium)Blocks.getRegisteredBlock("mycelium");
        WATERLILY = Blocks.getRegisteredBlock("waterlily");
        NETHER_BRICK = Blocks.getRegisteredBlock("nether_brick");
        NETHER_BRICK_FENCE = Blocks.getRegisteredBlock("nether_brick_fence");
        NETHER_BRICK_STAIRS = Blocks.getRegisteredBlock("nether_brick_stairs");
        NETHER_WART = Blocks.getRegisteredBlock("nether_wart");
        ENCHANTING_TABLE = Blocks.getRegisteredBlock("enchanting_table");
        BREWING_STAND = Blocks.getRegisteredBlock("brewing_stand");
        CAULDRON = (BlockCauldron)Blocks.getRegisteredBlock("cauldron");
        END_PORTAL = Blocks.getRegisteredBlock("end_portal");
        END_PORTAL_FRAME = Blocks.getRegisteredBlock("end_portal_frame");
        END_STONE = Blocks.getRegisteredBlock("end_stone");
        DRAGON_EGG = Blocks.getRegisteredBlock("dragon_egg");
        REDSTONE_LAMP = Blocks.getRegisteredBlock("redstone_lamp");
        LIT_REDSTONE_LAMP = Blocks.getRegisteredBlock("lit_redstone_lamp");
        DOUBLE_WOODEN_SLAB = (BlockSlab)Blocks.getRegisteredBlock("double_wooden_slab");
        WOODEN_SLAB = (BlockSlab)Blocks.getRegisteredBlock("wooden_slab");
        COCOA = Blocks.getRegisteredBlock("cocoa");
        SANDSTONE_STAIRS = Blocks.getRegisteredBlock("sandstone_stairs");
        EMERALD_ORE = Blocks.getRegisteredBlock("emerald_ore");
        ENDER_CHEST = Blocks.getRegisteredBlock("ender_chest");
        TRIPWIRE_HOOK = (BlockTripWireHook)Blocks.getRegisteredBlock("tripwire_hook");
        TRIPWIRE = Blocks.getRegisteredBlock("tripwire");
        EMERALD_BLOCK = Blocks.getRegisteredBlock("emerald_block");
        SPRUCE_STAIRS = Blocks.getRegisteredBlock("spruce_stairs");
        BIRCH_STAIRS = Blocks.getRegisteredBlock("birch_stairs");
        JUNGLE_STAIRS = Blocks.getRegisteredBlock("jungle_stairs");
        COMMAND_BLOCK = Blocks.getRegisteredBlock("command_block");
        BEACON = (BlockBeacon)Blocks.getRegisteredBlock("beacon");
        COBBLESTONE_WALL = Blocks.getRegisteredBlock("cobblestone_wall");
        FLOWER_POT = Blocks.getRegisteredBlock("flower_pot");
        CARROTS = Blocks.getRegisteredBlock("carrots");
        POTATOES = Blocks.getRegisteredBlock("potatoes");
        WOODEN_BUTTON = Blocks.getRegisteredBlock("wooden_button");
        SKULL = (BlockSkull)Blocks.getRegisteredBlock("skull");
        ANVIL = Blocks.getRegisteredBlock("anvil");
        TRAPPED_CHEST = Blocks.getRegisteredBlock("trapped_chest");
        LIGHT_WEIGHTED_PRESSURE_PLATE = Blocks.getRegisteredBlock("light_weighted_pressure_plate");
        HEAVY_WEIGHTED_PRESSURE_PLATE = Blocks.getRegisteredBlock("heavy_weighted_pressure_plate");
        UNPOWERED_COMPARATOR = (BlockRedstoneComparator)Blocks.getRegisteredBlock("unpowered_comparator");
        POWERED_COMPARATOR = (BlockRedstoneComparator)Blocks.getRegisteredBlock("powered_comparator");
        DAYLIGHT_DETECTOR = (BlockDaylightDetector)Blocks.getRegisteredBlock("daylight_detector");
        DAYLIGHT_DETECTOR_INVERTED = (BlockDaylightDetector)Blocks.getRegisteredBlock("daylight_detector_inverted");
        REDSTONE_BLOCK = Blocks.getRegisteredBlock("redstone_block");
        QUARTZ_ORE = Blocks.getRegisteredBlock("quartz_ore");
        HOPPER = (BlockHopper)Blocks.getRegisteredBlock("hopper");
        QUARTZ_BLOCK = Blocks.getRegisteredBlock("quartz_block");
        QUARTZ_STAIRS = Blocks.getRegisteredBlock("quartz_stairs");
        ACTIVATOR_RAIL = Blocks.getRegisteredBlock("activator_rail");
        DROPPER = Blocks.getRegisteredBlock("dropper");
        STAINED_HARDENED_CLAY = Blocks.getRegisteredBlock("stained_hardened_clay");
        BARRIER = Blocks.getRegisteredBlock("barrier");
        IRON_TRAPDOOR = Blocks.getRegisteredBlock("iron_trapdoor");
        HAY_BLOCK = Blocks.getRegisteredBlock("hay_block");
        CARPET = Blocks.getRegisteredBlock("carpet");
        HARDENED_CLAY = Blocks.getRegisteredBlock("hardened_clay");
        COAL_BLOCK = Blocks.getRegisteredBlock("coal_block");
        PACKED_ICE = Blocks.getRegisteredBlock("packed_ice");
        ACACIA_STAIRS = Blocks.getRegisteredBlock("acacia_stairs");
        DARK_OAK_STAIRS = Blocks.getRegisteredBlock("dark_oak_stairs");
        SLIME_BLOCK = Blocks.getRegisteredBlock("slime");
        DOUBLE_PLANT = (BlockDoublePlant)Blocks.getRegisteredBlock("double_plant");
        STAINED_GLASS = (BlockStainedGlass)Blocks.getRegisteredBlock("stained_glass");
        STAINED_GLASS_PANE = (BlockStainedGlassPane)Blocks.getRegisteredBlock("stained_glass_pane");
        PRISMARINE = Blocks.getRegisteredBlock("prismarine");
        SEA_LANTERN = Blocks.getRegisteredBlock("sea_lantern");
        STANDING_BANNER = Blocks.getRegisteredBlock("standing_banner");
        WALL_BANNER = Blocks.getRegisteredBlock("wall_banner");
        RED_SANDSTONE = Blocks.getRegisteredBlock("red_sandstone");
        RED_SANDSTONE_STAIRS = Blocks.getRegisteredBlock("red_sandstone_stairs");
        DOUBLE_STONE_SLAB2 = (BlockSlab)Blocks.getRegisteredBlock("double_stone_slab2");
        STONE_SLAB2 = (BlockSlab)Blocks.getRegisteredBlock("stone_slab2");
        END_ROD = Blocks.getRegisteredBlock("end_rod");
        CHORUS_PLANT = Blocks.getRegisteredBlock("chorus_plant");
        CHORUS_FLOWER = Blocks.getRegisteredBlock("chorus_flower");
        PURPUR_BLOCK = Blocks.getRegisteredBlock("purpur_block");
        PURPUR_PILLAR = Blocks.getRegisteredBlock("purpur_pillar");
        PURPUR_STAIRS = Blocks.getRegisteredBlock("purpur_stairs");
        PURPUR_DOUBLE_SLAB = (BlockSlab)Blocks.getRegisteredBlock("purpur_double_slab");
        PURPUR_SLAB = (BlockSlab)Blocks.getRegisteredBlock("purpur_slab");
        END_BRICKS = Blocks.getRegisteredBlock("end_bricks");
        BEETROOTS = Blocks.getRegisteredBlock("beetroots");
        GRASS_PATH = Blocks.getRegisteredBlock("grass_path");
        END_GATEWAY = Blocks.getRegisteredBlock("end_gateway");
        REPEATING_COMMAND_BLOCK = Blocks.getRegisteredBlock("repeating_command_block");
        CHAIN_COMMAND_BLOCK = Blocks.getRegisteredBlock("chain_command_block");
        FROSTED_ICE = Blocks.getRegisteredBlock("frosted_ice");
        MAGMA = Blocks.getRegisteredBlock("magma");
        NETHER_WART_BLOCK = Blocks.getRegisteredBlock("nether_wart_block");
        RED_NETHER_BRICK = Blocks.getRegisteredBlock("red_nether_brick");
        BONE_BLOCK = Blocks.getRegisteredBlock("bone_block");
        STRUCTURE_VOID = Blocks.getRegisteredBlock("structure_void");
        field_190976_dk = Blocks.getRegisteredBlock("observer");
        field_190977_dl = Blocks.getRegisteredBlock("white_shulker_box");
        field_190978_dm = Blocks.getRegisteredBlock("orange_shulker_box");
        field_190979_dn = Blocks.getRegisteredBlock("magenta_shulker_box");
        field_190980_do = Blocks.getRegisteredBlock("light_blue_shulker_box");
        field_190981_dp = Blocks.getRegisteredBlock("yellow_shulker_box");
        field_190982_dq = Blocks.getRegisteredBlock("lime_shulker_box");
        field_190983_dr = Blocks.getRegisteredBlock("pink_shulker_box");
        field_190984_ds = Blocks.getRegisteredBlock("gray_shulker_box");
        field_190985_dt = Blocks.getRegisteredBlock("silver_shulker_box");
        field_190986_du = Blocks.getRegisteredBlock("cyan_shulker_box");
        field_190987_dv = Blocks.getRegisteredBlock("purple_shulker_box");
        field_190988_dw = Blocks.getRegisteredBlock("blue_shulker_box");
        field_190989_dx = Blocks.getRegisteredBlock("brown_shulker_box");
        field_190990_dy = Blocks.getRegisteredBlock("green_shulker_box");
        field_190991_dz = Blocks.getRegisteredBlock("red_shulker_box");
        field_190975_dA = Blocks.getRegisteredBlock("black_shulker_box");
        field_192427_dB = Blocks.getRegisteredBlock("white_glazed_terracotta");
        field_192428_dC = Blocks.getRegisteredBlock("orange_glazed_terracotta");
        field_192429_dD = Blocks.getRegisteredBlock("magenta_glazed_terracotta");
        field_192430_dE = Blocks.getRegisteredBlock("light_blue_glazed_terracotta");
        field_192431_dF = Blocks.getRegisteredBlock("yellow_glazed_terracotta");
        field_192432_dG = Blocks.getRegisteredBlock("lime_glazed_terracotta");
        field_192433_dH = Blocks.getRegisteredBlock("pink_glazed_terracotta");
        field_192434_dI = Blocks.getRegisteredBlock("gray_glazed_terracotta");
        field_192435_dJ = Blocks.getRegisteredBlock("silver_glazed_terracotta");
        field_192436_dK = Blocks.getRegisteredBlock("cyan_glazed_terracotta");
        field_192437_dL = Blocks.getRegisteredBlock("purple_glazed_terracotta");
        field_192438_dM = Blocks.getRegisteredBlock("blue_glazed_terracotta");
        field_192439_dN = Blocks.getRegisteredBlock("brown_glazed_terracotta");
        field_192440_dO = Blocks.getRegisteredBlock("green_glazed_terracotta");
        field_192441_dP = Blocks.getRegisteredBlock("red_glazed_terracotta");
        field_192442_dQ = Blocks.getRegisteredBlock("black_glazed_terracotta");
        field_192443_dR = Blocks.getRegisteredBlock("concrete");
        field_192444_dS = Blocks.getRegisteredBlock("concrete_powder");
        STRUCTURE_BLOCK = Blocks.getRegisteredBlock("structure_block");
        CACHE.clear();
    }
}

