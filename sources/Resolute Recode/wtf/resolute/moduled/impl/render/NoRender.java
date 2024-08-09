package wtf.resolute.moduled.impl.render;

import com.google.common.eventbus.Subscribe;
import wtf.resolute.evented.EventCancelOverlay;
import wtf.resolute.evented.EventUpdate;
import wtf.resolute.moduled.Categories;
import wtf.resolute.moduled.Module;
import wtf.resolute.moduled.ModuleAnontion;
import wtf.resolute.moduled.settings.impl.BooleanSetting;
import wtf.resolute.moduled.settings.impl.ModeListSetting;
import net.minecraft.potion.Effects;

/* ��������!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */

@ModuleAnontion(name = "NoRender", type = Categories.Render,server = "")
public class NoRender extends Module {

    public ModeListSetting element = new ModeListSetting("�������",
            new BooleanSetting("����� �� ������", true),
            new BooleanSetting("����� �����", true),
            new BooleanSetting("�������� ������", true),
            new BooleanSetting("������", true),
            new BooleanSetting("�������", true),
            new BooleanSetting("�����", true),
            new BooleanSetting("������ ������", true),
            new BooleanSetting("������ �������", true),
            new BooleanSetting("�����", true));

    public NoRender() {
        addSettings(element);
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        handleEventUpdate(e);
    }

    @Subscribe
    private void onEventCancelOverlay(EventCancelOverlay e) {
        handleEventOverlaysRender(e);
    }

    private void handleEventOverlaysRender(EventCancelOverlay event) {
        boolean cancelOverlay = switch (event.overlayType) {
            case FIRE_OVERLAY -> element.getValueByName("����� �� ������").get();
            case BOSS_LINE -> element.getValueByName("����� �����").get();
            case SCOREBOARD -> element.getValueByName("�������").get();
            case TITLES -> element.getValueByName("������").get();
            case TOTEM -> element.getValueByName("�������� ������").get();
            case FOG -> element.getValueByName("�����").get();
            case HURT -> element.getValueByName("������ ������").get();
        };

        if (cancelOverlay) {
            event.cancel();
        }
    }

    private void handleEventUpdate(EventUpdate event) {
        boolean isRaining = mc.world.isRaining() && element.getValueByName("�����").get();

        boolean hasEffects = (mc.player.isPotionActive(Effects.BLINDNESS)
                || mc.player.isPotionActive(Effects.NAUSEA)) && element.getValueByName("������ �������").get();

        if (isRaining) {
            mc.world.setRainStrength(0);
            mc.world.setThunderStrength(0);
        }

        if (hasEffects) {
            mc.player.removePotionEffect(Effects.NAUSEA);
            mc.player.removePotionEffect(Effects.BLINDNESS);
        }
    }
}
