package dev.darkmoon.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.darkmoon.client.manager.macro.Macro;
import dev.darkmoon.client.DarkMoon;
import dev.darkmoon.client.command.Command;
import dev.darkmoon.client.command.CommandAbstract;
import dev.darkmoon.client.utility.misc.StringUtility;
import org.lwjgl.input.Keyboard;

@Command(name = "macro", description = "��������� ��������� ������� �� ������� ������")
public class MacroCommand extends CommandAbstract {
    @Override
    public void error() {
        sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
        sendMessage(ChatFormatting.WHITE + "." + "macro add " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "key" + ChatFormatting.GRAY + "> <" + ChatFormatting.RED + "message" +ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + "." + "macro remove " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "key" + ChatFormatting.GRAY + ">");
        sendMessage(ChatFormatting.WHITE + "." + "macro list");
        sendMessage(ChatFormatting.WHITE + "." + "macro clear");
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length > 1) {
            switch (args[1]) {
                case "add":
                    int keyIndex = Keyboard.getKeyIndex(args[2].toUpperCase());
                    StringBuilder sb = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String message = sb.toString().trim();
                    String redMessage = StringUtility.getStringRedColor(message);
                    if (keyIndex == 0) {
                        if (args[2].startsWith("mouse")) {
                            try {
                                int digit = Integer.parseInt(args[2].replaceAll("mouse", ""));
                                if (digit < 100) {
                                    DarkMoon.getInstance().getMacroManager().addMacros(new Macro(message, digit - 100));
                                    sendMessage(ChatFormatting.GRAY + "������� �������� ������ ��� ������ ����" + ChatFormatting.RED + " \""
                                            + digit + ChatFormatting.RED + "\" " + ChatFormatting.GRAY + "� �������� "
                                            + ChatFormatting.RED + redMessage);
                                } else {
                                    sendMessage(ChatFormatting.GRAY + "������ ���� �� ����� ���� 100 � ������!");
                                }
                            } catch (NumberFormatException exception) {
                                error();
                            }
                        } else {
                            sendMessage(ChatFormatting.GRAY + "����� ������� �� ����������!");
                        }
                    } else {
                        DarkMoon.getInstance().getMacroManager().addMacros(new Macro(message, keyIndex));
                        sendMessage(ChatFormatting.GRAY + "������� �������� ������ ��� ������" + ChatFormatting.RED + " \""
                                + args[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.GRAY + "� �������� "
                                + ChatFormatting.RED + redMessage);
                    }
                    break;
                case "clear":
                    if (DarkMoon.getInstance().getMacroManager().getMacros().isEmpty()) {
                        sendMessage(ChatFormatting.GRAY + "������ �������� ����.");
                    } else {
                        sendMessage(ChatFormatting.GRAY + "������ �������� ������� ������!");
                        DarkMoon.getInstance().getMacroManager().getMacros().clear();
                        DarkMoon.getInstance().getMacroManager().updateFile();
                    }
                    break;
                case "remove":
                    int keyIndex2 = Keyboard.getKeyIndex(args[2].toUpperCase());
                    if (keyIndex2 == 0) {
                        if (args[2].startsWith("mouse")) {
                            String digits = StringUtility.getDigits(args[2]);
                            try {
                                int digit = Integer.parseInt(digits);
                                DarkMoon.getInstance().getMacroManager().deleteMacro(digit - 100);
                                sendMessage(ChatFormatting.GRAY + "������ ��� ������ � ������ "
                                        + ChatFormatting.RED + "\"" + args[2] + "\"");
                            } catch (NumberFormatException exception) {
                                error();
                            }
                        } else {
                            sendMessage(ChatFormatting.GRAY + "����� ������� �� ����������!");
                        }
                    } else {
                        DarkMoon.getInstance().getMacroManager().deleteMacro(keyIndex2);
                        sendMessage(ChatFormatting.GRAY + "������ ��� ������ � ������ "
                                + ChatFormatting.RED + "\"" + args[2].toUpperCase() + "\"");
                    }
                    break;
                case "list":
                    if (DarkMoon.getInstance().getMacroManager().getMacros().isEmpty()) {
                        sendMessage(ChatFormatting.GRAY + "������ �������� ����.");
                    } else {
                        sendMessage(ChatFormatting.GREEN + "������ ��������: ");
                        for (Macro macro : DarkMoon.getInstance().getMacroManager().getMacros()) {
                            if (macro.getKey() < 0) {
                                sendMessage(ChatFormatting.WHITE + "�������: " + ChatFormatting.RED
                                        + macro.getMessage() + ChatFormatting.WHITE + ", ������: " + ChatFormatting.RED
                                        + (macro.getKey() + 100));
                            } else {
                                sendMessage(ChatFormatting.WHITE + "�������: " + ChatFormatting.RED
                                        + macro.getMessage() + ChatFormatting.WHITE + ", �������: " + ChatFormatting.RED
                                        + Keyboard.getKeyName(macro.getKey()));
                            }
                        }
                    }
                    break;
            }
        } else {
            error();
        }
    }
}
