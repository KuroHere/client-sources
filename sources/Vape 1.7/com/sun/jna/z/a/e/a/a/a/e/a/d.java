package com.sun.jna.z.a.e.a.a.a.e.a;

import com.sun.jna.z.a.e.a.a.a.e.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import com.sun.jna.z.a.e.a.a.a.a.*;
import com.sun.jna.z.a.e.a.a.a.f.*;
import com.sun.jna.z.a.e.a.a.a.*;
import java.awt.*;

public class d extends b<n>
{
    private final i e;
    Color f;
    private static final String g;
    
    d(final i a) {
        super(n.class);
        this.f = new Color(22, 22, 22, 220);
        this.e = a;
        this.b = Color.WHITE;
        this.c = new Color(128, 128, 128, 200);
    }
    
    protected void a(final n a) {
        final int a2 = i.f ? 1 : 0;
        final boolean a3 = Minecraft.func_71410_x().field_71462_r != null;
        GL11.glPushMatrix();
        final Rectangle a4 = new Rectangle(a.j());
        final int a5 = this.e.a().field_78288_b;
        this.a(a, false);
        final boolean a6 = GL11.glIsEnabled(3042);
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        int d;
        int length;
        final int n = length = (d = (a.d() ? 1 : 0));
        Label_0127: {
            Label_0126: {
                Label_0117: {
                    if (a2 == 0) {
                        if (n != 0) {
                            break Label_0117;
                        }
                        d = (length = a.b().length);
                    }
                    if (a2 != 0) {
                        break Label_0127;
                    }
                    if (length != 0) {
                        break Label_0126;
                    }
                }
                a4.height = a5 + 4;
            }
            d = 1;
        }
        final int a7 = d;
        int length2;
        final int n2 = length2 = a.b().length;
        if (a2 == 0) {
            if (n2 > 0) {
                length2 = 1;
            }
            else {
                length2 = 0;
            }
        }
        final boolean a8 = length2 != 0;
        int n4;
        final int n3 = n4 = (a8 ? 1 : 0);
        if (a2 == 0) {
            if (n3 != 0) {
                a.a(this.f);
                GL11.glBegin(7);
                GL11.glVertex2d((double)a7, 16.0);
                GL11.glVertex2d((double)(a4.width - a7), 16.0);
                final double n5 = a4.width - a7;
                final int height = a4.height;
                final int n6 = a8 ? 1 : 0;
                if (a2 == 0 && n6 == 0) {}
                GL11.glVertex2d(n5, (double)(height + n6));
                final double n7 = a7;
                final int height2 = a4.height;
                final int n8 = a8 ? 1 : 0;
                if (a2 == 0 && n8 == 0) {}
                GL11.glVertex2d(n7, (double)(height2 + n8));
                GL11.glEnd();
            }
            final boolean b;
            n4 = ((b = a3) ? 1 : 0);
        }
        if (a2 == 0) {
            if (n3 != 0) {
                a.a(new Color(h.e.getRed(), h.e.getGreen(), h.e.getBlue(), 255));
                GL11.glBegin(7);
                GL11.glVertex2d(0.0, 0.0);
                GL11.glVertex2d((double)a4.width, 0.0);
                GL11.glVertex2d((double)a4.width, 16.0);
                GL11.glVertex2d(0.0, 16.0);
                GL11.glEnd();
            }
            n4 = a.f() - 2;
        }
        int a9 = n4;
        final Point a10 = a.b();
        k a11 = a;
        while (a11 != null) {
            final Point point = a10;
            point.x -= a11.d();
            final Point point2 = a10;
            point2.y -= a11.e();
            a11 = a11.k();
            if (a2 != 0) {
                break;
            }
        }
        final boolean[] a12 = { a.g(), a.c(), a.e() };
        final boolean[] array = { false, a.b(), false };
        final int n9 = 2;
        final boolean d2 = a.d();
        if (a2 == 0 && d2) {}
        array[n9] = d2;
        final boolean[] a13 = array;
        int a14 = 0;
        while (true) {
            while (a14 < a12.length) {
                final boolean c;
                final int n11;
                final int n10 = n11 = ((c = a12[a14]) ? 1 : 0);
                if (a2 != 0) {
                    final double a15 = (!c) ? 13.0 : 0.0;
                    final boolean b3;
                    final boolean b2 = b3 = a3;
                    final boolean e;
                    if (a2 == 0) {
                        Label_0851: {
                            if (b2) {
                                e = a.e();
                                if (a2 == 0) {
                                    if (e) {
                                        int d3;
                                        final boolean b4 = (d3 = (a.d() ? 1 : 0)) != 0;
                                        if (a2 == 0) {
                                            if (b4) {
                                                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                                                GL11.glLineWidth(2.0f);
                                                GL11.glBegin(1);
                                                GL11.glVertex2d(a4.width - 23 + a15, 7.5);
                                                GL11.glVertex2d(a4.width - 18 + a15, 7.5);
                                                GL11.glVertex2d(a4.width - 20.5 + a15, 5.0);
                                                GL11.glVertex2d(a4.width - 20.5 + a15, 10.0);
                                                GL11.glEnd();
                                                if (a2 == 0) {
                                                    break Label_0851;
                                                }
                                            }
                                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                                            GL11.glLineWidth(2.0f);
                                            GL11.glBegin(1);
                                            GL11.glVertex2d(a4.width - 23 + a15, 7.5);
                                            d3 = a4.width - 18;
                                        }
                                        GL11.glVertex2d(d3 + a15, 7.5);
                                        GL11.glEnd();
                                    }
                                }
                            }
                        }
                    }
                    Label_1007: {
                        Label_0989: {
                            if (a2 == 0) {
                                if (b2) {
                                    this.e.a().a(a.a(), 18.0f, 2.0f, a.b(a.m()), -1879048192);
                                    com.sun.jna.z.a.h.d.b.a(Color.WHITE, 4.0f, 4.0f, a.a(), 0.4f);
                                    final boolean c2 = a.c();
                                    if (a2 != 0) {
                                        break Label_0989;
                                    }
                                    if (c2) {
                                        com.sun.jna.z.a.h.d.b.a(a.b() ? Color.WHITE : Color.GRAY, (a4.width - 7) * 2, 2.0f, com.sun.jna.z.a.e.a.a.a.e.a.d.g, 0.45f);
                                    }
                                }
                                GL11.glEnable(3553);
                                GL11.glEnable(2884);
                                if (a2 != 0) {
                                    break Label_1007;
                                }
                            }
                        }
                        if (!e) {
                            GL11.glDisable(3042);
                        }
                        this.a(a, true);
                        GL11.glPopMatrix();
                    }
                    if (com.sun.jna.z.a.i.g != 0) {
                        i.f = (a2 == 0);
                    }
                    return;
                }
                Label_0642: {
                    if (a2 == 0) {
                        if (n10 == 0 && a2 == 0) {
                            break Label_0642;
                        }
                        final int x = a10.x;
                    }
                    final int n12 = a9 - a5;
                    final int x2;
                    if (a2 == 0) {
                        if (n10 >= n12) {
                            x2 = a10.x;
                            final int n13 = a9;
                            if (a2 == 0) {
                                if (x2 <= n13) {
                                    final int y = a10.y;
                                    final int n14 = 2;
                                    if (a2 == 0) {
                                        if (y >= n14) {
                                            final int y2 = a10.y;
                                            final int n15 = a5 + 2;
                                            if (a2 == 0) {
                                                if (y2 <= n15) {
                                                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.16f);
                                                    GL11.glBegin(7);
                                                    GL11.glVertex2d((double)(a9 - a5), 2.0);
                                                    GL11.glVertex2d((double)a9, 2.0);
                                                    GL11.glVertex2d((double)a9, (double)(a5 + 2));
                                                    GL11.glVertex2d((double)(a9 - a5), (double)(a5 + 2));
                                                    GL11.glEnd();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    a9 = x2 - n12;
                }
                ++a14;
                if (a2 != 0) {
                    break;
                }
            }
            boolean c = a.c();
            continue;
        }
    }
    
    protected Rectangle b(final n a) {
        final Rectangle a2 = new Rectangle(a.j());
        a2.x = 2;
        a2.y = this.e.a().field_78288_b + 6;
        final Rectangle rectangle = a2;
        rectangle.width -= 4;
        final Rectangle rectangle2 = a2;
        rectangle2.height -= this.e.a().field_78288_b + 8;
        return a2;
    }
    
    protected Dimension c(final n a) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface com/sun/jna/z/a/e/a/a/a/a/n.b:()[Lcom/sun/jna/z/a/e/a/a/a/a/k;
        //     6: astore_3       
        //     7: getstatic       com/sun/jna/z/a/e/a/a/a/e/a/i.f:Z
        //    10: aload_3         /* a */
        //    11: arraylength    
        //    12: anewarray       Ljava/awt/Rectangle;
        //    15: astore          4
        //    17: istore_2        /* a */
        //    18: aload_3         /* a */
        //    19: arraylength    
        //    20: anewarray       [Lcom/sun/jna/z/a/e/a/a/a/c/d;
        //    23: astore          a
        //    25: iconst_0       
        //    26: istore          a
        //    28: iload           a
        //    30: aload_3         /* a */
        //    31: arraylength    
        //    32: if_icmpge       112
        //    35: aload_3         /* a */
        //    36: iload           a
        //    38: aaload         
        //    39: astore          a
        //    41: aload           a
        //    43: invokeinterface com/sun/jna/z/a/e/a/a/a/a/k.a:()Lcom/sun/jna/z/a/e/a/a/a/e/e;
        //    48: aload           a
        //    50: invokeinterface com/sun/jna/z/a/e/a/a/a/e/e.a:(Lcom/sun/jna/z/a/e/a/a/a/a/k;)Lcom/sun/jna/z/a/e/a/a/a/e/d;
        //    55: aload           a
        //    57: invokeinterface com/sun/jna/z/a/e/a/a/a/e/d.b:(Lcom/sun/jna/z/a/e/a/a/a/a/k;)Ljava/awt/Dimension;
        //    62: iload_2         /* a */
        //    63: ifne            127
        //    66: astore          a
        //    68: aload           a
        //    70: iload           a
        //    72: new             Ljava/awt/Rectangle;
        //    75: dup            
        //    76: iconst_0       
        //    77: iconst_0       
        //    78: aload           a
        //    80: getfield        java/awt/Dimension.width:I
        //    83: aload           a
        //    85: getfield        java/awt/Dimension.height:I
        //    88: invokespecial   java/awt/Rectangle.<init>:(IIII)V
        //    91: aastore        
        //    92: aload           a
        //    94: iload           a
        //    96: aload_1         /* a */
        //    97: aload           a
        //    99: invokeinterface com/sun/jna/z/a/e/a/a/a/a/n.a:(Lcom/sun/jna/z/a/e/a/a/a/a/k;)[Lcom/sun/jna/z/a/e/a/a/a/c/d;
        //   104: aastore        
        //   105: iinc            a, 1
        //   108: iload_2         /* a */
        //   109: ifeq            28
        //   112: aload_1         /* a */
        //   113: invokeinterface com/sun/jna/z/a/e/a/a/a/a/n.a:()Lcom/sun/jna/z/a/e/a/a/a/c/g;
        //   118: aload           a
        //   120: aload           a
        //   122: invokeinterface com/sun/jna/z/a/e/a/a/a/c/g.a:([Ljava/awt/Rectangle;[[Lcom/sun/jna/z/a/e/a/a/a/c/d;)Ljava/awt/Dimension;
        //   127: astore          a
        //   129: aload           a
        //   131: dup            
        //   132: getfield        java/awt/Dimension.width:I
        //   135: iconst_4       
        //   136: iadd           
        //   137: putfield        java/awt/Dimension.width:I
        //   140: aload           a
        //   142: dup            
        //   143: getfield        java/awt/Dimension.height:I
        //   146: aload_0         /* a */
        //   147: getfield        com/sun/jna/z/a/e/a/a/a/e/a/d.e:Lcom/sun/jna/z/a/e/a/a/a/e/a/i;
        //   150: invokevirtual   com/sun/jna/z/a/e/a/a/a/e/a/i.a:()Lcom/sun/jna/z/a/e/a/a/a/b/a;
        //   153: getfield        com/sun/jna/z/a/e/a/a/a/b/a.field_78288_b:I
        //   156: bipush          8
        //   158: iadd           
        //   159: iadd           
        //   160: putfield        java/awt/Dimension.height:I
        //   163: aload           a
        //   165: areturn        
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
    
    protected Rectangle[] d(final n a) {
        return new Rectangle[] { new Rectangle(0, 0, a.f(), this.e.a().field_78288_b + 4) };
    }
    
    protected void a(final n a, final Point a, final int a) {
        final int a2 = i.f ? 1 : 0;
        int n = a;
        if (a2 == 0) {
            if (a != 0) {
                return;
            }
            n = a.f() - 2;
        }
        int a3 = n;
        final float a4 = this.e.a().a("I");
        float n6;
        int y;
        int n5;
        int n4;
        int n3;
        final int n2 = n3 = (n4 = (n5 = (y = (int)(n6 = (a.g() ? 1 : 0)))));
        if (a2 == 0) {
            if (n2 != 0) {
                final float n7 = fcmpl((float)a.x, a3 - a4);
                Label_0134: {
                    if (a2 == 0) {
                        if (n7 >= 0) {
                            int n9;
                            final int n8 = n9 = a.x;
                            if (a2 != 0) {
                                break Label_0134;
                            }
                            if (n8 <= a3) {
                                final int n10 = n9 = a.y;
                                if (a2 != 0) {
                                    break Label_0134;
                                }
                                if (n10 >= 2) {
                                    final float n11 = fcmpg((float)a.y, a4 + 2.0f);
                                    if (a2 != 0) {
                                        break Label_0134;
                                    }
                                    if (n11 <= 0) {
                                        a.f();
                                        return;
                                    }
                                }
                            }
                        }
                        int n9 = (int)(a3 - (a4 + 2.0f));
                    }
                }
                a3 = (int)n7;
            }
            final int n12;
            n3 = (n12 = (n4 = (n5 = (y = (int)(n6 = (a.c() ? 1 : 0))))));
        }
        if (a2 == 0) {
            if (n2 != 0) {
                final float n13 = fcmpl((float)a.x, a3 - a4);
                Label_0250: {
                    if (a2 == 0) {
                        if (n13 >= 0) {
                            int n15;
                            final int n14 = n15 = a.x;
                            if (a2 != 0) {
                                break Label_0250;
                            }
                            if (n14 <= a3) {
                                final int n16 = n15 = a.y;
                                if (a2 != 0) {
                                    break Label_0250;
                                }
                                if (n16 >= 2) {
                                    final float n17 = fcmpg((float)a.y, a4 + 2.0f);
                                    if (a2 != 0) {
                                        break Label_0250;
                                    }
                                    if (n17 <= 0) {
                                        final boolean b = a.b();
                                        if (a2 == 0 && b) {}
                                        a.b(b);
                                        return;
                                    }
                                }
                            }
                        }
                        int n15 = (int)(a3 - (a4 + 2.0f));
                    }
                }
                a3 = (int)n13;
            }
            n4 = (n3 = (n5 = (y = (int)(n6 = (a.e() ? 1 : 0)))));
        }
        if (a2 == 0) {
            if (n3 != 0) {
                final float n18 = fcmpl((float)a.x, a3 - a4);
                Label_0366: {
                    if (a2 == 0) {
                        if (n18 >= 0) {
                            int n20;
                            final int n19 = n20 = a.x;
                            if (a2 != 0) {
                                break Label_0366;
                            }
                            if (n19 <= a3) {
                                final int n21 = n20 = a.y;
                                if (a2 != 0) {
                                    break Label_0366;
                                }
                                if (n21 >= 2) {
                                    final float n22 = fcmpg((float)a.y, a4 + 2.0f);
                                    if (a2 != 0) {
                                        break Label_0366;
                                    }
                                    if (n22 <= 0) {
                                        final boolean d = a.d();
                                        if (a2 == 0 && d) {}
                                        a.f(d);
                                        return;
                                    }
                                }
                            }
                        }
                        int n20 = (int)(a3 - (a4 + 2.0f));
                    }
                }
                a3 = (int)n18;
            }
            n5 = (n4 = (y = (int)(n6 = a.x)));
        }
        if (a2 == 0) {
            if (n4 < 0) {
                return;
            }
            y = (n5 = (int)(n6 = a.x));
        }
        if (a2 == 0) {
            if (n5 > a3) {
                return;
            }
            n6 = (y = a.y);
        }
        if (a2 == 0) {
            if (y < 0) {
                return;
            }
            n6 = fcmpg((float)a.y, a4 + 4.0f);
        }
        if (n6 <= 0) {
            a.a(true);
        }
    }
    
    static {
        final char[] charArray = "\u00c8\u0004\u008f".toCharArray();
        int length;
        int n2;
        final int n = n2 = (length = charArray.length);
        int n3 = 0;
        while (true) {
            Label_0124: {
                if (n > 1) {
                    break Label_0124;
                }
                length = (n2 = n3);
                do {
                    final char c = charArray[n2];
                    char c2 = '\0';
                    switch (n3 % 7) {
                        case 0: {
                            c2 = '�';
                            break;
                        }
                        case 1: {
                            c2 = 'm';
                            break;
                        }
                        case 2: {
                            c2 = '\u00e1';
                            break;
                        }
                        case 3: {
                            c2 = '\u00fd';
                            break;
                        }
                        case 4: {
                            c2 = '�';
                            break;
                        }
                        case 5: {
                            c2 = '\r';
                            break;
                        }
                        default: {
                            c2 = '\u0010';
                            break;
                        }
                    }
                    charArray[length] = (char)(c ^ c2);
                    ++n3;
                } while (n == 0);
            }
            if (n <= n3) {
                g = new String(charArray).intern();
                return;
            }
            continue;
        }
    }
}
