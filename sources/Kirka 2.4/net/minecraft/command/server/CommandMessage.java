/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandMessage
extends CommandBase {
    private static final String __OBFID = "CL_00000641";

    @Override
    public List getCommandAliases() {
        return Arrays.asList("w", "msg");
    }

    @Override
    public String getCommandName() {
        return "tell";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.message.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        EntityPlayerMP var3 = CommandMessage.getPlayer(sender, args[0]);
        if (var3 == sender) {
            throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
        }
        IChatComponent var4 = CommandMessage.getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
        ChatComponentTranslation var5 = new ChatComponentTranslation("commands.message.display.incoming", sender.getDisplayName(), var4.createCopy());
        ChatComponentTranslation var6 = new ChatComponentTranslation("commands.message.display.outgoing", var3.getDisplayName(), var4.createCopy());
        var5.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        var6.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        var3.addChatMessage(var5);
        sender.addChatMessage(var6);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandMessage.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

