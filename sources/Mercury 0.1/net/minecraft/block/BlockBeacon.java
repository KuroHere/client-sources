/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.block;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class BlockBeacon
extends BlockContainer {
    private static final String __OBFID = "CL_00000197";

    public BlockBeacon() {
        super(Material.glass);
        this.setHardness(3.0f);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBeacon();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity var9 = worldIn.getTileEntity(pos);
        if (var9 instanceof TileEntityBeacon) {
            playerIn.displayGUIChest((TileEntityBeacon)var9);
        }
        return true;
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
    public int getRenderType() {
        return 3;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity var6;
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName() && (var6 = worldIn.getTileEntity(pos)) instanceof TileEntityBeacon) {
            ((TileEntityBeacon)var6).func_145999_a(stack.getDisplayName());
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        TileEntity var5 = worldIn.getTileEntity(pos);
        if (var5 instanceof TileEntityBeacon) {
            ((TileEntityBeacon)var5).func_174908_m();
            worldIn.addBlockEvent(pos, this, 1, 0);
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public static void func_176450_d(final World worldIn, final BlockPos p_176450_1_) {
        HttpUtil.field_180193_a.submit(new Runnable(){
            private static final String __OBFID = "CL_00002136";

            @Override
            public void run() {
                Chunk var1 = worldIn.getChunkFromBlockCoords(p_176450_1_);
                for (int var2 = p_176450_1_.getY() - 1; var2 >= 0; --var2) {
                    final BlockPos var3 = new BlockPos(p_176450_1_.getX(), var2, p_176450_1_.getZ());
                    if (!var1.canSeeSky(var3)) break;
                    IBlockState var4 = worldIn.getBlockState(var3);
                    if (var4.getBlock() != Blocks.beacon) continue;
                    ((WorldServer)worldIn).addScheduledTask(new Runnable(){
                        private static final String __OBFID = "CL_00002135";

                        @Override
                        public void run() {
                            TileEntity var1 = worldIn.getTileEntity(var3);
                            if (var1 instanceof TileEntityBeacon) {
                                ((TileEntityBeacon)var1).func_174908_m();
                                worldIn.addBlockEvent(var3, Blocks.beacon, 1, 0);
                            }
                        }
                    });
                }
            }

        });
    }

}

