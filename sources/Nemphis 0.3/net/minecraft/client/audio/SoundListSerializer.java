/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.client.audio;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import java.util.List;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundList;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class SoundListSerializer
implements JsonDeserializer {
    private static final String __OBFID = "CL_00001124";

    public SoundList deserialize1(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
        JsonObject var4 = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "entry");
        SoundList var5 = new SoundList();
        var5.setReplaceExisting(JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "replace", false));
        SoundCategory var6 = SoundCategory.func_147154_a(JsonUtils.getJsonObjectStringFieldValueOrDefault(var4, "category", SoundCategory.MASTER.getCategoryName()));
        var5.setSoundCategory(var6);
        Validate.notNull((Object)((Object)var6), (String)"Invalid category", (Object[])new Object[0]);
        if (var4.has("sounds")) {
            JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var4, "sounds");
            int var8 = 0;
            while (var8 < var7.size()) {
                JsonElement var9 = var7.get(var8);
                SoundList.SoundEntry var10 = new SoundList.SoundEntry();
                if (JsonUtils.jsonElementTypeIsString(var9)) {
                    var10.setSoundEntryName(JsonUtils.getJsonElementStringValue(var9, "sound"));
                } else {
                    JsonObject var11 = JsonUtils.getElementAsJsonObject(var9, "sound");
                    var10.setSoundEntryName(JsonUtils.getJsonObjectStringFieldValue(var11, "name"));
                    if (var11.has("type")) {
                        SoundList.SoundEntry.Type var12 = SoundList.SoundEntry.Type.getType(JsonUtils.getJsonObjectStringFieldValue(var11, "type"));
                        Validate.notNull((Object)((Object)var12), (String)"Invalid type", (Object[])new Object[0]);
                        var10.setSoundEntryType(var12);
                    }
                    if (var11.has("volume")) {
                        float var13 = JsonUtils.getJsonObjectFloatFieldValue(var11, "volume");
                        Validate.isTrue((boolean)(var13 > 0.0f), (String)"Invalid volume", (Object[])new Object[0]);
                        var10.setSoundEntryVolume(var13);
                    }
                    if (var11.has("pitch")) {
                        float var13 = JsonUtils.getJsonObjectFloatFieldValue(var11, "pitch");
                        Validate.isTrue((boolean)(var13 > 0.0f), (String)"Invalid pitch", (Object[])new Object[0]);
                        var10.setSoundEntryPitch(var13);
                    }
                    if (var11.has("weight")) {
                        int var14 = JsonUtils.getJsonObjectIntegerFieldValue(var11, "weight");
                        Validate.isTrue((boolean)(var14 > 0), (String)"Invalid weight", (Object[])new Object[0]);
                        var10.setSoundEntryWeight(var14);
                    }
                    if (var11.has("stream")) {
                        var10.setStreaming(JsonUtils.getJsonObjectBooleanFieldValue(var11, "stream"));
                    }
                }
                var5.getSoundList().add(var10);
                ++var8;
            }
        }
        return var5;
    }

    public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
        return this.deserialize1(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
    }
}

