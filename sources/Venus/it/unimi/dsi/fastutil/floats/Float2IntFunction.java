/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleToIntFunction;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
@FunctionalInterface
public interface Float2IntFunction
extends Function<Float, Integer>,
DoubleToIntFunction {
    @Override
    @Deprecated
    default public int applyAsInt(double d) {
        return this.get(SafeMath.safeDoubleToFloat(d));
    }

    @Override
    default public int put(float f, int n) {
        throw new UnsupportedOperationException();
    }

    public int get(float var1);

    default public int remove(float f) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Integer put(Float f, Integer n) {
        float f2 = f.floatValue();
        boolean bl = this.containsKey(f2);
        int n2 = this.put(f2, (int)n);
        return bl ? Integer.valueOf(n2) : null;
    }

    @Override
    @Deprecated
    default public Integer get(Object object) {
        if (object == null) {
            return null;
        }
        float f = ((Float)object).floatValue();
        int n = this.get(f);
        return n != this.defaultReturnValue() || this.containsKey(f) ? Integer.valueOf(n) : null;
    }

    @Override
    @Deprecated
    default public Integer remove(Object object) {
        if (object == null) {
            return null;
        }
        float f = ((Float)object).floatValue();
        return this.containsKey(f) ? Integer.valueOf(this.remove(f)) : null;
    }

    default public boolean containsKey(float f) {
        return false;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object object) {
        return object == null ? false : this.containsKey(((Float)object).floatValue());
    }

    default public void defaultReturnValue(int n) {
        throw new UnsupportedOperationException();
    }

    default public int defaultReturnValue() {
        return 1;
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
        return this.put((Float)object, (Integer)object2);
    }
}

