/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity.item;

import net.minecraft.block.BlockChest;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityMinecartChest
extends EntityMinecartContainer {
    private static final String __OBFID = "CL_00001671";

    public EntityMinecartChest(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartChest(World worldIn, double p_i1715_2_, double p_i1715_4_, double p_i1715_6_) {
        super(worldIn, p_i1715_2_, p_i1715_4_, p_i1715_6_);
    }

    @Override
    public void killMinecart(DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.chest), 1, 0.0f);
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public EntityMinecart.EnumMinecartType func_180456_s() {
        return EntityMinecart.EnumMinecartType.CHEST;
    }

    @Override
    public IBlockState func_180457_u() {
        return Blocks.chest.getDefaultState().withProperty(BlockChest.FACING_PROP, (Comparable)((Object)EnumFacing.NORTH));
    }

    @Override
    public int getDefaultDisplayTileOffset() {
        return 8;
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }
}

