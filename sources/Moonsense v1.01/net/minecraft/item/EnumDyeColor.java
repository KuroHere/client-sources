// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE("WHITE", 0, "WHITE", 0, 0, 15, "white", "white", MapColor.snowColor, EnumChatFormatting.WHITE), 
    ORANGE("ORANGE", 1, "ORANGE", 1, 1, 14, "orange", "orange", MapColor.adobeColor, EnumChatFormatting.GOLD), 
    MAGENTA("MAGENTA", 2, "MAGENTA", 2, 2, 13, "magenta", "magenta", MapColor.magentaColor, EnumChatFormatting.AQUA), 
    LIGHT_BLUE("LIGHT_BLUE", 3, "LIGHT_BLUE", 3, 3, 12, "light_blue", "lightBlue", MapColor.lightBlueColor, EnumChatFormatting.BLUE), 
    YELLOW("YELLOW", 4, "YELLOW", 4, 4, 11, "yellow", "yellow", MapColor.yellowColor, EnumChatFormatting.YELLOW), 
    LIME("LIME", 5, "LIME", 5, 5, 10, "lime", "lime", MapColor.limeColor, EnumChatFormatting.GREEN), 
    PINK("PINK", 6, "PINK", 6, 6, 9, "pink", "pink", MapColor.pinkColor, EnumChatFormatting.LIGHT_PURPLE), 
    GRAY("GRAY", 7, "GRAY", 7, 7, 8, "gray", "gray", MapColor.grayColor, EnumChatFormatting.DARK_GRAY), 
    SILVER("SILVER", 8, "SILVER", 8, 8, 7, "silver", "silver", MapColor.silverColor, EnumChatFormatting.GRAY), 
    CYAN("CYAN", 9, "CYAN", 9, 9, 6, "cyan", "cyan", MapColor.cyanColor, EnumChatFormatting.DARK_AQUA), 
    PURPLE("PURPLE", 10, "PURPLE", 10, 10, 5, "purple", "purple", MapColor.purpleColor, EnumChatFormatting.DARK_PURPLE), 
    BLUE("BLUE", 11, "BLUE", 11, 11, 4, "blue", "blue", MapColor.blueColor, EnumChatFormatting.DARK_BLUE), 
    BROWN("BROWN", 12, "BROWN", 12, 12, 3, "brown", "brown", MapColor.brownColor, EnumChatFormatting.GOLD), 
    GREEN("GREEN", 13, "GREEN", 13, 13, 2, "green", "green", MapColor.greenColor, EnumChatFormatting.DARK_GREEN), 
    RED("RED", 14, "RED", 14, 14, 1, "red", "red", MapColor.redColor, EnumChatFormatting.DARK_RED), 
    BLACK("BLACK", 15, "BLACK", 15, 15, 0, "black", "black", MapColor.blackColor, EnumChatFormatting.BLACK);
    
    private static final EnumDyeColor[] field_176790_q;
    private static final EnumDyeColor[] field_176789_r;
    private final int field_176788_s;
    private final int field_176787_t;
    private final String field_176786_u;
    private final String field_176785_v;
    private final MapColor field_176784_w;
    private final EnumChatFormatting field_176793_x;
    private static final EnumDyeColor[] $VALUES;
    private static final String __OBFID = "CL_00002180";
    
    static {
        field_176790_q = new EnumDyeColor[values().length];
        field_176789_r = new EnumDyeColor[values().length];
        $VALUES = new EnumDyeColor[] { EnumDyeColor.WHITE, EnumDyeColor.ORANGE, EnumDyeColor.MAGENTA, EnumDyeColor.LIGHT_BLUE, EnumDyeColor.YELLOW, EnumDyeColor.LIME, EnumDyeColor.PINK, EnumDyeColor.GRAY, EnumDyeColor.SILVER, EnumDyeColor.CYAN, EnumDyeColor.PURPLE, EnumDyeColor.BLUE, EnumDyeColor.BROWN, EnumDyeColor.GREEN, EnumDyeColor.RED, EnumDyeColor.BLACK };
        for (final EnumDyeColor var4 : values()) {
            EnumDyeColor.field_176790_q[var4.func_176765_a()] = var4;
            EnumDyeColor.field_176789_r[var4.getDyeColorDamage()] = var4;
        }
    }
    
    private EnumDyeColor(final String name, final int ordinal, final String p_i45786_1_, final int p_i45786_2_, final int p_i45786_3_, final int p_i45786_4_, final String p_i45786_5_, final String p_i45786_6_, final MapColor p_i45786_7_, final EnumChatFormatting p_i45786_8_) {
        this.field_176788_s = p_i45786_3_;
        this.field_176787_t = p_i45786_4_;
        this.field_176786_u = p_i45786_5_;
        this.field_176785_v = p_i45786_6_;
        this.field_176784_w = p_i45786_7_;
        this.field_176793_x = p_i45786_8_;
    }
    
    public int func_176765_a() {
        return this.field_176788_s;
    }
    
    public int getDyeColorDamage() {
        return this.field_176787_t;
    }
    
    public String func_176762_d() {
        return this.field_176785_v;
    }
    
    public MapColor func_176768_e() {
        return this.field_176784_w;
    }
    
    public static EnumDyeColor func_176766_a(int p_176766_0_) {
        if (p_176766_0_ < 0 || p_176766_0_ >= EnumDyeColor.field_176789_r.length) {
            p_176766_0_ = 0;
        }
        return EnumDyeColor.field_176789_r[p_176766_0_];
    }
    
    public static EnumDyeColor func_176764_b(int p_176764_0_) {
        if (p_176764_0_ < 0 || p_176764_0_ >= EnumDyeColor.field_176790_q.length) {
            p_176764_0_ = 0;
        }
        return EnumDyeColor.field_176790_q[p_176764_0_];
    }
    
    @Override
    public String toString() {
        return this.field_176785_v;
    }
    
    @Override
    public String getName() {
        return this.field_176786_u;
    }
}
