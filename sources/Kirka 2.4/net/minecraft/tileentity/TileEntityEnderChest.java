/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityEnderChest
extends TileEntity
implements IUpdatePlayerListBox {
    public float field_145972_a;
    public float prevLidAngle;
    public int field_145973_j;
    private int field_145974_k;
    private static final String __OBFID = "CL_00000355";

    @Override
    public void update() {
        double var7;
        if (++this.field_145974_k % 20 * 4 == 0) {
            this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
        }
        this.prevLidAngle = this.field_145972_a;
        int var1 = this.pos.getX();
        int var2 = this.pos.getY();
        int var3 = this.pos.getZ();
        float var4 = 0.1f;
        if (this.field_145973_j > 0 && this.field_145972_a == 0.0f) {
            double var5 = (double)var1 + 0.5;
            var7 = (double)var3 + 0.5;
            this.worldObj.playSoundEffect(var5, (double)var2 + 0.5, var7, "random.chestopen", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if (this.field_145973_j == 0 && this.field_145972_a > 0.0f || this.field_145973_j > 0 && this.field_145972_a < 1.0f) {
            float var6;
            float var11 = this.field_145972_a;
            this.field_145972_a = this.field_145973_j > 0 ? (this.field_145972_a += var4) : (this.field_145972_a -= var4);
            if (this.field_145972_a > 1.0f) {
                this.field_145972_a = 1.0f;
            }
            if (this.field_145972_a < (var6 = 0.5f) && var11 >= var6) {
                var7 = (double)var1 + 0.5;
                double var9 = (double)var3 + 0.5;
                this.worldObj.playSoundEffect(var7, (double)var2 + 0.5, var9, "random.chestclosed", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.field_145972_a < 0.0f) {
                this.field_145972_a = 0.0f;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.field_145973_j = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }

    public void func_145969_a() {
        ++this.field_145973_j;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public void func_145970_b() {
        --this.field_145973_j;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public boolean func_145971_a(EntityPlayer p_145971_1_) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : p_145971_1_.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }
}

