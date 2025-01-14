package com.alan.clients.module.impl.render.targetinfo;

import com.alan.clients.component.impl.render.ParticleComponent;
import com.alan.clients.event.Listener;
import com.alan.clients.event.annotations.EventLink;
import com.alan.clients.event.impl.render.Render2DEvent;
import com.alan.clients.font.Fonts;
import com.alan.clients.font.Weight;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.module.impl.render.TargetInfo;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.render.StencilUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static com.alan.clients.layer.Layers.*;
import static com.alan.clients.util.animation.Easing.*;

public class RueTargetInfo extends Mode<TargetInfo> {
    public RueTargetInfo(String name, TargetInfo parent) {
        super(name, parent);
    }

    private final Font productSansMedium = Fonts.MAIN.get(18, Weight.REGULAR);
    private TargetInfo targetInfoModule;
    private final int EDGE_OFFSET = 6;
    private final int PADDING = 7;
    private final int INDENT = 4;

    private final Animation openingAnimation = new Animation(EASE_OUT_ELASTIC, 500);
    private final Animation healthAnimation = new Animation(EASE_OUT_SINE, 500);

    @EventLink
    public final Listener<Render2DEvent> onRender2D = event -> {
        if (this.targetInfoModule == null) {
            this.targetInfoModule = this.getModule(TargetInfo.class);
        }

        getLayer(BLOOM).add(ParticleComponent::bloom);
        getLayer(REGULAR, 1).add(ParticleComponent::render);

        Entity target = this.targetInfoModule.target;
        if (target == null) return;

        boolean out = (!this.targetInfoModule.inWorld || this.targetInfoModule.stopwatch.finished(1000));
        openingAnimation.setDuration(out ? 400 : 850);
        openingAnimation.setEasing(out ? EASE_IN_BACK : EASE_OUT_ELASTIC);
        openingAnimation.run(out ? 0 : 1);

        if (openingAnimation.getValue() <= 0) return;

        String name = target.getCommandSenderName();

        double x = this.targetInfoModule.position.x;
        double y = this.targetInfoModule.position.y;

        double nameWidth = productSansMedium.width(name);
        double health = Math.min(!this.targetInfoModule.inWorld ? 0 : MathUtil.round(((AbstractClientPlayer) target).getHealth(), 1), ((AbstractClientPlayer) target).getMaxHealth());
        double healthBarWidth = Math.max(nameWidth + 15, 70);

        healthAnimation.run((health / ((AbstractClientPlayer) target).getMaxHealth()) * healthBarWidth);
        healthAnimation.setEasing(EASE_OUT_QUINT);
        healthAnimation.setDuration(250);
        double healthRemainingWidth = healthAnimation.getValue();

        double hurtTime = (((AbstractClientPlayer) target).hurtTime == 0 ? 0 : ((AbstractClientPlayer) target).hurtTime - mc.timer.renderPartialTicks) * 0.5;
        int faceScale = 32;
        double faceOffset = hurtTime / 2f;
        double width = EDGE_OFFSET + faceScale + EDGE_OFFSET + healthBarWidth + INDENT + EDGE_OFFSET;
        double height = faceScale + EDGE_OFFSET * 2;
        this.targetInfoModule.positionValue.setScale(new Vector2d(width, height));

        double scale = openingAnimation.getValue();

        getLayer(REGULAR).add(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);

            // Draw background
            Color accent1 = getTheme().getFirstColor();
            Color accent2 = getTheme().getSecondColor();

            if (!(getModule(Interface.class).shaders.getValue())) {
                RenderUtil.roundedRectangle(x, y, width - 3.5, height - 4, 8, getTheme().getBackgroundShade());
            }
            RenderUtil.roundedRectangle(x, y, width - 3.5, height - 4, 8, getTheme().getBackgroundShade());
            // Render name

            String text = String.valueOf(Math.round(((AbstractClientPlayer) target).getHealth()));

            GlStateManager.pushMatrix();

            productSansMedium.drawWithShadow(name, x + EDGE_OFFSET + faceScale + PADDING - 2.5, y + EDGE_OFFSET + INDENT - 2, accent1.hashCode());
            productSansMedium.drawWithShadow(text, x + faceScale + mc.fontRendererObj.width("❤️") + 2, y + EDGE_OFFSET + INDENT + 8, -1);
            mc.fontRendererObj.drawWithShadow("§4❤", x + faceScale + 11, y + EDGE_OFFSET + INDENT + 7.5, -1);
            GlStateManager.popMatrix();

            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);

            // Health background
            RenderUtil.drawRoundedGradientRect(x + EDGE_OFFSET + faceScale + PADDING - 2.5, y + EDGE_OFFSET + faceScale - INDENT - 6, healthBarWidth, 6, 3, ColorUtil.withAlpha(getTheme().getBackgroundShade(), (int) (getTheme().getBackgroundShade().getAlpha() / 1.7f)), getTheme().getBackgroundShade(), true);

            // Health
            RenderUtil.drawRoundedGradientRect(x + EDGE_OFFSET + faceScale + PADDING - 2.5, y + EDGE_OFFSET + faceScale - INDENT - 6,
                    healthRemainingWidth, 6, 3, accent2, accent1, false);

            GlStateManager.popMatrix();
        });

        getLayer(REGULAR, 1).add(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);

            // Targets face
            RenderUtil.color(ColorUtil.mixColors(Color.RED, Color.WHITE, hurtTime / 9));
            RenderUtil.dropShadow(3, x + EDGE_OFFSET + faceOffset - 2, y + EDGE_OFFSET + faceOffset - 2, faceScale - hurtTime, faceScale - hurtTime, 20, this.getTheme().getRound() * 2);

            renderTargetHead((AbstractClientPlayer) target, x + EDGE_OFFSET + faceOffset - 2, y + EDGE_OFFSET + faceOffset - 2,
                    faceScale - hurtTime);

            GlStateManager.popMatrix();
        });

        getLayer(BLUR).add(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);
            RenderUtil.roundedRectangle(x, y, width - 3.5, height - 4, 8, Color.BLACK);
            GlStateManager.popMatrix();
        });

        getLayer(BLOOM).add(() -> {

            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);

            GlStateManager.popMatrix();

        });
    };

    private void renderTargetHead(final AbstractClientPlayer abstractClientPlayer, final double x, final double y, final double size) {
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
        RenderUtil.roundedRectangle(x, y, size, size, this.getTheme().getRound() * 2, this.getTheme().getBackgroundShade());
        StencilUtil.bindReadStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GlStateManager.enableTexture2D();

        final ResourceLocation resourceLocation = targetInfoModule.inWorld && abstractClientPlayer.getHealth() > 0
                ? abstractClientPlayer.getLocationSkin() : RenderSkeleton.getEntityTexture();

        mc.getTextureManager().bindTexture(resourceLocation);

        Gui.drawScaledCustomSizeModalRect(x, y, 4, 4, 4, 4, size, size, 32, 32);
        GlStateManager.disableBlend();
        StencilUtil.uninitStencilBuffer();

        float expand = 0.5f;
        RenderUtil.roundedOutlineRectangle(x - expand, y - expand, size + expand * 2, size + expand * 2, this.getTheme().getRound() * 2, 0.5, ColorUtil.withAlpha(Color.BLACK, 40));

    }
}

