/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

public class LocationPredicate {
    public static LocationPredicate field_193455_a = new LocationPredicate(MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, null, null, null);
    private final MinMaxBounds field_193457_c;
    private final MinMaxBounds field_193458_d;
    private final MinMaxBounds field_193459_e;
    @Nullable
    final Biome field_193456_b;
    @Nullable
    private final String field_193460_f;
    @Nullable
    private final DimensionType field_193461_g;

    public LocationPredicate(MinMaxBounds p_i47539_1_, MinMaxBounds p_i47539_2_, MinMaxBounds p_i47539_3_, @Nullable Biome p_i47539_4_, @Nullable String p_i47539_5_, @Nullable DimensionType p_i47539_6_) {
        this.field_193457_c = p_i47539_1_;
        this.field_193458_d = p_i47539_2_;
        this.field_193459_e = p_i47539_3_;
        this.field_193456_b = p_i47539_4_;
        this.field_193460_f = p_i47539_5_;
        this.field_193461_g = p_i47539_6_;
    }

    public boolean func_193452_a(WorldServer p_193452_1_, double p_193452_2_, double p_193452_4_, double p_193452_6_) {
        return this.func_193453_a(p_193452_1_, (float)p_193452_2_, (float)p_193452_4_, (float)p_193452_6_);
    }

    public boolean func_193453_a(WorldServer p_193453_1_, float p_193453_2_, float p_193453_3_, float p_193453_4_) {
        if (!this.field_193457_c.func_192514_a(p_193453_2_)) {
            return false;
        }
        if (!this.field_193458_d.func_192514_a(p_193453_3_)) {
            return false;
        }
        if (!this.field_193459_e.func_192514_a(p_193453_4_)) {
            return false;
        }
        if (this.field_193461_g != null && this.field_193461_g != p_193453_1_.provider.getDimensionType()) {
            return false;
        }
        BlockPos blockpos = new BlockPos(p_193453_2_, p_193453_3_, p_193453_4_);
        if (this.field_193456_b != null && this.field_193456_b != p_193453_1_.getBiome(blockpos)) {
            return false;
        }
        return this.field_193460_f == null || p_193453_1_.getChunkProvider().func_193413_a(p_193453_1_, this.field_193460_f, blockpos);
    }

    public static LocationPredicate func_193454_a(@Nullable JsonElement p_193454_0_) {
        if (p_193454_0_ != null && !p_193454_0_.isJsonNull()) {
            ResourceLocation resourcelocation;
            JsonObject jsonobject = JsonUtils.getJsonObject(p_193454_0_, "location");
            JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "position", new JsonObject());
            MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject1.get("x"));
            MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(jsonobject1.get("y"));
            MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject1.get("z"));
            DimensionType dimensiontype = jsonobject.has("dimension") ? DimensionType.func_193417_a(JsonUtils.getString(jsonobject, "dimension")) : null;
            String s = jsonobject.has("feature") ? JsonUtils.getString(jsonobject, "feature") : null;
            Biome biome = null;
            if (jsonobject.has("biome") && (biome = Biome.REGISTRY.getObject(resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "biome")))) == null) {
                throw new JsonSyntaxException("Unknown biome '" + resourcelocation + "'");
            }
            return new LocationPredicate(minmaxbounds, minmaxbounds1, minmaxbounds2, biome, s, dimensiontype);
        }
        return field_193455_a;
    }
}

