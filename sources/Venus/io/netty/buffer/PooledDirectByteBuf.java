/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBuf;
import io.netty.util.Recycler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

final class PooledDirectByteBuf
extends PooledByteBuf<ByteBuffer> {
    private static final Recycler<PooledDirectByteBuf> RECYCLER = new Recycler<PooledDirectByteBuf>(){

        @Override
        protected PooledDirectByteBuf newObject(Recycler.Handle<PooledDirectByteBuf> handle) {
            return new PooledDirectByteBuf(handle, 0, null);
        }

        @Override
        protected Object newObject(Recycler.Handle handle) {
            return this.newObject(handle);
        }
    };

    static PooledDirectByteBuf newInstance(int n) {
        PooledDirectByteBuf pooledDirectByteBuf = RECYCLER.get();
        pooledDirectByteBuf.reuse(n);
        return pooledDirectByteBuf;
    }

    private PooledDirectByteBuf(Recycler.Handle<PooledDirectByteBuf> handle, int n) {
        super(handle, n);
    }

    @Override
    protected ByteBuffer newInternalNioBuffer(ByteBuffer byteBuffer) {
        return byteBuffer.duplicate();
    }

    @Override
    public boolean isDirect() {
        return false;
    }

    @Override
    protected byte _getByte(int n) {
        return ((ByteBuffer)this.memory).get(this.idx(n));
    }

    @Override
    protected short _getShort(int n) {
        return ((ByteBuffer)this.memory).getShort(this.idx(n));
    }

    @Override
    protected short _getShortLE(int n) {
        return ByteBufUtil.swapShort(this._getShort(n));
    }

    @Override
    protected int _getUnsignedMedium(int n) {
        n = this.idx(n);
        return (((ByteBuffer)this.memory).get(n) & 0xFF) << 16 | (((ByteBuffer)this.memory).get(n + 1) & 0xFF) << 8 | ((ByteBuffer)this.memory).get(n + 2) & 0xFF;
    }

    @Override
    protected int _getUnsignedMediumLE(int n) {
        n = this.idx(n);
        return ((ByteBuffer)this.memory).get(n) & 0xFF | (((ByteBuffer)this.memory).get(n + 1) & 0xFF) << 8 | (((ByteBuffer)this.memory).get(n + 2) & 0xFF) << 16;
    }

    @Override
    protected int _getInt(int n) {
        return ((ByteBuffer)this.memory).getInt(this.idx(n));
    }

    @Override
    protected int _getIntLE(int n) {
        return ByteBufUtil.swapInt(this._getInt(n));
    }

    @Override
    protected long _getLong(int n) {
        return ((ByteBuffer)this.memory).getLong(this.idx(n));
    }

    @Override
    protected long _getLongLE(int n) {
        return ByteBufUtil.swapLong(this._getLong(n));
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkDstIndex(n, n3, n2, byteBuf.capacity());
        if (byteBuf.hasArray()) {
            this.getBytes(n, byteBuf.array(), byteBuf.arrayOffset() + n2, n3);
        } else if (byteBuf.nioBufferCount() > 0) {
            for (ByteBuffer byteBuffer : byteBuf.nioBuffers(n2, n3)) {
                int n4 = byteBuffer.remaining();
                this.getBytes(n, byteBuffer);
                n += n4;
            }
        } else {
            byteBuf.setBytes(n2, this, n, n3);
        }
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, byte[] byArray, int n2, int n3) {
        this.getBytes(n, byArray, n2, n3, false);
        return this;
    }

    private void getBytes(int n, byte[] byArray, int n2, int n3, boolean bl) {
        this.checkDstIndex(n, n3, n2, byArray.length);
        ByteBuffer byteBuffer = bl ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n3);
        byteBuffer.get(byArray, n2, n3);
    }

    @Override
    public ByteBuf readBytes(byte[] byArray, int n, int n2) {
        this.checkReadableBytes(n2);
        this.getBytes(this.readerIndex, byArray, n, n2, true);
        this.readerIndex += n2;
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, ByteBuffer byteBuffer) {
        this.getBytes(n, byteBuffer, false);
        return this;
    }

    private void getBytes(int n, ByteBuffer byteBuffer, boolean bl) {
        this.checkIndex(n, byteBuffer.remaining());
        ByteBuffer byteBuffer2 = bl ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        n = this.idx(n);
        byteBuffer2.clear().position(n).limit(n + byteBuffer.remaining());
        byteBuffer.put(byteBuffer2);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        int n = byteBuffer.remaining();
        this.checkReadableBytes(n);
        this.getBytes(this.readerIndex, byteBuffer, true);
        this.readerIndex += n;
        return this;
    }

    @Override
    public ByteBuf getBytes(int n, OutputStream outputStream, int n2) throws IOException {
        this.getBytes(n, outputStream, n2, false);
        return this;
    }

    private void getBytes(int n, OutputStream outputStream, int n2, boolean bl) throws IOException {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return;
        }
        ByteBufUtil.readBytes(this.alloc(), bl ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate(), this.idx(n), n2, outputStream);
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int n) throws IOException {
        this.checkReadableBytes(n);
        this.getBytes(this.readerIndex, outputStream, n, true);
        this.readerIndex += n;
        return this;
    }

    @Override
    public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
        return this.getBytes(n, gatheringByteChannel, n2, false);
    }

    private int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2, boolean bl) throws IOException {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return 1;
        }
        ByteBuffer byteBuffer = bl ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n2);
        return gatheringByteChannel.write(byteBuffer);
    }

    @Override
    public int getBytes(int n, FileChannel fileChannel, long l, int n2) throws IOException {
        return this.getBytes(n, fileChannel, l, n2, false);
    }

    private int getBytes(int n, FileChannel fileChannel, long l, int n2, boolean bl) throws IOException {
        this.checkIndex(n, n2);
        if (n2 == 0) {
            return 1;
        }
        ByteBuffer byteBuffer = bl ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n2);
        return fileChannel.write(byteBuffer, l);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
        this.checkReadableBytes(n);
        int n2 = this.getBytes(this.readerIndex, gatheringByteChannel, n, true);
        this.readerIndex += n2;
        return n2;
    }

    @Override
    public int readBytes(FileChannel fileChannel, long l, int n) throws IOException {
        this.checkReadableBytes(n);
        int n2 = this.getBytes(this.readerIndex, fileChannel, l, n, true);
        this.readerIndex += n2;
        return n2;
    }

    @Override
    protected void _setByte(int n, int n2) {
        ((ByteBuffer)this.memory).put(this.idx(n), (byte)n2);
    }

    @Override
    protected void _setShort(int n, int n2) {
        ((ByteBuffer)this.memory).putShort(this.idx(n), (short)n2);
    }

    @Override
    protected void _setShortLE(int n, int n2) {
        this._setShort(n, ByteBufUtil.swapShort((short)n2));
    }

    @Override
    protected void _setMedium(int n, int n2) {
        n = this.idx(n);
        ((ByteBuffer)this.memory).put(n, (byte)(n2 >>> 16));
        ((ByteBuffer)this.memory).put(n + 1, (byte)(n2 >>> 8));
        ((ByteBuffer)this.memory).put(n + 2, (byte)n2);
    }

    @Override
    protected void _setMediumLE(int n, int n2) {
        n = this.idx(n);
        ((ByteBuffer)this.memory).put(n, (byte)n2);
        ((ByteBuffer)this.memory).put(n + 1, (byte)(n2 >>> 8));
        ((ByteBuffer)this.memory).put(n + 2, (byte)(n2 >>> 16));
    }

    @Override
    protected void _setInt(int n, int n2) {
        ((ByteBuffer)this.memory).putInt(this.idx(n), n2);
    }

    @Override
    protected void _setIntLE(int n, int n2) {
        this._setInt(n, ByteBufUtil.swapInt(n2));
    }

    @Override
    protected void _setLong(int n, long l) {
        ((ByteBuffer)this.memory).putLong(this.idx(n), l);
    }

    @Override
    protected void _setLongLE(int n, long l) {
        this._setLong(n, ByteBufUtil.swapLong(l));
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuf byteBuf, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byteBuf.capacity());
        if (byteBuf.hasArray()) {
            this.setBytes(n, byteBuf.array(), byteBuf.arrayOffset() + n2, n3);
        } else if (byteBuf.nioBufferCount() > 0) {
            for (ByteBuffer byteBuffer : byteBuf.nioBuffers(n2, n3)) {
                int n4 = byteBuffer.remaining();
                this.setBytes(n, byteBuffer);
                n += n4;
            }
        } else {
            byteBuf.getBytes(n2, this, n, n3);
        }
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, byte[] byArray, int n2, int n3) {
        this.checkSrcIndex(n, n3, n2, byArray.length);
        ByteBuffer byteBuffer = this.internalNioBuffer();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n3);
        byteBuffer.put(byArray, n2, n3);
        return this;
    }

    @Override
    public ByteBuf setBytes(int n, ByteBuffer byteBuffer) {
        this.checkIndex(n, byteBuffer.remaining());
        ByteBuffer byteBuffer2 = this.internalNioBuffer();
        if (byteBuffer == byteBuffer2) {
            byteBuffer = byteBuffer.duplicate();
        }
        n = this.idx(n);
        byteBuffer2.clear().position(n).limit(n + byteBuffer.remaining());
        byteBuffer2.put(byteBuffer);
        return this;
    }

    @Override
    public int setBytes(int n, InputStream inputStream, int n2) throws IOException {
        this.checkIndex(n, n2);
        byte[] byArray = new byte[n2];
        int n3 = inputStream.read(byArray);
        if (n3 <= 0) {
            return n3;
        }
        ByteBuffer byteBuffer = this.internalNioBuffer();
        byteBuffer.clear().position(this.idx(n));
        byteBuffer.put(byArray, 0, n3);
        return n3;
    }

    @Override
    public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
        this.checkIndex(n, n2);
        ByteBuffer byteBuffer = this.internalNioBuffer();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n2);
        try {
            return scatteringByteChannel.read(byteBuffer);
        } catch (ClosedChannelException closedChannelException) {
            return 1;
        }
    }

    @Override
    public int setBytes(int n, FileChannel fileChannel, long l, int n2) throws IOException {
        this.checkIndex(n, n2);
        ByteBuffer byteBuffer = this.internalNioBuffer();
        n = this.idx(n);
        byteBuffer.clear().position(n).limit(n + n2);
        try {
            return fileChannel.read(byteBuffer, l);
        } catch (ClosedChannelException closedChannelException) {
            return 1;
        }
    }

    @Override
    public ByteBuf copy(int n, int n2) {
        this.checkIndex(n, n2);
        ByteBuf byteBuf = this.alloc().directBuffer(n2, this.maxCapacity());
        byteBuf.writeBytes(this, n, n2);
        return byteBuf;
    }

    @Override
    public int nioBufferCount() {
        return 0;
    }

    @Override
    public ByteBuffer nioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        n = this.idx(n);
        return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(n).limit(n + n2)).slice();
    }

    @Override
    public ByteBuffer[] nioBuffers(int n, int n2) {
        return new ByteBuffer[]{this.nioBuffer(n, n2)};
    }

    @Override
    public ByteBuffer internalNioBuffer(int n, int n2) {
        this.checkIndex(n, n2);
        n = this.idx(n);
        return (ByteBuffer)this.internalNioBuffer().clear().position(n).limit(n + n2);
    }

    @Override
    public boolean hasArray() {
        return true;
    }

    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override
    public boolean hasMemoryAddress() {
        return true;
    }

    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ByteBuffer newInternalNioBuffer(Object object) {
        return this.newInternalNioBuffer((ByteBuffer)object);
    }

    PooledDirectByteBuf(Recycler.Handle handle, int n, 1 var3_3) {
        this(handle, n);
    }
}

