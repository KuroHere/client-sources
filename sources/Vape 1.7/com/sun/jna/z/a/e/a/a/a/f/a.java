package com.sun.jna.z.a.e.a.a.a.f;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import java.awt.*;

public class a
{
    public static int b;
    
    public static void a(final int a, final int a, final int a, final int a) {
        final int a2 = a - a;
        final int a3 = a - a;
        final ScaledResolution a4 = new ScaledResolution(Minecraft.func_71410_x(), Minecraft.func_71410_x().field_71443_c, Minecraft.func_71410_x().field_71440_d);
        final int a5 = a4.func_78325_e();
        final int a6 = Minecraft.func_71410_x().field_71462_r.field_146295_m - a;
        GL11.glScissor(a * a5, a6 * a5, a2 * a5, a3 * a5);
    }
    
    public static void a() {
        GL11.glEnable(3042);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glHint(3154, 4354);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(32925);
        GL11.glEnable(32926);
        GL11.glShadeModel(7425);
    }
    
    public static void a(final double a, final double a, final double a, final double a, final double a, final double a, final float a) {
        GL11.glPushMatrix();
        a();
        GL11.glLineWidth(a);
        GL11.glBegin(1);
        GL11.glVertex3d(a, a, a);
        GL11.glVertex3d(a, a, a);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDisable(32925);
        GL11.glDisable(32926);
        GL11.glPopMatrix();
    }
    
    public static void a(final int a, final int a, final int a, final int a, final int a) {
        final double a2 = a / 2;
        final double a3 = a / 2;
        GL11.glDisable(2884);
        GL11.glBindTexture(3553, a);
        GL11.glPushMatrix();
        GL11.glTranslated(a + a3, a + a2, 0.0);
        GL11.glScalef((float)a, (float)a, 0.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(4);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(1.0, 1.0);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d(-1.0, 1.0);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(-1.0, -1.0);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(-1.0, -1.0);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d(1.0, -1.0);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(1.0, 1.0);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glBindTexture(3553, 0);
        GL11.glPopMatrix();
    }
    
    public static void a(final double a, final double a, final double a, final double a, final int a, final int a) {
    }
    
    public static int a(final int a, final int a, final float a) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: sipush          255
        //     4: iand           
        //     5: istore          5
        //     7: getstatic       com/sun/jna/z/a/e/a/a/a/f/a.b:I
        //    10: iload_0         /* a */
        //    11: bipush          8
        //    13: ishr           
        //    14: sipush          255
        //    17: iand           
        //    18: istore          a
        //    20: iload_0         /* a */
        //    21: bipush          16
        //    23: ishr           
        //    24: sipush          255
        //    27: iand           
        //    28: istore          a
        //    30: istore_3        /* a */
        //    31: iload_0         /* a */
        //    32: bipush          24
        //    34: ishr           
        //    35: sipush          255
        //    38: iand           
        //    39: istore          a
        //    41: iload_1         /* a */
        //    42: sipush          255
        //    45: iand           
        //    46: istore          a
        //    48: iload_1         /* a */
        //    49: bipush          8
        //    51: ishr           
        //    52: sipush          255
        //    55: iand           
        //    56: istore          a
        //    58: iload_1         /* a */
        //    59: bipush          16
        //    61: ishr           
        //    62: sipush          255
        //    65: iand           
        //    66: istore          a
        //    68: iload_1         /* a */
        //    69: bipush          24
        //    71: ishr           
        //    72: sipush          255
        //    75: iand           
        //    76: istore          a
        //    78: iload           a
        //    80: iload_3         /* a */
        //    81: ifne            106
        //    84: iload           a
        //    86: if_icmpge       104
        //    89: iload           a
        //    91: i2f            
        //    92: iload           a
        //    94: iload           a
        //    96: isub           
        //    97: i2f            
        //    98: fload_2         /* a */
        //    99: fmul           
        //   100: fadd           
        //   101: goto            116
        //   104: iload           a
        //   106: i2f            
        //   107: iload           a
        //   109: iload           a
        //   111: isub           
        //   112: i2f            
        //   113: fload_2         /* a */
        //   114: fmul           
        //   115: fadd           
        //   116: f2i            
        //   117: istore          a
        //   119: iload           a
        //   121: iload_3         /* a */
        //   122: ifne            147
        //   125: iload           a
        //   127: if_icmpge       145
        //   130: iload           a
        //   132: i2f            
        //   133: iload           a
        //   135: iload           a
        //   137: isub           
        //   138: i2f            
        //   139: fload_2         /* a */
        //   140: fmul           
        //   141: fadd           
        //   142: goto            157
        //   145: iload           a
        //   147: i2f            
        //   148: iload           a
        //   150: iload           a
        //   152: isub           
        //   153: i2f            
        //   154: fload_2         /* a */
        //   155: fmul           
        //   156: fadd           
        //   157: f2i            
        //   158: istore          a
        //   160: iload           a
        //   162: iload_3         /* a */
        //   163: ifne            188
        //   166: iload           a
        //   168: if_icmpge       186
        //   171: iload           a
        //   173: i2f            
        //   174: iload           a
        //   176: iload           a
        //   178: isub           
        //   179: i2f            
        //   180: fload_2         /* a */
        //   181: fmul           
        //   182: fadd           
        //   183: goto            198
        //   186: iload           a
        //   188: i2f            
        //   189: iload           a
        //   191: iload           a
        //   193: isub           
        //   194: i2f            
        //   195: fload_2         /* a */
        //   196: fmul           
        //   197: fadd           
        //   198: f2i            
        //   199: istore          a
        //   201: iload           a
        //   203: iload_3         /* a */
        //   204: ifne            229
        //   207: iload           a
        //   209: if_icmpge       227
        //   212: iload           a
        //   214: i2f            
        //   215: iload           a
        //   217: iload           a
        //   219: isub           
        //   220: i2f            
        //   221: fload_2         /* a */
        //   222: fmul           
        //   223: fadd           
        //   224: goto            239
        //   227: iload           a
        //   229: i2f            
        //   230: iload           a
        //   232: iload           a
        //   234: isub           
        //   235: i2f            
        //   236: fload_2         /* a */
        //   237: fmul           
        //   238: fadd           
        //   239: f2i            
        //   240: istore          a
        //   242: iload           a
        //   244: iload           a
        //   246: bipush          8
        //   248: ishl           
        //   249: ior            
        //   250: iload           a
        //   252: bipush          16
        //   254: ishl           
        //   255: ior            
        //   256: iload           a
        //   258: bipush          24
        //   260: ishl           
        //   261: ior            
        //   262: iload_3         /* a */
        //   263: ifeq            279
        //   266: getstatic       com/sun/jna/z/a/i.g:I
        //   269: istore          a
        //   271: iinc            a, 1
        //   274: iload           a
        //   276: putstatic       com/sun/jna/z/a/i.g:I
        //   279: ireturn        
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:191)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:46)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static void a(final Color a) {
        GL11.glColor4f(a.getRed() / 255.0f, a.getGreen() / 255.0f, a.getBlue() / 255.0f, a.getAlpha() / 255.0f);
    }
    
    public static Color a(final int a) {
        final int a2 = a & 0xFF;
        final int a3 = a >> 8 & 0xFF;
        final int a4 = a >> 16 & 0xFF;
        final int a5 = a >> 24 & 0xFF;
        return new Color(a2, a3, a4, a5);
    }
    
    public static int b(final Color a) {
        return a.getRed() | a.getGreen() << 8 | a.getBlue() << 16 | a.getAlpha() << 24;
    }
    
    public static void b(final int a) {
        final int a2 = a & 0xFF;
        final int a3 = a >> 8 & 0xFF;
        final int a4 = a >> 16 & 0xFF;
        final int a5 = a >> 24 & 0xFF;
        GL11.glColor4b((byte)a2, (byte)a3, (byte)a4, (byte)a5);
    }
    
    public static Point b() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: getfield        net/minecraft/client/Minecraft.field_71474_y:Lnet/minecraft/client/settings/GameSettings;
        //     6: getfield        net/minecraft/client/settings/GameSettings.field_74335_Z:I
        //     9: istore_1       
        //    10: getstatic       com/sun/jna/z/a/e/a/a/a/f/a.b:I
        //    13: istore_0        /* a */
        //    14: iload_1         /* a */
        //    15: iload_0         /* a */
        //    16: ifne            30
        //    19: iconst_3       
        //    20: if_icmpne       29
        //    23: ldc2_w          1.2
        //    26: goto            55
        //    29: iload_1         /* a */
        //    30: iload_0         /* a */
        //    31: ifne            44
        //    34: ifne            43
        //    37: ldc2_w          0.0025
        //    40: goto            55
        //    43: iload_1         /* a */
        //    44: iconst_1       
        //    45: if_icmpne       54
        //    48: ldc2_w          2.0
        //    51: goto            55
        //    54: dconst_1       
        //    55: d2f            
        //    56: fstore_2        /* a */
        //    57: invokestatic    net/minecraft/client/Minecraft.func_71410_x:()Lnet/minecraft/client/Minecraft;
        //    60: astore_3        /* a */
        //    61: aload_3         /* a */
        //    62: getfield        net/minecraft/client/Minecraft.field_71474_y:Lnet/minecraft/client/settings/GameSettings;
        //    65: getfield        net/minecraft/client/settings/GameSettings.field_74335_Z:I
        //    68: istore          a
        //    70: iload           a
        //    72: iload_0         /* a */
        //    73: ifne            92
        //    76: ifne            90
        //    79: ldc             1000.0
        //    81: fload_2         /* a */
        //    82: fmul           
        //    83: f2i            
        //    84: istore          a
        //    86: iload_0         /* a */
        //    87: ifeq            120
        //    90: iload           a
        //    92: iload_0         /* a */
        //    93: ifne            118
        //    96: iconst_3       
        //    97: if_icmpne       112
        //   100: iload           a
        //   102: i2f            
        //   103: fload_2         /* a */
        //   104: fdiv           
        //   105: f2i            
        //   106: istore          a
        //   108: iload_0         /* a */
        //   109: ifeq            120
        //   112: iload           a
        //   114: i2f            
        //   115: fload_2         /* a */
        //   116: fmul           
        //   117: f2i            
        //   118: istore          a
        //   120: iconst_0       
        //   121: istore          a
        //   123: iload           a
        //   125: iload           a
        //   127: if_icmpge       171
        //   130: aload_3         /* a */
        //   131: getfield        net/minecraft/client/Minecraft.field_71443_c:I
        //   134: iload           a
        //   136: iconst_1       
        //   137: iadd           
        //   138: idiv           
        //   139: sipush          320
        //   142: iload_0         /* a */
        //   143: ifne            161
        //   146: if_icmplt       171
        //   149: aload_3         /* a */
        //   150: getfield        net/minecraft/client/Minecraft.field_71440_d:I
        //   153: iload           a
        //   155: iconst_1       
        //   156: iadd           
        //   157: idiv           
        //   158: sipush          240
        //   161: if_icmplt       171
        //   164: iinc            a, 1
        //   167: iload_0         /* a */
        //   168: ifeq            123
        //   171: new             Ljava/awt/Point;
        //   174: dup            
        //   175: invokestatic    org/lwjgl/input/Mouse.getX:()I
        //   178: iload           a
        //   180: idiv           
        //   181: aload_3         /* a */
        //   182: getfield        net/minecraft/client/Minecraft.field_71440_d:I
        //   185: iload           a
        //   187: idiv           
        //   188: invokestatic    org/lwjgl/input/Mouse.getY:()I
        //   191: iload           a
        //   193: idiv           
        //   194: isub           
        //   195: iconst_1       
        //   196: isub           
        //   197: invokespecial   java/awt/Point.<init>:(II)V
        //   200: areturn        
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:191)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:46)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
