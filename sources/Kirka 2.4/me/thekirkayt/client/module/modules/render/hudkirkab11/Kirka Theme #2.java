/*
 * Decompiled with CFR 0.143.
 */
package me.thekirkayt.client.module.modules.render.hudkirkab11;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.thekirkayt.client.module.Module;
import me.thekirkayt.client.module.ModuleManager;
import me.thekirkayt.client.module.modules.render.hudkirkab11.Theme;
import me.thekirkayt.event.events.Render2DEvent;
import me.thekirkayt.utils.ClientUtils;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Kirka Theme #2
extends Theme {
    private float[] hue = new float[]{0.0f};

    public Kirka Theme #2(String string, boolean bl, Module module) {
        super(string, bl, module);
    }

    @Override
    public boolean onRender(Render2DEvent render2DEvent) {
        if (super.onRender(render2DEvent)) {
            float[] arrf;
            float[] arrf2;
            String string = new SimpleDateFormat("hh:mm a").format(new Date());
            if (string.startsWith("0")) {
                string = string.replaceFirst("0", "");
            }
            ClientUtils.mc().getRenderItem().func_180450_b(new ItemStack(Item.getByNameOrId("diamond_sword")), 52, -3);
            ClientUtils.clientFont().drawStringWithShadow("\u00a79\u00a7lK\u00a77\u00a7lirka \u00a79\u00a7lb\u00a77\u00a7l11", 2.0, 2.0, 13356753);
            int n = 2;
            float[] arrf22 = new float[]{this.hue[0]};
            for (Module object2 : ModuleManager.getModulesForRender()) {
                float[] arrf3 = arrf2 = arrf22;
                arrf3[0] = arrf3[0] + 9.0f;
                if (arrf22[0] > 255.0f) {
                    float[] arrf4;
                    float[] arrf5 = arrf4 = arrf22;
                    arrf5[0] = arrf5[0] - 255.0f;
                }
                int arrf4 = Color.getHSBColor(arrf22[0] / 255.0f, 1.0f, 1.0f).getRGB();
                if (!object2.drawDisplayName(render2DEvent.getWidth() - ClientUtils.clientFont().getStringWidth(String.format("%s" + (object2.getSuffix().length() > 0 ? " \u00a70[%s]" : ""), object2.getDisplayName(), object2.getSuffix())) - 2, n, arrf4)) continue;
                n += 10;
            }
            float[] arrf6 = arrf = this.hue;
            arrf6[0] = (float)((double)arrf6[0] + 0.5);
            if (this.hue[0] > 255.0f) {
                float[] arrf7 = arrf2 = this.hue;
                arrf7[0] = arrf7[0] - 255.0f;
            }
        }
        return super.onRender(render2DEvent);
    }
}

