/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import java.io.Serializable;

public class Int2ObjectFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Int2ObjectFunctions() {
    }

    public static <V> Int2ObjectFunction<V> singleton(int key, V value) {
        return new Singleton<V>(key, value);
    }

    public static <V> Int2ObjectFunction<V> singleton(Integer key, V value) {
        return new Singleton<V>(key, value);
    }

    public static <V> Int2ObjectFunction<V> synchronize(Int2ObjectFunction<V> f) {
        return new SynchronizedFunction<V>(f);
    }

    public static <V> Int2ObjectFunction<V> synchronize(Int2ObjectFunction<V> f, Object sync) {
        return new SynchronizedFunction<V>(f, sync);
    }

    public static <V> Int2ObjectFunction<V> unmodifiable(Int2ObjectFunction<V> f) {
        return new UnmodifiableFunction<V>(f);
    }

    public static class UnmodifiableFunction<V>
    extends AbstractInt2ObjectFunction<V>
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectFunction<V> function;

        protected UnmodifiableFunction(Int2ObjectFunction<V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }

        @Override
        public int size() {
            return this.function.size();
        }

        @Override
        public boolean containsKey(int k) {
            return this.function.containsKey(k);
        }

        @Override
        public V defaultReturnValue() {
            return this.function.defaultReturnValue();
        }

        @Override
        public void defaultReturnValue(V defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V put(int k, V v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return this.function.toString();
        }

        @Override
        public V remove(int k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V get(int k) {
            return this.function.get(k);
        }

        @Override
        public boolean containsKey(Object ok) {
            return this.function.containsKey(ok);
        }

        @Override
        public V remove(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V get(Object k) {
            return this.function.get(k);
        }
    }

    public static class SynchronizedFunction<V>
    extends AbstractInt2ObjectFunction<V>
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectFunction<V> function;
        protected final Object sync;

        protected SynchronizedFunction(Int2ObjectFunction<V> f, Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }

        protected SynchronizedFunction(Int2ObjectFunction<V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(int k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.containsKey(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V defaultReturnValue() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.defaultReturnValue();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void defaultReturnValue(V defRetValue) {
            Object object = this.sync;
            synchronized (object) {
                this.function.defaultReturnValue(defRetValue);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V put(int k, V v) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.put(k, v);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.sync;
            synchronized (object) {
                this.function.clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public String toString() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.toString();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public V put(Integer k, V v) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.put(k, v);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V get(Object k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.get(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V remove(Object k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.remove(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V remove(int k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.remove(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V get(int k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.get(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(Object ok) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.containsKey(ok);
            }
        }
    }

    public static class Singleton<V>
    extends AbstractInt2ObjectFunction<V>
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final V value;

        protected Singleton(int key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean containsKey(int k) {
            return this.key == k;
        }

        @Override
        public V get(int k) {
            if (this.key == k) {
                return this.value;
            }
            return (V)this.defRetValue;
        }

        @Override
        public int size() {
            return 1;
        }

        public Object clone() {
            return this;
        }
    }

    public static class EmptyFunction<V>
    extends AbstractInt2ObjectFunction<V>
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyFunction() {
        }

        @Override
        public V get(int k) {
            return null;
        }

        @Override
        public boolean containsKey(int k) {
            return false;
        }

        @Override
        public V defaultReturnValue() {
            return null;
        }

        @Override
        public void defaultReturnValue(V defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V get(Object k) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void clear() {
        }

        private Object readResolve() {
            return EMPTY_FUNCTION;
        }

        public Object clone() {
            return EMPTY_FUNCTION;
        }
    }
}

