/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.protocols.protocol1_13to1_12_2;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonParser;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl.NBTLegacyHoverEventSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;

public final class ChatRewriter {
    public static final GsonComponentSerializer HOVER_GSON_SERIALIZER = GsonComponentSerializer.builder().emitLegacyHoverEvent().legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get()).build();

    public static JsonObject emptyComponent() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", "");
        return jsonObject;
    }

    public static String emptyComponentString() {
        return "{\"text\":\"\"}";
    }

    public static String legacyTextToJsonString(String string, boolean bl) {
        Object object = LegacyComponentSerializer.legacySection().deserialize(string);
        if (bl) {
            object = ((TextComponent.Builder)((TextComponent.Builder)Component.text().decoration(TextDecoration.ITALIC, false)).append((Component)object)).build();
        }
        return (String)GsonComponentSerializer.gson().serialize(object);
    }

    public static String legacyTextToJsonString(String string) {
        return ChatRewriter.legacyTextToJsonString(string, false);
    }

    public static JsonElement legacyTextToJson(String string) {
        return JsonParser.parseString(ChatRewriter.legacyTextToJsonString(string, false));
    }

    public static String jsonToLegacyText(String string) {
        try {
            Object o = HOVER_GSON_SERIALIZER.deserialize(string);
            return LegacyComponentSerializer.legacySection().serialize((Component)o);
        } catch (Exception exception) {
            Via.getPlatform().getLogger().warning("Error converting json text to legacy: " + string);
            exception.printStackTrace();
            return "";
        }
    }

    @Deprecated
    public static void processTranslate(JsonElement jsonElement) {
        Via.getManager().getProtocolManager().getProtocol(Protocol1_13To1_12_2.class).getComponentRewriter().processText(jsonElement);
    }
}

