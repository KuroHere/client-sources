/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ 
/*    */ public class BlockLeavesBase
/*    */   extends Block
/*    */ {
/*    */   protected boolean fancyGraphics;
/*    */   
/*    */   protected BlockLeavesBase(Material materialIn, boolean fancyGraphics) {
/* 14 */     super(materialIn);
/* 15 */     this.fancyGraphics = fancyGraphics;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isOpaqueCube() {
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 28 */     return (!this.fancyGraphics && worldIn.getBlockState(pos).getBlock() == this) ? false : super.shouldSideBeRendered(worldIn, pos, side);
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\block\BlockLeavesBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */