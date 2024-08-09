package wtf.resolute.command.impl.feature;

import wtf.resolute.command.*;
import wtf.resolute.command.impl.CommandException;
import wtf.resolute.manage.config.Config;
import wtf.resolute.manage.config.ConfigStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.text.TextFormatting;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigCommand implements Command, CommandWithAdvice, MultiNamedCommand {

    final ConfigStorage configStorage;
    final Prefix prefix;
    final Logger logger;


    @Override
    public void execute(Parameters parameters) {
        String commandType = parameters.asString(0).orElse("");

        switch (commandType) {
            case "load" -> loadConfig(parameters);
            case "save" -> saveConfig(parameters);
            case "list" -> configList();
            case "dir" -> getDirectory();
            default ->
                    throw new CommandException(TextFormatting.RED + "������� ��� �������:" + TextFormatting.GRAY + " load, save, list, dir");
        }
    }

    // ... (
    @Override
    public String name() {
        return "config";
    }

    @Override
    public String description() {
        return "��������� ����������������� � ��������� � ����";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = prefix.get();

        return List.of(commandPrefix + name() + " load <config> - ��������� ������",
                commandPrefix + name() + " save <config> - ��������� ������",
                commandPrefix + name() + " list - �������� ������ ��������",
                commandPrefix + name() + " dir - ������� ����� � ���������",
                "������: " + TextFormatting.RED + commandPrefix + "cfg save myConfig",
                "������: " + TextFormatting.RED + commandPrefix + "cfg load myConfig"

        );
    }

    @Override
    public List<String> aliases() {
        return List.of("cfg");
    }

    private void loadConfig(Parameters parameters) {
        String configName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� �������� �������!"));

        if (new File(configStorage.CONFIG_DIR, configName + ".cfg").exists()) {
            configStorage.loadConfiguration(configName);
            logger.log(TextFormatting.GREEN + "������������ " + TextFormatting.RED + configName + TextFormatting.GREEN + " ���������!");

        } else {
            logger.log(TextFormatting.RED + "������������ " + TextFormatting.GRAY + configName + TextFormatting.RED + " �� �������!");
        }
    }

    private void saveConfig(Parameters parameters) {
        String configName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� �������� �������!"));

        configStorage.saveConfiguration(configName);
        logger.log(TextFormatting.GREEN + "������������ " + TextFormatting.RED + configName + TextFormatting.GREEN + " ���������!");

    }

    private void configList() {
        if (configStorage.isEmpty()) {
            logger.log(TextFormatting.RED + "������ ������������ ������");
            return;
        }
        logger.log(TextFormatting.GRAY + "������ ��������:");

        for (Config config : configStorage.getConfigs()) {
            logger.log(TextFormatting.GRAY + config.getName());
        }
    }

    private void getDirectory() {
        try {
            Runtime.getRuntime().exec("explorer " + configStorage.CONFIG_DIR.getAbsolutePath());
        } catch (IOException e) {
            logger.log(TextFormatting.RED + "����� � �������������� �� �������!" + e.getMessage());
        }
    }
}
