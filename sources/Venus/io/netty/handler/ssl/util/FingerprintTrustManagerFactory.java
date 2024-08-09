/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.handler.ssl.util;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.ssl.util.SimpleTrustManagerFactory;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.StringUtil;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class FingerprintTrustManagerFactory
extends SimpleTrustManagerFactory {
    private static final Pattern FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
    private static final Pattern FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
    private static final int SHA1_BYTE_LEN = 20;
    private static final int SHA1_HEX_LEN = 40;
    private static final FastThreadLocal<MessageDigest> tlmd = new FastThreadLocal<MessageDigest>(){

        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                throw new Error(noSuchAlgorithmException);
            }
        }

        @Override
        protected Object initialValue() throws Exception {
            return this.initialValue();
        }
    };
    private final TrustManager tm = new X509TrustManager(this){
        final FingerprintTrustManagerFactory this$0;
        {
            this.this$0 = fingerprintTrustManagerFactory;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
            this.checkTrusted("client", x509CertificateArray);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) throws CertificateException {
            this.checkTrusted("server", x509CertificateArray);
        }

        private void checkTrusted(String string, X509Certificate[] x509CertificateArray) throws CertificateException {
            X509Certificate x509Certificate = x509CertificateArray[0];
            byte[] byArray = this.fingerprint(x509Certificate);
            boolean bl = false;
            for (byte[] byArray2 : FingerprintTrustManagerFactory.access$000(this.this$0)) {
                if (!Arrays.equals(byArray, byArray2)) continue;
                bl = true;
                break;
            }
            if (!bl) {
                throw new CertificateException(string + " certificate with unknown fingerprint: " + x509Certificate.getSubjectDN());
            }
        }

        private byte[] fingerprint(X509Certificate x509Certificate) throws CertificateEncodingException {
            MessageDigest messageDigest = (MessageDigest)FingerprintTrustManagerFactory.access$100().get();
            messageDigest.reset();
            return messageDigest.digest(x509Certificate.getEncoded());
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return EmptyArrays.EMPTY_X509_CERTIFICATES;
        }
    };
    private final byte[][] fingerprints;

    public FingerprintTrustManagerFactory(Iterable<String> iterable) {
        this(FingerprintTrustManagerFactory.toFingerprintArray(iterable));
    }

    public FingerprintTrustManagerFactory(String ... stringArray) {
        this(FingerprintTrustManagerFactory.toFingerprintArray(Arrays.asList(stringArray)));
    }

    public FingerprintTrustManagerFactory(byte[] ... byArray) {
        if (byArray == null) {
            throw new NullPointerException("fingerprints");
        }
        ArrayList<Object> arrayList = new ArrayList<Object>(byArray.length);
        for (byte[] byArray2 : byArray) {
            if (byArray2 == null) break;
            if (byArray2.length != 20) {
                throw new IllegalArgumentException("malformed fingerprint: " + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(byArray2)) + " (expected: SHA1)");
            }
            arrayList.add(byArray2.clone());
        }
        this.fingerprints = (byte[][])arrayList.toArray((T[])new byte[arrayList.size()][]);
    }

    private static byte[][] toFingerprintArray(Iterable<String> iterable) {
        if (iterable == null) {
            throw new NullPointerException("fingerprints");
        }
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        for (String string : iterable) {
            if (string == null) break;
            if (!FINGERPRINT_PATTERN.matcher(string).matches()) {
                throw new IllegalArgumentException("malformed fingerprint: " + string);
            }
            if ((string = FINGERPRINT_STRIP_PATTERN.matcher(string).replaceAll("")).length() != 40) {
                throw new IllegalArgumentException("malformed fingerprint: " + string + " (expected: SHA1)");
            }
            arrayList.add(StringUtil.decodeHexDump(string));
        }
        return (byte[][])arrayList.toArray((T[])new byte[arrayList.size()][]);
    }

    @Override
    protected void engineInit(KeyStore keyStore) throws Exception {
    }

    @Override
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        return new TrustManager[]{this.tm};
    }

    static byte[][] access$000(FingerprintTrustManagerFactory fingerprintTrustManagerFactory) {
        return fingerprintTrustManagerFactory.fingerprints;
    }

    static FastThreadLocal access$100() {
        return tlmd;
    }
}

