/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ModelBlockDefinition {
    static final Gson field_178333_a = new GsonBuilder().registerTypeAdapter(ModelBlockDefinition.class, (Object)new Deserializer()).registerTypeAdapter(Variant.class, (Object)new Variant.Deserializer()).create();
    private final Map field_178332_b = Maps.newHashMap();
    private static final String __OBFID = "CL_00002498";

    public static ModelBlockDefinition func_178331_a(Reader p_178331_0_) {
        return (ModelBlockDefinition)field_178333_a.fromJson(p_178331_0_, ModelBlockDefinition.class);
    }

    public ModelBlockDefinition(Collection p_i46221_1_) {
        for (Variants var3 : p_i46221_1_) {
            this.field_178332_b.put(var3.field_178423_a, var3);
        }
    }

    public ModelBlockDefinition(List p_i46222_1_) {
        for (ModelBlockDefinition var3 : p_i46222_1_) {
            this.field_178332_b.putAll(var3.field_178332_b);
        }
    }

    public Variants func_178330_b(String p_178330_1_) {
        Variants var2 = (Variants)this.field_178332_b.get(p_178330_1_);
        if (var2 == null) {
            throw new MissingVariantException();
        }
        return var2;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof ModelBlockDefinition) {
            ModelBlockDefinition var2 = (ModelBlockDefinition)p_equals_1_;
            return this.field_178332_b.equals(var2.field_178332_b);
        }
        return false;
    }

    public int hashCode() {
        return this.field_178332_b.hashCode();
    }

    public static class Deserializer
    implements JsonDeserializer {
        private static final String __OBFID = "CL_00002497";

        public ModelBlockDefinition func_178336_a(JsonElement p_178336_1_, Type p_178336_2_, JsonDeserializationContext p_178336_3_) {
            JsonObject var4 = p_178336_1_.getAsJsonObject();
            List var5 = this.func_178334_a(p_178336_3_, var4);
            return new ModelBlockDefinition((Collection)var5);
        }

        protected List func_178334_a(JsonDeserializationContext p_178334_1_, JsonObject p_178334_2_) {
            JsonObject var3 = JsonUtils.getJsonObject(p_178334_2_, "variants");
            ArrayList var4 = Lists.newArrayList();
            for (Map.Entry var6 : var3.entrySet()) {
                var4.add(this.func_178335_a(p_178334_1_, var6));
            }
            return var4;
        }

        protected Variants func_178335_a(JsonDeserializationContext p_178335_1_, Map.Entry p_178335_2_) {
            String var3 = (String)p_178335_2_.getKey();
            ArrayList var4 = Lists.newArrayList();
            JsonElement var5 = (JsonElement)p_178335_2_.getValue();
            if (var5.isJsonArray()) {
                for (JsonElement var7 : var5.getAsJsonArray()) {
                    var4.add((Variant)p_178335_1_.deserialize(var7, Variant.class));
                }
            } else {
                var4.add((Variant)p_178335_1_.deserialize(var5, Variant.class));
            }
            return new Variants(var3, var4);
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            return this.func_178336_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }

    public class MissingVariantException
    extends RuntimeException {
        private static final String __OBFID = "CL_00002496";
    }

    public static class Variant {
        private final ResourceLocation field_178437_a;
        private final ModelRotation field_178435_b;
        private final boolean field_178436_c;
        private final int field_178434_d;
        private static final String __OBFID = "CL_00002495";

        public Variant(ResourceLocation p_i46219_1_, ModelRotation p_i46219_2_, boolean p_i46219_3_, int p_i46219_4_) {
            this.field_178437_a = p_i46219_1_;
            this.field_178435_b = p_i46219_2_;
            this.field_178436_c = p_i46219_3_;
            this.field_178434_d = p_i46219_4_;
        }

        public ResourceLocation getModelLocation() {
            return this.field_178437_a;
        }

        public ModelRotation getRotation() {
            return this.field_178435_b;
        }

        public boolean isUvLocked() {
            return this.field_178436_c;
        }

        public int getWeight() {
            return this.field_178434_d;
        }

        public boolean equals(Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            }
            if (!(p_equals_1_ instanceof Variant)) {
                return false;
            }
            Variant var2 = (Variant)p_equals_1_;
            if (this.field_178437_a.equals(var2.field_178437_a) && this.field_178435_b == var2.field_178435_b && this.field_178436_c == var2.field_178436_c) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int var1 = this.field_178437_a.hashCode();
            var1 = 31 * var1 + (this.field_178435_b != null ? this.field_178435_b.hashCode() : 0);
            var1 = 31 * var1 + (this.field_178436_c ? 1 : 0);
            return var1;
        }

        public static class Deserializer
        implements JsonDeserializer {
            private static final String __OBFID = "CL_00002494";

            public Variant func_178425_a(JsonElement p_178425_1_, Type p_178425_2_, JsonDeserializationContext p_178425_3_) {
                JsonObject var4 = p_178425_1_.getAsJsonObject();
                String var5 = this.func_178424_b(var4);
                ModelRotation var6 = this.func_178428_a(var4);
                boolean var7 = this.func_178429_d(var4);
                int var8 = this.func_178427_c(var4);
                return new Variant(this.func_178426_a(var5), var6, var7, var8);
            }

            private ResourceLocation func_178426_a(String p_178426_1_) {
                ResourceLocation var2 = new ResourceLocation(p_178426_1_);
                var2 = new ResourceLocation(var2.getResourceDomain(), "block/" + var2.getResourcePath());
                return var2;
            }

            private boolean func_178429_d(JsonObject p_178429_1_) {
                return JsonUtils.getJsonObjectBooleanFieldValueOrDefault(p_178429_1_, "uvlock", false);
            }

            protected ModelRotation func_178428_a(JsonObject p_178428_1_) {
                int var3;
                int var2 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178428_1_, "x", 0);
                ModelRotation var4 = ModelRotation.func_177524_a(var2, var3 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178428_1_, "y", 0));
                if (var4 == null) {
                    throw new JsonParseException("Invalid BlockModelRotation x: " + var2 + ", y: " + var3);
                }
                return var4;
            }

            protected String func_178424_b(JsonObject p_178424_1_) {
                return JsonUtils.getJsonObjectStringFieldValue(p_178424_1_, "model");
            }

            protected int func_178427_c(JsonObject p_178427_1_) {
                return JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178427_1_, "weight", 1);
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
                return this.func_178425_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }

    }

    public static class Variants {
        private final String field_178423_a;
        private final List field_178422_b;
        private static final String __OBFID = "CL_00002493";

        public Variants(String p_i46218_1_, List p_i46218_2_) {
            this.field_178423_a = p_i46218_1_;
            this.field_178422_b = p_i46218_2_;
        }

        public List getVariants() {
            return this.field_178422_b;
        }

        public boolean equals(Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            }
            if (!(p_equals_1_ instanceof Variants)) {
                return false;
            }
            Variants var2 = (Variants)p_equals_1_;
            return !this.field_178423_a.equals(var2.field_178423_a) ? false : this.field_178422_b.equals(var2.field_178422_b);
        }

        public int hashCode() {
            int var1 = this.field_178423_a.hashCode();
            var1 = 31 * var1 + this.field_178422_b.hashCode();
            return var1;
        }
    }

}

