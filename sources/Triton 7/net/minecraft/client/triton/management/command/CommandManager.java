package net.minecraft.client.triton.management.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

import net.minecraft.client.triton.impl.commands.OptionCommand;
import net.minecraft.client.triton.impl.commands.UnknownCommand;

public class CommandManager {
	public static List<Command> commandList;
	public static OptionCommand optionCommand;
	public static final UnknownCommand COMMAND_UNKNOWN;

	static {
		CommandManager.commandList = new ArrayList<Command>();
		CommandManager.optionCommand = new OptionCommand();
		COMMAND_UNKNOWN = new UnknownCommand();
	}

	public static void start() {
		try {
			final Reflections reflections = new Reflections("net.minecraft.client.triton.impl.commands", new Scanner[0]);
			final Set<Class<? extends Command>> classes = (Set<Class<? extends Command>>) reflections
					.getSubTypesOf((Class) Command.class);
			for (final Class<? extends Command> clazz : classes) {
				final Command loadedCommand = (Command) clazz.newInstance();
				if (clazz.isAnnotationPresent(Com.class)) {
					final Com comAnnotation = clazz.getAnnotation(Com.class);
					loadedCommand.setNames(comAnnotation.names());
					CommandManager.commandList.add(loadedCommand);
				}
			}
			CommandManager.commandList.add(CommandManager.optionCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Command parseMessage(String message) {
		message = message.replaceFirst(".", "");
		for (final Command command : CommandManager.commandList) {
			if (command.getNames() == null) {
				return new UnknownCommand();
			}
			String[] names;
			for (int length = (names = command.getNames()).length, i = 0; i < length; ++i) {
				final String name = names[i];
				if (message.split(" ")[0].equalsIgnoreCase(name)) {
					return command;
				}
			}
		}
		return CommandManager.COMMAND_UNKNOWN;
	}
}
