package net.minecraft.world.gen.feature;

import net.minecraft.block.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.block.material.*;

public class WorldGenHellLava extends WorldGenerator
{
    private final boolean field_94524_b;
    private final Block field_150553_a;
    
    @Override
    public boolean generate(final World world, final Random random, final BlockPos blockPos) {
        if (world.getBlockState(blockPos.up()).getBlock() != Blocks.netherrack) {
            return "".length() != 0;
        }
        if (world.getBlockState(blockPos).getBlock().getMaterial() != Material.air && world.getBlockState(blockPos).getBlock() != Blocks.netherrack) {
            return "".length() != 0;
        }
        int length = "".length();
        if (world.getBlockState(blockPos.west()).getBlock() == Blocks.netherrack) {
            ++length;
        }
        if (world.getBlockState(blockPos.east()).getBlock() == Blocks.netherrack) {
            ++length;
        }
        if (world.getBlockState(blockPos.north()).getBlock() == Blocks.netherrack) {
            ++length;
        }
        if (world.getBlockState(blockPos.south()).getBlock() == Blocks.netherrack) {
            ++length;
        }
        if (world.getBlockState(blockPos.down()).getBlock() == Blocks.netherrack) {
            ++length;
        }
        int length2 = "".length();
        if (world.isAirBlock(blockPos.west())) {
            ++length2;
        }
        if (world.isAirBlock(blockPos.east())) {
            ++length2;
        }
        if (world.isAirBlock(blockPos.north())) {
            ++length2;
        }
        if (world.isAirBlock(blockPos.south())) {
            ++length2;
        }
        if (world.isAirBlock(blockPos.down())) {
            ++length2;
        }
        if ((!this.field_94524_b && length == (0x82 ^ 0x86) && length2 == " ".length()) || length == (0x1A ^ 0x1F)) {
            world.setBlockState(blockPos, this.field_150553_a.getDefaultState(), "  ".length());
            world.forceBlockUpdateTick(this.field_150553_a, blockPos, random);
        }
        return " ".length() != 0;
    }
    
    private static String I(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        final char[] charArray = s2.toCharArray();
        int length = "".length();
        final char[] charArray2 = s.toCharArray();
        final int length2 = charArray2.length;
        int i = "".length();
        while (i < length2) {
            sb.append((char)(charArray2[i] ^ charArray[length % charArray.length]));
            ++length;
            ++i;
            "".length();
            if (-1 < -1) {
                throw null;
            }
        }
        return sb.toString();
    }
    
    public WorldGenHellLava(final Block field_150553_a, final boolean field_94524_b) {
        this.field_150553_a = field_150553_a;
        this.field_94524_b = field_94524_b;
    }
}
