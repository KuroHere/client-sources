package dev.darkmoon.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.darkmoon.client.DarkMoon;
import dev.darkmoon.client.command.Command;
import dev.darkmoon.client.command.CommandAbstract;
import dev.darkmoon.client.command.CommandManager;
import net.minecraft.client.Minecraft;

@Command(name = "friend", description = "��������� ��������� ������� ������")
public class FriendCommand extends CommandAbstract {
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������:");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend add " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + "> - �������� ������ � ������");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend remove " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + "> - ������� ������ �� ������");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend list" + ChatFormatting.GRAY + " - ���������� ������ ������");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend clear" + ChatFormatting.GRAY + " - �������� ������ ������");
    }

    @Override
    public void execute(String[] args) throws Exception {
        switch (args[1]) {
            case "add":
                if (args[2].equalsIgnoreCase(Minecraft.getMinecraft().getSession().getUsername())) {
                    sendMessage(ChatFormatting.GRAY + "������ �������� ������ ���� � ������ :)");
                } else {
                    if (DarkMoon.getInstance().getFriendManager().getFriends().contains(args[2])) {
                        sendMessage(ChatFormatting.GRAY + "����� " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " ��� ���� � ������ ������.");
                    } else {
                        DarkMoon.getInstance().getFriendManager().addFriend(args[2]);
                        sendMessage(ChatFormatting.GRAY + "����� " + ChatFormatting.RED + args[2]
                                + ChatFormatting.GRAY + " ������� �������� � ������!");
                    }
                }
                break;
            case "remove":
                if (DarkMoon.getInstance().getFriendManager().isFriend(args[2])) {
                    DarkMoon.getInstance().getFriendManager().removeFriend(args[2]);
                    sendMessage(ChatFormatting.GRAY + "����� " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " ������� ������ �� ������ ������.");
                } else {
                    sendMessage(ChatFormatting.GRAY + "������ " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " ��� � ������ ������.");
                }
                break;
            case "clear":
                if (DarkMoon.getInstance().getFriendManager().getFriends().isEmpty()) {
                    sendMessage(ChatFormatting.GRAY + "������ ������ ���� :(");
                } else {
                    DarkMoon.getInstance().getFriendManager().clearFriend();
                    sendMessage(ChatFormatting.GRAY + "������ ������ ������� ������!");
                }
                break;
            case "list":
                if (DarkMoon.getInstance().getFriendManager().getFriends().isEmpty()) {
                    sendMessage(ChatFormatting.GRAY + "������ ������ ���� :(");
                    return;
                }
                sendMessage(ChatFormatting.GRAY + "������ ������: ");
                DarkMoon.getInstance().getFriendManager().getFriends().forEach(friend -> sendMessage(friend.getName()));
                break;
        }
    }
}
