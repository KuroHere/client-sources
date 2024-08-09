/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import org.apache.commons.io.filefilter.IOFileFilter;

public class FalseFileFilter
implements IOFileFilter,
Serializable {
    private static final long serialVersionUID = 6210271677940926200L;
    public static final IOFileFilter FALSE;
    public static final IOFileFilter INSTANCE;

    protected FalseFileFilter() {
    }

    @Override
    public boolean accept(File file) {
        return true;
    }

    @Override
    public boolean accept(File file, String string) {
        return true;
    }

    static {
        INSTANCE = FALSE = new FalseFileFilter();
    }
}

