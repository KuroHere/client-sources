/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.structure.template;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.Mirror;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.Rotation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.BlockRotationProcessor;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class Template {
    private final List<BlockInfo> blocks = Lists.newArrayList();
    private final List<EntityInfo> entities = Lists.newArrayList();
    private BlockPos size = BlockPos.ORIGIN;
    private String author = "?";

    public BlockPos getSize() {
        return this.size;
    }

    public void setAuthor(String authorIn) {
        this.author = authorIn;
    }

    public String getAuthor() {
        return this.author;
    }

    public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos endPos, boolean takeEntities, @Nullable Block toIgnore) {
        if (endPos.getX() >= 1 && endPos.getY() >= 1 && endPos.getZ() >= 1) {
            BlockPos blockpos = startPos.add(endPos).add(-1, -1, -1);
            ArrayList<BlockInfo> list = Lists.newArrayList();
            ArrayList<BlockInfo> list1 = Lists.newArrayList();
            ArrayList<BlockInfo> list2 = Lists.newArrayList();
            BlockPos blockpos1 = new BlockPos(Math.min(startPos.getX(), blockpos.getX()), Math.min(startPos.getY(), blockpos.getY()), Math.min(startPos.getZ(), blockpos.getZ()));
            BlockPos blockpos2 = new BlockPos(Math.max(startPos.getX(), blockpos.getX()), Math.max(startPos.getY(), blockpos.getY()), Math.max(startPos.getZ(), blockpos.getZ()));
            this.size = endPos;
            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos1, blockpos2)) {
                BlockPos blockpos3 = blockpos$mutableblockpos.subtract(blockpos1);
                IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);
                if (toIgnore != null && toIgnore == iblockstate.getBlock()) continue;
                TileEntity tileentity = worldIn.getTileEntity(blockpos$mutableblockpos);
                if (tileentity != null) {
                    NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                    nbttagcompound.removeTag("x");
                    nbttagcompound.removeTag("y");
                    nbttagcompound.removeTag("z");
                    list1.add(new BlockInfo(blockpos3, iblockstate, nbttagcompound));
                    continue;
                }
                if (!iblockstate.isFullBlock() && !iblockstate.isFullCube()) {
                    list2.add(new BlockInfo(blockpos3, iblockstate, null));
                    continue;
                }
                list.add(new BlockInfo(blockpos3, iblockstate, null));
            }
            this.blocks.clear();
            this.blocks.addAll(list);
            this.blocks.addAll(list1);
            this.blocks.addAll(list2);
            if (takeEntities) {
                this.takeEntitiesFromWorld(worldIn, blockpos1, blockpos2.add(1, 1, 1));
            } else {
                this.entities.clear();
            }
        }
    }

    private void takeEntitiesFromWorld(World worldIn, BlockPos startPos, BlockPos endPos) {
        List<Entity> list = worldIn.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(startPos, endPos), new Predicate<Entity>(){

            @Override
            public boolean apply(@Nullable Entity p_apply_1_) {
                return !(p_apply_1_ instanceof EntityPlayer);
            }
        });
        this.entities.clear();
        for (Entity entity : list) {
            Vec3d vec3d = new Vec3d(entity.posX - (double)startPos.getX(), entity.posY - (double)startPos.getY(), entity.posZ - (double)startPos.getZ());
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            entity.writeToNBTOptional(nbttagcompound);
            BlockPos blockpos = entity instanceof EntityPainting ? ((EntityPainting)entity).getHangingPosition().subtract(startPos) : new BlockPos(vec3d);
            this.entities.add(new EntityInfo(vec3d, blockpos, nbttagcompound));
        }
    }

    public Map<BlockPos, String> getDataBlocks(BlockPos pos, PlacementSettings placementIn) {
        HashMap<BlockPos, String> map = Maps.newHashMap();
        StructureBoundingBox structureboundingbox = placementIn.getBoundingBox();
        for (BlockInfo template$blockinfo : this.blocks) {
            TileEntityStructure.Mode tileentitystructure$mode;
            IBlockState iblockstate;
            BlockPos blockpos = Template.transformedBlockPos(placementIn, template$blockinfo.pos).add(pos);
            if (structureboundingbox != null && !structureboundingbox.isVecInside(blockpos) || (iblockstate = template$blockinfo.blockState).getBlock() != Blocks.STRUCTURE_BLOCK || template$blockinfo.tileentityData == null || (tileentitystructure$mode = TileEntityStructure.Mode.valueOf(template$blockinfo.tileentityData.getString("mode"))) != TileEntityStructure.Mode.DATA) continue;
            map.put(blockpos, template$blockinfo.tileentityData.getString("metadata"));
        }
        return map;
    }

    public BlockPos calculateConnectedPos(PlacementSettings placementIn, BlockPos p_186262_2_, PlacementSettings p_186262_3_, BlockPos p_186262_4_) {
        BlockPos blockpos = Template.transformedBlockPos(placementIn, p_186262_2_);
        BlockPos blockpos1 = Template.transformedBlockPos(p_186262_3_, p_186262_4_);
        return blockpos.subtract(blockpos1);
    }

    public static BlockPos transformedBlockPos(PlacementSettings placementIn, BlockPos p_186266_1_) {
        return Template.transformedBlockPos(p_186266_1_, placementIn.getMirror(), placementIn.getRotation());
    }

    public void addBlocksToWorldChunk(World worldIn, BlockPos pos, PlacementSettings placementIn) {
        placementIn.setBoundingBoxFromChunk();
        this.addBlocksToWorld(worldIn, pos, placementIn);
    }

    public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn) {
        this.addBlocksToWorld(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, 2);
    }

    public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn, int flags) {
        this.addBlocksToWorld(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, flags);
    }

    public void addBlocksToWorld(World p_189960_1_, BlockPos p_189960_2_, @Nullable ITemplateProcessor p_189960_3_, PlacementSettings p_189960_4_, int p_189960_5_) {
        if (!(this.blocks.isEmpty() && (p_189960_4_.getIgnoreEntities() || this.entities.isEmpty()) || this.size.getX() < 1 || this.size.getY() < 1 || this.size.getZ() < 1)) {
            Block block = p_189960_4_.getReplacedBlock();
            StructureBoundingBox structureboundingbox = p_189960_4_.getBoundingBox();
            for (BlockInfo template$blockinfo : this.blocks) {
                TileEntity tileentity2;
                TileEntity tileentity;
                BlockPos blockpos = Template.transformedBlockPos(p_189960_4_, template$blockinfo.pos).add(p_189960_2_);
                BlockInfo template$blockinfo1 = p_189960_3_ != null ? p_189960_3_.processBlock(p_189960_1_, blockpos, template$blockinfo) : template$blockinfo;
                if (template$blockinfo1 == null) continue;
                Block block1 = template$blockinfo1.blockState.getBlock();
                if (block != null && block == block1 || p_189960_4_.getIgnoreStructureBlock() && block1 == Blocks.STRUCTURE_BLOCK || structureboundingbox != null && !structureboundingbox.isVecInside(blockpos)) continue;
                IBlockState iblockstate = template$blockinfo1.blockState.withMirror(p_189960_4_.getMirror());
                IBlockState iblockstate1 = iblockstate.withRotation(p_189960_4_.getRotation());
                if (template$blockinfo1.tileentityData != null && (tileentity = p_189960_1_.getTileEntity(blockpos)) != null) {
                    if (tileentity instanceof IInventory) {
                        ((IInventory)((Object)tileentity)).clear();
                    }
                    p_189960_1_.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 4);
                }
                if (!p_189960_1_.setBlockState(blockpos, iblockstate1, p_189960_5_) || template$blockinfo1.tileentityData == null || (tileentity2 = p_189960_1_.getTileEntity(blockpos)) == null) continue;
                template$blockinfo1.tileentityData.setInteger("x", blockpos.getX());
                template$blockinfo1.tileentityData.setInteger("y", blockpos.getY());
                template$blockinfo1.tileentityData.setInteger("z", blockpos.getZ());
                tileentity2.readFromNBT(template$blockinfo1.tileentityData);
                tileentity2.mirror(p_189960_4_.getMirror());
                tileentity2.rotate(p_189960_4_.getRotation());
            }
            for (BlockInfo template$blockinfo2 : this.blocks) {
                TileEntity tileentity1;
                if (block != null && block == template$blockinfo2.blockState.getBlock()) continue;
                BlockPos blockpos1 = Template.transformedBlockPos(p_189960_4_, template$blockinfo2.pos).add(p_189960_2_);
                if (structureboundingbox != null && !structureboundingbox.isVecInside(blockpos1)) continue;
                p_189960_1_.notifyNeighborsRespectDebug(blockpos1, template$blockinfo2.blockState.getBlock(), false);
                if (template$blockinfo2.tileentityData == null || (tileentity1 = p_189960_1_.getTileEntity(blockpos1)) == null) continue;
                tileentity1.markDirty();
            }
            if (!p_189960_4_.getIgnoreEntities()) {
                this.addEntitiesToWorld(p_189960_1_, p_189960_2_, p_189960_4_.getMirror(), p_189960_4_.getRotation(), structureboundingbox);
            }
        }
    }

    private void addEntitiesToWorld(World worldIn, BlockPos pos, Mirror mirrorIn, Rotation rotationIn, @Nullable StructureBoundingBox aabb) {
        for (EntityInfo template$entityinfo : this.entities) {
            Entity entity;
            BlockPos blockpos = Template.transformedBlockPos(template$entityinfo.blockPos, mirrorIn, rotationIn).add(pos);
            if (aabb != null && !aabb.isVecInside(blockpos)) continue;
            NBTTagCompound nbttagcompound = template$entityinfo.entityData;
            Vec3d vec3d = Template.transformedVec3d(template$entityinfo.pos, mirrorIn, rotationIn);
            Vec3d vec3d1 = vec3d.addVector(pos.getX(), pos.getY(), pos.getZ());
            NBTTagList nbttaglist = new NBTTagList();
            nbttaglist.appendTag(new NBTTagDouble(vec3d1.xCoord));
            nbttaglist.appendTag(new NBTTagDouble(vec3d1.yCoord));
            nbttaglist.appendTag(new NBTTagDouble(vec3d1.zCoord));
            nbttagcompound.setTag("Pos", nbttaglist);
            nbttagcompound.setUniqueId("UUID", UUID.randomUUID());
            try {
                entity = EntityList.createEntityFromNBT(nbttagcompound, worldIn);
            } catch (Exception var15) {
                entity = null;
            }
            if (entity == null) continue;
            float f = entity.getMirroredYaw(mirrorIn);
            entity.setLocationAndAngles(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, f += entity.rotationYaw - entity.getRotatedYaw(rotationIn), entity.rotationPitch);
            worldIn.spawnEntityInWorld(entity);
        }
    }

    public BlockPos transformedSize(Rotation rotationIn) {
        switch (rotationIn) {
            case COUNTERCLOCKWISE_90: 
            case CLOCKWISE_90: {
                return new BlockPos(this.size.getZ(), this.size.getY(), this.size.getX());
            }
        }
        return this.size;
    }

    private static BlockPos transformedBlockPos(BlockPos pos, Mirror mirrorIn, Rotation rotationIn) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                k = -k;
                break;
            }
            case FRONT_BACK: {
                i = -i;
                break;
            }
            default: {
                flag = false;
            }
        }
        switch (rotationIn) {
            case COUNTERCLOCKWISE_90: {
                return new BlockPos(k, j, -i);
            }
            case CLOCKWISE_90: {
                return new BlockPos(-k, j, i);
            }
            case CLOCKWISE_180: {
                return new BlockPos(-i, j, -k);
            }
        }
        return flag ? new BlockPos(i, j, k) : pos;
    }

    private static Vec3d transformedVec3d(Vec3d vec, Mirror mirrorIn, Rotation rotationIn) {
        double d0 = vec.xCoord;
        double d1 = vec.yCoord;
        double d2 = vec.zCoord;
        boolean flag = true;
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                d2 = 1.0 - d2;
                break;
            }
            case FRONT_BACK: {
                d0 = 1.0 - d0;
                break;
            }
            default: {
                flag = false;
            }
        }
        switch (rotationIn) {
            case COUNTERCLOCKWISE_90: {
                return new Vec3d(d2, d1, 1.0 - d0);
            }
            case CLOCKWISE_90: {
                return new Vec3d(1.0 - d2, d1, d0);
            }
            case CLOCKWISE_180: {
                return new Vec3d(1.0 - d0, d1, 1.0 - d2);
            }
        }
        return flag ? new Vec3d(d0, d1, d2) : vec;
    }

    public BlockPos getZeroPositionWithTransform(BlockPos p_189961_1_, Mirror p_189961_2_, Rotation p_189961_3_) {
        return Template.func_191157_a(p_189961_1_, p_189961_2_, p_189961_3_, this.getSize().getX(), this.getSize().getZ());
    }

    public static BlockPos func_191157_a(BlockPos p_191157_0_, Mirror p_191157_1_, Rotation p_191157_2_, int p_191157_3_, int p_191157_4_) {
        int i = p_191157_1_ == Mirror.FRONT_BACK ? --p_191157_3_ : 0;
        int j = p_191157_1_ == Mirror.LEFT_RIGHT ? --p_191157_4_ : 0;
        BlockPos blockpos = p_191157_0_;
        switch (p_191157_2_) {
            case COUNTERCLOCKWISE_90: {
                blockpos = p_191157_0_.add(j, 0, p_191157_3_ - i);
                break;
            }
            case CLOCKWISE_90: {
                blockpos = p_191157_0_.add(p_191157_4_ - j, 0, i);
                break;
            }
            case CLOCKWISE_180: {
                blockpos = p_191157_0_.add(p_191157_3_ - i, 0, p_191157_4_ - j);
                break;
            }
            case NONE: {
                blockpos = p_191157_0_.add(i, 0, j);
            }
        }
        return blockpos;
    }

    public static void func_191158_a(DataFixer p_191158_0_) {
        p_191158_0_.registerWalker(FixTypes.STRUCTURE, new IDataWalker(){

            @Override
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
                if (compound.hasKey("entities", 9)) {
                    NBTTagList nbttaglist = compound.getTagList("entities", 10);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.get(i);
                        if (!nbttagcompound.hasKey("nbt", 10)) continue;
                        nbttagcompound.setTag("nbt", fixer.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("nbt"), versionIn));
                    }
                }
                if (compound.hasKey("blocks", 9)) {
                    NBTTagList nbttaglist1 = compound.getTagList("blocks", 10);
                    for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist1.get(j);
                        if (!nbttagcompound1.hasKey("nbt", 10)) continue;
                        nbttagcompound1.setTag("nbt", fixer.process(FixTypes.BLOCK_ENTITY, nbttagcompound1.getCompoundTag("nbt"), versionIn));
                    }
                }
                return compound;
            }
        });
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        BasicPalette template$basicpalette = new BasicPalette();
        NBTTagList nbttaglist = new NBTTagList();
        for (BlockInfo blockInfo : this.blocks) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("pos", this.writeInts(blockInfo.pos.getX(), blockInfo.pos.getY(), blockInfo.pos.getZ()));
            nbttagcompound.setInteger("state", template$basicpalette.idFor(blockInfo.blockState));
            if (blockInfo.tileentityData != null) {
                nbttagcompound.setTag("nbt", blockInfo.tileentityData);
            }
            nbttaglist.appendTag(nbttagcompound);
        }
        NBTTagList nbttaglist1 = new NBTTagList();
        for (EntityInfo template$entityinfo : this.entities) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setTag("pos", this.writeDoubles(template$entityinfo.pos.xCoord, template$entityinfo.pos.yCoord, template$entityinfo.pos.zCoord));
            nbttagcompound1.setTag("blockPos", this.writeInts(template$entityinfo.blockPos.getX(), template$entityinfo.blockPos.getY(), template$entityinfo.blockPos.getZ()));
            if (template$entityinfo.entityData != null) {
                nbttagcompound1.setTag("nbt", template$entityinfo.entityData);
            }
            nbttaglist1.appendTag(nbttagcompound1);
        }
        NBTTagList nBTTagList = new NBTTagList();
        for (IBlockState iblockstate : template$basicpalette) {
            nBTTagList.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), iblockstate));
        }
        nbt.setTag("palette", nBTTagList);
        nbt.setTag("blocks", nbttaglist);
        nbt.setTag("entities", nbttaglist1);
        nbt.setTag("size", this.writeInts(this.size.getX(), this.size.getY(), this.size.getZ()));
        nbt.setString("author", this.author);
        nbt.setInteger("DataVersion", 1343);
        return nbt;
    }

    public void read(NBTTagCompound compound) {
        this.blocks.clear();
        this.entities.clear();
        NBTTagList nbttaglist = compound.getTagList("size", 3);
        this.size = new BlockPos(nbttaglist.getIntAt(0), nbttaglist.getIntAt(1), nbttaglist.getIntAt(2));
        this.author = compound.getString("author");
        BasicPalette template$basicpalette = new BasicPalette();
        NBTTagList nbttaglist1 = compound.getTagList("palette", 10);
        for (int i = 0; i < nbttaglist1.tagCount(); ++i) {
            template$basicpalette.addMapping(NBTUtil.readBlockState(nbttaglist1.getCompoundTagAt(i)), i);
        }
        NBTTagList nbttaglist3 = compound.getTagList("blocks", 10);
        for (int j = 0; j < nbttaglist3.tagCount(); ++j) {
            NBTTagCompound nbttagcompound = nbttaglist3.getCompoundTagAt(j);
            NBTTagList nbttaglist2 = nbttagcompound.getTagList("pos", 3);
            BlockPos blockpos = new BlockPos(nbttaglist2.getIntAt(0), nbttaglist2.getIntAt(1), nbttaglist2.getIntAt(2));
            IBlockState iblockstate = template$basicpalette.stateFor(nbttagcompound.getInteger("state"));
            NBTTagCompound nbttagcompound1 = nbttagcompound.hasKey("nbt") ? nbttagcompound.getCompoundTag("nbt") : null;
            this.blocks.add(new BlockInfo(blockpos, iblockstate, nbttagcompound1));
        }
        NBTTagList nbttaglist4 = compound.getTagList("entities", 10);
        for (int k = 0; k < nbttaglist4.tagCount(); ++k) {
            NBTTagCompound nbttagcompound3 = nbttaglist4.getCompoundTagAt(k);
            NBTTagList nbttaglist5 = nbttagcompound3.getTagList("pos", 6);
            Vec3d vec3d = new Vec3d(nbttaglist5.getDoubleAt(0), nbttaglist5.getDoubleAt(1), nbttaglist5.getDoubleAt(2));
            NBTTagList nbttaglist6 = nbttagcompound3.getTagList("blockPos", 3);
            BlockPos blockpos1 = new BlockPos(nbttaglist6.getIntAt(0), nbttaglist6.getIntAt(1), nbttaglist6.getIntAt(2));
            if (!nbttagcompound3.hasKey("nbt")) continue;
            NBTTagCompound nbttagcompound2 = nbttagcompound3.getCompoundTag("nbt");
            this.entities.add(new EntityInfo(vec3d, blockpos1, nbttagcompound2));
        }
    }

    private NBTTagList writeInts(int ... values) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i : values) {
            nbttaglist.appendTag(new NBTTagInt(i));
        }
        return nbttaglist;
    }

    private NBTTagList writeDoubles(double ... values) {
        NBTTagList nbttaglist = new NBTTagList();
        for (double d0 : values) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }
        return nbttaglist;
    }

    public static class BlockInfo {
        public final BlockPos pos;
        public final IBlockState blockState;
        public final NBTTagCompound tileentityData;

        public BlockInfo(BlockPos posIn, IBlockState stateIn, @Nullable NBTTagCompound compoundIn) {
            this.pos = posIn;
            this.blockState = stateIn;
            this.tileentityData = compoundIn;
        }
    }

    public static class EntityInfo {
        public final Vec3d pos;
        public final BlockPos blockPos;
        public final NBTTagCompound entityData;

        public EntityInfo(Vec3d vecIn, BlockPos posIn, NBTTagCompound compoundIn) {
            this.pos = vecIn;
            this.blockPos = posIn;
            this.entityData = compoundIn;
        }
    }

    static class BasicPalette
    implements Iterable<IBlockState> {
        public static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
        final ObjectIntIdentityMap<IBlockState> ids = new ObjectIntIdentityMap(16);
        private int lastId;

        private BasicPalette() {
        }

        public int idFor(IBlockState p_189954_1_) {
            int i = this.ids.get(p_189954_1_);
            if (i == -1) {
                i = this.lastId++;
                this.ids.put(p_189954_1_, i);
            }
            return i;
        }

        @Nullable
        public IBlockState stateFor(int p_189955_1_) {
            IBlockState iblockstate = this.ids.getByValue(p_189955_1_);
            return iblockstate == null ? DEFAULT_BLOCK_STATE : iblockstate;
        }

        @Override
        public Iterator<IBlockState> iterator() {
            return this.ids.iterator();
        }

        public void addMapping(IBlockState p_189956_1_, int p_189956_2_) {
            this.ids.put(p_189956_1_, p_189956_2_);
        }
    }
}

