/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.client.tutorial;

import net.minecraft.client.tutorial.ITutorialStep;
import net.minecraft.client.tutorial.Tutorial;

public class CompletedTutorialStep
implements ITutorialStep {
    private final Tutorial tutorial;

    public CompletedTutorialStep(Tutorial tutorial) {
        this.tutorial = tutorial;
    }
}

