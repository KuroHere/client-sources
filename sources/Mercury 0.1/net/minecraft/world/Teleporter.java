/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.world;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

public class Teleporter {
    private final WorldServer worldServerInstance;
    private final Random random;
    private final LongHashMap destinationCoordinateCache = new LongHashMap();
    private final List destinationCoordinateKeys = Lists.newArrayList();
    private static final String __OBFID = "CL_00000153";

    public Teleporter(WorldServer worldIn) {
        this.worldServerInstance = worldIn;
        this.random = new Random(worldIn.getSeed());
    }

    public void func_180266_a(Entity p_180266_1_, float p_180266_2_) {
        if (this.worldServerInstance.provider.getDimensionId() != 1) {
            if (!this.func_180620_b(p_180266_1_, p_180266_2_)) {
                this.makePortal(p_180266_1_);
                this.func_180620_b(p_180266_1_, p_180266_2_);
            }
        } else {
            int var3 = MathHelper.floor_double(p_180266_1_.posX);
            int var4 = MathHelper.floor_double(p_180266_1_.posY) - 1;
            int var5 = MathHelper.floor_double(p_180266_1_.posZ);
            int var6 = 1;
            int var7 = 0;
            for (int var8 = -2; var8 <= 2; ++var8) {
                for (int var9 = -2; var9 <= 2; ++var9) {
                    for (int var10 = -1; var10 < 3; ++var10) {
                        int var11 = var3 + var9 * var6 + var8 * var7;
                        int var12 = var4 + var10;
                        int var13 = var5 + var9 * var7 - var8 * var6;
                        boolean var14 = var10 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(var11, var12, var13), var14 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }
            p_180266_1_.setLocationAndAngles(var3, var4, var5, p_180266_1_.rotationYaw, 0.0f);
            p_180266_1_.motionZ = 0.0;
            p_180266_1_.motionY = 0.0;
            p_180266_1_.motionX = 0.0;
        }
    }

    public boolean func_180620_b(Entity p_180620_1_, float p_180620_2_) {
        boolean var3 = true;
        double var4 = -1.0;
        int var6 = MathHelper.floor_double(p_180620_1_.posX);
        int var7 = MathHelper.floor_double(p_180620_1_.posZ);
        boolean var8 = true;
        BlockPos var9 = BlockPos.ORIGIN;
        long var10 = ChunkCoordIntPair.chunkXZ2Int(var6, var7);
        if (this.destinationCoordinateCache.containsItem(var10)) {
            PortalPosition var12 = (PortalPosition)this.destinationCoordinateCache.getValueByKey(var10);
            var4 = 0.0;
            var9 = var12;
            var12.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            var8 = false;
        } else {
            BlockPos var34 = new BlockPos(p_180620_1_);
            for (int var13 = -128; var13 <= 128; ++var13) {
                for (int var14 = -128; var14 <= 128; ++var14) {
                    BlockPos var15 = var34.add(var13, this.worldServerInstance.getActualHeight() - 1 - var34.getY(), var14);
                    while (var15.getY() >= 0) {
                        BlockPos var16 = var15.offsetDown();
                        if (this.worldServerInstance.getBlockState(var15).getBlock() == Blocks.portal) {
                            while (this.worldServerInstance.getBlockState(var16 = var15.offsetDown()).getBlock() == Blocks.portal) {
                                var15 = var16;
                            }
                            double var17 = var15.distanceSq(var34);
                            if (var4 < 0.0 || var17 < var4) {
                                var4 = var17;
                                var9 = var15;
                            }
                        }
                        var15 = var16;
                    }
                }
            }
        }
        if (var4 >= 0.0) {
            if (var8) {
                this.destinationCoordinateCache.add(var10, new PortalPosition(var9, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(var10);
            }
            double var35 = (double)var9.getX() + 0.5;
            double var36 = (double)var9.getY() + 0.5;
            double var37 = (double)var9.getZ() + 0.5;
            EnumFacing var18 = null;
            if (this.worldServerInstance.getBlockState(var9.offsetWest()).getBlock() == Blocks.portal) {
                var18 = EnumFacing.NORTH;
            }
            if (this.worldServerInstance.getBlockState(var9.offsetEast()).getBlock() == Blocks.portal) {
                var18 = EnumFacing.SOUTH;
            }
            if (this.worldServerInstance.getBlockState(var9.offsetNorth()).getBlock() == Blocks.portal) {
                var18 = EnumFacing.EAST;
            }
            if (this.worldServerInstance.getBlockState(var9.offsetSouth()).getBlock() == Blocks.portal) {
                var18 = EnumFacing.WEST;
            }
            EnumFacing var19 = EnumFacing.getHorizontal(p_180620_1_.getTeleportDirection());
            if (var18 != null) {
                EnumFacing var20 = var18.rotateYCCW();
                BlockPos var21 = var9.offset(var18);
                boolean var22 = this.func_180265_a(var21);
                boolean var23 = this.func_180265_a(var21.offset(var20));
                if (var23 && var22) {
                    var9 = var9.offset(var20);
                    var18 = var18.getOpposite();
                    var20 = var20.getOpposite();
                    BlockPos var24 = var9.offset(var18);
                    var22 = this.func_180265_a(var24);
                    var23 = this.func_180265_a(var24.offset(var20));
                }
                float var38 = 0.5f;
                float var25 = 0.5f;
                if (!var23 && var22) {
                    var38 = 1.0f;
                } else if (var23 && !var22) {
                    var38 = 0.0f;
                } else if (var23) {
                    var25 = 0.0f;
                }
                var35 = (double)var9.getX() + 0.5;
                var36 = (double)var9.getY() + 0.5;
                var37 = (double)var9.getZ() + 0.5;
                var35 += (double)((float)var20.getFrontOffsetX() * var38 + (float)var18.getFrontOffsetX() * var25);
                var37 += (double)((float)var20.getFrontOffsetZ() * var38 + (float)var18.getFrontOffsetZ() * var25);
                float var26 = 0.0f;
                float var27 = 0.0f;
                float var28 = 0.0f;
                float var29 = 0.0f;
                if (var18 == var19) {
                    var26 = 1.0f;
                    var27 = 1.0f;
                } else if (var18 == var19.getOpposite()) {
                    var26 = -1.0f;
                    var27 = -1.0f;
                } else if (var18 == var19.rotateY()) {
                    var28 = 1.0f;
                    var29 = -1.0f;
                } else {
                    var28 = -1.0f;
                    var29 = 1.0f;
                }
                double var30 = p_180620_1_.motionX;
                double var32 = p_180620_1_.motionZ;
                p_180620_1_.motionX = var30 * (double)var26 + var32 * (double)var29;
                p_180620_1_.motionZ = var30 * (double)var28 + var32 * (double)var27;
                p_180620_1_.rotationYaw = p_180620_2_ - (float)(var19.getHorizontalIndex() * 90) + (float)(var18.getHorizontalIndex() * 90);
            } else {
                p_180620_1_.motionZ = 0.0;
                p_180620_1_.motionY = 0.0;
                p_180620_1_.motionX = 0.0;
            }
            p_180620_1_.setLocationAndAngles(var35, var36, var37, p_180620_1_.rotationYaw, p_180620_1_.rotationPitch);
            return true;
        }
        return false;
    }

    private boolean func_180265_a(BlockPos p_180265_1_) {
        return !this.worldServerInstance.isAirBlock(p_180265_1_) || !this.worldServerInstance.isAirBlock(p_180265_1_.offsetUp());
    }

    public boolean makePortal(Entity p_85188_1_) {
        double var32;
        int var22;
        int var13;
        double var14;
        int var26;
        double var33;
        int var25;
        int var24;
        int var23;
        int var21;
        int var20;
        double var17;
        int var16;
        int var27;
        int var19;
        int var2 = 16;
        double var3 = -1.0;
        int var5 = MathHelper.floor_double(p_85188_1_.posX);
        int var6 = MathHelper.floor_double(p_85188_1_.posY);
        int var7 = MathHelper.floor_double(p_85188_1_.posZ);
        int var8 = var5;
        int var9 = var6;
        int var10 = var7;
        int var11 = 0;
        int var12 = this.random.nextInt(4);
        for (var13 = var5 - var2; var13 <= var5 + var2; ++var13) {
            var14 = (double)var13 + 0.5 - p_85188_1_.posX;
            for (var16 = var7 - var2; var16 <= var7 + var2; ++var16) {
                var17 = (double)var16 + 0.5 - p_85188_1_.posZ;
                block2 : for (var19 = this.worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19) {
                    if (!this.worldServerInstance.isAirBlock(new BlockPos(var13, var19, var16))) continue;
                    while (var19 > 0 && this.worldServerInstance.isAirBlock(new BlockPos(var13, var19 - 1, var16))) {
                        --var19;
                    }
                    for (var20 = var12; var20 < var12 + 4; ++var20) {
                        var21 = var20 % 2;
                        var22 = 1 - var21;
                        if (var20 % 4 >= 2) {
                            var21 = -var21;
                            var22 = -var22;
                        }
                        for (var23 = 0; var23 < 3; ++var23) {
                            for (var24 = 0; var24 < 4; ++var24) {
                                for (var25 = -1; var25 < 4; ++var25) {
                                    var26 = var13 + (var24 - 1) * var21 + var23 * var22;
                                    var27 = var19 + var25;
                                    int var28 = var16 + (var24 - 1) * var22 - var23 * var21;
                                    if (var25 < 0 && !this.worldServerInstance.getBlockState(new BlockPos(var26, var27, var28)).getBlock().getMaterial().isSolid() || var25 >= 0 && !this.worldServerInstance.isAirBlock(new BlockPos(var26, var27, var28))) continue block2;
                                }
                            }
                        }
                        var32 = (double)var19 + 0.5 - p_85188_1_.posY;
                        var33 = var14 * var14 + var32 * var32 + var17 * var17;
                        if (!(var3 < 0.0) && !(var33 < var3)) continue;
                        var3 = var33;
                        var8 = var13;
                        var9 = var19;
                        var10 = var16;
                        var11 = var20 % 4;
                    }
                }
            }
        }
        if (var3 < 0.0) {
            for (var13 = var5 - var2; var13 <= var5 + var2; ++var13) {
                var14 = (double)var13 + 0.5 - p_85188_1_.posX;
                for (var16 = var7 - var2; var16 <= var7 + var2; ++var16) {
                    var17 = (double)var16 + 0.5 - p_85188_1_.posZ;
                    block10 : for (var19 = this.worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19) {
                        if (!this.worldServerInstance.isAirBlock(new BlockPos(var13, var19, var16))) continue;
                        while (var19 > 0 && this.worldServerInstance.isAirBlock(new BlockPos(var13, var19 - 1, var16))) {
                            --var19;
                        }
                        for (var20 = var12; var20 < var12 + 2; ++var20) {
                            var21 = var20 % 2;
                            var22 = 1 - var21;
                            for (var23 = 0; var23 < 4; ++var23) {
                                for (var24 = -1; var24 < 4; ++var24) {
                                    var25 = var13 + (var23 - 1) * var21;
                                    var26 = var19 + var24;
                                    var27 = var16 + (var23 - 1) * var22;
                                    if (var24 < 0 && !this.worldServerInstance.getBlockState(new BlockPos(var25, var26, var27)).getBlock().getMaterial().isSolid() || var24 >= 0 && !this.worldServerInstance.isAirBlock(new BlockPos(var25, var26, var27))) continue block10;
                                }
                            }
                            var32 = (double)var19 + 0.5 - p_85188_1_.posY;
                            var33 = var14 * var14 + var32 * var32 + var17 * var17;
                            if (!(var3 < 0.0) && !(var33 < var3)) continue;
                            var3 = var33;
                            var8 = var13;
                            var9 = var19;
                            var10 = var16;
                            var11 = var20 % 2;
                        }
                    }
                }
            }
        }
        int var29 = var8;
        int var15 = var9;
        var16 = var10;
        int var30 = var11 % 2;
        int var18 = 1 - var30;
        if (var11 % 4 >= 2) {
            var30 = -var30;
            var18 = -var18;
        }
        if (var3 < 0.0) {
            var15 = var9 = MathHelper.clamp_int(var9, 70, this.worldServerInstance.getActualHeight() - 10);
            for (var19 = -1; var19 <= 1; ++var19) {
                for (var20 = 1; var20 < 3; ++var20) {
                    for (var21 = -1; var21 < 3; ++var21) {
                        var22 = var29 + (var20 - 1) * var30 + var19 * var18;
                        var23 = var15 + var21;
                        var24 = var16 + (var20 - 1) * var18 - var19 * var30;
                        boolean var34 = var21 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(var22, var23, var24), var34 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }
        }
        IBlockState var31 = Blocks.portal.getDefaultState().withProperty(BlockPortal.field_176550_a, (Comparable)((Object)(var30 != 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z)));
        for (var20 = 0; var20 < 4; ++var20) {
            for (var21 = 0; var21 < 4; ++var21) {
                for (var22 = -1; var22 < 4; ++var22) {
                    var23 = var29 + (var21 - 1) * var30;
                    var24 = var15 + var22;
                    var25 = var16 + (var21 - 1) * var18;
                    boolean var35 = var21 == 0 || var21 == 3 || var22 == -1 || var22 == 3;
                    this.worldServerInstance.setBlockState(new BlockPos(var23, var24, var25), var35 ? Blocks.obsidian.getDefaultState() : var31, 2);
                }
            }
            for (var21 = 0; var21 < 4; ++var21) {
                for (var22 = -1; var22 < 4; ++var22) {
                    var23 = var29 + (var21 - 1) * var30;
                    var24 = var15 + var22;
                    var25 = var16 + (var21 - 1) * var18;
                    this.worldServerInstance.notifyNeighborsOfStateChange(new BlockPos(var23, var24, var25), this.worldServerInstance.getBlockState(new BlockPos(var23, var24, var25)).getBlock());
                }
            }
        }
        return true;
    }

    public void removeStalePortalLocations(long p_85189_1_) {
        if (p_85189_1_ % 100L == 0L) {
            Iterator var3 = this.destinationCoordinateKeys.iterator();
            long var4 = p_85189_1_ - 600L;
            while (var3.hasNext()) {
                Long var6 = (Long)var3.next();
                PortalPosition var7 = (PortalPosition)this.destinationCoordinateCache.getValueByKey(var6);
                if (var7 != null && var7.lastUpdateTime >= var4) continue;
                var3.remove();
                this.destinationCoordinateCache.remove(var6);
            }
        }
    }

    public class PortalPosition
    extends BlockPos {
        public long lastUpdateTime;
        private static final String __OBFID = "CL_00000154";

        public PortalPosition(BlockPos p_i45747_2_, long p_i45747_3_) {
            super(p_i45747_2_.getX(), p_i45747_2_.getY(), p_i45747_2_.getZ());
            this.lastUpdateTime = p_i45747_3_;
        }
    }

}

