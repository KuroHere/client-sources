package net.minecraft.world.gen.layer;

public class GenLayerAddSnow
  extends GenLayer
{
  private static final String __OBFID = "CL_00000553";
  
  public GenLayerAddSnow(long p_i2121_1_, GenLayer p_i2121_3_)
  {
    super(p_i2121_1_);
    this.parent = p_i2121_3_;
  }
  
  public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
  {
    int var5 = areaX - 1;
    int var6 = areaY - 1;
    int var7 = areaWidth + 2;
    int var8 = areaHeight + 2;
    int[] var9 = this.parent.getInts(var5, var6, var7, var8);
    int[] var10 = IntCache.getIntCache(areaWidth * areaHeight);
    for (int var11 = 0; var11 < areaHeight; var11++) {
      for (int var12 = 0; var12 < areaWidth; var12++)
      {
        int var13 = var9[(var12 + 1 + (var11 + 1) * var7)];
        initChunkSeed(var12 + areaX, var11 + areaY);
        if (var13 == 0)
        {
          var10[(var12 + var11 * areaWidth)] = 0;
        }
        else
        {
          int var14 = nextInt(6);
          byte var15;
          byte var15;
          if (var14 == 0)
          {
            var15 = 4;
          }
          else
          {
            byte var15;
            if (var14 <= 1) {
              var15 = 3;
            } else {
              var15 = 1;
            }
          }
          var10[(var12 + var11 * areaWidth)] = var15;
        }
      }
    }
    return var10;
  }
}
