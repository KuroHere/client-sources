/*    */ package net.minecraft.util;
/*    */ 
/*    */ import org.lwjgl.util.vector.Matrix4f;
/*    */ 
/*    */ public class Matrix4f extends Matrix4f {
/*    */   public Matrix4f(float[] p_i46413_1_) {
/*  7 */     this.m00 = p_i46413_1_[0];
/*  8 */     this.m01 = p_i46413_1_[1];
/*  9 */     this.m02 = p_i46413_1_[2];
/* 10 */     this.m03 = p_i46413_1_[3];
/* 11 */     this.m10 = p_i46413_1_[4];
/* 12 */     this.m11 = p_i46413_1_[5];
/* 13 */     this.m12 = p_i46413_1_[6];
/* 14 */     this.m13 = p_i46413_1_[7];
/* 15 */     this.m20 = p_i46413_1_[8];
/* 16 */     this.m21 = p_i46413_1_[9];
/* 17 */     this.m22 = p_i46413_1_[10];
/* 18 */     this.m23 = p_i46413_1_[11];
/* 19 */     this.m30 = p_i46413_1_[12];
/* 20 */     this.m31 = p_i46413_1_[13];
/* 21 */     this.m32 = p_i46413_1_[14];
/* 22 */     this.m33 = p_i46413_1_[15];
/*    */   }
/*    */ 
/*    */   
/*    */   public Matrix4f() {
/* 27 */     this.m00 = this.m01 = this.m02 = this.m03 = this.m10 = this.m11 = this.m12 = this.m13 = this.m20 = this.m21 = this.m22 = this.m23 = this.m30 = this.m31 = this.m32 = this.m33 = 0.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraf\\util\Matrix4f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */