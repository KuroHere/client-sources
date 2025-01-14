/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class SPacketMultiBlockChange
implements Packet<INetHandlerPlayClient> {
    private ChunkPos chunkPos;
    private BlockUpdateData[] changedBlocks;

    public SPacketMultiBlockChange() {
    }

    public SPacketMultiBlockChange(int p_i46959_1_, short[] p_i46959_2_, Chunk p_i46959_3_) {
        this.chunkPos = new ChunkPos(p_i46959_3_.xPosition, p_i46959_3_.zPosition);
        this.changedBlocks = new BlockUpdateData[p_i46959_1_];
        for (int i = 0; i < this.changedBlocks.length; ++i) {
            this.changedBlocks[i] = new BlockUpdateData(p_i46959_2_[i], p_i46959_3_);
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.chunkPos = new ChunkPos(buf.readInt(), buf.readInt());
        this.changedBlocks = new BlockUpdateData[buf.readVarIntFromBuffer()];
        for (int i = 0; i < this.changedBlocks.length; ++i) {
            this.changedBlocks[i] = new BlockUpdateData(buf.readShort(), Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer()));
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeInt(this.chunkPos.chunkXPos);
        buf.writeInt(this.chunkPos.chunkZPos);
        buf.writeVarIntToBuffer(this.changedBlocks.length);
        for (BlockUpdateData spacketmultiblockchange$blockupdatedata : this.changedBlocks) {
            buf.writeShort(spacketmultiblockchange$blockupdatedata.getOffset());
            buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(spacketmultiblockchange$blockupdatedata.getBlockState()));
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleMultiBlockChange(this);
    }

    public BlockUpdateData[] getChangedBlocks() {
        return this.changedBlocks;
    }

    public class BlockUpdateData {
        private final short offset;
        private final IBlockState blockState;

        public BlockUpdateData(short p_i46544_2_, IBlockState p_i46544_3_) {
            this.offset = p_i46544_2_;
            this.blockState = p_i46544_3_;
        }

        public BlockUpdateData(short p_i46545_2_, Chunk p_i46545_3_) {
            this.offset = p_i46545_2_;
            this.blockState = p_i46545_3_.getBlockState(this.getPos());
        }

        public BlockPos getPos() {
            return new BlockPos(SPacketMultiBlockChange.this.chunkPos.getBlock(this.offset >> 12 & 0xF, this.offset & 0xFF, this.offset >> 8 & 0xF));
        }

        public short getOffset() {
            return this.offset;
        }

        public IBlockState getBlockState() {
            return this.blockState;
        }
    }
}

