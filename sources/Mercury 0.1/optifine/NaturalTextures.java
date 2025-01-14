/*
 * Decompiled with CFR 0.145.
 */
package optifine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.ConnectedTextures;
import optifine.NaturalProperties;
import optifine.TextureUtils;

public class NaturalTextures {
    private static NaturalProperties[] propertiesByIndex = new NaturalProperties[0];

    public static void update() {
        propertiesByIndex = new NaturalProperties[0];
        if (Config.isNaturalTextures()) {
            String fileName = "optifine/natural.properties";
            try {
                ResourceLocation e2 = new ResourceLocation(fileName);
                if (!Config.hasResource(e2)) {
                    Config.dbg("NaturalTextures: configuration \"" + fileName + "\" not found");
                    return;
                }
                boolean defaultConfig = Config.isFromDefaultResourcePack(e2);
                InputStream in2 = Config.getResourceStream(e2);
                ArrayList<NaturalProperties> list = new ArrayList<NaturalProperties>(256);
                String configStr = Config.readInputStream(in2);
                in2.close();
                String[] configLines = Config.tokenize(configStr, "\n\r");
                if (defaultConfig) {
                    Config.dbg("Natural Textures: Parsing default configuration \"" + fileName + "\"");
                    Config.dbg("Natural Textures: Valid only for textures from default resource pack");
                } else {
                    Config.dbg("Natural Textures: Parsing configuration \"" + fileName + "\"");
                }
                TextureMap textureMapBlocks = TextureUtils.getTextureMapBlocks();
                for (int i2 = 0; i2 < configLines.length; ++i2) {
                    String line = configLines[i2].trim();
                    if (line.startsWith("#")) continue;
                    String[] strs = Config.tokenize(line, "=");
                    if (strs.length != 2) {
                        Config.warn("Natural Textures: Invalid \"" + fileName + "\" line: " + line);
                        continue;
                    }
                    String key = strs[0].trim();
                    String type = strs[1].trim();
                    TextureAtlasSprite ts2 = textureMapBlocks.getSpriteSafe("minecraft:blocks/" + key);
                    if (ts2 == null) {
                        Config.warn("Natural Textures: Texture not found: \"" + fileName + "\" line: " + line);
                        continue;
                    }
                    int tileNum = ts2.getIndexInMap();
                    if (tileNum < 0) {
                        Config.warn("Natural Textures: Invalid \"" + fileName + "\" line: " + line);
                        continue;
                    }
                    if (defaultConfig && !Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/" + key + ".png"))) {
                        return;
                    }
                    NaturalProperties props = new NaturalProperties(type);
                    if (!props.isValid()) continue;
                    while (list.size() <= tileNum) {
                        list.add(null);
                    }
                    list.set(tileNum, props);
                    Config.dbg("NaturalTextures: " + key + " = " + type);
                }
                propertiesByIndex = list.toArray(new NaturalProperties[list.size()]);
            }
            catch (FileNotFoundException var17) {
                Config.warn("NaturalTextures: configuration \"" + fileName + "\" not found");
                return;
            }
            catch (Exception var18) {
                var18.printStackTrace();
            }
        }
    }

    public static BakedQuad getNaturalTexture(BlockPos blockPosIn, BakedQuad quad) {
        TextureAtlasSprite sprite = quad.getSprite();
        if (sprite == null) {
            return quad;
        }
        NaturalProperties nps = NaturalTextures.getNaturalProperties(sprite);
        if (nps == null) {
            return quad;
        }
        int side = ConnectedTextures.getSide(quad.getFace());
        int rand = Config.getRandom(blockPosIn, side);
        int rotate = 0;
        boolean flipU = false;
        if (nps.rotation > 1) {
            rotate = rand & 3;
        }
        if (nps.rotation == 2) {
            rotate = rotate / 2 * 2;
        }
        if (nps.flip) {
            flipU = (rand & 4) != 0;
        }
        return nps.getQuad(quad, rotate, flipU);
    }

    public static NaturalProperties getNaturalProperties(TextureAtlasSprite icon) {
        if (!(icon instanceof TextureAtlasSprite)) {
            return null;
        }
        int tileNum = icon.getIndexInMap();
        if (tileNum >= 0 && tileNum < propertiesByIndex.length) {
            NaturalProperties props = propertiesByIndex[tileNum];
            return props;
        }
        return null;
    }
}

