package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderFallingBlock
  extends Render
{
  private static final String __OBFID = "CL_00000994";
  
  public RenderFallingBlock(RenderManager p_i46177_1_)
  {
    super(p_i46177_1_);
    this.shadowSize = 0.5F;
  }
  
  public void doRender(EntityFallingBlock p_180557_1_, double p_180557_2_, double p_180557_4_, double p_180557_6_, float p_180557_8_, float p_180557_9_)
  {
    if (p_180557_1_.getBlock() != null)
    {
      bindTexture(TextureMap.locationBlocksTexture);
      IBlockState var10 = p_180557_1_.getBlock();
      Block var11 = var10.getBlock();
      BlockPos var12 = new BlockPos(p_180557_1_);
      World var13 = p_180557_1_.getWorldObj();
      if ((var10 != var13.getBlockState(var12)) && (var11.getRenderType() != -1)) {
        if (var11.getRenderType() == 3)
        {
          GlStateManager.pushMatrix();
          GlStateManager.translate((float)p_180557_2_, (float)p_180557_4_, (float)p_180557_6_);
          GlStateManager.disableLighting();
          Tessellator var14 = Tessellator.getInstance();
          WorldRenderer var15 = var14.getWorldRenderer();
          var15.startDrawingQuads();
          var15.setVertexFormat(DefaultVertexFormats.field_176600_a);
          int var16 = var12.getX();
          int var17 = var12.getY();
          int var18 = var12.getZ();
          var15.setTranslation(-var16 - 0.5F, -var17, -var18 - 0.5F);
          BlockRendererDispatcher var19 = Minecraft.getMinecraft().getBlockRendererDispatcher();
          IBakedModel var20 = var19.getModelFromBlockState(var10, var13, null);
          var19.func_175019_b().renderBlockModel(var13, var20, var10, var12, var15, false);
          var15.setTranslation(0.0D, 0.0D, 0.0D);
          var14.draw();
          GlStateManager.enableLighting();
          GlStateManager.popMatrix();
          super.doRender(p_180557_1_, p_180557_2_, p_180557_4_, p_180557_6_, p_180557_8_, p_180557_9_);
        }
      }
    }
  }
  
  protected ResourceLocation getEntityTexture(EntityFallingBlock p_110775_1_)
  {
    return TextureMap.locationBlocksTexture;
  }
  
  protected ResourceLocation getEntityTexture(Entity p_110775_1_)
  {
    return getEntityTexture((EntityFallingBlock)p_110775_1_);
  }
  
  public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
  {
    doRender((EntityFallingBlock)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
  }
}
