/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.AbstractInt2ReferenceMap;
import it.unimi.dsi.fastutil.ints.AbstractIntSet;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

public class Int2ReferenceOpenHashMap<V>
extends AbstractInt2ReferenceMap<V>
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected final float f;
    protected transient Int2ReferenceMap.FastEntrySet<V> entries;
    protected transient IntSet keys;
    protected transient ReferenceCollection<V> values;

    public Int2ReferenceOpenHashMap(int expected, float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.n = HashCommon.arraySize(expected, f);
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new int[this.n + 1];
        this.value = new Object[this.n + 1];
    }

    public Int2ReferenceOpenHashMap(int expected) {
        this(expected, 0.75f);
    }

    public Int2ReferenceOpenHashMap() {
        this(16, 0.75f);
    }

    public Int2ReferenceOpenHashMap(Map<? extends Integer, ? extends V> m, float f) {
        this(m.size(), f);
        this.putAll(m);
    }

    public Int2ReferenceOpenHashMap(Map<? extends Integer, ? extends V> m) {
        this(m, 0.75f);
    }

    public Int2ReferenceOpenHashMap(Int2ReferenceMap<V> m, float f) {
        this(m.size(), f);
        this.putAll(m);
    }

    public Int2ReferenceOpenHashMap(Int2ReferenceMap<V> m) {
        this(m, 0.75f);
    }

    public Int2ReferenceOpenHashMap(int[] k, V[] v, float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }

    public Int2ReferenceOpenHashMap(int[] k, V[] v) {
        this(k, v, 0.75f);
    }

    private int realSize() {
        return this.containsNullKey ? this.size - 1 : this.size;
    }

    private void ensureCapacity(int capacity) {
        int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }

    private void tryCapacity(long capacity) {
        int needed = (int)Math.min(0x40000000L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((float)capacity / this.f))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }

    private V removeEntry(int pos) {
        V oldValue = this.value[pos];
        this.value[pos] = null;
        --this.size;
        this.shiftKeys(pos);
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    private V removeNullEntry() {
        this.containsNullKey = false;
        V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends V> m) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(m.size());
        } else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }

    private int insert(int k, V v) {
        int pos;
        if (k == 0) {
            if (this.containsNullKey) {
                return this.n;
            }
            this.containsNullKey = true;
            pos = this.n;
        } else {
            int[] key = this.key;
            pos = HashCommon.mix(k) & this.mask;
            int curr = key[pos];
            if (curr != 0) {
                if (curr == k) {
                    return pos;
                }
                while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                    if (curr != k) continue;
                    return pos;
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return -1;
    }

    @Override
    public V put(int k, V v) {
        int pos = this.insert(k, v);
        if (pos < 0) {
            return (V)this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    @Override
    @Deprecated
    public V put(Integer ok, V ov) {
        V v = ov;
        int pos = this.insert(ok, v);
        if (pos < 0) {
            return (V)this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    protected final void shiftKeys(int pos) {
        int[] key = this.key;
        while (true) {
            int curr;
            int last = pos;
            pos = last + 1 & this.mask;
            while (true) {
                if ((curr = key[pos]) == 0) {
                    key[last] = 0;
                    this.value[last] = null;
                    return;
                }
                int slot = HashCommon.mix(curr) & this.mask;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = pos + 1 & this.mask;
            }
            key[last] = curr;
            this.value[last] = this.value[pos];
        }
    }

    @Override
    public V remove(int k) {
        if (k == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return (V)this.defRetValue;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (k == curr) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (k != curr);
        return this.removeEntry(pos);
    }

    @Override
    @Deprecated
    public V remove(Object ok) {
        int k = (Integer)ok;
        if (k == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return (V)this.defRetValue;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (curr == k) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (curr != k);
        return this.removeEntry(pos);
    }

    @Deprecated
    public V get(Integer ok) {
        if (ok == null) {
            return null;
        }
        int k = ok;
        if (k == 0) {
            return (V)(this.containsNullKey ? this.value[this.n] : this.defRetValue);
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (k != curr);
        return this.value[pos];
    }

    @Override
    public V get(int k) {
        if (k == 0) {
            return (V)(this.containsNullKey ? this.value[this.n] : this.defRetValue);
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (k != curr);
        return this.value[pos];
    }

    @Override
    public boolean containsKey(int k) {
        if (k == 0) {
            return this.containsNullKey;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (k != curr);
        return true;
    }

    @Override
    public boolean containsValue(Object v) {
        V[] value = this.value;
        int[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] == 0 || value[i] != v) continue;
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0);
        Arrays.fill(this.value, null);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Deprecated
    public void growthFactor(int growthFactor) {
    }

    @Deprecated
    public int growthFactor() {
        return 16;
    }

    public Int2ReferenceMap.FastEntrySet<V> int2ReferenceEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    @Override
    public IntSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public ReferenceCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractReferenceCollection<V>(){

                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }

                @Override
                public int size() {
                    return Int2ReferenceOpenHashMap.this.size;
                }

                @Override
                public boolean contains(Object v) {
                    return Int2ReferenceOpenHashMap.this.containsValue(v);
                }

                @Override
                public void clear() {
                    Int2ReferenceOpenHashMap.this.clear();
                }
            };
        }
        return this.values;
    }

    @Deprecated
    public boolean rehash() {
        return true;
    }

    public boolean trim() {
        int l = HashCommon.arraySize(this.size, this.f);
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        } catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }

    public boolean trim(int n) {
        int l = HashCommon.nextPowerOfTwo((int)Math.ceil((float)n / this.f));
        if (l >= n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        } catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }

    protected void rehash(int newN) {
        int[] key = this.key;
        V[] value = this.value;
        int mask = newN - 1;
        int[] newKey = new int[newN + 1];
        Object[] newValue = new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0) {
            }
            int pos = HashCommon.mix(key[i]) & mask;
            if (newKey[pos] != 0) {
                while (newKey[pos = pos + 1 & mask] != 0) {
                }
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
        }
        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }

    public Int2ReferenceOpenHashMap<V> clone() {
        Int2ReferenceOpenHashMap c;
        try {
            c = (Int2ReferenceOpenHashMap)super.clone();
        } catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = (int[])this.key.clone();
        c.value = (Object[])this.value.clone();
        return c;
    }

    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (this.key[i] == 0) {
                ++i;
            }
            t = this.key[i];
            if (this != this.value[i]) {
                t ^= this.value[i] == null ? 0 : System.identityHashCode(this.value[i]);
            }
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += this.value[this.n] == null ? 0 : System.identityHashCode(this.value[this.n]);
        }
        return h;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        int[] key = this.key;
        V[] value = this.value;
        MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            int e = i.nextEntry();
            s.writeInt(key[e]);
            s.writeObject(value[e]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new int[this.n + 1];
        int[] key = this.key;
        this.value = new Object[this.n + 1];
        Object[] value = this.value;
        int i = this.size;
        while (i-- != 0) {
            int pos;
            int k = s.readInt();
            Object v = s.readObject();
            if (k == 0) {
                pos = this.n;
                this.containsNullKey = true;
            } else {
                pos = HashCommon.mix(k) & this.mask;
                while (key[pos] != 0) {
                    pos = pos + 1 & this.mask;
                }
            }
            key[pos] = k;
            value[pos] = v;
        }
    }

    private void checkTable() {
    }

    private final class ValueIterator
    extends MapIterator
    implements ObjectIterator<V> {
        @Override
        public V next() {
            return Int2ReferenceOpenHashMap.this.value[this.nextEntry()];
        }
    }

    private final class KeySet
    extends AbstractIntSet {
        private KeySet() {
        }

        @Override
        public IntIterator iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return Int2ReferenceOpenHashMap.this.size;
        }

        @Override
        public boolean contains(int k) {
            return Int2ReferenceOpenHashMap.this.containsKey(k);
        }

        @Override
        public boolean rem(int k) {
            int oldSize = Int2ReferenceOpenHashMap.this.size;
            Int2ReferenceOpenHashMap.this.remove(k);
            return Int2ReferenceOpenHashMap.this.size != oldSize;
        }

        @Override
        public void clear() {
            Int2ReferenceOpenHashMap.this.clear();
        }
    }

    private final class KeyIterator
    extends MapIterator
    implements IntIterator {
        @Override
        public int nextInt() {
            return Int2ReferenceOpenHashMap.this.key[this.nextEntry()];
        }

        @Override
        public Integer next() {
            return Int2ReferenceOpenHashMap.this.key[this.nextEntry()];
        }
    }

    private final class MapEntrySet
    extends AbstractObjectSet<Int2ReferenceMap.Entry<V>>
    implements Int2ReferenceMap.FastEntrySet<V> {
        private MapEntrySet() {
        }

        @Override
        public ObjectIterator<Int2ReferenceMap.Entry<V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public ObjectIterator<Int2ReferenceMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            int k = (Integer)e.getKey();
            Object v = e.getValue();
            if (k == 0) {
                return Int2ReferenceOpenHashMap.this.containsNullKey && Int2ReferenceOpenHashMap.this.value[Int2ReferenceOpenHashMap.this.n] == v;
            }
            int[] key = Int2ReferenceOpenHashMap.this.key;
            int pos = HashCommon.mix(k) & Int2ReferenceOpenHashMap.this.mask;
            int curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (k == curr) {
                return Int2ReferenceOpenHashMap.this.value[pos] == v;
            }
            do {
                if ((curr = key[pos = pos + 1 & Int2ReferenceOpenHashMap.this.mask]) != 0) continue;
                return false;
            } while (k != curr);
            return Int2ReferenceOpenHashMap.this.value[pos] == v;
        }

        @Override
        public boolean rem(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            int k = (Integer)e.getKey();
            Object v = e.getValue();
            if (k == 0) {
                if (Int2ReferenceOpenHashMap.this.containsNullKey && Int2ReferenceOpenHashMap.this.value[Int2ReferenceOpenHashMap.this.n] == v) {
                    Int2ReferenceOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            int[] key = Int2ReferenceOpenHashMap.this.key;
            int pos = HashCommon.mix(k) & Int2ReferenceOpenHashMap.this.mask;
            int curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (curr == k) {
                if (Int2ReferenceOpenHashMap.this.value[pos] == v) {
                    Int2ReferenceOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
            do {
                if ((curr = key[pos = pos + 1 & Int2ReferenceOpenHashMap.this.mask]) != 0) continue;
                return false;
            } while (curr != k || Int2ReferenceOpenHashMap.this.value[pos] != v);
            Int2ReferenceOpenHashMap.this.removeEntry(pos);
            return true;
        }

        @Override
        public int size() {
            return Int2ReferenceOpenHashMap.this.size;
        }

        @Override
        public void clear() {
            Int2ReferenceOpenHashMap.this.clear();
        }
    }

    private class FastEntryIterator
    extends MapIterator
    implements ObjectIterator<Int2ReferenceMap.Entry<V>> {
        private final MapEntry entry;

        private FastEntryIterator() {
            this.entry = new MapEntry();
        }

        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }

    private class EntryIterator
    extends MapIterator
    implements ObjectIterator<Int2ReferenceMap.Entry<V>> {
        private MapEntry entry;

        private EntryIterator() {
        }

        @Override
        public Int2ReferenceMap.Entry<V> next() {
            this.entry = new MapEntry(this.nextEntry());
            return this.entry;
        }

        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        IntArrayList wrapped;

        private MapIterator() {
            this.pos = Int2ReferenceOpenHashMap.this.n;
            this.last = -1;
            this.c = Int2ReferenceOpenHashMap.this.size;
            this.mustReturnNullKey = Int2ReferenceOpenHashMap.this.containsNullKey;
        }

        public boolean hasNext() {
            return this.c != 0;
        }

        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                this.last = Int2ReferenceOpenHashMap.this.n;
                return this.last;
            }
            int[] key = Int2ReferenceOpenHashMap.this.key;
            do {
                if (--this.pos >= 0) continue;
                this.last = Integer.MIN_VALUE;
                int k = this.wrapped.getInt(-this.pos - 1);
                int p = HashCommon.mix(k) & Int2ReferenceOpenHashMap.this.mask;
                while (k != key[p]) {
                    p = p + 1 & Int2ReferenceOpenHashMap.this.mask;
                }
                return p;
            } while (key[this.pos] == 0);
            this.last = this.pos;
            return this.last;
        }

        private final void shiftKeys(int pos) {
            int[] key = Int2ReferenceOpenHashMap.this.key;
            while (true) {
                int curr;
                int last = pos;
                pos = last + 1 & Int2ReferenceOpenHashMap.this.mask;
                while (true) {
                    if ((curr = key[pos]) == 0) {
                        key[last] = 0;
                        Int2ReferenceOpenHashMap.this.value[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(curr) & Int2ReferenceOpenHashMap.this.mask;
                    if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                    pos = pos + 1 & Int2ReferenceOpenHashMap.this.mask;
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new IntArrayList(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
                Int2ReferenceOpenHashMap.this.value[last] = Int2ReferenceOpenHashMap.this.value[pos];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Int2ReferenceOpenHashMap.this.n) {
                Int2ReferenceOpenHashMap.this.containsNullKey = false;
                Int2ReferenceOpenHashMap.this.value[Int2ReferenceOpenHashMap.this.n] = null;
            } else if (this.pos >= 0) {
                this.shiftKeys(this.last);
            } else {
                Int2ReferenceOpenHashMap.this.remove(this.wrapped.getInt(-this.pos - 1));
                this.last = -1;
                return;
            }
            --Int2ReferenceOpenHashMap.this.size;
            this.last = -1;
        }

        public int skip(int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
    }

    final class MapEntry
    implements Int2ReferenceMap.Entry<V>,
    Map.Entry<Integer, V> {
        int index;

        MapEntry(int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override
        @Deprecated
        public Integer getKey() {
            return Int2ReferenceOpenHashMap.this.key[this.index];
        }

        @Override
        public int getIntKey() {
            return Int2ReferenceOpenHashMap.this.key[this.index];
        }

        @Override
        public V getValue() {
            return Int2ReferenceOpenHashMap.this.value[this.index];
        }

        @Override
        public V setValue(V v) {
            Object oldValue = Int2ReferenceOpenHashMap.this.value[this.index];
            Int2ReferenceOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            return Int2ReferenceOpenHashMap.this.key[this.index] == (Integer)e.getKey() && Int2ReferenceOpenHashMap.this.value[this.index] == e.getValue();
        }

        @Override
        public int hashCode() {
            return Int2ReferenceOpenHashMap.this.key[this.index] ^ (Int2ReferenceOpenHashMap.this.value[this.index] == null ? 0 : System.identityHashCode(Int2ReferenceOpenHashMap.this.value[this.index]));
        }

        public String toString() {
            return Int2ReferenceOpenHashMap.this.key[this.index] + "=>" + Int2ReferenceOpenHashMap.this.value[this.index];
        }
    }
}

