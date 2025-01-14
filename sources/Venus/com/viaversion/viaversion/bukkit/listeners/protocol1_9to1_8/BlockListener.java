/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.plugin.Plugin
 */
package com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8;

import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class BlockListener
extends ViaBukkitListener {
    public BlockListener(Plugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void placeBlock(BlockPlaceEvent blockPlaceEvent) {
        if (this.isOnPipe(blockPlaceEvent.getPlayer())) {
            Block block = blockPlaceEvent.getBlockPlaced();
            EntityTracker1_9 entityTracker1_9 = (EntityTracker1_9)this.getUserConnection(blockPlaceEvent.getPlayer()).getEntityTracker(Protocol1_9To1_8.class);
            entityTracker1_9.addBlockInteraction(new Position(block.getX(), block.getY(), block.getZ()));
        }
    }
}

