package HORIZON-6-0-SKIDPROTECTION;

public class GenLayerZoom extends GenLayer
{
    private static final String Ý = "CL_00000572";
    
    public GenLayerZoom(final long p_i2134_1_, final GenLayer p_i2134_3_) {
        super(p_i2134_1_);
        super.HorizonCode_Horizon_È = p_i2134_3_;
    }
    
    @Override
    public int[] HorizonCode_Horizon_È(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int var5 = areaX >> 1;
        final int var6 = areaY >> 1;
        final int var7 = (areaWidth >> 1) + 2;
        final int var8 = (areaHeight >> 1) + 2;
        final int[] var9 = this.HorizonCode_Horizon_È.HorizonCode_Horizon_È(var5, var6, var7, var8);
        final int var10 = var7 - 1 << 1;
        final int var11 = var8 - 1 << 1;
        final int[] var12 = IntCache.HorizonCode_Horizon_È(var10 * var11);
        for (int var13 = 0; var13 < var8 - 1; ++var13) {
            int var14 = (var13 << 1) * var10;
            int var15 = 0;
            int var16 = var9[var15 + 0 + (var13 + 0) * var7];
            int var17 = var9[var15 + 0 + (var13 + 1) * var7];
            while (var15 < var7 - 1) {
                this.HorizonCode_Horizon_È(var15 + var5 << 1, (long)(var13 + var6 << 1));
                final int var18 = var9[var15 + 1 + (var13 + 0) * var7];
                final int var19 = var9[var15 + 1 + (var13 + 1) * var7];
                var12[var14] = var16;
                var12[var14++ + var10] = this.HorizonCode_Horizon_È(new int[] { var16, var17 });
                var12[var14] = this.HorizonCode_Horizon_È(new int[] { var16, var18 });
                var12[var14++ + var10] = this.Â(var16, var18, var17, var19);
                var16 = var18;
                var17 = var19;
                ++var15;
            }
        }
        final int[] var20 = IntCache.HorizonCode_Horizon_È(areaWidth * areaHeight);
        for (int var14 = 0; var14 < areaHeight; ++var14) {
            System.arraycopy(var12, (var14 + (areaY & 0x1)) * var10 + (areaX & 0x1), var20, var14 * areaWidth, areaWidth);
        }
        return var20;
    }
    
    public static GenLayer HorizonCode_Horizon_È(final long p_75915_0_, final GenLayer p_75915_2_, final int p_75915_3_) {
        Object var4 = p_75915_2_;
        for (int var5 = 0; var5 < p_75915_3_; ++var5) {
            var4 = new GenLayerZoom(p_75915_0_ + var5, (GenLayer)var4);
        }
        return (GenLayer)var4;
    }
}
