// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import java.awt.Graphics;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageBufferDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;
    private static final String __OBFID = "CL_00000956";
    
    @Override
    public BufferedImage parseUserSkin(final BufferedImage p_78432_1_) {
        if (p_78432_1_ == null) {
            return null;
        }
        this.imageWidth = 64;
        this.imageHeight = 64;
        final int srcWidth = p_78432_1_.getWidth();
        final int srcHeight = p_78432_1_.getHeight();
        int k = 1;
        while (this.imageWidth < srcWidth || this.imageHeight < srcHeight) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
            k *= 2;
        }
        final BufferedImage var2 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        final Graphics var3 = var2.getGraphics();
        var3.drawImage(p_78432_1_, 0, 0, null);
        if (p_78432_1_.getHeight() == 32 * k) {
            var3.drawImage(var2, 24 * k, 48 * k, 20 * k, 52 * k, 4 * k, 16 * k, 8 * k, 20 * k, null);
            var3.drawImage(var2, 28 * k, 48 * k, 24 * k, 52 * k, 8 * k, 16 * k, 12 * k, 20 * k, null);
            var3.drawImage(var2, 20 * k, 52 * k, 16 * k, 64 * k, 8 * k, 20 * k, 12 * k, 32 * k, null);
            var3.drawImage(var2, 24 * k, 52 * k, 20 * k, 64 * k, 4 * k, 20 * k, 8 * k, 32 * k, null);
            var3.drawImage(var2, 28 * k, 52 * k, 24 * k, 64 * k, 0 * k, 20 * k, 4 * k, 32 * k, null);
            var3.drawImage(var2, 32 * k, 52 * k, 28 * k, 64 * k, 12 * k, 20 * k, 16 * k, 32 * k, null);
            var3.drawImage(var2, 40 * k, 48 * k, 36 * k, 52 * k, 44 * k, 16 * k, 48 * k, 20 * k, null);
            var3.drawImage(var2, 44 * k, 48 * k, 40 * k, 52 * k, 48 * k, 16 * k, 52 * k, 20 * k, null);
            var3.drawImage(var2, 36 * k, 52 * k, 32 * k, 64 * k, 48 * k, 20 * k, 52 * k, 32 * k, null);
            var3.drawImage(var2, 40 * k, 52 * k, 36 * k, 64 * k, 44 * k, 20 * k, 48 * k, 32 * k, null);
            var3.drawImage(var2, 44 * k, 52 * k, 40 * k, 64 * k, 40 * k, 20 * k, 44 * k, 32 * k, null);
            var3.drawImage(var2, 48 * k, 52 * k, 44 * k, 64 * k, 52 * k, 20 * k, 56 * k, 32 * k, null);
        }
        var3.dispose();
        this.imageData = ((DataBufferInt)var2.getRaster().getDataBuffer()).getData();
        this.setAreaOpaque(0, 0, 32 * k, 16 * k);
        this.setAreaTransparent(32 * k, 0, 64 * k, 32 * k);
        this.setAreaOpaque(0, 16 * k, 64 * k, 32 * k);
        this.setAreaTransparent(0, 32 * k, 16 * k, 48 * k);
        this.setAreaTransparent(16 * k, 32 * k, 40 * k, 48 * k);
        this.setAreaTransparent(40 * k, 32 * k, 56 * k, 48 * k);
        this.setAreaTransparent(0, 48 * k, 16 * k, 64 * k);
        this.setAreaOpaque(16 * k, 48 * k, 48 * k, 64 * k);
        this.setAreaTransparent(48 * k, 48 * k, 64 * k, 64 * k);
        return var2;
    }
    
    @Override
    public void func_152634_a() {
    }
    
    private void setAreaTransparent(final int p_78434_1_, final int p_78434_2_, final int p_78434_3_, final int p_78434_4_) {
        if (!this.hasTransparency(p_78434_1_, p_78434_2_, p_78434_3_, p_78434_4_)) {
            for (int var5 = p_78434_1_; var5 < p_78434_3_; ++var5) {
                for (int var6 = p_78434_2_; var6 < p_78434_4_; ++var6) {
                    final int[] imageData = this.imageData;
                    final int n = var5 + var6 * this.imageWidth;
                    imageData[n] &= 0xFFFFFF;
                }
            }
        }
    }
    
    private void setAreaOpaque(final int p_78433_1_, final int p_78433_2_, final int p_78433_3_, final int p_78433_4_) {
        for (int var5 = p_78433_1_; var5 < p_78433_3_; ++var5) {
            for (int var6 = p_78433_2_; var6 < p_78433_4_; ++var6) {
                final int[] imageData = this.imageData;
                final int n = var5 + var6 * this.imageWidth;
                imageData[n] |= 0xFF000000;
            }
        }
    }
    
    private boolean hasTransparency(final int p_78435_1_, final int p_78435_2_, final int p_78435_3_, final int p_78435_4_) {
        for (int var5 = p_78435_1_; var5 < p_78435_3_; ++var5) {
            for (int var6 = p_78435_2_; var6 < p_78435_4_; ++var6) {
                final int var7 = this.imageData[var5 + var6 * this.imageWidth];
                if ((var7 >> 24 & 0xFF) < 128) {
                    return true;
                }
            }
        }
        return false;
    }
}
