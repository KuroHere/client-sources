package wtf.shiyeno.modules.impl.util;

import wtf.shiyeno.events.Event;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.modules.settings.Setting;
import wtf.shiyeno.modules.settings.imp.BooleanOption;
import wtf.shiyeno.modules.settings.imp.MultiBoxSetting;

@FunctionAnnotation(
        name = "Optimization",
        type = Type.Util
)
public class Optimization extends Function {
    public final MultiBoxSetting optimizeSelection = new MultiBoxSetting("��������������", new BooleanOption[]{new BooleanOption("���������", true), new BooleanOption("��������", true), new BooleanOption("��������� �������", false)});

    public Optimization() {
        this.addSettings(new Setting[]{this.optimizeSelection});
    }

    public void onEvent(Event event) {
    }
}