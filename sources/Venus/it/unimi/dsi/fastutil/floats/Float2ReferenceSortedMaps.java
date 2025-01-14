/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.floats.AbstractFloat2ReferenceMap;
import it.unimi.dsi.fastutil.floats.Float2ReferenceMap;
import it.unimi.dsi.fastutil.floats.Float2ReferenceMaps;
import it.unimi.dsi.fastutil.floats.Float2ReferenceSortedMap;
import it.unimi.dsi.fastutil.floats.FloatComparator;
import it.unimi.dsi.fastutil.floats.FloatSet;
import it.unimi.dsi.fastutil.floats.FloatSortedSet;
import it.unimi.dsi.fastutil.floats.FloatSortedSets;
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

public final class Float2ReferenceSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Float2ReferenceSortedMaps() {
    }

    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(FloatComparator floatComparator) {
        return (arg_0, arg_1) -> Float2ReferenceSortedMaps.lambda$entryComparator$0(floatComparator, arg_0, arg_1);
    }

    public static <V> ObjectBidirectionalIterator<Float2ReferenceMap.Entry<V>> fastIterator(Float2ReferenceSortedMap<V> float2ReferenceSortedMap) {
        ObjectSet objectSet = float2ReferenceSortedMap.float2ReferenceEntrySet();
        return objectSet instanceof Float2ReferenceSortedMap.FastSortedEntrySet ? ((Float2ReferenceSortedMap.FastSortedEntrySet)objectSet).fastIterator() : objectSet.iterator();
    }

    public static <V> ObjectBidirectionalIterable<Float2ReferenceMap.Entry<V>> fastIterable(Float2ReferenceSortedMap<V> float2ReferenceSortedMap) {
        ObjectSet objectSet = float2ReferenceSortedMap.float2ReferenceEntrySet();
        return objectSet instanceof Float2ReferenceSortedMap.FastSortedEntrySet ? ((Float2ReferenceSortedMap.FastSortedEntrySet)objectSet)::fastIterator : objectSet;
    }

    public static <V> Float2ReferenceSortedMap<V> emptyMap() {
        return EMPTY_MAP;
    }

    public static <V> Float2ReferenceSortedMap<V> singleton(Float f, V v) {
        return new Singleton<V>(f.floatValue(), v);
    }

    public static <V> Float2ReferenceSortedMap<V> singleton(Float f, V v, FloatComparator floatComparator) {
        return new Singleton<V>(f.floatValue(), v, floatComparator);
    }

    public static <V> Float2ReferenceSortedMap<V> singleton(float f, V v) {
        return new Singleton<V>(f, v);
    }

    public static <V> Float2ReferenceSortedMap<V> singleton(float f, V v, FloatComparator floatComparator) {
        return new Singleton<V>(f, v, floatComparator);
    }

    public static <V> Float2ReferenceSortedMap<V> synchronize(Float2ReferenceSortedMap<V> float2ReferenceSortedMap) {
        return new SynchronizedSortedMap<V>(float2ReferenceSortedMap);
    }

    public static <V> Float2ReferenceSortedMap<V> synchronize(Float2ReferenceSortedMap<V> float2ReferenceSortedMap, Object object) {
        return new SynchronizedSortedMap<V>(float2ReferenceSortedMap, object);
    }

    public static <V> Float2ReferenceSortedMap<V> unmodifiable(Float2ReferenceSortedMap<V> float2ReferenceSortedMap) {
        return new UnmodifiableSortedMap<V>(float2ReferenceSortedMap);
    }

    private static int lambda$entryComparator$0(FloatComparator floatComparator, Map.Entry entry, Map.Entry entry2) {
        return floatComparator.compare(((Float)entry.getKey()).floatValue(), ((Float)entry2.getKey()).floatValue());
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class UnmodifiableSortedMap<V>
    extends Float2ReferenceMaps.UnmodifiableMap<V>
    implements Float2ReferenceSortedMap<V>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ReferenceSortedMap<V> sortedMap;

        protected UnmodifiableSortedMap(Float2ReferenceSortedMap<V> float2ReferenceSortedMap) {
            super(float2ReferenceSortedMap);
            this.sortedMap = float2ReferenceSortedMap;
        }

        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }

        @Override
        public ObjectSortedSet<Float2ReferenceMap.Entry<V>> float2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.float2ReferenceEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return this.float2ReferenceEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2ReferenceSortedMap<V> subMap(float f, float f2) {
            return new UnmodifiableSortedMap<V>(this.sortedMap.subMap(f, f2));
        }

        @Override
        public Float2ReferenceSortedMap<V> headMap(float f) {
            return new UnmodifiableSortedMap<V>(this.sortedMap.headMap(f));
        }

        @Override
        public Float2ReferenceSortedMap<V> tailMap(float f) {
            return new UnmodifiableSortedMap<V>(this.sortedMap.tailMap(f));
        }

        @Override
        public float firstFloatKey() {
            return this.sortedMap.firstFloatKey();
        }

        @Override
        public float lastFloatKey() {
            return this.sortedMap.lastFloatKey();
        }

        @Override
        @Deprecated
        public Float firstKey() {
            return this.sortedMap.firstKey();
        }

        @Override
        @Deprecated
        public Float lastKey() {
            return this.sortedMap.lastKey();
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> subMap(Float f, Float f2) {
            return new UnmodifiableSortedMap<V>(this.sortedMap.subMap(f, f2));
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> headMap(Float f) {
            return new UnmodifiableSortedMap<V>(this.sortedMap.headMap(f));
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> tailMap(Float f) {
            return new UnmodifiableSortedMap<V>(this.sortedMap.tailMap(f));
        }

        @Override
        public FloatSet keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public ObjectSet entrySet() {
            return this.entrySet();
        }

        @Override
        public ObjectSet float2ReferenceEntrySet() {
            return this.float2ReferenceEntrySet();
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
            return this.tailMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Float)object, (Float)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class SynchronizedSortedMap<V>
    extends Float2ReferenceMaps.SynchronizedMap<V>
    implements Float2ReferenceSortedMap<V>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ReferenceSortedMap<V> sortedMap;

        protected SynchronizedSortedMap(Float2ReferenceSortedMap<V> float2ReferenceSortedMap, Object object) {
            super(float2ReferenceSortedMap, object);
            this.sortedMap = float2ReferenceSortedMap;
        }

        protected SynchronizedSortedMap(Float2ReferenceSortedMap<V> float2ReferenceSortedMap) {
            super(float2ReferenceSortedMap);
            this.sortedMap = float2ReferenceSortedMap;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public FloatComparator comparator() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.comparator();
            }
        }

        @Override
        public ObjectSortedSet<Float2ReferenceMap.Entry<V>> float2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.float2ReferenceEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return this.float2ReferenceEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2ReferenceSortedMap<V> subMap(float f, float f2) {
            return new SynchronizedSortedMap<V>(this.sortedMap.subMap(f, f2), this.sync);
        }

        @Override
        public Float2ReferenceSortedMap<V> headMap(float f) {
            return new SynchronizedSortedMap<V>(this.sortedMap.headMap(f), this.sync);
        }

        @Override
        public Float2ReferenceSortedMap<V> tailMap(float f) {
            return new SynchronizedSortedMap<V>(this.sortedMap.tailMap(f), this.sync);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public float firstFloatKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.firstFloatKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public float lastFloatKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastFloatKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Float firstKey() {
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
        public Float lastKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastKey();
            }
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> subMap(Float f, Float f2) {
            return new SynchronizedSortedMap<V>(this.sortedMap.subMap(f, f2), this.sync);
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> headMap(Float f) {
            return new SynchronizedSortedMap<V>(this.sortedMap.headMap(f), this.sync);
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> tailMap(Float f) {
            return new SynchronizedSortedMap<V>(this.sortedMap.tailMap(f), this.sync);
        }

        @Override
        public FloatSet keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public ObjectSet entrySet() {
            return this.entrySet();
        }

        @Override
        public ObjectSet float2ReferenceEntrySet() {
            return this.float2ReferenceEntrySet();
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
            return this.tailMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Float)object, (Float)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class Singleton<V>
    extends Float2ReferenceMaps.Singleton<V>
    implements Float2ReferenceSortedMap<V>,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;

        protected Singleton(float f, V v, FloatComparator floatComparator) {
            super(f, v);
            this.comparator = floatComparator;
        }

        protected Singleton(float f, V v) {
            this(f, v, null);
        }

        final int compare(float f, float f2) {
            return this.comparator == null ? Float.compare(f, f2) : this.comparator.compare(f, f2);
        }

        @Override
        public FloatComparator comparator() {
            return this.comparator;
        }

        @Override
        public ObjectSortedSet<Float2ReferenceMap.Entry<V>> float2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractFloat2ReferenceMap.BasicEntry<Object>(this.key, this.value), Float2ReferenceSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return this.float2ReferenceEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2ReferenceSortedMap<V> subMap(float f, float f2) {
            if (this.compare(f, this.key) <= 0 && this.compare(this.key, f2) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Float2ReferenceSortedMap<V> headMap(float f) {
            if (this.compare(this.key, f) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Float2ReferenceSortedMap<V> tailMap(float f) {
            if (this.compare(f, this.key) <= 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public float firstFloatKey() {
            return this.key;
        }

        @Override
        public float lastFloatKey() {
            return this.key;
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> headMap(Float f) {
            return this.headMap(f.floatValue());
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> tailMap(Float f) {
            return this.tailMap(f.floatValue());
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> subMap(Float f, Float f2) {
            return this.subMap(f.floatValue(), f2.floatValue());
        }

        @Override
        @Deprecated
        public Float firstKey() {
            return Float.valueOf(this.firstFloatKey());
        }

        @Override
        @Deprecated
        public Float lastKey() {
            return Float.valueOf(this.lastFloatKey());
        }

        @Override
        public FloatSet keySet() {
            return this.keySet();
        }

        @Override
        @Deprecated
        public ObjectSet entrySet() {
            return this.entrySet();
        }

        @Override
        public ObjectSet float2ReferenceEntrySet() {
            return this.float2ReferenceEntrySet();
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
            return this.tailMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Float)object, (Float)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class EmptySortedMap<V>
    extends Float2ReferenceMaps.EmptyMap<V>
    implements Float2ReferenceSortedMap<V>,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySortedMap() {
        }

        @Override
        public FloatComparator comparator() {
            return null;
        }

        @Override
        public ObjectSortedSet<Float2ReferenceMap.Entry<V>> float2ReferenceEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }

        @Override
        public Float2ReferenceSortedMap<V> subMap(float f, float f2) {
            return EMPTY_MAP;
        }

        @Override
        public Float2ReferenceSortedMap<V> headMap(float f) {
            return EMPTY_MAP;
        }

        @Override
        public Float2ReferenceSortedMap<V> tailMap(float f) {
            return EMPTY_MAP;
        }

        @Override
        public float firstFloatKey() {
            throw new NoSuchElementException();
        }

        @Override
        public float lastFloatKey() {
            throw new NoSuchElementException();
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> headMap(Float f) {
            return this.headMap(f.floatValue());
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> tailMap(Float f) {
            return this.tailMap(f.floatValue());
        }

        @Override
        @Deprecated
        public Float2ReferenceSortedMap<V> subMap(Float f, Float f2) {
            return this.subMap(f.floatValue(), f2.floatValue());
        }

        @Override
        @Deprecated
        public Float firstKey() {
            return Float.valueOf(this.firstFloatKey());
        }

        @Override
        @Deprecated
        public Float lastKey() {
            return Float.valueOf(this.lastFloatKey());
        }

        @Override
        public FloatSet keySet() {
            return this.keySet();
        }

        @Override
        public ObjectSet float2ReferenceEntrySet() {
            return this.float2ReferenceEntrySet();
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
            return this.tailMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap headMap(Object object) {
            return this.headMap((Float)object);
        }

        @Override
        @Deprecated
        public SortedMap subMap(Object object, Object object2) {
            return this.subMap((Float)object, (Float)object2);
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }
    }
}

