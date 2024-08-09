/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.compress.archivers.sevenz;

import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;

public class SevenZMethodConfiguration {
    private final SevenZMethod method;
    private final Object options;

    public SevenZMethodConfiguration(SevenZMethod sevenZMethod) {
        this(sevenZMethod, null);
    }

    public SevenZMethodConfiguration(SevenZMethod sevenZMethod, Object object) {
        this.method = sevenZMethod;
        this.options = object;
        if (object != null && !Coders.findByMethod(sevenZMethod).canAcceptOptions(object)) {
            throw new IllegalArgumentException("The " + (Object)((Object)sevenZMethod) + " method doesn't support options of type " + object.getClass());
        }
    }

    public SevenZMethod getMethod() {
        return this.method;
    }

    public Object getOptions() {
        return this.options;
    }
}

