/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT
extends Block {
    public static final PropertyBool field_176246_a = PropertyBool.create("explode");
    private static final String __OBFID = "CL_00000324";

    public BlockTNT() {
        super(Material.tnt);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176246_a, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(field_176246_a, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(field_176246_a, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityTNTPrimed var4 = new EntityTNTPrimed(worldIn, (float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, explosionIn.getExplosivePlacedBy());
            var4.fuse = worldIn.rand.nextInt(var4.fuse / 4) + var4.fuse / 8;
            worldIn.spawnEntityInWorld(var4);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.func_180692_a(worldIn, pos, state, null);
    }

    public void func_180692_a(World worldIn, BlockPos p_180692_2_, IBlockState p_180692_3_, EntityLivingBase p_180692_4_) {
        if (!worldIn.isRemote && ((Boolean)p_180692_3_.getValue(field_176246_a)).booleanValue()) {
            EntityTNTPrimed var5 = new EntityTNTPrimed(worldIn, (float)p_180692_2_.getX() + 0.5f, (float)p_180692_2_.getY() + 0.5f, (float)p_180692_2_.getZ() + 0.5f, p_180692_4_);
            worldIn.spawnEntityInWorld(var5);
            worldIn.playSoundAtEntity(var5, "game.tnt.primed", 1.0f, 1.0f);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        Item var9;
        if (playerIn.getCurrentEquippedItem() != null && ((var9 = playerIn.getCurrentEquippedItem().getItem()) == Items.flint_and_steel || var9 == Items.fire_charge)) {
            this.func_180692_a(worldIn, pos, state.withProperty(field_176246_a, Boolean.valueOf(true)), playerIn);
            worldIn.setBlockToAir(pos);
            if (var9 == Items.flint_and_steel) {
                playerIn.getCurrentEquippedItem().damageItem(1, playerIn);
            } else if (!playerIn.capabilities.isCreativeMode) {
                --playerIn.getCurrentEquippedItem().stackSize;
            }
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        EntityArrow var5;
        if (!worldIn.isRemote && entityIn instanceof EntityArrow && (var5 = (EntityArrow)entityIn).isBurning()) {
            this.func_180692_a(worldIn, pos, worldIn.getBlockState(pos).withProperty(field_176246_a, Boolean.valueOf(true)), var5.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)var5.shootingEntity : null);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176246_a, Boolean.valueOf((meta & 1) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue(field_176246_a) != false ? 1 : 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176246_a);
    }
}

