package dev.darkmoon.client.command.impl;

import dev.darkmoon.client.DarkMoon;
import dev.darkmoon.client.command.Command;
import dev.darkmoon.client.command.CommandAbstract;
import dev.darkmoon.client.command.CommandManager;
import dev.darkmoon.client.module.impl.render.StaffList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

@Command(name = "staff", description = "��������� �������� ������� � StaffList")
public class StaffCommand extends CommandAbstract {
    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "������ � �������������:");
        sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff add " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "name" + TextFormatting.GRAY + "> - �������� ������ � StaffList");
        sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff remove " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "name" + TextFormatting.GRAY + "> - ������� ������ �� StaffList");
        sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff list" + TextFormatting.GRAY + " - ���������� StaffList");
        sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff clear" + TextFormatting.GRAY + " - �������� StaffList");
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length > 1) {
            switch (args[1]) {
                case "add":
                    if (args[2].equalsIgnoreCase(Minecraft.getMinecraft().getSession().getUsername())) {
                        sendMessage(TextFormatting.GRAY + "������ �������� ������ ���� � StaffList");
                    } else {
                        if (DarkMoon.getInstance().getStaffManager().getStaff().contains(args[2])) {
                            sendMessage(TextFormatting.GRAY + "����� " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " ��� ���� � StaffList.");
                        } else {
                            DarkMoon.getInstance().getStaffManager().addStaff(args[2]);
                            sendMessage(TextFormatting.GRAY + "����� " + TextFormatting.RED + args[2]
                                    + TextFormatting.GRAY + " ������� �������� � StaffList!");
                            StaffList.updateList();
                        }
                    }
                    break;
                case "remove":
                    if (DarkMoon.getInstance().getStaffManager().isStaff(args[2])) {
                        DarkMoon.getInstance().getStaffManager().removeStaff(args[2]);
                        sendMessage(TextFormatting.GRAY + "����� " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " ������� ������ �� StaffList.");
                        StaffList.updateList();
                    } else {
                        sendMessage(TextFormatting.GRAY + "������ " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " ��� � StaffList.");
                    }
                    break;
                case "clear":
                    if (DarkMoon.getInstance().getStaffManager().getStaff().isEmpty()) {
                        sendMessage(TextFormatting.GRAY + "StaffList ����!");
                    } else {
                        DarkMoon.getInstance().getStaffManager().clearStaff();
                        sendMessage(TextFormatting.GRAY + "StaffList ������!");
                        StaffList.updateList();
                    }
                    break;
                case "list":
                    if (DarkMoon.getInstance().getStaffManager().getStaff().isEmpty()) {
                        sendMessage(TextFormatting.GRAY + "StaffList ����!");
                        return;
                    }
                    sendMessage(TextFormatting.GRAY + "Staff: ");
                    DarkMoon.getInstance().getStaffManager().getStaff().forEach(this::sendMessage);
                    break;
                default: {
                    error();
                    break;
                }
            }
        } else {
            error();
        }
    }
}
