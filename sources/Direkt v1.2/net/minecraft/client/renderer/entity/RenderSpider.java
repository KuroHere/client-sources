package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderSpider<T extends EntitySpider> extends RenderLiving<T> {
	private static final ResourceLocation SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/spider.png");

	public RenderSpider(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelSpider(), 1.0F);
		this.addLayer(new LayerSpiderEyes(this));
	}

	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn) {
		return 180.0F;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return SPIDER_TEXTURES;
	}
}
