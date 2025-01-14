/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package shadersmod.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.MatchBlock;
import optifine.PropertiesOrdered;
import optifine.StrUtils;
import shadersmod.client.BlockAlias;
import shadersmod.client.IShaderPack;

public class BlockAliases {
    private static BlockAlias[][] blockAliases = null;

    public static int getMappedBlockId(int blockId, int metadata) {
        if (blockAliases == null) {
            return blockId;
        }
        if (blockId >= 0 && blockId < blockAliases.length) {
            BlockAlias[] ablockalias = blockAliases[blockId];
            if (ablockalias == null) {
                return blockId;
            }
            for (int i = 0; i < ablockalias.length; ++i) {
                BlockAlias blockalias = ablockalias[i];
                if (!blockalias.matches(blockId, metadata)) continue;
                return blockalias.getBlockId();
            }
            return blockId;
        }
        return blockId;
    }

    public static void update(IShaderPack shaderPack) {
        BlockAliases.reset();
        String s = "/shaders/block.properties";
        try {
            InputStream inputstream = shaderPack.getResourceAsStream(s);
            if (inputstream == null) {
                return;
            }
            PropertiesOrdered properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            Config.dbg("[Shaders] Parsing block mappings: " + s);
            ArrayList<List<BlockAlias>> list = new ArrayList<List<BlockAlias>>();
            ConnectedParser connectedparser = new ConnectedParser("Shaders");
            for (Object s10 : ((Hashtable)properties).keySet()) {
                String s1 = (String)s10;
                String s2 = properties.getProperty(s1);
                String s3 = "block.";
                if (!s1.startsWith(s3)) {
                    Config.warn("[Shaders] Invalid block ID: " + s1);
                    continue;
                }
                String s4 = StrUtils.removePrefix(s1, s3);
                int i = Config.parseInt(s4, -1);
                if (i < 0) {
                    Config.warn("[Shaders] Invalid block ID: " + s1);
                    continue;
                }
                MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s2);
                if (amatchblock != null && amatchblock.length >= 1) {
                    BlockAlias blockalias = new BlockAlias(i, amatchblock);
                    BlockAliases.addToList(list, blockalias);
                    continue;
                }
                Config.warn("[Shaders] Invalid block ID mapping: " + s1 + "=" + s2);
            }
            if (list.size() <= 0) {
                return;
            }
            blockAliases = BlockAliases.toArrays(list);
        } catch (IOException var15) {
            Config.warn("[Shaders] Error reading: " + s);
        }
    }

    private static void addToList(List<List<BlockAlias>> blocksAliases, BlockAlias ba) {
        int[] aint = ba.getMatchBlockIds();
        for (int i = 0; i < aint.length; ++i) {
            int j = aint[i];
            while (j >= blocksAliases.size()) {
                blocksAliases.add(null);
            }
            List<BlockAlias> list = blocksAliases.get(j);
            if (list == null) {
                list = new ArrayList<BlockAlias>();
                blocksAliases.set(j, list);
            }
            list.add(ba);
        }
    }

    private static BlockAlias[][] toArrays(List<List<BlockAlias>> listBlocksAliases) {
        BlockAlias[][] ablockalias = new BlockAlias[listBlocksAliases.size()][];
        for (int i = 0; i < ablockalias.length; ++i) {
            List<BlockAlias> list = listBlocksAliases.get(i);
            if (list == null) continue;
            ablockalias[i] = list.toArray(new BlockAlias[list.size()]);
        }
        return ablockalias;
    }

    public static void reset() {
        blockAliases = null;
    }
}

