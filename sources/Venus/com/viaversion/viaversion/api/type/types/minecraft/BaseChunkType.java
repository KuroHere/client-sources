/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.Type;

public abstract class BaseChunkType
extends Type<Chunk> {
    protected BaseChunkType() {
        super(Chunk.class);
    }

    protected BaseChunkType(String string) {
        super(string, Chunk.class);
    }

    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
}

