/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Arrays;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public class AdvancementRewards {
    public static final AdvancementRewards field_192114_a = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.field_193519_a);
    private final int field_192115_b;
    private final ResourceLocation[] field_192116_c;
    private final ResourceLocation[] field_192117_d;
    private final FunctionObject.CacheableFunction field_193129_e;

    public AdvancementRewards(int p_i47587_1_, ResourceLocation[] p_i47587_2_, ResourceLocation[] p_i47587_3_, FunctionObject.CacheableFunction p_i47587_4_) {
        this.field_192115_b = p_i47587_1_;
        this.field_192116_c = p_i47587_2_;
        this.field_192117_d = p_i47587_3_;
        this.field_193129_e = p_i47587_4_;
    }

    public void func_192113_a(final EntityPlayerMP p_192113_1_) {
        MinecraftServer minecraftserver;
        FunctionObject functionobject;
        p_192113_1_.addExperience(this.field_192115_b);
        LootContext lootcontext = new LootContext.Builder(p_192113_1_.getServerWorld()).withLootedEntity(p_192113_1_).build();
        boolean flag = false;
        for (ResourceLocation resourcelocation : this.field_192116_c) {
            for (ItemStack itemstack : p_192113_1_.world.getLootTableManager().getLootTableFromLocation(resourcelocation).generateLootForPools(p_192113_1_.getRNG(), lootcontext)) {
                if (p_192113_1_.func_191521_c(itemstack)) {
                    p_192113_1_.world.playSound(null, p_192113_1_.posX, p_192113_1_.posY, p_192113_1_.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((p_192113_1_.getRNG().nextFloat() - p_192113_1_.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    flag = true;
                    continue;
                }
                EntityItem entityitem = p_192113_1_.dropItem(itemstack, false);
                if (entityitem == null) continue;
                entityitem.setNoPickupDelay();
                entityitem.setOwner(p_192113_1_.getName());
            }
        }
        if (flag) {
            p_192113_1_.inventoryContainer.detectAndSendChanges();
        }
        if (this.field_192117_d.length > 0) {
            p_192113_1_.func_193102_a(this.field_192117_d);
        }
        if ((functionobject = this.field_193129_e.func_193518_a((minecraftserver = p_192113_1_.mcServer).func_193030_aL())) != null) {
            ICommandSender icommandsender = new ICommandSender(){

                @Override
                public String getName() {
                    return p_192113_1_.getName();
                }

                @Override
                public ITextComponent getDisplayName() {
                    return p_192113_1_.getDisplayName();
                }

                @Override
                public void addChatMessage(ITextComponent component) {
                }

                @Override
                public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
                    return permLevel <= 2;
                }

                @Override
                public BlockPos getPosition() {
                    return p_192113_1_.getPosition();
                }

                @Override
                public Vec3d getPositionVector() {
                    return p_192113_1_.getPositionVector();
                }

                @Override
                public World getEntityWorld() {
                    return p_192113_1_.world;
                }

                @Override
                public Entity getCommandSenderEntity() {
                    return p_192113_1_;
                }

                @Override
                public boolean sendCommandFeedback() {
                    return minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
                }

                @Override
                public void setCommandStat(CommandResultStats.Type type2, int amount) {
                    p_192113_1_.setCommandStat(type2, amount);
                }

                @Override
                public MinecraftServer getServer() {
                    return p_192113_1_.getServer();
                }
            };
            minecraftserver.func_193030_aL().func_194019_a(functionobject, icommandsender);
        }
    }

    public String toString() {
        return "AdvancementRewards{experience=" + this.field_192115_b + ", loot=" + Arrays.toString(this.field_192116_c) + ", recipes=" + Arrays.toString(this.field_192117_d) + ", function=" + this.field_193129_e + "}";
    }

    public static class Deserializer
    implements JsonDeserializer<AdvancementRewards> {
        @Override
        public AdvancementRewards deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "rewards");
            int i = JsonUtils.getInt(jsonobject, "experience", 0);
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "loot", new JsonArray());
            ResourceLocation[] aresourcelocation = new ResourceLocation[jsonarray.size()];
            for (int j = 0; j < aresourcelocation.length; ++j) {
                aresourcelocation[j] = new ResourceLocation(JsonUtils.getString(jsonarray.get(j), "loot[" + j + "]"));
            }
            JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "recipes", new JsonArray());
            ResourceLocation[] aresourcelocation1 = new ResourceLocation[jsonarray1.size()];
            for (int k = 0; k < aresourcelocation1.length; ++k) {
                aresourcelocation1[k] = new ResourceLocation(JsonUtils.getString(jsonarray1.get(k), "recipes[" + k + "]"));
                IRecipe irecipe = CraftingManager.func_193373_a(aresourcelocation1[k]);
                if (irecipe != null) continue;
                throw new JsonSyntaxException("Unknown recipe '" + aresourcelocation1[k] + "'");
            }
            FunctionObject.CacheableFunction functionobject$cacheablefunction = jsonobject.has("function") ? new FunctionObject.CacheableFunction(new ResourceLocation(JsonUtils.getString(jsonobject, "function"))) : FunctionObject.CacheableFunction.field_193519_a;
            return new AdvancementRewards(i, aresourcelocation, aresourcelocation1, functionobject$cacheablefunction);
        }
    }
}

