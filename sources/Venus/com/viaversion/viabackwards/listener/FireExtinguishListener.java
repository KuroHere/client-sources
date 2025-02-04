/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 */
package com.viaversion.viabackwards.listener;

import com.viaversion.viabackwards.BukkitPlugin;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class FireExtinguishListener
extends ViaBukkitListener {
    public FireExtinguishListener(BukkitPlugin bukkitPlugin) {
        super((Plugin)bukkitPlugin, Protocol1_15_2To1_16.class);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onFireExtinguish(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Block block = playerInteractEvent.getClickedBlock();
        if (block == null) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        if (!this.isOnPipe(player)) {
            return;
        }
        Block block2 = block.getRelative(playerInteractEvent.getBlockFace());
        if (block2.getType() == Material.FIRE) {
            playerInteractEvent.setCancelled(false);
            block2.setType(Material.AIR);
        }
    }
}

