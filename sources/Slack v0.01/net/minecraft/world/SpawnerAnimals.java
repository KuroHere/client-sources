package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.optifine.BlockPosM;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;

public final class SpawnerAnimals {
   private static final int MOB_COUNT_DIV = (int)Math.pow(17.0D, 2.0D);
   private final Set<ChunkCoordIntPair> eligibleChunksForSpawning = Sets.newHashSet();
   private Map<Class, EntityLiving> mapSampleEntitiesByClass = new HashMap();
   private int lastPlayerChunkX = Integer.MAX_VALUE;
   private int lastPlayerChunkZ = Integer.MAX_VALUE;
   private int countChunkPos;

   public int findChunksForSpawning(WorldServer p_77192_1_, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean p_77192_4_) {
      if (!spawnHostileMobs && !spawnPeacefulMobs) {
         return 0;
      } else {
         boolean flag = true;
         EntityPlayer entityplayer = null;
         if (p_77192_1_.playerEntities.size() == 1) {
            entityplayer = (EntityPlayer)p_77192_1_.playerEntities.get(0);
            if (this.eligibleChunksForSpawning.size() > 0 && entityplayer != null && entityplayer.chunkCoordX == this.lastPlayerChunkX && entityplayer.chunkCoordZ == this.lastPlayerChunkZ) {
               flag = false;
            }
         }

         int j4;
         int k;
         int j1;
         if (flag) {
            this.eligibleChunksForSpawning.clear();
            j4 = 0;
            Iterator var8 = p_77192_1_.playerEntities.iterator();

            label199:
            while(true) {
               EntityPlayer entityplayer1;
               do {
                  if (!var8.hasNext()) {
                     this.countChunkPos = j4;
                     if (entityplayer != null) {
                        this.lastPlayerChunkX = entityplayer.chunkCoordX;
                        this.lastPlayerChunkZ = entityplayer.chunkCoordZ;
                     }
                     break label199;
                  }

                  entityplayer1 = (EntityPlayer)var8.next();
               } while(entityplayer1.isSpectator());

               int j = MathHelper.floor_double(entityplayer1.posX / 16.0D);
               k = MathHelper.floor_double(entityplayer1.posZ / 16.0D);
               int l = 8;

               for(int i1 = -l; i1 <= l; ++i1) {
                  for(j1 = -l; j1 <= l; ++j1) {
                     boolean flag1 = i1 == -l || i1 == l || j1 == -l || j1 == l;
                     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i1 + j, j1 + k);
                     if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)) {
                        ++j4;
                        if (!flag1 && p_77192_1_.getWorldBorder().contains(chunkcoordintpair)) {
                           this.eligibleChunksForSpawning.add(chunkcoordintpair);
                        }
                     }
                  }
               }
            }
         }

         j4 = 0;
         BlockPos blockpos2 = p_77192_1_.getSpawnPoint();
         BlockPosM blockposm = new BlockPosM(0, 0, 0);
         new BlockPos.MutableBlockPos();
         EnumCreatureType[] var42 = EnumCreatureType.values();
         k = var42.length;

         label172:
         for(int var43 = 0; var43 < k; ++var43) {
            EnumCreatureType enumcreaturetype = var42[var43];
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || p_77192_4_)) {
               j1 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt(p_77192_1_, Reflector.ForgeWorld_countEntities, enumcreaturetype, Boolean.TRUE) : p_77192_1_.countEntities(enumcreaturetype.getCreatureClass());
               int l4 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV;
               if (j1 <= l4) {
                  Collection<ChunkCoordIntPair> collection = this.eligibleChunksForSpawning;
                  if (Reflector.ForgeHooksClient.exists()) {
                     ArrayList<ChunkCoordIntPair> arraylist = Lists.newArrayList((Iterable)collection);
                     Collections.shuffle(arraylist);
                     collection = arraylist;
                  }

                  Iterator var47 = ((Collection)collection).iterator();

                  label169:
                  while(true) {
                     int k1;
                     int l1;
                     int i2;
                     Block block;
                     do {
                        if (!var47.hasNext()) {
                           continue label172;
                        }

                        ChunkCoordIntPair chunkcoordintpair1 = (ChunkCoordIntPair)var47.next();
                        BlockPos blockpos = getRandomChunkPosition(p_77192_1_, chunkcoordintpair1.chunkXPos, chunkcoordintpair1.chunkZPos, blockposm);
                        k1 = blockpos.getX();
                        l1 = blockpos.getY();
                        i2 = blockpos.getZ();
                        block = p_77192_1_.getBlockState(blockpos).getBlock();
                     } while(block.isNormalCube());

                     int j2 = 0;

                     for(int k2 = 0; k2 < 3; ++k2) {
                        int l2 = k1;
                        int i3 = l1;
                        int j3 = i2;
                        int k3 = 6;
                        BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = null;
                        IEntityLivingData ientitylivingdata = null;

                        for(int l3 = 0; l3 < 4; ++l3) {
                           l2 += p_77192_1_.rand.nextInt(k3) - p_77192_1_.rand.nextInt(k3);
                           i3 += p_77192_1_.rand.nextInt(1) - p_77192_1_.rand.nextInt(1);
                           j3 += p_77192_1_.rand.nextInt(k3) - p_77192_1_.rand.nextInt(k3);
                           BlockPos blockpos1 = new BlockPos(l2, i3, j3);
                           float f = (float)l2 + 0.5F;
                           float f1 = (float)j3 + 0.5F;
                           if (!p_77192_1_.isAnyPlayerWithinRangeAt((double)f, (double)i3, (double)f1, 24.0D) && blockpos2.distanceSq((double)f, (double)i3, (double)f1) >= 576.0D) {
                              if (biomegenbase$spawnlistentry == null) {
                                 biomegenbase$spawnlistentry = p_77192_1_.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos1);
                                 if (biomegenbase$spawnlistentry == null) {
                                    break;
                                 }
                              }

                              if (p_77192_1_.canCreatureTypeSpawnHere(enumcreaturetype, biomegenbase$spawnlistentry, blockpos1) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biomegenbase$spawnlistentry.entityClass), p_77192_1_, blockpos1)) {
                                 EntityLiving entityliving;
                                 try {
                                    entityliving = (EntityLiving)this.mapSampleEntitiesByClass.get(biomegenbase$spawnlistentry.entityClass);
                                    if (entityliving == null) {
                                       entityliving = (EntityLiving)biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(p_77192_1_);
                                       this.mapSampleEntitiesByClass.put(biomegenbase$spawnlistentry.entityClass, entityliving);
                                    }
                                 } catch (Exception var39) {
                                    var39.printStackTrace();
                                    return j4;
                                 }

                                 entityliving.setLocationAndAngles((double)f, (double)i3, (double)f1, p_77192_1_.rand.nextFloat() * 360.0F, 0.0F);
                                 boolean flag2 = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn(entityliving, p_77192_1_, f, (float)i3, f1) : entityliving.getCanSpawnHere() && entityliving.isNotColliding();
                                 if (flag2) {
                                    this.mapSampleEntitiesByClass.remove(biomegenbase$spawnlistentry.entityClass);
                                    if (!ReflectorForge.doSpecialSpawn(entityliving, p_77192_1_, f, i3, f1)) {
                                       ientitylivingdata = entityliving.onInitialSpawn(p_77192_1_.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                    }

                                    if (entityliving.isNotColliding()) {
                                       ++j2;
                                       p_77192_1_.spawnEntityInWorld(entityliving);
                                    }

                                    int i4 = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, entityliving) : entityliving.getMaxSpawnedInChunk();
                                    if (j2 >= i4) {
                                       continue label169;
                                    }
                                 }

                                 j4 += j2;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return j4;
      }
   }

   protected static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
      Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
      int i = x * 16 + worldIn.rand.nextInt(16);
      int j = z * 16 + worldIn.rand.nextInt(16);
      int k = MathHelper.func_154354_b(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
      int l = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
      return new BlockPos(i, l, j);
   }

   private static BlockPosM getRandomChunkPosition(World p_getRandomChunkPosition_0_, int p_getRandomChunkPosition_1_, int p_getRandomChunkPosition_2_, BlockPosM p_getRandomChunkPosition_3_) {
      Chunk chunk = p_getRandomChunkPosition_0_.getChunkFromChunkCoords(p_getRandomChunkPosition_1_, p_getRandomChunkPosition_2_);
      int i = p_getRandomChunkPosition_1_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
      int j = p_getRandomChunkPosition_2_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
      int k = MathHelper.func_154354_b(chunk.getHeightValue(i & 15, j & 15) + 1, 16);
      int l = p_getRandomChunkPosition_0_.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
      p_getRandomChunkPosition_3_.setXyz(i, l, j);
      return p_getRandomChunkPosition_3_;
   }

   public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType p_180267_0_, World worldIn, BlockPos pos) {
      if (!worldIn.getWorldBorder().contains(pos)) {
         return false;
      } else if (p_180267_0_ == null) {
         return false;
      } else {
         Block block = worldIn.getBlockState(pos).getBlock();
         if (p_180267_0_ == EntityLiving.SpawnPlacementType.IN_WATER) {
            return block.getMaterial().isLiquid() && worldIn.getBlockState(pos.down()).getBlock().getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
         } else {
            BlockPos blockpos = pos.down();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            boolean flag = Reflector.ForgeBlock_canCreatureSpawn.exists() ? Reflector.callBoolean(iblockstate.getBlock(), Reflector.ForgeBlock_canCreatureSpawn, worldIn, blockpos, p_180267_0_) : World.doesBlockHaveSolidTopSurface(worldIn, blockpos);
            if (!flag) {
               return false;
            } else {
               Block block1 = worldIn.getBlockState(blockpos).getBlock();
               boolean flag1 = block1 != Blocks.bedrock && block1 != Blocks.barrier;
               return flag1 && !block.isNormalCube() && !block.getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
            }
         }
      }
   }

   public static void performWorldGenSpawning(World worldIn, BiomeGenBase p_77191_1_, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random p_77191_6_) {
      List<BiomeGenBase.SpawnListEntry> list = p_77191_1_.getSpawnableList(EnumCreatureType.CREATURE);
      if (!list.isEmpty()) {
         while(p_77191_6_.nextFloat() < p_77191_1_.getSpawningChance()) {
            BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(worldIn.rand, list);
            int i = biomegenbase$spawnlistentry.minGroupCount + p_77191_6_.nextInt(1 + biomegenbase$spawnlistentry.maxGroupCount - biomegenbase$spawnlistentry.minGroupCount);
            IEntityLivingData ientitylivingdata = null;
            int j = p_77191_2_ + p_77191_6_.nextInt(p_77191_4_);
            int k = p_77191_3_ + p_77191_6_.nextInt(p_77191_5_);
            int l = j;
            int i1 = k;

            for(int j1 = 0; j1 < i; ++j1) {
               boolean flag = false;

               for(int k1 = 0; !flag && k1 < 4; ++k1) {
                  BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                  if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                     EntityLiving entityliving;
                     try {
                        entityliving = (EntityLiving)biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                     } catch (Exception var21) {
                        var21.printStackTrace();
                        continue;
                     }

                     if (Reflector.ForgeEventFactory_canEntitySpawn.exists()) {
                        Object object = Reflector.call(Reflector.ForgeEventFactory_canEntitySpawn, entityliving, worldIn, (float)j + 0.5F, blockpos.getY(), (float)k + 0.5F);
                        if (object == ReflectorForge.EVENT_RESULT_DENY) {
                           continue;
                        }
                     }

                     entityliving.setLocationAndAngles((double)((float)j + 0.5F), (double)blockpos.getY(), (double)((float)k + 0.5F), p_77191_6_.nextFloat() * 360.0F, 0.0F);
                     worldIn.spawnEntityInWorld(entityliving);
                     ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                     flag = true;
                  }

                  j += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);

                  for(k += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5); j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_; k = i1 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5)) {
                     j = l + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                  }
               }
            }
         }
      }

   }
}
