/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityAIOpenDoor
/*    */   extends EntityAIDoorInteract
/*    */ {
/*    */   boolean closeDoor;
/*    */   int closeDoorTemporisation;
/*    */   
/*    */   public EntityAIOpenDoor(EntityLiving entitylivingIn, boolean shouldClose) {
/* 17 */     super(entitylivingIn);
/* 18 */     this.theEntity = entitylivingIn;
/* 19 */     this.closeDoor = shouldClose;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 27 */     return (this.closeDoor && this.closeDoorTemporisation > 0 && super.continueExecuting());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 35 */     this.closeDoorTemporisation = 20;
/* 36 */     this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 44 */     if (this.closeDoor)
/*    */     {
/* 46 */       this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, false);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 55 */     this.closeDoorTemporisation--;
/* 56 */     super.updateTask();
/*    */   }
/*    */ }


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\entity\ai\EntityAIOpenDoor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */