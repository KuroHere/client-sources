/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ListenableFuture
 */
package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public interface IThreadListener {
    public ListenableFuture addScheduledTask(Runnable var1);

    public boolean isCallingFromMinecraftThread();
}

