package net.minecraft.client.main.neptune.Mod.Collection.Cmds;

import net.minecraft.client.main.neptune.Mod.Cmd;
import net.minecraft.client.main.neptune.Mod.Cmd.Info;
import net.minecraft.client.main.neptune.Utils.ChatUtils;
import net.minecraft.client.main.neptune.Utils.ModeUtils;

@Info(name = "tracers", syntax = { "<players/mobs/animals>" }, help = "Change ESP settings")
public class TracersCmd extends Cmd {
	@Override
	public void execute(final String[] p0) throws Error {
		if (p0.length > 1) {
			this.syntaxError();
		} else if (p0.length == 1) {
			if (p0[0].equalsIgnoreCase("players")) {
				ModeUtils.tracerP = !ModeUtils.tracerP;
				ChatUtils.sendMessageToPlayer("Players set to: " + ModeUtils.tracerP);
			} else if (p0[0].equalsIgnoreCase("mobs")) {
				ModeUtils.tracerM = !ModeUtils.tracerM;
				ChatUtils.sendMessageToPlayer("Mobs set to: " + ModeUtils.tracerM);
			} else if (p0[0].equalsIgnoreCase("animals")) {
				ModeUtils.tracerA = !ModeUtils.tracerA;
				ChatUtils.sendMessageToPlayer("Animals set to: " + ModeUtils.tracerA);
			} else {
				this.syntaxError();
			}
		} else {
			this.syntaxError();
		}
	}
}
