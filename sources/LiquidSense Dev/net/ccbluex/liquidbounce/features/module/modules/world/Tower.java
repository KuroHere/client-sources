/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.*;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.stats.StatList;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.Vec3;

import java.awt.*;

@ModuleInfo(name = "Tower", description = "Automatically builds a tower beneath you.", category = ModuleCategory.WORLD, keyBind = Keyboard.KEY_O)
public class Tower extends Module {

    /**
     * OPTIONS
     */

    private final ListValue modeValue = new ListValue("Mode", new String[] {"Hypixel","Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9"}, "Motion");
    private final BoolValue autoBlockValue = new BoolValue("AutoBlock", true);
    private final BoolValue stayAutoBlock = new BoolValue("StayAutoBlock", false);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false);
    private final BoolValue onJumpValue = new BoolValue("OnJump", false);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");

    private final FloatValue timerValue = new FloatValue("Timer", 1F, 0F, 10F);

    // Jump mode
    private final FloatValue jumpMotionValue = new FloatValue("JumpMotion", 0.42F, 0.3681289F, 0.79F);
    private final IntegerValue jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);

    // ConstantMotion
    private final FloatValue constantMotionValue = new FloatValue("ConstantMotion", 0.42F, 0.1F, 1F);
    private final FloatValue constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79F, 0.76F, 1F);

    // Teleport
    private final FloatValue teleportHeightValue = new FloatValue("TeleportHeight", 1.15F, 0.1F, 5F);
    private final IntegerValue teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
    private final BoolValue teleportGroundValue = new BoolValue("TeleportGround", true);
    private final BoolValue teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);

    // Render
    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);

    /**
     * MODULE
     */

    // Target block
    private PlaceInfo placeInfo;

    // Rotation lock
    private Rotation lockRotation;

    // Mode stuff
    private final TickTimer timer = new TickTimer();
    private double jumpGround = 0;

    // AutoBlock
    private int slot;

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;

        mc.timer.timerSpeed = 1F;
        lockRotation = null;

        if (slot != mc.thePlayer.inventory.currentItem)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (onJumpValue.get() && !mc.gameSettings.keyBindJump.isKeyDown())
            return;

        // Lock Rotation
        if (rotationsValue.get() && keepRotationValue.get() && lockRotation != null)
            RotationUtils.setTargetRotation(lockRotation);

        mc.timer.timerSpeed = timerValue.get();

        final EventState eventState = event.getEventState();

        if (placeModeValue.get().equalsIgnoreCase(eventState.getStateName()))
            place();

        if (eventState == EventState.PRE) {
            placeInfo = null;
            timer.update();

            if (autoBlockValue.get() ? InventoryUtils.findAutoBlockBlock() != -1 : mc.thePlayer.getHeldItem() != null
                    && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                if (!stopWhenBlockAbove.get() || BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX,
                        mc.thePlayer.posY + 2, mc.thePlayer.posZ)) instanceof BlockAir)
                    move();

                final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
                if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockAir) {
                    if (search(blockPos) && rotationsValue.get()) {
                        final VecRotation vecRotation = RotationUtils.faceBlock(blockPos);

                        if (vecRotation != null) {
                            RotationUtils.setTargetRotation(vecRotation.getRotation());
                            placeInfo.setVec3(vecRotation.getVec());
                        }
                    }
                }
            }
        }
    }

    //Send jump packets, bypasses Hypixel.
    private void fakeJump() {
        mc.thePlayer.isAirBorne = true;
        mc.thePlayer.triggerAchievement(StatList.jumpStat);
    }

    /**
     * Move player
     */
    private void move() {
        switch (modeValue.get().toLowerCase()) {
            case "hypixel":
                if(this.getBlocksAmount() > 0) {
                    if(mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0) {
                        double gay2 = 0.419999215712222222222531351325;
                        double lit = 0.42d;
                        double baseSpeed = 0.2873D;
                        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
                            baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
                        }
                        if (mc.thePlayer.isPotionActive(Potion.jump)) {
                            gay2 += (double) ((float) (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                            lit -= (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.075;
                            baseSpeed -= 0.05 * (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1);

                        }
                        if (mc.thePlayer.motionY < gay2 * lit) {
                            mc.thePlayer.motionY = gay2;
                        }
                        if (mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001) {
                            mc.thePlayer.motionY = 0;
                        }

                    }else {
                        double gay2 = 0.419999215712222222222531351325;
                        double lit = 0.42d;
                        if (mc.thePlayer.isPotionActive(Potion.jump)) {
                            gay2 += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                            lit -= (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1)*0.075;
                        }
                        if(mc.thePlayer.motionY <gay2*lit){
                            mc.thePlayer.motionY=gay2;
                        }

                    }
                }
                break;
            case "jump":
                if (mc.thePlayer.onGround && timer.hasTimePassed(jumpDelayValue.get())) {
                    fakeJump();
                    mc.thePlayer.motionY = jumpMotionValue.get();
                    timer.reset();
                }
                break;
            case "motion":
                if (mc.thePlayer.onGround) {
                    fakeJump();
                    mc.thePlayer.motionY = 0.42D;
                } else if (mc.thePlayer.motionY < 0.1D) mc.thePlayer.motionY = -0.3D;
                break;
            case "motiontp":
                if (mc.thePlayer.onGround) {
                    fakeJump();
                    mc.thePlayer.motionY = 0.42D;
                } else if (mc.thePlayer.motionY < 0.23D)
                    mc.thePlayer.setPosition(mc.thePlayer.posX, (int) mc.thePlayer.posY, mc.thePlayer.posZ);
                break;
            case "packet":
                if (mc.thePlayer.onGround && timer.hasTimePassed(2)) {
                    fakeJump();
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ, false));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 0.753D, mc.thePlayer.posZ, false));
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1D, mc.thePlayer.posZ);
                    timer.reset();
                }
                break;
            case "teleport":
                if (teleportNoMotionValue.get())
                    mc.thePlayer.motionY = 0;

                if ((mc.thePlayer.onGround || !teleportGroundValue.get()) && timer.hasTimePassed(teleportDelayValue.get())) {
                    fakeJump();
                    mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + teleportHeightValue.get(), mc.thePlayer.posZ);
                    timer.reset();
                }
                break;
            case "constantmotion":
                if (mc.thePlayer.onGround) {
                    fakeJump();
                    jumpGround = mc.thePlayer.posY;
                    mc.thePlayer.motionY = constantMotionValue.get();
                }

                if (mc.thePlayer.posY > jumpGround + constantMotionJumpGroundValue.get()) {
                    fakeJump();
                    mc.thePlayer.setPosition(mc.thePlayer.posX, (int) mc.thePlayer.posY, mc.thePlayer.posZ);
                    mc.thePlayer.motionY = constantMotionValue.get();
                    jumpGround = mc.thePlayer.posY;
                }
                break;
            case "aac3.3.9":
                if (mc.thePlayer.onGround) {
                    fakeJump();
                    mc.thePlayer.motionY = 0.4001;
                }
                mc.timer.timerSpeed = 1F;

                if (mc.thePlayer.motionY < 0) {
                    mc.thePlayer.motionY -= 0.00000945;
                    mc.timer.timerSpeed = 1.6F;
                }
                break;
        }
    }

    /**
     * Place target block
     */
    private void place() {
        if(placeInfo == null)
            return;

        // AutoBlock
        int blockSlot = -1;
        ItemStack itemStack = mc.thePlayer.getHeldItem();

        if(mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            if(!autoBlockValue.get())
                return;

            blockSlot = InventoryUtils.findAutoBlockBlock();

            if(blockSlot == -1)
                return;

            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
            itemStack = mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
        }

        // Place block
        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, this.placeInfo.getBlockPos(),
                placeInfo.getEnumFacing(), placeInfo.getVec3())) {
            if(swingValue.get())
                mc.thePlayer.swingItem();
            else
                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
        }
        this.placeInfo = null;

        // Switch back to old slot when using auto block
        if (!stayAutoBlock.get() && blockSlot >= 0)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    /**
     * Search for placeable block
     *
     * @param blockPosition pos
     * @return
     */
    private boolean search(final BlockPos blockPosition) {
        if (!BlockUtils.isReplaceable(blockPosition))
            return false;

        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY +
                mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        PlaceRotation placeRotation = null;

        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = blockPosition.offset(side);

            if (!BlockUtils.canBeClicked(neighbor))
                continue;

            final Vec3 dirVec = new Vec3(side.getDirectionVec());

            for (double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
                for (double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                    for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                        final Vec3 posVec = new Vec3(blockPosition).addVector(xSearch, ySearch, zSearch);
                        final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));

                        if ((eyesPos.squareDistanceTo(hitVec) > 18D ||
                                distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) ||
                                mc.theWorld.rayTraceBlocks(eyesPos, hitVec, false,
                                        true, false) != null))
                            continue;

                        // face block
                        final double diffX = hitVec.xCoord - eyesPos.xCoord;
                        final double diffY = hitVec.yCoord - eyesPos.yCoord;
                        final double diffZ = hitVec.zCoord - eyesPos.zCoord;

                        final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

                        final Rotation rotation = new Rotation(
                                MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                                MathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)))
                        );

                        final Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                        final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4, rotationVector.yCoord * 4, rotationVector.zCoord * 4);
                        final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(eyesPos, vector, false,
                                false, true);

                        if (!(obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && obj.getBlockPos().equals(neighbor)))
                            continue;

                        if (placeRotation == null || RotationUtils.getRotationDifference(rotation) <
                                RotationUtils.getRotationDifference(placeRotation.getRotation()))
                            placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                    }
                }
            }
        }

        if (placeRotation == null)
            return false;

        if (rotationsValue.get()) {
            RotationUtils.setTargetRotation(placeRotation.getRotation(), 0);
            lockRotation = placeRotation.getRotation();
        }

        placeInfo = placeRotation.getPlaceInfo();
        return true;
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        if(mc.thePlayer == null)
            return;

        final Packet<?> packet = event.getPacket();

        if(packet instanceof C09PacketHeldItemChange) {
            final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange) packet;

            slot = packetHeldItemChange.getSlotId();
        }
    }

    /**
     * Tower visuals
     *
     * @param event
     */
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if(counterDisplayValue.get()) {
            GlStateManager.pushMatrix();

            final String info = "Blocks: §7" + getBlocksAmount();
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            RenderUtils.drawBorderedRect((scaledResolution.getScaledWidth() / 2) - 2, (scaledResolution.getScaledHeight() / 2) + 5, (scaledResolution.getScaledWidth() / 2) + Fonts.font40.getStringWidth(info) + 2, (scaledResolution.getScaledHeight() / 2) + 16, 3, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.resetColor();
            Fonts.font40.drawString(info, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 + 7, Color.WHITE.getRGB());

            GlStateManager.popMatrix();
        }
    }

    @EventTarget
    public void onJump(final JumpEvent event) {
        if (onJumpValue.get())
            event.cancelEvent();
    }

    /**
     * @return hotbar blocks amount
     */
    private int getBlocksAmount() {
        int amount = 0;

        for(int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if(itemStack != null && itemStack.getItem() instanceof ItemBlock)
                amount += itemStack.stackSize;
        }

        return amount;
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
