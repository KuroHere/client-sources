package frapppyz.cutefurry.pics.modules.settings;

import frapppyz.cutefurry.pics.modules.Mod;

public class Setting {
    protected String name;
    protected Mod parent;

    private boolean showed = true;
    private boolean holdingMouseButton;

    public Setting() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowed() {
        return showed;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }

    public boolean isHoldingMouseButton() {
        return holdingMouseButton;
    }

    public void setHoldingMouseButton(boolean holdingMouseButton) {
        this.holdingMouseButton = holdingMouseButton;
    }
}
