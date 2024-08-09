/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.comparator.AbstractFileComparator;

class ReverseComparator
extends AbstractFileComparator
implements Serializable {
    private static final long serialVersionUID = -4808255005272229056L;
    private final Comparator<File> delegate;

    public ReverseComparator(Comparator<File> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Delegate comparator is missing");
        }
        this.delegate = comparator;
    }

    @Override
    public int compare(File file, File file2) {
        return this.delegate.compare(file2, file);
    }

    @Override
    public String toString() {
        return super.toString() + "[" + this.delegate.toString() + "]";
    }

    @Override
    public int compare(Object object, Object object2) {
        return this.compare((File)object, (File)object2);
    }
}

