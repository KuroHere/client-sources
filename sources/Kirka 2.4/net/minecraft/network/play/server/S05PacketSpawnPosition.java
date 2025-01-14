/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S05PacketSpawnPosition
implements Packet {
    private BlockPos field_179801_a;
    private static final String __OBFID = "CL_00001336";

    public S05PacketSpawnPosition() {
    }

    public S05PacketSpawnPosition(BlockPos p_i45956_1_) {
        this.field_179801_a = p_i45956_1_;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179801_a = data.readBlockPos();
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179801_a);
    }

    public void func_180752_a(INetHandlerPlayClient p_180752_1_) {
        p_180752_1_.handleSpawnPosition(this);
    }

    public BlockPos func_179800_a() {
        return this.field_179801_a;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.func_180752_a((INetHandlerPlayClient)handler);
    }
}

