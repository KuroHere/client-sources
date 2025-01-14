/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.network.status.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C01PacketPing
implements Packet {
    private long clientTime;
    private static final String __OBFID = "CL_00001392";

    public C01PacketPing() {
    }

    public C01PacketPing(long p_i45276_1_) {
        this.clientTime = p_i45276_1_;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.clientTime = data.readLong();
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeLong(this.clientTime);
    }

    public void func_180774_a(INetHandlerStatusServer p_180774_1_) {
        p_180774_1_.processPing(this);
    }

    public long getClientTime() {
        return this.clientTime;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.func_180774_a((INetHandlerStatusServer)handler);
    }
}

