/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package shadersmod.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.GuiShaders;
import shadersmod.client.Shaders;

public class GuiButtonEnumShaderOption
extends GuiButton {
    private EnumShaderOption enumShaderOption = null;

    public GuiButtonEnumShaderOption(EnumShaderOption enumShaderOption, int x, int y, int widthIn, int heightIn) {
        super(enumShaderOption.ordinal(), x, y, widthIn, heightIn, GuiButtonEnumShaderOption.getButtonText(enumShaderOption));
        this.enumShaderOption = enumShaderOption;
    }

    public EnumShaderOption getEnumShaderOption() {
        return this.enumShaderOption;
    }

    private static String getButtonText(EnumShaderOption eso) {
        String s = I18n.format(eso.getResourceKey(), new Object[0]) + ": ";
        switch (eso) {
            case ANTIALIASING: {
                return s + GuiShaders.toStringAa(Shaders.configAntialiasingLevel);
            }
            case NORMAL_MAP: {
                return s + GuiShaders.toStringOnOff(Shaders.configNormalMap);
            }
            case SPECULAR_MAP: {
                return s + GuiShaders.toStringOnOff(Shaders.configSpecularMap);
            }
            case RENDER_RES_MUL: {
                return s + GuiShaders.toStringQuality(Shaders.configRenderResMul);
            }
            case SHADOW_RES_MUL: {
                return s + GuiShaders.toStringQuality(Shaders.configShadowResMul);
            }
            case HAND_DEPTH_MUL: {
                return s + GuiShaders.toStringHandDepth(Shaders.configHandDepthMul);
            }
            case CLOUD_SHADOW: {
                return s + GuiShaders.toStringOnOff(Shaders.configCloudShadow);
            }
            case OLD_HAND_LIGHT: {
                return s + Shaders.configOldHandLight.getUserValue();
            }
            case OLD_LIGHTING: {
                return s + Shaders.configOldLighting.getUserValue();
            }
            case SHADOW_CLIP_FRUSTRUM: {
                return s + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
            }
            case TWEAK_BLOCK_DAMAGE: {
                return s + GuiShaders.toStringOnOff(Shaders.configTweakBlockDamage);
            }
        }
        return s + Shaders.getEnumShaderOption(eso);
    }

    public void updateButtonText() {
        this.displayString = GuiButtonEnumShaderOption.getButtonText(this.enumShaderOption);
    }
}

