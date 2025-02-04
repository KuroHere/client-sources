/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongMaps;
import it.unimi.dsi.fastutil.ints.Int2LongSortedMap;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSortedSets;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public final class Int2LongSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Int2LongSortedMaps() {
    }

    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(IntComparator intComparator) {
        return (arg_0, arg_1) -> Int2LongSortedMaps.lambda$entryComparator$0(intComparator, arg_0, arg_1);
    }

    public static ObjectBidirectionalIterator<Int2LongMap.Entry> fastIterator(Int2LongSortedMap int2LongSortedMap) {
        ObjectSet objectSet = int2LongSortedMap.int2LongEntrySet();
        return objectSet instanceof Int2LongSortedMap.FastSortedEntrySet ? ((Int2LongSortedMap.FastSortedEntrySet)objectSet).fastIterator() : objectSet.iterator();
    }

    public static ObjectBidirectionalIterable<Int2LongMap.Entry> fastIterable(Int2LongSortedMap int2LongSortedMap) {
        ObjectSet objectSet = int2LongSortedMap.int2LongEntrySet();
        return objectSet instanceof Int2LongSortedMap.FastSortedEntrySet ? ((Int2LongSortedMap.FastSortedEntrySet)objectSet)::fastIterator : objectSet;
    }

    public static Int2LongSortedMap singleton(Integer n, Long l) {
        return new Singleton(n, l);
    }

    public static Int2LongSortedMap singleton(Integer n, Long l, IntComparator intComparator) {
        return new Singleton(n, l, intComparator);
    }

    public static Int2LongSortedMap singleton(int n, long l) {
        return new Singleton(n, l);
    }

    public static Int2LongSortedMap singleton(int n, long l, IntComparator intComparator) {
        return new Singleton(n, l, intComparator);
    }

    public static Int2LongSortedMap synchronize(Int2LongSortedMap int2LongSortedMap) {
        return new SynchronizedSortedMap(int2LongSortedMap);
    }

    public static Int2LongSortedMap synchronize(Int2LongSortedMap int2LongSortedMap, Object object) {
        return new SynchronizedSortedMap(int2LongSortedMap, object);
    }

    public static Int2LongSortedMap unmodifiable(Int2LongSortedMap int2LongSortedMap) {
        return new UnmodifiableSortedMap(int2LongSortedMap);
    }

    private static int lambda$entryComparator$0(IntComparator intComparator, Map.Entry entry, Map.Entry entry2) {
        return intComparator.compare((int)((Integer)entry.getKey()), (int)((Integer)entry2.getKey()));
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class UnmodifiableSortedMap
    extends Int2LongMaps.UnmodifiableMap
    implements Int2LongSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2LongSortedMap sortedMap;

        protected UnmodifiableSortedMap(Int2LongSortedMap int2LongSortedMap) {
            super(int2LongSortedMap);
            this.sortedMap = int2LongSortedMap;
        }

        @Override
        public IntComparator comparator() {
            return this.sortedMap.comparator();
        }

        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.int2LongEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return this.int2LongEntrySet();
        }

        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (IntSortedSet)this.keys;
        }

        @Override
        public Int2LongSortedMap subMap(int n, int n2) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(n, n2));
        }

        @Override
        public Int2LongSortedMap headMap(int n) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(n));
        }

        @Override
        public Int2LongSortedMap tailMap(int n) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(n));
        }

        @Override
        public int firstIntKey() {
            return this.sortedMap.firstIntKey();
        }

        @Override
        public int lastIntKey() {
            return this.sortedMap.lastIntKey();
        }

        @Override
        @Deprecated
        public Integer firstKey() {
            return this.sortedMap.firstKey();
        }

        @Override
        @Deprecated
        public Integer lastKey() {
            return this.sortedMap.lastKey();
        }

        @Override
        @Deprecated
        public Int2LongSortedMap subMap(Integer n, Integer n2) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(n, n2));
        }

        @Override
        @Deprecated
        public Int2LongSortedMap headMap(Integer n) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(n));
        }

        @Override
        @Deprecated
        public Int2LongSortedMap tailMap(Integer n) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(n));
        }

        @Override
        public IntSet keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public ObjectSet entrySet() {
            return this.entrySet();
        }

        @Override
        public ObjectSet int2LongEntrySet() {
            return this.int2LongEntrySet();
        }

        @Override
        @Deprecated
        public Set entrySet() {
            return this.entrySet();
        }

        @Override
        public Set keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public Object lastKey() {
            return this.lastKey();
        }

        @Override
        @Deprecated
        public Object firstKey() {
            return this.firstKey();
        }

        @Override
        @Deprecated
        public SortedMap tailMap(Object object) {
            return this.tailMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Integer)object, (Integer)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class SynchronizedSortedMap
    extends Int2LongMaps.SynchronizedMap
    implements Int2LongSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2LongSortedMap sortedMap;

        protected SynchronizedSortedMap(Int2LongSortedMap int2LongSortedMap, Object object) {
            super(int2LongSortedMap, object);
            this.sortedMap = int2LongSortedMap;
        }

        protected SynchronizedSortedMap(Int2LongSortedMap int2LongSortedMap) {
            super(int2LongSortedMap);
            this.sortedMap = int2LongSortedMap;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public IntComparator comparator() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.comparator();
            }
        }

        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.int2LongEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return this.int2LongEntrySet();
        }

        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (IntSortedSet)this.keys;
        }

        @Override
        public Int2LongSortedMap subMap(int n, int n2) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(n, n2), this.sync);
        }

        @Override
        public Int2LongSortedMap headMap(int n) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(n), this.sync);
        }

        @Override
        public Int2LongSortedMap tailMap(int n) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(n), this.sync);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int firstIntKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.firstIntKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int lastIntKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastIntKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Integer firstKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.firstKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Integer lastKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastKey();
            }
        }

        @Override
        @Deprecated
        public Int2LongSortedMap subMap(Integer n, Integer n2) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(n, n2), this.sync);
        }

        @Override
        @Deprecated
        public Int2LongSortedMap headMap(Integer n) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(n), this.sync);
        }

        @Override
        @Deprecated
        public Int2LongSortedMap tailMap(Integer n) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(n), this.sync);
        }

        @Override
        public IntSet keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public ObjectSet entrySet() {
            return this.entrySet();
        }

        @Override
        public ObjectSet int2LongEntrySet() {
            return this.int2LongEntrySet();
        }

        @Override
        @Deprecated
        public Set entrySet() {
            return this.entrySet();
        }

        @Override
        public Set keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public Object lastKey() {
            return this.lastKey();
        }

        @Override
        @Deprecated
        public Object firstKey() {
            return this.firstKey();
        }

        @Override
        @Deprecated
        public SortedMap tailMap(Object object) {
            return this.tailMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Integer)object, (Integer)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class Singleton
    extends Int2LongMaps.Singleton
    implements Int2LongSortedMap,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;

        protected Singleton(int n, long l, IntComparator intComparator) {
            super(n, l);
            this.comparator = intComparator;
        }

        protected Singleton(int n, long l) {
            this(n, l, null);
        }

        final int compare(int n, int n2) {
            return this.comparator == null ? Integer.compare(n, n2) : this.comparator.compare(n, n2);
        }

        @Override
        public IntComparator comparator() {
            return this.comparator;
        }

        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractInt2LongMap.BasicEntry(this.key, this.value), Int2LongSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return this.int2LongEntrySet();
        }

        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }

        @Override
        public Int2LongSortedMap subMap(int n, int n2) {
            if (this.compare(n, this.key) <= 0 && this.compare(this.key, n2) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Int2LongSortedMap headMap(int n) {
            if (this.compare(this.key, n) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Int2LongSortedMap tailMap(int n) {
            if (this.compare(n, this.key) <= 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public int firstIntKey() {
            return this.key;
        }

        @Override
        public int lastIntKey() {
            return this.key;
        }

        @Override
        @Deprecated
        public Int2LongSortedMap headMap(Integer n) {
            return this.headMap((int)n);
        }

        @Override
        @Deprecated
        public Int2LongSortedMap tailMap(Integer n) {
            return this.tailMap((int)n);
        }

        @Override
        @Deprecated
        public Int2LongSortedMap subMap(Integer n, Integer n2) {
            return this.subMap((int)n, (int)n2);
        }

        @Override
        @Deprecated
        public Integer firstKey() {
            return this.firstIntKey();
        }

        @Override
        @Deprecated
        public Integer lastKey() {
            return this.lastIntKey();
        }

        @Override
        public IntSet keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public ObjectSet entrySet() {
            return this.entrySet();
        }

        @Override
        public ObjectSet int2LongEntrySet() {
            return this.int2LongEntrySet();
        }

        @Override
        @Deprecated
        public Set entrySet() {
            return this.entrySet();
        }

        @Override
        public Set keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public Object lastKey() {
            return this.lastKey();
        }

        @Override
        @Deprecated
        public Object firstKey() {
            return this.firstKey();
        }

        @Override
        @Deprecated
        public SortedMap tailMap(Object object) {
            return this.tailMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Integer)object, (Integer)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class EmptySortedMap
    extends Int2LongMaps.EmptyMap
    implements Int2LongSortedMap,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySortedMap() {
        }

        @Override
        public IntComparator comparator() {
            return null;
        }

        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }

        @Override
        public Int2LongSortedMap subMap(int n, int n2) {
            return EMPTY_MAP;
        }

        @Override
        public Int2LongSortedMap headMap(int n) {
            return EMPTY_MAP;
        }

        @Override
        public Int2LongSortedMap tailMap(int n) {
            return EMPTY_MAP;
        }

        @Override
        public int firstIntKey() {
            throw new NoSuchElementException();
        }

        @Override
        public int lastIntKey() {
            throw new NoSuchElementException();
        }

        @Override
        @Deprecated
        public Int2LongSortedMap headMap(Integer n) {
            return this.headMap((int)n);
        }

        @Override
        @Deprecated
        public Int2LongSortedMap tailMap(Integer n) {
            return this.tailMap((int)n);
        }

        @Override
        @Deprecated
        public Int2LongSortedMap subMap(Integer n, Integer n2) {
            return this.subMap((int)n, (int)n2);
        }

        @Override
        @Deprecated
        public Integer firstKey() {
            return this.firstIntKey();
        }

        @Override
        @Deprecated
        public Integer lastKey() {
            return this.lastIntKey();
        }

        @Override
        public IntSet keySet() {
            return this.keySet();
        }

        @Override
        public ObjectSet int2LongEntrySet() {
            return this.int2LongEntrySet();
        }

        @Override
        @Deprecated
        public ObjectSet entrySet() {
            return this.entrySet();
        }

        @Override
        @Deprecated
        public Set entrySet() {
            return this.entrySet();
        }

        @Override
        public Set keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public Object lastKey() {
            return this.lastKey();
        }

        @Override
        @Deprecated
        public Object firstKey() {
            return this.firstKey();
        }

        @Override
        @Deprecated
        public SortedMap tailMap(Object object) {
            return this.tailMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Integer)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Integer)object, (Integer)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }
}

