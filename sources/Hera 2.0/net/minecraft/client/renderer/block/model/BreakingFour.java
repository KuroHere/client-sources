/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ public class BreakingFour
/*    */   extends BakedQuad {
/*    */   private final TextureAtlasSprite texture;
/*    */   
/*    */   public BreakingFour(BakedQuad p_i46217_1_, TextureAtlasSprite textureIn) {
/* 12 */     super(Arrays.copyOf(p_i46217_1_.getVertexData(), (p_i46217_1_.getVertexData()).length), p_i46217_1_.tintIndex, FaceBakery.getFacingFromVertexData(p_i46217_1_.getVertexData()));
/* 13 */     this.texture = textureIn;
/* 14 */     func_178217_e();
/*    */   }
/*    */ 
/*    */   
/*    */   private void func_178217_e() {
/* 19 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 21 */       func_178216_a(i);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void func_178216_a(int p_178216_1_) {
/* 27 */     int i = 7 * p_178216_1_;
/* 28 */     float f = Float.intBitsToFloat(this.vertexData[i]);
/* 29 */     float f1 = Float.intBitsToFloat(this.vertexData[i + 1]);
/* 30 */     float f2 = Float.intBitsToFloat(this.vertexData[i + 2]);
/* 31 */     float f3 = 0.0F;
/* 32 */     float f4 = 0.0F;
/*    */     
/* 34 */     switch (this.face) {
/*    */       
/*    */       case null:
/* 37 */         f3 = f * 16.0F;
/* 38 */         f4 = (1.0F - f2) * 16.0F;
/*    */         break;
/*    */       
/*    */       case UP:
/* 42 */         f3 = f * 16.0F;
/* 43 */         f4 = f2 * 16.0F;
/*    */         break;
/*    */       
/*    */       case NORTH:
/* 47 */         f3 = (1.0F - f) * 16.0F;
/* 48 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */       
/*    */       case SOUTH:
/* 52 */         f3 = f * 16.0F;
/* 53 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */       
/*    */       case WEST:
/* 57 */         f3 = f2 * 16.0F;
/* 58 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */       
/*    */       case EAST:
/* 62 */         f3 = (1.0F - f2) * 16.0F;
/* 63 */         f4 = (1.0F - f1) * 16.0F;
/*    */         break;
/*    */     } 
/* 66 */     this.vertexData[i + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(f3));
/* 67 */     this.vertexData[i + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(f4));
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\client\renderer\block\model\BreakingFour.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */