package me.gishreload.yukon.command;

import net.minecraft.client.Minecraft;

public abstract class Command {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	public abstract String getAlias();
	public abstract String getDescription();
	public abstract String getSyntax();
	public abstract void onCommand(String command, String[] args) throws Exception;

}
