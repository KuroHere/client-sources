/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.protocols.protocol1_14_1to1_14.metadata;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.rewriter.EntityRewriter;
import java.util.List;

public class MetadataRewriter1_14_1To1_14
extends EntityRewriter<ClientboundPackets1_14, Protocol1_14_1To1_14> {
    public MetadataRewriter1_14_1To1_14(Protocol1_14_1To1_14 protocol) {
        super(protocol);
    }

    @Override
    public void handleMetadata(int entityId, EntityType type2, Metadata metadata, List<Metadata> metadatas, UserConnection connection) {
        if (type2 == null) {
            return;
        }
        if ((type2 == Entity1_14Types.VILLAGER || type2 == Entity1_14Types.WANDERING_TRADER) && metadata.id() >= 15) {
            metadata.setId(metadata.id() + 1);
        }
    }

    @Override
    public EntityType typeFromId(int type2) {
        return Entity1_14Types.getTypeFromId(type2);
    }
}

