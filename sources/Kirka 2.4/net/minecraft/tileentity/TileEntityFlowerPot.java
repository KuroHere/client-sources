/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;

public class TileEntityFlowerPot
extends TileEntity {
    private Item flowerPotItem;
    private int flowerPotData;
    private static final String __OBFID = "CL_00000356";

    public TileEntityFlowerPot() {
    }

    public TileEntityFlowerPot(Item p_i45442_1_, int p_i45442_2_) {
        this.flowerPotItem = p_i45442_1_;
        this.flowerPotData = p_i45442_2_;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ResourceLocation var2 = (ResourceLocation)Item.itemRegistry.getNameForObject(this.flowerPotItem);
        compound.setString("Item", var2 == null ? "" : var2.toString());
        compound.setInteger("Data", this.flowerPotData);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.flowerPotItem = compound.hasKey("Item", 8) ? Item.getByNameOrId(compound.getString("Item")) : Item.getItemById(compound.getInteger("Item"));
        this.flowerPotData = compound.getInteger("Data");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        var1.removeTag("Item");
        var1.setInteger("Item", Item.getIdFromItem(this.flowerPotItem));
        return new S35PacketUpdateTileEntity(this.pos, 5, var1);
    }

    public void func_145964_a(Item p_145964_1_, int p_145964_2_) {
        this.flowerPotItem = p_145964_1_;
        this.flowerPotData = p_145964_2_;
    }

    public Item getFlowerPotItem() {
        return this.flowerPotItem;
    }

    public int getFlowerPotData() {
        return this.flowerPotData;
    }
}

