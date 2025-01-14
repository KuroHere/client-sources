package net.minecraft.command;

import net.minecraft.command.server.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ServerCommandManager extends CommandHandler implements ICommandListener {
	private final MinecraftServer server;

	public ServerCommandManager(MinecraftServer serverIn) {
		this.server = serverIn;
		this.registerCommand(new CommandTime());
		this.registerCommand(new CommandGameMode());
		this.registerCommand(new CommandDifficulty());
		this.registerCommand(new CommandDefaultGameMode());
		this.registerCommand(new CommandKill());
		this.registerCommand(new CommandToggleDownfall());
		this.registerCommand(new CommandWeather());
		this.registerCommand(new CommandXP());
		this.registerCommand(new CommandTP());
		this.registerCommand(new CommandTeleport());
		this.registerCommand(new CommandGive());
		this.registerCommand(new CommandReplaceItem());
		this.registerCommand(new CommandStats());
		this.registerCommand(new CommandEffect());
		this.registerCommand(new CommandEnchant());
		this.registerCommand(new CommandParticle());
		this.registerCommand(new CommandEmote());
		this.registerCommand(new CommandShowSeed());
		this.registerCommand(new CommandHelp());
		this.registerCommand(new CommandDebug());
		this.registerCommand(new CommandMessage());
		this.registerCommand(new CommandBroadcast());
		this.registerCommand(new CommandSetSpawnpoint());
		this.registerCommand(new CommandSetDefaultSpawnpoint());
		this.registerCommand(new CommandGameRule());
		this.registerCommand(new CommandClearInventory());
		this.registerCommand(new CommandTestFor());
		this.registerCommand(new CommandSpreadPlayers());
		this.registerCommand(new CommandPlaySound());
		this.registerCommand(new CommandScoreboard());
		this.registerCommand(new CommandExecuteAt());
		this.registerCommand(new CommandTrigger());
		this.registerCommand(new CommandAchievement());
		this.registerCommand(new CommandSummon());
		this.registerCommand(new CommandSetBlock());
		this.registerCommand(new CommandFill());
		this.registerCommand(new CommandClone());
		this.registerCommand(new CommandCompare());
		this.registerCommand(new CommandBlockData());
		this.registerCommand(new CommandTestForBlock());
		this.registerCommand(new CommandMessageRaw());
		this.registerCommand(new CommandWorldBorder());
		this.registerCommand(new CommandTitle());
		this.registerCommand(new CommandEntityData());
		this.registerCommand(new CommandStopSound());

		if (serverIn.isDedicatedServer()) {
			this.registerCommand(new CommandOp());
			this.registerCommand(new CommandDeOp());
			this.registerCommand(new CommandStop());
			this.registerCommand(new CommandSaveAll());
			this.registerCommand(new CommandSaveOff());
			this.registerCommand(new CommandSaveOn());
			this.registerCommand(new CommandBanIp());
			this.registerCommand(new CommandPardonIp());
			this.registerCommand(new CommandBanPlayer());
			this.registerCommand(new CommandListBans());
			this.registerCommand(new CommandPardonPlayer());
			this.registerCommand(new CommandServerKick());
			this.registerCommand(new CommandListPlayers());
			this.registerCommand(new CommandWhitelist());
			this.registerCommand(new CommandSetPlayerTimeout());
		} else {
			this.registerCommand(new CommandPublishLocalServer());
		}

		CommandBase.setCommandListener(this);
	}

	/**
	 * Send an informative message to the server operators
	 */
	@Override
	public void notifyListener(ICommandSender sender, ICommand command, int flags, String translationKey, Object... translationArgs) {
		boolean flag = true;
		MinecraftServer minecraftserver = this.server;

		if (!sender.sendCommandFeedback()) {
			flag = false;
		}

		ITextComponent itextcomponent = new TextComponentTranslation("chat.type.admin", new Object[] { sender.getName(), new TextComponentTranslation(translationKey, translationArgs) });
		itextcomponent.getStyle().setColor(TextFormatting.GRAY);
		itextcomponent.getStyle().setItalic(Boolean.valueOf(true));

		if (flag) {
			for (EntityPlayer entityplayer : minecraftserver.getPlayerList().getPlayerList()) {
				if ((entityplayer != sender) && minecraftserver.getPlayerList().canSendCommands(entityplayer.getGameProfile()) && command.checkPermission(this.server, sender)) {
					boolean flag1 = (sender instanceof MinecraftServer) && this.server.shouldBroadcastConsoleToOps();
					boolean flag2 = (sender instanceof RConConsoleSource) && this.server.shouldBroadcastRconToOps();

					if (flag1 || flag2 || (!(sender instanceof RConConsoleSource) && !(sender instanceof MinecraftServer))) {
						entityplayer.addChatMessage(itextcomponent);
					}
				}
			}
		}

		if ((sender != minecraftserver) && minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands")) {
			minecraftserver.addChatMessage(itextcomponent);
		}

		boolean flag3 = minecraftserver.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");

		if (sender instanceof CommandBlockBaseLogic) {
			flag3 = ((CommandBlockBaseLogic) sender).shouldTrackOutput();
		}

		if ((((flags & 1) != 1) && flag3) || (sender instanceof MinecraftServer)) {
			sender.addChatMessage(new TextComponentTranslation(translationKey, translationArgs));
		}
	}

	@Override
	protected MinecraftServer getServer() {
		return this.server;
	}
}
