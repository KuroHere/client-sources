package me.r.touchgrass.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import me.r.touchgrass.module.Category;
import me.r.touchgrass.module.Info;
import me.r.touchgrass.module.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import me.r.touchgrass.events.EventUpdate;
import me.r.touchgrass.settings.Setting;
import me.r.touchgrass.utils.TimeUtils;

/**
 * Created by r on 08/02/2021
 */
@Info(name = "ChestStealer", description = "Steals items from chests", category = Category.Player)
public class ChestStealer extends Module {
    public ChestStealer() {
        addSetting(new Setting("Delay", this, 100.0, 0.0, 1000.0, false));
        addSetting(new Setting("Auto Close", this, false));
    }

    final TimeUtils delay = new TimeUtils();

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.currentScreen instanceof GuiChest && !(mc.currentScreen instanceof GuiInventory) && !(mc.currentScreen instanceof GuiContainerCreative)) {

            GuiChest chest = (GuiChest) mc.currentScreen;

            boolean empty = true;
            for (int i = 0; i < chest.lowerChestInventory.getSizeInventory(); i++) {
                ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);
                if (stack != null) {
                    empty = false;
                    break;
                }
            }
            if (empty && h2.settingsManager.getSettingByName("Auto Close").isEnabled()) {
                mc.thePlayer.closeScreen();
                return;
            }
            for (int i = 0; i < chest.lowerChestInventory.getSizeInventory(); i++) {
                ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);
                if (stack != null) {
                    if ((delay.isDelayComplete((long) h2.settingsManager.getSettingByName(this, "Delay").getValue() / 2))) {
                        mc.playerController.windowClick(chest.inventorySlots.windowId, Integer.valueOf(i).intValue(), 0,
                                1, mc.thePlayer);
                        delay.setLastMS();
                    }
                }
            }
        }
    }
}
