/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class TextWebSocketFrame
extends WebSocketFrame {
    public TextWebSocketFrame() {
        super(Unpooled.buffer(0));
    }

    public TextWebSocketFrame(String string) {
        super(TextWebSocketFrame.fromText(string));
    }

    public TextWebSocketFrame(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public TextWebSocketFrame(boolean bl, int n, String string) {
        super(bl, n, TextWebSocketFrame.fromText(string));
    }

    private static ByteBuf fromText(String string) {
        if (string == null || string.isEmpty()) {
            return Unpooled.EMPTY_BUFFER;
        }
        return Unpooled.copiedBuffer(string, CharsetUtil.UTF_8);
    }

    public TextWebSocketFrame(boolean bl, int n, ByteBuf byteBuf) {
        super(bl, n, byteBuf);
    }

    public String text() {
        return this.content().toString(CharsetUtil.UTF_8);
    }

    @Override
    public TextWebSocketFrame copy() {
        return (TextWebSocketFrame)super.copy();
    }

    @Override
    public TextWebSocketFrame duplicate() {
        return (TextWebSocketFrame)super.duplicate();
    }

    @Override
    public TextWebSocketFrame retainedDuplicate() {
        return (TextWebSocketFrame)super.retainedDuplicate();
    }

    @Override
    public TextWebSocketFrame replace(ByteBuf byteBuf) {
        return new TextWebSocketFrame(this.isFinalFragment(), this.rsv(), byteBuf);
    }

    @Override
    public TextWebSocketFrame retain() {
        super.retain();
        return this;
    }

    @Override
    public TextWebSocketFrame retain(int n) {
        super.retain(n);
        return this;
    }

    @Override
    public TextWebSocketFrame touch() {
        super.touch();
        return this;
    }

    @Override
    public TextWebSocketFrame touch(Object object) {
        super.touch(object);
        return this;
    }

    @Override
    public WebSocketFrame touch(Object object) {
        return this.touch(object);
    }

    @Override
    public WebSocketFrame touch() {
        return this.touch();
    }

    @Override
    public WebSocketFrame retain(int n) {
        return this.retain(n);
    }

    @Override
    public WebSocketFrame retain() {
        return this.retain();
    }

    @Override
    public WebSocketFrame replace(ByteBuf byteBuf) {
        return this.replace(byteBuf);
    }

    @Override
    public WebSocketFrame retainedDuplicate() {
        return this.retainedDuplicate();
    }

    @Override
    public WebSocketFrame duplicate() {
        return this.duplicate();
    }

    @Override
    public WebSocketFrame copy() {
        return this.copy();
    }

    @Override
    public ByteBufHolder touch(Object object) {
        return this.touch(object);
    }

    @Override
    public ByteBufHolder touch() {
        return this.touch();
    }

    @Override
    public ByteBufHolder retain(int n) {
        return this.retain(n);
    }

    @Override
    public ByteBufHolder retain() {
        return this.retain();
    }

    @Override
    public ByteBufHolder replace(ByteBuf byteBuf) {
        return this.replace(byteBuf);
    }

    @Override
    public ByteBufHolder retainedDuplicate() {
        return this.retainedDuplicate();
    }

    @Override
    public ByteBufHolder duplicate() {
        return this.duplicate();
    }

    @Override
    public ByteBufHolder copy() {
        return this.copy();
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

