// 
// Decompiled by Procyon v0.5.30
// 

package net.andrewsnetwork.icarus.command;

import net.minecraft.client.Minecraft;

public abstract class Command
{
    protected final String command;
    protected final String arguments;
    protected static final Minecraft mc;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public Command(final String command, final String arguments) {
        this.command = command;
        this.arguments = arguments;
    }
    
    public String getArguments() {
        return this.arguments;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public abstract void run(final String p0);
}
