package wtf.resolute.moduled.impl.misc;

import wtf.resolute.evented.interfaces.Event;
import wtf.resolute.moduled.Categories;
import wtf.resolute.moduled.Module;
import wtf.resolute.moduled.ModuleAnontion;
import wtf.resolute.moduled.settings.impl.BooleanSetting;

@ModuleAnontion(name = "BetterMinecraft", type = Categories.Misc,server = "")
public class BetterMinecraft extends Module {

    public final BooleanSetting smoothCamera = new BooleanSetting("������� ������", true);
    //public final BooleanSetting smoothTab = new BooleanSetting("������� ���", true); // ���
    public final BooleanSetting betterTab = new BooleanSetting("���������� ���", true);

    public BetterMinecraft() {
        addSettings(smoothCamera, betterTab);
    }
}
