package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiverMix
  extends GenLayer
{
  private GenLayer biomePatternGeneratorChain;
  private GenLayer riverPatternGeneratorChain;
  private static final String __OBFID = "CL_00000567";
  
  public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_)
  {
    super(p_i2129_1_);
    this.biomePatternGeneratorChain = p_i2129_3_;
    this.riverPatternGeneratorChain = p_i2129_4_;
  }
  
  public void initWorldGenSeed(long p_75905_1_)
  {
    this.biomePatternGeneratorChain.initWorldGenSeed(p_75905_1_);
    this.riverPatternGeneratorChain.initWorldGenSeed(p_75905_1_);
    super.initWorldGenSeed(p_75905_1_);
  }
  
  public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
  {
    int[] var5 = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
    int[] var6 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
    int[] var7 = IntCache.getIntCache(areaWidth * areaHeight);
    for (int var8 = 0; var8 < areaWidth * areaHeight; var8++) {
      if ((var5[var8] != BiomeGenBase.ocean.biomeID) && (var5[var8] != BiomeGenBase.deepOcean.biomeID))
      {
        if (var6[var8] == BiomeGenBase.river.biomeID)
        {
          if (var5[var8] == BiomeGenBase.icePlains.biomeID) {
            var7[var8] = BiomeGenBase.frozenRiver.biomeID;
          } else if ((var5[var8] != BiomeGenBase.mushroomIsland.biomeID) && (var5[var8] != BiomeGenBase.mushroomIslandShore.biomeID)) {
            var6[var8] &= 0xFF;
          } else {
            var7[var8] = BiomeGenBase.mushroomIslandShore.biomeID;
          }
        }
        else {
          var7[var8] = var5[var8];
        }
      }
      else {
        var7[var8] = var5[var8];
      }
    }
    return var7;
  }
}
