/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.SoundCategory
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.plugin.Plugin
 */
package com.viaversion.viabackwards.listener;

import com.viaversion.viabackwards.BukkitPlugin;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class FireDamageListener
extends ViaBukkitListener {
    public FireDamageListener(BukkitPlugin bukkitPlugin) {
        super((Plugin)bukkitPlugin, Protocol1_11_1To1_12.class);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onFireDamage(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getEntityType() != EntityType.PLAYER) {
            return;
        }
        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();
        if (damageCause != EntityDamageEvent.DamageCause.FIRE && damageCause != EntityDamageEvent.DamageCause.FIRE_TICK && damageCause != EntityDamageEvent.DamageCause.LAVA && damageCause != EntityDamageEvent.DamageCause.DROWNING) {
            return;
        }
        Player player = (Player)entityDamageEvent.getEntity();
        if (this.isOnPipe(player)) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }
}

