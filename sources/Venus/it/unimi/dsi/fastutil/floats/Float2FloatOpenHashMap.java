/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.floats.AbstractFloat2FloatMap;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import it.unimi.dsi.fastutil.floats.AbstractFloatSet;
import it.unimi.dsi.fastutil.floats.Float2FloatMap;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatSet;
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
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class Float2FloatOpenHashMap
extends AbstractFloat2FloatMap
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient float[] key;
    protected transient float[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Float2FloatMap.FastEntrySet entries;
    protected transient FloatSet keys;
    protected transient FloatCollection values;

    public Float2FloatOpenHashMap(int n, float f) {
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
        this.key = new float[this.n + 1];
        this.value = new float[this.n + 1];
    }

    public Float2FloatOpenHashMap(int n) {
        this(n, 0.75f);
    }

    public Float2FloatOpenHashMap() {
        this(16, 0.75f);
    }

    public Float2FloatOpenHashMap(Map<? extends Float, ? extends Float> map, float f) {
        this(map.size(), f);
        this.putAll(map);
    }

    public Float2FloatOpenHashMap(Map<? extends Float, ? extends Float> map) {
        this(map, 0.75f);
    }

    public Float2FloatOpenHashMap(Float2FloatMap float2FloatMap, float f) {
        this(float2FloatMap.size(), f);
        this.putAll(float2FloatMap);
    }

    public Float2FloatOpenHashMap(Float2FloatMap float2FloatMap) {
        this(float2FloatMap, 0.75f);
    }

    public Float2FloatOpenHashMap(float[] fArray, float[] fArray2, float f) {
        this(fArray.length, f);
        if (fArray.length != fArray2.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + fArray.length + " and " + fArray2.length + ")");
        }
        for (int i = 0; i < fArray.length; ++i) {
            this.put(fArray[i], fArray2[i]);
        }
    }

    public Float2FloatOpenHashMap(float[] fArray, float[] fArray2) {
        this(fArray, fArray2, 0.75f);
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

    private float removeEntry(int n) {
        float f = this.value[n];
        --this.size;
        this.shiftKeys(n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return f;
    }

    private float removeNullEntry() {
        this.containsNullKey = false;
        float f = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return f;
    }

    @Override
    public void putAll(Map<? extends Float, ? extends Float> map) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(map.size());
        } else {
            this.tryCapacity(this.size() + map.size());
        }
        super.putAll(map);
    }

    private int find(float f) {
        if (Float.floatToIntBits(f) == 0) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        float[] fArray = this.key;
        int n = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
        float f2 = fArray[n];
        if (Float.floatToIntBits(f2) == 0) {
            return -(n + 1);
        }
        if (Float.floatToIntBits(f) == Float.floatToIntBits(f2)) {
            return n;
        }
        do {
            if (Float.floatToIntBits(f2 = fArray[n = n + 1 & this.mask]) != 0) continue;
            return -(n + 1);
        } while (Float.floatToIntBits(f) != Float.floatToIntBits(f2));
        return n;
    }

    private void insert(int n, float f, float f2) {
        if (n == this.n) {
            this.containsNullKey = true;
        }
        this.key[n] = f;
        this.value[n] = f2;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override
    public float put(float f, float f2) {
        int n = this.find(f);
        if (n < 0) {
            this.insert(-n - 1, f, f2);
            return this.defRetValue;
        }
        float f3 = this.value[n];
        this.value[n] = f2;
        return f3;
    }

    private float addToValue(int n, float f) {
        float f2 = this.value[n];
        this.value[n] = f2 + f;
        return f2;
    }

    public float addTo(float f, float f2) {
        int n;
        if (Float.floatToIntBits(f) == 0) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, f2);
            }
            n = this.n;
            this.containsNullKey = true;
        } else {
            float[] fArray = this.key;
            n = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
            float f3 = fArray[n];
            if (Float.floatToIntBits(f3) != 0) {
                if (Float.floatToIntBits(f3) == Float.floatToIntBits(f)) {
                    return this.addToValue(n, f2);
                }
                while (Float.floatToIntBits(f3 = fArray[n = n + 1 & this.mask]) != 0) {
                    if (Float.floatToIntBits(f3) != Float.floatToIntBits(f)) continue;
                    return this.addToValue(n, f2);
                }
            }
        }
        this.key[n] = f;
        this.value[n] = this.defRetValue + f2;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }

    protected final void shiftKeys(int n) {
        float[] fArray = this.key;
        while (true) {
            float f;
            int n2 = n;
            n = n2 + 1 & this.mask;
            while (true) {
                if (Float.floatToIntBits(f = fArray[n]) == 0) {
                    fArray[n2] = 0.0f;
                    return;
                }
                int n3 = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
                if (n2 <= n ? n2 >= n3 || n3 > n : n2 >= n3 && n3 > n) break;
                n = n + 1 & this.mask;
            }
            fArray[n2] = f;
            this.value[n2] = this.value[n];
        }
    }

    @Override
    public float remove(float f) {
        if (Float.floatToIntBits(f) == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        float[] fArray = this.key;
        int n = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
        float f2 = fArray[n];
        if (Float.floatToIntBits(f2) == 0) {
            return this.defRetValue;
        }
        if (Float.floatToIntBits(f) == Float.floatToIntBits(f2)) {
            return this.removeEntry(n);
        }
        do {
            if (Float.floatToIntBits(f2 = fArray[n = n + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (Float.floatToIntBits(f) != Float.floatToIntBits(f2));
        return this.removeEntry(n);
    }

    @Override
    public float get(float f) {
        if (Float.floatToIntBits(f) == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        float[] fArray = this.key;
        int n = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
        float f2 = fArray[n];
        if (Float.floatToIntBits(f2) == 0) {
            return this.defRetValue;
        }
        if (Float.floatToIntBits(f) == Float.floatToIntBits(f2)) {
            return this.value[n];
        }
        do {
            if (Float.floatToIntBits(f2 = fArray[n = n + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (Float.floatToIntBits(f) != Float.floatToIntBits(f2));
        return this.value[n];
    }

    @Override
    public boolean containsKey(float f) {
        if (Float.floatToIntBits(f) == 0) {
            return this.containsNullKey;
        }
        float[] fArray = this.key;
        int n = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
        float f2 = fArray[n];
        if (Float.floatToIntBits(f2) == 0) {
            return true;
        }
        if (Float.floatToIntBits(f) == Float.floatToIntBits(f2)) {
            return false;
        }
        do {
            if (Float.floatToIntBits(f2 = fArray[n = n + 1 & this.mask]) != 0) continue;
            return true;
        } while (Float.floatToIntBits(f) != Float.floatToIntBits(f2));
        return false;
    }

    @Override
    public boolean containsValue(float f) {
        float[] fArray = this.value;
        float[] fArray2 = this.key;
        if (this.containsNullKey && Float.floatToIntBits(fArray[this.n]) == Float.floatToIntBits(f)) {
            return false;
        }
        int n = this.n;
        while (n-- != 0) {
            if (Float.floatToIntBits(fArray2[n]) == 0 || Float.floatToIntBits(fArray[n]) != Float.floatToIntBits(f)) continue;
            return false;
        }
        return true;
    }

    @Override
    public float getOrDefault(float f, float f2) {
        if (Float.floatToIntBits(f) == 0) {
            return this.containsNullKey ? this.value[this.n] : f2;
        }
        float[] fArray = this.key;
        int n = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
        float f3 = fArray[n];
        if (Float.floatToIntBits(f3) == 0) {
            return f2;
        }
        if (Float.floatToIntBits(f) == Float.floatToIntBits(f3)) {
            return this.value[n];
        }
        do {
            if (Float.floatToIntBits(f3 = fArray[n = n + 1 & this.mask]) != 0) continue;
            return f2;
        } while (Float.floatToIntBits(f) != Float.floatToIntBits(f3));
        return this.value[n];
    }

    @Override
    public float putIfAbsent(float f, float f2) {
        int n = this.find(f);
        if (n >= 0) {
            return this.value[n];
        }
        this.insert(-n - 1, f, f2);
        return this.defRetValue;
    }

    @Override
    public boolean remove(float f, float f2) {
        if (Float.floatToIntBits(f) == 0) {
            if (this.containsNullKey && Float.floatToIntBits(f2) == Float.floatToIntBits(this.value[this.n])) {
                this.removeNullEntry();
                return false;
            }
            return true;
        }
        float[] fArray = this.key;
        int n = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
        float f3 = fArray[n];
        if (Float.floatToIntBits(f3) == 0) {
            return true;
        }
        if (Float.floatToIntBits(f) == Float.floatToIntBits(f3) && Float.floatToIntBits(f2) == Float.floatToIntBits(this.value[n])) {
            this.removeEntry(n);
            return false;
        }
        do {
            if (Float.floatToIntBits(f3 = fArray[n = n + 1 & this.mask]) != 0) continue;
            return true;
        } while (Float.floatToIntBits(f) != Float.floatToIntBits(f3) || Float.floatToIntBits(f2) != Float.floatToIntBits(this.value[n]));
        this.removeEntry(n);
        return false;
    }

    @Override
    public boolean replace(float f, float f2, float f3) {
        int n = this.find(f);
        if (n < 0 || Float.floatToIntBits(f2) != Float.floatToIntBits(this.value[n])) {
            return true;
        }
        this.value[n] = f3;
        return false;
    }

    @Override
    public float replace(float f, float f2) {
        int n = this.find(f);
        if (n < 0) {
            return this.defRetValue;
        }
        float f3 = this.value[n];
        this.value[n] = f2;
        return f3;
    }

    @Override
    public float computeIfAbsent(float f, DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        int n = this.find(f);
        if (n >= 0) {
            return this.value[n];
        }
        float f2 = SafeMath.safeDoubleToFloat(doubleUnaryOperator.applyAsDouble(f));
        this.insert(-n - 1, f, f2);
        return f2;
    }

    @Override
    public float computeIfAbsentNullable(float f, DoubleFunction<? extends Float> doubleFunction) {
        Objects.requireNonNull(doubleFunction);
        int n = this.find(f);
        if (n >= 0) {
            return this.value[n];
        }
        Float f2 = doubleFunction.apply(f);
        if (f2 == null) {
            return this.defRetValue;
        }
        float f3 = f2.floatValue();
        this.insert(-n - 1, f, f3);
        return f3;
    }

    @Override
    public float computeIfPresent(float f, BiFunction<? super Float, ? super Float, ? extends Float> biFunction) {
        Objects.requireNonNull(biFunction);
        int n = this.find(f);
        if (n < 0) {
            return this.defRetValue;
        }
        Float f2 = biFunction.apply(Float.valueOf(f), Float.valueOf(this.value[n]));
        if (f2 == null) {
            if (Float.floatToIntBits(f) == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(n);
            }
            return this.defRetValue;
        }
        this.value[n] = f2.floatValue();
        return this.value[n];
    }

    @Override
    public float compute(float f, BiFunction<? super Float, ? super Float, ? extends Float> biFunction) {
        Objects.requireNonNull(biFunction);
        int n = this.find(f);
        Float f2 = biFunction.apply(Float.valueOf(f), n >= 0 ? Float.valueOf(this.value[n]) : null);
        if (f2 == null) {
            if (n >= 0) {
                if (Float.floatToIntBits(f) == 0) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(n);
                }
            }
            return this.defRetValue;
        }
        float f3 = f2.floatValue();
        if (n < 0) {
            this.insert(-n - 1, f, f3);
            return f3;
        }
        this.value[n] = f3;
        return this.value[n];
    }

    @Override
    public float merge(float f, float f2, BiFunction<? super Float, ? super Float, ? extends Float> biFunction) {
        Objects.requireNonNull(biFunction);
        int n = this.find(f);
        if (n < 0) {
            this.insert(-n - 1, f, f2);
            return f2;
        }
        Float f3 = biFunction.apply(Float.valueOf(this.value[n]), Float.valueOf(f2));
        if (f3 == null) {
            if (Float.floatToIntBits(f) == 0) {
                this.removeNullEntry();
            } else {
                this.removeEntry(n);
            }
            return this.defRetValue;
        }
        this.value[n] = f3.floatValue();
        return this.value[n];
    }

    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, 0.0f);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    public Float2FloatMap.FastEntrySet float2FloatEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet(this, null);
        }
        return this.entries;
    }

    @Override
    public FloatSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet(this, null);
        }
        return this.keys;
    }

    @Override
    public FloatCollection values() {
        if (this.values == null) {
            this.values = new AbstractFloatCollection(this){
                final Float2FloatOpenHashMap this$0;
                {
                    this.this$0 = float2FloatOpenHashMap;
                }

                @Override
                public FloatIterator iterator() {
                    return new ValueIterator(this.this$0);
                }

                @Override
                public int size() {
                    return this.this$0.size;
                }

                @Override
                public boolean contains(float f) {
                    return this.this$0.containsValue(f);
                }

                @Override
                public void clear() {
                    this.this$0.clear();
                }

                @Override
                public void forEach(DoubleConsumer doubleConsumer) {
                    if (this.this$0.containsNullKey) {
                        doubleConsumer.accept(this.this$0.value[this.this$0.n]);
                    }
                    int n = this.this$0.n;
                    while (n-- != 0) {
                        if (Float.floatToIntBits(this.this$0.key[n]) == 0) continue;
                        doubleConsumer.accept(this.this$0.value[n]);
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
        float[] fArray = this.key;
        float[] fArray2 = this.value;
        int n2 = n - 1;
        float[] fArray3 = new float[n + 1];
        float[] fArray4 = new float[n + 1];
        int n3 = this.n;
        int n4 = this.realSize();
        while (n4-- != 0) {
            while (Float.floatToIntBits(fArray[--n3]) == 0) {
            }
            int n5 = HashCommon.mix(HashCommon.float2int(fArray[n3])) & n2;
            if (Float.floatToIntBits(fArray3[n5]) != 0) {
                while (Float.floatToIntBits(fArray3[n5 = n5 + 1 & n2]) != 0) {
                }
            }
            fArray3[n5] = fArray[n3];
            fArray4[n5] = fArray2[n3];
        }
        fArray4[n] = fArray2[this.n];
        this.n = n;
        this.mask = n2;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = fArray3;
        this.value = fArray4;
    }

    public Float2FloatOpenHashMap clone() {
        Float2FloatOpenHashMap float2FloatOpenHashMap;
        try {
            float2FloatOpenHashMap = (Float2FloatOpenHashMap)super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError();
        }
        float2FloatOpenHashMap.keys = null;
        float2FloatOpenHashMap.values = null;
        float2FloatOpenHashMap.entries = null;
        float2FloatOpenHashMap.containsNullKey = this.containsNullKey;
        float2FloatOpenHashMap.key = (float[])this.key.clone();
        float2FloatOpenHashMap.value = (float[])this.value.clone();
        return float2FloatOpenHashMap;
    }

    @Override
    public int hashCode() {
        int n = 0;
        int n2 = this.realSize();
        int n3 = 0;
        int n4 = 0;
        while (n2-- != 0) {
            while (Float.floatToIntBits(this.key[n3]) == 0) {
                ++n3;
            }
            n4 = HashCommon.float2int(this.key[n3]);
            n += (n4 ^= HashCommon.float2int(this.value[n3]));
            ++n3;
        }
        if (this.containsNullKey) {
            n += HashCommon.float2int(this.value[this.n]);
        }
        return n;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        float[] fArray = this.key;
        float[] fArray2 = this.value;
        MapIterator mapIterator = new MapIterator(this, null);
        objectOutputStream.defaultWriteObject();
        int n = this.size;
        while (n-- != 0) {
            int n2 = mapIterator.nextEntry();
            objectOutputStream.writeFloat(fArray[n2]);
            objectOutputStream.writeFloat(fArray2[n2]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new float[this.n + 1];
        float[] fArray = this.key;
        this.value = new float[this.n + 1];
        float[] fArray2 = this.value;
        int n = this.size;
        while (n-- != 0) {
            int n2;
            float f = objectInputStream.readFloat();
            float f2 = objectInputStream.readFloat();
            if (Float.floatToIntBits(f) == 0) {
                n2 = this.n;
                this.containsNullKey = true;
            } else {
                n2 = HashCommon.mix(HashCommon.float2int(f)) & this.mask;
                while (Float.floatToIntBits(fArray[n2]) != 0) {
                    n2 = n2 + 1 & this.mask;
                }
            }
            fArray[n2] = f;
            fArray2[n2] = f2;
        }
    }

    private void checkTable() {
    }

    public ObjectSet float2FloatEntrySet() {
        return this.float2FloatEntrySet();
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

    static float access$300(Float2FloatOpenHashMap float2FloatOpenHashMap) {
        return float2FloatOpenHashMap.removeNullEntry();
    }

    static float access$400(Float2FloatOpenHashMap float2FloatOpenHashMap, int n) {
        return float2FloatOpenHashMap.removeEntry(n);
    }

    private final class ValueIterator
    extends MapIterator
    implements FloatIterator {
        final Float2FloatOpenHashMap this$0;

        public ValueIterator(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
            super(float2FloatOpenHashMap, null);
        }

        @Override
        public float nextFloat() {
            return this.this$0.value[this.nextEntry()];
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private final class KeySet
    extends AbstractFloatSet {
        final Float2FloatOpenHashMap this$0;

        private KeySet(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
        }

        @Override
        public FloatIterator iterator() {
            return new KeyIterator(this.this$0);
        }

        @Override
        public void forEach(DoubleConsumer doubleConsumer) {
            if (this.this$0.containsNullKey) {
                doubleConsumer.accept(this.this$0.key[this.this$0.n]);
            }
            int n = this.this$0.n;
            while (n-- != 0) {
                float f = this.this$0.key[n];
                if (Float.floatToIntBits(f) == 0) continue;
                doubleConsumer.accept(f);
            }
        }

        @Override
        public int size() {
            return this.this$0.size;
        }

        @Override
        public boolean contains(float f) {
            return this.this$0.containsKey(f);
        }

        @Override
        public boolean remove(float f) {
            int n = this.this$0.size;
            this.this$0.remove(f);
            return this.this$0.size != n;
        }

        @Override
        public void clear() {
            this.this$0.clear();
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        KeySet(Float2FloatOpenHashMap float2FloatOpenHashMap, 1 var2_2) {
            this(float2FloatOpenHashMap);
        }
    }

    private final class KeyIterator
    extends MapIterator
    implements FloatIterator {
        final Float2FloatOpenHashMap this$0;

        public KeyIterator(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
            super(float2FloatOpenHashMap, null);
        }

        @Override
        public float nextFloat() {
            return this.this$0.key[this.nextEntry()];
        }
    }

    private final class MapEntrySet
    extends AbstractObjectSet<Float2FloatMap.Entry>
    implements Float2FloatMap.FastEntrySet {
        final Float2FloatOpenHashMap this$0;

        private MapEntrySet(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
        }

        @Override
        public ObjectIterator<Float2FloatMap.Entry> iterator() {
            return new EntryIterator(this.this$0, null);
        }

        @Override
        public ObjectIterator<Float2FloatMap.Entry> fastIterator() {
            return new FastEntryIterator(this.this$0, null);
        }

        @Override
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            if (entry.getKey() == null || !(entry.getKey() instanceof Float)) {
                return true;
            }
            if (entry.getValue() == null || !(entry.getValue() instanceof Float)) {
                return true;
            }
            float f = ((Float)entry.getKey()).floatValue();
            float f2 = ((Float)entry.getValue()).floatValue();
            if (Float.floatToIntBits(f) == 0) {
                return this.this$0.containsNullKey && Float.floatToIntBits(this.this$0.value[this.this$0.n]) == Float.floatToIntBits(f2);
            }
            float[] fArray = this.this$0.key;
            int n = HashCommon.mix(HashCommon.float2int(f)) & this.this$0.mask;
            float f3 = fArray[n];
            if (Float.floatToIntBits(f3) == 0) {
                return true;
            }
            if (Float.floatToIntBits(f) == Float.floatToIntBits(f3)) {
                return Float.floatToIntBits(this.this$0.value[n]) == Float.floatToIntBits(f2);
            }
            do {
                if (Float.floatToIntBits(f3 = fArray[n = n + 1 & this.this$0.mask]) != 0) continue;
                return true;
            } while (Float.floatToIntBits(f) != Float.floatToIntBits(f3));
            return Float.floatToIntBits(this.this$0.value[n]) == Float.floatToIntBits(f2);
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            if (entry.getKey() == null || !(entry.getKey() instanceof Float)) {
                return true;
            }
            if (entry.getValue() == null || !(entry.getValue() instanceof Float)) {
                return true;
            }
            float f = ((Float)entry.getKey()).floatValue();
            float f2 = ((Float)entry.getValue()).floatValue();
            if (Float.floatToIntBits(f) == 0) {
                if (this.this$0.containsNullKey && Float.floatToIntBits(this.this$0.value[this.this$0.n]) == Float.floatToIntBits(f2)) {
                    Float2FloatOpenHashMap.access$300(this.this$0);
                    return false;
                }
                return true;
            }
            float[] fArray = this.this$0.key;
            int n = HashCommon.mix(HashCommon.float2int(f)) & this.this$0.mask;
            float f3 = fArray[n];
            if (Float.floatToIntBits(f3) == 0) {
                return true;
            }
            if (Float.floatToIntBits(f3) == Float.floatToIntBits(f)) {
                if (Float.floatToIntBits(this.this$0.value[n]) == Float.floatToIntBits(f2)) {
                    Float2FloatOpenHashMap.access$400(this.this$0, n);
                    return false;
                }
                return true;
            }
            do {
                if (Float.floatToIntBits(f3 = fArray[n = n + 1 & this.this$0.mask]) != 0) continue;
                return true;
            } while (Float.floatToIntBits(f3) != Float.floatToIntBits(f) || Float.floatToIntBits(this.this$0.value[n]) != Float.floatToIntBits(f2));
            Float2FloatOpenHashMap.access$400(this.this$0, n);
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
        public void forEach(Consumer<? super Float2FloatMap.Entry> consumer) {
            if (this.this$0.containsNullKey) {
                consumer.accept(new AbstractFloat2FloatMap.BasicEntry(this.this$0.key[this.this$0.n], this.this$0.value[this.this$0.n]));
            }
            int n = this.this$0.n;
            while (n-- != 0) {
                if (Float.floatToIntBits(this.this$0.key[n]) == 0) continue;
                consumer.accept(new AbstractFloat2FloatMap.BasicEntry(this.this$0.key[n], this.this$0.value[n]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Float2FloatMap.Entry> consumer) {
            AbstractFloat2FloatMap.BasicEntry basicEntry = new AbstractFloat2FloatMap.BasicEntry();
            if (this.this$0.containsNullKey) {
                basicEntry.key = this.this$0.key[this.this$0.n];
                basicEntry.value = this.this$0.value[this.this$0.n];
                consumer.accept(basicEntry);
            }
            int n = this.this$0.n;
            while (n-- != 0) {
                if (Float.floatToIntBits(this.this$0.key[n]) == 0) continue;
                basicEntry.key = this.this$0.key[n];
                basicEntry.value = this.this$0.value[n];
                consumer.accept(basicEntry);
            }
        }

        @Override
        public Iterator iterator() {
            return this.iterator();
        }

        MapEntrySet(Float2FloatOpenHashMap float2FloatOpenHashMap, 1 var2_2) {
            this(float2FloatOpenHashMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class FastEntryIterator
    extends MapIterator
    implements ObjectIterator<Float2FloatMap.Entry> {
        private final MapEntry entry;
        final Float2FloatOpenHashMap this$0;

        private FastEntryIterator(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
            super(float2FloatOpenHashMap, null);
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

        FastEntryIterator(Float2FloatOpenHashMap float2FloatOpenHashMap, 1 var2_2) {
            this(float2FloatOpenHashMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    private class EntryIterator
    extends MapIterator
    implements ObjectIterator<Float2FloatMap.Entry> {
        private MapEntry entry;
        final Float2FloatOpenHashMap this$0;

        private EntryIterator(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
            super(float2FloatOpenHashMap, null);
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

        EntryIterator(Float2FloatOpenHashMap float2FloatOpenHashMap, 1 var2_2) {
            this(float2FloatOpenHashMap);
        }
    }

    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        FloatArrayList wrapped;
        final Float2FloatOpenHashMap this$0;

        private MapIterator(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
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
            float[] fArray = this.this$0.key;
            do {
                if (--this.pos >= 0) continue;
                this.last = Integer.MIN_VALUE;
                float f = this.wrapped.getFloat(-this.pos - 1);
                int n = HashCommon.mix(HashCommon.float2int(f)) & this.this$0.mask;
                while (Float.floatToIntBits(f) != Float.floatToIntBits(fArray[n])) {
                    n = n + 1 & this.this$0.mask;
                }
                return n;
            } while (Float.floatToIntBits(fArray[this.pos]) == 0);
            this.last = this.pos;
            return this.last;
        }

        private void shiftKeys(int n) {
            float[] fArray = this.this$0.key;
            while (true) {
                float f;
                int n2 = n;
                n = n2 + 1 & this.this$0.mask;
                while (true) {
                    if (Float.floatToIntBits(f = fArray[n]) == 0) {
                        fArray[n2] = 0.0f;
                        return;
                    }
                    int n3 = HashCommon.mix(HashCommon.float2int(f)) & this.this$0.mask;
                    if (n2 <= n ? n2 >= n3 || n3 > n : n2 >= n3 && n3 > n) break;
                    n = n + 1 & this.this$0.mask;
                }
                if (n < n2) {
                    if (this.wrapped == null) {
                        this.wrapped = new FloatArrayList(2);
                    }
                    this.wrapped.add(fArray[n]);
                }
                fArray[n2] = f;
                this.this$0.value[n2] = this.this$0.value[n];
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
                this.this$0.remove(this.wrapped.getFloat(-this.pos - 1));
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

        MapIterator(Float2FloatOpenHashMap float2FloatOpenHashMap, 1 var2_2) {
            this(float2FloatOpenHashMap);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    final class MapEntry
    implements Float2FloatMap.Entry,
    Map.Entry<Float, Float> {
        int index;
        final Float2FloatOpenHashMap this$0;

        MapEntry(Float2FloatOpenHashMap float2FloatOpenHashMap, int n) {
            this.this$0 = float2FloatOpenHashMap;
            this.index = n;
        }

        MapEntry(Float2FloatOpenHashMap float2FloatOpenHashMap) {
            this.this$0 = float2FloatOpenHashMap;
        }

        @Override
        public float getFloatKey() {
            return this.this$0.key[this.index];
        }

        @Override
        public float getFloatValue() {
            return this.this$0.value[this.index];
        }

        @Override
        public float setValue(float f) {
            float f2 = this.this$0.value[this.index];
            this.this$0.value[this.index] = f;
            return f2;
        }

        @Override
        @Deprecated
        public Float getKey() {
            return Float.valueOf(this.this$0.key[this.index]);
        }

        @Override
        @Deprecated
        public Float getValue() {
            return Float.valueOf(this.this$0.value[this.index]);
        }

        @Override
        @Deprecated
        public Float setValue(Float f) {
            return Float.valueOf(this.setValue(f.floatValue()));
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry)) {
                return true;
            }
            Map.Entry entry = (Map.Entry)object;
            return Float.floatToIntBits(this.this$0.key[this.index]) == Float.floatToIntBits(((Float)entry.getKey()).floatValue()) && Float.floatToIntBits(this.this$0.value[this.index]) == Float.floatToIntBits(((Float)entry.getValue()).floatValue());
        }

        @Override
        public int hashCode() {
            return HashCommon.float2int(this.this$0.key[this.index]) ^ HashCommon.float2int(this.this$0.value[this.index]);
        }

        public String toString() {
            return this.this$0.key[this.index] + "=>" + this.this$0.value[this.index];
        }

        @Override
        @Deprecated
        public Object setValue(Object object) {
            return this.setValue((Float)object);
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

