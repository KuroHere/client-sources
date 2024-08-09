/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.commands.defaultsubs;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;

public class AutoTeamSubCmd
extends ViaSubCommand {
    @Override
    public String name() {
        return "autoteam";
    }

    @Override
    public String description() {
        return "Toggle automatically teaming to prevent colliding.";
    }

    @Override
    public boolean execute(ViaCommandSender viaCommandSender, String[] stringArray) {
        ConfigurationProvider configurationProvider = Via.getPlatform().getConfigurationProvider();
        boolean bl = !Via.getConfig().isAutoTeam();
        configurationProvider.set("auto-team", bl);
        configurationProvider.saveConfig();
        AutoTeamSubCmd.sendMessage(viaCommandSender, "&6We will %s", bl ? "&aautomatically team players" : "&cno longer auto team players");
        AutoTeamSubCmd.sendMessage(viaCommandSender, "&6All players will need to re-login for the change to take place.", new Object[0]);
        return false;
    }
}

