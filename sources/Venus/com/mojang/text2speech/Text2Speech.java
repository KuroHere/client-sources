/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mojang.text2speech;

import com.mojang.text2speech.Narrator;

public class Text2Speech {
    public static void main(String[] stringArray) {
        System.setProperty("jna.library.path", "./src/natives/resources/");
        Narrator narrator = Narrator.getNarrator();
        narrator.say("This is a test", false);
        while (true) {
            try {
                while (true) {
                    Thread.sleep(100L);
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
                continue;
            }
            break;
        }
    }
}

