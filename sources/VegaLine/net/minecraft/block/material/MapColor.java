/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.block.material;

import net.minecraft.item.EnumDyeColor;

public class MapColor {
    public static final MapColor[] COLORS = new MapColor[64];
    public static final MapColor[] field_193575_b = new MapColor[16];
    public static final MapColor AIR = new MapColor(0, 0);
    public static final MapColor GRASS = new MapColor(1, 8368696);
    public static final MapColor SAND = new MapColor(2, 16247203);
    public static final MapColor CLOTH = new MapColor(3, 0xC7C7C7);
    public static final MapColor TNT = new MapColor(4, 0xFF0000);
    public static final MapColor ICE = new MapColor(5, 0xA0A0FF);
    public static final MapColor IRON = new MapColor(6, 0xA7A7A7);
    public static final MapColor FOLIAGE = new MapColor(7, 31744);
    public static final MapColor SNOW = new MapColor(8, 0xFFFFFF);
    public static final MapColor CLAY = new MapColor(9, 10791096);
    public static final MapColor DIRT = new MapColor(10, 9923917);
    public static final MapColor STONE = new MapColor(11, 0x707070);
    public static final MapColor WATER = new MapColor(12, 0x4040FF);
    public static final MapColor WOOD = new MapColor(13, 9402184);
    public static final MapColor QUARTZ = new MapColor(14, 0xFFFCF5);
    public static final MapColor ADOBE = new MapColor(15, 14188339);
    public static final MapColor MAGENTA = new MapColor(16, 11685080);
    public static final MapColor LIGHT_BLUE = new MapColor(17, 6724056);
    public static final MapColor YELLOW = new MapColor(18, 0xE5E533);
    public static final MapColor LIME = new MapColor(19, 8375321);
    public static final MapColor PINK = new MapColor(20, 15892389);
    public static final MapColor GRAY = new MapColor(21, 0x4C4C4C);
    public static final MapColor SILVER = new MapColor(22, 0x999999);
    public static final MapColor CYAN = new MapColor(23, 5013401);
    public static final MapColor PURPLE = new MapColor(24, 8339378);
    public static final MapColor BLUE = new MapColor(25, 3361970);
    public static final MapColor BROWN = new MapColor(26, 6704179);
    public static final MapColor GREEN = new MapColor(27, 6717235);
    public static final MapColor RED = new MapColor(28, 0x993333);
    public static final MapColor BLACK = new MapColor(29, 0x191919);
    public static final MapColor GOLD = new MapColor(30, 16445005);
    public static final MapColor DIAMOND = new MapColor(31, 6085589);
    public static final MapColor LAPIS = new MapColor(32, 4882687);
    public static final MapColor EMERALD = new MapColor(33, 55610);
    public static final MapColor OBSIDIAN = new MapColor(34, 8476209);
    public static final MapColor NETHERRACK = new MapColor(35, 0x700200);
    public static final MapColor field_193561_M = new MapColor(36, 13742497);
    public static final MapColor field_193562_N = new MapColor(37, 10441252);
    public static final MapColor field_193563_O = new MapColor(38, 9787244);
    public static final MapColor field_193564_P = new MapColor(39, 7367818);
    public static final MapColor field_193565_Q = new MapColor(40, 12223780);
    public static final MapColor field_193566_R = new MapColor(41, 6780213);
    public static final MapColor field_193567_S = new MapColor(42, 10505550);
    public static final MapColor field_193568_T = new MapColor(43, 0x392923);
    public static final MapColor field_193569_U = new MapColor(44, 8874850);
    public static final MapColor field_193570_V = new MapColor(45, 0x575C5C);
    public static final MapColor field_193571_W = new MapColor(46, 8014168);
    public static final MapColor field_193572_X = new MapColor(47, 4996700);
    public static final MapColor field_193573_Y = new MapColor(48, 4993571);
    public static final MapColor field_193574_Z = new MapColor(49, 5001770);
    public static final MapColor field_193559_aa = new MapColor(50, 9321518);
    public static final MapColor field_193560_ab = new MapColor(51, 2430480);
    public int colorValue;
    public final int colorIndex;

    private MapColor(int index, int color) {
        if (index < 0 || index > 63) {
            throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
        }
        this.colorIndex = index;
        this.colorValue = color;
        MapColor.COLORS[index] = this;
    }

    public int getMapColor(int p_151643_1_) {
        int i = 220;
        if (p_151643_1_ == 3) {
            i = 135;
        }
        if (p_151643_1_ == 2) {
            i = 255;
        }
        if (p_151643_1_ == 1) {
            i = 220;
        }
        if (p_151643_1_ == 0) {
            i = 180;
        }
        int j = (this.colorValue >> 16 & 0xFF) * i / 255;
        int k = (this.colorValue >> 8 & 0xFF) * i / 255;
        int l = (this.colorValue & 0xFF) * i / 255;
        return 0xFF000000 | j << 16 | k << 8 | l;
    }

    public static MapColor func_193558_a(EnumDyeColor p_193558_0_) {
        return field_193575_b[p_193558_0_.getMetadata()];
    }

    static {
        MapColor.field_193575_b[EnumDyeColor.WHITE.getMetadata()] = SNOW;
        MapColor.field_193575_b[EnumDyeColor.ORANGE.getMetadata()] = ADOBE;
        MapColor.field_193575_b[EnumDyeColor.MAGENTA.getMetadata()] = MAGENTA;
        MapColor.field_193575_b[EnumDyeColor.LIGHT_BLUE.getMetadata()] = LIGHT_BLUE;
        MapColor.field_193575_b[EnumDyeColor.YELLOW.getMetadata()] = YELLOW;
        MapColor.field_193575_b[EnumDyeColor.LIME.getMetadata()] = LIME;
        MapColor.field_193575_b[EnumDyeColor.PINK.getMetadata()] = PINK;
        MapColor.field_193575_b[EnumDyeColor.GRAY.getMetadata()] = GRAY;
        MapColor.field_193575_b[EnumDyeColor.SILVER.getMetadata()] = SILVER;
        MapColor.field_193575_b[EnumDyeColor.CYAN.getMetadata()] = CYAN;
        MapColor.field_193575_b[EnumDyeColor.PURPLE.getMetadata()] = PURPLE;
        MapColor.field_193575_b[EnumDyeColor.BLUE.getMetadata()] = BLUE;
        MapColor.field_193575_b[EnumDyeColor.BROWN.getMetadata()] = BROWN;
        MapColor.field_193575_b[EnumDyeColor.GREEN.getMetadata()] = GREEN;
        MapColor.field_193575_b[EnumDyeColor.RED.getMetadata()] = RED;
        MapColor.field_193575_b[EnumDyeColor.BLACK.getMetadata()] = BLACK;
    }
}

