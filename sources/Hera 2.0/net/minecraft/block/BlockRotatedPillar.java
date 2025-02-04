/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.PropertyEnum;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ public abstract class BlockRotatedPillar
/*    */   extends Block {
/* 10 */   public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
/*    */ 
/*    */   
/*    */   protected BlockRotatedPillar(Material materialIn) {
/* 14 */     super(materialIn, materialIn.getMaterialMapColor());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockRotatedPillar(Material p_i46385_1_, MapColor p_i46385_2_) {
/* 19 */     super(p_i46385_1_, p_i46385_2_);
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\block\BlockRotatedPillar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */