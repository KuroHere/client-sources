package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class RenderSheep
  extends RenderLiving
{
  private static final ResourceLocation shearedSheepTextures = new ResourceLocation("textures/entity/sheep/sheep.png");
  private static final String __OBFID = "CL_00001021";
  
  public RenderSheep(RenderManager p_i46145_1_, ModelBase p_i46145_2_, float p_i46145_3_)
  {
    super(p_i46145_1_, p_i46145_2_, p_i46145_3_);
    addLayer(new LayerSheepWool(this));
  }
  
  protected ResourceLocation getEntityTexture(EntitySheep p_110775_1_)
  {
    return shearedSheepTextures;
  }
  
  protected ResourceLocation getEntityTexture(Entity p_110775_1_)
  {
    return getEntityTexture((EntitySheep)p_110775_1_);
  }
}
