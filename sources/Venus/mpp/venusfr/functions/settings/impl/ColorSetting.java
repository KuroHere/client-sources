/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package mpp.venusfr.functions.settings.impl;

import java.util.function.Supplier;
import mpp.venusfr.functions.settings.Setting;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class ColorSetting
extends Setting<Integer> {
    public ColorSetting(String string, Integer n) {
        super(string, n);
    }

    public ColorSetting setVisible(Supplier<Boolean> supplier) {
        return (ColorSetting)super.setVisible(supplier);
    }

    @Override
    public Setting setVisible(Supplier supplier) {
        return this.setVisible(supplier);
    }
}

