/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class C02PacketUseEntity
implements Packet {
    private int entityId;
    private Action action;
    private Vec3 field_179713_c;
    private static final String __OBFID = "CL_00001357";

    public C02PacketUseEntity() {
    }

    public C02PacketUseEntity(Entity p_i45251_1_, Action p_i45251_2_) {
        this.entityId = p_i45251_1_.getEntityId();
        this.action = p_i45251_2_;
    }

    public C02PacketUseEntity(Entity p_i45944_1_, Vec3 p_i45944_2_) {
        this(p_i45944_1_, Action.INTERACT_AT);
        this.field_179713_c = p_i45944_2_;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.entityId = data.readVarIntFromBuffer();
        this.action = (Action)data.readEnumValue(Action.class);
        if (this.action == Action.INTERACT_AT) {
            this.field_179713_c = new Vec3(data.readFloat(), data.readFloat(), data.readFloat());
        }
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.entityId);
        data.writeEnumValue(this.action);
        if (this.action == Action.INTERACT_AT) {
            data.writeFloat((float)this.field_179713_c.xCoord);
            data.writeFloat((float)this.field_179713_c.yCoord);
            data.writeFloat((float)this.field_179713_c.zCoord);
        }
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processUseEntity(this);
    }

    public Entity getEntityFromWorld(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    public Action getAction() {
        return this.action;
    }

    public Vec3 func_179712_b() {
        return this.field_179713_c;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }

    public static enum Action {
        INTERACT("INTERACT", 0),
        ATTACK("ATTACK", 1),
        INTERACT_AT("INTERACT_AT", 2);
        
        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00001358";

        static {
            $VALUES = new Action[]{INTERACT, ATTACK, INTERACT_AT};
        }

        private Action(String p_i45943_1_, int p_i45943_2_, String string2, int n2) {
        }
    }

}

