/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFirework
extends Item {
    private static final String __OBFID = "CL_00000031";

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityFireworkRocket var9 = new EntityFireworkRocket(worldIn, (float)pos.getX() + hitX, (float)pos.getY() + hitY, (float)pos.getZ() + hitZ, stack);
            worldIn.spawnEntityInWorld(var9);
            if (!playerIn.capabilities.isCreativeMode) {
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        NBTTagCompound var5;
        if (stack.hasTagCompound() && (var5 = stack.getTagCompound().getCompoundTag("Fireworks")) != null) {
            NBTTagList var6;
            if (var5.hasKey("Flight", 99)) {
                tooltip.add(String.valueOf(StatCollector.translateToLocal("item.fireworks.flight")) + " " + var5.getByte("Flight"));
            }
            if ((var6 = var5.getTagList("Explosions", 10)) != null && var6.tagCount() > 0) {
                for (int var7 = 0; var7 < var6.tagCount(); ++var7) {
                    NBTTagCompound var8 = var6.getCompoundTagAt(var7);
                    ArrayList<String> var9 = Lists.newArrayList();
                    ItemFireworkCharge.func_150902_a(var8, var9);
                    if (var9.size() <= 0) continue;
                    for (int var10 = 1; var10 < var9.size(); ++var10) {
                        var9.set(var10, "  " + (String)var9.get(var10));
                    }
                    tooltip.addAll(var9);
                }
            }
        }
    }
}

