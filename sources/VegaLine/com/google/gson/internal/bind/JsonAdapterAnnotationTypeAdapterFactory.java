/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        Class<T> rawType = targetType.getRawType();
        JsonAdapter annotation = rawType.getAnnotation(JsonAdapter.class);
        if (annotation == null) {
            return null;
        }
        return this.getTypeAdapter(this.constructorConstructor, gson, targetType, annotation);
    }

    TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> type2, JsonAdapter annotation) {
        TypeAdapter<?> typeAdapter;
        Object instance = constructorConstructor.get(TypeToken.get(annotation.value())).construct();
        if (instance instanceof TypeAdapter) {
            typeAdapter = (TypeAdapter<?>)instance;
        } else if (instance instanceof TypeAdapterFactory) {
            typeAdapter = ((TypeAdapterFactory)instance).create(gson, type2);
        } else if (instance instanceof JsonSerializer || instance instanceof JsonDeserializer) {
            JsonSerializer serializer = instance instanceof JsonSerializer ? (JsonSerializer)instance : null;
            JsonDeserializer deserializer = instance instanceof JsonDeserializer ? (JsonDeserializer)instance : null;
            typeAdapter = new TreeTypeAdapter(serializer, deserializer, gson, type2, null);
        } else {
            throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer reference.");
        }
        if (typeAdapter != null && annotation.nullSafe()) {
            typeAdapter = typeAdapter.nullSafe();
        }
        return typeAdapter;
    }
}

