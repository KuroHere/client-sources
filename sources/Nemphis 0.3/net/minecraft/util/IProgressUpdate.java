/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.util;

public interface IProgressUpdate {
    public void displaySavingString(String var1);

    public void resetProgressAndMessage(String var1);

    public void displayLoadingString(String var1);

    public void setLoadingProgress(int var1);

    public void setDoneWorking();
}

