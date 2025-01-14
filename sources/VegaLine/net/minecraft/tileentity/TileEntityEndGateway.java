/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.tileentity;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TileEntityEndGateway
extends TileEntityEndPortal
implements ITickable {
    private static final Logger LOG = LogManager.getLogger();
    private long age;
    private int teleportCooldown;
    private BlockPos exitPortal;
    private boolean exactTeleport;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setLong("Age", this.age);
        if (this.exitPortal != null) {
            compound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
        }
        if (this.exactTeleport) {
            compound.setBoolean("ExactTeleport", this.exactTeleport);
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.age = compound.getLong("Age");
        if (compound.hasKey("ExitPortal", 10)) {
            this.exitPortal = NBTUtil.getPosFromTag(compound.getCompoundTag("ExitPortal"));
        }
        this.exactTeleport = compound.getBoolean("ExactTeleport");
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }

    @Override
    public void update() {
        boolean flag = this.isSpawning();
        boolean flag1 = this.isCoolingDown();
        ++this.age;
        if (flag1) {
            --this.teleportCooldown;
        } else if (!this.world.isRemote) {
            List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos()));
            if (!list.isEmpty()) {
                this.teleportEntity(list.get(0));
            }
            if (this.age % 2400L == 0L) {
                this.triggerCooldown();
            }
        }
        if (flag != this.isSpawning() || flag1 != this.isCoolingDown()) {
            this.markDirty();
        }
    }

    public boolean isSpawning() {
        return this.age < 200L;
    }

    public boolean isCoolingDown() {
        return this.teleportCooldown > 0;
    }

    public float getSpawnPercent(float p_184302_1_) {
        return MathHelper.clamp(((float)this.age + p_184302_1_) / 200.0f, 0.0f, 1.0f);
    }

    public float getCooldownPercent(float p_184305_1_) {
        return 1.0f - MathHelper.clamp(((float)this.teleportCooldown - p_184305_1_) / 40.0f, 0.0f, 1.0f);
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 8, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public void triggerCooldown() {
        if (!this.world.isRemote) {
            this.teleportCooldown = 40;
            this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, 0);
            this.markDirty();
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type2) {
        if (id == 1) {
            this.teleportCooldown = 40;
            return true;
        }
        return super.receiveClientEvent(id, type2);
    }

    public void teleportEntity(Entity entityIn) {
        if (!this.world.isRemote && !this.isCoolingDown()) {
            this.teleportCooldown = 100;
            if (this.exitPortal == null && this.world.provider instanceof WorldProviderEnd) {
                this.findExitPortal();
            }
            if (this.exitPortal != null) {
                BlockPos blockpos = this.exactTeleport ? this.exitPortal : this.findExitPosition();
                entityIn.setPositionAndUpdate((double)blockpos.getX() + 0.5, (double)blockpos.getY() + 0.5, (double)blockpos.getZ() + 0.5);
            }
            this.triggerCooldown();
        }
    }

    private BlockPos findExitPosition() {
        BlockPos blockpos = TileEntityEndGateway.findHighestBlock(this.world, this.exitPortal, 5, false);
        LOG.debug("Best exit position for portal at {} is {}", (Object)this.exitPortal, (Object)blockpos);
        return blockpos.up();
    }

    private void findExitPortal() {
        Vec3d vec3d = new Vec3d(this.getPos().getX(), 0.0, this.getPos().getZ()).normalize();
        Vec3d vec3d1 = vec3d.scale(1024.0);
        int i = 16;
        while (TileEntityEndGateway.getChunk(this.world, vec3d1).getTopFilledSegment() > 0 && i-- > 0) {
            LOG.debug("Skipping backwards past nonempty chunk at {}", (Object)vec3d1);
            vec3d1 = vec3d1.add(vec3d.scale(-16.0));
        }
        int j = 16;
        while (TileEntityEndGateway.getChunk(this.world, vec3d1).getTopFilledSegment() == 0 && j-- > 0) {
            LOG.debug("Skipping forward past empty chunk at {}", (Object)vec3d1);
            vec3d1 = vec3d1.add(vec3d.scale(16.0));
        }
        LOG.debug("Found chunk at {}", (Object)vec3d1);
        Chunk chunk = TileEntityEndGateway.getChunk(this.world, vec3d1);
        this.exitPortal = TileEntityEndGateway.findSpawnpointInChunk(chunk);
        if (this.exitPortal == null) {
            this.exitPortal = new BlockPos(vec3d1.xCoord + 0.5, 75.0, vec3d1.zCoord + 0.5);
            LOG.debug("Failed to find suitable block, settling on {}", (Object)this.exitPortal);
            new WorldGenEndIsland().generate(this.world, new Random(this.exitPortal.toLong()), this.exitPortal);
        } else {
            LOG.debug("Found block at {}", (Object)this.exitPortal);
        }
        this.exitPortal = TileEntityEndGateway.findHighestBlock(this.world, this.exitPortal, 16, true);
        LOG.debug("Creating portal at {}", (Object)this.exitPortal);
        this.exitPortal = this.exitPortal.up(10);
        this.createExitPortal(this.exitPortal);
        this.markDirty();
    }

    private static BlockPos findHighestBlock(World p_184308_0_, BlockPos p_184308_1_, int p_184308_2_, boolean p_184308_3_) {
        Vec3i blockpos = null;
        for (int i = -p_184308_2_; i <= p_184308_2_; ++i) {
            block1: for (int j = -p_184308_2_; j <= p_184308_2_; ++j) {
                if (i == 0 && j == 0 && !p_184308_3_) continue;
                for (int k = 255; k > (blockpos == null ? 0 : blockpos.getY()); --k) {
                    BlockPos blockpos1 = new BlockPos(p_184308_1_.getX() + i, k, p_184308_1_.getZ() + j);
                    IBlockState iblockstate = p_184308_0_.getBlockState(blockpos1);
                    if (!iblockstate.isBlockNormalCube() || !p_184308_3_ && iblockstate.getBlock() == Blocks.BEDROCK) continue;
                    blockpos = blockpos1;
                    continue block1;
                }
            }
        }
        return blockpos == null ? p_184308_1_ : blockpos;
    }

    private static Chunk getChunk(World worldIn, Vec3d vec3) {
        return worldIn.getChunkFromChunkCoords(MathHelper.floor(vec3.xCoord / 16.0), MathHelper.floor(vec3.zCoord / 16.0));
    }

    @Nullable
    private static BlockPos findSpawnpointInChunk(Chunk chunkIn) {
        BlockPos blockpos = new BlockPos(chunkIn.xPosition * 16, 30, chunkIn.zPosition * 16);
        int i = chunkIn.getTopFilledSegment() + 16 - 1;
        BlockPos blockpos1 = new BlockPos(chunkIn.xPosition * 16 + 16 - 1, i, chunkIn.zPosition * 16 + 16 - 1);
        BlockPos blockpos2 = null;
        double d0 = 0.0;
        for (BlockPos blockpos3 : BlockPos.getAllInBox(blockpos, blockpos1)) {
            IBlockState iblockstate = chunkIn.getBlockState(blockpos3);
            if (iblockstate.getBlock() != Blocks.END_STONE || chunkIn.getBlockState(blockpos3.up(1)).isBlockNormalCube() || chunkIn.getBlockState(blockpos3.up(2)).isBlockNormalCube()) continue;
            double d1 = blockpos3.distanceSqToCenter(0.0, 0.0, 0.0);
            if (blockpos2 != null && !(d1 < d0)) continue;
            blockpos2 = blockpos3;
            d0 = d1;
        }
        return blockpos2;
    }

    private void createExitPortal(BlockPos posIn) {
        new WorldGenEndGateway().generate(this.world, new Random(), posIn);
        TileEntity tileentity = this.world.getTileEntity(posIn);
        if (tileentity instanceof TileEntityEndGateway) {
            TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;
            tileentityendgateway.exitPortal = new BlockPos(this.getPos());
            tileentityendgateway.markDirty();
        } else {
            LOG.warn("Couldn't save exit portal at {}", (Object)posIn);
        }
    }

    @Override
    public boolean shouldRenderFace(EnumFacing p_184313_1_) {
        return this.getBlockType().getDefaultState().shouldSideBeRendered(this.world, this.getPos(), p_184313_1_);
    }

    public int getParticleAmount() {
        int i = 0;
        for (EnumFacing enumfacing : EnumFacing.values()) {
            i += this.shouldRenderFace(enumfacing) ? 1 : 0;
        }
        return i;
    }

    public void func_190603_b(BlockPos p_190603_1_) {
        this.exactTeleport = true;
        this.exitPortal = p_190603_1_;
    }
}

