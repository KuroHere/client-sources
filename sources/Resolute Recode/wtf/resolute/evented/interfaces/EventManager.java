package wtf.resolute.evented.interfaces;

import net.minecraft.client.Minecraft;
import wtf.resolute.ResoluteInfo;
import wtf.resolute.moduled.Module;

public class EventManager {
    /**
     * �������� ������� � �������� ��� ���� �������� ������� ��� ���������.
     *
     * @param event ������� ��� ������.
     */
    public static void call(final Event event) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().world == null) {
            return;
        }

        if (event.isCancel()) {
            return;
        }

        // ������� ���� �������� ������� � ����� �������
     //   if (!ClientUtil.legitMode) {
        //    callEvent(event);
     //   }
    }

    /**
     * �������� ��������� ������� � �������� ��� ���� �������� ������� ��� ���������.
     *
     * @param event ������� ��� ������.
     */
    private static void callEvent(Event event) {
        for (final Module module : ResoluteInfo.getInstance().getFunctionRegistry().getFunctions()) {
            if (!module.isState())
                continue;

        }
    }
}
