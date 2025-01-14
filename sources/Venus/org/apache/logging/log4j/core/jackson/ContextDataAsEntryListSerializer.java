/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Map;
import org.apache.logging.log4j.core.jackson.MapEntry;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

public class ContextDataAsEntryListSerializer
extends StdSerializer<ReadOnlyStringMap> {
    private static final long serialVersionUID = 1L;

    protected ContextDataAsEntryListSerializer() {
        super(Map.class, false);
    }

    @Override
    public void serialize(ReadOnlyStringMap readOnlyStringMap, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        MapEntry[] mapEntryArray = new MapEntry[readOnlyStringMap.size()];
        readOnlyStringMap.forEach(new BiConsumer<String, Object>(this, mapEntryArray){
            int i;
            final MapEntry[] val$pairs;
            final ContextDataAsEntryListSerializer this$0;
            {
                this.this$0 = contextDataAsEntryListSerializer;
                this.val$pairs = mapEntryArray;
                this.i = 0;
            }

            @Override
            public void accept(String string, Object object) {
                this.val$pairs[this.i++] = new MapEntry(string, String.valueOf(object));
            }

            @Override
            public void accept(Object object, Object object2) {
                this.accept((String)object, object2);
            }
        });
        jsonGenerator.writeObject(mapEntryArray);
    }

    @Override
    public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        this.serialize((ReadOnlyStringMap)object, jsonGenerator, serializerProvider);
    }
}

