package net.mojang.blaze3d.vertex;

public abstract class DefaultColorVertexBuilder implements IVertexBuilder
{
    protected boolean defaultColor = false;
    protected int defaultRed = 255;
    protected int defaultGreen = 255;
    protected int defaultBlue = 255;
    protected int defaultAlpha = 255;

    public void setDefaultColor(int red, int green, int blue, int alpha)
    {
        this.defaultRed = red;
        this.defaultGreen = green;
        this.defaultBlue = blue;
        this.defaultAlpha = alpha;
        this.defaultColor = true;
    }
}
