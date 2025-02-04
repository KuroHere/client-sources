/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public interface Int2IntMap
extends Int2IntFunction,
Map<Integer, Integer> {
    @Override
    public int size();

    @Override
    default public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void defaultReturnValue(int var1);

    @Override
    public int defaultReturnValue();

    public ObjectSet<Entry> int2IntEntrySet();

    @Override
    @Deprecated
    default public ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
        return this.int2IntEntrySet();
    }

    @Override
    @Deprecated
    default public Integer put(Integer n, Integer n2) {
        return Int2IntFunction.super.put(n, n2);
    }

    @Override
    @Deprecated
    default public Integer get(Object object) {
        return Int2IntFunction.super.get(object);
    }

    @Override
    @Deprecated
    default public Integer remove(Object object) {
        return Int2IntFunction.super.remove(object);
    }

    public IntSet keySet();

    public IntCollection values();

    @Override
    public boolean containsKey(int var1);

    @Override
    @Deprecated
    default public boolean containsKey(Object object) {
        return Int2IntFunction.super.containsKey(object);
    }

    public boolean containsValue(int var1);

    @Override
    @Deprecated
    default public boolean containsValue(Object object) {
        return object == null ? false : this.containsValue((Integer)object);
    }

    default public int getOrDefault(int n, int n2) {
        int n3 = this.get(n);
        return n3 != this.defaultReturnValue() || this.containsKey(n) ? n3 : n2;
    }

    @Override
    default public int putIfAbsent(int n, int n2) {
        int n3;
        int n4 = this.get(n);
        if (n4 != (n3 = this.defaultReturnValue()) || this.containsKey(n)) {
            return n4;
        }
        this.put(n, n2);
        return n3;
    }

    default public boolean remove(int n, int n2) {
        int n3 = this.get(n);
        if (n3 != n2 || n3 == this.defaultReturnValue() && !this.containsKey(n)) {
            return true;
        }
        this.remove(n);
        return false;
    }

    @Override
    default public boolean replace(int n, int n2, int n3) {
        int n4 = this.get(n);
        if (n4 != n2 || n4 == this.defaultReturnValue() && !this.containsKey(n)) {
            return true;
        }
        this.put(n, n3);
        return false;
    }

    @Override
    default public int replace(int n, int n2) {
        return this.containsKey(n) ? this.put(n, n2) : this.defaultReturnValue();
    }

    default public int computeIfAbsent(int n, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        int n2 = this.get(n);
        if (n2 != this.defaultReturnValue() || this.containsKey(n)) {
            return n2;
        }
        int n3 = intUnaryOperator.applyAsInt(n);
        this.put(n, n3);
        return n3;
    }

    default public int computeIfAbsentNullable(int n, IntFunction<? extends Integer> intFunction) {
        Objects.requireNonNull(intFunction);
        int n2 = this.get(n);
        int n3 = this.defaultReturnValue();
        if (n2 != n3 || this.containsKey(n)) {
            return n2;
        }
        Integer n4 = intFunction.apply(n);
        if (n4 == null) {
            return n3;
        }
        int n5 = n4;
        this.put(n, n5);
        return n5;
    }

    default public int computeIfAbsentPartial(int n, Int2IntFunction int2IntFunction) {
        Objects.requireNonNull(int2IntFunction);
        int n2 = this.get(n);
        int n3 = this.defaultReturnValue();
        if (n2 != n3 || this.containsKey(n)) {
            return n2;
        }
        if (!int2IntFunction.containsKey(n)) {
            return n3;
        }
        int n4 = int2IntFunction.get(n);
        this.put(n, n4);
        return n4;
    }

    @Override
    default public int computeIfPresent(int n, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.get(n);
        int n3 = this.defaultReturnValue();
        if (n2 == n3 && !this.containsKey(n)) {
            return n3;
        }
        Integer n4 = biFunction.apply((Integer)n, (Integer)n2);
        if (n4 == null) {
            this.remove(n);
            return n3;
        }
        int n5 = n4;
        this.put(n, n5);
        return n5;
    }

    @Override
    default public int compute(int n, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        Objects.requireNonNull(biFunction);
        int n2 = this.get(n);
        int n3 = this.defaultReturnValue();
        boolean bl = n2 != n3 || this.containsKey(n);
        Integer n4 = biFunction.apply((Integer)n, bl ? Integer.valueOf(n2) : null);
        if (n4 == null) {
            if (bl) {
                this.remove(n);
            }
            return n3;
        }
        int n5 = n4;
        this.put(n, n5);
        return n5;
    }

    @Override
    default public int merge(int n, int n2, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        int n3;
        Objects.requireNonNull(biFunction);
        int n4 = this.get(n);
        int n5 = this.defaultReturnValue();
        if (n4 != n5 || this.containsKey(n)) {
            Integer n6 = biFunction.apply((Integer)n4, (Integer)n2);
            if (n6 == null) {
                this.remove(n);
                return n5;
            }
            n3 = n6;
        } else {
            n3 = n2;
        }
        this.put(n, n3);
        return n3;
    }

    @Override
    @Deprecated
    default public Integer getOrDefault(Object object, Integer n) {
        return Map.super.getOrDefault(object, n);
    }

    @Override
    @Deprecated
    default public Integer putIfAbsent(Integer n, Integer n2) {
        return Map.super.putIfAbsent(n, n2);
    }

    @Override
    @Deprecated
    default public boolean remove(Object object, Object object2) {
        return Map.super.remove(object, object2);
    }

    @Override
    @Deprecated
    default public boolean replace(Integer n, Integer n2, Integer n3) {
        return Map.super.replace(n, n2, n3);
    }

    @Override
    @Deprecated
    default public Integer replace(Integer n, Integer n2) {
        return Map.super.replace(n, n2);
    }

    @Override
    @Deprecated
    default public Integer computeIfAbsent(Integer n, Function<? super Integer, ? extends Integer> function) {
        return Map.super.computeIfAbsent(n, function);
    }

    @Override
    @Deprecated
    default public Integer computeIfPresent(Integer n, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        return Map.super.computeIfPresent(n, biFunction);
    }

    @Override
    @Deprecated
    default public Integer compute(Integer n, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        return Map.super.compute(n, biFunction);
    }

    @Override
    @Deprecated
    default public Integer merge(Integer n, Integer n2, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        return Map.super.merge(n, n2, biFunction);
    }

    @Override
    @Deprecated
    default public Object remove(Object object) {
        return this.remove(object);
    }

    @Override
    @Deprecated
    default public Object get(Object object) {
        return this.get(object);
    }

    @Override
    @Deprecated
    default public Object put(Object object, Object object2) {
        return this.put((Integer)object, (Integer)object2);
    }

    @Override
    @Deprecated
    default public Object merge(Object object, Object object2, BiFunction biFunction) {
        return this.merge((Integer)object, (Integer)object2, (BiFunction<? super Integer, ? super Integer, ? extends Integer>)biFunction);
    }

    @Override
    @Deprecated
    default public Object compute(Object object, BiFunction biFunction) {
        return this.compute((Integer)object, (BiFunction<? super Integer, ? super Integer, ? extends Integer>)biFunction);
    }

    @Override
    @Deprecated
    default public Object computeIfPresent(Object object, BiFunction biFunction) {
        return this.computeIfPresent((Integer)object, (BiFunction<? super Integer, ? super Integer, ? extends Integer>)biFunction);
    }

    @Override
    @Deprecated
    default public Object computeIfAbsent(Object object, Function function) {
        return this.computeIfAbsent((Integer)object, (Function<? super Integer, ? extends Integer>)function);
    }

    @Override
    @Deprecated
    default public Object replace(Object object, Object object2) {
        return this.replace((Integer)object, (Integer)object2);
    }

    @Override
    @Deprecated
    default public boolean replace(Object object, Object object2, Object object3) {
        return this.replace((Integer)object, (Integer)object2, (Integer)object3);
    }

    @Override
    @Deprecated
    default public Object putIfAbsent(Object object, Object object2) {
        return this.putIfAbsent((Integer)object, (Integer)object2);
    }

    @Override
    @Deprecated
    default public Object getOrDefault(Object object, Object object2) {
        return this.getOrDefault(object, (Integer)object2);
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

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static interface Entry
    extends Map.Entry<Integer, Integer> {
        public int getIntKey();

        @Override
        @Deprecated
        default public Integer getKey() {
            return this.getIntKey();
        }

        public int getIntValue();

        @Override
        public int setValue(int var1);

        @Override
        @Deprecated
        default public Integer getValue() {
            return this.getIntValue();
        }

        @Override
        @Deprecated
        default public Integer setValue(Integer n) {
            return this.setValue((int)n);
        }

        @Override
        @Deprecated
        default public Object setValue(Object object) {
            return this.setValue((Integer)object);
        }

        @Override
        @Deprecated
        default public Object getValue() {
            return this.getValue();
        }

        @Override
        @Deprecated
        default public Object getKey() {
            return this.getKey();
        }
    }

    public static interface FastEntrySet
    extends ObjectSet<Entry> {
        public ObjectIterator<Entry> fastIterator();

        default public void fastForEach(Consumer<? super Entry> consumer) {
            this.forEach(consumer);
        }
    }
}

