package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketChatMessage implements Packet<INetHandlerPlayServer> {
	private String message;

	public CPacketChatMessage() {
	}

	public CPacketChatMessage(String messageIn) {
		if (messageIn.length() > 100) {
			messageIn = messageIn.substring(0, 100);
		}

		this.message = messageIn;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.message = buf.readStringFromBuffer(100);
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeString(this.message);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	@Override
	public void processPacket(INetHandlerPlayServer handler) {
		handler.processChatMessage(this);
	}

	public String getMessage() {
		return this.message;
	}
}
