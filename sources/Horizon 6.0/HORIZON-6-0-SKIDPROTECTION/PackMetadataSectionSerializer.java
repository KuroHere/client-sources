package HORIZON-6-0-SKIDPROTECTION;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;

public class PackMetadataSectionSerializer extends BaseMetadataSectionSerializer implements JsonSerializer
{
    private static final String HorizonCode_Horizon_È = "CL_00001113";
    
    public PackMetadataSection HorizonCode_Horizon_È(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
        final JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        final IChatComponent var5 = (IChatComponent)p_deserialize_3_.deserialize(var4.get("description"), (Type)IChatComponent.class);
        if (var5 == null) {
            throw new JsonParseException("Invalid/missing description!");
        }
        final int var6 = JsonUtils.áŒŠÆ(var4, "pack_format");
        return new PackMetadataSection(var5, var6);
    }
    
    public JsonElement HorizonCode_Horizon_È(final PackMetadataSection p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
        final JsonObject var4 = new JsonObject();
        var4.addProperty("pack_format", (Number)p_serialize_1_.Â());
        var4.add("description", p_serialize_3_.serialize((Object)p_serialize_1_.HorizonCode_Horizon_È()));
        return (JsonElement)var4;
    }
    
    public String HorizonCode_Horizon_È() {
        return "pack";
    }
    
    public JsonElement serialize(final Object p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
        return this.HorizonCode_Horizon_È((PackMetadataSection)p_serialize_1_, p_serialize_2_, p_serialize_3_);
    }
}
