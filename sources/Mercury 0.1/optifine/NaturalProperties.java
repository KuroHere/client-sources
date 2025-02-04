/*
 * Decompiled with CFR 0.145.
 */
package optifine;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import optifine.Config;

public class NaturalProperties {
    public int rotation = 1;
    public boolean flip = false;
    private Map[] quadMaps = new Map[8];

    public NaturalProperties(String type) {
        if (type.equals("4")) {
            this.rotation = 4;
        } else if (type.equals("2")) {
            this.rotation = 2;
        } else if (type.equals("F")) {
            this.flip = true;
        } else if (type.equals("4F")) {
            this.rotation = 4;
            this.flip = true;
        } else if (type.equals("2F")) {
            this.rotation = 2;
            this.flip = true;
        } else {
            Config.warn("NaturalTextures: Unknown type: " + type);
        }
    }

    public boolean isValid() {
        return this.rotation != 2 && this.rotation != 4 ? this.flip : true;
    }

    public synchronized BakedQuad getQuad(BakedQuad quadIn, int rotate, boolean flipU) {
        int index = rotate;
        if (flipU) {
            index = rotate | 4;
        }
        if (index > 0 && index < this.quadMaps.length) {
            BakedQuad quad;
            IdentityHashMap map = this.quadMaps[index];
            if (map == null) {
                map = new IdentityHashMap(1);
                this.quadMaps[index] = map;
            }
            if ((quad = (BakedQuad)map.get(quadIn)) == null) {
                quad = this.makeQuad(quadIn, rotate, flipU);
                ((Map)map).put(quadIn, quad);
            }
            return quad;
        }
        return quadIn;
    }

    private BakedQuad makeQuad(BakedQuad quad, int rotate, boolean flipU) {
        int[] vertexData = quad.func_178209_a();
        int tintIndex = quad.func_178211_c();
        EnumFacing face = quad.getFace();
        TextureAtlasSprite sprite = quad.getSprite();
        if (!this.isFullSprite(quad)) {
            return quad;
        }
        vertexData = this.transformVertexData(vertexData, rotate, flipU);
        BakedQuad bq2 = new BakedQuad(vertexData, tintIndex, face, sprite);
        return bq2;
    }

    private int[] transformVertexData(int[] vertexData, int rotate, boolean flipU) {
        int[] vertexData2 = (int[])vertexData.clone();
        int v2 = 4 - rotate;
        if (flipU) {
            v2 += 3;
        }
        v2 %= 4;
        int step = vertexData2.length / 4;
        for (int v3 = 0; v3 < 4; ++v3) {
            int pos = v3 * step;
            int pos2 = v2 * step;
            vertexData2[pos2 + 4] = vertexData[pos + 4];
            vertexData2[pos2 + 4 + 1] = vertexData[pos + 4 + 1];
            if (flipU) {
                if (--v2 >= 0) continue;
                v2 = 3;
                continue;
            }
            if (++v2 <= 3) continue;
            v2 = 0;
        }
        return vertexData2;
    }

    private boolean isFullSprite(BakedQuad quad) {
        TextureAtlasSprite sprite = quad.getSprite();
        float uMin = sprite.getMinU();
        float uMax = sprite.getMaxU();
        float uSize = uMax - uMin;
        float uDelta = uSize / 256.0f;
        float vMin = sprite.getMinV();
        float vMax = sprite.getMaxV();
        float vSize = vMax - vMin;
        float vDelta = vSize / 256.0f;
        int[] vertexData = quad.func_178209_a();
        int step = vertexData.length / 4;
        for (int i2 = 0; i2 < 4; ++i2) {
            int pos = i2 * step;
            float u2 = Float.intBitsToFloat(vertexData[pos + 4]);
            float v2 = Float.intBitsToFloat(vertexData[pos + 4 + 1]);
            if (!this.equalsDelta(u2, uMin, uDelta) && !this.equalsDelta(u2, uMax, uDelta)) {
                return false;
            }
            if (this.equalsDelta(v2, vMin, vDelta) || this.equalsDelta(v2, vMax, vDelta)) continue;
            return false;
        }
        return true;
    }

    private boolean equalsDelta(float x1, float x2, float deltaMax) {
        float deltaAbs = MathHelper.abs(x1 - x2);
        return deltaAbs < deltaMax;
    }
}

