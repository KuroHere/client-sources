package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateSwimmer extends PathNavigate {
	public PathNavigateSwimmer(EntityLiving entitylivingIn, World worldIn) {
		super(entitylivingIn, worldIn);
	}

	@Override
	protected PathFinder getPathFinder() {
		return new PathFinder(new SwimNodeProcessor());
	}

	/**
	 * If on ground or swimming and can swim
	 */
	@Override
	protected boolean canNavigate() {
		return this.isInLiquid();
	}

	@Override
	protected Vec3d getEntityPosition() {
		return new Vec3d(this.theEntity.posX, this.theEntity.posY + (this.theEntity.height * 0.5D), this.theEntity.posZ);
	}

	@Override
	protected void pathFollow() {
		Vec3d vec3d = this.getEntityPosition();
		float f = this.theEntity.width * this.theEntity.width;
		int i = 6;

		if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < f) {
			this.currentPath.incrementPathIndex();
		}

		for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
			Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.theEntity, j);

			if ((vec3d1.squareDistanceTo(vec3d) <= 36.0D) && this.isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0)) {
				this.currentPath.setCurrentPathIndex(j);
				break;
			}
		}

		this.checkForStuck(vec3d);
	}

	/**
	 * Trims path data from the end to the first sun covered block
	 */
	@Override
	protected void removeSunnyPath() {
		super.removeSunnyPath();
	}

	/**
	 * Checks if the specified entity can safely walk to the specified location.
	 */
	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
		RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(posVec31, new Vec3d(posVec32.xCoord, posVec32.yCoord + (this.theEntity.height * 0.5D), posVec32.zCoord), false, true, false);
		return (raytraceresult == null) || (raytraceresult.typeOfHit == RayTraceResult.Type.MISS);
	}

	@Override
	public boolean canEntityStandOnPos(BlockPos pos) {
		return !this.worldObj.getBlockState(pos).isFullBlock();
	}
}
