/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityPainting
extends EntityHanging {
    public EnumArt art;
    private static final String __OBFID = "CL_00001556";

    public EntityPainting(World worldIn) {
        super(worldIn);
    }

    public EntityPainting(World worldIn, BlockPos p_i45849_2_, EnumFacing p_i45849_3_) {
        super(worldIn, p_i45849_2_);
        ArrayList var4 = Lists.newArrayList();
        EnumArt[] var5 = EnumArt.values();
        int var6 = var5.length;
        for (int var7 = 0; var7 < var6; ++var7) {
            EnumArt var8;
            this.art = var8 = var5[var7];
            this.func_174859_a(p_i45849_3_);
            if (!this.onValidSurface()) continue;
            var4.add(var8);
        }
        if (!var4.isEmpty()) {
            this.art = (EnumArt)((Object)var4.get(this.rand.nextInt(var4.size())));
        }
        this.func_174859_a(p_i45849_3_);
    }

    public EntityPainting(World worldIn, BlockPos p_i45850_2_, EnumFacing p_i45850_3_, String p_i45850_4_) {
        this(worldIn, p_i45850_2_, p_i45850_3_);
        for (EnumArt var8 : EnumArt.values()) {
            if (!var8.title.equals(p_i45850_4_)) continue;
            this.art = var8;
            break;
        }
        this.func_174859_a(p_i45850_3_);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setString("Motive", this.art.title);
        super.writeEntityToNBT(tagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        String var2 = tagCompund.getString("Motive");
        for (EnumArt var6 : EnumArt.values()) {
            if (!var6.title.equals(var2)) continue;
            this.art = var6;
        }
        if (this.art == null) {
            this.art = EnumArt.KEBAB;
        }
        super.readEntityFromNBT(tagCompund);
    }

    @Override
    public int getWidthPixels() {
        return this.art.sizeX;
    }

    @Override
    public int getHeightPixels() {
        return this.art.sizeY;
    }

    @Override
    public void onBroken(Entity p_110128_1_) {
        if (this.worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            if (p_110128_1_ instanceof EntityPlayer) {
                EntityPlayer var2 = (EntityPlayer)p_110128_1_;
                if (var2.capabilities.isCreativeMode) {
                    return;
                }
            }
            this.entityDropItem(new ItemStack(Items.painting), 0.0f);
        }
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        BlockPos var9 = new BlockPos(x - this.posX, y - this.posY, z - this.posZ);
        BlockPos var10 = this.field_174861_a.add(var9);
        this.setPosition(var10.getX(), var10.getY(), var10.getZ());
    }

    @Override
    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        BlockPos var11 = new BlockPos(p_180426_1_ - this.posX, p_180426_3_ - this.posY, p_180426_5_ - this.posZ);
        BlockPos var12 = this.field_174861_a.add(var11);
        this.setPosition(var12.getX(), var12.getY(), var12.getZ());
    }

    public static enum EnumArt {
        KEBAB("KEBAB", 0, "Kebab", 16, 16, 0, 0),
        AZTEC("AZTEC", 1, "Aztec", 16, 16, 16, 0),
        ALBAN("ALBAN", 2, "Alban", 16, 16, 32, 0),
        AZTEC_2("AZTEC_2", 3, "Aztec2", 16, 16, 48, 0),
        BOMB("BOMB", 4, "Bomb", 16, 16, 64, 0),
        PLANT("PLANT", 5, "Plant", 16, 16, 80, 0),
        WASTELAND("WASTELAND", 6, "Wasteland", 16, 16, 96, 0),
        POOL("POOL", 7, "Pool", 32, 16, 0, 32),
        COURBET("COURBET", 8, "Courbet", 32, 16, 32, 32),
        SEA("SEA", 9, "Sea", 32, 16, 64, 32),
        SUNSET("SUNSET", 10, "Sunset", 32, 16, 96, 32),
        CREEBET("CREEBET", 11, "Creebet", 32, 16, 128, 32),
        WANDERER("WANDERER", 12, "Wanderer", 16, 32, 0, 64),
        GRAHAM("GRAHAM", 13, "Graham", 16, 32, 16, 64),
        MATCH("MATCH", 14, "Match", 32, 32, 0, 128),
        BUST("BUST", 15, "Bust", 32, 32, 32, 128),
        STAGE("STAGE", 16, "Stage", 32, 32, 64, 128),
        VOID("VOID", 17, "Void", 32, 32, 96, 128),
        SKULL_AND_ROSES("SKULL_AND_ROSES", 18, "SkullAndRoses", 32, 32, 128, 128),
        WITHER("WITHER", 19, "Wither", 32, 32, 160, 128),
        FIGHTERS("FIGHTERS", 20, "Fighters", 64, 32, 0, 96),
        POINTER("POINTER", 21, "Pointer", 64, 64, 0, 192),
        PIGSCENE("PIGSCENE", 22, "Pigscene", 64, 64, 64, 192),
        BURNING_SKULL("BURNING_SKULL", 23, "BurningSkull", 64, 64, 128, 192),
        SKELETON("SKELETON", 24, "Skeleton", 64, 48, 192, 64),
        DONKEY_KONG("DONKEY_KONG", 25, "DonkeyKong", 64, 48, 192, 112);
        
        public static final int field_180001_A;
        public final String title;
        public final int sizeX;
        public final int sizeY;
        public final int offsetX;
        public final int offsetY;
        private static final EnumArt[] $VALUES;
        private static final String __OBFID = "CL_00001557";

        static {
            field_180001_A = "SkullAndRoses".length();
            $VALUES = new EnumArt[]{KEBAB, AZTEC, ALBAN, AZTEC_2, BOMB, PLANT, WASTELAND, POOL, COURBET, SEA, SUNSET, CREEBET, WANDERER, GRAHAM, MATCH, BUST, STAGE, VOID, SKULL_AND_ROSES, WITHER, FIGHTERS, POINTER, PIGSCENE, BURNING_SKULL, SKELETON, DONKEY_KONG};
        }

        private EnumArt(String p_i1598_1_, int p_i1598_2_, String p_i1598_3_, int p_i1598_4_, int p_i1598_5_, int p_i1598_6_, int p_i1598_7_) {
            this.title = p_i1598_3_;
            this.sizeX = p_i1598_4_;
            this.sizeY = p_i1598_5_;
            this.offsetX = p_i1598_6_;
            this.offsetY = p_i1598_7_;
        }
    }

}

