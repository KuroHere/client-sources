package net.minecraft.src;

import java.util.*;

public class WorldGenGlowStone1 extends WorldGenerator
{
    @Override
    public boolean generate(final World par1World, final Random par2Random, final int par3, final int par4, final int par5) {
        if (!par1World.isAirBlock(par3, par4, par5)) {
            return false;
        }
        if (par1World.getBlockId(par3, par4 + 1, par5) != Block.netherrack.blockID) {
            return false;
        }
        par1World.setBlock(par3, par4, par5, Block.glowStone.blockID, 0, 2);
        for (int var6 = 0; var6 < 1500; ++var6) {
            final int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            final int var8 = par4 - par2Random.nextInt(12);
            final int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            if (par1World.getBlockId(var7, var8, var9) == 0) {
                int var10 = 0;
                for (int var11 = 0; var11 < 6; ++var11) {
                    int var12 = 0;
                    if (var11 == 0) {
                        var12 = par1World.getBlockId(var7 - 1, var8, var9);
                    }
                    if (var11 == 1) {
                        var12 = par1World.getBlockId(var7 + 1, var8, var9);
                    }
                    if (var11 == 2) {
                        var12 = par1World.getBlockId(var7, var8 - 1, var9);
                    }
                    if (var11 == 3) {
                        var12 = par1World.getBlockId(var7, var8 + 1, var9);
                    }
                    if (var11 == 4) {
                        var12 = par1World.getBlockId(var7, var8, var9 - 1);
                    }
                    if (var11 == 5) {
                        var12 = par1World.getBlockId(var7, var8, var9 + 1);
                    }
                    if (var12 == Block.glowStone.blockID) {
                        ++var10;
                    }
                }
                if (var10 == 1) {
                    par1World.setBlock(var7, var8, var9, Block.glowStone.blockID, 0, 2);
                }
            }
        }
        return true;
    }
}
