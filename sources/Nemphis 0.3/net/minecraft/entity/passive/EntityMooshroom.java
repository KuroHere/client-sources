/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockMycelium;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityMooshroom
extends EntityCow {
    private static final String __OBFID = "CL_00001645";

    public EntityMooshroom(World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 1.3f);
        this.field_175506_bl = Blocks.mycelium;
    }

    @Override
    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.bowl && this.getGrowingAge() >= 0) {
            if (var2.stackSize == 1) {
                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, new ItemStack(Items.mushroom_stew));
                return true;
            }
            if (p_70085_1_.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew)) && !p_70085_1_.capabilities.isCreativeMode) {
                p_70085_1_.inventory.decrStackSize(p_70085_1_.inventory.currentItem, 1);
                return true;
            }
        }
        if (var2 != null && var2.getItem() == Items.shears && this.getGrowingAge() >= 0) {
            this.setDead();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (double)(this.height / 2.0f), this.posZ, 0.0, 0.0, 0.0, new int[0]);
            if (!this.worldObj.isRemote) {
                EntityCow var3 = new EntityCow(this.worldObj);
                var3.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                var3.setHealth(this.getHealth());
                var3.renderYawOffset = this.renderYawOffset;
                if (this.hasCustomName()) {
                    var3.setCustomNameTag(this.getCustomNameTag());
                }
                this.worldObj.spawnEntityInWorld(var3);
                int var4 = 0;
                while (var4 < 5) {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + (double)this.height, this.posZ, new ItemStack(Blocks.red_mushroom)));
                    ++var4;
                }
                var2.damageItem(1, p_70085_1_);
                this.playSound("mob.sheep.shear", 1.0f, 1.0f);
            }
            return true;
        }
        return super.interact(p_70085_1_);
    }

    @Override
    public EntityMooshroom createChild(EntityAgeable p_90011_1_) {
        return new EntityMooshroom(this.worldObj);
    }
}

