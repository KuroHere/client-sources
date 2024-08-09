package wtf.resolute.moduled.impl.misc;


import com.google.common.eventbus.Subscribe;
import wtf.resolute.evented.EventPacket;
import wtf.resolute.evented.EventUpdate;
import wtf.resolute.evented.interfaces.Event;
import wtf.resolute.moduled.Categories;
import wtf.resolute.moduled.Module;
import wtf.resolute.moduled.ModuleAnontion;
import wtf.resolute.moduled.settings.impl.StringSetting;
import wtf.resolute.utiled.client.ClientUtil;
import wtf.resolute.utiled.math.StopWatch;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

// ��������
@ModuleAnontion(name = "AutoTransfer", type = Categories.Misc,server = "FT")
public class AutoTransfer extends Module {

    private final StringSetting anarchyNumberSetting = new StringSetting("�������", "", "������� ����� ������� \n��� �������� �����", true);
    private final StringSetting itemsCountSetting = new StringSetting("���������� ���������", "", "������� ���������� ��������� ��� �������", true);
    private final StringSetting sellPriceSetting = new StringSetting("����", "", "������� ���� (�� 10$)", true);

    private final StopWatch stopWatch = new StopWatch(), changeServerStopWatch = new StopWatch();
    private boolean allItemsToSell = false;
    private boolean connectedToServer = false;
    private final List<Item> playerItems = new ArrayList<>();
    private int sellCount = 0;
    private boolean isReadyToSell;

    public AutoTransfer() {
        addSettings(anarchyNumberSetting, itemsCountSetting, sellPriceSetting);
    }

    @Subscribe
    private void onPacket(EventPacket packetEvent) {
        if (!ClientUtil.isConnectedToServer("funtime")) return;

        if (packetEvent.getPacket() instanceof SChatPacket chatPacket) {
            String chatMessage = chatPacket.getChatComponent().getString().toLowerCase(Locale.ROOT);
            if (chatMessage.contains("���������� ���������") && !playerItems.isEmpty()) allItemsToSell = true;
            if (chatMessage.contains("�� ��� ����������")) connectedToServer = true;
            if (chatMessage.contains("��������� �� �������")) {
                sellCount++;
            }
        }
    }

    List<ItemStack> stacks = new LinkedList<>();

    @Subscribe
    private void onUpdate(EventUpdate updateEvent) {
        if (mc.player.ticksExisted < 500 && !isReadyToSell) {
            int ticksRemaining = 500 - mc.player.ticksExisted;
            int secondsRemaining = ticksRemaining / 20;
            print("��������� ��� " + TextFormatting.RED + secondsRemaining + TextFormatting.RESET + " ������(�), ������ ��� ������������");
            toggle();
            return;
        }

        if (mc.ingameGUI.getTabList().header != null) {
            String serverHeader = TextFormatting.getTextWithoutFormattingCodes(mc.ingameGUI.getTabList().header.getString());
            if (serverHeader != null && serverHeader.contains(anarchyNumberSetting.get())) connectedToServer = true;
        }

        int itemCountToSell = Integer.parseInt(itemsCountSetting.get());

        if (itemCountToSell > 9) {
            print("���-�� ��������� �� ������ ��������� 9!");
            toggle();
            return;
        }

        int sellPrice = Integer.parseInt(sellPriceSetting.get());

        if (!isReadyToSell) {
            for (int i = 0; i < 9; i++) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
                    continue;
                }
                if (stopWatch.isReached(100)) {
                    mc.player.inventory.currentItem = i;
                    mc.player.sendChatMessage("/ah dsell " + sellPrice);
                    playerItems.add(mc.player.inventory.getStackInSlot(i).getItem());
                    stopWatch.reset();
                }
            }
        }

        if (sellCount >= itemCountToSell || allItemsToSell) {
            isReadyToSell = true;
            int anarchyNumber = Integer.parseInt(anarchyNumberSetting.get());

            if (!connectedToServer) {
                if (changeServerStopWatch.isReached(100)) {
                    mc.player.sendChatMessage("/an" + anarchyNumber);
                    changeServerStopWatch.reset();
                }
                return;
            }

            if (mc.player.openContainer instanceof ChestContainer container) {
                IInventory lowerChestInventory = container.getLowerChestInventory();

                for (int index = 0; index < lowerChestInventory.getSizeInventory(); ++index) {
                    if (stopWatch.isReached(200) && lowerChestInventory.getStackInSlot(index).getItem() != Items.AIR) {
                        if (playerItems.contains(lowerChestInventory.getStackInSlot(index).getItem())) {
                            mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                            stopWatch.reset();
                        } else {
                            resetAndToggle();
                            toggle();
                        }
                    }
                }
            } else {
                if (stopWatch.isReached(500)) {
                    mc.player.sendChatMessage("/ah " + mc.player.getNameClear());
                    stopWatch.reset();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        resetAndToggle();
        super.onDisable();
    }

    private void resetAndToggle() {
        allItemsToSell = false;
        connectedToServer = false;
        playerItems.clear();
        isReadyToSell = false;
        sellCount = 0;
    }
}