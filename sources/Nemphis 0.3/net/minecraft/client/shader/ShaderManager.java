/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.shader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonBlendingMode;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderManager {
    private static final Logger logger = LogManager.getLogger();
    private static final ShaderDefault defaultShaderUniform = new ShaderDefault();
    private static ShaderManager staticShaderManager = null;
    private static int currentProgram = -1;
    private static boolean field_148000_e = true;
    private final Map shaderSamplers;
    private final List samplerNames;
    private final List shaderSamplerLocations;
    private final List shaderUniforms;
    private final List shaderUniformLocations;
    private final Map mappedShaderUniforms;
    private final int program;
    private final String programFilename;
    private final boolean useFaceCulling;
    private boolean isDirty;
    private final JsonBlendingMode field_148016_p;
    private final List field_148015_q;
    private final List field_148014_r;
    private final ShaderLoader vertexShaderLoader;
    private final ShaderLoader fragmentShaderLoader;
    private static final String __OBFID = "CL_00001040";

    public ShaderManager(IResourceManager resourceManager, String programName) throws JsonException {
        this.shaderSamplers = Maps.newHashMap();
        this.samplerNames = Lists.newArrayList();
        this.shaderSamplerLocations = Lists.newArrayList();
        this.shaderUniforms = Lists.newArrayList();
        this.shaderUniformLocations = Lists.newArrayList();
        this.mappedShaderUniforms = Maps.newHashMap();
        JsonParser var3 = new JsonParser();
        ResourceLocation var4 = new ResourceLocation("shaders/program/" + programName + ".json");
        this.programFilename = programName;
        InputStream var5 = null;
        try {
            try {
                JsonArray var29;
                var5 = resourceManager.getResource(var4).getInputStream();
                JsonObject var6 = var3.parse(IOUtils.toString((InputStream)var5, (Charset)Charsets.UTF_8)).getAsJsonObject();
                String var7 = JsonUtils.getJsonObjectStringFieldValue(var6, "vertex");
                String var28 = JsonUtils.getJsonObjectStringFieldValue(var6, "fragment");
                JsonArray var9 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "samplers", null);
                if (var9 != null) {
                    int var10 = 0;
                    for (JsonElement var12 : var9) {
                        try {
                            this.parseSampler(var12);
                        }
                        catch (Exception var25) {
                            JsonException var14 = JsonException.func_151379_a(var25);
                            var14.func_151380_a("samplers[" + var10 + "]");
                            throw var14;
                        }
                        ++var10;
                    }
                }
                if ((var29 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "attributes", null)) != null) {
                    int var30 = 0;
                    this.field_148015_q = Lists.newArrayListWithCapacity((int)var29.size());
                    this.field_148014_r = Lists.newArrayListWithCapacity((int)var29.size());
                    for (JsonElement var13 : var29) {
                        try {
                            this.field_148014_r.add(JsonUtils.getJsonElementStringValue(var13, "attribute"));
                        }
                        catch (Exception var24) {
                            JsonException var15 = JsonException.func_151379_a(var24);
                            var15.func_151380_a("attributes[" + var30 + "]");
                            throw var15;
                        }
                        ++var30;
                    }
                } else {
                    this.field_148015_q = null;
                    this.field_148014_r = null;
                }
                JsonArray var31 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "uniforms", null);
                if (var31 != null) {
                    int var33 = 0;
                    for (JsonElement var36 : var31) {
                        try {
                            this.parseUniform(var36);
                        }
                        catch (Exception var23) {
                            JsonException var16 = JsonException.func_151379_a(var23);
                            var16.func_151380_a("uniforms[" + var33 + "]");
                            throw var16;
                        }
                        ++var33;
                    }
                }
                this.field_148016_p = JsonBlendingMode.func_148110_a(JsonUtils.getJsonObjectFieldOrDefault(var6, "blend", null));
                this.useFaceCulling = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var6, "cull", true);
                this.vertexShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.VERTEX, var7);
                this.fragmentShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.FRAGMENT, var28);
                this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
                ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
                this.setupUniforms();
                if (this.field_148014_r != null) {
                    for (String var35 : this.field_148014_r) {
                        int var37 = OpenGlHelper.glGetAttribLocation(this.program, var35);
                        this.field_148015_q.add(var37);
                    }
                }
            }
            catch (Exception var26) {
                JsonException var8 = JsonException.func_151379_a(var26);
                var8.func_151381_b(var4.getResourcePath());
                throw var8;
            }
        }
        finally {
            IOUtils.closeQuietly((InputStream)var5);
        }
        this.markDirty();
    }

    public void deleteShader() {
        ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
    }

    public void endShader() {
        OpenGlHelper.glUseProgram(0);
        currentProgram = -1;
        staticShaderManager = null;
        field_148000_e = true;
        int var1 = 0;
        while (var1 < this.shaderSamplerLocations.size()) {
            if (this.shaderSamplers.get(this.samplerNames.get(var1)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + var1);
                GlStateManager.func_179144_i(0);
            }
            ++var1;
        }
    }

    public void useShader() {
        this.isDirty = false;
        staticShaderManager = this;
        this.field_148016_p.func_148109_a();
        if (this.program != currentProgram) {
            OpenGlHelper.glUseProgram(this.program);
            currentProgram = this.program;
        }
        if (this.useFaceCulling) {
            GlStateManager.enableCull();
        } else {
            GlStateManager.disableCull();
        }
        int var1 = 0;
        while (var1 < this.shaderSamplerLocations.size()) {
            if (this.shaderSamplers.get(this.samplerNames.get(var1)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + var1);
                GlStateManager.func_179098_w();
                Object var2 = this.shaderSamplers.get(this.samplerNames.get(var1));
                int var3 = -1;
                if (var2 instanceof Framebuffer) {
                    var3 = ((Framebuffer)var2).framebufferTexture;
                } else if (var2 instanceof ITextureObject) {
                    var3 = ((ITextureObject)var2).getGlTextureId();
                } else if (var2 instanceof Integer) {
                    var3 = (Integer)var2;
                }
                if (var3 != -1) {
                    GlStateManager.func_179144_i(var3);
                    OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, (CharSequence)this.samplerNames.get(var1)), var1);
                }
            }
            ++var1;
        }
        for (ShaderUniform var5 : this.shaderUniforms) {
            var5.upload();
        }
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public ShaderUniform getShaderUniform(String p_147991_1_) {
        return this.mappedShaderUniforms.containsKey(p_147991_1_) ? (ShaderUniform)this.mappedShaderUniforms.get(p_147991_1_) : null;
    }

    public ShaderUniform getShaderUniformOrDefault(String p_147984_1_) {
        return this.mappedShaderUniforms.containsKey(p_147984_1_) ? (ShaderUniform)this.mappedShaderUniforms.get(p_147984_1_) : defaultShaderUniform;
    }

    private void setupUniforms() {
        String var3;
        int var4;
        int var1 = 0;
        int var2 = 0;
        while (var1 < this.samplerNames.size()) {
            var3 = (String)this.samplerNames.get(var1);
            var4 = OpenGlHelper.glGetUniformLocation(this.program, var3);
            if (var4 == -1) {
                logger.warn("Shader " + this.programFilename + "could not find sampler named " + var3 + " in the specified shader program.");
                this.shaderSamplers.remove(var3);
                this.samplerNames.remove(var2);
                --var2;
            } else {
                this.shaderSamplerLocations.add(var4);
            }
            ++var1;
            ++var2;
        }
        for (ShaderUniform var6 : this.shaderUniforms) {
            var3 = var6.getShaderName();
            var4 = OpenGlHelper.glGetUniformLocation(this.program, var3);
            if (var4 == -1) {
                logger.warn("Could not find uniform named " + var3 + " in the specified" + " shader program.");
                continue;
            }
            this.shaderUniformLocations.add(var4);
            var6.setUniformLocation(var4);
            this.mappedShaderUniforms.put(var3, var6);
        }
    }

    private void parseSampler(JsonElement p_147996_1_) {
        JsonObject var2 = JsonUtils.getElementAsJsonObject(p_147996_1_, "sampler");
        String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        if (!JsonUtils.jsonObjectFieldTypeIsString(var2, "file")) {
            this.shaderSamplers.put(var3, null);
            this.samplerNames.add(var3);
        } else {
            this.samplerNames.add(var3);
        }
    }

    public void addSamplerTexture(String p_147992_1_, Object p_147992_2_) {
        if (this.shaderSamplers.containsKey(p_147992_1_)) {
            this.shaderSamplers.remove(p_147992_1_);
        }
        this.shaderSamplers.put(p_147992_1_, p_147992_2_);
        this.markDirty();
    }

    private void parseUniform(JsonElement p_147987_1_) throws JsonException {
        JsonObject var2 = JsonUtils.getElementAsJsonObject(p_147987_1_, "uniform");
        String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        int var4 = ShaderUniform.parseType(JsonUtils.getJsonObjectStringFieldValue(var2, "type"));
        int var5 = JsonUtils.getJsonObjectIntegerFieldValue(var2, "count");
        float[] var6 = new float[Math.max(var5, 16)];
        JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var2, "values");
        if (var7.size() != var5 && var7.size() > 1) {
            throw new JsonException("Invalid amount of values specified (expected " + var5 + ", found " + var7.size() + ")");
        }
        int var8 = 0;
        for (JsonElement var10 : var7) {
            try {
                var6[var8] = JsonUtils.getJsonElementFloatValue(var10, "value");
            }
            catch (Exception var13) {
                JsonException var12 = JsonException.func_151379_a(var13);
                var12.func_151380_a("values[" + var8 + "]");
                throw var12;
            }
            ++var8;
        }
        if (var5 > 1 && var7.size() == 1) {
            while (var8 < var5) {
                var6[var8] = var6[0];
                ++var8;
            }
        }
        int var14 = var5 > 1 && var5 <= 4 && var4 < 8 ? var5 - 1 : 0;
        ShaderUniform var15 = new ShaderUniform(var3, var4 + var14, var5, this);
        if (var4 <= 3) {
            var15.set((int)var6[0], (int)var6[1], (int)var6[2], (int)var6[3]);
        } else if (var4 <= 7) {
            var15.func_148092_b(var6[0], var6[1], var6[2], var6[3]);
        } else {
            var15.set(var6);
        }
        this.shaderUniforms.add(var15);
    }

    public ShaderLoader getVertexShaderLoader() {
        return this.vertexShaderLoader;
    }

    public ShaderLoader getFragmentShaderLoader() {
        return this.fragmentShaderLoader;
    }

    public int getProgram() {
        return this.program;
    }
}

