/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil;

public interface Size64 {
    public long size64();

    @Deprecated
    default public int size() {
        return (int)Math.min(Integer.MAX_VALUE, this.size64());
    }
}

