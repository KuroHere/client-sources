/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.VerifyException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class Verify {
    public static void verify(boolean bl) {
        if (!bl) {
            throw new VerifyException();
        }
    }

    public static void verify(boolean bl, @Nullable String string, @Nullable Object ... objectArray) {
        if (!bl) {
            throw new VerifyException(Preconditions.format(string, objectArray));
        }
    }

    @CanIgnoreReturnValue
    public static <T> T verifyNotNull(@Nullable T t) {
        return Verify.verifyNotNull(t, "expected a non-null reference", new Object[0]);
    }

    @CanIgnoreReturnValue
    public static <T> T verifyNotNull(@Nullable T t, @Nullable String string, @Nullable Object ... objectArray) {
        Verify.verify(t != null, string, objectArray);
        return t;
    }

    private Verify() {
    }
}

