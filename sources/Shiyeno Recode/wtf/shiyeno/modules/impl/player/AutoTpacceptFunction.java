package wtf.shiyeno.modules.impl.player;

import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.TextFormatting;
import wtf.shiyeno.events.Event;
import wtf.shiyeno.events.impl.packet.EventPacket;
import wtf.shiyeno.friend.Friend;
import wtf.shiyeno.managment.Managment;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.modules.settings.imp.BooleanOption;

import java.util.Arrays;

@FunctionAnnotation(name = "AutoTPAccept", type = Type.Player)
public class AutoTpacceptFunction extends Function {
    private final BooleanOption onlyfriends = new BooleanOption("������ ������",
            "��������� ������� ������ �� ������", false);

    private final String[] teleportMessages = new String[]{"has requested teleport", "������ �����������������", "������ � ��� �����������������"};

    public AutoTpacceptFunction() {
        addSettings(onlyfriends);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket packetEvent) {
            if (packetEvent.isReceivePacket()) {
                if (packetEvent.getPacket() instanceof SChatPacket packetChat) {
                    handleReceivePacket(packetChat);
                }
            }
        }
    }

    /**
     * ������������ ���������� ����� ����.
     *
     * @param packet ����� ����
     */
    private void handleReceivePacket(SChatPacket packet) {
        String message = TextFormatting.getTextWithoutFormattingCodes(packet.getChatComponent().getString());

        if (isTeleportMessage(message)) {
            if (onlyFriendsEnabled()) {
                handleTeleportWithFriends(message);
                return;
            }
            acceptTeleport();

        }
    }

    /**
     * ���������, �������� �� ��������� ������� ������������.
     *
     * @param message ��������� ����
     * @return true, ���� ��������� �������� ������� ������������, ����� false
     */
    private boolean isTeleportMessage(String message) {
        return Arrays.stream(this.teleportMessages)
                .map(String::toLowerCase)
                .anyMatch(message::contains);
    }

    /**
     * ���������, �������� �� ����� "������ ��� ������".
     *
     * @return true, ���� ����� "������ ��� ������" ��������, ����� false
     */
    private boolean onlyFriendsEnabled() {
        return onlyfriends.get();
    }

    /**
     * ������������ ����� ������������, ����� �������� ����� "������ ��� ������".
     *
     * @param message ��������� ����
     */
    private void handleTeleportWithFriends(String message) {
        for (Friend friend : Managment.FRIEND_MANAGER.getFriends()) {

            StringBuilder builder = new StringBuilder();
            char[] buffer = message.toCharArray();
            for (int w = 0; w < buffer.length; w++) {
                char c = buffer[w];
                if (c == '�') {
                    w++;
                } else {
                    builder.append(c);
                }
            }

            if (builder.toString().contains(friend.getName()))
                acceptTeleport();
        }
    }

    /**
     * ���������� ������� ��� �������� ������������.
     */
    private void acceptTeleport() {
        mc.player.sendChatMessage("/tpaccept");
    }
}