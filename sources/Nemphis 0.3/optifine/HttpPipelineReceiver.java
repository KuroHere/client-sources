/*
 * Decompiled with CFR 0_118.
 */
package optifine;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import optifine.Config;
import optifine.HttpPipelineConnection;
import optifine.HttpPipelineRequest;
import optifine.HttpResponse;

public class HttpPipelineReceiver
extends Thread {
    private HttpPipelineConnection httpPipelineConnection = null;
    private static final Charset ASCII = Charset.forName("ASCII");
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final char CR = '\r';
    private static final char LF = '\n';

    public HttpPipelineReceiver(HttpPipelineConnection httpPipelineConnection) {
        super("HttpPipelineReceiver");
        this.httpPipelineConnection = httpPipelineConnection;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            HttpPipelineRequest currentRequest = null;
            try {
                currentRequest = this.httpPipelineConnection.getNextRequestReceive();
                InputStream e = this.httpPipelineConnection.getInputStream();
                HttpResponse resp = this.readResponse(e);
                this.httpPipelineConnection.onResponseReceived(currentRequest, resp);
                continue;
            }
            catch (InterruptedException var4) {
                return;
            }
            catch (Exception var5) {
                this.httpPipelineConnection.onExceptionReceive(currentRequest, var5);
            }
        }
    }

    private HttpResponse readResponse(InputStream in) throws IOException {
        String statusLine = this.readLine(in);
        String[] parts = Config.tokenize(statusLine, " ");
        if (parts.length < 3) {
            throw new IOException("Invalid status line: " + statusLine);
        }
        String http = parts[0];
        int status = Config.parseInt(parts[1], 0);
        String message = parts[2];
        LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
        do {
            String body;
            String enc;
            if ((body = this.readLine(in)).length() <= 0) {
                byte[] body1 = null;
                String lenStr1 = (String)headers.get("Content-Length");
                if (lenStr1 != null) {
                    int enc1 = Config.parseInt(lenStr1, -1);
                    if (enc1 > 0) {
                        body1 = new byte[enc1];
                        this.readFull(body1, in);
                    }
                } else {
                    enc = (String)headers.get("Transfer-Encoding");
                    if (Config.equals(enc, "chunked")) {
                        body1 = this.readContentChunked(in);
                    }
                }
                return new HttpResponse(status, statusLine, headers, body1);
            }
            int lenStr = body.indexOf(":");
            if (lenStr <= 0) continue;
            enc = body.substring(0, lenStr).trim();
            String val = body.substring(lenStr + 1).trim();
            headers.put(enc, val);
        } while (true);
    }

    private byte[] readContentChunked(InputStream in) throws IOException {
        int len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        do {
            String line = this.readLine(in);
            String[] parts = Config.tokenize(line, "; ");
            len = Integer.parseInt(parts[0], 16);
            byte[] buf = new byte[len];
            this.readFull(buf, in);
            baos.write(buf);
            this.readLine(in);
        } while (len != 0);
        return baos.toByteArray();
    }

    private void readFull(byte[] buf, InputStream in) throws IOException {
        int pos = 0;
        while (pos < buf.length) {
            int len = in.read(buf, pos, buf.length - pos);
            if (len < 0) {
                throw new EOFException();
            }
            pos += len;
        }
    }

    private String readLine(InputStream in) throws IOException {
        int bytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int prev = -1;
        boolean hasCRLF = false;
        while ((bytes = in.read()) >= 0) {
            baos.write(bytes);
            if (prev == 13 && bytes == 10) {
                hasCRLF = true;
                break;
            }
            prev = bytes;
        }
        byte[] bytes1 = baos.toByteArray();
        String str = new String(bytes1, ASCII);
        if (hasCRLF) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
}

