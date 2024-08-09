package wtf.shiyeno.modules.impl.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TextFormatting;
import wtf.shiyeno.events.Event;
import wtf.shiyeno.events.impl.game.EventMouseTick;
import wtf.shiyeno.managment.Managment;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.util.ClientUtil;

@FunctionAnnotation(name = "MiddleClickFriend", type = Type.Util)
public class MiddleClickFriendFunction extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMouseTick e) {
            handleMouseTickEvent(e);
        }
    }

    /**
     * ������������ ������� ������� ������ ����.
     *
     * @param event ������� ������� ������ ����
     */
    private void handleMouseTickEvent(EventMouseTick event) {
        if (event.getButton() == 2 && mc.pointedEntity instanceof LivingEntity) {
            String entityName = mc.pointedEntity.getName().getString();

            if (Managment.FRIEND_MANAGER.isFriend(entityName)) {
                Managment.FRIEND_MANAGER.removeFriend(entityName);
                displayRemoveFriendMessage(entityName);
            } else {
                Managment.FRIEND_MANAGER.addFriend(entityName);
                displayAddFriendMessage(entityName);
            }
        }
    }

    /**
     * ���������� ��������� � �������� �����.
     *
     * @param friendName ��� �����
     */
    private void displayRemoveFriendMessage(String friendName) {
        ClientUtil.sendMesage(TextFormatting.RED + "������ " + TextFormatting.RESET + friendName + " �� ������!");
    }

    /**
     * ���������� ��������� � ���������� �����.
     *
     * @param friendName ��� �����
     */
    private void displayAddFriendMessage(String friendName) {
        ClientUtil.sendMesage(TextFormatting.GREEN + "������� " + TextFormatting.RESET + friendName + " � ������!");
    }
}