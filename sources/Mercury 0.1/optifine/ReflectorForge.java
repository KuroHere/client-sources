/*
 * Decompiled with CFR 0.145.
 */
package optifine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import optifine.Reflector;
import optifine.ReflectorField;
import optifine.ReflectorMethod;

public class ReflectorForge {
    public static void FMLClientHandler_trackBrokenTexture(ResourceLocation loc, String message) {
        if (!Reflector.FMLClientHandler_trackBrokenTexture.exists()) {
            Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackBrokenTexture, loc, message);
        }
    }

    public static void FMLClientHandler_trackMissingTexture(ResourceLocation loc) {
        if (!Reflector.FMLClientHandler_trackMissingTexture.exists()) {
            Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            Reflector.call(instance, Reflector.FMLClientHandler_trackMissingTexture, loc);
        }
    }

    public static void putLaunchBlackboard(String key, Object value) {
        Map blackboard = (Map)Reflector.getFieldValue(Reflector.Launch_blackboard);
        if (blackboard != null) {
            blackboard.put(key, value);
        }
    }

    public static InputStream getOptiFineResourceStream(String path) {
        byte[] bytes;
        if (!Reflector.OptiFineClassTransformer_instance.exists()) {
            return null;
        }
        Object instance = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);
        if (instance == null) {
            return null;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if ((bytes = (byte[])Reflector.call(instance, Reflector.OptiFineClassTransformer_getOptiFineResource, path)) == null) {
            return null;
        }
        ByteArrayInputStream in2 = new ByteArrayInputStream(bytes);
        return in2;
    }

    public static boolean blockHasTileEntity(IBlockState state) {
        Block block = state.getBlock();
        return !Reflector.ForgeBlock_hasTileEntity.exists() ? block.hasTileEntity() : Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, state);
    }

    public static boolean isItemDamaged(ItemStack stack) {
        return !Reflector.ForgeItem_showDurabilityBar.exists() ? stack.isItemDamaged() : Reflector.callBoolean(stack.getItem(), Reflector.ForgeItem_showDurabilityBar, stack);
    }
}

