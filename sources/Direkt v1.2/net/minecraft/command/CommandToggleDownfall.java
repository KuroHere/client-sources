package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall extends CommandBase {
	/**
	 * Gets the name of the command
	 */
	@Override
	public String getCommandName() {
		return "toggledownfall";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.downfall.usage";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		this.toggleRainfall(server);
		notifyCommandListener(sender, this, "commands.downfall.success", new Object[0]);
	}

	protected void toggleRainfall(MinecraftServer server) {
		WorldInfo worldinfo = server.worldServers[0].getWorldInfo();
		worldinfo.setRaining(!worldinfo.isRaining());
	}
}
