/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.LongComparator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public interface Long2IntSortedMap
extends Long2IntMap,
SortedMap<Long, Integer> {
    public Long2IntSortedMap subMap(long var1, long var3);

    public Long2IntSortedMap headMap(long var1);

    public Long2IntSortedMap tailMap(long var1);

    public long firstLongKey();

    public long lastLongKey();

    @Deprecated
    default public Long2IntSortedMap subMap(Long l, Long l2) {
        return this.subMap((long)l, (long)l2);
    }

    @Deprecated
    default public Long2IntSortedMap headMap(Long l) {
        return this.headMap((long)l);
    }

    @Deprecated
    default public Long2IntSortedMap tailMap(Long l) {
        return this.tailMap((long)l);
    }

    @Override
    @Deprecated
    default public Long firstKey() {
        return this.firstLongKey();
    }

    @Override
    @Deprecated
    default public Long lastKey() {
        return this.lastLongKey();
    }

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<Long, Integer>> entrySet() {
        return this.long2IntEntrySet();
    }

    public ObjectSortedSet<Long2IntMap.Entry> long2IntEntrySet();

    @Override
    public LongSortedSet keySet();

    @Override
    public IntCollection values();

    public LongComparator comparator();

    @Override
    default public LongSet keySet() {
        return this.keySet();
    }

    @Override
    @Deprecated
    default public ObjectSet entrySet() {
        return this.entrySet();
    }

    default public ObjectSet long2IntEntrySet() {
        return this.long2IntEntrySet();
    }

    @Override
    @Deprecated
    default public Set entrySet() {
        return this.entrySet();
    }

    @Override
    default public Collection values() {
        return this.values();
    }

    @Override
    default public Set keySet() {
        return this.keySet();
    }

    @Override
    @Deprecated
    default public Object lastKey() {
        return this.lastKey();
    }

    @Override
    @Deprecated
    default public Object firstKey() {
        return this.firstKey();
    }

    @Override
    @Deprecated
    default public SortedMap tailMap(Object object) {
        return this.tailMap((Long)object);
    }

    @Override
    @Deprecated
    default public SortedMap headMap(Object object) {
        return this.headMap((Long)object);
    }

    @Override
    @Deprecated
    default public SortedMap subMap(Object object, Object object2) {
        return this.subMap((Long)object, (Long)object2);
    }

    @Override
    default public Comparator comparator() {
        return this.comparator();
    }

    public static interface FastSortedEntrySet
    extends ObjectSortedSet<Long2IntMap.Entry>,
    Long2IntMap.FastEntrySet {
        public ObjectBidirectionalIterator<Long2IntMap.Entry> fastIterator();

        public ObjectBidirectionalIterator<Long2IntMap.Entry> fastIterator(Long2IntMap.Entry var1);

        default public ObjectIterator fastIterator() {
            return this.fastIterator();
        }
    }
}

