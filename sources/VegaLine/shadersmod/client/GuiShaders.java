/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.Lang;
import org.lwjgl.Sys;
import ru.govno.client.utils.Command.impl.Panic;
import ru.govno.client.utils.Render.RenderUtils;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.GuiButtonEnumShaderOption;
import shadersmod.client.GuiShaderOptions;
import shadersmod.client.GuiSlotShaders;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersTex;

public class GuiShaders
extends GuiScreen {
    protected GuiScreen parentGui;
    protected String screenTitle = "Shaders";
    private int updateTimer = -1;
    private GuiSlotShaders shaderList;
    private boolean saved = false;
    private static final float[] QUALITY_MULTIPLIERS = new float[]{0.5f, 0.70710677f, 1.0f, 1.4142135f, 2.0f};
    private static final String[] QUALITY_MULTIPLIER_NAMES = new String[]{"0.5x", "0.7x", "1x", "1.5x", "2x"};
    private static final float[] HAND_DEPTH_VALUES = new float[]{0.0625f, 0.125f, 0.25f};
    private static final String[] HAND_DEPTH_NAMES = new String[]{"0.5x", "1x", "2x"};
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;

    public GuiShaders(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
        this.parentGui = par1GuiScreen;
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.format("of.options.shadersTitle", new Object[0]);
        if (Shaders.shadersConfig == null) {
            Shaders.loadConfig();
        }
        int i = 120;
        int j = 20;
        int k = width - i - 10;
        int l = 30;
        int i1 = 20;
        int j1 = width - i - 20;
        this.shaderList = new GuiSlotShaders(this, j1, height, l, height - 50, 16);
        this.shaderList.registerScrollButtons(7, 8);
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, k, l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, k, i1 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, k, 2 * i1 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, k, 3 * i1 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, k, 4 * i1 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, k, 5 * i1 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, k, 6 * i1 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, k, 7 * i1 + l, i, j));
        int k1 = Math.min(150, j1 / 2 - 10);
        this.buttonList.add(new GuiButton(201, j1 / 4 - k1 / 2, height - 25, k1, j, Lang.get("of.options.shaders.shadersFolder")));
        this.buttonList.add(new GuiButton(202, j1 / 4 * 3 - k1 / 2, height - 25, k1, j, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(203, k, height - 25, i, j, Lang.get("of.options.shaders.shaderOptions")));
        this.updateButtons();
    }

    public void updateButtons() {
        boolean flag = Config.isShaders();
        for (GuiButton guibutton : this.buttonList) {
            if (guibutton.id == 201 || guibutton.id == 202 || guibutton.id == EnumShaderOption.ANTIALIASING.ordinal()) continue;
            guibutton.enabled = flag;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.shaderList.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button instanceof GuiButtonEnumShaderOption) {
                GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)button;
                switch (guibuttonenumshaderoption.getEnumShaderOption()) {
                    case ANTIALIASING: {
                        Shaders.nextAntialiasingLevel();
                        Shaders.uninit();
                        break;
                    }
                    case NORMAL_MAP: {
                        Shaders.configNormalMap = !Shaders.configNormalMap;
                        Shaders.uninit();
                        this.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case SPECULAR_MAP: {
                        Shaders.configSpecularMap = !Shaders.configSpecularMap;
                        Shaders.uninit();
                        this.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case RENDER_RES_MUL: {
                        float f2 = Shaders.configRenderResMul;
                        float[] afloat2 = QUALITY_MULTIPLIERS;
                        String[] astring2 = QUALITY_MULTIPLIER_NAMES;
                        int k = GuiShaders.getValueIndex(f2, afloat2);
                        if (GuiShaders.isShiftKeyDown()) {
                            if (--k < 0) {
                                k = afloat2.length - 1;
                            }
                        } else if (++k >= afloat2.length) {
                            k = 0;
                        }
                        Shaders.configRenderResMul = afloat2[k];
                        Shaders.uninit();
                        Shaders.scheduleResize();
                        break;
                    }
                    case SHADOW_RES_MUL: {
                        float f1 = Shaders.configShadowResMul;
                        float[] afloat1 = QUALITY_MULTIPLIERS;
                        String[] astring1 = QUALITY_MULTIPLIER_NAMES;
                        int j = GuiShaders.getValueIndex(f1, afloat1);
                        if (GuiShaders.isShiftKeyDown()) {
                            if (--j < 0) {
                                j = afloat1.length - 1;
                            }
                        } else if (++j >= afloat1.length) {
                            j = 0;
                        }
                        Shaders.configShadowResMul = afloat1[j];
                        Shaders.uninit();
                        Shaders.scheduleResizeShadow();
                        break;
                    }
                    case HAND_DEPTH_MUL: {
                        float f = Shaders.configHandDepthMul;
                        float[] afloat = HAND_DEPTH_VALUES;
                        String[] astring = HAND_DEPTH_NAMES;
                        int i = GuiShaders.getValueIndex(f, afloat);
                        if (GuiShaders.isShiftKeyDown()) {
                            if (--i < 0) {
                                i = afloat.length - 1;
                            }
                        } else if (++i >= afloat.length) {
                            i = 0;
                        }
                        Shaders.configHandDepthMul = afloat[i];
                        Shaders.uninit();
                        break;
                    }
                    case OLD_HAND_LIGHT: {
                        Shaders.configOldHandLight.nextValue();
                        Shaders.uninit();
                        break;
                    }
                    case OLD_LIGHTING: {
                        Shaders.configOldLighting.nextValue();
                        Shaders.updateBlockLightLevel();
                        Shaders.uninit();
                        this.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case TWEAK_BLOCK_DAMAGE: {
                        Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                        break;
                    }
                    case CLOUD_SHADOW: {
                        Shaders.configCloudShadow = !Shaders.configCloudShadow;
                        break;
                    }
                    case TEX_MIN_FIL_B: {
                        Shaders.configTexMinFilN = Shaders.configTexMinFilS = (Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3);
                        button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case TEX_MAG_FIL_N: {
                        Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                        button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case TEX_MAG_FIL_S: {
                        Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                        button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case SHADOW_CLIP_FRUSTRUM: {
                        Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                        button.displayString = "ShadowClipFrustrum: " + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
                        ShadersTex.updateTextureMinMagFilter();
                    }
                }
                guibuttonenumshaderoption.updateButtonText();
            } else {
                switch (button.id) {
                    case 201: {
                        switch (GuiShaders.getOSType()) {
                            case 1: {
                                String s = String.format("cmd.exe /C start \"Open file\" \"%s\"", Shaders.shaderpacksdir.getAbsolutePath());
                                try {
                                    Runtime.getRuntime().exec(s);
                                    return;
                                } catch (IOException ioexception) {
                                    ioexception.printStackTrace();
                                    break;
                                }
                            }
                            case 2: {
                                try {
                                    Runtime.getRuntime().exec(new String[]{"/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath()});
                                    return;
                                } catch (IOException ioexception1) {
                                    ioexception1.printStackTrace();
                                }
                            }
                        }
                        boolean flag = false;
                        try {
                            Class<?> oclass = Class.forName("java.awt.Desktop");
                            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                            oclass.getMethod("browse", URI.class).invoke(object, new File(this.mc.mcDataDir, Shaders.shaderpacksdirname).toURI());
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            flag = true;
                        }
                        if (!flag) break;
                        Config.dbg("Opening via system class!");
                        Sys.openURL("file://" + Shaders.shaderpacksdir.getAbsolutePath());
                        break;
                    }
                    case 202: {
                        new File(Shaders.shadersdir, "current.cfg");
                        Shaders.storeConfig();
                        this.saved = true;
                        this.mc.displayGuiScreen(this.parentGui);
                        break;
                    }
                    case 203: {
                        GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, Config.getGameSettings());
                        Config.getMinecraft().displayGuiScreen(guishaderoptions);
                        break;
                    }
                    default: {
                        this.shaderList.actionPerformed(button);
                    }
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (!this.saved) {
            Shaders.storeConfig();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GuiScreen guiScreen;
        this.drawDefaultBackground();
        if (!Panic.stop && (guiScreen = this.parentGui) instanceof GuiVideoSettings) {
            GuiVideoSettings vid = (GuiVideoSettings)guiScreen;
            guiScreen = vid.parentGuiScreen;
            if (guiScreen instanceof GuiOptions) {
                GuiOptions op = (GuiOptions)guiScreen;
                if (op.lastScreen instanceof GuiMainMenu) {
                    RenderUtils.drawScreenShaderBackground(new ScaledResolution(this.mc), mouseX, mouseY);
                }
            }
        }
        this.shaderList.drawScreen(mouseX, mouseY, partialTicks);
        if (this.updateTimer <= 0) {
            this.shaderList.updateList();
            this.updateTimer += 20;
        }
        this.drawCenteredString(this.fontRendererObj, this.screenTitle + " ", width / 2, 15, 0xFFFFFF);
        String s = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
        int i = this.fontRendererObj.getStringWidth(s);
        if (i < width - 5) {
            this.drawCenteredString(this.fontRendererObj, s, width / 2, height - 40, 0x808080);
        } else {
            this.drawString(this.fontRendererObj, s, 5, height - 40, 0x808080);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        --this.updateTimer;
    }

    public Minecraft getMc() {
        return this.mc;
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        this.drawCenteredString(this.fontRendererObj, text, x, y, color);
    }

    public static String toStringOnOff(boolean value) {
        String s = Lang.getOn();
        String s1 = Lang.getOff();
        return value ? s : s1;
    }

    public static String toStringAa(int value) {
        if (value == 2) {
            return "FXAA 2x";
        }
        return value == 4 ? "FXAA 4x" : Lang.getOff();
    }

    public static String toStringValue(float val, float[] values, String[] names) {
        int i = GuiShaders.getValueIndex(val, values);
        return names[i];
    }

    public static int getValueIndex(float val, float[] values) {
        for (int i = 0; i < values.length; ++i) {
            float f = values[i];
            if (!(f >= val)) continue;
            return i;
        }
        return values.length - 1;
    }

    public static String toStringQuality(float val) {
        return GuiShaders.toStringValue(val, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
    }

    public static String toStringHandDepth(float val) {
        return GuiShaders.toStringValue(val, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
    }

    public static int getOSType() {
        String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("win")) {
            return 1;
        }
        if (s.contains("mac")) {
            return 2;
        }
        if (s.contains("solaris")) {
            return 3;
        }
        if (s.contains("sunos")) {
            return 3;
        }
        if (s.contains("linux")) {
            return 4;
        }
        return s.contains("unix") ? 4 : 0;
    }
}

