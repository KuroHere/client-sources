/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.client.renderer.vertex;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorField;
import shadersmod.client.SVertexFormat;

public class DefaultVertexFormats {
    public static VertexFormat field_176600_a = new VertexFormat();
    public static VertexFormat field_176599_b = new VertexFormat();
    private static final VertexFormat BLOCK_VANILLA = field_176600_a;
    private static final VertexFormat ITEM_VANILLA = field_176599_b;
    private static final String __OBFID = "CL_00002403";

    static {
        field_176600_a.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
        field_176600_a.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.COLOR, 4));
        field_176600_a.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2));
        field_176600_a.func_177349_a(new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUseage.UV, 2));
        field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
        field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.COLOR, 4));
        field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2));
        field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUseage.NORMAL, 3));
        field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUseage.PADDING, 1));
    }

    public static void updateVertexFormats() {
        if (Config.isShaders()) {
            field_176600_a = SVertexFormat.makeDefVertexFormatBlock();
            field_176599_b = SVertexFormat.makeDefVertexFormatItem();
        } else {
            field_176600_a = BLOCK_VANILLA;
            field_176599_b = ITEM_VANILLA;
        }
        if (Reflector.Attributes_DEFAULT_BAKED_FORMAT.exists()) {
            VertexFormat vfSrc = field_176599_b;
            VertexFormat vfDst = (VertexFormat)Reflector.getFieldValue(Reflector.Attributes_DEFAULT_BAKED_FORMAT);
            vfDst.clear();
            for (int i2 = 0; i2 < vfSrc.func_177345_h(); ++i2) {
                vfDst.func_177349_a(vfSrc.func_177348_c(i2));
            }
        }
    }
}

