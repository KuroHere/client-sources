/*
 * Decompiled with CFR 0_122.
 */
package Monix.Mod.mods;

import Monix.Category.Category;
import Monix.Event.EventTarget;
import Monix.Event.events.EventRender3D;
import Monix.Mod.Mod;
import Monix.Utils.RenderUtils;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;

public class ESP
extends Mod {
    public static boolean Players = true;
    public static boolean Mobs = true;
    public static boolean Animals = true;

    public ESP() {
        super("ESP", "ESP", 0, Category.WORLD);
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        for (Object theObject : ESP.mc.theWorld.loadedEntityList) {
            if (!(theObject instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = (EntityLivingBase)theObject;
            if (entity instanceof EntityPlayer && Players) {
                if (entity == ESP.mc.thePlayer) continue;
                this.player(entity);
                continue;
            }
            if (entity instanceof EntityMob && Mobs) {
                this.mob(entity);
                continue;
            }
            if (!(entity instanceof EntityAnimal) || !Animals) continue;
            this.animal(entity);
        }
        super.onRender();
    }

    public void player(EntityLivingBase entity) {
        float red = 0.5f;
        float green = 0.5f;
        float blue = 1.0f;
        double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosX;
        double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosY;
        double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosZ;
        this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }

    public void mob(EntityLivingBase entity) {
        float red = 1.0f;
        float green = 0.0f;
        float blue = 0.0f;
        double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosX;
        double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosY;
        double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosZ;
        this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }

    public void animal(EntityLivingBase entity) {
        float red = 0.5f;
        float green = 0.5f;
        float blue = 0.5f;
        double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosX;
        double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosY;
        double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)ESP.mc.timer.renderPartialTicks - ESP.mc.getRenderManager().renderPosZ;
        this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
    }

    public void render(float red, float green, float blue, double x, double y, double z, float width, float height) {
        RenderUtils.drawEntityESP(x, y, z, width, height, red, green, blue, 0.45f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f);
    }
}

