/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.http.client.entity;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.entity.DeflateInputStreamFactory;

public class DeflateDecompressingEntity
extends DecompressingEntity {
    public DeflateDecompressingEntity(HttpEntity httpEntity) {
        super(httpEntity, DeflateInputStreamFactory.getInstance());
    }
}

