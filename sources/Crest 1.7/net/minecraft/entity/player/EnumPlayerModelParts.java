// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.player;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum EnumPlayerModelParts
{
    CAPE("CAPE", 0, "CAPE", 0, 0, "cape"), 
    JACKET("JACKET", 1, "JACKET", 1, 1, "jacket"), 
    LEFT_SLEEVE("LEFT_SLEEVE", 2, "LEFT_SLEEVE", 2, 2, "left_sleeve"), 
    RIGHT_SLEEVE("RIGHT_SLEEVE", 3, "RIGHT_SLEEVE", 3, 3, "right_sleeve"), 
    LEFT_PANTS_LEG("LEFT_PANTS_LEG", 4, "LEFT_PANTS_LEG", 4, 4, "left_pants_leg"), 
    RIGHT_PANTS_LEG("RIGHT_PANTS_LEG", 5, "RIGHT_PANTS_LEG", 5, 5, "right_pants_leg"), 
    HAT("HAT", 6, "HAT", 6, 6, "hat");
    
    private final int field_179340_h;
    private final int field_179341_i;
    private final String field_179338_j;
    private final IChatComponent field_179339_k;
    private static final EnumPlayerModelParts[] $VALUES;
    private static final String __OBFID = "CL_00002187";
    
    static {
        $VALUES = new EnumPlayerModelParts[] { EnumPlayerModelParts.CAPE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.RIGHT_SLEEVE, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.HAT };
    }
    
    private EnumPlayerModelParts(final String s, final int n, final String p_i45809_1_, final int p_i45809_2_, final int p_i45809_3_, final String p_i45809_4_) {
        this.field_179340_h = p_i45809_3_;
        this.field_179341_i = 1 << p_i45809_3_;
        this.field_179338_j = p_i45809_4_;
        this.field_179339_k = new ChatComponentTranslation("options.modelPart." + p_i45809_4_, new Object[0]);
    }
    
    public int func_179327_a() {
        return this.field_179341_i;
    }
    
    public int func_179328_b() {
        return this.field_179340_h;
    }
    
    public String func_179329_c() {
        return this.field_179338_j;
    }
    
    public IChatComponent func_179326_d() {
        return this.field_179339_k;
    }
}
