/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.block;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class BlockDynamicLiquid
extends BlockLiquid {
    int field_149815_a;
    private static final String __OBFID = "CL_00000234";

    protected BlockDynamicLiquid(Material p_i45403_1_) {
        super(p_i45403_1_);
    }

    private void func_180690_f(World worldIn, BlockPos p_180690_2_, IBlockState p_180690_3_) {
        worldIn.setBlockState(p_180690_2_, BlockDynamicLiquid.getStaticLiquidForMaterial(this.blockMaterial).getDefaultState().withProperty(LEVEL, p_180690_3_.getValue(LEVEL)), 2);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var16;
        int var5 = (Integer)state.getValue(LEVEL);
        int var6 = 1;
        if (this.blockMaterial == Material.lava && !worldIn.provider.func_177500_n()) {
            var6 = 2;
        }
        int var7 = this.tickRate(worldIn);
        if (var5 > 0) {
            int var8 = -100;
            this.field_149815_a = 0;
            for (EnumFacing var10 : EnumFacing.Plane.HORIZONTAL) {
                var8 = this.func_176371_a(worldIn, pos.offset(var10), var8);
            }
            int var14 = var8 + var6;
            if (var14 >= 8 || var8 < 0) {
                var14 = -1;
            }
            if (this.func_176362_e(worldIn, pos.offsetUp()) >= 0) {
                var16 = this.func_176362_e(worldIn, pos.offsetUp());
                var14 = var16 >= 8 ? var16 : var16 + 8;
            }
            if (this.field_149815_a >= 2 && this.blockMaterial == Material.water) {
                IBlockState var17 = worldIn.getBlockState(pos.offsetDown());
                if (var17.getBlock().getMaterial().isSolid()) {
                    var14 = 0;
                } else if (var17.getBlock().getMaterial() == this.blockMaterial && (Integer)var17.getValue(LEVEL) == 0) {
                    var14 = 0;
                }
            }
            if (this.blockMaterial == Material.lava && var5 < 8 && var14 < 8 && var14 > var5 && rand.nextInt(4) != 0) {
                var7 *= 4;
            }
            if (var14 == var5) {
                this.func_180690_f(worldIn, pos, state);
            } else {
                var5 = var14;
                if (var14 < 0) {
                    worldIn.setBlockToAir(pos);
                } else {
                    state = state.withProperty(LEVEL, Integer.valueOf(var14));
                    worldIn.setBlockState(pos, state, 2);
                    worldIn.scheduleUpdate(pos, this, var7);
                    worldIn.notifyNeighborsOfStateChange(pos, this);
                }
            }
        } else {
            this.func_180690_f(worldIn, pos, state);
        }
        IBlockState var13 = worldIn.getBlockState(pos.offsetDown());
        if (this.func_176373_h(worldIn, pos.offsetDown(), var13)) {
            if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial() == Material.water) {
                worldIn.setBlockState(pos.offsetDown(), Blocks.stone.getDefaultState());
                this.func_180688_d(worldIn, pos.offsetDown());
                return;
            }
            if (var5 >= 8) {
                this.func_176375_a(worldIn, pos.offsetDown(), var13, var5);
            } else {
                this.func_176375_a(worldIn, pos.offsetDown(), var13, var5 + 8);
            }
        } else if (var5 >= 0 && (var5 == 0 || this.func_176372_g(worldIn, pos.offsetDown(), var13))) {
            Set var15 = this.func_176376_e(worldIn, pos);
            var16 = var5 + var6;
            if (var5 >= 8) {
                var16 = 1;
            }
            if (var16 >= 8) {
                return;
            }
            for (EnumFacing var12 : var15) {
                this.func_176375_a(worldIn, pos.offset(var12), worldIn.getBlockState(pos.offset(var12)), var16);
            }
        }
    }

    private void func_176375_a(World worldIn, BlockPos p_176375_2_, IBlockState p_176375_3_, int p_176375_4_) {
        if (this.func_176373_h(worldIn, p_176375_2_, p_176375_3_)) {
            if (p_176375_3_.getBlock() != Blocks.air) {
                if (this.blockMaterial == Material.lava) {
                    this.func_180688_d(worldIn, p_176375_2_);
                } else {
                    p_176375_3_.getBlock().dropBlockAsItem(worldIn, p_176375_2_, p_176375_3_, 0);
                }
            }
            worldIn.setBlockState(p_176375_2_, this.getDefaultState().withProperty(LEVEL, Integer.valueOf(p_176375_4_)), 3);
        }
    }

    private int func_176374_a(World worldIn, BlockPos p_176374_2_, int p_176374_3_, EnumFacing p_176374_4_) {
        int var5 = 1000;
        for (EnumFacing var7 : EnumFacing.Plane.HORIZONTAL) {
            IBlockState var9;
            int var10;
            BlockPos var8;
            if (var7 == p_176374_4_ || this.func_176372_g(worldIn, var8 = p_176374_2_.offset(var7), var9 = worldIn.getBlockState(var8)) || var9.getBlock().getMaterial() == this.blockMaterial && (Integer)var9.getValue(LEVEL) <= 0) continue;
            if (!this.func_176372_g(worldIn, var8.offsetDown(), var9)) {
                return p_176374_3_;
            }
            if (p_176374_3_ >= 4 || (var10 = this.func_176374_a(worldIn, var8, p_176374_3_ + 1, var7.getOpposite())) >= var5) continue;
            var5 = var10;
        }
        return var5;
    }

    private Set func_176376_e(World worldIn, BlockPos p_176376_2_) {
        int var3 = 1000;
        EnumSet<EnumFacing> var4 = EnumSet.noneOf(EnumFacing.class);
        for (EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            IBlockState var8;
            BlockPos var7 = p_176376_2_.offset(var6);
            if (this.func_176372_g(worldIn, var7, var8 = worldIn.getBlockState(var7)) || var8.getBlock().getMaterial() == this.blockMaterial && (Integer)var8.getValue(LEVEL) <= 0) continue;
            int var9 = this.func_176372_g(worldIn, var7.offsetDown(), worldIn.getBlockState(var7.offsetDown())) ? this.func_176374_a(worldIn, var7, 1, var6.getOpposite()) : 0;
            if (var9 < var3) {
                var4.clear();
            }
            if (var9 > var3) continue;
            var4.add(var6);
            var3 = var9;
        }
        return var4;
    }

    private boolean func_176372_g(World worldIn, BlockPos p_176372_2_, IBlockState p_176372_3_) {
        Block var4 = worldIn.getBlockState(p_176372_2_).getBlock();
        return !(var4 instanceof BlockDoor) && var4 != Blocks.standing_sign && var4 != Blocks.ladder && var4 != Blocks.reeds ? (var4.blockMaterial == Material.portal ? true : var4.blockMaterial.blocksMovement()) : true;
    }

    protected int func_176371_a(World worldIn, BlockPos p_176371_2_, int p_176371_3_) {
        int var4 = this.func_176362_e(worldIn, p_176371_2_);
        if (var4 < 0) {
            return p_176371_3_;
        }
        if (var4 == 0) {
            ++this.field_149815_a;
        }
        if (var4 >= 8) {
            var4 = 0;
        }
        return p_176371_3_ >= 0 && var4 >= p_176371_3_ ? p_176371_3_ : var4;
    }

    private boolean func_176373_h(World worldIn, BlockPos p_176373_2_, IBlockState p_176373_3_) {
        Material var4 = p_176373_3_.getBlock().getMaterial();
        return var4 != this.blockMaterial && var4 != Material.lava && !this.func_176372_g(worldIn, p_176373_2_, p_176373_3_);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.func_176365_e(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
}

