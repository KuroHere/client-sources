/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketOpenWindow
implements Packet<INetHandlerPlayClient> {
    private int windowId;
    private String inventoryType;
    private ITextComponent windowTitle;
    private int slotCount;
    private int entityId;

    public SPacketOpenWindow() {
    }

    public SPacketOpenWindow(int windowIdIn, String inventoryTypeIn, ITextComponent windowTitleIn) {
        this(windowIdIn, inventoryTypeIn, windowTitleIn, 0);
    }

    public SPacketOpenWindow(int windowIdIn, String inventoryTypeIn, ITextComponent windowTitleIn, int slotCountIn) {
        this.windowId = windowIdIn;
        this.inventoryType = inventoryTypeIn;
        this.windowTitle = windowTitleIn;
        this.slotCount = slotCountIn;
    }

    public SPacketOpenWindow(int windowIdIn, String inventoryTypeIn, ITextComponent windowTitleIn, int slotCountIn, int entityIdIn) {
        this(windowIdIn, inventoryTypeIn, windowTitleIn, slotCountIn);
        this.entityId = entityIdIn;
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleOpenWindow(this);
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.inventoryType = buf.readStringFromBuffer(32);
        this.windowTitle = buf.readTextComponent();
        this.slotCount = buf.readUnsignedByte();
        if (this.inventoryType.equals("EntityHorse")) {
            this.entityId = buf.readInt();
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeString(this.inventoryType);
        buf.writeTextComponent(this.windowTitle);
        buf.writeByte(this.slotCount);
        if (this.inventoryType.equals("EntityHorse")) {
            buf.writeInt(this.entityId);
        }
    }

    public int getWindowId() {
        return this.windowId;
    }

    public String getGuiId() {
        return this.inventoryType;
    }

    public ITextComponent getWindowTitle() {
        return this.windowTitle;
    }

    public int getSlotCount() {
        return this.slotCount;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean hasSlots() {
        return this.slotCount > 0;
    }
}

