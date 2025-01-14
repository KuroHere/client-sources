/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull
extends BlockContainer {
    public static final PropertyDirection field_176418_a = PropertyDirection.create("facing");
    public static final PropertyBool field_176417_b = PropertyBool.create("nodrop");
    private static final Predicate field_176419_M = new Predicate(){
        private static final String __OBFID = "CL_00002065";

        public boolean func_177062_a(BlockWorldState p_177062_1_) {
            return p_177062_1_.func_177509_a().getBlock() == Blocks.skull && p_177062_1_.func_177507_b() instanceof TileEntitySkull && ((TileEntitySkull)p_177062_1_.func_177507_b()).getSkullType() == 1;
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_177062_a((BlockWorldState)p_apply_1_);
        }
    };
    private BlockPattern field_176420_N;
    private BlockPattern field_176421_O;
    private static final String __OBFID = "CL_00000307";

    protected BlockSkull() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176418_a, (Comparable)((Object)EnumFacing.NORTH)).withProperty(field_176417_b, Boolean.valueOf(false)));
        this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        switch (SwitchEnumFacing.field_177063_a[((EnumFacing)((Object)access.getBlockState(pos).getValue(field_176418_a))).ordinal()]) {
            default: {
                this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
                break;
            }
            case 2: {
                this.setBlockBounds(0.25f, 0.25f, 0.5f, 0.75f, 0.75f, 1.0f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 0.5f);
                break;
            }
            case 4: {
                this.setBlockBounds(0.5f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
                break;
            }
            case 5: {
                this.setBlockBounds(0.0f, 0.25f, 0.25f, 0.5f, 0.75f, 0.75f);
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(field_176418_a, (Comparable)((Object)placer.func_174811_aO())).withProperty(field_176417_b, Boolean.valueOf(false));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySkull();
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.skull;
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        TileEntity var3 = worldIn.getTileEntity(pos);
        return var3 instanceof TileEntitySkull ? ((TileEntitySkull)var3).getSkullType() : super.getDamageValue(worldIn, pos);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if (playerIn.capabilities.isCreativeMode) {
            state = state.withProperty(field_176417_b, Boolean.valueOf(true));
            worldIn.setBlockState(pos, state, 4);
        }
        super.onBlockHarvested(worldIn, pos, state, playerIn);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            TileEntity var4;
            if (!((Boolean)state.getValue(field_176417_b)).booleanValue() && (var4 = worldIn.getTileEntity(pos)) instanceof TileEntitySkull) {
                TileEntitySkull var5 = (TileEntitySkull)var4;
                ItemStack var6 = new ItemStack(Items.skull, 1, this.getDamageValue(worldIn, pos));
                if (var5.getSkullType() == 3 && var5.getPlayerProfile() != null) {
                    var6.setTagCompound(new NBTTagCompound());
                    NBTTagCompound var7 = new NBTTagCompound();
                    NBTUtil.writeGameProfile(var7, var5.getPlayerProfile());
                    var6.getTagCompound().setTag("SkullOwner", var7);
                }
                BlockSkull.spawnAsEntity(worldIn, pos, var6);
            }
            super.breakBlock(worldIn, pos, state);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.skull;
    }

    public boolean func_176415_b(World worldIn, BlockPos p_176415_2_, ItemStack p_176415_3_) {
        return p_176415_3_.getMetadata() == 1 && p_176415_2_.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote ? this.func_176414_j().func_177681_a(worldIn, p_176415_2_) != null : false;
    }

    public void func_180679_a(World worldIn, BlockPos p_180679_2_, TileEntitySkull p_180679_3_) {
        BlockPattern var4;
        BlockPattern.PatternHelper var5;
        if (p_180679_3_.getSkullType() == 1 && p_180679_2_.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote && (var5 = (var4 = this.func_176416_l()).func_177681_a(worldIn, p_180679_2_)) != null) {
            int var16;
            int var6;
            for (var6 = 0; var6 < 3; ++var6) {
                BlockWorldState var7 = var5.func_177670_a(var6, 0, 0);
                worldIn.setBlockState(var7.getPos(), var7.func_177509_a().withProperty(field_176417_b, Boolean.valueOf(true)), 2);
            }
            for (var6 = 0; var6 < var4.func_177684_c(); ++var6) {
                for (int var13 = 0; var13 < var4.func_177685_b(); ++var13) {
                    BlockWorldState var8 = var5.func_177670_a(var6, var13, 0);
                    worldIn.setBlockState(var8.getPos(), Blocks.air.getDefaultState(), 2);
                }
            }
            BlockPos var12 = var5.func_177670_a(1, 0, 0).getPos();
            EntityWither var14 = new EntityWither(worldIn);
            BlockPos var15 = var5.func_177670_a(1, 2, 0).getPos();
            var14.setLocationAndAngles((double)var15.getX() + 0.5, (double)var15.getY() + 0.55, (double)var15.getZ() + 0.5, var5.func_177669_b().getAxis() == EnumFacing.Axis.X ? 0.0f : 90.0f, 0.0f);
            var14.renderYawOffset = var5.func_177669_b().getAxis() == EnumFacing.Axis.X ? 0.0f : 90.0f;
            var14.func_82206_m();
            for (EntityPlayer var10 : worldIn.getEntitiesWithinAABB(EntityPlayer.class, var14.getEntityBoundingBox().expand(50.0, 50.0, 50.0))) {
                var10.triggerAchievement(AchievementList.spawnWither);
            }
            worldIn.spawnEntityInWorld(var14);
            for (var16 = 0; var16 < 120; ++var16) {
                worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double)var12.getX() + worldIn.rand.nextDouble(), (double)(var12.getY() - 2) + worldIn.rand.nextDouble() * 3.9, (double)var12.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
            }
            for (var16 = 0; var16 < var4.func_177684_c(); ++var16) {
                for (int var17 = 0; var17 < var4.func_177685_b(); ++var17) {
                    BlockWorldState var11 = var5.func_177670_a(var16, var17, 0);
                    worldIn.func_175722_b(var11.getPos(), Blocks.air);
                }
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176418_a, (Comparable)((Object)EnumFacing.getFront(meta & 7))).withProperty(field_176417_b, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(field_176418_a))).getIndex();
        if (((Boolean)state.getValue(field_176417_b)).booleanValue()) {
            var3 |= 8;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176418_a, field_176417_b);
    }

    protected BlockPattern func_176414_j() {
        if (this.field_176420_N == null) {
            this.field_176420_N = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.field_176420_N;
    }

    protected BlockPattern func_176416_l() {
        if (this.field_176421_O == null) {
            this.field_176421_O = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('^', field_176419_M).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.field_176421_O;
    }

    static final class SwitchEnumFacing {
        static final int[] field_177063_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002064";

        static {
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177063_a[EnumFacing.EAST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }

}

