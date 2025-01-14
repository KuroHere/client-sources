/*    */ package net.minecraft.scoreboard;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class ScoreDummyCriteria
/*    */   implements IScoreObjectiveCriteria
/*    */ {
/*    */   private final String dummyName;
/*    */   
/*    */   public ScoreDummyCriteria(String name) {
/* 12 */     this.dummyName = name;
/* 13 */     IScoreObjectiveCriteria.INSTANCES.put(name, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 18 */     return this.dummyName;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_96635_a(List<EntityPlayer> p_96635_1_) {
/* 23 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReadOnly() {
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
/* 33 */     return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\scoreboard\ScoreDummyCriteria.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */