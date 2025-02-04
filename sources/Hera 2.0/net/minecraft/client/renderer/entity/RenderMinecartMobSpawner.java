/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
/*    */ import net.minecraft.entity.ai.EntityMinecartMobSpawner;
/*    */ import net.minecraft.entity.item.EntityMinecart;
/*    */ import net.minecraft.init.Blocks;
/*    */ 
/*    */ public class RenderMinecartMobSpawner
/*    */   extends RenderMinecart<EntityMinecartMobSpawner> {
/*    */   public RenderMinecartMobSpawner(RenderManager renderManagerIn) {
/* 12 */     super(renderManagerIn);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void func_180560_a(EntityMinecartMobSpawner minecart, float partialTicks, IBlockState state) {
/* 17 */     super.func_180560_a(minecart, partialTicks, state);
/*    */     
/* 19 */     if (state.getBlock() == Blocks.mob_spawner)
/*    */     {
/* 21 */       TileEntityMobSpawnerRenderer.renderMob(minecart.func_98039_d(), minecart.posX, minecart.posY, minecart.posZ, partialTicks);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\client\renderer\entity\RenderMinecartMobSpawner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */