// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.triton.impl.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.triton.impl.modules.movement.phase.OLDNCP;
import net.minecraft.client.triton.impl.modules.movement.phase.Para;
import net.minecraft.client.triton.management.event.EventTarget;
import net.minecraft.client.triton.management.event.events.BoundingBoxEvent;
import net.minecraft.client.triton.management.event.events.InsideBlockRenderEvent;
import net.minecraft.client.triton.management.event.events.MoveEvent;
import net.minecraft.client.triton.management.event.events.UpdateEvent;
import net.minecraft.client.triton.management.module.Module;
import net.minecraft.client.triton.management.module.Module.Mod;
import net.minecraft.client.triton.management.option.Option;
import net.minecraft.client.triton.management.option.OptionManager;
import net.minecraft.client.triton.utils.ClientUtils;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@Mod
public class Phase extends Module
{
    private OLDNCP instant;
    private Para para;
    @Option.Op(min = 0.0, max = 10.0, increment = 0.1, name = "Distance")
	public double distance;
    
    public Phase() {
        this.instant = new OLDNCP("Silent", true, this);
        this.para = new Para("Para", false, this);
        this.distance = 1.2;
    }
    
    @EventTarget
    private void onInsideBlockRender(final InsideBlockRenderEvent event) {
        event.setCancelled(true);
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
    	this.instant.onUpdate(event);
    	this.para.onUpdate(event);
    	this.updateSuffix();
    }
    
    @EventTarget
    private void onMove(final MoveEvent event) {
    	this.instant.onMove(event);
    }
    
    @EventTarget
    private void onSetBoundingbox(final BoundingBoxEvent event){
    	this.instant.onSetBoundingbox(event);
    	this.para.onSetBoundingbox(event);
    }
    
    @Override
    public void postInitialize() {
        OptionManager.getOptionList().add(this.instant);
        OptionManager.getOptionList().add(this.para);
        this.updateSuffix();
        super.postInitialize();
    }
    
    private void updateSuffix() {
        if (this.para.getValue()) {
            this.setSuffix("HCF");
        }
        if(this.instant.getValue()){
            this.setSuffix("Instant");
        }
    }
    
    public boolean isInsideBlock()
    {
      for (int x = MathHelper.floor_double(ClientUtils.player().boundingBox.minX); x < MathHelper.floor_double(ClientUtils.player().boundingBox.maxX) + 1; x++) {
        for (int y = MathHelper.floor_double(ClientUtils.player().boundingBox.minY); y < MathHelper.floor_double(ClientUtils.player().boundingBox.maxY) + 1; y++) {
          for (int z = MathHelper.floor_double(ClientUtils.player().boundingBox.minZ); z < MathHelper.floor_double(ClientUtils.player().boundingBox.maxZ) + 1; z++)
          {
            Block block = ClientUtils.world().getBlockState(new BlockPos(x, y, z)).getBlock();
            if ((block != null) && (!(block instanceof BlockAir)))
            {
              AxisAlignedBB boundingBox = block.getCollisionBoundingBox(ClientUtils.world(), new BlockPos(x, y, z), ClientUtils.world().getBlockState(new BlockPos(x, y, z)));
              if ((block instanceof BlockHopper)) {
                boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
              }
              if ((boundingBox != null) && 
                (ClientUtils.player().boundingBox.intersectsWith(boundingBox))) {
                return true;
              }
            }
          }
        }
      }
      return false;
    }
}
