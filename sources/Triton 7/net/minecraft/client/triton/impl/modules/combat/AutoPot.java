package net.minecraft.client.triton.impl.modules.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.triton.impl.modules.movement.Speed;
import net.minecraft.client.triton.management.event.EventTarget;
import net.minecraft.client.triton.management.event.events.UpdateEvent;
import net.minecraft.client.triton.management.module.Module;
import net.minecraft.client.triton.management.module.Module.Mod;
import net.minecraft.client.triton.management.option.Option.Op;
import net.minecraft.client.triton.management.option.OptionManager;
import net.minecraft.client.triton.management.option.types.BooleanOption;
import net.minecraft.client.triton.utils.ClientUtils;
import net.minecraft.client.triton.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@Mod(displayName = "Auto Potion")
public class AutoPot extends Module {
	@Op(min = 0.5, max = 10.0, increment = 0.5)
	public static double health;
	@Op(min = 200.0, max = 1000.0, increment = 50.0)
	public static double delay;
	@Op(min = 0.0, max = 9.0, increment = 1.0)
	private static double slot;
	@Op
	private boolean compatible;
	private int haltTicks;
	public static Timer timer;
	public static boolean potting;
	public static boolean potNextCompat;
	public static boolean swapTarget;
	private double x;
	private double y;
	private double z;

	static {
		AutoPot.health = 4.5;
		AutoPot.delay = 250.0;
		AutoPot.timer = new Timer();
	}

	public AutoPot() {
		this.slot = 6.0;
		this.compatible = true;
		this.haltTicks = -1;
	}

	@Override
	public void enable() {
		this.haltTicks = -1;
		super.enable();
	}

	@EventTarget(3)
	private void onUpdate(final UpdateEvent event) {
		Aura auraMod = new Aura();
		auraMod = (Aura) auraMod.getInstance();
		if (event.getState() == event.getState().PRE) {
			boolean targets = false;
			final int nearbyEntities = 0;
			for (final Entity entity : ClientUtils.loadedEntityList()) {
				if (auraMod.isEntityValid(entity)) {
					targets = true;
					break;
				}
			}
			boolean check = AutoPot.timer.delay((float) AutoPot.delay);
			if (this.compatible && auraMod.isEnabled() && targets
					&& ((BooleanOption) OptionManager.getOption("Tick", "Aura")).getValue()) {
				check = AutoPot.potNextCompat;
			}
			if (getPotionFromInventory() != -1 && ClientUtils.player().getHealth() <= AutoPot.health * 2.0 && check) {
				if (ClientUtils.player().isCollidedVertically && !new Speed().getInstance().isEnabled()) {
					AutoPot.swapTarget = true;
					AutoPot.timer.reset();
					ClientUtils.packet(new C03PacketPlayer.C06PacketPlayerPosLook(ClientUtils.x(), ClientUtils.y(),
							ClientUtils.z(), ClientUtils.yaw(), -90.0f, true));
					this.swap(getPotionFromInventory(), (int) this.slot);
					ClientUtils.packet(new C09PacketHeldItemChange((int) this.slot));
					ClientUtils
							.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
					ClientUtils.packet(new C09PacketHeldItemChange(ClientUtils.player().inventory.currentItem));
					ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(),
							ClientUtils.y() + 0.42, ClientUtils.z(), true));
					ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(),
							ClientUtils.y() + 0.75, ClientUtils.z(), true));
					ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(),
							ClientUtils.y() + 1.0, ClientUtils.z(), true));
					ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(),
							ClientUtils.y() + 1.16, ClientUtils.z(), true));
					ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(),
							ClientUtils.y() + 1.24, ClientUtils.z(), true));
					this.x = ClientUtils.x();
					this.y = ClientUtils.y() + 1.24;
					this.z = ClientUtils.z();
					this.haltTicks = 5;
				} else {
					event.setAlwaysSend(true);
					event.setPitch(90.0f);
					AutoPot.potting = true;
					AutoPot.swapTarget = true;
					AutoPot.timer.reset();
				}
			}
			if (this.haltTicks >= 0) {
				event.setCancelled(true);
			}
			if (this.haltTicks == 0) {
				final EntityPlayerSP player = ClientUtils.player();
				final EntityPlayerSP player2 = ClientUtils.player();
				final double n = 0.0;
				player2.motionZ = n;
				player.motionX = n;
				ClientUtils.player().setPositionAndUpdate(this.x, this.y, this.z);
				ClientUtils.player().motionY = -0.08;
			}
			--this.haltTicks;

		}
		if (event.getState() == event.getState().POST) {
			final int potSlot = getPotionFromInventory();
			if (AutoPot.potting) {
				this.swap(getPotionFromInventory(), (int) this.slot);
				ClientUtils.packet(new C09PacketHeldItemChange((int) this.slot));
				ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
				ClientUtils.packet(new C09PacketHeldItemChange(ClientUtils.player().inventory.currentItem));
				AutoPot.timer.reset();
				AutoPot.potting = false;
			}
		}
		if (event.getState() == event.getState().PRE && AutoPot.potNextCompat) {
			AutoPot.potNextCompat = false;
		}
	}

	protected void swap(final int slot, final int hotbarNum) {
		ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, slot, hotbarNum, 2,
				ClientUtils.player());
	}

	public static int getPotionFromInventory() {
		int pot = -1;
		int counter = 0;
		for (int i = 1; i < 45; ++i) {
			if (ClientUtils.player().inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = ClientUtils.player().inventoryContainer.getSlot(i).getStack();
				final Item item = is.getItem();
				if (item instanceof ItemPotion) {
					final ItemPotion potion = (ItemPotion) item;
					if (potion.getEffects(is) != null) {
						for (final Object o : potion.getEffects(is)) {
							final PotionEffect effect = (PotionEffect) o;
							if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
								++counter;
								pot = i;
							}
						}
					}
				}
			}
		}
		new AutoPot().getInstance().setSuffix(new StringBuilder(String.valueOf(counter)).toString());
		return pot;
	}

	@Override
	public void disable() {
		AutoPot.potting = false;
		super.disable();
	}

}
