/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.command.server;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanList;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class CommandBanIp
extends CommandBase {
    public static final Pattern field_147211_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final String __OBFID = "CL_00000139";

    @Override
    public String getCommandName() {
        return "ban-ip";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(sender)) {
            return true;
        }
        return false;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.banip.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1 && args[0].length() > 1) {
            IChatComponent var3 = args.length >= 2 ? CommandBanIp.getChatComponentFromNthArg(sender, args, 1) : null;
            Matcher var4 = field_147211_a.matcher(args[0]);
            if (var4.matches()) {
                this.func_147210_a(sender, args[0], var3 == null ? null : var3.getUnformattedText());
            } else {
                EntityPlayerMP var5 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
                if (var5 == null) {
                    throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
                }
                this.func_147210_a(sender, var5.getPlayerIP(), var3 == null ? null : var3.getUnformattedText());
            }
        } else {
            throw new WrongUsageException("commands.banip.usage", new Object[0]);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandBanIp.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    protected void func_147210_a(ICommandSender p_147210_1_, String p_147210_2_, String p_147210_3_) {
        IPBanEntry var4 = new IPBanEntry(p_147210_2_, null, p_147210_1_.getName(), null, p_147210_3_);
        MinecraftServer.getServer().getConfigurationManager().getBannedIPs().addEntry(var4);
        List var5 = MinecraftServer.getServer().getConfigurationManager().getPlayersMatchingAddress(p_147210_2_);
        Object[] var6 = new String[var5.size()];
        int var7 = 0;
        for (EntityPlayerMP var9 : var5) {
            var9.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
            var6[var7++] = var9.getName();
        }
        if (var5.isEmpty()) {
            CommandBanIp.notifyOperators(p_147210_1_, (ICommand)this, "commands.banip.success", p_147210_2_);
        } else {
            CommandBanIp.notifyOperators(p_147210_1_, (ICommand)this, "commands.banip.success.players", p_147210_2_, CommandBanIp.joinNiceString(var6));
        }
    }
}

