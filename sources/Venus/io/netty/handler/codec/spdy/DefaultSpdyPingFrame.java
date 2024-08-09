/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyPingFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyPingFrame
implements SpdyPingFrame {
    private int id;

    public DefaultSpdyPingFrame(int n) {
        this.setId(n);
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public SpdyPingFrame setId(int n) {
        this.id = n;
        return this;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> ID = " + this.id();
    }
}

