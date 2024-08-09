package wtf.shiyeno.modules.impl.render;

import wtf.shiyeno.events.Event;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.modules.settings.imp.ModeSetting;
import wtf.shiyeno.modules.settings.imp.SliderSetting;

@FunctionAnnotation(name = "SwordAnimation", type = Type.Render)
public class SwingAnimationFunction extends Function {
    public final ModeSetting swordAnim = new ModeSetting("���", "Self", "Smooth", "Self", "Block", "Back","Swipe");

    public final SliderSetting angle = new SliderSetting("����", 100, 0, 360, 1).setVisible(() -> swordAnim.is("Self") || swordAnim.is("Block"));
    public final SliderSetting swipePower = new SliderSetting("���� ������", 8, 1, 10, 1).setVisible(() -> swordAnim.is("Self") || swordAnim.is("Block") || swordAnim.is("Back"));
    public final SliderSetting swipeSpeed = new SliderSetting("��������� ������", 11, 1, 20, 1);

    public final SliderSetting right_x = new SliderSetting("RightX", 0.0F, -2, 2, 0.1F);
    public final SliderSetting right_y = new SliderSetting("RightY", 0.0F, -2, 2, 0.1F);
    public final SliderSetting right_z = new SliderSetting("RightZ", 0.0F, -2, 2, 0.1F);
    public final SliderSetting left_x = new SliderSetting("LeftX", 0.0F, -2, 2, 0.1F);
    public final SliderSetting left_y = new SliderSetting("LeftY", 0.0F, -2, 2, 0.1F);
    public final SliderSetting left_z = new SliderSetting("LeftZ", 0.0F, -2, 2, 0.1F);

    public SwingAnimationFunction() {
        addSettings(swordAnim, angle, swipePower, swipeSpeed, right_x, right_y, right_z, left_x, left_y, left_z);
    }
    @Override
    public void onEvent(Event event) {}
}