package wtf.resolute.moduled.impl.misc;

import wtf.resolute.evented.interfaces.Event;
import wtf.resolute.moduled.Categories;
import wtf.resolute.moduled.Module;
import wtf.resolute.moduled.ModuleAnontion;
import wtf.resolute.moduled.settings.impl.ModeSetting;
import wtf.resolute.moduled.settings.impl.SliderSetting;

@ModuleAnontion(name = "ClientSounds", type = Categories.Misc,server = "")
public class ClientSounds extends Module {

    public ModeSetting mode = new ModeSetting("���", "�������", "�������");
    public SliderSetting volume = new SliderSetting("���������", 100.0f, 0.0f, 150.0f, 1.0f);

    public ClientSounds() {
        addSettings(mode, volume);
    }


    public String getFileName(boolean state) {
        switch (mode.get()) {
            case "�������" -> {
                return state ? "enable" : "disable".toString();
            }
            case "��������" -> {
                return state ? "enableBubbles" : "disableBubbles";
            }
        }
        return "";
    }
}
