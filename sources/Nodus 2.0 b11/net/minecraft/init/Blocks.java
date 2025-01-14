/*   1:    */ package net.minecraft.init;
/*   2:    */ 
/*   3:    */ import net.minecraft.block.Block;
/*   4:    */ import net.minecraft.block.BlockBeacon;
/*   5:    */ import net.minecraft.block.BlockBush;
/*   6:    */ import net.minecraft.block.BlockCauldron;
/*   7:    */ import net.minecraft.block.BlockChest;
/*   8:    */ import net.minecraft.block.BlockDaylightDetector;
/*   9:    */ import net.minecraft.block.BlockDeadBush;
/*  10:    */ import net.minecraft.block.BlockDoublePlant;
/*  11:    */ import net.minecraft.block.BlockFire;
/*  12:    */ import net.minecraft.block.BlockFlower;
/*  13:    */ import net.minecraft.block.BlockGrass;
/*  14:    */ import net.minecraft.block.BlockHopper;
/*  15:    */ import net.minecraft.block.BlockLeaves;
/*  16:    */ import net.minecraft.block.BlockLiquid;
/*  17:    */ import net.minecraft.block.BlockMycelium;
/*  18:    */ import net.minecraft.block.BlockPistonBase;
/*  19:    */ import net.minecraft.block.BlockPistonExtension;
/*  20:    */ import net.minecraft.block.BlockPistonMoving;
/*  21:    */ import net.minecraft.block.BlockPortal;
/*  22:    */ import net.minecraft.block.BlockRedstoneComparator;
/*  23:    */ import net.minecraft.block.BlockRedstoneRepeater;
/*  24:    */ import net.minecraft.block.BlockRedstoneWire;
/*  25:    */ import net.minecraft.block.BlockSlab;
/*  26:    */ import net.minecraft.block.BlockStainedGlass;
/*  27:    */ import net.minecraft.block.BlockStainedGlassPane;
/*  28:    */ import net.minecraft.block.BlockTallGrass;
/*  29:    */ import net.minecraft.block.BlockTripWireHook;
/*  30:    */ import net.minecraft.util.RegistryNamespaced;
/*  31:    */ 
/*  32:    */ public class Blocks
/*  33:    */ {
/*  34: 33 */   public static final Block air = (Block)Block.blockRegistry.getObject("air");
/*  35: 34 */   public static final Block stone = (Block)Block.blockRegistry.getObject("stone");
/*  36: 35 */   public static final BlockGrass grass = (BlockGrass)Block.blockRegistry.getObject("grass");
/*  37: 36 */   public static final Block dirt = (Block)Block.blockRegistry.getObject("dirt");
/*  38: 37 */   public static final Block cobblestone = (Block)Block.blockRegistry.getObject("cobblestone");
/*  39: 38 */   public static final Block planks = (Block)Block.blockRegistry.getObject("planks");
/*  40: 39 */   public static final Block sapling = (Block)Block.blockRegistry.getObject("sapling");
/*  41: 40 */   public static final Block bedrock = (Block)Block.blockRegistry.getObject("bedrock");
/*  42: 41 */   public static final BlockLiquid flowing_water = (BlockLiquid)Block.blockRegistry.getObject("flowing_water");
/*  43: 42 */   public static final Block water = (Block)Block.blockRegistry.getObject("water");
/*  44: 43 */   public static final BlockLiquid flowing_lava = (BlockLiquid)Block.blockRegistry.getObject("flowing_lava");
/*  45: 44 */   public static final Block lava = (Block)Block.blockRegistry.getObject("lava");
/*  46: 45 */   public static final Block sand = (Block)Block.blockRegistry.getObject("sand");
/*  47: 46 */   public static final Block gravel = (Block)Block.blockRegistry.getObject("gravel");
/*  48: 47 */   public static final Block gold_ore = (Block)Block.blockRegistry.getObject("gold_ore");
/*  49: 48 */   public static final Block iron_ore = (Block)Block.blockRegistry.getObject("iron_ore");
/*  50: 49 */   public static final Block coal_ore = (Block)Block.blockRegistry.getObject("coal_ore");
/*  51: 50 */   public static final Block log = (Block)Block.blockRegistry.getObject("log");
/*  52: 51 */   public static final Block log2 = (Block)Block.blockRegistry.getObject("log2");
/*  53: 52 */   public static final BlockLeaves leaves = (BlockLeaves)Block.blockRegistry.getObject("leaves");
/*  54: 53 */   public static final BlockLeaves leaves2 = (BlockLeaves)Block.blockRegistry.getObject("leaves2");
/*  55: 54 */   public static final Block sponge = (Block)Block.blockRegistry.getObject("sponge");
/*  56: 55 */   public static final Block glass = (Block)Block.blockRegistry.getObject("glass");
/*  57: 56 */   public static final Block lapis_ore = (Block)Block.blockRegistry.getObject("lapis_ore");
/*  58: 57 */   public static final Block lapis_block = (Block)Block.blockRegistry.getObject("lapis_block");
/*  59: 58 */   public static final Block dispenser = (Block)Block.blockRegistry.getObject("dispenser");
/*  60: 59 */   public static final Block sandstone = (Block)Block.blockRegistry.getObject("sandstone");
/*  61: 60 */   public static final Block noteblock = (Block)Block.blockRegistry.getObject("noteblock");
/*  62: 61 */   public static final Block bed = (Block)Block.blockRegistry.getObject("bed");
/*  63: 62 */   public static final Block golden_rail = (Block)Block.blockRegistry.getObject("golden_rail");
/*  64: 63 */   public static final Block detector_rail = (Block)Block.blockRegistry.getObject("detector_rail");
/*  65: 64 */   public static final BlockPistonBase sticky_piston = (BlockPistonBase)Block.blockRegistry.getObject("sticky_piston");
/*  66: 65 */   public static final Block web = (Block)Block.blockRegistry.getObject("web");
/*  67: 66 */   public static final BlockTallGrass tallgrass = (BlockTallGrass)Block.blockRegistry.getObject("tallgrass");
/*  68: 67 */   public static final BlockDeadBush deadbush = (BlockDeadBush)Block.blockRegistry.getObject("deadbush");
/*  69: 68 */   public static final BlockPistonBase piston = (BlockPistonBase)Block.blockRegistry.getObject("piston");
/*  70: 69 */   public static final BlockPistonExtension piston_head = (BlockPistonExtension)Block.blockRegistry.getObject("piston_head");
/*  71: 70 */   public static final Block wool = (Block)Block.blockRegistry.getObject("wool");
/*  72: 71 */   public static final BlockPistonMoving piston_extension = (BlockPistonMoving)Block.blockRegistry.getObject("piston_extension");
/*  73: 72 */   public static final BlockFlower yellow_flower = (BlockFlower)Block.blockRegistry.getObject("yellow_flower");
/*  74: 73 */   public static final BlockFlower red_flower = (BlockFlower)Block.blockRegistry.getObject("red_flower");
/*  75: 74 */   public static final BlockBush brown_mushroom = (BlockBush)Block.blockRegistry.getObject("brown_mushroom");
/*  76: 75 */   public static final BlockBush red_mushroom = (BlockBush)Block.blockRegistry.getObject("red_mushroom");
/*  77: 76 */   public static final Block gold_block = (Block)Block.blockRegistry.getObject("gold_block");
/*  78: 77 */   public static final Block iron_block = (Block)Block.blockRegistry.getObject("iron_block");
/*  79: 78 */   public static final BlockSlab double_stone_slab = (BlockSlab)Block.blockRegistry.getObject("double_stone_slab");
/*  80: 79 */   public static final BlockSlab stone_slab = (BlockSlab)Block.blockRegistry.getObject("stone_slab");
/*  81: 80 */   public static final Block brick_block = (Block)Block.blockRegistry.getObject("brick_block");
/*  82: 81 */   public static final Block tnt = (Block)Block.blockRegistry.getObject("tnt");
/*  83: 82 */   public static final Block bookshelf = (Block)Block.blockRegistry.getObject("bookshelf");
/*  84: 83 */   public static final Block mossy_cobblestone = (Block)Block.blockRegistry.getObject("mossy_cobblestone");
/*  85: 84 */   public static final Block obsidian = (Block)Block.blockRegistry.getObject("obsidian");
/*  86: 85 */   public static final Block torch = (Block)Block.blockRegistry.getObject("torch");
/*  87: 86 */   public static final BlockFire fire = (BlockFire)Block.blockRegistry.getObject("fire");
/*  88: 87 */   public static final Block mob_spawner = (Block)Block.blockRegistry.getObject("mob_spawner");
/*  89: 88 */   public static final Block oak_stairs = (Block)Block.blockRegistry.getObject("oak_stairs");
/*  90: 89 */   public static final BlockChest chest = (BlockChest)Block.blockRegistry.getObject("chest");
/*  91: 90 */   public static final BlockRedstoneWire redstone_wire = (BlockRedstoneWire)Block.blockRegistry.getObject("redstone_wire");
/*  92: 91 */   public static final Block diamond_ore = (Block)Block.blockRegistry.getObject("diamond_ore");
/*  93: 92 */   public static final Block diamond_block = (Block)Block.blockRegistry.getObject("diamond_block");
/*  94: 93 */   public static final Block crafting_table = (Block)Block.blockRegistry.getObject("crafting_table");
/*  95: 94 */   public static final Block wheat = (Block)Block.blockRegistry.getObject("wheat");
/*  96: 95 */   public static final Block farmland = (Block)Block.blockRegistry.getObject("farmland");
/*  97: 96 */   public static final Block furnace = (Block)Block.blockRegistry.getObject("furnace");
/*  98: 97 */   public static final Block lit_furnace = (Block)Block.blockRegistry.getObject("lit_furnace");
/*  99: 98 */   public static final Block standing_sign = (Block)Block.blockRegistry.getObject("standing_sign");
/* 100: 99 */   public static final Block wooden_door = (Block)Block.blockRegistry.getObject("wooden_door");
/* 101:100 */   public static final Block ladder = (Block)Block.blockRegistry.getObject("ladder");
/* 102:101 */   public static final Block rail = (Block)Block.blockRegistry.getObject("rail");
/* 103:102 */   public static final Block stone_stairs = (Block)Block.blockRegistry.getObject("stone_stairs");
/* 104:103 */   public static final Block wall_sign = (Block)Block.blockRegistry.getObject("wall_sign");
/* 105:104 */   public static final Block lever = (Block)Block.blockRegistry.getObject("lever");
/* 106:105 */   public static final Block stone_pressure_plate = (Block)Block.blockRegistry.getObject("stone_pressure_plate");
/* 107:106 */   public static final Block iron_door = (Block)Block.blockRegistry.getObject("iron_door");
/* 108:107 */   public static final Block wooden_pressure_plate = (Block)Block.blockRegistry.getObject("wooden_pressure_plate");
/* 109:108 */   public static final Block redstone_ore = (Block)Block.blockRegistry.getObject("redstone_ore");
/* 110:109 */   public static final Block lit_redstone_ore = (Block)Block.blockRegistry.getObject("lit_redstone_ore");
/* 111:110 */   public static final Block unlit_redstone_torch = (Block)Block.blockRegistry.getObject("unlit_redstone_torch");
/* 112:111 */   public static final Block redstone_torch = (Block)Block.blockRegistry.getObject("redstone_torch");
/* 113:112 */   public static final Block stone_button = (Block)Block.blockRegistry.getObject("stone_button");
/* 114:113 */   public static final Block snow_layer = (Block)Block.blockRegistry.getObject("snow_layer");
/* 115:114 */   public static final Block ice = (Block)Block.blockRegistry.getObject("ice");
/* 116:115 */   public static final Block snow = (Block)Block.blockRegistry.getObject("snow");
/* 117:116 */   public static final Block cactus = (Block)Block.blockRegistry.getObject("cactus");
/* 118:117 */   public static final Block clay = (Block)Block.blockRegistry.getObject("clay");
/* 119:118 */   public static final Block reeds = (Block)Block.blockRegistry.getObject("reeds");
/* 120:119 */   public static final Block jukebox = (Block)Block.blockRegistry.getObject("jukebox");
/* 121:120 */   public static final Block fence = (Block)Block.blockRegistry.getObject("fence");
/* 122:121 */   public static final Block pumpkin = (Block)Block.blockRegistry.getObject("pumpkin");
/* 123:122 */   public static final Block netherrack = (Block)Block.blockRegistry.getObject("netherrack");
/* 124:123 */   public static final Block soul_sand = (Block)Block.blockRegistry.getObject("soul_sand");
/* 125:124 */   public static final Block glowstone = (Block)Block.blockRegistry.getObject("glowstone");
/* 126:125 */   public static final BlockPortal portal = (BlockPortal)Block.blockRegistry.getObject("portal");
/* 127:126 */   public static final Block lit_pumpkin = (Block)Block.blockRegistry.getObject("lit_pumpkin");
/* 128:127 */   public static final Block cake = (Block)Block.blockRegistry.getObject("cake");
/* 129:128 */   public static final BlockRedstoneRepeater unpowered_repeater = (BlockRedstoneRepeater)Block.blockRegistry.getObject("unpowered_repeater");
/* 130:129 */   public static final BlockRedstoneRepeater powered_repeater = (BlockRedstoneRepeater)Block.blockRegistry.getObject("powered_repeater");
/* 131:130 */   public static final Block trapdoor = (Block)Block.blockRegistry.getObject("trapdoor");
/* 132:131 */   public static final Block monster_egg = (Block)Block.blockRegistry.getObject("monster_egg");
/* 133:132 */   public static final Block stonebrick = (Block)Block.blockRegistry.getObject("stonebrick");
/* 134:133 */   public static final Block brown_mushroom_block = (Block)Block.blockRegistry.getObject("brown_mushroom_block");
/* 135:134 */   public static final Block red_mushroom_block = (Block)Block.blockRegistry.getObject("red_mushroom_block");
/* 136:135 */   public static final Block iron_bars = (Block)Block.blockRegistry.getObject("iron_bars");
/* 137:136 */   public static final Block glass_pane = (Block)Block.blockRegistry.getObject("glass_pane");
/* 138:137 */   public static final Block melon_block = (Block)Block.blockRegistry.getObject("melon_block");
/* 139:138 */   public static final Block pumpkin_stem = (Block)Block.blockRegistry.getObject("pumpkin_stem");
/* 140:139 */   public static final Block melon_stem = (Block)Block.blockRegistry.getObject("melon_stem");
/* 141:140 */   public static final Block vine = (Block)Block.blockRegistry.getObject("vine");
/* 142:141 */   public static final Block fence_gate = (Block)Block.blockRegistry.getObject("fence_gate");
/* 143:142 */   public static final Block brick_stairs = (Block)Block.blockRegistry.getObject("brick_stairs");
/* 144:143 */   public static final Block stone_brick_stairs = (Block)Block.blockRegistry.getObject("stone_brick_stairs");
/* 145:144 */   public static final BlockMycelium mycelium = (BlockMycelium)Block.blockRegistry.getObject("mycelium");
/* 146:145 */   public static final Block waterlily = (Block)Block.blockRegistry.getObject("waterlily");
/* 147:146 */   public static final Block nether_brick = (Block)Block.blockRegistry.getObject("nether_brick");
/* 148:147 */   public static final Block nether_brick_fence = (Block)Block.blockRegistry.getObject("nether_brick_fence");
/* 149:148 */   public static final Block nether_brick_stairs = (Block)Block.blockRegistry.getObject("nether_brick_stairs");
/* 150:149 */   public static final Block nether_wart = (Block)Block.blockRegistry.getObject("nether_wart");
/* 151:150 */   public static final Block enchanting_table = (Block)Block.blockRegistry.getObject("enchanting_table");
/* 152:151 */   public static final Block brewing_stand = (Block)Block.blockRegistry.getObject("brewing_stand");
/* 153:152 */   public static final BlockCauldron cauldron = (BlockCauldron)Block.blockRegistry.getObject("cauldron");
/* 154:153 */   public static final Block end_portal = (Block)Block.blockRegistry.getObject("end_portal");
/* 155:154 */   public static final Block end_portal_frame = (Block)Block.blockRegistry.getObject("end_portal_frame");
/* 156:155 */   public static final Block end_stone = (Block)Block.blockRegistry.getObject("end_stone");
/* 157:156 */   public static final Block dragon_egg = (Block)Block.blockRegistry.getObject("dragon_egg");
/* 158:157 */   public static final Block redstone_lamp = (Block)Block.blockRegistry.getObject("redstone_lamp");
/* 159:158 */   public static final Block lit_redstone_lamp = (Block)Block.blockRegistry.getObject("lit_redstone_lamp");
/* 160:159 */   public static final BlockSlab double_wooden_slab = (BlockSlab)Block.blockRegistry.getObject("double_wooden_slab");
/* 161:160 */   public static final BlockSlab wooden_slab = (BlockSlab)Block.blockRegistry.getObject("wooden_slab");
/* 162:161 */   public static final Block cocoa = (Block)Block.blockRegistry.getObject("cocoa");
/* 163:162 */   public static final Block sandstone_stairs = (Block)Block.blockRegistry.getObject("sandstone_stairs");
/* 164:163 */   public static final Block emerald_ore = (Block)Block.blockRegistry.getObject("emerald_ore");
/* 165:164 */   public static final Block ender_chest = (Block)Block.blockRegistry.getObject("ender_chest");
/* 166:165 */   public static final BlockTripWireHook tripwire_hook = (BlockTripWireHook)Block.blockRegistry.getObject("tripwire_hook");
/* 167:166 */   public static final Block tripwire = (Block)Block.blockRegistry.getObject("tripwire");
/* 168:167 */   public static final Block emerald_block = (Block)Block.blockRegistry.getObject("emerald_block");
/* 169:168 */   public static final Block spruce_stairs = (Block)Block.blockRegistry.getObject("spruce_stairs");
/* 170:169 */   public static final Block birch_stairs = (Block)Block.blockRegistry.getObject("birch_stairs");
/* 171:170 */   public static final Block jungle_stairs = (Block)Block.blockRegistry.getObject("jungle_stairs");
/* 172:171 */   public static final Block command_block = (Block)Block.blockRegistry.getObject("command_block");
/* 173:172 */   public static final BlockBeacon beacon = (BlockBeacon)Block.blockRegistry.getObject("beacon");
/* 174:173 */   public static final Block cobblestone_wall = (Block)Block.blockRegistry.getObject("cobblestone_wall");
/* 175:174 */   public static final Block flower_pot = (Block)Block.blockRegistry.getObject("flower_pot");
/* 176:175 */   public static final Block carrots = (Block)Block.blockRegistry.getObject("carrots");
/* 177:176 */   public static final Block potatoes = (Block)Block.blockRegistry.getObject("potatoes");
/* 178:177 */   public static final Block wooden_button = (Block)Block.blockRegistry.getObject("wooden_button");
/* 179:178 */   public static final Block skull = (Block)Block.blockRegistry.getObject("skull");
/* 180:179 */   public static final Block anvil = (Block)Block.blockRegistry.getObject("anvil");
/* 181:180 */   public static final Block trapped_chest = (Block)Block.blockRegistry.getObject("trapped_chest");
/* 182:181 */   public static final Block light_weighted_pressure_plate = (Block)Block.blockRegistry.getObject("light_weighted_pressure_plate");
/* 183:182 */   public static final Block heavy_weighted_pressure_plate = (Block)Block.blockRegistry.getObject("heavy_weighted_pressure_plate");
/* 184:183 */   public static final BlockRedstoneComparator unpowered_comparator = (BlockRedstoneComparator)Block.blockRegistry.getObject("unpowered_comparator");
/* 185:184 */   public static final BlockRedstoneComparator powered_comparator = (BlockRedstoneComparator)Block.blockRegistry.getObject("powered_comparator");
/* 186:185 */   public static final BlockDaylightDetector daylight_detector = (BlockDaylightDetector)Block.blockRegistry.getObject("daylight_detector");
/* 187:186 */   public static final Block redstone_block = (Block)Block.blockRegistry.getObject("redstone_block");
/* 188:187 */   public static final Block quartz_ore = (Block)Block.blockRegistry.getObject("quartz_ore");
/* 189:188 */   public static final BlockHopper hopper = (BlockHopper)Block.blockRegistry.getObject("hopper");
/* 190:189 */   public static final Block quartz_block = (Block)Block.blockRegistry.getObject("quartz_block");
/* 191:190 */   public static final Block quartz_stairs = (Block)Block.blockRegistry.getObject("quartz_stairs");
/* 192:191 */   public static final Block activator_rail = (Block)Block.blockRegistry.getObject("activator_rail");
/* 193:192 */   public static final Block dropper = (Block)Block.blockRegistry.getObject("dropper");
/* 194:193 */   public static final Block stained_hardened_clay = (Block)Block.blockRegistry.getObject("stained_hardened_clay");
/* 195:194 */   public static final Block hay_block = (Block)Block.blockRegistry.getObject("hay_block");
/* 196:195 */   public static final Block carpet = (Block)Block.blockRegistry.getObject("carpet");
/* 197:196 */   public static final Block hardened_clay = (Block)Block.blockRegistry.getObject("hardened_clay");
/* 198:197 */   public static final Block coal_block = (Block)Block.blockRegistry.getObject("coal_block");
/* 199:198 */   public static final Block packed_ice = (Block)Block.blockRegistry.getObject("packed_ice");
/* 200:199 */   public static final Block acacia_stairs = (Block)Block.blockRegistry.getObject("acacia_stairs");
/* 201:200 */   public static final Block dark_oak_stairs = (Block)Block.blockRegistry.getObject("dark_oak_stairs");
/* 202:201 */   public static final BlockDoublePlant double_plant = (BlockDoublePlant)Block.blockRegistry.getObject("double_plant");
/* 203:202 */   public static final BlockStainedGlass stained_glass = (BlockStainedGlass)Block.blockRegistry.getObject("stained_glass");
/* 204:203 */   public static final BlockStainedGlassPane stained_glass_pane = (BlockStainedGlassPane)Block.blockRegistry.getObject("stained_glass_pane");
/* 205:    */   private static final String __OBFID = "CL_00000204";
/* 206:    */ }


/* Location:           C:\Users\D\AppData\Roaming\.minecraft\versions\Nodus_2.0-1.7.x\Nodus_2.0-1.7.x.jar
 * Qualified Name:     net.minecraft.init.Blocks
 * JD-Core Version:    0.7.0.1
 */