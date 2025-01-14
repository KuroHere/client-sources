/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.stats;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.JsonSerializableSet;

public class AchievementList {
    public static int minDisplayColumn;
    public static int minDisplayRow;
    public static int maxDisplayColumn;
    public static int maxDisplayRow;
    public static List achievementList;
    public static Achievement openInventory;
    public static Achievement mineWood;
    public static Achievement buildWorkBench;
    public static Achievement buildPickaxe;
    public static Achievement buildFurnace;
    public static Achievement acquireIron;
    public static Achievement buildHoe;
    public static Achievement makeBread;
    public static Achievement bakeCake;
    public static Achievement buildBetterPickaxe;
    public static Achievement cookFish;
    public static Achievement onARail;
    public static Achievement buildSword;
    public static Achievement killEnemy;
    public static Achievement killCow;
    public static Achievement flyPig;
    public static Achievement snipeSkeleton;
    public static Achievement diamonds;
    public static Achievement diamondsToYou;
    public static Achievement portal;
    public static Achievement ghast;
    public static Achievement blazeRod;
    public static Achievement potion;
    public static Achievement theEnd;
    public static Achievement theEnd2;
    public static Achievement enchantments;
    public static Achievement overkill;
    public static Achievement bookcase;
    public static Achievement breedCow;
    public static Achievement spawnWither;
    public static Achievement killWither;
    public static Achievement fullBeacon;
    public static Achievement exploreAllBiomes;
    public static Achievement overpowered;
    private static final String __OBFID = "CL_00001467";

    static {
        achievementList = Lists.newArrayList();
        openInventory = new Achievement("achievement.openInventory", "openInventory", 0, 0, Items.book, null).func_180789_a().func_180788_c();
        mineWood = new Achievement("achievement.mineWood", "mineWood", 2, 1, Blocks.log, openInventory).func_180788_c();
        buildWorkBench = new Achievement("achievement.buildWorkBench", "buildWorkBench", 4, -1, Blocks.crafting_table, mineWood).func_180788_c();
        buildPickaxe = new Achievement("achievement.buildPickaxe", "buildPickaxe", 4, 2, Items.wooden_pickaxe, buildWorkBench).func_180788_c();
        buildFurnace = new Achievement("achievement.buildFurnace", "buildFurnace", 3, 4, Blocks.furnace, buildPickaxe).func_180788_c();
        acquireIron = new Achievement("achievement.acquireIron", "acquireIron", 1, 4, Items.iron_ingot, buildFurnace).func_180788_c();
        buildHoe = new Achievement("achievement.buildHoe", "buildHoe", 2, -3, Items.wooden_hoe, buildWorkBench).func_180788_c();
        makeBread = new Achievement("achievement.makeBread", "makeBread", -1, -3, Items.bread, buildHoe).func_180788_c();
        bakeCake = new Achievement("achievement.bakeCake", "bakeCake", 0, -5, Items.cake, buildHoe).func_180788_c();
        buildBetterPickaxe = new Achievement("achievement.buildBetterPickaxe", "buildBetterPickaxe", 6, 2, Items.stone_pickaxe, buildPickaxe).func_180788_c();
        cookFish = new Achievement("achievement.cookFish", "cookFish", 2, 6, Items.cooked_fish, buildFurnace).func_180788_c();
        onARail = new Achievement("achievement.onARail", "onARail", 2, 3, Blocks.rail, acquireIron).setSpecial().func_180788_c();
        buildSword = new Achievement("achievement.buildSword", "buildSword", 6, -1, Items.wooden_sword, buildWorkBench).func_180788_c();
        killEnemy = new Achievement("achievement.killEnemy", "killEnemy", 8, -1, Items.bone, buildSword).func_180788_c();
        killCow = new Achievement("achievement.killCow", "killCow", 7, -3, Items.leather, buildSword).func_180788_c();
        flyPig = new Achievement("achievement.flyPig", "flyPig", 9, -3, Items.saddle, killCow).setSpecial().func_180788_c();
        snipeSkeleton = new Achievement("achievement.snipeSkeleton", "snipeSkeleton", 7, 0, Items.bow, killEnemy).setSpecial().func_180788_c();
        diamonds = new Achievement("achievement.diamonds", "diamonds", -1, 5, Blocks.diamond_ore, acquireIron).func_180788_c();
        diamondsToYou = new Achievement("achievement.diamondsToYou", "diamondsToYou", -1, 2, Items.diamond, diamonds).func_180788_c();
        portal = new Achievement("achievement.portal", "portal", -1, 7, Blocks.obsidian, diamonds).func_180788_c();
        ghast = new Achievement("achievement.ghast", "ghast", -4, 8, Items.ghast_tear, portal).setSpecial().func_180788_c();
        blazeRod = new Achievement("achievement.blazeRod", "blazeRod", 0, 9, Items.blaze_rod, portal).func_180788_c();
        potion = new Achievement("achievement.potion", "potion", 2, 8, Items.potionitem, blazeRod).func_180788_c();
        theEnd = new Achievement("achievement.theEnd", "theEnd", 3, 10, Items.ender_eye, blazeRod).setSpecial().func_180788_c();
        theEnd2 = new Achievement("achievement.theEnd2", "theEnd2", 4, 13, Blocks.dragon_egg, theEnd).setSpecial().func_180788_c();
        enchantments = new Achievement("achievement.enchantments", "enchantments", -4, 4, Blocks.enchanting_table, diamonds).func_180788_c();
        overkill = new Achievement("achievement.overkill", "overkill", -4, 1, Items.diamond_sword, enchantments).setSpecial().func_180788_c();
        bookcase = new Achievement("achievement.bookcase", "bookcase", -3, 6, Blocks.bookshelf, enchantments).func_180788_c();
        breedCow = new Achievement("achievement.breedCow", "breedCow", 7, -5, Items.wheat, killCow).func_180788_c();
        spawnWither = new Achievement("achievement.spawnWither", "spawnWither", 7, 12, new ItemStack(Items.skull, 1, 1), theEnd2).func_180788_c();
        killWither = new Achievement("achievement.killWither", "killWither", 7, 10, Items.nether_star, spawnWither).func_180788_c();
        fullBeacon = new Achievement("achievement.fullBeacon", "fullBeacon", 7, 8, Blocks.beacon, killWither).setSpecial().func_180788_c();
        exploreAllBiomes = new Achievement("achievement.exploreAllBiomes", "exploreAllBiomes", 4, 8, Items.diamond_boots, theEnd).func_180787_a(JsonSerializableSet.class).setSpecial().func_180788_c();
        overpowered = new Achievement("achievement.overpowered", "overpowered", 6, 4, Items.golden_apple, buildBetterPickaxe).setSpecial().func_180788_c();
    }

    public static void init() {
    }
}

