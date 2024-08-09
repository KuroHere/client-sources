package wtf.shiyeno.modules.impl.render;

import net.minecraft.network.play.server.SUpdateTimePacket;
import wtf.shiyeno.events.Event;
import wtf.shiyeno.events.impl.packet.EventPacket;
import wtf.shiyeno.events.impl.player.EventUpdate;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.modules.settings.Setting;
import wtf.shiyeno.modules.settings.imp.BooleanOption;
import wtf.shiyeno.modules.settings.imp.ColorSetting;
import wtf.shiyeno.modules.settings.imp.ModeSetting;
import wtf.shiyeno.modules.settings.imp.MultiBoxSetting;
import wtf.shiyeno.modules.settings.imp.SliderSetting;

@FunctionAnnotation(
        name = "CustomWorld",
        type = Type.Render
)
public class CustomWorld extends Function {
    public MultiBoxSetting modes = new MultiBoxSetting("��������", new BooleanOption[]{new BooleanOption("�����", false), new BooleanOption("�����", false)});
    private ModeSetting timeOfDay = (new ModeSetting("����� �����", "����", new String[]{"����", "�����", "�������", "����", "�������", "�������"})).setVisible(() -> {
        return this.modes.get(0);
    });
    public ColorSetting colorFog = (new ColorSetting("���� ������", -1)).setVisible(() -> {
        return this.modes.get(1);
    });
    public SliderSetting distanceFog = (new SliderSetting("��������� ������", 4.0F, 1.1F, 30.0F, 0.01F)).setVisible(() -> {
        return this.modes.get(1);
    });
    float time;

    public CustomWorld() {
        this.addSettings(new Setting[]{this.modes, this.timeOfDay, this.colorFog, this.distanceFog});
    }

    public void onEvent(Event event) {
        if (event instanceof EventPacket eventPacket) {
            if (((EventPacket)event).isReceivePacket() && eventPacket.getPacket() instanceof SUpdateTimePacket && this.modes.get(0)) {
                eventPacket.setCancel(true);
            }
        }

        if (event instanceof EventUpdate && this.modes.get(0)) {
            float time = 0.0F;
            switch (this.timeOfDay.get()) {
                case "����" -> time = 1000.0F;
                case "�����" -> time = 12000.0F;
                case "�������" -> time = 23000.0F;
                case "����" -> time = 13000.0F;
                case "�������" -> time = 18000.0F;
                case "�������" -> time = 6000.0F;
            }

            mc.world.setDayTime((long)time);
        }
    }
}