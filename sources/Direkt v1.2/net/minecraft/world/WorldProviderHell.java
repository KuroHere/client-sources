package net.minecraft.world;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderHell;

public class WorldProviderHell extends WorldProvider {
	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	@Override
	public void createBiomeProvider() {
		this.biomeProvider = new BiomeProviderSingle(Biomes.HELL);
		this.isHellWorld = true;
		this.hasNoSky = true;
	}

	/**
	 * Return Vec3D with biome specific fog color
	 */
	@Override
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
		return new Vec3d(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
	}

	/**
	 * Creates the light to brightness table
	 */
	@Override
	protected void generateLightBrightnessTable() {
		float f = 0.1F;

		for (int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (i / 15.0F);
			this.lightBrightnessTable[i] = (((1.0F - f1) / ((f1 * 3.0F) + 1.0F)) * 0.9F) + 0.1F;
		}
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
	 */
	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map spawn point
	 */
	@Override
	public boolean canCoordinateBeSpawn(int x, int z) {
		return false;
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 */
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return 0.5F;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false = nether).
	 */
	@Override
	public boolean canRespawnHere() {
		return false;
	}

	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	@Override
	public boolean doesXZShowFog(int x, int z) {
		return true;
	}

	@Override
	public WorldBorder createWorldBorder() {
		return new WorldBorder() {
			@Override
			public double getCenterX() {
				return super.getCenterX() / 8.0D;
			}

			@Override
			public double getCenterZ() {
				return super.getCenterZ() / 8.0D;
			}
		};
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.NETHER;
	}
}
