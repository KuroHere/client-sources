package net.minecraft.network.login.client;

import java.io.IOException;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class CPacketLoginStart implements Packet<INetHandlerLoginServer> {
	private GameProfile profile;

	public CPacketLoginStart() {
	}

	public CPacketLoginStart(GameProfile profileIn) {
		this.profile = profileIn;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.profile = new GameProfile((UUID) null, buf.readStringFromBuffer(16));
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeString(this.profile.getName());
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	@Override
	public void processPacket(INetHandlerLoginServer handler) {
		handler.processLoginStart(this);
	}

	public GameProfile getProfile() {
		return this.profile;
	}
}
