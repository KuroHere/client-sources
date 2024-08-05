package me.darki.konas.util.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class FreecamCamera extends EntityPlayerSP {

    private final Minecraft mc = Minecraft.getMinecraft();

    private boolean copyInventory;
    private boolean follow;

    private float hSpeed;
    private float vSpeed;

    public FreecamCamera(World world) {
        super(Minecraft.getMinecraft(), world, Minecraft.getMinecraft().getConnection(), Minecraft.getMinecraft().player.getStatFileWriter(), Minecraft.getMinecraft().player.getRecipeBook());
    }

    public FreecamCamera(boolean copyInventory, boolean follow, float hSpeed, float vSpeed) {
        super(Minecraft.getMinecraft(), Minecraft.getMinecraft().world, Minecraft.getMinecraft().getConnection(), Minecraft.getMinecraft().player.getStatFileWriter(), Minecraft.getMinecraft().player.getRecipeBook());
        this.copyInventory = copyInventory;
        this.follow = follow;
        this.hSpeed = hSpeed;
        this.vSpeed = vSpeed;
        this.noClip = true;
        this.setHealth(mc.player.getHealth());
        this.posX = mc.player.posX;
        this.posY = mc.player.posY;
        this.posZ = mc.player.posZ;
        this.prevPosX = mc.player.prevPosX;
        this.prevPosY = mc.player.prevPosY;
        this.prevPosZ = mc.player.prevPosZ;
        this.lastTickPosX = mc.player.lastTickPosX;
        this.lastTickPosY = mc.player.lastTickPosY;
        this.lastTickPosZ = mc.player.lastTickPosZ;
        this.rotationYaw = mc.player.rotationYaw;
        this.rotationPitch = mc.player.rotationPitch;
        this.rotationYawHead = mc.player.rotationYawHead;
        this.prevRotationYaw = mc.player.prevRotationYaw;
        this.prevRotationPitch = mc.player.prevRotationPitch;
        this.prevRotationYawHead = mc.player.prevRotationYawHead;
        if(this.copyInventory) {
            this.inventory = mc.player.inventory;
            this.inventoryContainer = mc.player.inventoryContainer;
            this.setHeldItem(EnumHand.MAIN_HAND, mc.player.getHeldItemMainhand());
            this.setHeldItem(EnumHand.OFF_HAND, mc.player.getHeldItemOffhand());
        }
        NBTTagCompound compound = new NBTTagCompound();
        mc.player.capabilities.writeCapabilitiesToNBT(compound);
        this.capabilities.readCapabilitiesFromNBT(compound);
        this.capabilities.isFlying = true;
        this.attackedAtYaw = mc.player.attackedAtYaw;
        this.movementInput = new MovementInputFromOptions(mc.gameSettings);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {}

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {}

    @Override
    public boolean isInsideOfMaterial(@NotNull Material material) {
        return mc.player.isInsideOfMaterial(material);
    }

    @NotNull
    @Override
    public Map<Potion, PotionEffect> getActivePotionMap() {
        return mc.player.getActivePotionMap();
    }

    @NotNull
    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return mc.player.getActivePotionEffects();
    }

    @Override
    public int getTotalArmorValue() {
        return mc.player.getTotalArmorValue();
    }

    @Override
    public float getAbsorptionAmount() {
        return mc.player.getAbsorptionAmount();
    }

    @Override
    public boolean isPotionActive(@NotNull Potion potion) {
        return mc.player.isPotionActive(potion);
    }

    @Override
    public PotionEffect getActivePotionEffect(@NotNull Potion potion) {
        return mc.player.getActivePotionEffect(potion);
    }

    @NotNull
    @Override
    public FoodStats getFoodStats() {
        return mc.player.getFoodStats();
    }

    @Override
    public boolean canTriggerWalking() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return null;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    @NotNull
    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void applyEntityCollision(@NotNull Entity entity) {}

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBeRidden(@NotNull Entity entity) {
        return false;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public boolean canTrample(@NotNull World world, @NotNull Block block, @NotNull BlockPos pos, float fallDistance) {
        return false;
    }

    @Override
    public void doBlockCollisions() {}

    @Override
    public void updateFallState(double y, boolean onGroundIn, @NotNull IBlockState state, @NotNull BlockPos pos) {}

    @Override
    public boolean getIsInvulnerable() {
        return true;
    }

    @NotNull
    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.movementInput.updatePlayerMoveState();
        float up = this.movementInput.jump ? 1f :this.movementInput.sneak ? -1f : 0f;
        setMotion(this.movementInput.moveStrafe, up, this.movementInput.moveForward);
        if (mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.motionX *= 2;
            this.motionY *= 2;
            this.motionZ *= 2;
            this.setSprinting(true);
        } else {
            this.setSprinting(false);
        }
        if (follow) {
            if (Math.abs(this.motionX) <= 1e-8f) {
                this.posX += (mc.player.posX - mc.player.prevPosX);
            }
            if (Math.abs(this.motionY) <= 1e-8f) {
                this.motionY += (mc.player.posY - mc.player.prevPosY);
            }
            if (Math.abs(this.motionZ) <= 1e-8f) {
                this.motionZ += (mc.player.posZ - mc.player.prevPosZ);
            }
        }
        this.setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }

    public void setMotion(float strafe, float up, float forward) {
        float f = strafe * strafe + up * up + forward * forward;
        if(f >= 1.0E-4f) {
            f = MathHelper.sqrt(f);
            if (f < 1.0f) f = 1.0f;
            f /= 2f;
            strafe *= f;
            up *= f;
            forward *= f;

            float f1 = MathHelper.sin(rotationYaw * 0.017453292f);
            float f2 = MathHelper.cos(rotationYaw * 0.017453292f);
            motionX = (strafe * f2 - forward * f1) * hSpeed;
            motionY = (double) up * vSpeed;
            motionZ = (forward * f2 + strafe * f1) * hSpeed;
        }

    }

    public boolean isCopyInventory() {
        return copyInventory;
    }

    public void setCopyInventory(boolean copyInventory) {
        this.copyInventory = copyInventory;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public float gethSpeed() {
        return hSpeed;
    }

    public void sethSpeed(float hSpeed) {
        this.hSpeed = hSpeed;
    }

    public float getvSpeed() {
        return vSpeed;
    }

    public void setvSpeed(float vSpeed) {
        this.vSpeed = vSpeed;
    }
}