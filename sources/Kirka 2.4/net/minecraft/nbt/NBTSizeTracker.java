/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.nbt;

public class NBTSizeTracker {
    public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L){
        private static final String __OBFID = "CL_00001902";

        @Override
        public void read(long bits) {
        }
    };
    private final long max;
    private long read;
    private static final String __OBFID = "CL_00001903";

    public NBTSizeTracker(long max) {
        this.max = max;
    }

    public void read(long bits) {
        this.read += bits / 8L;
        if (this.read > this.max) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.read + "bytes where max allowed: " + this.max);
        }
    }

}

