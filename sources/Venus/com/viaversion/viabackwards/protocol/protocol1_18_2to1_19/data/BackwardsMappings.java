/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data;

import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class BackwardsMappings
extends com.viaversion.viabackwards.api.data.BackwardsMappings {
    private final Int2ObjectMap<CompoundTag> defaultChatTypes = new Int2ObjectOpenHashMap<CompoundTag>();

    public BackwardsMappings() {
        super("1.19", "1.18", Protocol1_19To1_18_2.class);
    }

    @Override
    protected void loadExtras(CompoundTag compoundTag) {
        super.loadExtras(compoundTag);
        try {
            ListTag listTag = (ListTag)BinaryTagIO.readInputStream(VBMappingDataLoader.getResource("chat-types-1.19.1.nbt")).get("values");
            for (Tag tag : listTag) {
                CompoundTag compoundTag2 = (CompoundTag)tag;
                NumberTag numberTag = (NumberTag)compoundTag2.get("id");
                this.defaultChatTypes.put(numberTag.asInt(), compoundTag2);
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public @Nullable CompoundTag chatType(int n) {
        return (CompoundTag)this.defaultChatTypes.get(n);
    }
}

