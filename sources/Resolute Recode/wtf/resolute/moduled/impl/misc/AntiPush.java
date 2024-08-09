package wtf.resolute.moduled.impl.misc;

import wtf.resolute.evented.interfaces.Event;
import wtf.resolute.moduled.Categories;
import wtf.resolute.moduled.Module;
import wtf.resolute.moduled.ModuleAnontion;
import wtf.resolute.moduled.settings.impl.BooleanSetting;
import wtf.resolute.moduled.settings.impl.ModeListSetting;
import lombok.Getter;

@Getter
@ModuleAnontion(name = "AntiPush", type = Categories.Player,server = "")
public class AntiPush extends Module {

    private final ModeListSetting modes = new ModeListSetting("���",
            new BooleanSetting("������", true),
            new BooleanSetting("����", false),
            new BooleanSetting("�����", true));

    public AntiPush() {
        addSettings(modes);
    }

}
