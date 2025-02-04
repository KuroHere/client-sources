/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Object2DoubleMap<K>
extends Object2DoubleFunction<K>,
Map<K, Double> {
    @Override
    public ObjectSet<Map.Entry<K, Double>> entrySet();

    public ObjectSet<Entry<K>> object2DoubleEntrySet();

    @Override
    public ObjectSet<K> keySet();

    public DoubleCollection values();

    public boolean containsValue(double var1);

    public static interface Entry<K>
    extends Map.Entry<K, Double> {
        @Override
        @Deprecated
        public Double getValue();

        @Override
        public double setValue(double var1);

        public double getDoubleValue();
    }

    public static interface FastEntrySet<K>
    extends ObjectSet<Entry<K>> {
        public ObjectIterator<Entry<K>> fastIterator();
    }
}

