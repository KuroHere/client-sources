/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker00;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker07;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker08;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker13;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import java.net.URI;

public final class WebSocketClientHandshakerFactory {
    private WebSocketClientHandshakerFactory() {
    }

    public static WebSocketClientHandshaker newHandshaker(URI uRI, WebSocketVersion webSocketVersion, String string, boolean bl, HttpHeaders httpHeaders) {
        return WebSocketClientHandshakerFactory.newHandshaker(uRI, webSocketVersion, string, bl, httpHeaders, 65536);
    }

    public static WebSocketClientHandshaker newHandshaker(URI uRI, WebSocketVersion webSocketVersion, String string, boolean bl, HttpHeaders httpHeaders, int n) {
        return WebSocketClientHandshakerFactory.newHandshaker(uRI, webSocketVersion, string, bl, httpHeaders, n, true, false);
    }

    public static WebSocketClientHandshaker newHandshaker(URI uRI, WebSocketVersion webSocketVersion, String string, boolean bl, HttpHeaders httpHeaders, int n, boolean bl2, boolean bl3) {
        if (webSocketVersion == WebSocketVersion.V13) {
            return new WebSocketClientHandshaker13(uRI, WebSocketVersion.V13, string, bl, httpHeaders, n, bl2, bl3);
        }
        if (webSocketVersion == WebSocketVersion.V08) {
            return new WebSocketClientHandshaker08(uRI, WebSocketVersion.V08, string, bl, httpHeaders, n, bl2, bl3);
        }
        if (webSocketVersion == WebSocketVersion.V07) {
            return new WebSocketClientHandshaker07(uRI, WebSocketVersion.V07, string, bl, httpHeaders, n, bl2, bl3);
        }
        if (webSocketVersion == WebSocketVersion.V00) {
            return new WebSocketClientHandshaker00(uRI, WebSocketVersion.V00, string, httpHeaders, n);
        }
        throw new WebSocketHandshakeException("Protocol version " + (Object)((Object)webSocketVersion) + " not supported.");
    }
}

