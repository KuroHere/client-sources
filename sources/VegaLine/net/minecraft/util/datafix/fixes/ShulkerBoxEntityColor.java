/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxEntityColor
implements IFixableData {
    @Override
    public int getFixVersion() {
        return 808;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if ("minecraft:shulker".equals(compound.getString("id")) && !compound.hasKey("Color", 99)) {
            compound.setByte("Color", (byte)10);
        }
        return compound;
    }
}

