/*
 * Decompiled with CFR 0_118.
 */
package shadersmod.client;

import shadersmod.client.ShaderOption;
import shadersmod.client.Shaders;

public class ShaderOptionScreen
extends ShaderOption {
    public ShaderOptionScreen(String name) {
        super(name, null, null, new String[1], null, null);
    }

    @Override
    public String getNameText() {
        return Shaders.translate("screen." + this.getName(), this.getName());
    }

    @Override
    public String getDescriptionText() {
        return Shaders.translate("screen." + this.getName() + ".comment", null);
    }
}

