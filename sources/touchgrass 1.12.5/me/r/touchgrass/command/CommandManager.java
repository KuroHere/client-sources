package me.r.touchgrass.command;

import me.r.touchgrass.command.commands.BindCommand;
import me.r.touchgrass.command.commands.EasterEggCommand;
import me.r.touchgrass.command.commands.HelpCommand;
import me.r.touchgrass.command.commands.ToggleCommand;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by r on 13/03/2021
 */
public class CommandManager {

    public NetHandlerPlayClient sendQueue;
    private final static CommandManager me = new CommandManager();
    private final List<Command> commands = new ArrayList();
    private final String prefix = ".";

    public CommandManager() {
        add(new BindCommand());
        add(new HelpCommand());
        add(new ToggleCommand());
        add(new EasterEggCommand());
    }

    public void add(Command command) {
        this.commands.add(command);
    }

    public static CommandManager get() {
        return me;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public boolean execute(String text) {
        if (!text.startsWith(prefix)) {
            return false;
        }

        text = text.substring(1);

        String[] arguments = text.split(" ");
        String ranCmd = arguments[0];
        for (Command cmd : this.commands) {
            if (cmd.getName().equalsIgnoreCase(arguments[0])) {
                String[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
                String[] args1 = text.split(" ");
                cmd.execute(args);
                return true;
            }
        }
        Command.msg("The command \"§9" + ranCmd + "§7\" has not been found!");

        return false;
    }
}
