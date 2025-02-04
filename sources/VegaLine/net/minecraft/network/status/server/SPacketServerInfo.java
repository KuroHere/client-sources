/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.network.status.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.Type;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class SPacketServerInfo
implements Packet<INetHandlerStatusClient> {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter((Type)((Object)ServerStatusResponse.Version.class), new ServerStatusResponse.Version.Serializer()).registerTypeAdapter((Type)((Object)ServerStatusResponse.Players.class), new ServerStatusResponse.Players.Serializer()).registerTypeAdapter((Type)((Object)ServerStatusResponse.class), new ServerStatusResponse.Serializer()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeHierarchyAdapter(Style.class, new Style.Serializer()).registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
    private ServerStatusResponse response;

    public SPacketServerInfo() {
    }

    public SPacketServerInfo(ServerStatusResponse responseIn) {
        this.response = responseIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.response = JsonUtils.gsonDeserialize(GSON, buf.readStringFromBuffer(Short.MAX_VALUE), ServerStatusResponse.class);
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(GSON.toJson(this.response));
    }

    @Override
    public void processPacket(INetHandlerStatusClient handler) {
        handler.handleServerInfo(this);
    }

    public ServerStatusResponse getResponse() {
        return this.response;
    }
}

