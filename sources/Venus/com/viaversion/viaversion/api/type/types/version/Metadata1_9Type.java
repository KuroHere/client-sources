/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viaversion.api.type.types.minecraft.ModernMetaType;

public class Metadata1_9Type
extends ModernMetaType {
    @Override
    protected MetaType getType(int n) {
        return MetaType1_9.byId(n);
    }
}

