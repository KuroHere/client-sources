/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class SVertexAttrib {
    public int index;
    public int count;
    public VertexFormatElement.EnumType type;
    public int offset;

    public SVertexAttrib(int index, int count, VertexFormatElement.EnumType type2) {
        this.index = index;
        this.count = count;
        this.type = type2;
    }
}

