package me.darkmagician6.morbid.gui.screens;

import org.lwjgl.opengl.*;

public class PasswordField extends awx
{
    private final awv fontRenderer;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    private boolean enableBackgroundDrawing;
    private boolean canLoseFocus;
    private boolean isFocused;
    private boolean isEnabled;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor;
    private int disabledColor;
    private boolean visible;
    
    public PasswordField(final awv par1FontRenderer, final int par2, final int par3, final int par4, final int par5) {
        this.text = "";
        this.maxStringLength = 32;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isFocused = false;
        this.isEnabled = true;
        this.lineScrollOffset = 0;
        this.cursorPosition = 0;
        this.selectionEnd = 0;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.fontRenderer = par1FontRenderer;
        this.xPos = par2;
        this.yPos = par3;
        this.width = par4;
        this.height = par5;
    }
    
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
    
    public void setText(final String par1Str) {
        if (par1Str.length() > this.maxStringLength) {
            this.text = par1Str.substring(0, this.maxStringLength);
        }
        else {
            this.text = par1Str;
        }
        this.setCursorPositionEnd();
    }
    
    public String getText() {
        return this.text;
    }
    
    public String getSelectedtext() {
        final int var1 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int var2 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(var1, var2);
    }
    
    public void writeText(final String par1Str) {
        String var2 = "";
        final String var3 = v.a(par1Str);
        final int var4 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int var5 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        final int var6 = this.maxStringLength - this.text.length() - (var4 - this.selectionEnd);
        final boolean var7 = false;
        if (this.text.length() > 0) {
            var2 += this.text.substring(0, var4);
        }
        int var8;
        if (var6 < var3.length()) {
            var2 += var3.substring(0, var6);
            var8 = var6;
        }
        else {
            var2 += var3;
            var8 = var3.length();
        }
        if (this.text.length() > 0 && var5 < this.text.length()) {
            var2 += this.text.substring(var5);
        }
        this.text = var2;
        this.moveCursorBy(var4 - this.selectionEnd + var8);
    }
    
    public void deleteWords(final int par1) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                this.deleteFromCursor(this.getNthWordFromCursor(par1) - this.cursorPosition);
            }
        }
    }
    
    public void deleteFromCursor(final int par1) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                final boolean var2 = par1 < 0;
                final int var3 = var2 ? (this.cursorPosition + par1) : this.cursorPosition;
                final int var4 = var2 ? this.cursorPosition : (this.cursorPosition + par1);
                String var5 = "";
                if (var3 >= 0) {
                    var5 = this.text.substring(0, var3);
                }
                if (var4 < this.text.length()) {
                    var5 += this.text.substring(var4);
                }
                this.text = var5;
                if (var2) {
                    this.moveCursorBy(par1);
                }
            }
        }
    }
    
    public int getNthWordFromCursor(final int par1) {
        return this.getNthWordFromPos(par1, this.getCursorPosition());
    }
    
    public int getNthWordFromPos(final int par1, final int par2) {
        return this.func_73798_a(par1, this.getCursorPosition(), true);
    }
    
    public int func_73798_a(final int par1, final int par2, final boolean par3) {
        int var4 = par2;
        final boolean var5 = par1 < 0;
        for (int var6 = Math.abs(par1), var7 = 0; var7 < var6; ++var7) {
            if (var5) {
                while (par3 && var4 > 0 && this.text.charAt(var4 - 1) == ' ') {
                    --var4;
                }
                while (var4 > 0 && this.text.charAt(var4 - 1) != ' ') {
                    --var4;
                }
            }
            else {
                final int var8 = this.text.length();
                var4 = this.text.indexOf(32, var4);
                if (var4 == -1) {
                    var4 = var8;
                }
                else {
                    while (par3 && var4 < var8 && this.text.charAt(var4) == ' ') {
                        ++var4;
                    }
                }
            }
        }
        return var4;
    }
    
    public void moveCursorBy(final int par1) {
        this.setCursorPosition(this.selectionEnd + par1);
    }
    
    public void setCursorPosition(final int par1) {
        this.cursorPosition = par1;
        final int var2 = this.text.length();
        if (this.cursorPosition < 0) {
            this.cursorPosition = 0;
        }
        if (this.cursorPosition > var2) {
            this.cursorPosition = var2;
        }
        this.setSelectionPos(this.cursorPosition);
    }
    
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }
    
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }
    
    public boolean textboxKeyTyped(final char par1, final int par2) {
        if (!this.isEnabled || !this.isFocused) {
            return false;
        }
        switch (par1) {
            case '\u0001': {
                this.setCursorPositionEnd();
                this.setSelectionPos(0);
                return true;
            }
            case '\u0003': {
                axr.d(this.getSelectedtext());
                return true;
            }
            case '\u0016': {
                this.writeText(axr.l());
                return true;
            }
            case '\u0018': {
                axr.d(this.getSelectedtext());
                this.writeText("");
                return true;
            }
            default: {
                switch (par2) {
                    case 14: {
                        if (axr.o()) {
                            this.deleteWords(-1);
                        }
                        else {
                            this.deleteFromCursor(-1);
                        }
                        return true;
                    }
                    case 199: {
                        if (axr.p()) {
                            this.setSelectionPos(0);
                        }
                        else {
                            this.setCursorPositionZero();
                        }
                        return true;
                    }
                    case 203: {
                        if (axr.p()) {
                            if (axr.o()) {
                                this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                            }
                            else {
                                this.setSelectionPos(this.getSelectionEnd() - 1);
                            }
                        }
                        else if (axr.o()) {
                            this.setCursorPosition(this.getNthWordFromCursor(-1));
                        }
                        else {
                            this.moveCursorBy(-1);
                        }
                        return true;
                    }
                    case 205: {
                        if (axr.p()) {
                            if (axr.o()) {
                                this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                            }
                            else {
                                this.setSelectionPos(this.getSelectionEnd() + 1);
                            }
                        }
                        else if (axr.o()) {
                            this.setCursorPosition(this.getNthWordFromCursor(1));
                        }
                        else {
                            this.moveCursorBy(1);
                        }
                        return true;
                    }
                    case 207: {
                        if (axr.p()) {
                            this.setSelectionPos(this.text.length());
                        }
                        else {
                            this.setCursorPositionEnd();
                        }
                        return true;
                    }
                    case 211: {
                        if (axr.o()) {
                            this.deleteWords(1);
                        }
                        else {
                            this.deleteFromCursor(1);
                        }
                        return true;
                    }
                    default: {
                        if (v.a(par1)) {
                            this.writeText(Character.toString(par1));
                            return true;
                        }
                        return false;
                    }
                }
                break;
            }
        }
    }
    
    public void mouseClicked(final int par1, final int par2, final int par3) {
        final boolean var4 = par1 >= this.xPos && par1 < this.xPos + this.width && par2 >= this.yPos && par2 < this.yPos + this.height;
        if (this.canLoseFocus) {
            this.setFocused(this.isEnabled && var4);
        }
        if (this.isFocused && par3 == 0) {
            int var5 = par1 - this.xPos;
            if (this.enableBackgroundDrawing) {
                var5 -= 4;
            }
            final String var6 = this.fontRenderer.a(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRenderer.a(var6, var5).length() + this.lineScrollOffset);
        }
    }
    
    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                awx.a(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
                awx.a(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
            }
            final int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int var2 = this.cursorPosition - this.lineScrollOffset;
            int var3 = this.selectionEnd - this.lineScrollOffset;
            final String var4 = this.fontRenderer.a(this.text.substring(this.lineScrollOffset), this.getWidth());
            final boolean var5 = var2 >= 0 && var2 <= var4.length();
            final boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
            final int var7 = this.enableBackgroundDrawing ? (this.xPos + 4) : this.xPos;
            final int var8 = this.enableBackgroundDrawing ? (this.yPos + (this.height - 8) / 2) : this.yPos;
            int var9 = var7;
            if (var3 > var4.length()) {
                var3 = var4.length();
            }
            if (var4.length() > 0) {
                String var10 = var5 ? var4.substring(0, var2) : var4;
                var10 = var10.replaceAll(".", "*");
                var9 = this.fontRenderer.a(var10, var7, var8, var1);
            }
            final boolean var11 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int var12 = var9;
            if (!var5) {
                var12 = ((var2 > 0) ? (var7 + this.width) : var7);
            }
            else if (var11) {
                var12 = var9 - 1;
                --var9;
            }
            if (var4.length() > 0 && var5 && var2 < var4.length()) {
                this.fontRenderer.a(var4.substring(var2), var9, var8, var1);
            }
            if (var6) {
                if (var11) {
                    awx.a(var12, var8 - 1, var12 + 1, var8 + 1 + this.fontRenderer.a, -3092272);
                }
                else {
                    this.fontRenderer.a("_", var12, var8, var1);
                }
            }
            if (var3 != var2) {
                final int var13 = var7 + this.fontRenderer.a(var4.substring(0, var3));
                this.drawCursorVertical(var12, var8 - 1, var13 - 1, var8 + 1 + this.fontRenderer.a);
            }
        }
    }
    
    private void drawCursorVertical(int par1, int par2, int par3, int par4) {
        if (par1 < par3) {
            final int var5 = par1;
            par1 = par3;
            par3 = var5;
        }
        if (par2 < par4) {
            final int var5 = par2;
            par2 = par4;
            par4 = var5;
        }
        final bgd var6 = bgd.a;
        GL11.glColor4f(0.0f, 0.0f, 255.0f, 255.0f);
        GL11.glDisable(3553);
        GL11.glEnable(3058);
        GL11.glLogicOp(5387);
        var6.b();
        var6.a(par1, par4, 0.0);
        var6.a(par3, par4, 0.0);
        var6.a(par3, par2, 0.0);
        var6.a(par1, par2, 0.0);
        var6.a();
        GL11.glDisable(3058);
        GL11.glEnable(3553);
    }
    
    public void setMaxStringLength(final int par1) {
        this.maxStringLength = par1;
        if (this.text.length() > par1) {
            this.text = this.text.substring(0, par1);
        }
    }
    
    public int getMaxStringLength() {
        return this.maxStringLength;
    }
    
    public int getCursorPosition() {
        return this.cursorPosition;
    }
    
    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }
    
    public void setEnableBackgroundDrawing(final boolean par1) {
        this.enableBackgroundDrawing = par1;
    }
    
    public void setTextColor(final int par1) {
        this.enabledColor = par1;
    }
    
    public void setDisabledTextColour(final int par1) {
        this.disabledColor = par1;
    }
    
    public void setFocused(final boolean par1) {
        if (par1 && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = par1;
    }
    
    public boolean isFocused() {
        return this.isFocused;
    }
    
    public void setEnabled(final boolean par1) {
        this.isEnabled = par1;
    }
    
    public int getSelectionEnd() {
        return this.selectionEnd;
    }
    
    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
    }
    
    public void setSelectionPos(int par1) {
        final int var2 = this.text.length();
        if (par1 > var2) {
            par1 = var2;
        }
        if (par1 < 0) {
            par1 = 0;
        }
        this.selectionEnd = par1;
        if (this.fontRenderer != null) {
            if (this.lineScrollOffset > var2) {
                this.lineScrollOffset = var2;
            }
            final int var3 = this.getWidth();
            final String var4 = this.fontRenderer.a(this.text.substring(this.lineScrollOffset), var3);
            final int var5 = var4.length() + this.lineScrollOffset;
            if (par1 == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRenderer.a(this.text, var3, true).length();
            }
            if (par1 > var5) {
                this.lineScrollOffset += par1 - var5;
            }
            else if (par1 <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - par1;
            }
            if (this.lineScrollOffset < 0) {
                this.lineScrollOffset = 0;
            }
            if (this.lineScrollOffset > var2) {
                this.lineScrollOffset = var2;
            }
        }
    }
    
    public void setCanLoseFocus(final boolean par1) {
        this.canLoseFocus = par1;
    }
    
    public boolean getVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean par1) {
        this.visible = par1;
    }
}
