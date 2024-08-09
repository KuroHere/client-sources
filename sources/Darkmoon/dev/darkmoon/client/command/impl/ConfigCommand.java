package dev.darkmoon.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.darkmoon.client.manager.config.ConfigManager;
import dev.darkmoon.client.DarkMoon;
import dev.darkmoon.client.command.Command;
import dev.darkmoon.client.command.CommandAbstract;
import dev.darkmoon.client.command.CommandManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

import java.io.File;
import java.util.Objects;

@Command(name = "cfg", description = "��������� ��������� ��������� ����")
public class ConfigCommand extends CommandAbstract {
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg load " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg save " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg delete " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "name" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg list" + ChatFormatting.GRAY
                + " - ������ ��������");
        sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg dir" + ChatFormatting.GRAY
                + " - ������� ����� � ���������");
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length >= 2) {
            switch (args[1]) {
                case "dir":
                    OpenGlHelper.openFile(new File(System.getenv("SystemDrive") + "\\DarkMoon\\configs"));
                    break;
                case "list":
                    File file = new File(System.getenv("SystemDrive") + "\\DarkMoon\\configs");
                    if (ConfigManager.getLoadedConfigs().isEmpty()) {
                        sendMessage("������������ �� �������.");
                    } else {
                        sendMessage(ChatFormatting.GREEN + "������ ������������: ");
                        for (File s : Objects.requireNonNull(file.listFiles())) {
                            sendMessage(s.getName().replaceAll(".dm", ""));
                        }
                    }
                    break;
                case "load":
                    if (DarkMoon.getInstance().getConfigManager().loadConfig(args[2])) {
                        sendMessage(ChatFormatting.GRAY + "������������ " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " ���� ���������.");
                    } else {
                        sendMessage(ChatFormatting.GRAY + "������������ " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " �� ���� ���������. ������ �����, ��� �� ����������.");
                    }
                    break;
                case "save":
                    DarkMoon.getInstance().getConfigManager().saveConfig(args[2]);
                    sendMessage(ChatFormatting.GRAY + "������������ " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " ���� ���������.");
                    break;
                case "delete":
                    if (DarkMoon.getInstance().getConfigManager().deleteConfig(args[2])) {
                        sendMessage(ChatFormatting.GRAY + "������������ " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " ���� �������.");
                    } else {
                        sendMessage(ChatFormatting.GRAY + "������������ " + ChatFormatting.RED + args[2] + ChatFormatting.GRAY + " �� ���� �������. ������ �����, ��� �� ����������.");
                    }
                    break;
            }
        } else {
            error();
        }
    }
}
