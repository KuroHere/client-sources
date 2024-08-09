/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.handler.codec.serialization;

import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.CompactObjectInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.StreamCorruptedException;

public class ObjectDecoderInputStream
extends InputStream
implements ObjectInput {
    private final DataInputStream in;
    private final int maxObjectSize;
    private final ClassResolver classResolver;

    public ObjectDecoderInputStream(InputStream inputStream) {
        this(inputStream, null);
    }

    public ObjectDecoderInputStream(InputStream inputStream, ClassLoader classLoader) {
        this(inputStream, classLoader, 0x100000);
    }

    public ObjectDecoderInputStream(InputStream inputStream, int n) {
        this(inputStream, null, n);
    }

    public ObjectDecoderInputStream(InputStream inputStream, ClassLoader classLoader, int n) {
        if (inputStream == null) {
            throw new NullPointerException("in");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("maxObjectSize: " + n);
        }
        this.in = inputStream instanceof DataInputStream ? (DataInputStream)inputStream : new DataInputStream(inputStream);
        this.classResolver = ClassResolvers.weakCachingResolver(classLoader);
        this.maxObjectSize = n;
    }

    @Override
    public Object readObject() throws ClassNotFoundException, IOException {
        int n = this.readInt();
        if (n <= 0) {
            throw new StreamCorruptedException("invalid data length: " + n);
        }
        if (n > this.maxObjectSize) {
            throw new StreamCorruptedException("data length too big: " + n + " (max: " + this.maxObjectSize + ')');
        }
        return new CompactObjectInputStream(this.in, this.classResolver).readObject();
    }

    @Override
    public int available() throws IOException {
        return this.in.available();
    }

    @Override
    public void close() throws IOException {
        this.in.close();
    }

    @Override
    public void mark(int n) {
        this.in.mark(n);
    }

    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }

    @Override
    public int read() throws IOException {
        return this.in.read();
    }

    @Override
    public final int read(byte[] byArray, int n, int n2) throws IOException {
        return this.in.read(byArray, n, n2);
    }

    @Override
    public final int read(byte[] byArray) throws IOException {
        return this.in.read(byArray);
    }

    @Override
    public final boolean readBoolean() throws IOException {
        return this.in.readBoolean();
    }

    @Override
    public final byte readByte() throws IOException {
        return this.in.readByte();
    }

    @Override
    public final char readChar() throws IOException {
        return this.in.readChar();
    }

    @Override
    public final double readDouble() throws IOException {
        return this.in.readDouble();
    }

    @Override
    public final float readFloat() throws IOException {
        return this.in.readFloat();
    }

    @Override
    public final void readFully(byte[] byArray, int n, int n2) throws IOException {
        this.in.readFully(byArray, n, n2);
    }

    @Override
    public final void readFully(byte[] byArray) throws IOException {
        this.in.readFully(byArray);
    }

    @Override
    public final int readInt() throws IOException {
        return this.in.readInt();
    }

    @Override
    @Deprecated
    public final String readLine() throws IOException {
        return this.in.readLine();
    }

    @Override
    public final long readLong() throws IOException {
        return this.in.readLong();
    }

    @Override
    public final short readShort() throws IOException {
        return this.in.readShort();
    }

    @Override
    public final int readUnsignedByte() throws IOException {
        return this.in.readUnsignedByte();
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        return this.in.readUnsignedShort();
    }

    @Override
    public final String readUTF() throws IOException {
        return this.in.readUTF();
    }

    @Override
    public void reset() throws IOException {
        this.in.reset();
    }

    @Override
    public long skip(long l) throws IOException {
        return this.in.skip(l);
    }

    @Override
    public final int skipBytes(int n) throws IOException {
        return this.in.skipBytes(n);
    }
}

