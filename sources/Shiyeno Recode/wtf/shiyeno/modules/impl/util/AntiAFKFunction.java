package wtf.shiyeno.modules.impl.util;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import wtf.shiyeno.events.Event;
import wtf.shiyeno.events.impl.player.EventUpdate;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.modules.settings.Setting;
import wtf.shiyeno.modules.settings.imp.BooleanOption;
import wtf.shiyeno.modules.settings.imp.MultiBoxSetting;
import wtf.shiyeno.modules.settings.imp.SliderSetting;
import wtf.shiyeno.util.misc.TimerUtil;

@FunctionAnnotation(
        name = "AntiAFK",
        type = Type.Util
)
public class AntiAFKFunction extends Function {
    private final TimerUtil timerUtil = new TimerUtil();
    private final MultiBoxSetting mode = new MultiBoxSetting("�����������", new BooleanOption[]{new BooleanOption("���� ������", true), new BooleanOption("���� ���� �� �����", true), new BooleanOption("���� ����������", false), new BooleanOption("���� �������� �� ��������", false), new BooleanOption("�������� ����� RW", true)});
    private final SliderSetting delay = new SliderSetting("��������", 1.0F, 1.0F, 10000.0F, 1.0F);

    public AntiAFKFunction() {
        this.addSettings(new Setting[]{this.mode, this.delay});
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (this.mode.get("���� ������") && this.timerUtil.hasTimeElapsed((long)this.delay.getValue().intValue()) && mc.player.isOnGround() && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isInWater() && mc.player.ticksExisted % 1 == 0) {
                mc.player.jump();
                this.timerUtil.reset();
            }

            if (this.mode.get("���� ���� �� �����") && this.timerUtil.hasTimeElapsed((long)this.delay.getValue().intValue())) {
                mc.gameSettings.keyBindAttack.setPressed(true);
                this.timerUtil.reset();
            }

            if (this.mode.get("���� ����������") && this.timerUtil.hasTimeElapsed((long)this.delay.getValue().intValue())) {
                mc.gameSettings.keyBindSneak.pressed = true;
                if (this.timerUtil.hasTimeElapsed((long)(this.delay.getValue().intValue() + 100))) {
                    mc.gameSettings.keyBindSneak.pressed = false;
                    this.timerUtil.reset();
                }
            }

            if (this.mode.get("���� �������� �� ��������") && this.timerUtil.hasTimeElapsed((long)this.delay.getValue().intValue())) {
                mc.gameSettings.keyBindLeft.pressed = true;
                if (this.timerUtil.hasTimeElapsed((long)(this.delay.getValue().intValue() + 50))) {
                    mc.gameSettings.keyBindLeft.pressed = false;
                    mc.gameSettings.keyBindBack.pressed = true;
                    if (this.timerUtil.hasTimeElapsed((long)(this.delay.getValue().intValue() + 100))) {
                        mc.gameSettings.keyBindBack.pressed = false;
                        mc.gameSettings.keyBindRight.pressed = true;
                        if (this.timerUtil.hasTimeElapsed((long)(this.delay.getValue().intValue() + 150))) {
                            mc.gameSettings.keyBindRight.pressed = false;
                            mc.gameSettings.keyBindForward.pressed = true;
                            if (this.timerUtil.hasTimeElapsed((long)(this.delay.getValue().intValue() + 200))) {
                                mc.gameSettings.keyBindForward.pressed = false;
                                this.timerUtil.reset();
                            }
                        }
                    }
                }
            }

            if (this.mode.get("�������� ����� RW") && this.timerUtil.hasTimeElapsed((long)this.delay.getValue().intValue())) {
                mc.player.swingArm(Hand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 20.0, mc.player.getPosZ()), mc.player.getHorizontalFacing());
            }
        }
    }

    public void onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false;
        mc.gameSettings.keyBindAttack.pressed = false;
        mc.gameSettings.keyBindLeft.pressed = false;
        mc.gameSettings.keyBindBack.pressed = false;
        mc.gameSettings.keyBindRight.pressed = false;
        mc.gameSettings.keyBindForward.pressed = false;
    }
}