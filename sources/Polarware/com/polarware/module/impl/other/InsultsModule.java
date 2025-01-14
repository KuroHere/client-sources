package com.polarware.module.impl.other;

import com.polarware.component.impl.player.BadPacketsComponent;
import com.polarware.module.Module;
import com.polarware.module.api.Category;
import com.polarware.module.api.ModuleInfo;
import com.polarware.event.bus.Listener;
import com.polarware.event.annotations.EventLink;
import com.polarware.event.impl.motion.PreMotionEvent;
import com.polarware.event.impl.other.AttackEvent;
import com.polarware.event.impl.other.WorldChangeEvent;
import com.polarware.util.player.PlayerUtil;
import com.polarware.value.impl.ModeValue;
import com.polarware.value.impl.NumberValue;
import com.polarware.value.impl.SubMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ModuleInfo(name = "module.other.insults.name", description = "module.other.insults.description", category = Category.OTHER)
public final class InsultsModule extends Module {

    public final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Default"))
            .add(new SubMode("Watchdog"))
            .add(new SubMode("WhatsApp"))
            .setDefault("Default");

    public final Map<String, List<String>> map = new HashMap<>();

    private final NumberValue delay = new NumberValue("Delay", this, 0, 0, 50, 1);

    private final String[] defaultInsults = {
            "Wow! My combo is Rise'n!",
            "Why would someone as bad as you not use Rise 6.0?",
            "Here's your ticket to spectator from Rise 6.0!",
            "I see you're a pay to lose player, huh?",
            "Do you need some PvP advice? Well Rise 6.0 is all you need.",
            "Hey! Wise up, don't waste another day without Rise.",
            "You didn't even stand a chance against Rise.",
            "We regret to inform you that your free trial of life has unfortunately expired.",
            "RISE against other cheaters by getting Rise!",
            "You can pay for that loss by getting Rise.",
            "Remember to use hand sanitizer to get rid of bacteria like you!",
            "Hey, try not to drown in your own salt.",
            "Having problems with forgetting to left click? Rise 6.0 can fix it!",
            "Come on, is that all you have against Rise 6.0?",
            "Rise up today by getting Rise 6.0!",
            "Get Rise, you need it."
    };

    private final String[] whatsAppInsults = {
            "Add me on WhatsApp ",
    };

    private EntityPlayer target;
    private double lastSpeed;
    private int ticks;

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (target != null && !mc.theWorld.playerEntities.contains(target)) {

            if (ticks >= delay.getValue().intValue() + Math.random() * 5 && !BadPacketsComponent.bad()) {
                String insult = "";

                switch (mode.getValue().getName()) {
                    case "Default":
                        insult = defaultInsults[RandomUtils.nextInt(0, defaultInsults.length)];
                        break;

                    case "Watchdog":
                        insult = "[STAFF] [WATCHDOG] %s reeled in.";
                        break;

                    case "WhatsApp":
                        insult = whatsAppInsults[RandomUtils.nextInt(0, whatsAppInsults.length)];
                        break;
                }

                mc.thePlayer.sendChatMessage(String.format(insult, PlayerUtil.name(target)));
                target = null;
            }

            ticks++;
        }
    };

    @EventLink()
    public final Listener<AttackEvent> onAttack = event -> {

        final Entity target = event.getTarget();

        if (target instanceof EntityPlayer) {
            this.target = (EntityPlayer) target;
            ticks = 0;
        }
    };

    @EventLink()
    public final Listener<WorldChangeEvent> onWorldChange = event -> {

        target = null;
        ticks = 0;
    };
}