/*
 * Decompiled with CFR 0.145.
 */
package optifine;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optifine.Config;
import optifine.HttpListener;
import optifine.HttpPipeline;
import optifine.HttpPipelineReceiver;
import optifine.HttpPipelineRequest;
import optifine.HttpPipelineSender;
import optifine.HttpRequest;
import optifine.HttpResponse;

public class HttpPipelineConnection {
    private String host = null;
    private int port = 0;
    private Proxy proxy = Proxy.NO_PROXY;
    private List<HttpPipelineRequest> listRequests = new LinkedList<HttpPipelineRequest>();
    private List<HttpPipelineRequest> listRequestsSend = new LinkedList<HttpPipelineRequest>();
    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private HttpPipelineSender httpPipelineSender = null;
    private HttpPipelineReceiver httpPipelineReceiver = null;
    private int countRequests = 0;
    private boolean responseReceived = false;
    private long keepaliveTimeoutMs = 5000L;
    private int keepaliveMaxCount = 1000;
    private long timeLastActivityMs = System.currentTimeMillis();
    private boolean terminated = false;
    private static final String LF = "\n";
    public static final int TIMEOUT_CONNECT_MS = 5000;
    public static final int TIMEOUT_READ_MS = 5000;
    private static final Pattern patternFullUrl = Pattern.compile("^[a-zA-Z]+://.*");

    public HttpPipelineConnection(String host, int port) {
        this(host, port, Proxy.NO_PROXY);
    }

    public HttpPipelineConnection(String host, int port, Proxy proxy) {
        this.host = host;
        this.port = port;
        this.proxy = proxy;
        this.httpPipelineSender = new HttpPipelineSender(this);
        this.httpPipelineSender.start();
        this.httpPipelineReceiver = new HttpPipelineReceiver(this);
        this.httpPipelineReceiver.start();
    }

    public synchronized boolean addRequest(HttpPipelineRequest pr2) {
        if (this.isClosed()) {
            return false;
        }
        this.addRequest(pr2, this.listRequests);
        this.addRequest(pr2, this.listRequestsSend);
        ++this.countRequests;
        return true;
    }

    private void addRequest(HttpPipelineRequest pr2, List<HttpPipelineRequest> list) {
        list.add(pr2);
        this.notifyAll();
    }

    public synchronized void setSocket(Socket s2) throws IOException {
        if (!this.terminated) {
            if (this.socket != null) {
                throw new IllegalArgumentException("Already connected");
            }
            this.socket = s2;
            this.socket.setTcpNoDelay(true);
            this.inputStream = this.socket.getInputStream();
            this.outputStream = new BufferedOutputStream(this.socket.getOutputStream());
            this.onActivity();
            this.notifyAll();
        }
    }

    public synchronized OutputStream getOutputStream() throws IOException, InterruptedException {
        while (this.outputStream == null) {
            this.checkTimeout();
            this.wait(1000L);
        }
        return this.outputStream;
    }

    public synchronized InputStream getInputStream() throws IOException, InterruptedException {
        while (this.inputStream == null) {
            this.checkTimeout();
            this.wait(1000L);
        }
        return this.inputStream;
    }

    public synchronized HttpPipelineRequest getNextRequestSend() throws InterruptedException, IOException {
        if (this.listRequestsSend.size() <= 0 && this.outputStream != null) {
            this.outputStream.flush();
        }
        return this.getNextRequest(this.listRequestsSend, true);
    }

    public synchronized HttpPipelineRequest getNextRequestReceive() throws InterruptedException {
        return this.getNextRequest(this.listRequests, false);
    }

    private HttpPipelineRequest getNextRequest(List<HttpPipelineRequest> list, boolean remove) throws InterruptedException {
        while (list.size() <= 0) {
            this.checkTimeout();
            this.wait(1000L);
        }
        this.onActivity();
        if (remove) {
            return list.remove(0);
        }
        return list.get(0);
    }

    private void checkTimeout() {
        if (this.socket != null) {
            long timeNowMs;
            long timeoutMs = this.keepaliveTimeoutMs;
            if (this.listRequests.size() > 0) {
                timeoutMs = 5000L;
            }
            if ((timeNowMs = System.currentTimeMillis()) > this.timeLastActivityMs + timeoutMs) {
                this.terminate(new InterruptedException("Timeout " + timeoutMs));
            }
        }
    }

    private void onActivity() {
        this.timeLastActivityMs = System.currentTimeMillis();
    }

    public synchronized void onRequestSent(HttpPipelineRequest pr2) {
        if (!this.terminated) {
            this.onActivity();
        }
    }

    public synchronized void onResponseReceived(HttpPipelineRequest pr2, HttpResponse resp) {
        if (!this.terminated) {
            this.responseReceived = true;
            this.onActivity();
            if (this.listRequests.size() > 0 && this.listRequests.get(0) == pr2) {
                this.listRequests.remove(0);
                pr2.setClosed(true);
                String location = resp.getHeader("Location");
                if (resp.getStatus() / 100 == 3 && location != null && pr2.getHttpRequest().getRedirects() < 5) {
                    try {
                        location = this.normalizeUrl(location, pr2.getHttpRequest());
                        HttpRequest listener1 = HttpPipeline.makeRequest(location, pr2.getHttpRequest().getProxy());
                        listener1.setRedirects(pr2.getHttpRequest().getRedirects() + 1);
                        HttpPipelineRequest hpr2 = new HttpPipelineRequest(listener1, pr2.getHttpListener());
                        HttpPipeline.addRequest(hpr2);
                    }
                    catch (IOException var6) {
                        pr2.getHttpListener().failed(pr2.getHttpRequest(), var6);
                    }
                } else {
                    HttpListener listener = pr2.getHttpListener();
                    listener.finished(pr2.getHttpRequest(), resp);
                }
                this.checkResponseHeader(resp);
            } else {
                throw new IllegalArgumentException("Response out of order: " + pr2);
            }
        }
    }

    private String normalizeUrl(String url, HttpRequest hr2) {
        if (patternFullUrl.matcher(url).matches()) {
            return url;
        }
        if (url.startsWith("//")) {
            return "http:" + url;
        }
        String server = hr2.getHost();
        if (hr2.getPort() != 80) {
            server = String.valueOf(server) + ":" + hr2.getPort();
        }
        if (url.startsWith("/")) {
            return "http://" + server + url;
        }
        String file = hr2.getFile();
        int pos = file.lastIndexOf("/");
        return pos >= 0 ? "http://" + server + file.substring(0, pos + 1) + url : "http://" + server + "/" + url;
    }

    private void checkResponseHeader(HttpResponse resp) {
        String keepAliveStr;
        String connStr = resp.getHeader("Connection");
        if (connStr != null && !connStr.toLowerCase().equals("keep-alive")) {
            this.terminate(new EOFException("Connection not keep-alive"));
        }
        if ((keepAliveStr = resp.getHeader("Keep-Alive")) != null) {
            String[] parts = Config.tokenize(keepAliveStr, ",;");
            for (int i2 = 0; i2 < parts.length; ++i2) {
                int max;
                String part = parts[i2];
                String[] tokens = this.split(part, '=');
                if (tokens.length < 2) continue;
                if (tokens[0].equals("timeout") && (max = Config.parseInt(tokens[1], -1)) > 0) {
                    this.keepaliveTimeoutMs = max * 1000;
                }
                if (!tokens[0].equals("max") || (max = Config.parseInt(tokens[1], -1)) <= 0) continue;
                this.keepaliveMaxCount = max;
            }
        }
    }

    private String[] split(String str, char separator) {
        int pos = str.indexOf(separator);
        if (pos < 0) {
            return new String[]{str};
        }
        String str1 = str.substring(0, pos);
        String str2 = str.substring(pos + 1);
        return new String[]{str1, str2};
    }

    public synchronized void onExceptionSend(HttpPipelineRequest pr2, Exception e2) {
        this.terminate(e2);
    }

    public synchronized void onExceptionReceive(HttpPipelineRequest pr2, Exception e2) {
        this.terminate(e2);
    }

    private synchronized void terminate(Exception e2) {
        if (!this.terminated) {
            this.terminated = true;
            this.terminateRequests(e2);
            if (this.httpPipelineSender != null) {
                this.httpPipelineSender.interrupt();
            }
            if (this.httpPipelineReceiver != null) {
                this.httpPipelineReceiver.interrupt();
            }
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
            this.socket = null;
            this.inputStream = null;
            this.outputStream = null;
        }
    }

    private void terminateRequests(Exception e2) {
        if (this.listRequests.size() > 0) {
            HttpPipelineRequest pr2;
            if (!this.responseReceived) {
                pr2 = this.listRequests.remove(0);
                pr2.getHttpListener().failed(pr2.getHttpRequest(), e2);
                pr2.setClosed(true);
            }
            while (this.listRequests.size() > 0) {
                pr2 = this.listRequests.remove(0);
                HttpPipeline.addRequest(pr2);
            }
        }
    }

    public synchronized boolean isClosed() {
        return this.terminated ? true : this.countRequests >= this.keepaliveMaxCount;
    }

    public int getCountRequests() {
        return this.countRequests;
    }

    public synchronized boolean hasActiveRequests() {
        return this.listRequests.size() > 0;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public Proxy getProxy() {
        return this.proxy;
    }
}

