/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmorStand;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand
extends EntityLivingBase {
    private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
    private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0f, 0.0f, 0.0f);
    private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0f, 0.0f, -10.0f);
    private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0f, 0.0f, 10.0f);
    private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0f, 0.0f, -1.0f);
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0f, 0.0f, 1.0f);
    private final ItemStack[] contents = new ItemStack[5];
    private boolean canInteract;
    private long field_175437_i;
    private int disabledSlots;
    private Rotations headRotation = DEFAULT_HEAD_ROTATION;
    private Rotations bodyRotation = DEFAULT_BODY_ROTATION;
    private Rotations leftArmRotation = DEFAULT_LEFTARM_ROTATION;
    private Rotations rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
    private Rotations leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
    private Rotations rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
    private static final String __OBFID = "CL_00002228";

    public EntityArmorStand(World worldIn) {
        super(worldIn);
        this.func_174810_b(true);
        this.noClip = this.hasNoGravity();
        this.setSize(0.5f, 1.975f);
    }

    public EntityArmorStand(World worldIn, double p_i45855_2_, double p_i45855_4_, double p_i45855_6_) {
        this(worldIn);
        this.setPosition(p_i45855_2_, p_i45855_4_, p_i45855_6_);
    }

    @Override
    public boolean isServerWorld() {
        if (super.isServerWorld() && !this.hasNoGravity()) {
            return true;
        }
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(10, Byte.valueOf(0));
        this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
        this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
        this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
        this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
        this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
        this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
    }

    @Override
    public ItemStack getHeldItem() {
        return this.contents[0];
    }

    @Override
    public ItemStack getEquipmentInSlot(int p_71124_1_) {
        return this.contents[p_71124_1_];
    }

    @Override
    public ItemStack getCurrentArmor(int p_82169_1_) {
        return this.contents[p_82169_1_ + 1];
    }

    @Override
    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn) {
        this.contents[slotIn] = itemStackIn;
    }

    @Override
    public ItemStack[] getInventory() {
        return this.contents;
    }

    @Override
    public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_) {
        int var3;
        if (p_174820_1_ == 99) {
            var3 = 0;
        } else {
            var3 = p_174820_1_ - 100 + 1;
            if (var3 < 0 || var3 >= this.contents.length) {
                return false;
            }
        }
        if (!(p_174820_2_ == null || EntityLiving.getArmorPosition(p_174820_2_) == var3 || var3 == 4 && p_174820_2_.getItem() instanceof ItemBlock)) {
            return false;
        }
        this.setCurrentItemOrArmor(var3, p_174820_2_);
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        NBTTagList var2 = new NBTTagList();
        int var3 = 0;
        while (var3 < this.contents.length) {
            NBTTagCompound var4 = new NBTTagCompound();
            if (this.contents[var3] != null) {
                this.contents[var3].writeToNBT(var4);
            }
            var2.appendTag(var4);
            ++var3;
        }
        tagCompound.setTag("Equipment", var2);
        if (this.getAlwaysRenderNameTag() && (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0)) {
            tagCompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
        }
        tagCompound.setBoolean("Invisible", this.isInvisible());
        tagCompound.setBoolean("Small", this.isSmall());
        tagCompound.setBoolean("ShowArms", this.getShowArms());
        tagCompound.setInteger("DisabledSlots", this.disabledSlots);
        tagCompound.setBoolean("NoGravity", this.hasNoGravity());
        tagCompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
        tagCompound.setTag("Pose", this.readPoseFromNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("Equipment", 9)) {
            NBTTagList var2 = tagCompund.getTagList("Equipment", 10);
            int var3 = 0;
            while (var3 < this.contents.length) {
                this.contents[var3] = ItemStack.loadItemStackFromNBT(var2.getCompoundTagAt(var3));
                ++var3;
            }
        }
        this.setInvisible(tagCompund.getBoolean("Invisible"));
        this.setSmall(tagCompund.getBoolean("Small"));
        this.setShowArms(tagCompund.getBoolean("ShowArms"));
        this.disabledSlots = tagCompund.getInteger("DisabledSlots");
        this.setNoGravity(tagCompund.getBoolean("NoGravity"));
        this.setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
        this.noClip = this.hasNoGravity();
        NBTTagCompound var4 = tagCompund.getCompoundTag("Pose");
        this.writePoseToNBT(var4);
    }

    private void writePoseToNBT(NBTTagCompound p_175416_1_) {
        NBTTagList var2 = p_175416_1_.getTagList("Head", 5);
        if (var2.tagCount() > 0) {
            this.setHeadRotation(new Rotations(var2));
        } else {
            this.setHeadRotation(DEFAULT_HEAD_ROTATION);
        }
        NBTTagList var3 = p_175416_1_.getTagList("Body", 5);
        if (var3.tagCount() > 0) {
            this.setBodyRotation(new Rotations(var3));
        } else {
            this.setBodyRotation(DEFAULT_BODY_ROTATION);
        }
        NBTTagList var4 = p_175416_1_.getTagList("LeftArm", 5);
        if (var4.tagCount() > 0) {
            this.setLeftArmRotation(new Rotations(var4));
        } else {
            this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
        }
        NBTTagList var5 = p_175416_1_.getTagList("RightArm", 5);
        if (var5.tagCount() > 0) {
            this.setRightArmRotation(new Rotations(var5));
        } else {
            this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
        }
        NBTTagList var6 = p_175416_1_.getTagList("LeftLeg", 5);
        if (var6.tagCount() > 0) {
            this.setLeftLegRotation(new Rotations(var6));
        } else {
            this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
        }
        NBTTagList var7 = p_175416_1_.getTagList("RightLeg", 5);
        if (var7.tagCount() > 0) {
            this.setRightLegRotation(new Rotations(var7));
        } else {
            this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
        }
    }

    private NBTTagCompound readPoseFromNBT() {
        NBTTagCompound var1 = new NBTTagCompound();
        if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
            var1.setTag("Head", this.headRotation.func_179414_a());
        }
        if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
            var1.setTag("Body", this.bodyRotation.func_179414_a());
        }
        if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
            var1.setTag("LeftArm", this.leftArmRotation.func_179414_a());
        }
        if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
            var1.setTag("RightArm", this.rightArmRotation.func_179414_a());
        }
        if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
            var1.setTag("LeftLeg", this.leftLegRotation.func_179414_a());
        }
        if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
            var1.setTag("RightLeg", this.rightLegRotation.func_179414_a());
        }
        return var1;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity p_82167_1_) {
    }

    @Override
    protected void collideWithNearbyEntities() {
        List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
        if (var1 != null && !var1.isEmpty()) {
            int var2 = 0;
            while (var2 < var1.size()) {
                Entity var3 = (Entity)var1.get(var2);
                if (var3 instanceof EntityMinecart && ((EntityMinecart)var3).func_180456_s() == EntityMinecart.EnumMinecartType.RIDEABLE && this.getDistanceSqToEntity(var3) <= 0.2) {
                    var3.applyEntityCollision(this);
                }
                ++var2;
            }
        }
    }

    @Override
    public boolean func_174825_a(EntityPlayer p_174825_1_, Vec3 p_174825_2_) {
        if (!this.worldObj.isRemote && !p_174825_1_.func_175149_v()) {
            double var16;
            boolean var5;
            boolean var18;
            int var3 = 0;
            ItemStack var4 = p_174825_1_.getCurrentEquippedItem();
            boolean bl = var5 = var4 != null;
            if (var5 && var4.getItem() instanceof ItemArmor) {
                ItemArmor var6 = (ItemArmor)var4.getItem();
                if (var6.armorType == 3) {
                    var3 = 1;
                } else if (var6.armorType == 2) {
                    var3 = 2;
                } else if (var6.armorType == 1) {
                    var3 = 3;
                } else if (var6.armorType == 0) {
                    var3 = 4;
                }
            }
            if (var5 && (var4.getItem() == Items.skull || var4.getItem() == Item.getItemFromBlock(Blocks.pumpkin))) {
                var3 = 4;
            }
            double var19 = 0.1;
            double var8 = 0.9;
            double var10 = 0.4;
            double var12 = 1.6;
            int var14 = 0;
            boolean var15 = this.isSmall();
            double d = var16 = var15 ? p_174825_2_.yCoord * 2.0 : p_174825_2_.yCoord;
            if (var16 >= 0.1 && var16 < 0.1 + (var15 ? 0.8 : 0.45) && this.contents[1] != null) {
                var14 = 1;
            } else if (var16 >= 0.9 + (var15 ? 0.3 : 0.0) && var16 < 0.9 + (var15 ? 1.0 : 0.7) && this.contents[3] != null) {
                var14 = 3;
            } else if (var16 >= 0.4 && var16 < 0.4 + (var15 ? 1.0 : 0.8) && this.contents[2] != null) {
                var14 = 2;
            } else if (var16 >= 1.6 && this.contents[4] != null) {
                var14 = 4;
            }
            boolean bl2 = var18 = this.contents[var14] != null;
            if ((this.disabledSlots & 1 << var14) != 0 || (this.disabledSlots & 1 << var3) != 0) {
                var14 = var3;
                if ((this.disabledSlots & 1 << var3) != 0) {
                    if ((this.disabledSlots & 1) != 0) {
                        return true;
                    }
                    var14 = 0;
                }
            }
            if (var5 && var3 == 0 && !this.getShowArms()) {
                return true;
            }
            if (var5) {
                this.func_175422_a(p_174825_1_, var3);
            } else if (var18) {
                this.func_175422_a(p_174825_1_, var14);
            }
            return true;
        }
        return true;
    }

    private void func_175422_a(EntityPlayer p_175422_1_, int p_175422_2_) {
        ItemStack var3 = this.contents[p_175422_2_];
        if (!(var3 != null && (this.disabledSlots & 1 << p_175422_2_ + 8) != 0 || var3 == null && (this.disabledSlots & 1 << p_175422_2_ + 16) != 0)) {
            int var4 = p_175422_1_.inventory.currentItem;
            ItemStack var5 = p_175422_1_.inventory.getStackInSlot(var4);
            if (p_175422_1_.capabilities.isCreativeMode && (var3 == null || var3.getItem() == Item.getItemFromBlock(Blocks.air)) && var5 != null) {
                ItemStack var6 = var5.copy();
                var6.stackSize = 1;
                this.setCurrentItemOrArmor(p_175422_2_, var6);
            } else if (var5 != null && var5.stackSize > 1) {
                if (var3 == null) {
                    ItemStack var6 = var5.copy();
                    var6.stackSize = 1;
                    this.setCurrentItemOrArmor(p_175422_2_, var6);
                    --var5.stackSize;
                }
            } else {
                this.setCurrentItemOrArmor(p_175422_2_, var5);
                p_175422_1_.inventory.setInventorySlotContents(var4, var3);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.worldObj.isRemote && !this.canInteract) {
            if (DamageSource.outOfWorld.equals(source)) {
                this.setDead();
                return false;
            }
            if (this.func_180431_b(source)) {
                return false;
            }
            if (source.isExplosion()) {
                this.dropContents();
                this.setDead();
                return false;
            }
            if (DamageSource.inFire.equals(source)) {
                if (!this.isBurning()) {
                    this.setFire(5);
                } else {
                    this.func_175406_a(0.15f);
                }
                return false;
            }
            if (DamageSource.onFire.equals(source) && this.getHealth() > 0.5f) {
                this.func_175406_a(4.0f);
                return false;
            }
            boolean var3 = "arrow".equals(source.getDamageType());
            boolean var4 = "player".equals(source.getDamageType());
            if (!var4 && !var3) {
                return false;
            }
            if (source.getSourceOfDamage() instanceof EntityArrow) {
                source.getSourceOfDamage().setDead();
            }
            if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit) {
                return false;
            }
            if (source.func_180136_u()) {
                this.playParticles();
                this.setDead();
                return false;
            }
            long var5 = this.worldObj.getTotalWorldTime();
            if (var5 - this.field_175437_i > 5 && !var3) {
                this.field_175437_i = var5;
            } else {
                this.dropBlock();
                this.playParticles();
                this.setDead();
            }
            return false;
        }
        return false;
    }

    private void playParticles() {
        if (this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).func_175739_a(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5, this.posZ, 10, this.width / 4.0f, this.height / 4.0f, this.width / 4.0f, 0.05, Block.getStateId(Blocks.planks.getDefaultState()));
        }
    }

    private void func_175406_a(float p_175406_1_) {
        float var2 = this.getHealth();
        if ((var2 -= p_175406_1_) <= 0.5f) {
            this.dropContents();
            this.setDead();
        } else {
            this.setHealth(var2);
        }
    }

    private void dropBlock() {
        Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
        this.dropContents();
    }

    private void dropContents() {
        int var1 = 0;
        while (var1 < this.contents.length) {
            if (this.contents[var1] != null && this.contents[var1].stackSize > 0) {
                if (this.contents[var1] != null) {
                    Block.spawnAsEntity(this.worldObj, new BlockPos(this).offsetUp(), this.contents[var1]);
                }
                this.contents[var1] = null;
            }
            ++var1;
        }
    }

    @Override
    protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0f;
    }

    @Override
    public float getEyeHeight() {
        return this.isChild() ? this.height * 0.5f : this.height * 0.9f;
    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        if (!this.hasNoGravity()) {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
    }

    @Override
    public void onUpdate() {
        Rotations var3;
        Rotations var4;
        Rotations var2;
        Rotations var5;
        Rotations var6;
        super.onUpdate();
        Rotations var1 = this.dataWatcher.getWatchableObjectRotations(11);
        if (!this.headRotation.equals(var1)) {
            this.setHeadRotation(var1);
        }
        if (!this.bodyRotation.equals(var2 = this.dataWatcher.getWatchableObjectRotations(12))) {
            this.setBodyRotation(var2);
        }
        if (!this.leftArmRotation.equals(var3 = this.dataWatcher.getWatchableObjectRotations(13))) {
            this.setLeftArmRotation(var3);
        }
        if (!this.rightArmRotation.equals(var4 = this.dataWatcher.getWatchableObjectRotations(14))) {
            this.setRightArmRotation(var4);
        }
        if (!this.leftLegRotation.equals(var5 = this.dataWatcher.getWatchableObjectRotations(15))) {
            this.setLeftLegRotation(var5);
        }
        if (!this.rightLegRotation.equals(var6 = this.dataWatcher.getWatchableObjectRotations(16))) {
            this.setRightLegRotation(var6);
        }
    }

    @Override
    protected void func_175135_B() {
        this.setInvisible(this.canInteract);
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.canInteract = invisible;
        super.setInvisible(invisible);
    }

    @Override
    public boolean isChild() {
        return this.isSmall();
    }

    @Override
    public void func_174812_G() {
        this.setDead();
    }

    @Override
    public boolean func_180427_aV() {
        return this.isInvisible();
    }

    private void setSmall(boolean p_175420_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);
        var2 = p_175420_1_ ? (byte)(var2 | 1) : (byte)(var2 & -2);
        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean isSmall() {
        if ((this.dataWatcher.getWatchableObjectByte(10) & 1) != 0) {
            return true;
        }
        return false;
    }

    private void setNoGravity(boolean p_175425_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);
        var2 = p_175425_1_ ? (byte)(var2 | 2) : (byte)(var2 & -3);
        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean hasNoGravity() {
        if ((this.dataWatcher.getWatchableObjectByte(10) & 2) != 0) {
            return true;
        }
        return false;
    }

    private void setShowArms(boolean p_175413_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);
        var2 = p_175413_1_ ? (byte)(var2 | 4) : (byte)(var2 & -5);
        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean getShowArms() {
        if ((this.dataWatcher.getWatchableObjectByte(10) & 4) != 0) {
            return true;
        }
        return false;
    }

    private void setNoBasePlate(boolean p_175426_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);
        var2 = p_175426_1_ ? (byte)(var2 | 8) : (byte)(var2 & -9);
        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean hasNoBasePlate() {
        if ((this.dataWatcher.getWatchableObjectByte(10) & 8) != 0) {
            return true;
        }
        return false;
    }

    public void setHeadRotation(Rotations p_175415_1_) {
        this.headRotation = p_175415_1_;
        this.dataWatcher.updateObject(11, p_175415_1_);
    }

    public void setBodyRotation(Rotations p_175424_1_) {
        this.bodyRotation = p_175424_1_;
        this.dataWatcher.updateObject(12, p_175424_1_);
    }

    public void setLeftArmRotation(Rotations p_175405_1_) {
        this.leftArmRotation = p_175405_1_;
        this.dataWatcher.updateObject(13, p_175405_1_);
    }

    public void setRightArmRotation(Rotations p_175428_1_) {
        this.rightArmRotation = p_175428_1_;
        this.dataWatcher.updateObject(14, p_175428_1_);
    }

    public void setLeftLegRotation(Rotations p_175417_1_) {
        this.leftLegRotation = p_175417_1_;
        this.dataWatcher.updateObject(15, p_175417_1_);
    }

    public void setRightLegRotation(Rotations p_175427_1_) {
        this.rightLegRotation = p_175427_1_;
        this.dataWatcher.updateObject(16, p_175427_1_);
    }

    public Rotations getHeadRotation() {
        return this.headRotation;
    }

    public Rotations getBodyRotation() {
        return this.bodyRotation;
    }

    public Rotations getLeftArmRotation() {
        return this.leftArmRotation;
    }

    public Rotations getRightArmRotation() {
        return this.rightArmRotation;
    }

    public Rotations getLeftLegRotation() {
        return this.leftLegRotation;
    }

    public Rotations getRightLegRotation() {
        return this.rightLegRotation;
    }
}

