package wtf.resolute.command.impl.feature;

import wtf.resolute.ResoluteInfo;
import wtf.resolute.command.*;
import wtf.resolute.command.impl.CommandException;
import wtf.resolute.moduled.Module;
import wtf.resolute.utiled.client.KeyStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BindCommand implements Command, CommandWithAdvice {

    final Prefix prefix;
    final Logger logger;

    @Override
    public void execute(Parameters parameters) {
        String commandType = parameters.asString(0).orElse("");

        switch (commandType) {
            case "add" -> addBindToFunction(parameters, logger);
            case "remove" -> removeBindFromFunction(parameters, logger);
            case "clear" -> clearAllBindings(logger);
            case "list" -> listBoundKeys(logger);
            default ->
                    throw new CommandException(TextFormatting.RED + "������� ��� �������:" + TextFormatting.GRAY + " add, remove, clear, list");
        }
    }

    @Override
    public String name() {
        return "bind";
    }

    @Override
    public String description() {
        return "��������� ��������� ������� �� ������������ �������";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = prefix.get();
        return List.of(commandPrefix + "bind add <function> <key> - �������� ����� ����",
                commandPrefix + "bind remove <function> <key> - ������� ����",
                commandPrefix + "bind list - �������� ������ ������",
                commandPrefix + "bind clear - �������� ������ ������",
                "������: " + TextFormatting.RED + commandPrefix + "bind add KillAura R"
        );
    }

    private void addBindToFunction(Parameters parameters, Logger logger) {
        String functionName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� �������� �������!"));
        String keyName = parameters.asString(2)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� ������!"));

        Module function = null;

        for (Module func : ResoluteInfo.getInstance().getFunctionRegistry().getFunctions()) {
            if (func.getName().toLowerCase(Locale.ROOT).equals(functionName.toLowerCase(Locale.ROOT))) {
                function = func;
                break;
            }
        }

        Integer key = KeyStorage.getKey(keyName.toUpperCase());

        if (function == null) {
            logger.log(TextFormatting.RED + "������� " + functionName + " �� ���� �������");
            return;
        }

        if (key == null) {
            logger.log(TextFormatting.RED + "������� " + keyName + " �� ���� �������");
            return;
        }

        function.setBind(key);
        logger.log(TextFormatting.GREEN + "���� " + TextFormatting.RED
                + keyName.toUpperCase() + TextFormatting.GREEN
                + " ��� ���������� ��� ������� " + TextFormatting.RED + functionName);
    }

    private void removeBindFromFunction(Parameters parameters, Logger logger) {
        String functionName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� �������� �������!"));
        String keyName = parameters.asString(2)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� ������!"));

        ResoluteInfo.getInstance().getFunctionRegistry().getFunctions().stream()
                .filter(f -> f.getName().equalsIgnoreCase(functionName))
                .forEach(f -> {
                    f.setBind(0);
                    logger.log(TextFormatting.GREEN + "������� " + TextFormatting.RED + keyName.toUpperCase()
                            + TextFormatting.GREEN + " ���� �������� �� ������� " + TextFormatting.RED + f.getName());
                });
    }

    private void clearAllBindings(Logger logger) {
        ResoluteInfo.getInstance().getFunctionRegistry().getFunctions().forEach(function -> function.setBind(0));
        logger.log(TextFormatting.GREEN + "��� ������� ���� �������� �� �������");
    }

    private void listBoundKeys(Logger logger) {
        logger.log(TextFormatting.GRAY + "������ ���� ������� � ������������ ���������:");

        ResoluteInfo.getInstance().getFunctionRegistry().getFunctions().stream()
                .filter(f -> f.getBind() != 0)
                .map(f -> {
                    String keyName = GLFW.glfwGetKeyName(f.getBind(), -1);
                    keyName = keyName != null ? keyName : "";
                    return String.format("%s [%s%s%s]", f.getName(), TextFormatting.GRAY, keyName, TextFormatting.WHITE);
                })
                .forEach(logger::log);
    }

}
