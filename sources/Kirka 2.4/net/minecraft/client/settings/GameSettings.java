/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.client.settings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.src.ClearWater;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColorizer;
import net.minecraft.src.CustomSky;
import net.minecraft.src.NaturalTextures;
import net.minecraft.src.RandomMobs;
import net.minecraft.src.Reflector;
import net.minecraft.src.ReflectorClass;
import net.minecraft.src.ReflectorMethod;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameSettings {
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private static final ParameterizedType typeListString = new ParameterizedType(){
        private static final String __OBFID = "CL_00000651";

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    };
    private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] STREAM_COMPRESSIONS = new String[]{"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
    private static final String[] STREAM_CHAT_MODES = new String[]{"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
    private static final String[] STREAM_CHAT_FILTER_MODES = new String[]{"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
    private static final String[] STREAM_MIC_MODES = new String[]{"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
    public float mouseSensitivity = 0.5f;
    public boolean invertMouse;
    public int renderDistanceChunks = -1;
    public boolean viewBobbing = true;
    public boolean anaglyph;
    public boolean fboEnable = true;
    public int limitFramerate = 120;
    public int ofFogType = 1;
    public float ofFogStart = 0.8f;
    public int ofMipmapType = 0;
    public boolean ofLoadFar = false;
    public int ofPreloadedChunks = 0;
    public boolean ofOcclusionFancy = false;
    public boolean ofSmoothWorld = Config.isSingleProcessor();
    public boolean ofLazyChunkLoading = Config.isSingleProcessor();
    public float ofAoLevel = 1.0f;
    public int ofAaLevel = 0;
    public int ofClouds = 0;
    public float ofCloudsHeight = 0.0f;
    public int ofTrees = 0;
    public int ofRain = 0;
    public int ofDroppedItems = 0;
    public int ofBetterGrass = 3;
    public int ofAutoSaveTicks = 4000;
    public boolean ofLagometer = false;
    public boolean ofProfiler = false;
    public boolean ofWeather = true;
    public boolean ofSky = true;
    public boolean ofStars = true;
    public boolean ofSunMoon = true;
    public int ofChunkUpdates = 1;
    public int ofChunkLoading = 0;
    public boolean ofChunkUpdatesDynamic = false;
    public int ofTime = 0;
    public boolean ofClearWater = false;
    public boolean ofBetterSnow = false;
    public String ofFullscreenMode = "Default";
    public boolean ofSwampColors = true;
    public boolean ofRandomMobs = true;
    public boolean ofSmoothBiomes = true;
    public boolean ofCustomFonts = true;
    public boolean ofCustomColors = true;
    public boolean ofCustomSky = true;
    public boolean ofShowCapes = true;
    public int ofConnectedTextures = 2;
    public boolean ofNaturalTextures = false;
    public boolean ofFastMath = false;
    public boolean ofFastRender = true;
    public int ofTranslucentBlocks = 0;
    public int ofAnimatedWater = 0;
    public int ofAnimatedLava = 0;
    public boolean ofAnimatedFire = true;
    public boolean ofAnimatedPortal = true;
    public boolean ofAnimatedRedstone = true;
    public boolean ofAnimatedExplosion = true;
    public boolean ofAnimatedFlame = true;
    public boolean ofAnimatedSmoke = true;
    public boolean ofVoidParticles = true;
    public boolean ofWaterParticles = true;
    public boolean ofRainSplash = true;
    public boolean ofPortalParticles = true;
    public boolean ofPotionParticles = true;
    public boolean ofDrippingWaterLava = true;
    public boolean ofAnimatedTerrain = true;
    public boolean ofAnimatedTextures = true;
    public static final int DEFAULT = 0;
    public static final int FAST = 1;
    public static final int FANCY = 2;
    public static final int OFF = 3;
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public static final int CL_DEFAULT = 0;
    public static final int CL_SMOOTH = 1;
    public static final int CL_THREADED = 2;
    public static final String DEFAULT_STR = "Default";
    public KeyBinding ofKeyBindZoom;
    private File optionsFileOF;
    public boolean clouds = true;
    public boolean fancyGraphics = true;
    public int ambientOcclusion = 2;
    public List resourcePacks = Lists.newArrayList();
    public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
    public boolean chatColours = true;
    public boolean chatLinks = true;
    public boolean chatLinksPrompt = true;
    public float chatOpacity = 1.0f;
    public boolean snooperEnabled = true;
    public boolean fullScreen;
    public boolean enableVsync = true;
    public boolean field_178881_t = false;
    public boolean field_178880_u = true;
    public boolean field_178879_v = false;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus = true;
    private final Set field_178882_aU = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
    public boolean touchscreen;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips = true;
    public float chatScale = 1.0f;
    public float chatWidth = 1.0f;
    public float chatHeightUnfocused = 0.44366196f;
    public float chatHeightFocused = 1.0f;
    public boolean showInventoryAchievementHint = true;
    public int mipmapLevels = 4;
    private Map mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
    public float streamBytesPerPixel = 0.5f;
    public float streamMicVolume = 1.0f;
    public float streamGameVolume = 1.0f;
    public float streamKbps = 0.5412844f;
    public float streamFps = 0.31690142f;
    public int streamCompression = 1;
    public boolean streamSendMetadata = true;
    public String streamPreferredServer = "";
    public int streamChatEnabled = 0;
    public int streamChatUserFilter = 0;
    public int streamMicToggleBehavior = 0;
    public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
    public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
    public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
    public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
    public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
    public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
    public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
    public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
    public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
    public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.gameplay");
    public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
    public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
    public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
    public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
    public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
    public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
    public KeyBinding keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
    public KeyBinding field_178883_an = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
    public KeyBinding keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
    public KeyBinding keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
    public KeyBinding keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
    public KeyBinding keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
    public KeyBinding[] keyBindsHotbar = new KeyBinding[]{new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
    public KeyBinding[] keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindSprint, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.field_178883_an}, (Object[])this.keyBindsHotbar);
    protected Minecraft mc;
    private File optionsFile;
    public EnumDifficulty difficulty = EnumDifficulty.NORMAL;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public String lastServer = "";
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float fovSetting = 70.0f;
    public float gammaSetting;
    public float saturation;
    public int guiScale;
    public int particleSetting;
    public String language = "en_US";
    public boolean forceUnicodeFont = false;
    private static final String __OBFID = "CL_00000650";

    public GameSettings(Minecraft mcIn, File p_i46326_2_) {
        this.mc = mcIn;
        this.optionsFile = new File(p_i46326_2_, "options.txt");
        this.optionsFileOF = new File(p_i46326_2_, "optionsof.txt");
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.ofKeyBindZoom = new KeyBinding("Zoom", 46, "key.categories.misc");
        this.keyBindings = (KeyBinding[])ArrayUtils.add((Object[])this.keyBindings, (Object)this.ofKeyBindZoom);
        Options.RENDER_DISTANCE.setValueMax(32.0f);
        this.renderDistanceChunks = 8;
        this.loadOptions();
        Config.initGameSettings(this);
    }

    public GameSettings() {
    }

    public static String getKeyDisplayString(int p_74298_0_) {
        return p_74298_0_ < 0 ? I18n.format("key.mouseButton", p_74298_0_ + 101) : (p_74298_0_ < 256 ? Keyboard.getKeyName((int)p_74298_0_) : String.format("%c", Character.valueOf((char)(p_74298_0_ - 256))).toUpperCase());
    }

    public static boolean isKeyDown(KeyBinding p_100015_0_) {
        int keyCode = p_100015_0_.getKeyCode();
        return keyCode >= -100 && keyCode <= 255 ? (p_100015_0_.getKeyCode() == 0 ? false : (p_100015_0_.getKeyCode() < 0 ? Mouse.isButtonDown((int)(p_100015_0_.getKeyCode() + 100)) : Keyboard.isKeyDown((int)p_100015_0_.getKeyCode()))) : false;
    }

    public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_) {
        p_151440_1_.setKeyCode(p_151440_2_);
        this.saveOptions();
    }

    public void setOptionFloatValue(Options p_74304_1_, float p_74304_2_) {
        this.setOptionFloatValueOF(p_74304_1_, p_74304_2_);
        if (p_74304_1_ == Options.SENSITIVITY) {
            this.mouseSensitivity = p_74304_2_;
        }
        if (p_74304_1_ == Options.FOV) {
            this.fovSetting = p_74304_2_;
        }
        if (p_74304_1_ == Options.GAMMA) {
            this.gammaSetting = p_74304_2_;
        }
        if (p_74304_1_ == Options.FRAMERATE_LIMIT) {
            this.limitFramerate = (int)p_74304_2_;
            this.enableVsync = false;
            if (this.limitFramerate <= 0) {
                this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                this.enableVsync = true;
            }
            this.updateVSync();
        }
        if (p_74304_1_ == Options.CHAT_OPACITY) {
            this.chatOpacity = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_HEIGHT_FOCUSED) {
            this.chatHeightFocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_HEIGHT_UNFOCUSED) {
            this.chatHeightUnfocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_WIDTH) {
            this.chatWidth = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.CHAT_SCALE) {
            this.chatScale = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (p_74304_1_ == Options.MIPMAP_LEVELS) {
            int var3 = this.mipmapLevels;
            this.mipmapLevels = (int)p_74304_2_;
            if ((float)var3 != p_74304_2_) {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.mc.getTextureMapBlocks().func_174937_a(false, this.mipmapLevels > 0);
                this.mc.func_175603_A();
            }
        }
        if (p_74304_1_ == Options.BLOCK_ALTERNATIVES) {
            this.field_178880_u = !this.field_178880_u;
            Minecraft.renderGlobal.loadRenderers();
        }
        if (p_74304_1_ == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)p_74304_2_;
            Minecraft.renderGlobal.func_174979_m();
        }
        if (p_74304_1_ == Options.STREAM_BYTES_PER_PIXEL) {
            this.streamBytesPerPixel = p_74304_2_;
        }
        if (p_74304_1_ == Options.STREAM_VOLUME_MIC) {
            this.streamMicVolume = p_74304_2_;
            this.mc.getTwitchStream().func_152915_s();
        }
        if (p_74304_1_ == Options.STREAM_VOLUME_SYSTEM) {
            this.streamGameVolume = p_74304_2_;
            this.mc.getTwitchStream().func_152915_s();
        }
        if (p_74304_1_ == Options.STREAM_KBPS) {
            this.streamKbps = p_74304_2_;
        }
        if (p_74304_1_ == Options.STREAM_FPS) {
            this.streamFps = p_74304_2_;
        }
    }

    public void setOptionValue(Options p_74306_1_, int p_74306_2_) {
        this.setOptionValueOF(p_74306_1_, p_74306_2_);
        if (p_74306_1_ == Options.INVERT_MOUSE) {
            boolean bl = this.invertMouse = !this.invertMouse;
        }
        if (p_74306_1_ == Options.GUI_SCALE) {
            this.guiScale = this.guiScale + p_74306_2_ & 3;
        }
        if (p_74306_1_ == Options.PARTICLES) {
            this.particleSetting = (this.particleSetting + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.VIEW_BOBBING) {
            boolean bl = this.viewBobbing = !this.viewBobbing;
        }
        if (p_74306_1_ == Options.RENDER_CLOUDS) {
            boolean bl = this.clouds = !this.clouds;
        }
        if (p_74306_1_ == Options.FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }
        if (p_74306_1_ == Options.FBO_ENABLE) {
            boolean bl = this.fboEnable = !this.fboEnable;
        }
        if (p_74306_1_ == Options.ANAGLYPH) {
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }
        if (p_74306_1_ == Options.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            Minecraft.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = (this.ambientOcclusion + p_74306_2_) % 3;
            Minecraft.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.CHAT_VISIBILITY) {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + p_74306_2_) % 3);
        }
        if (p_74306_1_ == Options.STREAM_COMPRESSION) {
            this.streamCompression = (this.streamCompression + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.STREAM_SEND_METADATA) {
            boolean bl = this.streamSendMetadata = !this.streamSendMetadata;
        }
        if (p_74306_1_ == Options.STREAM_CHAT_ENABLED) {
            this.streamChatEnabled = (this.streamChatEnabled + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.STREAM_CHAT_USER_FILTER) {
            this.streamChatUserFilter = (this.streamChatUserFilter + p_74306_2_) % 3;
        }
        if (p_74306_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            this.streamMicToggleBehavior = (this.streamMicToggleBehavior + p_74306_2_) % 2;
        }
        if (p_74306_1_ == Options.CHAT_COLOR) {
            boolean bl = this.chatColours = !this.chatColours;
        }
        if (p_74306_1_ == Options.CHAT_LINKS) {
            boolean bl = this.chatLinks = !this.chatLinks;
        }
        if (p_74306_1_ == Options.CHAT_LINKS_PROMPT) {
            boolean bl = this.chatLinksPrompt = !this.chatLinksPrompt;
        }
        if (p_74306_1_ == Options.SNOOPER_ENABLED) {
            boolean bl = this.snooperEnabled = !this.snooperEnabled;
        }
        if (p_74306_1_ == Options.TOUCHSCREEN) {
            boolean bl = this.touchscreen = !this.touchscreen;
        }
        if (p_74306_1_ == Options.USE_FULLSCREEN) {
            boolean bl = this.fullScreen = !this.fullScreen;
            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }
        if (p_74306_1_ == Options.ENABLE_VSYNC) {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled((boolean)this.enableVsync);
        }
        if (p_74306_1_ == Options.USE_VBO) {
            this.field_178881_t = !this.field_178881_t;
            Minecraft.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.BLOCK_ALTERNATIVES) {
            this.field_178880_u = !this.field_178880_u;
            Minecraft.renderGlobal.loadRenderers();
        }
        if (p_74306_1_ == Options.REDUCED_DEBUG_INFO) {
            this.field_178879_v = !this.field_178879_v;
        }
        this.saveOptions();
    }

    public float getOptionFloatValue(Options p_74296_1_) {
        return p_74296_1_ == Options.CLOUD_HEIGHT ? this.ofCloudsHeight : (p_74296_1_ == Options.AO_LEVEL ? this.ofAoLevel : (p_74296_1_ == Options.FRAMERATE_LIMIT ? ((float)this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0f : (float)this.limitFramerate) : (p_74296_1_ == Options.FOV ? this.fovSetting : (p_74296_1_ == Options.GAMMA ? this.gammaSetting : (p_74296_1_ == Options.SATURATION ? this.saturation : (p_74296_1_ == Options.SENSITIVITY ? this.mouseSensitivity : (p_74296_1_ == Options.CHAT_OPACITY ? this.chatOpacity : (p_74296_1_ == Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (p_74296_1_ == Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (p_74296_1_ == Options.CHAT_SCALE ? this.chatScale : (p_74296_1_ == Options.CHAT_WIDTH ? this.chatWidth : (p_74296_1_ == Options.FRAMERATE_LIMIT ? (float)this.limitFramerate : (p_74296_1_ == Options.MIPMAP_LEVELS ? (float)this.mipmapLevels : (p_74296_1_ == Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : (p_74296_1_ == Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : (p_74296_1_ == Options.STREAM_VOLUME_MIC ? this.streamMicVolume : (p_74296_1_ == Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : (p_74296_1_ == Options.STREAM_KBPS ? this.streamKbps : (p_74296_1_ == Options.STREAM_FPS ? this.streamFps : 0.0f)))))))))))))))))));
    }

    public boolean getOptionOrdinalValue(Options p_74308_1_) {
        switch (SwitchOptions.optionIds[p_74308_1_.ordinal()]) {
            case 1: {
                return this.invertMouse;
            }
            case 2: {
                return this.viewBobbing;
            }
            case 3: {
                return this.anaglyph;
            }
            case 4: {
                return this.fboEnable;
            }
            case 5: {
                return this.clouds;
            }
            case 6: {
                return this.chatColours;
            }
            case 7: {
                return this.chatLinks;
            }
            case 8: {
                return this.chatLinksPrompt;
            }
            case 9: {
                return this.snooperEnabled;
            }
            case 10: {
                return this.fullScreen;
            }
            case 11: {
                return this.enableVsync;
            }
            case 12: {
                return this.field_178881_t;
            }
            case 13: {
                return this.touchscreen;
            }
            case 14: {
                return this.streamSendMetadata;
            }
            case 15: {
                return this.forceUnicodeFont;
            }
            case 16: {
                return this.field_178880_u;
            }
            case 17: {
                return this.field_178879_v;
            }
        }
        return false;
    }

    private static String getTranslation(String[] p_74299_0_, int p_74299_1_) {
        if (p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length) {
            p_74299_1_ = 0;
        }
        return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
    }

    public String getKeyBinding(Options p_74297_1_) {
        String strOF = this.getKeyBindingOF(p_74297_1_);
        if (strOF != null) {
            return strOF;
        }
        String var2 = String.valueOf(I18n.format(p_74297_1_.getEnumString(), new Object[0])) + ": ";
        if (p_74297_1_.getEnumFloat()) {
            float var32 = this.getOptionFloatValue(p_74297_1_);
            float var4 = p_74297_1_.normalizeValue(var32);
            return p_74297_1_ == Options.SENSITIVITY ? (var4 == 0.0f ? String.valueOf(var2) + I18n.format("options.sensitivity.min", new Object[0]) : (var4 == 1.0f ? String.valueOf(var2) + I18n.format("options.sensitivity.max", new Object[0]) : String.valueOf(var2) + (int)(var4 * 200.0f) + "%")) : (p_74297_1_ == Options.FOV ? (var32 == 70.0f ? String.valueOf(var2) + I18n.format("options.fov.min", new Object[0]) : (var32 == 110.0f ? String.valueOf(var2) + I18n.format("options.fov.max", new Object[0]) : String.valueOf(var2) + (int)var32)) : (p_74297_1_ == Options.FRAMERATE_LIMIT ? (var32 == p_74297_1_.valueMax ? String.valueOf(var2) + I18n.format("options.framerateLimit.max", new Object[0]) : String.valueOf(var2) + (int)var32 + " fps") : (p_74297_1_ == Options.RENDER_CLOUDS ? (var32 == p_74297_1_.valueMin ? String.valueOf(var2) + I18n.format("options.cloudHeight.min", new Object[0]) : String.valueOf(var2) + ((int)var32 + 128)) : (p_74297_1_ == Options.GAMMA ? (var4 == 0.0f ? String.valueOf(var2) + I18n.format("options.gamma.min", new Object[0]) : (var4 == 1.0f ? String.valueOf(var2) + I18n.format("options.gamma.max", new Object[0]) : String.valueOf(var2) + "+" + (int)(var4 * 100.0f) + "%")) : (p_74297_1_ == Options.SATURATION ? String.valueOf(var2) + (int)(var4 * 400.0f) + "%" : (p_74297_1_ == Options.CHAT_OPACITY ? String.valueOf(var2) + (int)(var4 * 90.0f + 10.0f) + "%" : (p_74297_1_ == Options.CHAT_HEIGHT_UNFOCUSED ? String.valueOf(var2) + GuiNewChat.calculateChatboxHeight(var4) + "px" : (p_74297_1_ == Options.CHAT_HEIGHT_FOCUSED ? String.valueOf(var2) + GuiNewChat.calculateChatboxHeight(var4) + "px" : (p_74297_1_ == Options.CHAT_WIDTH ? String.valueOf(var2) + GuiNewChat.calculateChatboxWidth(var4) + "px" : (p_74297_1_ == Options.RENDER_DISTANCE ? String.valueOf(var2) + (int)var32 + " chunks" : (p_74297_1_ == Options.MIPMAP_LEVELS ? (var32 == 0.0f ? String.valueOf(var2) + I18n.format("options.off", new Object[0]) : String.valueOf(var2) + (int)var32) : (p_74297_1_ == Options.STREAM_FPS ? String.valueOf(var2) + TwitchStream.func_152948_a(var4) + " fps" : (p_74297_1_ == Options.STREAM_KBPS ? String.valueOf(var2) + TwitchStream.func_152946_b(var4) + " Kbps" : (p_74297_1_ == Options.STREAM_BYTES_PER_PIXEL ? String.valueOf(var2) + String.format("%.3f bpp", Float.valueOf(TwitchStream.func_152947_c(var4))) : (var4 == 0.0f ? String.valueOf(var2) + I18n.format("options.off", new Object[0]) : String.valueOf(var2) + (int)(var4 * 100.0f) + "%")))))))))))))));
        }
        if (p_74297_1_.getEnumBoolean()) {
            boolean var31 = this.getOptionOrdinalValue(p_74297_1_);
            return var31 ? String.valueOf(var2) + I18n.format("options.on", new Object[0]) : String.valueOf(var2) + I18n.format("options.off", new Object[0]);
        }
        if (p_74297_1_ == Options.GUI_SCALE) {
            return String.valueOf(var2) + GameSettings.getTranslation(GUISCALES, this.guiScale);
        }
        if (p_74297_1_ == Options.CHAT_VISIBILITY) {
            return String.valueOf(var2) + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
        }
        if (p_74297_1_ == Options.PARTICLES) {
            return String.valueOf(var2) + GameSettings.getTranslation(PARTICLES, this.particleSetting);
        }
        if (p_74297_1_ == Options.AMBIENT_OCCLUSION) {
            return String.valueOf(var2) + GameSettings.getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (p_74297_1_ == Options.STREAM_COMPRESSION) {
            return String.valueOf(var2) + GameSettings.getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_ENABLED) {
            return String.valueOf(var2) + GameSettings.getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        if (p_74297_1_ == Options.STREAM_CHAT_USER_FILTER) {
            return String.valueOf(var2) + GameSettings.getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        if (p_74297_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return String.valueOf(var2) + GameSettings.getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        if (p_74297_1_ == Options.GRAPHICS) {
            if (this.fancyGraphics) {
                return String.valueOf(var2) + I18n.format("options.graphics.fancy", new Object[0]);
            }
            String var3 = "options.graphics.fast";
            return String.valueOf(var2) + I18n.format("options.graphics.fast", new Object[0]);
        }
        return var2;
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            BufferedReader var9 = new BufferedReader(new FileReader(this.optionsFile));
            String var2 = "";
            this.mapSoundLevels.clear();
            while ((var2 = var9.readLine()) != null) {
                try {
                    String[] var8 = var2.split(":");
                    if (var8[0].equals("mouseSensitivity")) {
                        this.mouseSensitivity = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("fov")) {
                        this.fovSetting = this.parseFloat(var8[1]) * 40.0f + 70.0f;
                    }
                    if (var8[0].equals("gamma")) {
                        this.gammaSetting = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("saturation")) {
                        this.saturation = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("invertYMouse")) {
                        this.invertMouse = var8[1].equals("true");
                    }
                    if (var8[0].equals("renderDistance")) {
                        this.renderDistanceChunks = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("guiScale")) {
                        this.guiScale = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("particles")) {
                        this.particleSetting = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("bobView")) {
                        this.viewBobbing = var8[1].equals("true");
                    }
                    if (var8[0].equals("anaglyph3d")) {
                        this.anaglyph = var8[1].equals("true");
                    }
                    if (var8[0].equals("maxFps")) {
                        this.limitFramerate = Integer.parseInt(var8[1]);
                        this.enableVsync = false;
                        if (this.limitFramerate <= 0) {
                            this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                            this.enableVsync = true;
                        }
                        this.updateVSync();
                    }
                    if (var8[0].equals("fboEnable")) {
                        this.fboEnable = var8[1].equals("true");
                    }
                    if (var8[0].equals("difficulty")) {
                        this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(var8[1]));
                    }
                    if (var8[0].equals("fancyGraphics")) {
                        this.fancyGraphics = var8[1].equals("true");
                    }
                    if (var8[0].equals("ao")) {
                        this.ambientOcclusion = var8[1].equals("true") ? 2 : (var8[1].equals("false") ? 0 : Integer.parseInt(var8[1]));
                    }
                    if (var8[0].equals("renderClouds")) {
                        this.clouds = var8[1].equals("true");
                    }
                    if (var8[0].equals("resourcePacks")) {
                        this.resourcePacks = (List)gson.fromJson(var2.substring(var2.indexOf(58) + 1), (Type)typeListString);
                        if (this.resourcePacks == null) {
                            this.resourcePacks = Lists.newArrayList();
                        }
                    }
                    if (var8[0].equals("lastServer") && var8.length >= 2) {
                        this.lastServer = var2.substring(var2.indexOf(58) + 1);
                    }
                    if (var8[0].equals("lang") && var8.length >= 2) {
                        this.language = var8[1];
                    }
                    if (var8[0].equals("chatVisibility")) {
                        this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(var8[1]));
                    }
                    if (var8[0].equals("chatColors")) {
                        this.chatColours = var8[1].equals("true");
                    }
                    if (var8[0].equals("chatLinks")) {
                        this.chatLinks = var8[1].equals("true");
                    }
                    if (var8[0].equals("chatLinksPrompt")) {
                        this.chatLinksPrompt = var8[1].equals("true");
                    }
                    if (var8[0].equals("chatOpacity")) {
                        this.chatOpacity = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("snooperEnabled")) {
                        this.snooperEnabled = var8[1].equals("true");
                    }
                    if (var8[0].equals("fullscreen")) {
                        this.fullScreen = var8[1].equals("true");
                    }
                    if (var8[0].equals("enableVsync")) {
                        this.enableVsync = var8[1].equals("true");
                        this.updateVSync();
                    }
                    if (var8[0].equals("useVbo")) {
                        this.field_178881_t = var8[1].equals("true");
                    }
                    if (var8[0].equals("hideServerAddress")) {
                        this.hideServerAddress = var8[1].equals("true");
                    }
                    if (var8[0].equals("advancedItemTooltips")) {
                        this.advancedItemTooltips = var8[1].equals("true");
                    }
                    if (var8[0].equals("pauseOnLostFocus")) {
                        this.pauseOnLostFocus = var8[1].equals("true");
                    }
                    if (var8[0].equals("touchscreen")) {
                        this.touchscreen = var8[1].equals("true");
                    }
                    if (var8[0].equals("overrideHeight")) {
                        this.overrideHeight = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("overrideWidth")) {
                        this.overrideWidth = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("heldItemTooltips")) {
                        this.heldItemTooltips = var8[1].equals("true");
                    }
                    if (var8[0].equals("chatHeightFocused")) {
                        this.chatHeightFocused = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("chatHeightUnfocused")) {
                        this.chatHeightUnfocused = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("chatScale")) {
                        this.chatScale = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("chatWidth")) {
                        this.chatWidth = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("showInventoryAchievementHint")) {
                        this.showInventoryAchievementHint = var8[1].equals("true");
                    }
                    if (var8[0].equals("mipmapLevels")) {
                        this.mipmapLevels = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("streamBytesPerPixel")) {
                        this.streamBytesPerPixel = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("streamMicVolume")) {
                        this.streamMicVolume = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("streamSystemVolume")) {
                        this.streamGameVolume = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("streamKbps")) {
                        this.streamKbps = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("streamFps")) {
                        this.streamFps = this.parseFloat(var8[1]);
                    }
                    if (var8[0].equals("streamCompression")) {
                        this.streamCompression = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("streamSendMetadata")) {
                        this.streamSendMetadata = var8[1].equals("true");
                    }
                    if (var8[0].equals("streamPreferredServer") && var8.length >= 2) {
                        this.streamPreferredServer = var2.substring(var2.indexOf(58) + 1);
                    }
                    if (var8[0].equals("streamChatEnabled")) {
                        this.streamChatEnabled = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("streamChatUserFilter")) {
                        this.streamChatUserFilter = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("streamMicToggleBehavior")) {
                        this.streamMicToggleBehavior = Integer.parseInt(var8[1]);
                    }
                    if (var8[0].equals("forceUnicodeFont")) {
                        this.forceUnicodeFont = var8[1].equals("true");
                    }
                    if (var8[0].equals("allowBlockAlternatives")) {
                        this.field_178880_u = var8[1].equals("true");
                    }
                    if (var8[0].equals("reducedDebugInfo")) {
                        this.field_178879_v = var8[1].equals("true");
                    }
                    for (KeyBinding var10 : this.keyBindings) {
                        if (!var8[0].equals("key_" + var10.getKeyDescription())) continue;
                        var10.setKeyCode(Integer.parseInt(var8[1]));
                    }
                    for (SoundCategory var11 : SoundCategory.values()) {
                        if (!var8[0].equals("soundCategory_" + var11.getCategoryName())) continue;
                        this.mapSoundLevels.put(var11, Float.valueOf(this.parseFloat(var8[1])));
                    }
                    for (EnumPlayerModelParts var13 : EnumPlayerModelParts.values()) {
                        if (!var8[0].equals("modelPart_" + var13.func_179329_c())) continue;
                        this.func_178878_a(var13, var8[1].equals("true"));
                    }
                }
                catch (Exception var101) {
                    logger.warn("Skipping bad option: " + var2);
                    var101.printStackTrace();
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            var9.close();
        }
        catch (Exception var111) {
            logger.error("Failed to load options", (Throwable)var111);
        }
        this.loadOfOptions();
    }

    private float parseFloat(String p_74305_1_) {
        return p_74305_1_.equals("true") ? 1.0f : (p_74305_1_.equals("false") ? 0.0f : Float.parseFloat(p_74305_1_));
    }

    public void saveOptions() {
        Object var6;
        if (Reflector.FMLClientHandler.exists() && (var6 = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0])) != null && Reflector.callBoolean(var6, Reflector.FMLClientHandler_isLoading, new Object[0])) {
            return;
        }
        try {
            PrintWriter var9 = new PrintWriter(new FileWriter(this.optionsFile));
            var9.println("invertYMouse:" + this.invertMouse);
            var9.println("mouseSensitivity:" + this.mouseSensitivity);
            var9.println("fov:" + (this.fovSetting - 70.0f) / 40.0f);
            var9.println("gamma:" + this.gammaSetting);
            var9.println("saturation:" + this.saturation);
            var9.println("renderDistance:" + this.renderDistanceChunks);
            var9.println("guiScale:" + this.guiScale);
            var9.println("particles:" + this.particleSetting);
            var9.println("bobView:" + this.viewBobbing);
            var9.println("anaglyph3d:" + this.anaglyph);
            var9.println("maxFps:" + this.limitFramerate);
            var9.println("fboEnable:" + this.fboEnable);
            var9.println("difficulty:" + this.difficulty.getDifficultyId());
            var9.println("fancyGraphics:" + this.fancyGraphics);
            var9.println("ao:" + this.ambientOcclusion);
            var9.println("renderClouds:" + this.clouds);
            var9.println("resourcePacks:" + gson.toJson((Object)this.resourcePacks));
            var9.println("lastServer:" + this.lastServer);
            var9.println("lang:" + this.language);
            var9.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
            var9.println("chatColors:" + this.chatColours);
            var9.println("chatLinks:" + this.chatLinks);
            var9.println("chatLinksPrompt:" + this.chatLinksPrompt);
            var9.println("chatOpacity:" + this.chatOpacity);
            var9.println("snooperEnabled:" + this.snooperEnabled);
            var9.println("fullscreen:" + this.fullScreen);
            var9.println("enableVsync:" + this.enableVsync);
            var9.println("useVbo:" + this.field_178881_t);
            var9.println("hideServerAddress:" + this.hideServerAddress);
            var9.println("advancedItemTooltips:" + this.advancedItemTooltips);
            var9.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
            var9.println("touchscreen:" + this.touchscreen);
            var9.println("overrideWidth:" + this.overrideWidth);
            var9.println("overrideHeight:" + this.overrideHeight);
            var9.println("heldItemTooltips:" + this.heldItemTooltips);
            var9.println("chatHeightFocused:" + this.chatHeightFocused);
            var9.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
            var9.println("chatScale:" + this.chatScale);
            var9.println("chatWidth:" + this.chatWidth);
            var9.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
            var9.println("mipmapLevels:" + this.mipmapLevels);
            var9.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
            var9.println("streamMicVolume:" + this.streamMicVolume);
            var9.println("streamSystemVolume:" + this.streamGameVolume);
            var9.println("streamKbps:" + this.streamKbps);
            var9.println("streamFps:" + this.streamFps);
            var9.println("streamCompression:" + this.streamCompression);
            var9.println("streamSendMetadata:" + this.streamSendMetadata);
            var9.println("streamPreferredServer:" + this.streamPreferredServer);
            var9.println("streamChatEnabled:" + this.streamChatEnabled);
            var9.println("streamChatUserFilter:" + this.streamChatUserFilter);
            var9.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
            var9.println("forceUnicodeFont:" + this.forceUnicodeFont);
            var9.println("allowBlockAlternatives:" + this.field_178880_u);
            var9.println("reducedDebugInfo:" + this.field_178879_v);
            for (KeyBinding var7 : this.keyBindings) {
                var9.println("key_" + var7.getKeyDescription() + ":" + var7.getKeyCode());
            }
            for (SoundCategory var8 : SoundCategory.values()) {
                var9.println("soundCategory_" + var8.getCategoryName() + ":" + this.getSoundLevel(var8));
            }
            for (EnumPlayerModelParts var10 : EnumPlayerModelParts.values()) {
                var9.println("modelPart_" + var10.func_179329_c() + ":" + this.field_178882_aU.contains((Object)var10));
            }
            var9.close();
        }
        catch (Exception var81) {
            logger.error("Failed to save options", (Throwable)var81);
        }
        this.saveOfOptions();
        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory p_151438_1_) {
        return this.mapSoundLevels.containsKey((Object)p_151438_1_) ? ((Float)this.mapSoundLevels.get((Object)p_151438_1_)).floatValue() : 1.0f;
    }

    public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_) {
        this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
        this.mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
    }

    public void sendSettingsToServer() {
        if (Minecraft.thePlayer != null) {
            int var1 = 0;
            for (EnumPlayerModelParts var3 : this.field_178882_aU) {
                var1 |= var3.func_179327_a();
            }
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, var1));
        }
    }

    public Set func_178876_d() {
        return ImmutableSet.copyOf((Collection)this.field_178882_aU);
    }

    public void func_178878_a(EnumPlayerModelParts p_178878_1_, boolean p_178878_2_) {
        if (p_178878_2_) {
            this.field_178882_aU.add(p_178878_1_);
        } else {
            this.field_178882_aU.remove((Object)p_178878_1_);
        }
        this.sendSettingsToServer();
    }

    public void func_178877_a(EnumPlayerModelParts p_178877_1_) {
        if (!this.func_178876_d().contains((Object)p_178877_1_)) {
            this.field_178882_aU.add(p_178877_1_);
        } else {
            this.field_178882_aU.remove((Object)p_178877_1_);
        }
        this.sendSettingsToServer();
    }

    public boolean shouldRenderClouds() {
        return this.renderDistanceChunks >= 4 && this.clouds;
    }

    private void setOptionFloatValueOF(Options option, float val) {
        if (option == Options.CLOUD_HEIGHT) {
            this.ofCloudsHeight = val;
            Minecraft.renderGlobal.resetClouds();
        }
        if (option == Options.AO_LEVEL) {
            this.ofAoLevel = val;
            Minecraft.renderGlobal.loadRenderers();
        }
    }

    private void setOptionValueOF(Options par1EnumOptions, int par2) {
        if (par1EnumOptions == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    this.ofFogType = 2;
                    if (Config.isFancyFogAvailable()) break;
                    this.ofFogType = 3;
                    break;
                }
                case 2: {
                    this.ofFogType = 3;
                    break;
                }
                case 3: {
                    this.ofFogType = 1;
                    break;
                }
                default: {
                    this.ofFogType = 1;
                }
            }
        }
        if (par1EnumOptions == Options.FOG_START) {
            this.ofFogStart += 0.2f;
            if (this.ofFogStart > 0.81f) {
                this.ofFogStart = 0.2f;
            }
        }
        if (par1EnumOptions == Options.MIPMAP_TYPE) {
            ++this.ofMipmapType;
            if (this.ofMipmapType > 3) {
                this.ofMipmapType = 0;
            }
            TextureUtils.refreshBlockTextures();
        }
        if (par1EnumOptions == Options.LOAD_FAR) {
            this.ofLoadFar = false;
        }
        if (par1EnumOptions == Options.PRELOADED_CHUNKS) {
            this.ofPreloadedChunks = 0;
        }
        if (par1EnumOptions == Options.SMOOTH_WORLD) {
            this.ofSmoothWorld = !this.ofSmoothWorld;
            Config.updateThreadPriorities();
        }
        if (par1EnumOptions == Options.CLOUDS) {
            ++this.ofClouds;
            if (this.ofClouds > 3) {
                this.ofClouds = 0;
            }
            Minecraft.renderGlobal.resetClouds();
        }
        if (par1EnumOptions == Options.TREES) {
            ++this.ofTrees;
            if (this.ofTrees > 2) {
                this.ofTrees = 0;
            }
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.DROPPED_ITEMS) {
            ++this.ofDroppedItems;
            if (this.ofDroppedItems > 2) {
                this.ofDroppedItems = 0;
            }
        }
        if (par1EnumOptions == Options.RAIN) {
            ++this.ofRain;
            if (this.ofRain > 3) {
                this.ofRain = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_WATER) {
            ++this.ofAnimatedWater;
            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_LAVA) {
            ++this.ofAnimatedLava;
            if (this.ofAnimatedLava > 2) {
                this.ofAnimatedLava = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_FIRE) {
            boolean bl = this.ofAnimatedFire = !this.ofAnimatedFire;
        }
        if (par1EnumOptions == Options.ANIMATED_PORTAL) {
            boolean bl = this.ofAnimatedPortal = !this.ofAnimatedPortal;
        }
        if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
            boolean bl = this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }
        if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
            boolean bl = this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }
        if (par1EnumOptions == Options.ANIMATED_FLAME) {
            boolean bl = this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }
        if (par1EnumOptions == Options.ANIMATED_SMOKE) {
            boolean bl = this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }
        if (par1EnumOptions == Options.VOID_PARTICLES) {
            boolean bl = this.ofVoidParticles = !this.ofVoidParticles;
        }
        if (par1EnumOptions == Options.WATER_PARTICLES) {
            boolean bl = this.ofWaterParticles = !this.ofWaterParticles;
        }
        if (par1EnumOptions == Options.PORTAL_PARTICLES) {
            boolean bl = this.ofPortalParticles = !this.ofPortalParticles;
        }
        if (par1EnumOptions == Options.POTION_PARTICLES) {
            boolean bl = this.ofPotionParticles = !this.ofPotionParticles;
        }
        if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
            boolean bl = this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
        }
        if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
            boolean bl = this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
        }
        if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
            boolean bl = this.ofAnimatedTextures = !this.ofAnimatedTextures;
        }
        if (par1EnumOptions == Options.RAIN_SPLASH) {
            boolean bl = this.ofRainSplash = !this.ofRainSplash;
        }
        if (par1EnumOptions == Options.LAGOMETER) {
            boolean bl = this.ofLagometer = !this.ofLagometer;
        }
        if (par1EnumOptions == Options.AUTOSAVE_TICKS) {
            this.ofAutoSaveTicks *= 10;
            if (this.ofAutoSaveTicks > 40000) {
                this.ofAutoSaveTicks = 40;
            }
        }
        if (par1EnumOptions == Options.BETTER_GRASS) {
            ++this.ofBetterGrass;
            if (this.ofBetterGrass > 3) {
                this.ofBetterGrass = 1;
            }
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CONNECTED_TEXTURES) {
            ++this.ofConnectedTextures;
            if (this.ofConnectedTextures > 3) {
                this.ofConnectedTextures = 1;
            }
            if (this.ofConnectedTextures != 2) {
                this.mc.func_175603_A();
            }
        }
        if (par1EnumOptions == Options.WEATHER) {
            boolean bl = this.ofWeather = !this.ofWeather;
        }
        if (par1EnumOptions == Options.SKY) {
            boolean bl = this.ofSky = !this.ofSky;
        }
        if (par1EnumOptions == Options.STARS) {
            boolean bl = this.ofStars = !this.ofStars;
        }
        if (par1EnumOptions == Options.SUN_MOON) {
            boolean bl = this.ofSunMoon = !this.ofSunMoon;
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES) {
            ++this.ofChunkUpdates;
            if (this.ofChunkUpdates > 5) {
                this.ofChunkUpdates = 1;
            }
        }
        if (par1EnumOptions == Options.CHUNK_LOADING) {
            ++this.ofChunkLoading;
            if (this.ofChunkLoading > 2) {
                this.ofChunkLoading = 0;
            }
            this.updateChunkLoading();
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
            boolean bl = this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }
        if (par1EnumOptions == Options.TIME) {
            ++this.ofTime;
            if (this.ofTime > 3) {
                this.ofTime = 0;
            }
        }
        if (par1EnumOptions == Options.CLEAR_WATER) {
            this.ofClearWater = !this.ofClearWater;
            this.updateWaterOpacity();
        }
        if (par1EnumOptions == Options.AA_LEVEL) {
            this.ofAaLevel = 0;
        }
        if (par1EnumOptions == Options.PROFILER) {
            boolean bl = this.ofProfiler = !this.ofProfiler;
        }
        if (par1EnumOptions == Options.BETTER_SNOW) {
            this.ofBetterSnow = !this.ofBetterSnow;
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.SWAMP_COLORS) {
            this.ofSwampColors = !this.ofSwampColors;
            CustomColorizer.updateUseDefaultColorMultiplier();
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.RANDOM_MOBS) {
            this.ofRandomMobs = !this.ofRandomMobs;
            RandomMobs.resetTextures();
        }
        if (par1EnumOptions == Options.SMOOTH_BIOMES) {
            this.ofSmoothBiomes = !this.ofSmoothBiomes;
            CustomColorizer.updateUseDefaultColorMultiplier();
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CUSTOM_FONTS) {
            this.ofCustomFonts = !this.ofCustomFonts;
            this.mc.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
            this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
        }
        if (par1EnumOptions == Options.CUSTOM_COLORS) {
            this.ofCustomColors = !this.ofCustomColors;
            CustomColorizer.update();
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CUSTOM_SKY) {
            this.ofCustomSky = !this.ofCustomSky;
            CustomSky.update();
        }
        if (par1EnumOptions == Options.SHOW_CAPES) {
            boolean bl = this.ofShowCapes = !this.ofShowCapes;
        }
        if (par1EnumOptions == Options.NATURAL_TEXTURES) {
            this.ofNaturalTextures = !this.ofNaturalTextures;
            NaturalTextures.update();
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.FAST_MATH) {
            MathHelper.fastMath = this.ofFastMath = !this.ofFastMath;
        }
        if (par1EnumOptions == Options.FAST_RENDER) {
            this.ofFastRender = !this.ofFastRender;
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS) {
            this.ofTranslucentBlocks = this.ofTranslucentBlocks == 0 ? 1 : (this.ofTranslucentBlocks == 1 ? 2 : (this.ofTranslucentBlocks == 2 ? 0 : 0));
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.LAZY_CHUNK_LOADING) {
            this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
            Config.updateAvailableProcessors();
            if (!Config.isSingleProcessor()) {
                this.ofLazyChunkLoading = false;
            }
            Minecraft.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.FULLSCREEN_MODE) {
            int index;
            List<String> modeList = Arrays.asList(Config.getFullscreenModes());
            this.ofFullscreenMode = this.ofFullscreenMode.equals(DEFAULT_STR) ? modeList.get(0) : ((index = modeList.indexOf(this.ofFullscreenMode)) < 0 ? DEFAULT_STR : (++index >= modeList.size() ? DEFAULT_STR : modeList.get(index)));
        }
        if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
            this.heldItemTooltips = !this.heldItemTooltips;
        }
    }

    private String getKeyBindingOF(Options par1EnumOptions) {
        String var2 = String.valueOf(I18n.format(par1EnumOptions.getEnumString(), new Object[0])) + ": ";
        if (var2 == null) {
            var2 = par1EnumOptions.getEnumString();
        }
        if (par1EnumOptions == Options.RENDER_DISTANCE) {
            int var61 = (int)this.getOptionFloatValue(par1EnumOptions);
            String str = "Tiny";
            int baseDist = 2;
            if (var61 >= 4) {
                str = "Short";
                baseDist = 4;
            }
            if (var61 >= 8) {
                str = "Normal";
                baseDist = 8;
            }
            if (var61 >= 16) {
                str = "Far";
                baseDist = 16;
            }
            if (var61 >= 32) {
                str = "Extreme";
                baseDist = 32;
            }
            int diff = this.renderDistanceChunks - baseDist;
            String descr = str;
            if (diff > 0) {
                descr = String.valueOf(str) + "+";
            }
            return String.valueOf(var2) + var61 + " " + descr;
        }
        if (par1EnumOptions == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    return String.valueOf(var2) + "Fast";
                }
                case 2: {
                    return String.valueOf(var2) + "Fancy";
                }
                case 3: {
                    return String.valueOf(var2) + "OFF";
                }
            }
            return String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.FOG_START) {
            return String.valueOf(var2) + this.ofFogStart;
        }
        if (par1EnumOptions == Options.MIPMAP_TYPE) {
            switch (this.ofMipmapType) {
                case 0: {
                    return String.valueOf(var2) + "Nearest";
                }
                case 1: {
                    return String.valueOf(var2) + "Linear";
                }
                case 2: {
                    return String.valueOf(var2) + "Bilinear";
                }
                case 3: {
                    return String.valueOf(var2) + "Trilinear";
                }
            }
            return String.valueOf(var2) + "Nearest";
        }
        if (par1EnumOptions == Options.LOAD_FAR) {
            return this.ofLoadFar ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.PRELOADED_CHUNKS) {
            return this.ofPreloadedChunks == 0 ? String.valueOf(var2) + "OFF" : String.valueOf(var2) + this.ofPreloadedChunks;
        }
        if (par1EnumOptions == Options.SMOOTH_WORLD) {
            return this.ofSmoothWorld ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.CLOUDS) {
            switch (this.ofClouds) {
                case 1: {
                    return String.valueOf(var2) + "Fast";
                }
                case 2: {
                    return String.valueOf(var2) + "Fancy";
                }
                case 3: {
                    return String.valueOf(var2) + "OFF";
                }
            }
            return String.valueOf(var2) + DEFAULT_STR;
        }
        if (par1EnumOptions == Options.TREES) {
            switch (this.ofTrees) {
                case 1: {
                    return String.valueOf(var2) + "Fast";
                }
                case 2: {
                    return String.valueOf(var2) + "Fancy";
                }
            }
            return String.valueOf(var2) + DEFAULT_STR;
        }
        if (par1EnumOptions == Options.DROPPED_ITEMS) {
            switch (this.ofDroppedItems) {
                case 1: {
                    return String.valueOf(var2) + "Fast";
                }
                case 2: {
                    return String.valueOf(var2) + "Fancy";
                }
            }
            return String.valueOf(var2) + DEFAULT_STR;
        }
        if (par1EnumOptions == Options.RAIN) {
            switch (this.ofRain) {
                case 1: {
                    return String.valueOf(var2) + "Fast";
                }
                case 2: {
                    return String.valueOf(var2) + "Fancy";
                }
                case 3: {
                    return String.valueOf(var2) + "OFF";
                }
            }
            return String.valueOf(var2) + DEFAULT_STR;
        }
        if (par1EnumOptions == Options.ANIMATED_WATER) {
            switch (this.ofAnimatedWater) {
                case 1: {
                    return String.valueOf(var2) + "Dynamic";
                }
                case 2: {
                    return String.valueOf(var2) + "OFF";
                }
            }
            return String.valueOf(var2) + "ON";
        }
        if (par1EnumOptions == Options.ANIMATED_LAVA) {
            switch (this.ofAnimatedLava) {
                case 1: {
                    return String.valueOf(var2) + "Dynamic";
                }
                case 2: {
                    return String.valueOf(var2) + "OFF";
                }
            }
            return String.valueOf(var2) + "ON";
        }
        if (par1EnumOptions == Options.ANIMATED_FIRE) {
            return this.ofAnimatedFire ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.ANIMATED_PORTAL) {
            return this.ofAnimatedPortal ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
            return this.ofAnimatedRedstone ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
            return this.ofAnimatedExplosion ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.ANIMATED_FLAME) {
            return this.ofAnimatedFlame ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.ANIMATED_SMOKE) {
            return this.ofAnimatedSmoke ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.VOID_PARTICLES) {
            return this.ofVoidParticles ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.WATER_PARTICLES) {
            return this.ofWaterParticles ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.PORTAL_PARTICLES) {
            return this.ofPortalParticles ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.POTION_PARTICLES) {
            return this.ofPotionParticles ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
            return this.ofDrippingWaterLava ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
            return this.ofAnimatedTerrain ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
            return this.ofAnimatedTextures ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.RAIN_SPLASH) {
            return this.ofRainSplash ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.LAGOMETER) {
            return this.ofLagometer ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.AUTOSAVE_TICKS) {
            return this.ofAutoSaveTicks <= 40 ? String.valueOf(var2) + "Default (2s)" : (this.ofAutoSaveTicks <= 400 ? String.valueOf(var2) + "20s" : (this.ofAutoSaveTicks <= 4000 ? String.valueOf(var2) + "3min" : String.valueOf(var2) + "30min"));
        }
        if (par1EnumOptions == Options.BETTER_GRASS) {
            switch (this.ofBetterGrass) {
                case 1: {
                    return String.valueOf(var2) + "Fast";
                }
                case 2: {
                    return String.valueOf(var2) + "Fancy";
                }
            }
            return String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.CONNECTED_TEXTURES) {
            switch (this.ofConnectedTextures) {
                case 1: {
                    return String.valueOf(var2) + "Fast";
                }
                case 2: {
                    return String.valueOf(var2) + "Fancy";
                }
            }
            return String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.WEATHER) {
            return this.ofWeather ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.SKY) {
            return this.ofSky ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.STARS) {
            return this.ofStars ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.SUN_MOON) {
            return this.ofSunMoon ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES) {
            return String.valueOf(var2) + this.ofChunkUpdates;
        }
        if (par1EnumOptions == Options.CHUNK_LOADING) {
            return this.ofChunkLoading == 1 ? String.valueOf(var2) + "Smooth" : (this.ofChunkLoading == 2 ? String.valueOf(var2) + "Multi-Core" : String.valueOf(var2) + DEFAULT_STR);
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
            return this.ofChunkUpdatesDynamic ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.TIME) {
            return this.ofTime == 1 ? String.valueOf(var2) + "Day Only" : (this.ofTime == 3 ? String.valueOf(var2) + "Night Only" : String.valueOf(var2) + DEFAULT_STR);
        }
        if (par1EnumOptions == Options.CLEAR_WATER) {
            return this.ofClearWater ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.AA_LEVEL) {
            return this.ofAaLevel == 0 ? String.valueOf(var2) + "OFF" : String.valueOf(var2) + this.ofAaLevel;
        }
        if (par1EnumOptions == Options.PROFILER) {
            return this.ofProfiler ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.BETTER_SNOW) {
            return this.ofBetterSnow ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.SWAMP_COLORS) {
            return this.ofSwampColors ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.RANDOM_MOBS) {
            return this.ofRandomMobs ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.SMOOTH_BIOMES) {
            return this.ofSmoothBiomes ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.CUSTOM_FONTS) {
            return this.ofCustomFonts ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.CUSTOM_COLORS) {
            return this.ofCustomColors ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.CUSTOM_SKY) {
            return this.ofCustomSky ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.SHOW_CAPES) {
            return this.ofShowCapes ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.NATURAL_TEXTURES) {
            return this.ofNaturalTextures ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.FAST_MATH) {
            return this.ofFastMath ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.FAST_RENDER) {
            return this.ofFastRender ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS) {
            return this.ofTranslucentBlocks == 1 ? String.valueOf(var2) + "Fast" : (this.ofTranslucentBlocks == 2 ? String.valueOf(var2) + "Fancy" : String.valueOf(var2) + DEFAULT_STR);
        }
        if (par1EnumOptions == Options.LAZY_CHUNK_LOADING) {
            return this.ofLazyChunkLoading ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.FULLSCREEN_MODE) {
            return String.valueOf(var2) + this.ofFullscreenMode;
        }
        if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
            return this.heldItemTooltips ? String.valueOf(var2) + "ON" : String.valueOf(var2) + "OFF";
        }
        if (par1EnumOptions == Options.FRAMERATE_LIMIT) {
            float var6 = this.getOptionFloatValue(par1EnumOptions);
            return var6 == 0.0f ? String.valueOf(var2) + "VSync" : (var6 == par1EnumOptions.valueMax ? String.valueOf(var2) + I18n.format("options.framerateLimit.max", new Object[0]) : String.valueOf(var2) + (int)var6 + " fps");
        }
        return null;
    }

    public void loadOfOptions() {
        try {
            File exception = this.optionsFileOF;
            if (!exception.exists()) {
                exception = this.optionsFile;
            }
            if (!exception.exists()) {
                return;
            }
            BufferedReader bufferedreader = new BufferedReader(new FileReader(exception));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                try {
                    String[] exception1 = s.split(":");
                    if (exception1[0].equals("ofRenderDistanceChunks") && exception1.length >= 2) {
                        this.renderDistanceChunks = Integer.valueOf(exception1[1]);
                        this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 32);
                    }
                    if (exception1[0].equals("ofFogType") && exception1.length >= 2) {
                        this.ofFogType = Integer.valueOf(exception1[1]);
                        this.ofFogType = Config.limit(this.ofFogType, 1, 3);
                    }
                    if (exception1[0].equals("ofFogStart") && exception1.length >= 2) {
                        this.ofFogStart = Float.valueOf(exception1[1]).floatValue();
                        if (this.ofFogStart < 0.2f) {
                            this.ofFogStart = 0.2f;
                        }
                        if (this.ofFogStart > 0.81f) {
                            this.ofFogStart = 0.8f;
                        }
                    }
                    if (exception1[0].equals("ofMipmapType") && exception1.length >= 2) {
                        this.ofMipmapType = Integer.valueOf(exception1[1]);
                        this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
                    }
                    if (exception1[0].equals("ofLoadFar") && exception1.length >= 2) {
                        this.ofLoadFar = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofPreloadedChunks") && exception1.length >= 2) {
                        this.ofPreloadedChunks = Integer.valueOf(exception1[1]);
                        if (this.ofPreloadedChunks < 0) {
                            this.ofPreloadedChunks = 0;
                        }
                        if (this.ofPreloadedChunks > 8) {
                            this.ofPreloadedChunks = 8;
                        }
                    }
                    if (exception1[0].equals("ofOcclusionFancy") && exception1.length >= 2) {
                        this.ofOcclusionFancy = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofSmoothWorld") && exception1.length >= 2) {
                        this.ofSmoothWorld = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAoLevel") && exception1.length >= 2) {
                        this.ofAoLevel = Float.valueOf(exception1[1]).floatValue();
                        this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0f, 1.0f);
                    }
                    if (exception1[0].equals("ofClouds") && exception1.length >= 2) {
                        this.ofClouds = Integer.valueOf(exception1[1]);
                        this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                    }
                    if (exception1[0].equals("ofCloudsHeight") && exception1.length >= 2) {
                        this.ofCloudsHeight = Float.valueOf(exception1[1]).floatValue();
                        this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0f, 1.0f);
                    }
                    if (exception1[0].equals("ofTrees") && exception1.length >= 2) {
                        this.ofTrees = Integer.valueOf(exception1[1]);
                        this.ofTrees = Config.limit(this.ofTrees, 0, 2);
                    }
                    if (exception1[0].equals("ofDroppedItems") && exception1.length >= 2) {
                        this.ofDroppedItems = Integer.valueOf(exception1[1]);
                        this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
                    }
                    if (exception1[0].equals("ofRain") && exception1.length >= 2) {
                        this.ofRain = Integer.valueOf(exception1[1]);
                        this.ofRain = Config.limit(this.ofRain, 0, 3);
                    }
                    if (exception1[0].equals("ofAnimatedWater") && exception1.length >= 2) {
                        this.ofAnimatedWater = Integer.valueOf(exception1[1]);
                        this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
                    }
                    if (exception1[0].equals("ofAnimatedLava") && exception1.length >= 2) {
                        this.ofAnimatedLava = Integer.valueOf(exception1[1]);
                        this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
                    }
                    if (exception1[0].equals("ofAnimatedFire") && exception1.length >= 2) {
                        this.ofAnimatedFire = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAnimatedPortal") && exception1.length >= 2) {
                        this.ofAnimatedPortal = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAnimatedRedstone") && exception1.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAnimatedExplosion") && exception1.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAnimatedFlame") && exception1.length >= 2) {
                        this.ofAnimatedFlame = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAnimatedSmoke") && exception1.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofVoidParticles") && exception1.length >= 2) {
                        this.ofVoidParticles = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofWaterParticles") && exception1.length >= 2) {
                        this.ofWaterParticles = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofPortalParticles") && exception1.length >= 2) {
                        this.ofPortalParticles = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofPotionParticles") && exception1.length >= 2) {
                        this.ofPotionParticles = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofDrippingWaterLava") && exception1.length >= 2) {
                        this.ofDrippingWaterLava = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAnimatedTerrain") && exception1.length >= 2) {
                        this.ofAnimatedTerrain = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAnimatedTextures") && exception1.length >= 2) {
                        this.ofAnimatedTextures = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofRainSplash") && exception1.length >= 2) {
                        this.ofRainSplash = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofLagometer") && exception1.length >= 2) {
                        this.ofLagometer = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofAutoSaveTicks") && exception1.length >= 2) {
                        this.ofAutoSaveTicks = Integer.valueOf(exception1[1]);
                        this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
                    }
                    if (exception1[0].equals("ofBetterGrass") && exception1.length >= 2) {
                        this.ofBetterGrass = Integer.valueOf(exception1[1]);
                        this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
                    }
                    if (exception1[0].equals("ofConnectedTextures") && exception1.length >= 2) {
                        this.ofConnectedTextures = Integer.valueOf(exception1[1]);
                        this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
                    }
                    if (exception1[0].equals("ofWeather") && exception1.length >= 2) {
                        this.ofWeather = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofSky") && exception1.length >= 2) {
                        this.ofSky = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofStars") && exception1.length >= 2) {
                        this.ofStars = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofSunMoon") && exception1.length >= 2) {
                        this.ofSunMoon = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofChunkUpdates") && exception1.length >= 2) {
                        this.ofChunkUpdates = Integer.valueOf(exception1[1]);
                        this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
                    }
                    if (exception1[0].equals("ofChunkLoading") && exception1.length >= 2) {
                        this.ofChunkLoading = Integer.valueOf(exception1[1]);
                        this.ofChunkLoading = Config.limit(this.ofChunkLoading, 0, 2);
                        this.updateChunkLoading();
                    }
                    if (exception1[0].equals("ofChunkUpdatesDynamic") && exception1.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofTime") && exception1.length >= 2) {
                        this.ofTime = Integer.valueOf(exception1[1]);
                        this.ofTime = Config.limit(this.ofTime, 0, 3);
                    }
                    if (exception1[0].equals("ofClearWater") && exception1.length >= 2) {
                        this.ofClearWater = Boolean.valueOf(exception1[1]);
                        this.updateWaterOpacity();
                    }
                    if (exception1[0].equals("ofAaLevel") && exception1.length >= 2) {
                        this.ofAaLevel = Integer.valueOf(exception1[1]);
                        this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
                    }
                    if (exception1[0].equals("ofProfiler") && exception1.length >= 2) {
                        this.ofProfiler = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofBetterSnow") && exception1.length >= 2) {
                        this.ofBetterSnow = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofSwampColors") && exception1.length >= 2) {
                        this.ofSwampColors = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofRandomMobs") && exception1.length >= 2) {
                        this.ofRandomMobs = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofSmoothBiomes") && exception1.length >= 2) {
                        this.ofSmoothBiomes = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofCustomFonts") && exception1.length >= 2) {
                        this.ofCustomFonts = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofCustomColors") && exception1.length >= 2) {
                        this.ofCustomColors = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofCustomSky") && exception1.length >= 2) {
                        this.ofCustomSky = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofShowCapes") && exception1.length >= 2) {
                        this.ofShowCapes = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofNaturalTextures") && exception1.length >= 2) {
                        this.ofNaturalTextures = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofLazyChunkLoading") && exception1.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofFullscreenMode") && exception1.length >= 2) {
                        this.ofFullscreenMode = exception1[1];
                    }
                    if (exception1[0].equals("ofFastMath") && exception1.length >= 2) {
                        MathHelper.fastMath = this.ofFastMath = Boolean.valueOf(exception1[1]).booleanValue();
                    }
                    if (exception1[0].equals("ofFastRender") && exception1.length >= 2) {
                        this.ofFastRender = Boolean.valueOf(exception1[1]);
                    }
                    if (exception1[0].equals("ofTranslucentBlocks") && exception1.length >= 2) {
                        this.ofTranslucentBlocks = Integer.valueOf(exception1[1]);
                        this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
                    }
                    if (!exception1[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) continue;
                    this.ofKeyBindZoom.setKeyCode(Integer.parseInt(exception1[1]));
                }
                catch (Exception var5) {
                    Config.dbg("Skipping bad option: " + s);
                    var5.printStackTrace();
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception var6) {
            Config.warn("Failed to load options");
            var6.printStackTrace();
        }
    }

    public void saveOfOptions() {
        try {
            PrintWriter exception = new PrintWriter(new FileWriter(this.optionsFileOF));
            exception.println("ofRenderDistanceChunks:" + this.renderDistanceChunks);
            exception.println("ofFogType:" + this.ofFogType);
            exception.println("ofFogStart:" + this.ofFogStart);
            exception.println("ofMipmapType:" + this.ofMipmapType);
            exception.println("ofLoadFar:" + this.ofLoadFar);
            exception.println("ofPreloadedChunks:" + this.ofPreloadedChunks);
            exception.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
            exception.println("ofSmoothWorld:" + this.ofSmoothWorld);
            exception.println("ofAoLevel:" + this.ofAoLevel);
            exception.println("ofClouds:" + this.ofClouds);
            exception.println("ofCloudsHeight:" + this.ofCloudsHeight);
            exception.println("ofTrees:" + this.ofTrees);
            exception.println("ofDroppedItems:" + this.ofDroppedItems);
            exception.println("ofRain:" + this.ofRain);
            exception.println("ofAnimatedWater:" + this.ofAnimatedWater);
            exception.println("ofAnimatedLava:" + this.ofAnimatedLava);
            exception.println("ofAnimatedFire:" + this.ofAnimatedFire);
            exception.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
            exception.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
            exception.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
            exception.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
            exception.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
            exception.println("ofVoidParticles:" + this.ofVoidParticles);
            exception.println("ofWaterParticles:" + this.ofWaterParticles);
            exception.println("ofPortalParticles:" + this.ofPortalParticles);
            exception.println("ofPotionParticles:" + this.ofPotionParticles);
            exception.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
            exception.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
            exception.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
            exception.println("ofRainSplash:" + this.ofRainSplash);
            exception.println("ofLagometer:" + this.ofLagometer);
            exception.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
            exception.println("ofBetterGrass:" + this.ofBetterGrass);
            exception.println("ofConnectedTextures:" + this.ofConnectedTextures);
            exception.println("ofWeather:" + this.ofWeather);
            exception.println("ofSky:" + this.ofSky);
            exception.println("ofStars:" + this.ofStars);
            exception.println("ofSunMoon:" + this.ofSunMoon);
            exception.println("ofChunkUpdates:" + this.ofChunkUpdates);
            exception.println("ofChunkLoading:" + this.ofChunkLoading);
            exception.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
            exception.println("ofTime:" + this.ofTime);
            exception.println("ofClearWater:" + this.ofClearWater);
            exception.println("ofAaLevel:" + this.ofAaLevel);
            exception.println("ofProfiler:" + this.ofProfiler);
            exception.println("ofBetterSnow:" + this.ofBetterSnow);
            exception.println("ofSwampColors:" + this.ofSwampColors);
            exception.println("ofRandomMobs:" + this.ofRandomMobs);
            exception.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
            exception.println("ofCustomFonts:" + this.ofCustomFonts);
            exception.println("ofCustomColors:" + this.ofCustomColors);
            exception.println("ofCustomSky:" + this.ofCustomSky);
            exception.println("ofShowCapes:" + this.ofShowCapes);
            exception.println("ofNaturalTextures:" + this.ofNaturalTextures);
            exception.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
            exception.println("ofFullscreenMode:" + this.ofFullscreenMode);
            exception.println("ofFastMath:" + this.ofFastMath);
            exception.println("ofFastRender:" + this.ofFastRender);
            exception.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
            exception.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
            exception.close();
        }
        catch (Exception var2) {
            Config.warn("Failed to save options");
            var2.printStackTrace();
        }
    }

    public void resetSettings() {
        this.renderDistanceChunks = 8;
        this.viewBobbing = true;
        this.anaglyph = false;
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.enableVsync = false;
        this.updateVSync();
        this.mipmapLevels = 4;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.clouds = true;
        this.fovSetting = 70.0f;
        this.gammaSetting = 0.0f;
        this.guiScale = 0;
        this.particleSetting = 0;
        this.heldItemTooltips = true;
        this.field_178881_t = false;
        this.field_178880_u = true;
        this.forceUnicodeFont = false;
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
        this.ofMipmapType = 0;
        this.ofLoadFar = false;
        this.ofPreloadedChunks = 0;
        this.ofOcclusionFancy = false;
        Config.updateAvailableProcessors();
        this.ofSmoothWorld = Config.isSingleProcessor();
        this.ofLazyChunkLoading = Config.isSingleProcessor();
        this.ofFastMath = false;
        this.ofFastRender = true;
        this.ofTranslucentBlocks = 0;
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
        this.ofTrees = 0;
        this.ofRain = 0;
        this.ofBetterGrass = 3;
        this.ofAutoSaveTicks = 4000;
        this.ofLagometer = false;
        this.ofProfiler = false;
        this.ofWeather = true;
        this.ofSky = true;
        this.ofStars = true;
        this.ofSunMoon = true;
        this.ofChunkUpdates = 1;
        this.ofChunkLoading = 0;
        this.ofChunkUpdatesDynamic = false;
        this.ofTime = 0;
        this.ofClearWater = false;
        this.ofBetterSnow = false;
        this.ofFullscreenMode = DEFAULT_STR;
        this.ofSwampColors = true;
        this.ofRandomMobs = true;
        this.ofSmoothBiomes = true;
        this.ofCustomFonts = true;
        this.ofCustomColors = true;
        this.ofCustomSky = true;
        this.ofShowCapes = true;
        this.ofConnectedTextures = 2;
        this.ofNaturalTextures = false;
        this.ofAnimatedWater = 0;
        this.ofAnimatedLava = 0;
        this.ofAnimatedFire = true;
        this.ofAnimatedPortal = true;
        this.ofAnimatedRedstone = true;
        this.ofAnimatedExplosion = true;
        this.ofAnimatedFlame = true;
        this.ofAnimatedSmoke = true;
        this.ofVoidParticles = true;
        this.ofWaterParticles = true;
        this.ofRainSplash = true;
        this.ofPortalParticles = true;
        this.ofPotionParticles = true;
        this.ofDrippingWaterLava = true;
        this.ofAnimatedTerrain = true;
        this.ofAnimatedTextures = true;
        this.updateWaterOpacity();
        this.mc.refreshResources();
        this.saveOptions();
    }

    public void updateVSync() {
        Display.setVSyncEnabled((boolean)this.enableVsync);
    }

    private void updateWaterOpacity() {
        if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
            Config.waterOpacityChanged = true;
        }
        ClearWater.updateWaterOpacity(this, Minecraft.theWorld);
    }

    public void updateChunkLoading() {
        if (Minecraft.renderGlobal != null) {
            Minecraft.renderGlobal.loadRenderers();
        }
    }

    public void setAllAnimations(boolean flag) {
        int animVal;
        this.ofAnimatedWater = animVal = flag ? 0 : 2;
        this.ofAnimatedLava = animVal;
        this.ofAnimatedFire = flag;
        this.ofAnimatedPortal = flag;
        this.ofAnimatedRedstone = flag;
        this.ofAnimatedExplosion = flag;
        this.ofAnimatedFlame = flag;
        this.ofAnimatedSmoke = flag;
        this.ofVoidParticles = flag;
        this.ofWaterParticles = flag;
        this.ofRainSplash = flag;
        this.ofPortalParticles = flag;
        this.ofPotionParticles = flag;
        this.particleSetting = flag ? 0 : 2;
        this.ofDrippingWaterLava = flag;
        this.ofAnimatedTerrain = flag;
        this.ofAnimatedTextures = flag;
    }

    public static enum Options {
        INVERT_MOUSE("INVERT_MOUSE", 0, "INVERT_MOUSE", 0, "options.invertMouse", false, true),
        SENSITIVITY("SENSITIVITY", 1, "SENSITIVITY", 1, "options.sensitivity", true, false),
        FOV("FOV", 2, "FOV", 2, "options.fov", true, false, 30.0f, 110.0f, 1.0f),
        GAMMA("GAMMA", 3, "GAMMA", 3, "options.gamma", true, false),
        SATURATION("SATURATION", 4, "SATURATION", 4, "options.saturation", true, false),
        RENDER_DISTANCE("RENDER_DISTANCE", 5, "RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0f, 16.0f, 1.0f),
        VIEW_BOBBING("VIEW_BOBBING", 6, "VIEW_BOBBING", 6, "options.viewBobbing", false, true),
        ANAGLYPH("ANAGLYPH", 7, "ANAGLYPH", 7, "options.anaglyph", false, true),
        FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "FRAMERATE_LIMIT", 8, "options.framerateLimit", true, false, 0.0f, 260.0f, 5.0f),
        FBO_ENABLE("FBO_ENABLE", 9, "FBO_ENABLE", 9, "options.fboEnable", false, true),
        RENDER_CLOUDS("RENDER_CLOUDS", 10, "RENDER_CLOUDS", 10, "options.renderClouds", false, true),
        GRAPHICS("GRAPHICS", 11, "GRAPHICS", 11, "options.graphics", false, false),
        AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 12, "AMBIENT_OCCLUSION", 12, "options.ao", false, false),
        GUI_SCALE("GUI_SCALE", 13, "GUI_SCALE", 13, "options.guiScale", false, false),
        PARTICLES("PARTICLES", 14, "PARTICLES", 14, "options.particles", false, false),
        CHAT_VISIBILITY("CHAT_VISIBILITY", 15, "CHAT_VISIBILITY", 15, "options.chat.visibility", false, false),
        CHAT_COLOR("CHAT_COLOR", 16, "CHAT_COLOR", 16, "options.chat.color", false, true),
        CHAT_LINKS("CHAT_LINKS", 17, "CHAT_LINKS", 17, "options.chat.links", false, true),
        CHAT_OPACITY("CHAT_OPACITY", 18, "CHAT_OPACITY", 18, "options.chat.opacity", true, false),
        CHAT_LINKS_PROMPT("CHAT_LINKS_PROMPT", 19, "CHAT_LINKS_PROMPT", 19, "options.chat.links.prompt", false, true),
        SNOOPER_ENABLED("SNOOPER_ENABLED", 20, "SNOOPER_ENABLED", 20, "options.snooper", false, true),
        USE_FULLSCREEN("USE_FULLSCREEN", 21, "USE_FULLSCREEN", 21, "options.fullscreen", false, true),
        ENABLE_VSYNC("ENABLE_VSYNC", 22, "ENABLE_VSYNC", 22, "options.vsync", false, true),
        USE_VBO("USE_VBO", 23, "USE_VBO", 23, "options.vbo", false, true),
        TOUCHSCREEN("TOUCHSCREEN", 24, "TOUCHSCREEN", 24, "options.touchscreen", false, true),
        CHAT_SCALE("CHAT_SCALE", 25, "CHAT_SCALE", 25, "options.chat.scale", true, false),
        CHAT_WIDTH("CHAT_WIDTH", 26, "CHAT_WIDTH", 26, "options.chat.width", true, false),
        CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 27, "CHAT_HEIGHT_FOCUSED", 27, "options.chat.height.focused", true, false),
        CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 28, "CHAT_HEIGHT_UNFOCUSED", 28, "options.chat.height.unfocused", true, false),
        MIPMAP_LEVELS("MIPMAP_LEVELS", 29, "MIPMAP_LEVELS", 29, "options.mipmapLevels", true, false, 0.0f, 4.0f, 1.0f),
        FORCE_UNICODE_FONT("FORCE_UNICODE_FONT", 30, "FORCE_UNICODE_FONT", 30, "options.forceUnicodeFont", false, true),
        STREAM_BYTES_PER_PIXEL("STREAM_BYTES_PER_PIXEL", 31, "STREAM_BYTES_PER_PIXEL", 31, "options.stream.bytesPerPixel", true, false),
        STREAM_VOLUME_MIC("STREAM_VOLUME_MIC", 32, "STREAM_VOLUME_MIC", 32, "options.stream.micVolumne", true, false),
        STREAM_VOLUME_SYSTEM("STREAM_VOLUME_SYSTEM", 33, "STREAM_VOLUME_SYSTEM", 33, "options.stream.systemVolume", true, false),
        STREAM_KBPS("STREAM_KBPS", 34, "STREAM_KBPS", 34, "options.stream.kbps", true, false),
        STREAM_FPS("STREAM_FPS", 35, "STREAM_FPS", 35, "options.stream.fps", true, false),
        STREAM_COMPRESSION("STREAM_COMPRESSION", 36, "STREAM_COMPRESSION", 36, "options.stream.compression", false, false),
        STREAM_SEND_METADATA("STREAM_SEND_METADATA", 37, "STREAM_SEND_METADATA", 37, "options.stream.sendMetadata", false, true),
        STREAM_CHAT_ENABLED("STREAM_CHAT_ENABLED", 38, "STREAM_CHAT_ENABLED", 38, "options.stream.chat.enabled", false, false),
        STREAM_CHAT_USER_FILTER("STREAM_CHAT_USER_FILTER", 39, "STREAM_CHAT_USER_FILTER", 39, "options.stream.chat.userFilter", false, false),
        STREAM_MIC_TOGGLE_BEHAVIOR("STREAM_MIC_TOGGLE_BEHAVIOR", 40, "STREAM_MIC_TOGGLE_BEHAVIOR", 40, "options.stream.micToggleBehavior", false, false),
        BLOCK_ALTERNATIVES("BLOCK_ALTERNATIVES", 41, "BLOCK_ALTERNATIVES", 41, "options.blockAlternatives", false, true),
        REDUCED_DEBUG_INFO("REDUCED_DEBUG_INFO", 42, "REDUCED_DEBUG_INFO", 42, "options.reducedDebugInfo", false, true),
        FOG_FANCY("FOG_FANCY", 43, "FOG", 999, "Fog", false, false),
        FOG_START("FOG_START", 44, "", 999, "Fog Start", false, false),
        MIPMAP_TYPE("MIPMAP_TYPE", 45, "", 999, "Mipmap Type", false, false),
        LOAD_FAR("LOAD_FAR", 46, "", 999, "Load Far", false, false),
        PRELOADED_CHUNKS("PRELOADED_CHUNKS", 47, "", 999, "Preloaded Chunks", false, false),
        CLOUDS("CLOUDS", 48, "", 999, "Clouds", false, false),
        CLOUD_HEIGHT("CLOUD_HEIGHT", 49, "", 999, "Cloud Height", true, false),
        TREES("TREES", 50, "", 999, "Trees", false, false),
        RAIN("RAIN", 51, "", 999, "Rain & Snow", false, false),
        ANIMATED_WATER("ANIMATED_WATER", 52, "", 999, "Water Animated", false, false),
        ANIMATED_LAVA("ANIMATED_LAVA", 53, "", 999, "Lava Animated", false, false),
        ANIMATED_FIRE("ANIMATED_FIRE", 54, "", 999, "Fire Animated", false, false),
        ANIMATED_PORTAL("ANIMATED_PORTAL", 55, "", 999, "Portal Animated", false, false),
        AO_LEVEL("AO_LEVEL", 56, "", 999, "Smooth Lighting Level", true, false),
        LAGOMETER("LAGOMETER", 57, "", 999, "Lagometer", false, false),
        AUTOSAVE_TICKS("AUTOSAVE_TICKS", 58, "", 999, "Autosave", false, false),
        BETTER_GRASS("BETTER_GRASS", 59, "", 999, "Better Grass", false, false),
        ANIMATED_REDSTONE("ANIMATED_REDSTONE", 60, "", 999, "Redstone Animated", false, false),
        ANIMATED_EXPLOSION("ANIMATED_EXPLOSION", 61, "", 999, "Explosion Animated", false, false),
        ANIMATED_FLAME("ANIMATED_FLAME", 62, "", 999, "Flame Animated", false, false),
        ANIMATED_SMOKE("ANIMATED_SMOKE", 63, "", 999, "Smoke Animated", false, false),
        WEATHER("WEATHER", 64, "", 999, "Weather", false, false),
        SKY("SKY", 65, "", 999, "Sky", false, false),
        STARS("STARS", 66, "", 999, "Stars", false, false),
        SUN_MOON("SUN_MOON", 67, "", 999, "Sun & Moon", false, false),
        CHUNK_UPDATES("CHUNK_UPDATES", 68, "", 999, "Chunk Updates", false, false),
        CHUNK_UPDATES_DYNAMIC("CHUNK_UPDATES_DYNAMIC", 69, "", 999, "Dynamic Updates", false, false),
        TIME("TIME", 70, "", 999, "Time", false, false),
        CLEAR_WATER("CLEAR_WATER", 71, "", 999, "Clear Water", false, false),
        SMOOTH_WORLD("SMOOTH_WORLD", 72, "", 999, "Smooth World", false, false),
        VOID_PARTICLES("VOID_PARTICLES", 73, "", 999, "Void Particles", false, false),
        WATER_PARTICLES("WATER_PARTICLES", 74, "", 999, "Water Particles", false, false),
        RAIN_SPLASH("RAIN_SPLASH", 75, "", 999, "Rain Splash", false, false),
        PORTAL_PARTICLES("PORTAL_PARTICLES", 76, "", 999, "Portal Particles", false, false),
        POTION_PARTICLES("POTION_PARTICLES", 77, "", 999, "Potion Particles", false, false),
        PROFILER("PROFILER", 78, "", 999, "Debug Profiler", false, false),
        DRIPPING_WATER_LAVA("DRIPPING_WATER_LAVA", 79, "", 999, "Dripping Water/Lava", false, false),
        BETTER_SNOW("BETTER_SNOW", 80, "", 999, "Better Snow", false, false),
        FULLSCREEN_MODE("FULLSCREEN_MODE", 81, "", 999, "Fullscreen Mode", false, false),
        ANIMATED_TERRAIN("ANIMATED_TERRAIN", 82, "", 999, "Terrain Animated", false, false),
        SWAMP_COLORS("SWAMP_COLORS", 83, "", 999, "Swamp Colors", false, false),
        RANDOM_MOBS("RANDOM_MOBS", 84, "", 999, "Random Mobs", false, false),
        SMOOTH_BIOMES("SMOOTH_BIOMES", 85, "", 999, "Smooth Biomes", false, false),
        CUSTOM_FONTS("CUSTOM_FONTS", 86, "", 999, "Custom Fonts", false, false),
        CUSTOM_COLORS("CUSTOM_COLORS", 87, "", 999, "Custom Colors", false, false),
        SHOW_CAPES("SHOW_CAPES", 88, "", 999, "Show Capes", false, false),
        CONNECTED_TEXTURES("CONNECTED_TEXTURES", 89, "", 999, "Connected Textures", false, false),
        AA_LEVEL("AA_LEVEL", 90, "", 999, "Antialiasing", false, false),
        AF_LEVEL("AF_LEVEL", 91, "", 999, "Anisotropic Filtering", false, false),
        ANIMATED_TEXTURES("ANIMATED_TEXTURES", 92, "", 999, "Textures Animated", false, false),
        NATURAL_TEXTURES("NATURAL_TEXTURES", 93, "", 999, "Natural Textures", false, false),
        CHUNK_LOADING("CHUNK_LOADING", 94, "", 999, "Chunk Loading", false, false),
        HELD_ITEM_TOOLTIPS("HELD_ITEM_TOOLTIPS", 95, "", 999, "Held Item Tooltips", false, false),
        DROPPED_ITEMS("DROPPED_ITEMS", 96, "", 999, "Dropped Items", false, false),
        LAZY_CHUNK_LOADING("LAZY_CHUNK_LOADING", 97, "", 999, "Lazy Chunk Loading", false, false),
        CUSTOM_SKY("CUSTOM_SKY", 98, "", 999, "Custom Sky", false, false),
        FAST_MATH("FAST_MATH", 99, "", 999, "Fast Math", false, false),
        FAST_RENDER("FAST_RENDER", 100, "", 999, "Fast Render", false, false),
        TRANSLUCENT_BLOCKS("TRANSLUCENT_BLOCKS", 101, "", 999, "Translucent Blocks", false, false);
        
        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;
        private static final Options[] $VALUES;
        private static final String __OBFID = "CL_00000653";
        private static final Options[] $VALUES$;

        static {
            $VALUES = new Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO};
            $VALUES$ = new Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, FOG_FANCY, FOG_START, MIPMAP_TYPE, LOAD_FAR, PRELOADED_CHUNKS, CLOUDS, CLOUD_HEIGHT, TREES, RAIN, ANIMATED_WATER, ANIMATED_LAVA, ANIMATED_FIRE, ANIMATED_PORTAL, AO_LEVEL, LAGOMETER, AUTOSAVE_TICKS, BETTER_GRASS, ANIMATED_REDSTONE, ANIMATED_EXPLOSION, ANIMATED_FLAME, ANIMATED_SMOKE, WEATHER, SKY, STARS, SUN_MOON, CHUNK_UPDATES, CHUNK_UPDATES_DYNAMIC, TIME, CLEAR_WATER, SMOOTH_WORLD, VOID_PARTICLES, WATER_PARTICLES, RAIN_SPLASH, PORTAL_PARTICLES, POTION_PARTICLES, PROFILER, DRIPPING_WATER_LAVA, BETTER_SNOW, FULLSCREEN_MODE, ANIMATED_TERRAIN, SWAMP_COLORS, RANDOM_MOBS, SMOOTH_BIOMES, CUSTOM_FONTS, CUSTOM_COLORS, SHOW_CAPES, CONNECTED_TEXTURES, AA_LEVEL, AF_LEVEL, ANIMATED_TEXTURES, NATURAL_TEXTURES, CHUNK_LOADING, HELD_ITEM_TOOLTIPS, DROPPED_ITEMS, LAZY_CHUNK_LOADING, CUSTOM_SKY, FAST_MATH, FAST_RENDER, TRANSLUCENT_BLOCKS};
        }

        public static Options getEnumOptions(int p_74379_0_) {
            for (Options var4 : Options.values()) {
                if (var4.returnEnumOrdinal() != p_74379_0_) continue;
                return var4;
            }
            return null;
        }

        private Options(String p_i46381_1_, int p_i46381_2_, String p_i1015_1_, int p_i1015_2_, String p_i1015_3_, boolean p_i1015_4_, boolean p_i1015_5_) {
            this(p_i46381_1_, p_i46381_2_, p_i1015_1_, p_i1015_2_, p_i1015_3_, p_i1015_4_, p_i1015_5_, 0.0f, 1.0f, 0.0f);
        }

        private Options(String p_i46382_1_, int p_i46382_2_, String p_i45004_1_, int p_i45004_2_, String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_, float p_i45004_8_) {
            this.enumString = p_i45004_3_;
            this.enumFloat = p_i45004_4_;
            this.enumBoolean = p_i45004_5_;
            this.valueMin = p_i45004_6_;
            this.valueMax = p_i45004_7_;
            this.valueStep = p_i45004_8_;
        }

        public boolean getEnumFloat() {
            return this.enumFloat;
        }

        public boolean getEnumBoolean() {
            return this.enumBoolean;
        }

        public int returnEnumOrdinal() {
            return this.ordinal();
        }

        public String getEnumString() {
            return this.enumString;
        }

        public float getValueMax() {
            return this.valueMax;
        }

        public void setValueMax(float p_148263_1_) {
            this.valueMax = p_148263_1_;
        }

        public float normalizeValue(float p_148266_1_) {
            return MathHelper.clamp_float((this.snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0f, 1.0f);
        }

        public float denormalizeValue(float p_148262_1_) {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0f, 1.0f));
        }

        public float snapToStepClamp(float p_148268_1_) {
            p_148268_1_ = this.snapToStep(p_148268_1_);
            return MathHelper.clamp_float(p_148268_1_, this.valueMin, this.valueMax);
        }

        protected float snapToStep(float p_148264_1_) {
            if (this.valueStep > 0.0f) {
                p_148264_1_ = this.valueStep * (float)Math.round(p_148264_1_ / this.valueStep);
            }
            return p_148264_1_;
        }
    }

    static final class SwitchOptions {
        static final int[] optionIds = new int[Options.values().length];
        private static final String __OBFID = "CL_00000652";

        static {
            try {
                SwitchOptions.optionIds[Options.INVERT_MOUSE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.VIEW_BOBBING.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.ANAGLYPH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.FBO_ENABLE.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.RENDER_CLOUDS.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.CHAT_COLOR.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.CHAT_LINKS.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.CHAT_LINKS_PROMPT.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.SNOOPER_ENABLED.ordinal()] = 9;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.USE_FULLSCREEN.ordinal()] = 10;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.ENABLE_VSYNC.ordinal()] = 11;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.USE_VBO.ordinal()] = 12;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.TOUCHSCREEN.ordinal()] = 13;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.STREAM_SEND_METADATA.ordinal()] = 14;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.FORCE_UNICODE_FONT.ordinal()] = 15;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.BLOCK_ALTERNATIVES.ordinal()] = 16;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchOptions.optionIds[Options.REDUCED_DEBUG_INFO.ordinal()] = 17;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchOptions() {
        }
    }

}

