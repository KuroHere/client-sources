/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl;

import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl.NBTLegacyHoverEventSerializerImpl;
import org.jetbrains.annotations.NotNull;

public interface NBTLegacyHoverEventSerializer
extends LegacyHoverEventSerializer {
    @NotNull
    public static LegacyHoverEventSerializer get() {
        return NBTLegacyHoverEventSerializerImpl.INSTANCE;
    }
}

