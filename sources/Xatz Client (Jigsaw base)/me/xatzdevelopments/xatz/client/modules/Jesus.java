package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.PreMotionEvent;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.Timer.WaitTimer;
import net.minecraft.block.material.Material;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.potion.Potion;

public class Jesus extends Module {

	private double moveSpeed;
	private WaitTimer timer = new WaitTimer();

	public Jesus() {
		super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to walk on liquids");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		timer.reset();
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (Utils.getBlockRelativeToEntity(mc.thePlayer, -0.1).getMaterial() == Material.water) {

		}

		super.onUpdate();
	}

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {

		super.onPacketRecieved(packetIn);
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C03PacketPlayer) {
			if (mc.gameSettings.keyBindJump.pressed) {
				if (Utils.getBlockRelativeToEntity(mc.thePlayer, -0.1).getMaterial() == Material.water) {
					// Xatz.chatMessage("LOL");
					packet.cancel();
					C06PacketPlayerPosLook playerPacket = new C06PacketPlayerPosLook();
					playerPacket.rotating = true;
					playerPacket.moving = true;
					playerPacket.x = mc.thePlayer.posX;
					playerPacket.y = mc.thePlayer.posY - 0.005;
					playerPacket.z = mc.thePlayer.posZ;
					playerPacket.onGround = mc.thePlayer.onGround;
					playerPacket.yaw = mc.thePlayer.rotationYaw;
					playerPacket.pitch = mc.thePlayer.rotationPitch;
					sendPacketFinal(playerPacket);
				}
			} else {
				if (Utils.getBlockRelativeToEntity(mc.thePlayer, -0.1).getMaterial() == Material.water
						&& timer.hasTimeElapsed(100, false)) {
					// Xatz.chatMessage("LOL");
					packet.cancel();
					C06PacketPlayerPosLook playerPacket = new C06PacketPlayerPosLook();
					playerPacket.rotating = true;
					playerPacket.moving = true;
					playerPacket.x = mc.thePlayer.posX;
					playerPacket.y = mc.thePlayer.posY - 0.005;
					playerPacket.z = mc.thePlayer.posZ;
					playerPacket.onGround = mc.thePlayer.onGround;
					playerPacket.yaw = mc.thePlayer.rotationYaw;
					playerPacket.pitch = mc.thePlayer.rotationPitch;
					sendPacketFinal(playerPacket);
					timer.reset();
				}
			}
			if (Utils.getBlockRelativeToEntity(mc.thePlayer, 0.1).getMaterial() != Material.water) {

			}

		}
		super.onPacketSent(packet);
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {

		super.onPreMotion(event);
	}

	@Override
	public String[] getModes() {
		return super.getModes();
	}
	public String getModeName() {
		return "Mode: ";
	}

	@Override
	public String getAddonText() {
		return super.getAddonText();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

}
