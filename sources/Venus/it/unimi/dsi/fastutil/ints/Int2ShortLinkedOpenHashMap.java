/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.ints.AbstractInt2ShortMap;
import it.unimi.dsi.fastutil.ints.AbstractInt2ShortSortedMap;
import it.unimi.dsi.fastutil.ints.AbstractIntSortedSet;
import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import it.unimi.dsi.fastutil.ints.Int2ShortSortedMap;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class Int2ShortLinkedOpenHashMap
extends AbstractInt2ShortSortedMap
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient short[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int first = -1;
    protected transient int last = -1;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Int2ShortSortedMap.FastSortedEntrySet entries;
    protected transient IntSortedSet keys;
    protected transient ShortCollection values;

    public Int2ShortLinkedOpenHashMap(int n, float f) {
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
        this.value = new short[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public Int2ShortLinkedOpenHashMap(int n) {
        this(n, 0.75f);
    }

    public Int2ShortLinkedOpenHashMap() {
        this(16, 0.75f);
    }

    public Int2ShortLinkedOpenHashMap(Map<? extends Integer, ? extends Short> map, float f) {
        this(map.size(), f);
        this.putAll(map);
    }

    public Int2ShortLinkedOpenHashMap(Map<? extends Integer, ? extends Short> map) {
        this(map, 0.75f);
    }

    public Int2ShortLinkedOpenHashMap(Int2ShortMap int2ShortMap, float f) {
        this(int2ShortMap.size(), f);
        this.putAll(int2ShortMap);
    }

    public Int2ShortLinkedOpenHashMap(Int2ShortMap int2ShortMap) {
        this(int2ShortMap, 0.75f);
    }

    public Int2ShortLinkedOpenHashMap(int[] nArray, short[] sArray, float f) {
        this(nArray.length, f);
        if (nArray.length != sArray.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + nArray.length + " and " + sArray.length + ")");
        }
        for (int i = 0; i < nArray.length; ++i) {
            this.put(nArray[i], sArray[i]);
        }
    }

    public Int2ShortLinkedOpenHashMap(int[] nArray, short[] sArray) {
        this(nArray, sArray, 0.75f);
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

    private short removeEntry(int n) {
        short s = this.value[n];
        --this.size;
        this.fixPointers(n);
        this.shiftKeys(n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return s;
    }

    private short removeNullEntry() {
        this.containsNullKey = false;
        short s = this.value[this.n];
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return s;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Short> map) {
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

    private void insert(int n, int n2, short s) {
        if (n == this.n) {
            this.containsNullKey = true;
        }
        this.key[n] = n2;
        this.value[n] = s;
        if (this.size == 0) {
            this.first = this.last = n;
            this.link[n] = -1L;
        } else {
            int n3 = this.last;
            this.link[n3] = this.link[n3] ^ (this.link[this.last] ^ (long)n & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[n] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = n;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override
    public short put(int n, short s) {
        int n2 = this.find(n);
        if (n2 < 0) {
            this.insert(-n2 - 1, n, s);
            return this.defRetValue;
        }
        short s2 = this.value[n2];
        this.value[n2] = s;
        return s2;
    }

    private short addToValue(int n, short s) {
        short s2 = this.value[n];
        this.value[n] = (short)(s2 + s);
        return s2;
    }

    public short addTo(int n, short s) {
        int n2;
        if (n == 0) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, s);
            }
            n2 = this.n;
            this.containsNullKey = true;
        } else {
            int[] nArray = this.key;
            n2 = HashCommon.mix(n) & this.mask;
            int n3 = nArray[n2];
            if (n3 != 0) {
                if (n3 == n) {
                    return this.addToValue(n2, s);
                }
                while ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) {
                    if (n3 != n) continue;
                    return this.addToValue(n2, s);
                }
            }
        }
        this.key[n2] = n;
        this.value[n2] = (short)(this.defRetValue + s);
        if (this.size == 0) {
            this.first = this.last = n2;
            this.link[n2] = -1L;
        } else {
            int n4 = this.last;
            this.link[n4] = this.link[n4] ^ (this.link[this.last] ^ (long)n2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[n2] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = n2;
        }
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
            this.fixPointers(n, n3);
        }
    }

    @Override
    public short remove(int n) {
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

    private short setValue(int n, short s) {
        short s2 = this.value[n];
        this.value[n] = s;
        return s2;
    }

    public short removeFirstShort() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int n = this.first;
        this.first = (int)this.link[n];
        if (0 <= this.first) {
            int n2 = this.first;
            this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
        }
        --this.size;
        short s = this.value[n];
        if (n == this.n) {
            this.containsNullKey = false;
        } else {
            this.shiftKeys(n);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return s;
    }

    public short removeLastShort() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int n = this.last;
        this.last = (int)(this.link[n] >>> 32);
        if (0 <= this.last) {
            int n2 = this.last;
            this.link[n2] = this.link[n2] | 0xFFFFFFFFL;
        }
        --this.size;
        short s = this.value[n];
        if (n == this.n) {
            this.containsNullKey = false;
        } else {
            this.shiftKeys(n);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return s;
    }

    private void moveIndexToFirst(int n) {
        if (this.size == 1 || this.first == n) {
            return;
        }
        if (this.last == n) {
            int n2 = this.last = (int)(this.link[n] >>> 32);
            this.link[n2] = this.link[n2] | 0xFFFFFFFFL;
        } else {
            long l = this.link[n];
            int n3 = (int)(l >>> 32);
            int n4 = (int)l;
            int n5 = n3;
            this.link[n5] = this.link[n5] ^ (this.link[n3] ^ l & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n6 = n4;
            this.link[n6] = this.link[n6] ^ (this.link[n4] ^ l & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n7 = this.first;
        this.link[n7] = this.link[n7] ^ (this.link[this.first] ^ ((long)n & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[n] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
        this.first = n;
    }

    private void moveIndexToLast(int n) {
        if (this.size == 1 || this.last == n) {
            return;
        }
        if (this.first == n) {
            int n2 = this.first = (int)this.link[n];
            this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
        } else {
            long l = this.link[n];
            int n3 = (int)(l >>> 32);
            int n4 = (int)l;
            int n5 = n3;
            this.link[n5] = this.link[n5] ^ (this.link[n3] ^ l & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n6 = n4;
            this.link[n6] = this.link[n6] ^ (this.link[n4] ^ l & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n7 = this.last;
        this.link[n7] = this.link[n7] ^ (this.link[this.last] ^ (long)n & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        this.link[n] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
        this.last = n;
    }

    public short getAndMoveToFirst(int n) {
        if (n == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
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
            this.moveIndexToFirst(n2);
            return this.value[n2];
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (n != n3);
        this.moveIndexToFirst(n2);
        return this.value[n2];
    }

    public short getAndMoveToLast(int n) {
        if (n == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
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
            this.moveIndexToLast(n2);
            return this.value[n2];
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (n != n3);
        this.moveIndexToLast(n2);
        return this.value[n2];
    }

    public short putAndMoveToFirst(int n, short s) {
        int n2;
        if (n == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, s);
            }
            this.containsNullKey = true;
            n2 = this.n;
        } else {
            int[] nArray = this.key;
            n2 = HashCommon.mix(n) & this.mask;
            int n3 = nArray[n2];
            if (n3 != 0) {
                if (n3 == n) {
                    this.moveIndexToFirst(n2);
                    return this.setValue(n2, s);
                }
                while ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) {
                    if (n3 != n) continue;
                    this.moveIndexToFirst(n2);
                    return this.setValue(n2, s);
                }
            }
        }
        this.key[n2] = n;
        this.value[n2] = s;
        if (this.size == 0) {
            this.first = this.last = n2;
            this.link[n2] = -1L;
        } else {
            int n4 = this.first;
            this.link[n4] = this.link[n4] ^ (this.link[this.first] ^ ((long)n2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[n2] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
            this.first = n2;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }

    public short putAndMoveToLast(int n, short s) {
        int n2;
        if (n == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, s);
            }
            this.containsNullKey = true;
            n2 = this.n;
        } else {
            int[] nArray = this.key;
            n2 = HashCommon.mix(n) & this.mask;
            int n3 = nArray[n2];
            if (n3 != 0) {
                if (n3 == n) {
                    this.moveIndexToLast(n2);
                    return this.setValue(n2, s);
                }
                while ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) {
                    if (n3 != n) continue;
                    this.moveIndexToLast(n2);
                    return this.setValue(n2, s);
                }
            }
        }
        this.key[n2] = n;
        this.value[n2] = s;
        if (this.size == 0) {
            this.first = this.last = n2;
            this.link[n2] = -1L;
        } else {
            int n4 = this.last;
            this.link[n4] = this.link[n4] ^ (this.link[this.last] ^ (long)n2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[n2] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = n2;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }

    @Override
    public short get(int n) {
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
    public boolean containsValue(short s) {
        short[] sArray = this.value;
        int[] nArray = this.key;
        if (this.containsNullKey && sArray[this.n] == s) {
            return false;
        }
        int n = this.n;
        while (n-- != 0) {
            if (nArray[n] == 0 || sArray[n] != s) continue;
            return false;
        }
        return true;
    }

    @Override
    public short getOrDefault(int n, short s) {
        if (n == 0) {
            return this.containsNullKey ? this.value[this.n] : s;
        }
        int[] nArray = this.key;
        int n2 = HashCommon.mix(n) & this.mask;
        int n3 = nArray[n2];
        if (n3 == 0) {
            return s;
        }
        if (n == n3) {
            return this.value[n2];
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return s;
        } while (n != n3);
        return this.value[n2];
    }

    @Override
    public short putIfAbsent(int n, short s) {
        int n2 = this.find(n);
        if (n2 >= 0) {
            return this.value[n2];
        }
        this.insert(-n2 - 1, n, s);
        return this.defRetValue;
    }

    @Override
    public boolean remove(int n, short s) {
        if (n == 0) {
            if (this.containsNullKey && s == this.value[this.n]) {
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
        if (n == n3 && s == this.value[n2]) {
            this.removeEntry(n2);
            return false;
        }
        do {
            if ((n3 = nArray[n2 = n2 + 1 & this.mask]) != 0) continue;
            return true;
        } while (n != n3 || s != this.value[n2]);
        this.removeEntry(n2);
        return false;
    }

    @Override
    public boolean replace(int n, short s, short s2) {
        int n2 = this.find(n);
        if (n2 < 0 || s != this.value[n2]) {
            return true;
        }
        this.value[n2] = s2;
        return false;
    }

    @Override
    public short replace(int n, short s) {
        int n2 = this.find(n);
        if (n2 < 0) {
            return this.defRetValue;
        }
        short s2 = this.value[n2];
        this.value[n2] = s;
        return s2;
    }

    @Override
    public short computeIfAbsent(int n, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        int n2 = this.find(n);
        if (n2 >= 0) {
            return this.value[n2];
        }
        short s = SafeMath.safeIntToShort(intUnaryOperator.applyAsInt(n));
        this.insert(-n2 - 1, n, s);
        return s;
    }

    @Override
    public short computeIfAbsentNullable(int n, IntFunction<? extends Short> intFunction) {
        Objects.requireNonNull(intFunction);
        int n2 = this.find(n);
        if (n2 >= 0) {
            return this.value[n2];
        }
        Short s = intFunction.apply(n);
        if (s == null) {
            return this.defRetValue;
        }
        short s2 = s;
        this.insert(-n2 - 1, n, s2);
        return s2;
    }

    @Override
    public short computeIfPresent(int n, BiFunction<? super Integer, ? super Short, ? extends Short> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.find(n);
        if (n2 < 0) {
            return this.defRetValue;
        }
        Short s = biFunction.apply((Integer)n, (Short)this.value[n2]);
        if (s == null) {
            if (n == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(n2);
            }
            return this.defRetValue;
        }
        this.value[n2] = s;
        return this.value[n2];
    }

    @Override
    public short compute(int n, BiFunction<? super Integer, ? super Short, ? extends Short> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.find(n);
        Short s = biFunction.apply((Integer)n, n2 >= 0 ? Short.valueOf(this.value[n2]) : null);
        if (s == null) {
            if (n2 >= 0) {
                if (n == 0) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(n2);
                }
            }
            return this.defRetValue;
        }
        short s2 = s;
        if (n2 < 0) {
            this.insert(-n2 - 1, n, s2);
            return s2;
        }
        this.value[n2] = s2;
        return this.value[n2];
    }

    @Override
    public short merge(int n, short s, BiFunction<? super Short, ? super Short, ? extends Short> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.find(n);
        if (n2 < 0) {
            this.insert(-n2 - 1, n, s);
            return s;
        }
        Short s2 = biFunction.apply((Short)this.value[n2], (Short)s);
        if (s2 == null) {
            if (n == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(n2);
            }
            return this.defRetValue;
        }
        this.value[n2] = s2;
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
        this.last = -1;
        this.first = -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    protected void fixPointers(int n) {
        if (this.size == 0) {
            this.last = -1;
            this.first = -1;
            return;
        }
        if (this.first == n) {
            this.first = (int)this.link[n];
            if (0 <= this.first) {
                int n2 = this.first;
                this.link[n2] = this.link[n2] | 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == n) {
            this.last = (int)(this.link[n] >>> 32);
            if (0 <= this.last) {
                int n3 = this.last;
                this.link[n3] = this.link[n3] | 0xFFFFFFFFL;
            }
            return;
        }
        long l = this.link[n];
        int n4 = (int)(l >>> 32);
        int n5 = (int)l;
        int n6 = n4;
        this.link[n6] = this.link[n6] ^ (this.link[n4] ^ l & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n7 = n5;
        this.link[n7] = this.link[n7] ^ (this.link[n5] ^ l & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
    }

    protected void fixPointers(int n, int n2) {
        if (this.size == 1) {
            this.first = this.last = n2;
            this.link[n2] = -1L;
            return;
        }
        if (this.first == n) {
            this.first = n2;
            int n3 = (int)this.link[n];
            this.link[n3] = this.link[n3] ^ (this.link[(int)this.link[n]] ^ ((long)n2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[n2] = this.link[n];
            return;
        }
        if (this.last == n) {
            this.last = n2;
            int n4 = (int)(this.link[n] >>> 32);
            this.link[n4] = this.link[n4] ^ (this.link[(int)(this.link[n] >>> 32)] ^ (long)n2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[n2] = this.link[n];
            return;
        }
        long l = this.link[n];
        int n5 = (int)(l >>> 32);
        int n6 = (int)l;
        int n7 = n5;
        this.link[n7] = this.link[n7] ^ (this.link[n5] ^ (long)n2 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n8 = n6;
        this.link[n8] = this.link[n8] ^ (this.link[n6] ^ ((long)n2 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[n2] = l;
    }

    @Override
    public int firstIntKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override
    public int lastIntKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override
    public Int2ShortSortedMap tailMap(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Int2ShortSortedMap headMap(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Int2ShortSortedMap subMap(int n, int n2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IntComparator comparator() {
        return null;
    }

    @Override
    public Int2ShortSortedMap.FastSortedEntrySet int2ShortEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet(this, null);
        }
        return this.entries;
    }

    @Override
    public IntSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet(this, null);
        }
        return this.keys;
    }

    @Override
    public ShortCollection values() {
        if (this.values == null) {
            this.values = new AbstractShortCollection(this){
                final Int2ShortLinkedOpenHashMap this$0;
                {
                    this.this$0 = int2ShortLinkedOpenHashMap;
                }

                @Override
                public ShortIterator iterator() {
                    return new ValueIterator(this.this$0);
                }

                @Override
                public int size() {
                    return this.this$0.size;
                }

                @Override
                public boolean contains(short s) {
                    return this.this$0.containsValue(s);
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
        short[] sArray = this.value;
        int n2 = n - 1;
        int[] nArray2 = new int[n + 1];
        short[] sArray2 = new short[n + 1];
        int n3 = this.first;
        int n4 = -1;
        int n5 = -1;
        long[] lArray = this.link;
        long[] lArray2 = new long[n + 1];
        this.first = -1;
        int n6 = this.size;
        while (n6-- != 0) {
            int n7;
            if (nArray[n3] == 0) {
                n7 = n;
            } else {
                n7 = HashCommon.mix(nArray[n3]) & n2;
                while (nArray2[n7] != 0) {
                    n7 = n7 + 1 & n2;
                }
            }
            nArray2[n7] = nArray[n3];
            sArray2[n7] = sArray[n3];
            if (n4 != -1) {
                int n8 = n5;
                lArray2[n8] = lArray2[n8] ^ (lArray2[n5] ^ (long)n7 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n9 = n7;
                lArray2[n9] = lArray2[n9] ^ (lArray2[n7] ^ ((long)n5 & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                n5 = n7;
            } else {
                n5 = this.first = n7;
                lArray2[n7] = -1L;
            }
            int n10 = n3;
            n3 = (int)lArray[n3];
            n4 = n10;
        }
        this.link = lArray2;
        this.last = n5;
        if (n5 != -1) {
            int n11 = n5;
            lArray2[n11] = lArray2[n11] | 0xFFFFFFFFL;
        }
        this.n = n;
        this.mask = n2;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = nArray2;
        this.value = sArray2;
    }

    public Int2ShortLinkedOpenHashMap clone() {
        Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap;
        try {
            int2ShortLinkedOpenHashMap = (Int2ShortLinkedOpenHashMap)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        int2ShortLinkedOpenHashMap.keys = null;
        int2ShortLinkedOpenHashMap.values = null;
        int2ShortLinkedOpenHashMap.entries = null;
        int2ShortLinkedOpenHashMap.containsNullKey = this.containsNullKey;
        int2ShortLinkedOpenHashMap.key = (int[])this.key.clone();
        int2ShortLinkedOpenHashMap.value = (short[])this.value.clone();
        int2ShortLinkedOpenHashMap.link = (long[])this.link.clone();
        return int2ShortLinkedOpenHashMap;
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
        short[] sArray = this.value;
        MapIterator mapIterator = new MapIterator(this);
        objectOutputStream.defaultWriteObject();
        int n = this.size;
        while (n-- != 0) {
            int n2 = mapIterator.nextEntry();
            objectOutputStream.writeInt(nArray[n2]);
            objectOutputStream.writeShort(sArray[n2]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new int[this.n + 1];
        int[] nArray = this.key;
        this.value = new short[this.n + 1];
        short[] sArray = this.value;
        this.link = new long[this.n + 1];
        long[] lArray = this.link;
        int n = -1;
        this.last = -1;
        this.first = -1;
        int n2 = this.size;
        while (n2-- != 0) {
            int n3;
            int n4 = objectInputStream.readInt();
            short s = objectInputStream.readShort();
            if (n4 == 0) {
                n3 = this.n;
                this.containsNullKey = true;
            } else {
                n3 = HashCommon.mix(n4) & this.mask;
                while (nArray[n3] != 0) {
                    n3 = n3 + 1 & this.mask;
                }
            }
            nArray[n3] = n4;
            sArray[n3] = s;
            if (this.first != -1) {
                int n5 = n;
                lArray[n5] = lArray[n5] ^ (lArray[n] ^ (long)n3 & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n6 = n3;
                lArray[n6] = lArray[n6] ^ (lArray[n3] ^ ((long)n & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                n = n3;
                continue;
            }
            n = this.first = n3;
            int n7 = n3;
            lArray[n7] = lArray[n7] | 0xFFFFFFFF00000000L;
        }
        this.last = n;
        if (n != -1) {
            int n8 = n;
            lArray[n8] = lArray[n8] | 0xFFFFFFFFL;
        }
    }

    private void checkTable() {
    }

    @Override
    public ObjectSortedSet int2ShortEntrySet() {
        return this.int2ShortEntrySet();
    }

    @Override
    public IntSet keySet() {
        return this.keySet();
    }

    @Override
    public ObjectSet int2ShortEntrySet() {
        return this.int2ShortEntrySet();
    }

    @Override
    public Collection values() {
        return this.values();
    }

    @Override
    public Set keySet() {
        return this.keySet();
    }

    @Override
    public Comparator comparator() {
        return this.comparator();
    }

    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

    static short access$100(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
        return int2ShortLinkedOpenHashMap.removeNullEntry();
    }

    static short access$200(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, int n) {
        return int2ShortLinkedOpenHashMap.removeEntry(n);
    }

    private final class ValueIterator
    extends MapIterator
    implements ShortListIterator {
        final Int2ShortLinkedOpenHashMap this$0;

        @Override
        public short previousShort() {
            return this.this$0.value[this.previousEntry()];
        }

        public ValueIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            super(int2ShortLinkedOpenHashMap);
        }

        @Override
        public short nextShort() {
            return this.this$0.value[this.nextEntry()];
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private final class KeySet
    extends AbstractIntSortedSet {
        final Int2ShortLinkedOpenHashMap this$0;

        private KeySet(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
        }

        @Override
        public IntListIterator iterator(int n) {
            return new KeyIterator(this.this$0, n);
        }

        @Override
        public IntListIterator iterator() {
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
        public int firstInt() {
            if (this.this$0.size == 0) {
                throw new NoSuchElementException();
            }
            return this.this$0.key[this.this$0.first];
        }

        @Override
        public int lastInt() {
            if (this.this$0.size == 0) {
                throw new NoSuchElementException();
            }
            return this.this$0.key[this.this$0.last];
        }

        @Override
        public IntComparator comparator() {
            return null;
        }

        @Override
        public IntSortedSet tailSet(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IntSortedSet headSet(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IntSortedSet subSet(int n, int n2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IntBidirectionalIterator iterator() {
            return this.iterator();
        }

        @Override
        public IntBidirectionalIterator iterator(int n) {
            return this.iterator(n);
        }

        @Override
        public IntIterator iterator() {
            return this.iterator();
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        @Override
        public Comparator comparator() {
            return this.comparator();
        }

        KeySet(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, 1 var2_2) {
            this(int2ShortLinkedOpenHashMap);
        }
    }

    private final class KeyIterator
    extends MapIterator
    implements IntListIterator {
        final Int2ShortLinkedOpenHashMap this$0;

        public KeyIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, int n) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            super(int2ShortLinkedOpenHashMap, n, null);
        }

        @Override
        public int previousInt() {
            return this.this$0.key[this.previousEntry()];
        }

        public KeyIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            super(int2ShortLinkedOpenHashMap);
        }

        @Override
        public int nextInt() {
            return this.this$0.key[this.nextEntry()];
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private final class MapEntrySet
    extends AbstractObjectSortedSet<Int2ShortMap.Entry>
    implements Int2ShortSortedMap.FastSortedEntrySet {
        final Int2ShortLinkedOpenHashMap this$0;

        private MapEntrySet(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
        }

        @Override
        public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator() {
            return new EntryIterator(this.this$0);
        }

        @Override
        public Comparator<? super Int2ShortMap.Entry> comparator() {
            return null;
        }

        @Override
        public ObjectSortedSet<Int2ShortMap.Entry> subSet(Int2ShortMap.Entry entry, Int2ShortMap.Entry entry2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectSortedSet<Int2ShortMap.Entry> headSet(Int2ShortMap.Entry entry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectSortedSet<Int2ShortMap.Entry> tailSet(Int2ShortMap.Entry entry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Int2ShortMap.Entry first() {
            if (this.this$0.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(this.this$0, this.this$0.first);
        }

        @Override
        public Int2ShortMap.Entry last() {
            if (this.this$0.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(this.this$0, this.this$0.last);
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
            if (entry.getValue() == null || !(entry.getValue() instanceof Short)) {
                return true;
            }
            int n = (Integer)entry.getKey();
            short s = (Short)entry.getValue();
            if (n == 0) {
                return this.this$0.containsNullKey && this.this$0.value[this.this$0.n] == s;
            }
            int[] nArray = this.this$0.key;
            int n2 = HashCommon.mix(n) & this.this$0.mask;
            int n3 = nArray[n2];
            if (n3 == 0) {
                return true;
            }
            if (n == n3) {
                return this.this$0.value[n2] == s;
            }
            do {
                if ((n3 = nArray[n2 = n2 + 1 & this.this$0.mask]) != 0) continue;
                return true;
            } while (n != n3);
            return this.this$0.value[n2] == s;
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
            if (entry.getValue() == null || !(entry.getValue() instanceof Short)) {
                return true;
            }
            int n = (Integer)entry.getKey();
            short s = (Short)entry.getValue();
            if (n == 0) {
                if (this.this$0.containsNullKey && this.this$0.value[this.this$0.n] == s) {
                    Int2ShortLinkedOpenHashMap.access$100(this.this$0);
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
                if (this.this$0.value[n2] == s) {
                    Int2ShortLinkedOpenHashMap.access$200(this.this$0, n2);
                    return false;
                }
                return true;
            }
            do {
                if ((n3 = nArray[n2 = n2 + 1 & this.this$0.mask]) != 0) continue;
                return true;
            } while (n3 != n || this.this$0.value[n2] != s);
            Int2ShortLinkedOpenHashMap.access$200(this.this$0, n2);
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
        public ObjectListIterator<Int2ShortMap.Entry> iterator(Int2ShortMap.Entry entry) {
            return new EntryIterator(this.this$0, entry.getIntKey());
        }

        @Override
        public ObjectListIterator<Int2ShortMap.Entry> fastIterator() {
            return new FastEntryIterator(this.this$0);
        }

        public ObjectListIterator<Int2ShortMap.Entry> fastIterator(Int2ShortMap.Entry entry) {
            return new FastEntryIterator(this.this$0, entry.getIntKey());
        }

        @Override
        public void forEach(Consumer<? super Int2ShortMap.Entry> consumer) {
            int n = this.this$0.size;
            int n2 = this.this$0.first;
            while (n-- != 0) {
                int n3 = n2;
                n2 = (int)this.this$0.link[n3];
                consumer.accept(new AbstractInt2ShortMap.BasicEntry(this.this$0.key[n3], this.this$0.value[n3]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Int2ShortMap.Entry> consumer) {
            AbstractInt2ShortMap.BasicEntry basicEntry = new AbstractInt2ShortMap.BasicEntry();
            int n = this.this$0.size;
            int n2 = this.this$0.first;
            while (n-- != 0) {
                int n3 = n2;
                n2 = (int)this.this$0.link[n3];
                basicEntry.key = this.this$0.key[n3];
                basicEntry.value = this.this$0.value[n3];
                consumer.accept(basicEntry);
            }
        }

        @Override
        public ObjectSortedSet tailSet(Object object) {
            return this.tailSet((Int2ShortMap.Entry)object);
        }

        @Override
        public ObjectSortedSet headSet(Object object) {
            return this.headSet((Int2ShortMap.Entry)object);
        }

        @Override
        public ObjectSortedSet subSet(Object object, Object object2) {
            return this.subSet((Int2ShortMap.Entry)object, (Int2ShortMap.Entry)object2);
        }

        @Override
        public ObjectBidirectionalIterator iterator(Object object) {
            return this.iterator((Int2ShortMap.Entry)object);
        }

        @Override
        public ObjectIterator iterator() {
            return this.iterator();
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        @Override
        public Object last() {
            return this.last();
        }

        @Override
        public Object first() {
            return this.first();
        }

        @Override
        public SortedSet tailSet(Object object) {
            return this.tailSet((Int2ShortMap.Entry)object);
        }

        @Override
        public SortedSet headSet(Object object) {
            return this.headSet((Int2ShortMap.Entry)object);
        }

        @Override
        public SortedSet subSet(Object object, Object object2) {
            return this.subSet((Int2ShortMap.Entry)object, (Int2ShortMap.Entry)object2);
        }

        public ObjectBidirectionalIterator fastIterator(Int2ShortMap.Entry entry) {
            return this.fastIterator(entry);
        }

        @Override
        public ObjectBidirectionalIterator fastIterator() {
            return this.fastIterator();
        }

        @Override
        public ObjectIterator fastIterator() {
            return this.fastIterator();
        }

        MapEntrySet(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, 1 var2_2) {
            this(int2ShortLinkedOpenHashMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class FastEntryIterator
    extends MapIterator
    implements ObjectListIterator<Int2ShortMap.Entry> {
        final MapEntry entry;
        final Int2ShortLinkedOpenHashMap this$0;

        public FastEntryIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            super(int2ShortLinkedOpenHashMap);
            this.entry = new MapEntry(this.this$0);
        }

        public FastEntryIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, int n) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            super(int2ShortLinkedOpenHashMap, n, null);
            this.entry = new MapEntry(this.this$0);
        }

        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }

        @Override
        public MapEntry previous() {
            this.entry.index = this.previousEntry();
            return this.entry;
        }

        @Override
        public void add(Object object) {
            super.add((Int2ShortMap.Entry)object);
        }

        @Override
        public void set(Object object) {
            super.set((Int2ShortMap.Entry)object);
        }

        @Override
        public Object next() {
            return this.next();
        }

        @Override
        public Object previous() {
            return this.previous();
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class EntryIterator
    extends MapIterator
    implements ObjectListIterator<Int2ShortMap.Entry> {
        private MapEntry entry;
        final Int2ShortLinkedOpenHashMap this$0;

        public EntryIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            super(int2ShortLinkedOpenHashMap);
        }

        public EntryIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, int n) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            super(int2ShortLinkedOpenHashMap, n, null);
        }

        @Override
        public MapEntry next() {
            this.entry = new MapEntry(this.this$0, this.nextEntry());
            return this.entry;
        }

        @Override
        public MapEntry previous() {
            this.entry = new MapEntry(this.this$0, this.previousEntry());
            return this.entry;
        }

        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }

        @Override
        public void add(Object object) {
            super.add((Int2ShortMap.Entry)object);
        }

        @Override
        public void set(Object object) {
            super.set((Int2ShortMap.Entry)object);
        }

        @Override
        public Object next() {
            return this.next();
        }

        @Override
        public Object previous() {
            return this.previous();
        }
    }

    private class MapIterator {
        int prev;
        int next;
        int curr;
        int index;
        final Int2ShortLinkedOpenHashMap this$0;

        protected MapIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = int2ShortLinkedOpenHashMap.first;
            this.index = 0;
        }

        private MapIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, int n) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (n == 0) {
                if (int2ShortLinkedOpenHashMap.containsNullKey) {
                    this.next = (int)int2ShortLinkedOpenHashMap.link[int2ShortLinkedOpenHashMap.n];
                    this.prev = int2ShortLinkedOpenHashMap.n;
                    return;
                }
                throw new NoSuchElementException("The key " + n + " does not belong to this map.");
            }
            if (int2ShortLinkedOpenHashMap.key[int2ShortLinkedOpenHashMap.last] == n) {
                this.prev = int2ShortLinkedOpenHashMap.last;
                this.index = int2ShortLinkedOpenHashMap.size;
                return;
            }
            int n2 = HashCommon.mix(n) & int2ShortLinkedOpenHashMap.mask;
            while (int2ShortLinkedOpenHashMap.key[n2] != 0) {
                if (int2ShortLinkedOpenHashMap.key[n2] == n) {
                    this.next = (int)int2ShortLinkedOpenHashMap.link[n2];
                    this.prev = n2;
                    return;
                }
                n2 = n2 + 1 & int2ShortLinkedOpenHashMap.mask;
            }
            throw new NoSuchElementException("The key " + n + " does not belong to this map.");
        }

        public boolean hasNext() {
            return this.next != -1;
        }

        public boolean hasPrevious() {
            return this.prev != -1;
        }

        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = this.this$0.size;
                return;
            }
            int n = this.this$0.first;
            this.index = 1;
            while (n != this.prev) {
                n = (int)this.this$0.link[n];
                ++this.index;
            }
        }

        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }

        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }

        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)this.this$0.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return this.curr;
        }

        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(this.this$0.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.curr;
        }

        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(this.this$0.link[this.curr] >>> 32);
            } else {
                this.next = (int)this.this$0.link[this.curr];
            }
            --this.this$0.size;
            if (this.prev == -1) {
                this.this$0.first = this.next;
            } else {
                int n = this.prev;
                this.this$0.link[n] = this.this$0.link[n] ^ (this.this$0.link[this.prev] ^ (long)this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            }
            if (this.next == -1) {
                this.this$0.last = this.prev;
            } else {
                int n = this.next;
                this.this$0.link[n] = this.this$0.link[n] ^ (this.this$0.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            }
            int n = this.curr;
            this.curr = -1;
            if (n != this.this$0.n) {
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
                    nArray[n3] = n2;
                    this.this$0.value[n3] = this.this$0.value[n];
                    if (this.next == n) {
                        this.next = n3;
                    }
                    if (this.prev == n) {
                        this.prev = n3;
                    }
                    this.this$0.fixPointers(n, n3);
                }
            }
            this.this$0.containsNullKey = false;
        }

        public int skip(int n) {
            int n2 = n;
            while (n2-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - n2 - 1;
        }

        public int back(int n) {
            int n2 = n;
            while (n2-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - n2 - 1;
        }

        public void set(Int2ShortMap.Entry entry) {
            throw new UnsupportedOperationException();
        }

        public void add(Int2ShortMap.Entry entry) {
            throw new UnsupportedOperationException();
        }

        MapIterator(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, int n, 1 var3_3) {
            this(int2ShortLinkedOpenHashMap, n);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    final class MapEntry
    implements Int2ShortMap.Entry,
    Map.Entry<Integer, Short> {
        int index;
        final Int2ShortLinkedOpenHashMap this$0;

        MapEntry(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap, int n) {
            this.this$0 = int2ShortLinkedOpenHashMap;
            this.index = n;
        }

        MapEntry(Int2ShortLinkedOpenHashMap int2ShortLinkedOpenHashMap) {
            this.this$0 = int2ShortLinkedOpenHashMap;
        }

        @Override
        public int getIntKey() {
            return this.this$0.key[this.index];
        }

        @Override
        public short getShortValue() {
            return this.this$0.value[this.index];
        }

        @Override
        public short setValue(short s) {
            short s2 = this.this$0.value[this.index];
            this.this$0.value[this.index] = s;
            return s2;
        }

        @Override
        @Deprecated
        public Integer getKey() {
            return this.this$0.key[this.index];
        }

        @Override
        @Deprecated
        public Short getValue() {
            return this.this$0.value[this.index];
        }

        @Override
        @Deprecated
        public Short setValue(Short s) {
            return this.setValue((short)s);
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            return this.this$0.key[this.index] == (Integer)entry.getKey() && this.this$0.value[this.index] == (Short)entry.getValue();
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
            return this.setValue((Short)object);
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

