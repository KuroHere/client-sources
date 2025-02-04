package me.valk.agway.modules.world;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.valk.event.EventListener;
import me.valk.event.EventType;
import me.valk.event.events.other.EventPacket;
import me.valk.event.events.player.EventMotion;
import me.valk.module.ModData;
import me.valk.module.ModType;
import me.valk.module.Module;
import net.minecraft.network.play.client.C0BPacketEntityAction;

/**
 * Created by Zeb on 4/24/2016.
 */
public class SneakMod extends Module {

    public SneakMod(){
        super(new ModData("Sneak", Keyboard.KEY_NONE, new Color(183, 108, 103)), ModType.PLAYER);
    }

    @EventListener
    public void onMotion(EventMotion event){
        if(event.getType() == EventType.PRE){
            p.sendQueue.netManager.sendPacket(new C0BPacketEntityAction(p, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }else{
            p.sendQueue.netManager.sendPacket(new C0BPacketEntityAction(p, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }

    @EventListener
    public void onPacket(EventPacket event){
        if(event.getPacket() instanceof C0BPacketEntityAction){
            C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();

            event.setCancelled(true);

            if(packet.func_180764_b() == C0BPacketEntityAction.Action.START_SNEAKING || packet.func_180764_b() == C0BPacketEntityAction.Action.STOP_SNEAKING){
                event.setCancelled(true);
            }
        }
    }

}
