/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C10PacketCreativeInventoryAction
implements Packet {
    private int slotId;
    private ItemStack stack;
    private static final String __OBFID = "CL_00001369";

    public C10PacketCreativeInventoryAction() {
    }

    public C10PacketCreativeInventoryAction(int p_i45263_1_, ItemStack p_i45263_2_) {
        this.slotId = p_i45263_1_;
        this.stack = p_i45263_2_ != null ? p_i45263_2_.copy() : null;
    }

    public void func_180767_a(INetHandlerPlayServer p_180767_1_) {
        p_180767_1_.processCreativeInventoryAction(this);
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.slotId = data.readShort();
        this.stack = data.readItemStackFromBuffer();
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeShort(this.slotId);
        data.writeItemStackToBuffer(this.stack);
    }

    public int getSlotId() {
        return this.slotId;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.func_180767_a((INetHandlerPlayServer)handler);
    }
}

