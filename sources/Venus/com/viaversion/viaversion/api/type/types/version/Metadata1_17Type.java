/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.ModernMetaType;
import com.viaversion.viaversion.api.type.types.version.Types1_17;

@Deprecated
public class Metadata1_17Type
extends ModernMetaType {
    @Override
    protected MetaType getType(int n) {
        return Types1_17.META_TYPES.byId(n);
    }
}

