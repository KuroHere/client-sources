package net.minecraft.client.renderer.entity.layers;

public interface LayerRenderer<E extends net.minecraft.entity.EntityLivingBase> {
  void doRenderLayer(E paramE, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
  
  boolean shouldCombineTextures();
}


/* Location:              C:\Users\mymon\AppData\Roaming\.minecraft\versions\Hera\Hera.jar!\net\minecraft\client\renderer\entity\layers\LayerRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */