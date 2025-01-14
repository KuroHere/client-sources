package net.minecraft.src;

public class GenLayerAddSnow extends GenLayer
{
    public GenLayerAddSnow(final long par1, final GenLayer par3GenLayer) {
        super(par1);
        this.parent = par3GenLayer;
    }
    
    @Override
    public int[] getInts(final int par1, final int par2, final int par3, final int par4) {
        final int var5 = par1 - 1;
        final int var6 = par2 - 1;
        final int var7 = par3 + 2;
        final int var8 = par4 + 2;
        final int[] var9 = this.parent.getInts(var5, var6, var7, var8);
        final int[] var10 = IntCache.getIntCache(par3 * par4);
        for (int var11 = 0; var11 < par4; ++var11) {
            for (int var12 = 0; var12 < par3; ++var12) {
                final int var13 = var9[var12 + 1 + (var11 + 1) * var7];
                this.initChunkSeed(var12 + par1, var11 + par2);
                if (var13 == 0) {
                    var10[var12 + var11 * par3] = 0;
                }
                else {
                    int var14 = this.nextInt(5);
                    if (var14 == 0) {
                        var14 = BiomeGenBase.icePlains.biomeID;
                    }
                    else {
                        var14 = 1;
                    }
                    var10[var12 + var11 * par3] = var14;
                }
            }
        }
        return var10;
    }
}
