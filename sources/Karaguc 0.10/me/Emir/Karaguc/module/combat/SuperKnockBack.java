package me.Emir.Karaguc.module.combat;

import de.Hero.settings.Setting;
import me.Emir.Karaguc.Karaguc;
import me.Emir.Karaguc.event.EventTarget;
import me.Emir.Karaguc.event.events.EventUpdate;
import me.Emir.Karaguc.module.Category;
import me.Emir.Karaguc.module.Module;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class SuperKnockBack extends Module {
    public SuperKnockBack() { super("SuperKnockBack", Keyboard.KEY_NONE, Category.MOVEMENT); }

    
    // Does nothing
    
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        //TODO: Add more AutoBreaker modes
        options.add("Cake");
        Karaguc.instance.settingsManager.rSetting(new Setting("AutoBreaker Mode", this, "Normal", options));
        Karaguc.instance.settingsManager.rSetting(new Setting("SlimeJump", this, false));
        Karaguc.instance.settingsManager.rSetting(new Setting("Aac", this, false));
        Karaguc.instance.settingsManager.rSetting(new Setting("Hypixel", this, false));
        Karaguc.instance.settingsManager.rSetting(new Setting("HiveMC", this, false));
        Karaguc.instance.settingsManager.rSetting(new Setting("NCP", this, false));
        Karaguc.instance.settingsManager.rSetting(new Setting("Cubecraft/Sentinel", this, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if(mc.thePlayer.isBlocking()){
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0,0,0), EnumFacing.UP));
        }
    }
}
