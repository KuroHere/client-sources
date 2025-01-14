/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.src;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.src.IFileDownloadListener;
import net.minecraft.src.PlayerConfiguration;
import net.minecraft.src.PlayerConfigurationParser;
import net.minecraft.src.PlayerConfigurations;

public class PlayerConfigurationReceiver
implements IFileDownloadListener {
    private String player = null;

    public PlayerConfigurationReceiver(String player) {
        this.player = player;
    }

    @Override
    public void fileDownloadFinished(String url, byte[] bytes, Throwable exception) {
        if (bytes != null) {
            try {
                String e = new String(bytes, "ASCII");
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(e);
                PlayerConfigurationParser pcp = new PlayerConfigurationParser(this.player);
                PlayerConfiguration pc = pcp.parsePlayerConfiguration(je);
                if (pc != null) {
                    pc.setInitialized(true);
                    PlayerConfigurations.setPlayerConfiguration(this.player, pc);
                }
            }
            catch (Exception var9) {
                var9.printStackTrace();
            }
        }
    }
}

