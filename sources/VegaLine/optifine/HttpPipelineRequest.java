/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package optifine;

import optifine.HttpListener;
import optifine.HttpRequest;

public class HttpPipelineRequest {
    private HttpRequest httpRequest = null;
    private HttpListener httpListener = null;
    private boolean closed = false;

    public HttpPipelineRequest(HttpRequest p_i58_1_, HttpListener p_i58_2_) {
        this.httpRequest = p_i58_1_;
        this.httpListener = p_i58_2_;
    }

    public HttpRequest getHttpRequest() {
        return this.httpRequest;
    }

    public HttpListener getHttpListener() {
        return this.httpListener;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean p_setClosed_1_) {
        this.closed = p_setClosed_1_;
    }
}

