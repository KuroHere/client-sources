package net.minecraft.network.login.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketEnableCompression implements Packet<INetHandlerLoginClient> {
	private int compressionThreshold;

	public SPacketEnableCompression() {
	}

	public SPacketEnableCompression(int thresholdIn) {
		this.compressionThreshold = thresholdIn;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.compressionThreshold = buf.readVarIntFromBuffer();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeVarIntToBuffer(this.compressionThreshold);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	@Override
	public void processPacket(INetHandlerLoginClient handler) {
		handler.handleEnableCompression(this);
	}

	public int getCompressionThreshold() {
		return this.compressionThreshold;
	}
}
