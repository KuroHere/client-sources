/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.client.audio;

import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundPoolEntry;

public class SoundEventAccessor
implements ISoundEventAccessor {
    private final SoundPoolEntry entry;
    private final int weight;
    private static final String __OBFID = "CL_00001153";

    SoundEventAccessor(SoundPoolEntry entry, int weight) {
        this.entry = entry;
        this.weight = weight;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public SoundPoolEntry cloneEntry() {
        return new SoundPoolEntry(this.entry);
    }

    public Object cloneEntry1() {
        return this.cloneEntry();
    }
}

