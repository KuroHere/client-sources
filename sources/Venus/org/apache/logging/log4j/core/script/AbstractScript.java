/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractScript {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected static final String DEFAULT_LANGUAGE = "JavaScript";
    private final String language;
    private final String scriptText;
    private final String name;

    public AbstractScript(String string, String string2, String string3) {
        this.language = string2;
        this.scriptText = string3;
        this.name = string == null ? this.toString() : string;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getScriptText() {
        return this.scriptText;
    }

    public String getName() {
        return this.name;
    }
}

