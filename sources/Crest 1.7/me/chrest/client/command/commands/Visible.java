// 
// Decompiled by Procyon v0.5.30
// 

package me.chrest.client.command.commands;

import me.chrest.client.module.Module;
import me.chrest.utils.ClientUtils;
import me.chrest.client.module.ModuleManager;
import me.chrest.client.command.Com;
import me.chrest.client.command.Command;

@Com(names = { "visible", "v", "show", "hide" })
public class Visible extends Command
{
    @Override
    public void runCommand(final String[] args) {
        String modName = "";
        if (args.length > 1) {
            modName = args[1];
        }
        final Module module = ModuleManager.getModule(modName);
        if (module.getId().equalsIgnoreCase("null")) {
            ClientUtils.sendMessage("Invalid Module.");
            return;
        }
        module.setShown(!module.isShown());
        ClientUtils.sendMessage(String.valueOf(String.valueOf(module.getDisplayName())) + " is now " + (module.isEnabled() ? "shown" : "hidden"));
        ModuleManager.save();
    }
    
    @Override
    public String getHelp() {
        return "Visible - visible <v, show, hide> (module) - Shows or hides a module on the arraylist.";
    }
}
