/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
final class Present<T>
extends Optional<T> {
    private final T reference;
    private static final long serialVersionUID = 0L;

    Present(T t) {
        this.reference = t;
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public T get() {
        return this.reference;
    }

    @Override
    public T or(T t) {
        Preconditions.checkNotNull(t, "use Optional.orNull() instead of Optional.or(null)");
        return this.reference;
    }

    @Override
    public Optional<T> or(Optional<? extends T> optional) {
        Preconditions.checkNotNull(optional);
        return this;
    }

    @Override
    public T or(Supplier<? extends T> supplier) {
        Preconditions.checkNotNull(supplier);
        return this.reference;
    }

    @Override
    public T orNull() {
        return this.reference;
    }

    @Override
    public Set<T> asSet() {
        return Collections.singleton(this.reference);
    }

    @Override
    public <V> Optional<V> transform(Function<? super T, V> function) {
        return new Present<V>(Preconditions.checkNotNull(function.apply(this.reference), "the Function passed to Optional.transform() must not return null."));
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Present) {
            Present present = (Present)object;
            return this.reference.equals(present.reference);
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 1502476572 + this.reference.hashCode();
    }

    @Override
    public String toString() {
        return "Optional.of(" + this.reference + ")";
    }
}

