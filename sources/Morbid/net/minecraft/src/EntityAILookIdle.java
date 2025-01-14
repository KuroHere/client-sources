package net.minecraft.src;

public class EntityAILookIdle extends EntityAIBase
{
    private EntityLiving idleEntity;
    private double lookX;
    private double lookZ;
    private int idleTime;
    
    public EntityAILookIdle(final EntityLiving par1EntityLiving) {
        this.idleTime = 0;
        this.idleEntity = par1EntityLiving;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        return this.idleEntity.getRNG().nextFloat() < 0.02f;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.idleTime >= 0;
    }
    
    @Override
    public void startExecuting() {
        final double var1 = 6.283185307179586 * this.idleEntity.getRNG().nextDouble();
        this.lookX = Math.cos(var1);
        this.lookZ = Math.sin(var1);
        this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
    }
    
    @Override
    public void updateTask() {
        --this.idleTime;
        this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0f, this.idleEntity.getVerticalFaceSpeed());
    }
}
