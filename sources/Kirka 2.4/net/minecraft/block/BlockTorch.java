/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTorch
extends Block {
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", new Predicate(){
        private static final String __OBFID = "CL_00002054";

        public boolean func_176601_a(EnumFacing p_176601_1_) {
            return p_176601_1_ != EnumFacing.DOWN;
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_176601_a((EnumFacing)p_apply_1_);
        }
    });
    private static final String __OBFID = "CL_00000325";

    protected BlockTorch() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.UP)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    private boolean func_176594_d(World worldIn, BlockPos p_176594_2_) {
        if (World.doesBlockHaveSolidTopSurface(worldIn, p_176594_2_)) {
            return true;
        }
        Block var3 = worldIn.getBlockState(p_176594_2_).getBlock();
        return var3 instanceof BlockFence || var3 == Blocks.glass || var3 == Blocks.cobblestone_wall || var3 == Blocks.stained_glass;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        EnumFacing var4;
        Iterator var3 = FACING_PROP.getAllowedValues().iterator();
        do {
            if (var3.hasNext()) continue;
            return false;
        } while (!this.func_176595_b(worldIn, pos, var4 = (EnumFacing)var3.next()));
        return true;
    }

    private boolean func_176595_b(World worldIn, BlockPos p_176595_2_, EnumFacing p_176595_3_) {
        BlockPos var4 = p_176595_2_.offset(p_176595_3_.getOpposite());
        boolean var5 = p_176595_3_.getAxis().isHorizontal();
        return var5 && worldIn.func_175677_d(var4, true) || p_176595_3_.equals(EnumFacing.UP) && this.func_176594_d(worldIn, var4);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing var10;
        if (this.func_176595_b(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(FACING_PROP, (Comparable)((Object)facing));
        }
        Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (var9.hasNext()) continue;
            return this.getDefaultState();
        } while (!worldIn.func_175677_d(pos.offset((var10 = (EnumFacing)var9.next()).getOpposite()), true));
        return this.getDefaultState().withProperty(FACING_PROP, (Comparable)((Object)var10));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.func_176593_f(worldIn, pos, state);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.func_176592_e(worldIn, pos, state);
    }

    protected boolean func_176592_e(World worldIn, BlockPos p_176592_2_, IBlockState p_176592_3_) {
        if (!this.func_176593_f(worldIn, p_176592_2_, p_176592_3_)) {
            return true;
        }
        EnumFacing var4 = (EnumFacing)((Object)p_176592_3_.getValue(FACING_PROP));
        EnumFacing.Axis var5 = var4.getAxis();
        EnumFacing var6 = var4.getOpposite();
        boolean var7 = false;
        if (var5.isHorizontal() && !worldIn.func_175677_d(p_176592_2_.offset(var6), true)) {
            var7 = true;
        } else if (var5.isVertical() && !this.func_176594_d(worldIn, p_176592_2_.offset(var6))) {
            var7 = true;
        }
        if (var7) {
            this.dropBlockAsItem(worldIn, p_176592_2_, p_176592_3_, 0);
            worldIn.setBlockToAir(p_176592_2_);
            return true;
        }
        return false;
    }

    protected boolean func_176593_f(World worldIn, BlockPos p_176593_2_, IBlockState p_176593_3_) {
        if (p_176593_3_.getBlock() == this && this.func_176595_b(worldIn, p_176593_2_, (EnumFacing)((Object)p_176593_3_.getValue(FACING_PROP)))) {
            return true;
        }
        if (worldIn.getBlockState(p_176593_2_).getBlock() == this) {
            this.dropBlockAsItem(worldIn, p_176593_2_, p_176593_3_, 0);
            worldIn.setBlockToAir(p_176593_2_);
        }
        return false;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        EnumFacing var5 = (EnumFacing)((Object)worldIn.getBlockState(pos).getValue(FACING_PROP));
        float var6 = 0.15f;
        if (var5 == EnumFacing.EAST) {
            this.setBlockBounds(0.0f, 0.2f, 0.5f - var6, var6 * 2.0f, 0.8f, 0.5f + var6);
        } else if (var5 == EnumFacing.WEST) {
            this.setBlockBounds(1.0f - var6 * 2.0f, 0.2f, 0.5f - var6, 1.0f, 0.8f, 0.5f + var6);
        } else if (var5 == EnumFacing.SOUTH) {
            this.setBlockBounds(0.5f - var6, 0.2f, 0.0f, 0.5f + var6, 0.8f, var6 * 2.0f);
        } else if (var5 == EnumFacing.NORTH) {
            this.setBlockBounds(0.5f - var6, 0.2f, 1.0f - var6 * 2.0f, 0.5f + var6, 0.8f, 1.0f);
        } else {
            var6 = 0.1f;
            this.setBlockBounds(0.5f - var6, 0.0f, 0.5f - var6, 0.5f + var6, 0.6f, 0.5f + var6);
        }
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        EnumFacing var5 = (EnumFacing)((Object)state.getValue(FACING_PROP));
        double var6 = (double)pos.getX() + 0.5;
        double var8 = (double)pos.getY() + 0.7;
        double var10 = (double)pos.getZ() + 0.5;
        double var12 = 0.22;
        double var14 = 0.27;
        if (var5.getAxis().isHorizontal()) {
            EnumFacing var16 = var5.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var14 * (double)var16.getFrontOffsetX(), var8 + var12, var10 + var14 * (double)var16.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var14 * (double)var16.getFrontOffsetX(), var8 + var12, var10 + var14 * (double)var16.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
        } else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6, var8, var10, 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, var6, var8, var10, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState();
        switch (meta) {
            case 1: {
                var2 = var2.withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.EAST));
                break;
            }
            case 2: {
                var2 = var2.withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.WEST));
                break;
            }
            case 3: {
                var2 = var2.withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.SOUTH));
                break;
            }
            case 4: {
                var2 = var2.withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.NORTH));
                break;
            }
            default: {
                var2 = var2.withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.UP));
            }
        }
        return var2;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var3;
        int var2 = 0;
        switch (SwitchEnumFacing.field_176609_a[((EnumFacing)((Object)state.getValue(FACING_PROP))).ordinal()]) {
            case 1: {
                var3 = var2 | true;
                break;
            }
            case 2: {
                var3 = var2 | 2;
                break;
            }
            case 3: {
                var3 = var2 | 3;
                break;
            }
            case 4: {
                var3 = var2 | 4;
                break;
            }
            default: {
                var3 = var2 | 5;
            }
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING_PROP);
    }

    static final class SwitchEnumFacing {
        static final int[] field_176609_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002053";

        static {
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.DOWN.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.UP.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }

}

