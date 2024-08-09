/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.channel;

import io.netty.channel.AddressedEnvelope;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

public class DefaultAddressedEnvelope<M, A extends SocketAddress>
implements AddressedEnvelope<M, A> {
    private final M message;
    private final A sender;
    private final A recipient;

    public DefaultAddressedEnvelope(M m, A a, A a2) {
        if (m == null) {
            throw new NullPointerException("message");
        }
        if (a == null && a2 == null) {
            throw new NullPointerException("recipient and sender");
        }
        this.message = m;
        this.sender = a2;
        this.recipient = a;
    }

    public DefaultAddressedEnvelope(M m, A a) {
        this(m, a, null);
    }

    @Override
    public M content() {
        return this.message;
    }

    @Override
    public A sender() {
        return this.sender;
    }

    @Override
    public A recipient() {
        return this.recipient;
    }

    @Override
    public int refCnt() {
        if (this.message instanceof ReferenceCounted) {
            return ((ReferenceCounted)this.message).refCnt();
        }
        return 0;
    }

    @Override
    public AddressedEnvelope<M, A> retain() {
        ReferenceCountUtil.retain(this.message);
        return this;
    }

    @Override
    public AddressedEnvelope<M, A> retain(int n) {
        ReferenceCountUtil.retain(this.message, n);
        return this;
    }

    @Override
    public boolean release() {
        return ReferenceCountUtil.release(this.message);
    }

    @Override
    public boolean release(int n) {
        return ReferenceCountUtil.release(this.message, n);
    }

    @Override
    public AddressedEnvelope<M, A> touch() {
        ReferenceCountUtil.touch(this.message);
        return this;
    }

    @Override
    public AddressedEnvelope<M, A> touch(Object object) {
        ReferenceCountUtil.touch(this.message, object);
        return this;
    }

    public String toString() {
        if (this.sender != null) {
            return StringUtil.simpleClassName(this) + '(' + this.sender + " => " + this.recipient + ", " + this.message + ')';
        }
        return StringUtil.simpleClassName(this) + "(=> " + this.recipient + ", " + this.message + ')';
    }

    @Override
    public ReferenceCounted touch(Object object) {
        return this.touch(object);
    }

    @Override
    public ReferenceCounted touch() {
        return this.touch();
    }

    @Override
    public ReferenceCounted retain(int n) {
        return this.retain(n);
    }

    @Override
    public ReferenceCounted retain() {
        return this.retain();
    }
}

