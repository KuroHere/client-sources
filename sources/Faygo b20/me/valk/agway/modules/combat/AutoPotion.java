package me.valk.agway.modules.combat;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.valk.event.EventListener;
import me.valk.event.EventPriority;
import me.valk.event.EventPriorityListener;
import me.valk.event.EventType;
import me.valk.event.events.player.EventMotion;
import me.valk.module.ModData;
import me.valk.module.ModType;
import me.valk.module.Module;
import me.valk.utils.value.RestrictedValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoPotion extends Module {
	public static int ticks = 0;
	private RestrictedValue<Integer> health = new RestrictedValue<Integer>("health", 3, 2, 19);

	private ItemThing potSlot;

	public AutoPotion() {
		super(new ModData("AutoPotion", Keyboard.KEY_NONE, new Color(138, 8, 26)), ModType.COMBAT);
		addValue(health);
	
	}

	@EventPriorityListener(priority = EventPriority.HIGH)
	public void onMotion(EventMotion event) {
			setDisplayName(getName() + "�7 " + getCount());
		if (ticks > 0) {
			ticks--;
			return;
		}
		if (event.getType() == EventType.PRE) {
			potSlot = null;
			ItemThing temp = getHealingItemFromInventory();
			if (ticks == 0 && p.getHealth() <= health.getValue() && temp.getSlot() != -1) {
				event.breakEvent();
			 
					event.getLocation().setPitch(90);
				
				potSlot = temp;
			}
		} else {
			if (potSlot != null) {
				if (potSlot.getSlot() < 9) {
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(potSlot.getSlot()));
					mc.thePlayer.sendQueue
							.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
					mc.thePlayer.sendQueue
							.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				} else {
					swap(potSlot.getSlot(), 5);
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(5));
					mc.thePlayer.sendQueue
							.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
					
				}
				ticks =  10;
				event.breakEvent();
			}
		}
	}

	public int getCount() {
		int pot = -1;
		int counter = 0;
		for (int i = 0; i < 36; ++i) {
			if (mc.thePlayer.inventory.mainInventory[i] != null) {
				ItemStack is = mc.thePlayer.inventory.mainInventory[i];
				Item item = is.getItem();
				if (item instanceof ItemPotion) {
					ItemPotion potion = (ItemPotion) item;
					if (potion.getEffects(is) != null) {
						for (Object o : potion.getEffects(is)) {
							PotionEffect effect = (PotionEffect) o;
							if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
								++counter;
							}
						}
					}
				}

				
			}
		}

		return counter;
	}

	private ItemThing getHealingItemFromInventory() {
		int itemSlot = -1;
		int counter = 0;
		boolean soup = false;
		for (int i = 0; i < 36; ++i) {
			if (mc.thePlayer.inventory.mainInventory[i] != null) {
				ItemStack is = mc.thePlayer.inventory.mainInventory[i];
				Item item = is.getItem();
				if (item instanceof ItemPotion) {
					ItemPotion potion = (ItemPotion) item;
					if (potion.getEffects(is) != null) {
						for (Object o : potion.getEffects(is)) {
							PotionEffect effect = (PotionEffect) o;
							if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
								++counter;
								itemSlot = i;
								soup = false;
							}
						}
					}
				}

				if (item instanceof ItemSoup) {
					counter++;
					itemSlot = i;
					soup = true;
				}
			}
		}

		return new ItemThing(itemSlot, soup);
	}

	private void swap(int slot, int hotbarSlot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarSlot, 2, mc.thePlayer);
	}

	public class ItemThing {

		private boolean soup;
		private int slot;

		public ItemThing(int slot, boolean soup) {
			this.slot = slot;
			this.soup = soup;
		}

		public int getSlot() {
			return slot;
		}

		public boolean isSoup() {
			return soup;
		}

	}

}

