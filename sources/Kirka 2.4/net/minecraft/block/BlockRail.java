/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRail
extends BlockRailBase {
    public static final PropertyEnum field_176565_b = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);
    private static final String __OBFID = "CL_00000293";

    protected BlockRail() {
        super(false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176565_b, (Comparable)((Object)BlockRailBase.EnumRailDirection.NORTH_SOUTH)));
    }

    @Override
    protected void func_176561_b(World worldIn, BlockPos p_176561_2_, IBlockState p_176561_3_, Block p_176561_4_) {
        if (p_176561_4_.canProvidePower() && new BlockRailBase.Rail(this, worldIn, p_176561_2_, p_176561_3_).countAdjacentRails() == 3) {
            this.func_176564_a(worldIn, p_176561_2_, p_176561_3_, false);
        }
    }

    @Override
    public IProperty func_176560_l() {
        return field_176565_b;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176565_b, (Comparable)((Object)BlockRailBase.EnumRailDirection.func_177016_a(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockRailBase.EnumRailDirection)((Object)state.getValue(field_176565_b))).func_177015_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176565_b);
    }
}

