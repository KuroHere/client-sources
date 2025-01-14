package net.minecraft.client.renderer.entity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.ZombieType;
import net.minecraft.util.ResourceLocation;

public class RenderZombie extends RenderBiped<EntityZombie> {
	private static final ResourceLocation ZOMBIE_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_FARMER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_farmer.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_LIBRARIAN_LOC = new ResourceLocation("textures/entity/zombie_villager/zombie_librarian.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_priest.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_SMITH_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_smith.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_BUTCHER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_butcher.png");
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation field_190086_r = new ResourceLocation("textures/entity/zombie/husk.png");
	private final ModelBiped defaultModel;
	private final ModelZombieVillager zombieVillagerModel;
	private final List<LayerRenderer<EntityZombie>> villagerLayers;
	private final List<LayerRenderer<EntityZombie>> defaultLayers;

	public RenderZombie(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
		LayerRenderer<?> layerrenderer = this.layerRenderers.get(0);
		this.defaultModel = this.modelBipedMain;
		this.zombieVillagerModel = new ModelZombieVillager();
		this.addLayer(new LayerHeldItem(this));
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelZombie(0.5F, true);
				this.modelArmor = new ModelZombie(1.0F, true);
			}
		};
		this.addLayer(layerbipedarmor);
		this.defaultLayers = Lists.newArrayList(this.layerRenderers);

		if (layerrenderer instanceof LayerCustomHead) {
			this.removeLayer(layerrenderer);
			this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
		}

		this.removeLayer(layerbipedarmor);
		this.addLayer(new LayerVillagerArmor(this));
		this.villagerLayers = Lists.newArrayList(this.layerRenderers);
	}

	/**
	 * Allows the render to do state modifications necessary before the model is rendered.
	 */
	@Override
	protected void preRenderCallback(EntityZombie entitylivingbaseIn, float partialTickTime) {
		if (entitylivingbaseIn.func_189777_di() == ZombieType.HUSK) {
			float f = 1.0625F;
			GlStateManager.scale(1.0625F, 1.0625F, 1.0625F);
		}

		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.swapArmor(entity);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityZombie entity) {
		if (entity.isVillager()) {
			switch (entity.getVillagerType()) {
			case 0:
				return ZOMBIE_VILLAGER_FARMER_LOCATION;

			case 1:
				return ZOMBIE_VILLAGER_LIBRARIAN_LOC;

			case 2:
				return ZOMBIE_VILLAGER_PRIEST_LOCATION;

			case 3:
				return ZOMBIE_VILLAGER_SMITH_LOCATION;

			case 4:
				return ZOMBIE_VILLAGER_BUTCHER_LOCATION;

			default:
				return ZOMBIE_VILLAGER_TEXTURES;
			}
		} else {
			return entity.func_189777_di() == ZombieType.HUSK ? field_190086_r : ZOMBIE_TEXTURES;
		}
	}

	private void swapArmor(EntityZombie zombie) {
		if (zombie.isVillager()) {
			this.mainModel = this.zombieVillagerModel;
			this.layerRenderers = this.villagerLayers;
		} else {
			this.mainModel = this.defaultModel;
			this.layerRenderers = this.defaultLayers;
		}

		this.modelBipedMain = (ModelBiped) this.mainModel;
	}

	@Override
	protected void rotateCorpse(EntityZombie entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
		if (entityLiving.isConverting()) {
			p_77043_3_ += (float) (Math.cos(entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);
		}

		super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
	}
}
