/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package joptsimple;

import java.util.Collections;
import joptsimple.OptionException;

class UnrecognizedOptionException
extends OptionException {
    private static final long serialVersionUID = -1L;

    UnrecognizedOptionException(String string) {
        super(Collections.singletonList(string));
    }

    @Override
    Object[] messageArguments() {
        return new Object[]{this.singleOptionString()};
    }
}

