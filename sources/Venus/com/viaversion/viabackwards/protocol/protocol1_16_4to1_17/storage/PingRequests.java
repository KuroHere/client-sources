/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

public final class PingRequests
implements StorableObject {
    private final IntSet ids = new IntOpenHashSet();

    public void addId(short s) {
        this.ids.add(s);
    }

    public boolean removeId(short s) {
        return this.ids.remove(s);
    }
}

