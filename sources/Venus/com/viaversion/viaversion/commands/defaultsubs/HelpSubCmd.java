/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.commands.defaultsubs;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class HelpSubCmd
extends ViaSubCommand {
    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "You are looking at it right now!";
    }

    @Override
    public boolean execute(ViaCommandSender viaCommandSender, String[] stringArray) {
        Via.getManager().getCommandHandler().showHelp(viaCommandSender);
        return false;
    }
}

