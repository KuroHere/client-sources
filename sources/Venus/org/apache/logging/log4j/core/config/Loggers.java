/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.logging.log4j.core.config;

import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Loggers {
    private final ConcurrentMap<String, LoggerConfig> map;
    private final LoggerConfig root;

    public Loggers(ConcurrentMap<String, LoggerConfig> concurrentMap, LoggerConfig loggerConfig) {
        this.map = concurrentMap;
        this.root = loggerConfig;
    }

    public ConcurrentMap<String, LoggerConfig> getMap() {
        return this.map;
    }

    public LoggerConfig getRoot() {
        return this.root;
    }
}

