/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase
extends Block {
    protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
    protected static final AxisAlignedBB field_190959_b = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    protected final boolean isPowered;

    public static boolean isRailBlock(World worldIn, BlockPos pos) {
        return BlockRailBase.isRailBlock(worldIn.getBlockState(pos));
    }

    public static boolean isRailBlock(IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.RAIL || block == Blocks.GOLDEN_RAIL || block == Blocks.DETECTOR_RAIL || block == Blocks.ACTIVATOR_RAIL;
    }

    protected BlockRailBase(boolean isPowered) {
        super(Material.CIRCUITS);
        this.isPowered = isPowered;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumRailDirection blockrailbase$enumraildirection = state.getBlock() == this ? state.getValue(this.getShapeProperty()) : null;
        return blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending() ? field_190959_b : FLAT_AABB;
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isFullyOpaque();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            state = this.updateDir(worldIn, pos, state, true);
            if (this.isPowered) {
                state.neighborChanged(worldIn, pos, this, pos);
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (!worldIn.isRemote) {
            EnumRailDirection blockrailbase$enumraildirection = state.getValue(this.getShapeProperty());
            boolean flag = false;
            if (!worldIn.getBlockState(pos.down()).isFullyOpaque()) {
                flag = true;
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_EAST && !worldIn.getBlockState(pos.east()).isFullyOpaque()) {
                flag = true;
            } else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_WEST && !worldIn.getBlockState(pos.west()).isFullyOpaque()) {
                flag = true;
            } else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_NORTH && !worldIn.getBlockState(pos.north()).isFullyOpaque()) {
                flag = true;
            } else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_SOUTH && !worldIn.getBlockState(pos.south()).isFullyOpaque()) {
                flag = true;
            }
            if (flag && !worldIn.isAirBlock(pos)) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            } else {
                this.updateState(state, worldIn, pos, blockIn);
            }
        }
    }

    protected void updateState(IBlockState p_189541_1_, World p_189541_2_, BlockPos p_189541_3_, Block p_189541_4_) {
    }

    protected IBlockState updateDir(World worldIn, BlockPos pos, IBlockState state, boolean p_176564_4_) {
        return worldIn.isRemote ? state : new Rail(worldIn, pos, state).place(worldIn.isBlockPowered(pos), p_176564_4_).getBlockState();
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (state.getValue(this.getShapeProperty()).isAscending()) {
            worldIn.notifyNeighborsOfStateChange(pos.up(), this, false);
        }
        if (this.isPowered) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
        }
    }

    public abstract IProperty<EnumRailDirection> getShapeProperty();

    public static enum EnumRailDirection implements IStringSerializable
    {
        NORTH_SOUTH(0, "north_south"),
        EAST_WEST(1, "east_west"),
        ASCENDING_EAST(2, "ascending_east"),
        ASCENDING_WEST(3, "ascending_west"),
        ASCENDING_NORTH(4, "ascending_north"),
        ASCENDING_SOUTH(5, "ascending_south"),
        SOUTH_EAST(6, "south_east"),
        SOUTH_WEST(7, "south_west"),
        NORTH_WEST(8, "north_west"),
        NORTH_EAST(9, "north_east");

        private static final EnumRailDirection[] META_LOOKUP;
        private final int meta;
        private final String name;

        private EnumRailDirection(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public boolean isAscending() {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        public static EnumRailDirection byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }
            return META_LOOKUP[meta];
        }

        @Override
        public String getName() {
            return this.name;
        }

        static {
            META_LOOKUP = new EnumRailDirection[EnumRailDirection.values().length];
            EnumRailDirection[] enumRailDirectionArray = EnumRailDirection.values();
            int n = enumRailDirectionArray.length;
            for (int i = 0; i < n; ++i) {
                EnumRailDirection blockrailbase$enumraildirection;
                EnumRailDirection.META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection = enumRailDirectionArray[i];
            }
        }
    }

    public class Rail {
        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List<BlockPos> connectedRails = Lists.newArrayList();

        public Rail(World worldIn, BlockPos pos, IBlockState state) {
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BlockRailBase)state.getBlock();
            EnumRailDirection blockrailbase$enumraildirection = state.getValue(this.block.getShapeProperty());
            this.isPowered = this.block.isPowered;
            this.updateConnectedRails(blockrailbase$enumraildirection);
        }

        public List<BlockPos> getConnectedRails() {
            return this.connectedRails;
        }

        private void updateConnectedRails(EnumRailDirection railDirection) {
            this.connectedRails.clear();
            switch (railDirection) {
                case NORTH_SOUTH: {
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case EAST_WEST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east());
                    break;
                }
                case ASCENDING_EAST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east().up());
                    break;
                }
                case ASCENDING_WEST: {
                    this.connectedRails.add(this.pos.west().up());
                    this.connectedRails.add(this.pos.east());
                    break;
                }
                case ASCENDING_NORTH: {
                    this.connectedRails.add(this.pos.north().up());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case ASCENDING_SOUTH: {
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south().up());
                    break;
                }
                case SOUTH_EAST: {
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case SOUTH_WEST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case NORTH_WEST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.north());
                    break;
                }
                case NORTH_EAST: {
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.north());
                }
            }
        }

        private void removeSoftConnections() {
            for (int i = 0; i < this.connectedRails.size(); ++i) {
                Rail blockrailbase$rail = this.findRailAt(this.connectedRails.get(i));
                if (blockrailbase$rail != null && blockrailbase$rail.isConnectedToRail(this)) {
                    this.connectedRails.set(i, blockrailbase$rail.pos);
                    continue;
                }
                this.connectedRails.remove(i--);
            }
        }

        private boolean hasRailAt(BlockPos pos) {
            return BlockRailBase.isRailBlock(this.world, pos) || BlockRailBase.isRailBlock(this.world, pos.up()) || BlockRailBase.isRailBlock(this.world, pos.down());
        }

        @Nullable
        private Rail findRailAt(BlockPos pos) {
            Rail rail;
            IBlockState iblockstate = this.world.getBlockState(pos);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                BlockRailBase blockRailBase = BlockRailBase.this;
                Objects.requireNonNull(blockRailBase);
                return blockRailBase.new Rail(this.world, pos, iblockstate);
            }
            BlockPos lvt_2_1_ = pos.up();
            iblockstate = this.world.getBlockState(lvt_2_1_);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                BlockRailBase blockRailBase = BlockRailBase.this;
                Objects.requireNonNull(blockRailBase);
                return blockRailBase.new Rail(this.world, lvt_2_1_, iblockstate);
            }
            lvt_2_1_ = pos.down();
            iblockstate = this.world.getBlockState(lvt_2_1_);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                BlockRailBase blockRailBase = BlockRailBase.this;
                Objects.requireNonNull(blockRailBase);
                rail = blockRailBase.new Rail(this.world, lvt_2_1_, iblockstate);
            } else {
                rail = null;
            }
            return rail;
        }

        private boolean isConnectedToRail(Rail rail) {
            return this.isConnectedTo(rail.pos);
        }

        private boolean isConnectedTo(BlockPos posIn) {
            for (int i = 0; i < this.connectedRails.size(); ++i) {
                BlockPos blockpos = this.connectedRails.get(i);
                if (blockpos.getX() != posIn.getX() || blockpos.getZ() != posIn.getZ()) continue;
                return true;
            }
            return false;
        }

        protected int countAdjacentRails() {
            int i = 0;
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (!this.hasRailAt(this.pos.offset(enumfacing))) continue;
                ++i;
            }
            return i;
        }

        private boolean canConnectTo(Rail rail) {
            return this.isConnectedToRail(rail) || this.connectedRails.size() != 2;
        }

        private void connectTo(Rail p_150645_1_) {
            this.connectedRails.add(p_150645_1_.pos);
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.isConnectedTo(blockpos);
            boolean flag1 = this.isConnectedTo(blockpos1);
            boolean flag2 = this.isConnectedTo(blockpos2);
            boolean flag3 = this.isConnectedTo(blockpos3);
            EnumRailDirection blockrailbase$enumraildirection = null;
            if (flag || flag1) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            if (flag2 || flag3) {
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
            }
            if (!this.isPowered) {
                if (flag1 && flag3 && !flag && !flag2) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                }
                if (flag1 && flag2 && !flag && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                }
                if (flag && flag2 && !flag1 && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                }
                if (flag && flag3 && !flag1 && !flag2) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos1.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            this.world.setBlockState(this.pos, this.state, 3);
        }

        private boolean hasNeighborRail(BlockPos p_180361_1_) {
            Rail blockrailbase$rail = this.findRailAt(p_180361_1_);
            if (blockrailbase$rail == null) {
                return false;
            }
            blockrailbase$rail.removeSoftConnections();
            return blockrailbase$rail.canConnectTo(this);
        }

        public Rail place(boolean p_180364_1_, boolean p_180364_2_) {
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.hasNeighborRail(blockpos);
            boolean flag1 = this.hasNeighborRail(blockpos1);
            boolean flag2 = this.hasNeighborRail(blockpos2);
            boolean flag3 = this.hasNeighborRail(blockpos3);
            EnumRailDirection blockrailbase$enumraildirection = null;
            if ((flag || flag1) && !flag2 && !flag3) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            if ((flag2 || flag3) && !flag && !flag1) {
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
            }
            if (!this.isPowered) {
                if (flag1 && flag3 && !flag && !flag2) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                }
                if (flag1 && flag2 && !flag && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                }
                if (flag && flag2 && !flag1 && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                }
                if (flag && flag3 && !flag1 && !flag2) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                if (flag || flag1) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                }
                if (flag2 || flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                }
                if (!this.isPowered) {
                    if (p_180364_1_) {
                        if (flag1 && flag3) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                        }
                        if (flag2 && flag1) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                        }
                        if (flag3 && flag) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                        }
                        if (flag && flag2) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                        }
                    } else {
                        if (flag && flag2) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                        }
                        if (flag3 && flag) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                        }
                        if (flag2 && flag1) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                        }
                        if (flag1 && flag3) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos1.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            this.updateConnectedRails(blockrailbase$enumraildirection);
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            if (p_180364_2_ || this.world.getBlockState(this.pos) != this.state) {
                this.world.setBlockState(this.pos, this.state, 3);
                for (int i = 0; i < this.connectedRails.size(); ++i) {
                    Rail blockrailbase$rail = this.findRailAt(this.connectedRails.get(i));
                    if (blockrailbase$rail == null) continue;
                    blockrailbase$rail.removeSoftConnections();
                    if (!blockrailbase$rail.canConnectTo(this)) continue;
                    blockrailbase$rail.connectTo(this);
                }
            }
            return this;
        }

        public IBlockState getBlockState() {
            return this.state;
        }
    }
}

