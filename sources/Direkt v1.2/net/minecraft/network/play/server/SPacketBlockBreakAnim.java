package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockBreakAnim implements Packet<INetHandlerPlayClient> {
	private int breakerId;
	private BlockPos position;
	private int progress;

	public SPacketBlockBreakAnim() {
	}

	public SPacketBlockBreakAnim(int breakerIdIn, BlockPos positionIn, int progressIn) {
		this.breakerId = breakerIdIn;
		this.position = positionIn;
		this.progress = progressIn;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.breakerId = buf.readVarIntFromBuffer();
		this.position = buf.readBlockPos();
		this.progress = buf.readUnsignedByte();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeVarIntToBuffer(this.breakerId);
		buf.writeBlockPos(this.position);
		buf.writeByte(this.progress);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	@Override
	public void processPacket(INetHandlerPlayClient handler) {
		handler.handleBlockBreakAnim(this);
	}

	public int getBreakerId() {
		return this.breakerId;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public int getProgress() {
		return this.progress;
	}
}
