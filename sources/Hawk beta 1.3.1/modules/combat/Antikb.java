package eze.modules.combat;

import eze.modules.*;
import eze.events.*;
import eze.events.listeners.*;
import net.minecraft.network.play.server.*;

public class Antikb extends Module
{
    public Antikb() {
        super("Antikb", 25, Category.COMBAT);
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket && e.isIncoming()) {
            final EventPacket event = (EventPacket)e;
            if (EventPacket.getPacket() instanceof S12PacketEntityVelocity) {
                event.setCancelled(true);
            }
        }
    }
}
