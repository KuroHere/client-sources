/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ class EntityRenderer1
/*    */   implements Predicate
/*    */ {
/*    */   final EntityRenderer field_90032_a;
/*    */   
/*    */   EntityRenderer1(EntityRenderer p_i1243_1_) {
/* 12 */     this.field_90032_a = p_i1243_1_;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean apply(Entity p_apply_1_) {
/* 17 */     return p_apply_1_.canBeCollidedWith();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean apply(Object p_apply_1_) {
/* 22 */     return apply((Entity)p_apply_1_);
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\client\renderer\EntityRenderer1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */