package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\n\b\bf\u000020R\b00X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketTabComplete;", "", "completions", "", "", "getCompletions", "()[Ljava/lang/String;", "Pride"})
public interface ISPacketTabComplete {
    @NotNull
    public String[] getCompletions();
}
