/*
 * Decompiled with CFR 0.152.
 */
package jx.utils.novoline.impl;

import java.awt.Font;
import jx.utils.novoline.api.FontFamily;
import jx.utils.novoline.api.FontRenderer;
import jx.utils.novoline.api.FontType;
import jx.utils.novoline.impl.SimpleFontRenderer;

final class SimpleFontFamily
implements FontFamily {
    private final FontType fontType;
    private final Font awtFont;

    private SimpleFontFamily(FontType fontType, Font awtFont) {
        this.fontType = fontType;
        this.awtFont = awtFont;
    }

    static FontFamily create(FontType fontType, Font awtFont) {
        return new SimpleFontFamily(fontType, awtFont);
    }

    @Override
    public FontRenderer ofSize(int size) {
        return SimpleFontRenderer.create(this.awtFont.deriveFont(0, size));
    }

    @Override
    public FontType font() {
        return this.fontType;
    }
}

