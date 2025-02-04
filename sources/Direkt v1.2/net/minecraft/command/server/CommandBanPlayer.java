package net.minecraft.command.server;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.math.BlockPos;

public class CommandBanPlayer extends CommandBase {
	/**
	 * Gets the name of the command
	 */
	@Override
	public String getCommandName() {
		return "ban";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.ban.usage";
	}

	/**
	 * Check if the given ICommandSender has permission to execute this command
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender);
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if ((args.length >= 1) && (args[0].length() > 0)) {
			GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);

			if (gameprofile == null) {
				throw new CommandException("commands.ban.failed", new Object[] { args[0] });
			} else {
				String s = null;

				if (args.length >= 2) {
					s = getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
				}

				UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date) null, sender.getName(), (Date) null, s);
				server.getPlayerList().getBannedPlayers().addEntry(userlistbansentry);
				EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);

				if (entityplayermp != null) {
					entityplayermp.connection.kickPlayerFromServer("You are banned from this server.");
				}

				notifyCommandListener(sender, this, "commands.ban.success", new Object[] { args[0] });
			}
		} else {
			throw new WrongUsageException("commands.ban.usage", new Object[0]);
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return args.length >= 1 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.<String> emptyList();
	}
}
