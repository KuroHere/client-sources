package me.napoleon.napoline.modules.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;
import java.util.Random;

import me.napoleon.napoline.events.EventPacketSend;
import me.napoleon.napoline.events.EventPreUpdate;
import me.napoleon.napoline.junk.values.type.Mode;
import me.napoleon.napoline.manager.event.EventTarget;
import me.napoleon.napoline.modules.Mod;
import me.napoleon.napoline.modules.ModCategory;
import me.napoleon.napoline.utils.timer.TimerUtil;

public class FlyDisabler
        extends Mod {
    public Mode mode = new Mode("Mode", (Enum[]) DMode.values(), (Enum) DMode.WatchDoge);

    public ArrayList<C0FPacketConfirmTransaction> cs = new ArrayList<>();
    public TimerUtil timer = new TimerUtil();
    public TimerUtil timer2 = new TimerUtil();

    public FlyDisabler() {
        super("FlyDisabler", ModCategory.Player, "Disable AntiCheat(Fly Only)");
        this.addValues(this.mode);
    }

    private int ticks;

    @EventTarget
    void onUpdate(EventPreUpdate event) {
        if (mc.thePlayer.ticksExisted <= 1)
            ticks = 0;
        Random random = new Random();
        if (timer.delay(1000 + random.nextInt() * 3)) {
            if (timer2.delay(10000)) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0FPacketConfirmTransaction(0, (short) ((short) -623 + random.nextInt()), false));
            }
            timer.reset();
        }


    }

    @EventTarget
    void onPacket(EventPacketSend event) {
        Packet<?> p = event.getPacket();
        if (p instanceof C0FPacketConfirmTransaction) {
            C0FPacketConfirmTransaction c0FPacketConfirmTransaction = (C0FPacketConfirmTransaction) p;
            if (((C0FPacketConfirmTransaction) p).windowId == 0 && ((C0FPacketConfirmTransaction) p).uid < -1) {
                event.setCancelled(true);
                cs.add(((C0FPacketConfirmTransaction) p));
            }

            if (cs.size() > 7) {
                for (C0FPacketConfirmTransaction c : cs) {
                    mc.getNetHandler().addToSendQueueNoEvent(c);
                }
                cs.clear();
                timer2.reset();
            }
        }
    }

    public static enum DMode {
        WatchDoge;
    }
}