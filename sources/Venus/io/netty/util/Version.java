/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public final class Version {
    private static final String PROP_VERSION = ".version";
    private static final String PROP_BUILD_DATE = ".buildDate";
    private static final String PROP_COMMIT_DATE = ".commitDate";
    private static final String PROP_SHORT_COMMIT_HASH = ".shortCommitHash";
    private static final String PROP_LONG_COMMIT_HASH = ".longCommitHash";
    private static final String PROP_REPO_STATUS = ".repoStatus";
    private final String artifactId;
    private final String artifactVersion;
    private final long buildTimeMillis;
    private final long commitTimeMillis;
    private final String shortCommitHash;
    private final String longCommitHash;
    private final String repositoryStatus;

    public static Map<String, Version> identify() {
        return Version.identify(null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Map<String, Version> identify(ClassLoader classLoader) {
        String string;
        Object object3;
        TreeMap<String, Version> treeMap;
        Object object2;
        if (classLoader == null) {
            classLoader = PlatformDependent.getContextClassLoader();
        }
        Properties properties = new Properties();
        try {
            object2 = classLoader.getResources("META-INF/io.netty.versions.properties");
            while (object2.hasMoreElements()) {
                treeMap = (URL)object2.nextElement();
                object3 = ((URL)((Object)treeMap)).openStream();
                try {
                    properties.load((InputStream)object3);
                } finally {
                    try {
                        ((InputStream)object3).close();
                    } catch (Exception exception) {}
                }
            }
        } catch (Exception exception) {
            // empty catch block
        }
        object2 = new HashSet();
        for (Object object3 : properties.keySet()) {
            String string2;
            string = (String)object3;
            int n = string.indexOf(46);
            if (n <= 0 || !properties.containsKey((string2 = string.substring(0, n)) + PROP_VERSION) || !properties.containsKey(string2 + PROP_BUILD_DATE) || !properties.containsKey(string2 + PROP_COMMIT_DATE) || !properties.containsKey(string2 + PROP_SHORT_COMMIT_HASH) || !properties.containsKey(string2 + PROP_LONG_COMMIT_HASH) || !properties.containsKey(string2 + PROP_REPO_STATUS)) continue;
            object2.add(string2);
        }
        treeMap = new TreeMap<String, Version>();
        object3 = object2.iterator();
        while (object3.hasNext()) {
            string = (String)object3.next();
            treeMap.put(string, new Version(string, properties.getProperty(string + PROP_VERSION), Version.parseIso8601(properties.getProperty(string + PROP_BUILD_DATE)), Version.parseIso8601(properties.getProperty(string + PROP_COMMIT_DATE)), properties.getProperty(string + PROP_SHORT_COMMIT_HASH), properties.getProperty(string + PROP_LONG_COMMIT_HASH), properties.getProperty(string + PROP_REPO_STATUS)));
        }
        return treeMap;
    }

    private static long parseIso8601(String string) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(string).getTime();
        } catch (ParseException parseException) {
            return 0L;
        }
    }

    public static void main(String[] stringArray) {
        for (Version version : Version.identify().values()) {
            System.err.println(version);
        }
    }

    private Version(String string, String string2, long l, long l2, String string3, String string4, String string5) {
        this.artifactId = string;
        this.artifactVersion = string2;
        this.buildTimeMillis = l;
        this.commitTimeMillis = l2;
        this.shortCommitHash = string3;
        this.longCommitHash = string4;
        this.repositoryStatus = string5;
    }

    public String artifactId() {
        return this.artifactId;
    }

    public String artifactVersion() {
        return this.artifactVersion;
    }

    public long buildTimeMillis() {
        return this.buildTimeMillis;
    }

    public long commitTimeMillis() {
        return this.commitTimeMillis;
    }

    public String shortCommitHash() {
        return this.shortCommitHash;
    }

    public String longCommitHash() {
        return this.longCommitHash;
    }

    public String repositoryStatus() {
        return this.repositoryStatus;
    }

    public String toString() {
        return this.artifactId + '-' + this.artifactVersion + '.' + this.shortCommitHash + ("clean".equals(this.repositoryStatus) ? "" : " (repository: " + this.repositoryStatus + ')');
    }
}

