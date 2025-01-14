package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.event.Listener;
import com.alan.clients.event.annotations.EventLink;
import com.alan.clients.event.impl.motion.PreMotionEvent;
import com.alan.clients.event.impl.packet.PacketReceiveEvent;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;

public class CreativeFlight extends Mode<Flight> {

    public CreativeFlight(String name, Flight parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        mc.thePlayer.capabilities.allowFlying = true;
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        final Packet<?> p = event.getPacket();

        if (p instanceof S39PacketPlayerAbilities) {
            final S39PacketPlayerAbilities wrapper = (S39PacketPlayerAbilities) p;
            wrapper.setFlying(mc.thePlayer.capabilities.isFlying);
        }
    };

    @Override
    public void onEnable() {
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }
        mc.thePlayer.capabilities.isFlying = true;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.allowFlying = mc.playerController.getCurrentGameType().isCreative();
        mc.thePlayer.capabilities.isFlying = false;
    }
}