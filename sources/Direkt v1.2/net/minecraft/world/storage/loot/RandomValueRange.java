package net.minecraft.world.storage.loot;

import java.lang.reflect.Type;
import java.util.Random;

import com.google.gson.*;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class RandomValueRange {
	private final float min;
	private final float max;

	public RandomValueRange(float minIn, float maxIn) {
		this.min = minIn;
		this.max = maxIn;
	}

	public RandomValueRange(float value) {
		this.min = value;
		this.max = value;
	}

	public float getMin() {
		return this.min;
	}

	public float getMax() {
		return this.max;
	}

	public int generateInt(Random rand) {
		return MathHelper.getRandomIntegerInRange(rand, MathHelper.floor_float(this.min), MathHelper.floor_float(this.max));
	}

	public float generateFloat(Random rand) {
		return MathHelper.randomFloatClamp(rand, this.min, this.max);
	}

	public boolean isInRange(int value) {
		return (value <= this.max) && (value >= this.min);
	}

	public static class Serializer implements JsonDeserializer<RandomValueRange>, JsonSerializer<RandomValueRange> {
		@Override
		public RandomValueRange deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
			if (JsonUtils.isNumber(p_deserialize_1_)) {
				return new RandomValueRange(JsonUtils.getFloat(p_deserialize_1_, "value"));
			} else {
				JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "value");
				float f = JsonUtils.getFloat(jsonobject, "min");
				float f1 = JsonUtils.getFloat(jsonobject, "max");
				return new RandomValueRange(f, f1);
			}
		}

		@Override
		public JsonElement serialize(RandomValueRange p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
			if (p_serialize_1_.min == p_serialize_1_.max) {
				return new JsonPrimitive(Float.valueOf(p_serialize_1_.min));
			} else {
				JsonObject jsonobject = new JsonObject();
				jsonobject.addProperty("min", Float.valueOf(p_serialize_1_.min));
				jsonobject.addProperty("max", Float.valueOf(p_serialize_1_.max));
				return jsonobject;
			}
		}
	}
}
