/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class VillagerTradeTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_192237_a = new ResourceLocation("villager_trade");
    private final Map<PlayerAdvancements, Listeners> field_192238_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_192237_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners villagertradetrigger$listeners = this.field_192238_b.get(p_192165_1_);
        if (villagertradetrigger$listeners == null) {
            villagertradetrigger$listeners = new Listeners(p_192165_1_);
            this.field_192238_b.put(p_192165_1_, villagertradetrigger$listeners);
        }
        villagertradetrigger$listeners.func_192540_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners villagertradetrigger$listeners = this.field_192238_b.get(p_192164_1_);
        if (villagertradetrigger$listeners != null) {
            villagertradetrigger$listeners.func_192538_b(p_192164_2_);
            if (villagertradetrigger$listeners.func_192539_a()) {
                this.field_192238_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_192238_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("villager"));
        ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        return new Instance(entitypredicate, itempredicate);
    }

    public void func_192234_a(EntityPlayerMP p_192234_1_, EntityVillager p_192234_2_, ItemStack p_192234_3_) {
        Listeners villagertradetrigger$listeners = this.field_192238_b.get(p_192234_1_.func_192039_O());
        if (villagertradetrigger$listeners != null) {
            villagertradetrigger$listeners.func_192537_a(p_192234_1_, p_192234_2_, p_192234_3_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_192541_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_192542_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47458_1_) {
            this.field_192541_a = p_i47458_1_;
        }

        public boolean func_192539_a() {
            return this.field_192542_b.isEmpty();
        }

        public void func_192540_a(ICriterionTrigger.Listener<Instance> p_192540_1_) {
            this.field_192542_b.add(p_192540_1_);
        }

        public void func_192538_b(ICriterionTrigger.Listener<Instance> p_192538_1_) {
            this.field_192542_b.remove(p_192538_1_);
        }

        public void func_192537_a(EntityPlayerMP p_192537_1_, EntityVillager p_192537_2_, ItemStack p_192537_3_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_192542_b) {
                if (!listener.func_192158_a().func_192285_a(p_192537_1_, p_192537_2_, p_192537_3_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_192541_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final EntityPredicate field_192286_a;
        private final ItemPredicate field_192287_b;

        public Instance(EntityPredicate p_i47457_1_, ItemPredicate p_i47457_2_) {
            super(field_192237_a);
            this.field_192286_a = p_i47457_1_;
            this.field_192287_b = p_i47457_2_;
        }

        public boolean func_192285_a(EntityPlayerMP p_192285_1_, EntityVillager p_192285_2_, ItemStack p_192285_3_) {
            if (!this.field_192286_a.func_192482_a(p_192285_1_, p_192285_2_)) {
                return false;
            }
            return this.field_192287_b.func_192493_a(p_192285_3_);
        }
    }
}

