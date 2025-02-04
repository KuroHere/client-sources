/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlaceGhostRecipe
implements Packet<INetHandlerPlayClient> {
    private int field_194314_a;
    private IRecipe field_194315_b;

    public SPacketPlaceGhostRecipe() {
    }

    public SPacketPlaceGhostRecipe(int p_i47615_1_, IRecipe p_i47615_2_) {
        this.field_194314_a = p_i47615_1_;
        this.field_194315_b = p_i47615_2_;
    }

    public IRecipe func_194311_a() {
        return this.field_194315_b;
    }

    public int func_194313_b() {
        return this.field_194314_a;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.field_194314_a = buf.readByte();
        this.field_194315_b = CraftingManager.func_193374_a(buf.readVarIntFromBuffer());
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.field_194314_a);
        buf.writeVarIntToBuffer(CraftingManager.func_193375_a(this.field_194315_b));
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.func_194307_a(this);
    }
}

