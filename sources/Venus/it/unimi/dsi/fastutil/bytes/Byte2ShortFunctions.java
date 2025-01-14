/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.AbstractByte2ShortFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public final class Byte2ShortFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Byte2ShortFunctions() {
    }

    public static Byte2ShortFunction singleton(byte by, short s) {
        return new Singleton(by, s);
    }

    public static Byte2ShortFunction singleton(Byte by, Short s) {
        return new Singleton(by, s);
    }

    public static Byte2ShortFunction synchronize(Byte2ShortFunction byte2ShortFunction) {
        return new SynchronizedFunction(byte2ShortFunction);
    }

    public static Byte2ShortFunction synchronize(Byte2ShortFunction byte2ShortFunction, Object object) {
        return new SynchronizedFunction(byte2ShortFunction, object);
    }

    public static Byte2ShortFunction unmodifiable(Byte2ShortFunction byte2ShortFunction) {
        return new UnmodifiableFunction(byte2ShortFunction);
    }

    public static Byte2ShortFunction primitive(java.util.function.Function<? super Byte, ? extends Short> function) {
        Objects.requireNonNull(function);
        if (function instanceof Byte2ShortFunction) {
            return (Byte2ShortFunction)function;
        }
        if (function instanceof IntUnaryOperator) {
            return arg_0 -> Byte2ShortFunctions.lambda$primitive$0(function, arg_0);
        }
        return new PrimitiveFunction(function);
    }

    private static short lambda$primitive$0(java.util.function.Function function, byte by) {
        return SafeMath.safeIntToShort(((IntUnaryOperator)((Object)function)).applyAsInt(by));
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class PrimitiveFunction
    implements Byte2ShortFunction {
        protected final java.util.function.Function<? super Byte, ? extends Short> function;

        protected PrimitiveFunction(java.util.function.Function<? super Byte, ? extends Short> function) {
            this.function = function;
        }

        @Override
        public boolean containsKey(byte by) {
            return this.function.apply((Byte)by) != null;
        }

        @Override
        @Deprecated
        public boolean containsKey(Object object) {
            if (object == null) {
                return true;
            }
            return this.function.apply((Byte)object) != null;
        }

        @Override
        public short get(byte by) {
            Short s = this.function.apply((Byte)by);
            if (s == null) {
                return this.defaultReturnValue();
            }
            return s;
        }

        @Override
        @Deprecated
        public Short get(Object object) {
            if (object == null) {
                return null;
            }
            return this.function.apply((Byte)object);
        }

        @Override
        @Deprecated
        public Short put(Byte by, Short s) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Object get(Object object) {
            return this.get(object);
        }

        @Override
        @Deprecated
        public Object put(Object object, Object object2) {
            return this.put((Byte)object, (Short)object2);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class UnmodifiableFunction
    extends AbstractByte2ShortFunction
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ShortFunction function;

        protected UnmodifiableFunction(Byte2ShortFunction byte2ShortFunction) {
            if (byte2ShortFunction == null) {
                throw new NullPointerException();
            }
            this.function = byte2ShortFunction;
        }

        @Override
        public int size() {
            return this.function.size();
        }

        @Override
        public short defaultReturnValue() {
            return this.function.defaultReturnValue();
        }

        @Override
        public void defaultReturnValue(short s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsKey(byte by) {
            return this.function.containsKey(by);
        }

        @Override
        public short put(byte by, short s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public short get(byte by) {
            return this.function.get(by);
        }

        @Override
        public short remove(byte by) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Short put(Byte by, Short s) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Short get(Object object) {
            return this.function.get(object);
        }

        @Override
        @Deprecated
        public Short remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public int hashCode() {
            return this.function.hashCode();
        }

        public boolean equals(Object object) {
            return object == this || this.function.equals(object);
        }

        public String toString() {
            return this.function.toString();
        }

        @Override
        @Deprecated
        public Object remove(Object object) {
            return this.remove(object);
        }

        @Override
        @Deprecated
        public Object get(Object object) {
            return this.get(object);
        }

        @Override
        @Deprecated
        public Object put(Object object, Object object2) {
            return this.put((Byte)object, (Short)object2);
        }
    }

    /*
     * Duplicate member names - consider using --renamedupmembers true
     */
    public static class SynchronizedFunction
    implements Byte2ShortFunction,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ShortFunction function;
        protected final Object sync;

        protected SynchronizedFunction(Byte2ShortFunction byte2ShortFunction, Object object) {
            if (byte2ShortFunction == null) {
                throw new NullPointerException();
            }
            this.function = byte2ShortFunction;
            this.sync = object;
        }

        protected SynchronizedFunction(Byte2ShortFunction byte2ShortFunction) {
            if (byte2ShortFunction == null) {
                throw new NullPointerException();
            }
            this.function = byte2ShortFunction;
            this.sync = this;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public int applyAsInt(int n) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.applyAsInt(n);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Short apply(Byte by) {
            Object object = this.sync;
            synchronized (object) {
                return (Short)this.function.apply(by);
            }
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
        public short defaultReturnValue() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.defaultReturnValue();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void defaultReturnValue(short s) {
            Object object = this.sync;
            synchronized (object) {
                this.function.defaultReturnValue(s);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(byte by) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.containsKey(by);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public boolean containsKey(Object object) {
            Object object2 = this.sync;
            synchronized (object2) {
                return this.function.containsKey(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public short put(byte by, short s) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.put(by, s);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public short get(byte by) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.get(by);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public short remove(byte by) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.remove(by);
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
        @Override
        @Deprecated
        public Short put(Byte by, Short s) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.put(by, s);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Short get(Object object) {
            Object object2 = this.sync;
            synchronized (object2) {
                return this.function.get(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Short remove(Object object) {
            Object object2 = this.sync;
            synchronized (object2) {
                return this.function.remove(object);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public int hashCode() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.hashCode();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public boolean equals(Object object) {
            if (object == this) {
                return false;
            }
            Object object2 = this.sync;
            synchronized (object2) {
                return this.function.equals(object);
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
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            Object object = this.sync;
            synchronized (object) {
                objectOutputStream.defaultWriteObject();
            }
        }

        @Override
        @Deprecated
        public Object remove(Object object) {
            return this.remove(object);
        }

        @Override
        @Deprecated
        public Object get(Object object) {
            return this.get(object);
        }

        @Override
        @Deprecated
        public Object put(Object object, Object object2) {
            return this.put((Byte)object, (Short)object2);
        }

        @Override
        @Deprecated
        public Object apply(Object object) {
            return this.apply((Byte)object);
        }
    }

    public static class Singleton
    extends AbstractByte2ShortFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final byte key;
        protected final short value;

        protected Singleton(byte by, short s) {
            this.key = by;
            this.value = s;
        }

        @Override
        public boolean containsKey(byte by) {
            return this.key == by;
        }

        @Override
        public short get(byte by) {
            return this.key == by ? this.value : this.defRetValue;
        }

        @Override
        public int size() {
            return 0;
        }

        public Object clone() {
            return this;
        }
    }

    public static class EmptyFunction
    extends AbstractByte2ShortFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyFunction() {
        }

        @Override
        public short get(byte by) {
            return 1;
        }

        @Override
        public boolean containsKey(byte by) {
            return true;
        }

        @Override
        public short defaultReturnValue() {
            return 1;
        }

        @Override
        public void defaultReturnValue(short s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public void clear() {
        }

        public Object clone() {
            return EMPTY_FUNCTION;
        }

        public int hashCode() {
            return 1;
        }

        public boolean equals(Object object) {
            if (!(object instanceof Function)) {
                return true;
            }
            return ((Function)object).size() == 0;
        }

        public String toString() {
            return "{}";
        }

        private Object readResolve() {
            return EMPTY_FUNCTION;
        }
    }
}

