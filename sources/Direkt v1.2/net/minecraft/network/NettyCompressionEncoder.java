package net.minecraft.network;

import java.util.zip.Deflater;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf> {
	private final byte[] buffer = new byte[8192];
	private final Deflater deflater;
	private int threshold;

	public NettyCompressionEncoder(int thresholdIn) {
		this.threshold = thresholdIn;
		this.deflater = new Deflater();
	}

	@Override
	protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_) throws Exception {
		int i = p_encode_2_.readableBytes();
		PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);

		if (i < this.threshold) {
			packetbuffer.writeVarIntToBuffer(0);
			packetbuffer.writeBytes(p_encode_2_);
		} else {
			byte[] abyte = new byte[i];
			p_encode_2_.readBytes(abyte);
			packetbuffer.writeVarIntToBuffer(abyte.length);
			this.deflater.setInput(abyte, 0, i);
			this.deflater.finish();

			while (!this.deflater.finished()) {
				int j = this.deflater.deflate(this.buffer);
				packetbuffer.writeBytes(this.buffer, 0, j);
			}

			this.deflater.reset();
		}
	}

	public void setCompressionThreshold(int thresholdIn) {
		this.threshold = thresholdIn;
	}
}
