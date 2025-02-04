package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCustomPayload implements Packet<INetHandlerPlayServer> {
	private String channel;
	private PacketBuffer data;

	public CPacketCustomPayload() {
	}

	public CPacketCustomPayload(String channelIn, PacketBuffer bufIn) {
		this.channel = channelIn;
		this.data = bufIn;

		if (bufIn.writerIndex() > 32767) { throw new IllegalArgumentException("Payload may not be larger than 32767 bytes"); }
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.channel = buf.readStringFromBuffer(20);
		int i = buf.readableBytes();

		if ((i >= 0) && (i <= 32767)) {
			this.data = new PacketBuffer(buf.readBytes(i));
		} else {
			throw new IOException("Payload may not be larger than 32767 bytes");
		}
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeString(this.channel);
		buf.writeBytes(this.data);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	@Override
	public void processPacket(INetHandlerPlayServer handler) {
		handler.processCustomPayload(this);

		if (this.data != null) {
			this.data.release();
		}
	}

	public String getChannelName() {
		return this.channel;
	}

	public PacketBuffer getBufferData() {
		return this.data;
	}
}
