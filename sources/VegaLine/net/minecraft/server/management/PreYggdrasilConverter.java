/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
    public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");
    public static final File OLD_OPS_FILE = new File("ops.txt");
    public static final File OLD_WHITELIST_FILE = new File("white-list.txt");

    private static void lookupNames(MinecraftServer server, Collection<String> names, ProfileLookupCallback callback) {
        String[] astring = Iterators.toArray(Iterators.filter(names.iterator(), new Predicate<String>(){

            @Override
            public boolean apply(@Nullable String p_apply_1_) {
                return !StringUtils.isNullOrEmpty(p_apply_1_);
            }
        }), String.class);
        if (server.isServerInOnlineMode()) {
            server.getGameProfileRepository().findProfilesByNames(astring, Agent.MINECRAFT, callback);
        } else {
            for (String s : astring) {
                UUID uuid = EntityPlayer.getUUID(new GameProfile(null, s));
                GameProfile gameprofile = new GameProfile(uuid, s);
                callback.onProfileLookupSucceeded(gameprofile);
            }
        }
    }

    public static String convertMobOwnerIfNeeded(final MinecraftServer server, String username) {
        if (!StringUtils.isNullOrEmpty(username) && username.length() <= 16) {
            GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(username);
            if (gameprofile != null && gameprofile.getId() != null) {
                return gameprofile.getId().toString();
            }
            if (!server.isSinglePlayer() && server.isServerInOnlineMode()) {
                final ArrayList list = Lists.newArrayList();
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback(){

                    @Override
                    public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                        server.getPlayerProfileCache().addEntry(p_onProfileLookupSucceeded_1_);
                        list.add(p_onProfileLookupSucceeded_1_);
                    }

                    @Override
                    public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                        LOGGER.warn("Could not lookup user whitelist entry for {}", (Object)p_onProfileLookupFailed_1_.getName(), (Object)p_onProfileLookupFailed_2_);
                    }
                };
                PreYggdrasilConverter.lookupNames(server, Lists.newArrayList(username), profilelookupcallback);
                return !list.isEmpty() && ((GameProfile)list.get(0)).getId() != null ? ((GameProfile)list.get(0)).getId().toString() : "";
            }
            return EntityPlayer.getUUID(new GameProfile(null, username)).toString();
        }
        return username;
    }
}

