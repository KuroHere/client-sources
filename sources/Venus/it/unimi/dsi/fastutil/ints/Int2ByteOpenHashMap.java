/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.ints.AbstractInt2ByteMap;
import it.unimi.dsi.fastutil.ints.AbstractIntSet;
import it.unimi.dsi.fastutil.ints.Int2ByteMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class Int2ByteOpenHashMap
extends AbstractInt2ByteMap
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient byte[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Int2ByteMap.FastEntrySet entries;
    protected transient IntSet keys;
    protected transient ByteCollection values;

    public Int2ByteOpenHashMap(int n, float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (n < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.minN = this.n = HashCommon.arraySize(n, f);
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new int[this.n + 1];
        this.value = new byte[this.n + 1];
    }

    public Int2ByteOpenHashMap(int n) {
        this(n, 0.75f);
    }

    public Int2ByteOpenHashMap() {
        this(16, 0.75f);
    }

    public Int2ByteOpenHashMap(Map<? extends Integer, ? extends Byte> map, float f) {
        this(map.size(), f);
        this.putAll(map);
    }

    public Int2ByteOpenHashMap(Map<? extends Integer, ? extends Byte> map) {
        this(map, 0.75f);
    }

    public Int2ByteOpenHashMap(Int2ByteMap int2ByteMap, float f) {
        this(int2ByteMap.size(), f);
        this.putAll(int2ByteMap);
    }

    public Int2ByteOpenHashMap(Int2ByteMap int2ByteMap) {
        this(int2ByteMap, 0.75f);
    }

    public Int2ByteOpenHashMap(int[] nArray, byte[] byArray, float f) {
        this(nArray.length, f);
        if (nArray.length != byArray.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + nArray.length + " and " + byArray.length + ")");
        }
        for (int i = 0; i < nArray.length; ++i) {
            this.put(nArray[i], byArray[i]);
        }
    }

    public Int2ByteOpenHashMap(int[] nArray, byte[] byArray) {
        this(nArray, byArray, 0.75f);
    }

    private int realSize() {
        return this.containsNullKey ? this.size - 1 : this.size;
    }

    private void ensureCapacity(int n) {
        int n2 = HashCommon.arraySize(n, this.f);
        if (n2 > this.n) {
            this.rehash(n2);
        }
    }

    private void tryCapacity(long l) {
        int n = (int)Math.min(0x40000000L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((float)l / this.f))));
        if (n > this.n) {
            this.rehash(n);
        }
    }

    private byte removeEntry(int n) {
        byte by = this.value[n];
        --this.size;
        this.shiftKeys(n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return by;
    }

    private byte removeNullEntry() {
        this.containsNullKey = false;
        byte by = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return by;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Byte> map) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(map.size());
        } else {
            this.tryCapacity(this.size() + map.size());
        }
        super.putAll(map);
    }

    private int find(int n) {
        if (n == 0) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        int[] nArray = this.key;
        int n2 = HashCommon.mix(n) & this.mask;
        int n3 = nArray[n2];
        if (n3 == 0) {
            return -(n2 + 1);
        }
        if (n == n3) {
            return n2;
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return -(n2 + 1);
        } while (n != n3);
        return n2;
    }

    private void insert(int n, int n2, byte by) {
        if (n == this.n) {
            this.containsNullKey = true;
        }
        this.key[n] = n2;
        this.value[n] = by;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override
    public byte put(int n, byte by) {
        int n2 = this.find(n);
        if (n2 < 0) {
            this.insert(-n2 - 1, n, by);
            return this.defRetValue;
        }
        byte by2 = this.value[n2];
        this.value[n2] = by;
        return by2;
    }

    private byte addToValue(int n, byte by) {
        byte by2 = this.value[n];
        this.value[n] = (byte)(by2 + by);
        return by2;
    }

    public byte addTo(int n, byte by) {
        int n2;
        if (n == 0) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, by);
            }
            n2 = this.n;
            this.containsNullKey = true;
        } else {
            int[] nArray = this.key;
            n2 = HashCommon.mix(n) & this.mask;
            int n3 = nArray[n2];
            if (n3 != 0) {
                if (n3 == n) {
                    return this.addToValue(n2, by);
                }
                while ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) {
                    if (n3 != n) continue;
                    return this.addToValue(n2, by);
                }
            }
        }
        this.key[n2] = n;
        this.value[n2] = (byte)(this.defRetValue + by);
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }

    protected final void shiftKeys(int n) {
        int[] nArray = this.key;
        while (true) {
            int n2;
            int n3 = n;
            n = n3 + 1 & this.mask;
            while (true) {
                if ((n2 = nArray[n]) == 0) {
                    nArray[n3] = 0;
                    return;
                }
                int n4 = HashCommon.mix(n2) & this.mask;
                if (n3 <= n ? n3 >= n4 || n4 > n : n3 >= n4 && n4 > n) break;
                n = n + 1 & this.mask;
            }
            nArray[n3] = n2;
            this.value[n3] = this.value[n];
        }
    }

    @Override
    public byte remove(int n) {
        if (n == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        int[] nArray = this.key;
        int n2 = HashCommon.mix(n) & this.mask;
        int n3 = nArray[n2];
        if (n3 == 0) {
            return this.defRetValue;
        }
        if (n == n3) {
            return this.removeEntry(n2);
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (n != n3);
        return this.removeEntry(n2);
    }

    @Override
    public byte get(int n) {
        if (n == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        int[] nArray = this.key;
        int n2 = HashCommon.mix(n) & this.mask;
        int n3 = nArray[n2];
        if (n3 == 0) {
            return this.defRetValue;
        }
        if (n == n3) {
            return this.value[n2];
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (n != n3);
        return this.value[n2];
    }

    @Override
    public boolean containsKey(int n) {
        if (n == 0) {
            return this.containsNullKey;
        }
        int[] nArray = this.key;
        int n2 = HashCommon.mix(n) & this.mask;
        int n3 = nArray[n2];
        if (n3 == 0) {
            return true;
        }
        if (n == n3) {
            return false;
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return true;
        } while (n != n3);
        return false;
    }

    @Override
    public boolean containsValue(byte by) {
        byte[] byArray = this.value;
        int[] nArray = this.key;
        if (this.containsNullKey && byArray[this.n] == by) {
            return false;
        }
        int n = this.n;
        while (n-- != 0) {
            if (nArray[n] == 0 || byArray[n] != by) continue;
            return false;
        }
        return true;
    }

    @Override
    public byte getOrDefault(int n, byte by) {
        if (n == 0) {
            return this.containsNullKey ? this.value[this.n] : by;
        }
        int[] nArray = this.key;
        int n2 = HashCommon.mix(n) & this.mask;
        int n3 = nArray[n2];
        if (n3 == 0) {
            return by;
        }
        if (n == n3) {
            return this.value[n2];
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return by;
        } while (n != n3);
        return this.value[n2];
    }

    @Override
    public byte putIfAbsent(int n, byte by) {
        int n2 = this.find(n);
        if (n2 >= 0) {
            return this.value[n2];
        }
        this.insert(-n2 - 1, n, by);
        return this.defRetValue;
    }

    @Override
    public boolean remove(int n, byte by) {
        if (n == 0) {
            if (this.containsNullKey && by == this.value[this.n]) {
                this.removeNullEntry();
                return false;
            }
            return true;
        }
        int[] nArray = this.key;
        int n2 = HashCommon.mix(n) & this.mask;
        int n3 = nArray[n2];
        if (n3 == 0) {
            return true;
        }
        if (n == n3 && by == this.value[n2]) {
            this.removeEntry(n2);
            return false;
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return true;
        } while (n != n3 || by != this.value[n2]);
        this.removeEntry(n2);
        return false;
    }

    @Override
    public boolean replace(int n, byte by, byte by2) {
        int n2 = this.find(n);
        if (n2 < 0 || by != this.value[n2]) {
            return true;
        }
        this.value[n2] = by2;
        return false;
    }

    @Override
    public byte replace(int n, byte by) {
        int n2 = this.find(n);
        if (n2 < 0) {
            return this.defRetValue;
        }
        byte by2 = this.value[n2];
        this.value[n2] = by;
        return by2;
    }

    @Override
    public byte computeIfAbsent(int n, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        int n2 = this.find(n);
        if (n2 >= 0) {
            return this.value[n2];
        }
        byte by = SafeMath.safeIntToByte(intUnaryOperator.applyAsInt(n));
        this.insert(-n2 - 1, n, by);
        return by;
    }

    @Override
    public byte computeIfAbsentNullable(int n, IntFunction<? extends Byte> intFunction) {
        Objects.requireNonNull(intFunction);
        int n2 = this.find(n);
        if (n2 >= 0) {
            return this.value[n2];
        }
        Byte by = intFunction.apply(n);
        if (by == null) {
            return this.defRetValue;
        }
        byte by2 = by;
        this.insert(-n2 - 1, n, by2);
        return by2;
    }

    @Override
    public byte computeIfPresent(int n, BiFunction<? super Integer, ? super Byte, ? extends Byte> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.find(n);
        if (n2 < 0) {
            return this.defRetValue;
        }
        Byte by = biFunction.apply((Integer)n, (Byte)this.value[n2]);
        if (by == null) {
            if (n == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(n2);
            }
            return this.defRetValue;
        }
        this.value[n2] = by;
        return this.value[n2];
    }

    @Override
    public byte compute(int n, BiFunction<? super Integer, ? super Byte, ? extends Byte> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.find(n);
        Byte by = biFunction.apply((Integer)n, n2 >= 0 ? Byte.valueOf(this.value[n2]) : null);
        if (by == null) {
            if (n2 >= 0) {
                if (n == 0) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(n2);
                }
            }
            return this.defRetValue;
        }
        byte by2 = by;
        if (n2 < 0) {
            this.insert(-n2 - 1, n, by2);
            return by2;
        }
        this.value[n2] = by2;
        return this.value[n2];
    }

    @Override
    public byte merge(int n, byte by, BiFunction<? super Byte, ? super Byte, ? extends Byte> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.find(n);
        if (n2 < 0) {
            this.insert(-n2 - 1, n, by);
            return by;
        }
        Byte by2 = biFunction.apply((Byte)this.value[n2], (Byte)by);
        if (by2 == null) {
            if (n == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(n2);
            }
            return this.defRetValue;
        }
        this.value[n2] = by2;
        return this.value[n2];
    }

    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    public Int2ByteMap.FastEntrySet int2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet(this, null);
        }
        return this.entries;
    }

    @Override
    public IntSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet(this, null);
        }
        return this.keys;
    }

    @Override
    public ByteCollection values() {
        if (this.values == null) {
            this.values = new AbstractByteCollection(this){
                final Int2ByteOpenHashMap this$0;
                {
                    this.this$0 = int2ByteOpenHashMap;
                }

                @Override
                public ByteIterator iterator() {
                    return new ValueIterator(this.this$0);
                }

                @Override
                public int size() {
                    return this.this$0.size;
                }

                @Override
                public boolean contains(byte by) {
                    return this.this$0.containsValue(by);
                }

                @Override
                public void clear() {
                    this.this$0.clear();
                }

                @Override
                public void forEach(IntConsumer intConsumer) {
                    if (this.this$0.containsNullKey) {
                        intConsumer.accept(this.this$0.value[this.this$0.n]);
                    }
                    int n = this.this$0.n;
                    while (n-- != 0) {
                        if (this.this$0.key[n] == 0) continue;
                        intConsumer.accept(this.this$0.value[n]);
                    }
                }

                @Override
                public Iterator iterator() {
                    return this.iterator();
                }
            };
        }
        return this.values;
    }

    public boolean trim() {
        int n = HashCommon.arraySize(this.size, this.f);
        if (n >= this.n || this.size > HashCommon.maxFill(n, this.f)) {
            return false;
        }
        try {
            this.rehash(n);
        } catch (OutOfMemoryError outOfMemoryError) {
            return true;
        }
        return false;
    }

    public boolean trim(int n) {
        int n2 = HashCommon.nextPowerOfTwo((int)Math.ceil((float)n / this.f));
        if (n2 >= n || this.size > HashCommon.maxFill(n2, this.f)) {
            return false;
        }
        try {
            this.rehash(n2);
        } catch (OutOfMemoryError outOfMemoryError) {
            return true;
        }
        return false;
    }

    protected void rehash(int n) {
        int[] nArray = this.key;
        byte[] byArray = this.value;
        int n2 = n - 1;
        int[] nArray2 = new int[n + 1];
        byte[] byArray2 = new byte[n + 1];
        int n3 = this.n;
        int n4 = this.realSize();
        while (n4-- != 0) {
            while (nArray[--n3] == 0) {
            }
            int n5 = HashCommon.mix(nArray[n3]) & n2;
            if (nArray2[n5] != 0) {
                while (nArray2[n5 = n5 + 1 & n2] != 0) {
                }
            }
            nArray2[n5] = nArray[n3];
            byArray2[n5] = byArray[n3];
        }
        byArray2[n] = byArray[this.n];
        this.n = n;
        this.mask = n2;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = nArray2;
        this.value = byArray2;
    }

    public Int2ByteOpenHashMap clone() {
        Int2ByteOpenHashMap int2ByteOpenHashMap;
        try {
            int2ByteOpenHashMap = (Int2ByteOpenHashMap)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        int2ByteOpenHashMap.keys = null;
        int2ByteOpenHashMap.values = null;
        int2ByteOpenHashMap.entries = null;
        int2ByteOpenHashMap.containsNullKey = this.containsNullKey;
        int2ByteOpenHashMap.key = (int[])this.key.clone();
        int2ByteOpenHashMap.value = (byte[])this.value.clone();
        return int2ByteOpenHashMap;
    }

    @Override
    public int hashCode() {
        int n = 0;
        int n2 = this.realSize();
        int n3 = 0;
        int n4 = 0;
        while (n2-- != 0) {
            while (this.key[n3] == 0) {
                ++n3;
            }
            n4 = this.key[n3];
            n += (n4 ^= this.value[n3]);
            ++n3;
        }
        if (this.containsNullKey) {
            n += this.value[this.n];
        }
        return n;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int[] nArray = this.key;
        byte[] byArray = this.value;
        MapIterator mapIterator = new MapIterator(this, null);
        objectOutputStream.defaultWriteObject();
        int n = this.size;
        while (n-- != 0) {
            int n2 = mapIterator.nextEntry();
            objectOutputStream.writeInt(nArray[n2]);
            objectOutputStream.writeByte(byArray[n2]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new int[this.n + 1];
        int[] nArray = this.key;
        this.value = new byte[this.n + 1];
        byte[] byArray = this.value;
        int n = this.size;
        while (n-- != 0) {
            int n2;
            int n3 = objectInputStream.readInt();
            byte by = objectInputStream.readByte();
            if (n3 == 0) {
                n2 = this.n;
                this.containsNullKey = true;
            } else {
                n2 = HashCommon.mix(n3) & this.mask;
                while (nArray[n2] != 0) {
                    n2 = n2 + 1 & this.mask;
                }
            }
            nArray[n2] = n3;
            byArray[n2] = by;
        }
    }

    private void checkTable() {
    }

    public ObjectSet int2ByteEntrySet() {
        return this.int2ByteEntrySet();
    }

    @Override
    public Collection values() {
        return this.values();
    }

    @Override
    public Set keySet() {
        return this.keySet();
    }

    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

    static byte access$300(Int2ByteOpenHashMap int2ByteOpenHashMap) {
        return int2ByteOpenHashMap.removeNullEntry();
    }

    static byte access$400(Int2ByteOpenHashMap int2ByteOpenHashMap, int n) {
        return int2ByteOpenHashMap.removeEntry(n);
    }

    private final class ValueIterator
    extends MapIterator
    implements ByteIterator {
        final Int2ByteOpenHashMap this$0;

        public ValueIterator(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
            super(int2ByteOpenHashMap, null);
        }

        @Override
        public byte nextByte() {
            return this.this$0.value[this.nextEntry()];
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private final class KeySet
    extends AbstractIntSet {
        final Int2ByteOpenHashMap this$0;

        private KeySet(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
        }

        @Override
        public IntIterator iterator() {
            return new KeyIterator(this.this$0);
        }

        @Override
        public void forEach(IntConsumer intConsumer) {
            if (this.this$0.containsNullKey) {
                intConsumer.accept(this.this$0.key[this.this$0.n]);
            }
            int n = this.this$0.n;
            while (n-- != 0) {
                int n2 = this.this$0.key[n];
                if (n2 == 0) continue;
                intConsumer.accept(n2);
            }
        }

        @Override
        public int size() {
            return this.this$0.size;
        }

        @Override
        public boolean contains(int n) {
            return this.this$0.containsKey(n);
        }

        @Override
        public boolean remove(int n) {
            int n2 = this.this$0.size;
            this.this$0.remove(n);
            return this.this$0.size != n2;
        }

        @Override
        public void clear() {
            this.this$0.clear();
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        KeySet(Int2ByteOpenHashMap int2ByteOpenHashMap, 1 var2_2) {
            this(int2ByteOpenHashMap);
        }
    }

    private final class KeyIterator
    extends MapIterator
    implements IntIterator {
        final Int2ByteOpenHashMap this$0;

        public KeyIterator(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
            super(int2ByteOpenHashMap, null);
        }

        @Override
        public int nextInt() {
            return this.this$0.key[this.nextEntry()];
        }
    }

    private final class MapEntrySet
    extends AbstractObjectSet<Int2ByteMap.Entry>
    implements Int2ByteMap.FastEntrySet {
        final Int2ByteOpenHashMap this$0;

        private MapEntrySet(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
        }

        @Override
        public ObjectIterator<Int2ByteMap.Entry> iterator() {
            return new EntryIterator(this.this$0, null);
        }

        @Override
        public ObjectIterator<Int2ByteMap.Entry> fastIterator() {
            return new FastEntryIterator(this.this$0, null);
        }

        @Override
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            if (entry.getKey() == null || !(entry.getKey() instanceof Integer)) {
                return true;
            }
            if (entry.getValue() == null || !(entry.getValue() instanceof Byte)) {
                return true;
            }
            int n = (Integer)entry.getKey();
            byte by = (Byte)entry.getValue();
            if (n == 0) {
                return this.this$0.containsNullKey && this.this$0.value[this.this$0.n] == by;
            }
            int[] nArray = this.this$0.key;
            int n2 = HashCommon.mix(n) & this.this$0.mask;
            int n3 = nArray[n2];
            if (n3 == 0) {
                return true;
            }
            if (n == n3) {
                return this.this$0.value[n2] == by;
            }
            do {
                if ((n3 = nArray[n2 = n2 + 1 & this.this$0.mask]) != 0) continue;
                return true;
            } while (n != n3);
            return this.this$0.value[n2] == by;
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            if (entry.getKey() == null || !(entry.getKey() instanceof Integer)) {
                return true;
            }
            if (entry.getValue() == null || !(entry.getValue() instanceof Byte)) {
                return true;
            }
            int n = (Integer)entry.getKey();
            byte by = (Byte)entry.getValue();
            if (n == 0) {
                if (this.this$0.containsNullKey && this.this$0.value[this.this$0.n] == by) {
                    Int2ByteOpenHashMap.access$300(this.this$0);
                    return false;
                }
                return true;
            }
            int[] nArray = this.this$0.key;
            int n2 = HashCommon.mix(n) & this.this$0.mask;
            int n3 = nArray[n2];
            if (n3 == 0) {
                return true;
            }
            if (n3 == n) {
                if (this.this$0.value[n2] == by) {
                    Int2ByteOpenHashMap.access$400(this.this$0, n2);
                    return false;
                }
                return true;
            }
            do {
                if ((n3 = nArray[n2 = n2 + 1 & this.this$0.mask]) != 0) continue;
                return true;
            } while (n3 != n || this.this$0.value[n2] != by);
            Int2ByteOpenHashMap.access$400(this.this$0, n2);
            return false;
        }

        @Override
        public int size() {
            return this.this$0.size;
        }

        @Override
        public void clear() {
            this.this$0.clear();
        }

        @Override
        public void forEach(Consumer<? super Int2ByteMap.Entry> consumer) {
            if (this.this$0.containsNullKey) {
                consumer.accept(new AbstractInt2ByteMap.BasicEntry(this.this$0.key[this.this$0.n], this.this$0.value[this.this$0.n]));
            }
            int n = this.this$0.n;
            while (n-- != 0) {
                if (this.this$0.key[n] == 0) continue;
                consumer.accept(new AbstractInt2ByteMap.BasicEntry(this.this$0.key[n], this.this$0.value[n]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Int2ByteMap.Entry> consumer) {
            AbstractInt2ByteMap.BasicEntry basicEntry = new AbstractInt2ByteMap.BasicEntry();
            if (this.this$0.containsNullKey) {
                basicEntry.key = this.this$0.key[this.this$0.n];
                basicEntry.value = this.this$0.value[this.this$0.n];
                consumer.accept(basicEntry);
            }
            int n = this.this$0.n;
            while (n-- != 0) {
                if (this.this$0.key[n] == 0) continue;
                basicEntry.key = this.this$0.key[n];
                basicEntry.value = this.this$0.value[n];
                consumer.accept(basicEntry);
            }
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        MapEntrySet(Int2ByteOpenHashMap int2ByteOpenHashMap, 1 var2_2) {
            this(int2ByteOpenHashMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class FastEntryIterator
    extends MapIterator
    implements ObjectIterator<Int2ByteMap.Entry> {
        private final MapEntry entry;
        final Int2ByteOpenHashMap this$0;

        private FastEntryIterator(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
            super(int2ByteOpenHashMap, null);
            this.entry = new MapEntry(this.this$0);
        }

        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }

        @Override
        public Object next() {
            return this.next();
        }

        FastEntryIterator(Int2ByteOpenHashMap int2ByteOpenHashMap, 1 var2_2) {
            this(int2ByteOpenHashMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class EntryIterator
    extends MapIterator
    implements ObjectIterator<Int2ByteMap.Entry> {
        private MapEntry entry;
        final Int2ByteOpenHashMap this$0;

        private EntryIterator(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
            super(int2ByteOpenHashMap, null);
        }

        @Override
        public MapEntry next() {
            this.entry = new MapEntry(this.this$0, this.nextEntry());
            return this.entry;
        }

        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }

        @Override
        public Object next() {
            return this.next();
        }

        EntryIterator(Int2ByteOpenHashMap int2ByteOpenHashMap, 1 var2_2) {
            this(int2ByteOpenHashMap);
        }
    }

    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        IntArrayList wrapped;
        final Int2ByteOpenHashMap this$0;

        private MapIterator(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
            this.pos = this.this$0.n;
            this.last = -1;
            this.c = this.this$0.size;
            this.mustReturnNullKey = this.this$0.containsNullKey;
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
                this.last = this.this$0.n;
                return this.last;
            }
            int[] nArray = this.this$0.key;
            do {
                if (--this.pos >= 0) continue;
                this.last = Integer.MIN_VALUE;
                int n = this.wrapped.getInt(-this.pos - 1);
                int n2 = HashCommon.mix(n) & this.this$0.mask;
                while (n != nArray[n2]) {
                    n2 = n2 + 1 & this.this$0.mask;
                }
                return n2;
            } while (nArray[this.pos] == 0);
            this.last = this.pos;
            return this.last;
        }

        private void shiftKeys(int n) {
            int[] nArray = this.this$0.key;
            while (true) {
                int n2;
                int n3 = n;
                n = n3 + 1 & this.this$0.mask;
                while (true) {
                    if ((n2 = nArray[n]) == 0) {
                        nArray[n3] = 0;
                        return;
                    }
                    int n4 = HashCommon.mix(n2) & this.this$0.mask;
                    if (n3 <= n ? n3 >= n4 || n4 > n : n3 >= n4 && n4 > n) break;
                    n = n + 1 & this.this$0.mask;
                }
                if (n < n3) {
                    if (this.wrapped == null) {
                        this.wrapped = new IntArrayList(2);
                    }
                    this.wrapped.add(nArray[n]);
                }
                nArray[n3] = n2;
                this.this$0.value[n3] = this.this$0.value[n];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == this.this$0.n) {
                this.this$0.containsNullKey = false;
            } else if (this.pos >= 0) {
                this.shiftKeys(this.last);
            } else {
                this.this$0.remove(this.wrapped.getInt(-this.pos - 1));
                this.last = -1;
                return;
            }
            --this.this$0.size;
            this.last = -1;
        }

        public int skip(int n) {
            int n2 = n;
            while (n2-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - n2 - 1;
        }

        MapIterator(Int2ByteOpenHashMap int2ByteOpenHashMap, 1 var2_2) {
            this(int2ByteOpenHashMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    final class MapEntry
    implements Int2ByteMap.Entry,
    Map.Entry<Integer, Byte> {
        int index;
        final Int2ByteOpenHashMap this$0;

        MapEntry(Int2ByteOpenHashMap int2ByteOpenHashMap, int n) {
            this.this$0 = int2ByteOpenHashMap;
            this.index = n;
        }

        MapEntry(Int2ByteOpenHashMap int2ByteOpenHashMap) {
            this.this$0 = int2ByteOpenHashMap;
        }

        @Override
        public int getIntKey() {
            return this.this$0.key[this.index];
        }

        @Override
        public byte getByteValue() {
            return this.this$0.value[this.index];
        }

        @Override
        public byte setValue(byte by) {
            byte by2 = this.this$0.value[this.index];
            this.this$0.value[this.index] = by;
            return by2;
        }

        @Override
        @Deprecated
        public Integer getKey() {
            return this.this$0.key[this.index];
        }

        @Override
        @Deprecated
        public Byte getValue() {
            return this.this$0.value[this.index];
        }

        @Override
        @Deprecated
        public Byte setValue(Byte by) {
            return this.setValue((byte)by);
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            return this.this$0.key[this.index] == (Integer)entry.getKey() && this.this$0.value[this.index] == (Byte)entry.getValue();
        }

        @Override
        public int hashCode() {
            return this.this$0.key[this.index] ^ this.this$0.value[this.index];
        }

        public String toString() {
            return this.this$0.key[this.index] + "=>" + this.this$0.value[this.index];
        }

        @Override
        @Deprecated
        public Object setValue(Object object) {
            return this.setValue((Byte)object);
        }

        @Override
        @Deprecated
        public Object getValue() {
            return this.getValue();
        }

        @Override
        @Deprecated
        public Object getKey() {
            return this.getKey();
        }
    }
}

