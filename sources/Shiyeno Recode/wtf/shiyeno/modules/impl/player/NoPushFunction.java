package wtf.shiyeno.modules.impl.player;

import wtf.shiyeno.events.Event;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.modules.settings.Setting;
import wtf.shiyeno.modules.settings.imp.BooleanOption;
import wtf.shiyeno.modules.settings.imp.MultiBoxSetting;

@FunctionAnnotation(
        name = "NoPush",
        type = Type.Player
)
public class NoPushFunction extends Function {
    public final MultiBoxSetting modes = new MultiBoxSetting("���", new BooleanOption[]{new BooleanOption("������", true), new BooleanOption("�����", true), new BooleanOption("����", true)});

    public NoPushFunction() {
        this.addSettings(new Setting[]{this.modes});
    }

    public void onEvent(Event event) {
    }
}