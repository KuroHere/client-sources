// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import net.minecraft.client.renderer.OpenGlHelper;
import javax.vecmath.Matrix4f;
import org.lwjgl.BufferUtils;
import org.apache.logging.log4j.LogManager;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.apache.logging.log4j.Logger;

public class ShaderUniform
{
    private static final Logger logger;
    private int uniformLocation;
    private final int uniformCount;
    private final int uniformType;
    private final IntBuffer uniformIntBuffer;
    private final FloatBuffer uniformFloatBuffer;
    private final String shaderName;
    private boolean field_148105_h;
    private final ShaderManager shaderManager;
    private static final String __OBFID = "CL_00001046";
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ShaderUniform(final String name, final int type, final int count, final ShaderManager manager) {
        this.shaderName = name;
        this.uniformCount = count;
        this.uniformType = type;
        this.shaderManager = manager;
        if (type <= 3) {
            this.uniformIntBuffer = BufferUtils.createIntBuffer(count);
            this.uniformFloatBuffer = null;
        }
        else {
            this.uniformIntBuffer = null;
            this.uniformFloatBuffer = BufferUtils.createFloatBuffer(count);
        }
        this.uniformLocation = -1;
        this.markDirty();
    }
    
    private void markDirty() {
        this.field_148105_h = true;
        if (this.shaderManager != null) {
            this.shaderManager.markDirty();
        }
    }
    
    public static int parseType(final String p_148085_0_) {
        byte var1 = -1;
        if (p_148085_0_.equals("int")) {
            var1 = 0;
        }
        else if (p_148085_0_.equals("float")) {
            var1 = 4;
        }
        else if (p_148085_0_.startsWith("matrix")) {
            if (p_148085_0_.endsWith("2x2")) {
                var1 = 8;
            }
            else if (p_148085_0_.endsWith("3x3")) {
                var1 = 9;
            }
            else if (p_148085_0_.endsWith("4x4")) {
                var1 = 10;
            }
        }
        return var1;
    }
    
    public void setUniformLocation(final int p_148084_1_) {
        this.uniformLocation = p_148084_1_;
    }
    
    public String getShaderName() {
        return this.shaderName;
    }
    
    public void set(final float p_148090_1_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148090_1_);
        this.markDirty();
    }
    
    public void set(final float p_148087_1_, final float p_148087_2_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148087_1_);
        this.uniformFloatBuffer.put(1, p_148087_2_);
        this.markDirty();
    }
    
    public void set(final float p_148095_1_, final float p_148095_2_, final float p_148095_3_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148095_1_);
        this.uniformFloatBuffer.put(1, p_148095_2_);
        this.uniformFloatBuffer.put(2, p_148095_3_);
        this.markDirty();
    }
    
    public void set(final float p_148081_1_, final float p_148081_2_, final float p_148081_3_, final float p_148081_4_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(p_148081_1_);
        this.uniformFloatBuffer.put(p_148081_2_);
        this.uniformFloatBuffer.put(p_148081_3_);
        this.uniformFloatBuffer.put(p_148081_4_);
        this.uniformFloatBuffer.flip();
        this.markDirty();
    }
    
    public void func_148092_b(final float p_148092_1_, final float p_148092_2_, final float p_148092_3_, final float p_148092_4_) {
        this.uniformFloatBuffer.position(0);
        if (this.uniformType >= 4) {
            this.uniformFloatBuffer.put(0, p_148092_1_);
        }
        if (this.uniformType >= 5) {
            this.uniformFloatBuffer.put(1, p_148092_2_);
        }
        if (this.uniformType >= 6) {
            this.uniformFloatBuffer.put(2, p_148092_3_);
        }
        if (this.uniformType >= 7) {
            this.uniformFloatBuffer.put(3, p_148092_4_);
        }
        this.markDirty();
    }
    
    public void set(final int p_148083_1_, final int p_148083_2_, final int p_148083_3_, final int p_148083_4_) {
        this.uniformIntBuffer.position(0);
        if (this.uniformType >= 0) {
            this.uniformIntBuffer.put(0, p_148083_1_);
        }
        if (this.uniformType >= 1) {
            this.uniformIntBuffer.put(1, p_148083_2_);
        }
        if (this.uniformType >= 2) {
            this.uniformIntBuffer.put(2, p_148083_3_);
        }
        if (this.uniformType >= 3) {
            this.uniformIntBuffer.put(3, p_148083_4_);
        }
        this.markDirty();
    }
    
    public void set(final float[] p_148097_1_) {
        if (p_148097_1_.length < this.uniformCount) {
            ShaderUniform.logger.warn("Uniform.set called with a too-small value array (expected " + this.uniformCount + ", got " + p_148097_1_.length + "). Ignoring.");
        }
        else {
            this.uniformFloatBuffer.position(0);
            this.uniformFloatBuffer.put(p_148097_1_);
            this.uniformFloatBuffer.position(0);
            this.markDirty();
        }
    }
    
    public void set(final float p_148094_1_, final float p_148094_2_, final float p_148094_3_, final float p_148094_4_, final float p_148094_5_, final float p_148094_6_, final float p_148094_7_, final float p_148094_8_, final float p_148094_9_, final float p_148094_10_, final float p_148094_11_, final float p_148094_12_, final float p_148094_13_, final float p_148094_14_, final float p_148094_15_, final float p_148094_16_) {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148094_1_);
        this.uniformFloatBuffer.put(1, p_148094_2_);
        this.uniformFloatBuffer.put(2, p_148094_3_);
        this.uniformFloatBuffer.put(3, p_148094_4_);
        this.uniformFloatBuffer.put(4, p_148094_5_);
        this.uniformFloatBuffer.put(5, p_148094_6_);
        this.uniformFloatBuffer.put(6, p_148094_7_);
        this.uniformFloatBuffer.put(7, p_148094_8_);
        this.uniformFloatBuffer.put(8, p_148094_9_);
        this.uniformFloatBuffer.put(9, p_148094_10_);
        this.uniformFloatBuffer.put(10, p_148094_11_);
        this.uniformFloatBuffer.put(11, p_148094_12_);
        this.uniformFloatBuffer.put(12, p_148094_13_);
        this.uniformFloatBuffer.put(13, p_148094_14_);
        this.uniformFloatBuffer.put(14, p_148094_15_);
        this.uniformFloatBuffer.put(15, p_148094_16_);
        this.markDirty();
    }
    
    public void set(final Matrix4f p_148088_1_) {
        this.set(p_148088_1_.m00, p_148088_1_.m01, p_148088_1_.m02, p_148088_1_.m03, p_148088_1_.m10, p_148088_1_.m11, p_148088_1_.m12, p_148088_1_.m13, p_148088_1_.m20, p_148088_1_.m21, p_148088_1_.m22, p_148088_1_.m23, p_148088_1_.m30, p_148088_1_.m31, p_148088_1_.m32, p_148088_1_.m33);
    }
    
    public void upload() {
        if (!this.field_148105_h) {}
        this.field_148105_h = false;
        if (this.uniformType <= 3) {
            this.uploadInt();
        }
        else if (this.uniformType <= 7) {
            this.uploadFloat();
        }
        else {
            if (this.uniformType > 10) {
                ShaderUniform.logger.warn("Uniform.upload called, but type value (" + this.uniformType + ") is not " + "a valid type. Ignoring.");
                return;
            }
            this.uploadFloatMatrix();
        }
    }
    
    private void uploadInt() {
        switch (this.uniformType) {
            case 0: {
                OpenGlHelper.glUniform1(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            case 1: {
                OpenGlHelper.glUniform2(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            case 2: {
                OpenGlHelper.glUniform3(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            case 3: {
                OpenGlHelper.glUniform4(this.uniformLocation, this.uniformIntBuffer);
                break;
            }
            default: {
                ShaderUniform.logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + " not in the range of 1 to 4. Ignoring.");
                break;
            }
        }
    }
    
    private void uploadFloat() {
        switch (this.uniformType) {
            case 4: {
                OpenGlHelper.glUniform1(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            case 5: {
                OpenGlHelper.glUniform2(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            case 6: {
                OpenGlHelper.glUniform3(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            case 7: {
                OpenGlHelper.glUniform4(this.uniformLocation, this.uniformFloatBuffer);
                break;
            }
            default: {
                ShaderUniform.logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + "not in the range of 1 to 4. Ignoring.");
                break;
            }
        }
    }
    
    private void uploadFloatMatrix() {
        switch (this.uniformType) {
            case 8: {
                OpenGlHelper.glUniformMatrix2(this.uniformLocation, true, this.uniformFloatBuffer);
                break;
            }
            case 9: {
                OpenGlHelper.glUniformMatrix3(this.uniformLocation, true, this.uniformFloatBuffer);
                break;
            }
            case 10: {
                OpenGlHelper.glUniformMatrix4(this.uniformLocation, true, this.uniformFloatBuffer);
                break;
            }
        }
    }
    
    public ShaderManager getShaderManager() {
        return this.shaderManager;
    }
}
