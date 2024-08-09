package wtf.resolute.command.impl.feature;

import wtf.resolute.ResoluteInfo;

import wtf.resolute.command.*;
import wtf.resolute.command.impl.CommandException;
import wtf.resolute.manage.MacroManager;
import wtf.resolute.utiled.client.KeyStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MacroCommand implements Command, MultiNamedCommand, CommandWithAdvice {

    final MacroManager macroManager;
    final Prefix prefix;
    final Logger logger;

    @Override
    public void execute(Parameters parameters) {
        String commandType = parameters.asString(0).orElseThrow();
        switch (commandType) {
            case "add" -> addMacro(parameters);
            case "remove" -> removeMacro(parameters);
            case "clear" -> clearMacros();
            case "list" -> printMacrosList();
            default ->
                    throw new CommandException(TextFormatting.RED + "������� ��� �������:" + TextFormatting.GRAY + " add, remove, clear, list");
        }
    }

    @Override
    public String name() {
        return "macro";
    }

    @Override
    public String description() {
        return "��������� ��������� ���������";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = prefix.get();
        return List.of(commandPrefix + "macro add <name> <key> <message> - �������� ����� ������",
                commandPrefix + "macro remove <name> - ������� ������",
                commandPrefix + "macro list - �������� ������ ��������",
                commandPrefix + "macro clear - �������� ������ ��������",
                "������: " + TextFormatting.RED + commandPrefix + "macro add home H /home home"
        );
    }

    @Override
    public List<String> aliases() {
        return List.of("macros");
    }

    private void addMacro(Parameters parameters) {
        String macroName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.GRAY + "������� �������� �������."));
        String macroKey = parameters.asString(2)
                .orElseThrow(() -> new CommandException(TextFormatting.GRAY + "������� ������ ��� ������� ������� ��������� ������."));

        String macroMessage = parameters.collectMessage(3);

        if (macroMessage.isEmpty()) {
            throw new CommandException(TextFormatting.RED + "������� ���������, ������� ����� ������ ������.");
        }
        Integer key = KeyStorage.getKey(macroKey.toUpperCase());

        if (key == null) {
            logger.log("������� " + macroKey + " �� �������!");
            return;
        }

        checkMacroExist(macroName);

        macroManager.addMacro(macroName, macroMessage, key);

        logger.log(TextFormatting.GREEN +
                "�������� ������ � ��������� " + TextFormatting.RED
                + macroName + TextFormatting.GREEN
                + " � ������� " + TextFormatting.RED
                + macroKey + TextFormatting.GREEN
                + " � �������� " + TextFormatting.RED
                + macroMessage);
    }

    private void removeMacro(Parameters parameters) {
        String macroName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.GRAY + "������� �������� �������."));

        ResoluteInfo.getInstance().getMacroManager().deleteMacro(macroName);

        logger.log(TextFormatting.GREEN + "������ " + TextFormatting.RED + macroName + TextFormatting.GREEN + " ��� ������� ������!");
    }

    private void clearMacros() {
        ResoluteInfo.getInstance().getMacroManager().clearList();
        logger.log(TextFormatting.GREEN + "��� ������� ���� �������.");
    }

    private void printMacrosList() {
        if (ResoluteInfo.getInstance().getMacroManager().isEmpty()) {
            logger.log(TextFormatting.RED + "������ ������");
            return;
        }
        ResoluteInfo.getInstance().getMacroManager().macroList
                .forEach(macro -> logger.log(TextFormatting.WHITE + "��������: " + TextFormatting.GRAY
                        + macro.getName() + TextFormatting.WHITE + ", �������: " + TextFormatting.GRAY
                        + macro.getMessage() + TextFormatting.WHITE + ", ������: " + TextFormatting.GRAY
                        + macro.getKey()));
    }

    private void checkMacroExist(String macroName) {
        if (macroManager.hasMacro(macroName)) {
            throw new CommandException(TextFormatting.RED + "������ � ����� ������ ��� ���� � ������!");
        }
    }
}
