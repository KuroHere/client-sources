package net.minecraft.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityMinecartMobSpawner.1;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.world.World;

public class EntityMinecartMobSpawner extends EntityMinecart {
   private final MobSpawnerBaseLogic mobSpawnerLogic = new 1(this);

   public EntityMinecartMobSpawner(World worldIn) {
      super(worldIn);
   }

   public EntityMinecartMobSpawner(World worldIn, double p_i1726_2_, double p_i1726_4_, double p_i1726_6_) {
      super(worldIn, p_i1726_2_, p_i1726_4_, p_i1726_6_);
   }

   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
      super.readEntityFromNBT(tagCompund);
      this.mobSpawnerLogic.readFromNBT(tagCompund);
   }

   public void handleStatusUpdate(byte id) {
      this.mobSpawnerLogic.setDelayToMin(id);
   }

   public EntityMinecart.EnumMinecartType getMinecartType() {
      return EntityMinecart.EnumMinecartType.SPAWNER;
   }

   public void onUpdate() {
      super.onUpdate();
      this.mobSpawnerLogic.updateSpawner();
   }

   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
      super.writeEntityToNBT(tagCompound);
      this.mobSpawnerLogic.writeToNBT(tagCompound);
   }

   public IBlockState getDefaultDisplayTile() {
      return Blocks.mob_spawner.getDefaultState();
   }

   public MobSpawnerBaseLogic func_98039_d() {
      return this.mobSpawnerLogic;
   }
}
