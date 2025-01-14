/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPlayer
extends ModelBiped {
    public ModelRenderer field_178734_a;
    public ModelRenderer field_178732_b;
    public ModelRenderer field_178733_c;
    public ModelRenderer field_178731_d;
    public ModelRenderer field_178730_v;
    private ModelRenderer field_178729_w;
    private ModelRenderer field_178736_x;
    private boolean field_178735_y;
    private static final String __OBFID = "CL_00002626";

    public ModelPlayer(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, 0.0f, 64, 64);
        this.field_178735_y = p_i46304_2_;
        this.field_178736_x = new ModelRenderer(this, 24, 0);
        this.field_178736_x.addBox(-3.0f, -6.0f, -1.0f, 6, 6, 1, p_i46304_1_);
        this.field_178729_w = new ModelRenderer(this, 0, 0);
        this.field_178729_w.setTextureSize(64, 32);
        this.field_178729_w.addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, p_i46304_1_);
        if (p_i46304_2_) {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.5f, 0.0f);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.5f, 0.0f);
            this.field_178734_a = new ModelRenderer(this, 48, 48);
            this.field_178734_a.addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_ + 0.25f);
            this.field_178734_a.setRotationPoint(5.0f, 2.5f, 0.0f);
            this.field_178732_b = new ModelRenderer(this, 40, 32);
            this.field_178732_b.addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_ + 0.25f);
            this.field_178732_b.setRotationPoint(-5.0f, 2.5f, 10.0f);
        } else {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            this.field_178734_a = new ModelRenderer(this, 48, 48);
            this.field_178734_a.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
            this.field_178734_a.setRotationPoint(5.0f, 2.0f, 0.0f);
            this.field_178732_b = new ModelRenderer(this, 40, 32);
            this.field_178732_b.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
            this.field_178732_b.setRotationPoint(-5.0f, 2.0f, 10.0f);
        }
        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.field_178733_c = new ModelRenderer(this, 0, 48);
        this.field_178733_c.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
        this.field_178733_c.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.field_178731_d = new ModelRenderer(this, 0, 32);
        this.field_178731_d.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
        this.field_178731_d.setRotationPoint(-1.9f, 12.0f, 0.0f);
        this.field_178730_v = new ModelRenderer(this, 16, 32);
        this.field_178730_v.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i46304_1_ + 0.25f);
        this.field_178730_v.setRotationPoint(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float var8 = 2.0f;
            GlStateManager.scale(1.0f / var8, 1.0f / var8, 1.0f / var8);
            GlStateManager.translate(0.0f, 24.0f * p_78088_7_, 0.0f);
            this.field_178733_c.render(p_78088_7_);
            this.field_178731_d.render(p_78088_7_);
            this.field_178734_a.render(p_78088_7_);
            this.field_178732_b.render(p_78088_7_);
            this.field_178730_v.render(p_78088_7_);
        } else {
            if (p_78088_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.field_178733_c.render(p_78088_7_);
            this.field_178731_d.render(p_78088_7_);
            this.field_178734_a.render(p_78088_7_);
            this.field_178732_b.render(p_78088_7_);
            this.field_178730_v.render(p_78088_7_);
        }
        GlStateManager.popMatrix();
    }

    public void func_178727_b(float p_178727_1_) {
        ModelPlayer.func_178685_a(this.bipedHead, this.field_178736_x);
        this.field_178736_x.rotationPointX = 0.0f;
        this.field_178736_x.rotationPointY = 0.0f;
        this.field_178736_x.render(p_178727_1_);
    }

    public void func_178728_c(float p_178728_1_) {
        this.field_178729_w.render(p_178728_1_);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        ModelPlayer.func_178685_a(this.bipedLeftLeg, this.field_178733_c);
        ModelPlayer.func_178685_a(this.bipedRightLeg, this.field_178731_d);
        ModelPlayer.func_178685_a(this.bipedLeftArm, this.field_178734_a);
        ModelPlayer.func_178685_a(this.bipedRightArm, this.field_178732_b);
        ModelPlayer.func_178685_a(this.bipedBody, this.field_178730_v);
    }

    public void func_178725_a() {
        this.bipedRightArm.render(0.0625f);
        this.field_178732_b.render(0.0625f);
    }

    public void func_178726_b() {
        this.bipedLeftArm.render(0.0625f);
        this.field_178734_a.render(0.0625f);
    }

    @Override
    public void func_178719_a(boolean p_178719_1_) {
        super.func_178719_a(p_178719_1_);
        this.field_178734_a.showModel = p_178719_1_;
        this.field_178732_b.showModel = p_178719_1_;
        this.field_178733_c.showModel = p_178719_1_;
        this.field_178731_d.showModel = p_178719_1_;
        this.field_178730_v.showModel = p_178719_1_;
        this.field_178729_w.showModel = p_178719_1_;
        this.field_178736_x.showModel = p_178719_1_;
    }

    @Override
    public void postRenderHiddenArm(float p_178718_1_) {
        if (this.field_178735_y) {
            this.bipedRightArm.rotationPointX += 1.0f;
            this.bipedRightArm.postRender(p_178718_1_);
            this.bipedRightArm.rotationPointX -= 1.0f;
        } else {
            this.bipedRightArm.postRender(p_178718_1_);
        }
    }
}

