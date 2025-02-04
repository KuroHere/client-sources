/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.server.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;

public class NetHandlerStatusServer
implements INetHandlerStatusServer {
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    private static final String __OBFID = "CL_00001464";

    public NetHandlerStatusServer(MinecraftServer serverIn, NetworkManager netManager) {
        this.server = serverIn;
        this.networkManager = netManager;
    }

    @Override
    public void onDisconnect(IChatComponent reason) {
    }

    @Override
    public void processServerQuery(C00PacketServerQuery packetIn) {
        this.networkManager.sendPacket(new S00PacketServerInfo(this.server.getServerStatusResponse()));
    }

    @Override
    public void processPing(C01PacketPing packetIn) {
        this.networkManager.sendPacket(new S01PacketPong(packetIn.getClientTime()));
    }
}

