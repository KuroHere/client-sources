// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

public enum EnumSkyBlock
{
    SKY(15), 
    BLOCK(0);
    
    public final int defaultLightValue;
    
    private EnumSkyBlock(final int defaultLightValueIn) {
        this.defaultLightValue = defaultLightValueIn;
    }
}
