/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class UsedTotemTrigger
extends AbstractCriterionTrigger<Instance> {
    private static final ResourceLocation ID = new ResourceLocation("used_totem");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance deserializeTrigger(JsonObject jsonObject, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser conditionArrayParser) {
        ItemPredicate itemPredicate = ItemPredicate.deserialize(jsonObject.get("item"));
        return new Instance(andPredicate, itemPredicate);
    }

    public void trigger(ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        this.triggerListeners(serverPlayerEntity, arg_0 -> UsedTotemTrigger.lambda$trigger$0(itemStack, arg_0));
    }

    @Override
    public CriterionInstance deserializeTrigger(JsonObject jsonObject, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser conditionArrayParser) {
        return this.deserializeTrigger(jsonObject, andPredicate, conditionArrayParser);
    }

    private static boolean lambda$trigger$0(ItemStack itemStack, Instance instance) {
        return instance.test(itemStack);
    }

    public static class Instance
    extends CriterionInstance {
        private final ItemPredicate item;

        public Instance(EntityPredicate.AndPredicate andPredicate, ItemPredicate itemPredicate) {
            super(ID, andPredicate);
            this.item = itemPredicate;
        }

        public static Instance usedTotem(IItemProvider iItemProvider) {
            return new Instance(EntityPredicate.AndPredicate.ANY_AND, ItemPredicate.Builder.create().item(iItemProvider).build());
        }

        public boolean test(ItemStack itemStack) {
            return this.item.test(itemStack);
        }

        @Override
        public JsonObject serialize(ConditionArraySerializer conditionArraySerializer) {
            JsonObject jsonObject = super.serialize(conditionArraySerializer);
            jsonObject.add("item", this.item.serialize());
            return jsonObject;
        }
    }
}

