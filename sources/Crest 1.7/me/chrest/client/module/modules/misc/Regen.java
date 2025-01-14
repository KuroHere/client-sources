// 
// Decompiled by Procyon v0.5.30
// 

package me.chrest.client.module.modules.misc;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import me.chrest.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.chrest.event.Event;
import net.minecraft.potion.Potion;
import me.chrest.utils.ClientUtils;
import me.chrest.event.events.UpdateEvent;
import me.chrest.client.option.Option;
import me.chrest.client.module.Module;

@Mod(displayName = "Regen")
public class Regen extends Module
{
    @Option.Op(min = 0.0, max = 6000.0, increment = 100.0, name = "Packet")
    private double packets;
    @Option.Op(name = "Potion Packets", min = 0.0, max = 200.0, increment = 10.0)
    private double potionPackets;
    @Option.Op(min = 0.0, max = 10.0, increment = 0.5, name = "Health")
    private double health;
    @Option.Op(name = "NCP")
    private boolean potion;
    
    public Regen() {
        this.packets = 3000.0;
        this.potionPackets = 60.0;
        this.health = 8.0;
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (this.potion) {
            if (ClientUtils.player().getActivePotionEffect(Potion.regeneration) != null && ClientUtils.player().getActivePotionEffect(Potion.regeneration).getDuration() > 0 && ClientUtils.player().getHealth() <= this.health * 2.0 && (ClientUtils.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(Event.State.POST)) {
                for (int i = 0; i < this.potionPackets; ++i) {
                    ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
                    ClientUtils.packet(new C03PacketPlayer(true));
                }
            }
        }
        else if (ClientUtils.player().getHealth() <= this.health * 2.0 && (ClientUtils.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(Event.State.POST)) {
            for (int i = 0; i < this.packets; ++i) {
                ClientUtils.packet(new C03PacketPlayer(true));
            }
        }
    }
    
    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(ClientUtils.player().boundingBox.minX); x < MathHelper.floor_double(ClientUtils.player().boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(ClientUtils.player().boundingBox.minY); y < MathHelper.floor_double(ClientUtils.player().boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(ClientUtils.player().boundingBox.minZ); z < MathHelper.floor_double(ClientUtils.player().boundingBox.maxZ) + 1; ++z) {
                    final Block block = ClientUtils.world().getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(ClientUtils.world(), new BlockPos(x, y, z), ClientUtils.world().getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && ClientUtils.player().boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
