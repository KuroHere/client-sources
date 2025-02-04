package net.minecraft.util;

public class AxisAlignedBB
{
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public AxisAlignedBB(BlockPos pos1, BlockPos pos2)
    {
        this.minX = (double)pos1.getX();
        this.minY = (double)pos1.getY();
        this.minZ = (double)pos1.getZ();
        this.maxX = (double)pos2.getX();
        this.maxY = (double)pos2.getY();
        this.maxZ = (double)pos2.getZ();
    }

    /**
     * Adds the coordinates to the bounding box extending it if the point lies outside the current ranges. Args: x, y, z
     */
    public AxisAlignedBB addCoord(double x, double y, double z)
    {
        double var7 = this.minX;
        double var9 = this.minY;
        double var11 = this.minZ;
        double var13 = this.maxX;
        double var15 = this.maxY;
        double var17 = this.maxZ;

        if (x < 0.0D)
        {
            var7 += x;
        }
        else if (x > 0.0D)
        {
            var13 += x;
        }

        if (y < 0.0D)
        {
            var9 += y;
        }
        else if (y > 0.0D)
        {
            var15 += y;
        }

        if (z < 0.0D)
        {
            var11 += z;
        }
        else if (z > 0.0D)
        {
            var17 += z;
        }

        return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
    }

    /**
     * Returns a bounding box expanded by the specified vector (if negative numbers are given it will shrink). Args: x,
     * y, z
     */
    public AxisAlignedBB expand(double x, double y, double z)
    {
        double var7 = this.minX - x;
        double var9 = this.minY - y;
        double var11 = this.minZ - z;
        double var13 = this.maxX + x;
        double var15 = this.maxY + y;
        double var17 = this.maxZ + z;
        return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
    }

    public AxisAlignedBB union(AxisAlignedBB other)
    {
        double var2 = Math.min(this.minX, other.minX);
        double var4 = Math.min(this.minY, other.minY);
        double var6 = Math.min(this.minZ, other.minZ);
        double var8 = Math.max(this.maxX, other.maxX);
        double var10 = Math.max(this.maxY, other.maxY);
        double var12 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(var2, var4, var6, var8, var10, var12);
    }

    /**
     * returns an AABB with corners x1, y1, z1 and x2, y2, z2
     */
    public static AxisAlignedBB fromBounds(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double var12 = Math.min(x1, x2);
        double var14 = Math.min(y1, y2);
        double var16 = Math.min(z1, z2);
        double var18 = Math.max(x1, x2);
        double var20 = Math.max(y1, y2);
        double var22 = Math.max(z1, z2);
        return new AxisAlignedBB(var12, var14, var16, var18, var20, var22);
    }

    /**
     * Offsets the current bounding box by the specified coordinates. Args: x, y, z
     */
    public AxisAlignedBB offset(double x, double y, double z)
    {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateXOffset(AxisAlignedBB other, double offsetX)
    {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ)
        {
            double var4;

            if (offsetX > 0.0D && other.maxX <= this.minX)
            {
                var4 = this.minX - other.maxX;

                if (var4 < offsetX)
                {
                    offsetX = var4;
                }
            }
            else if (offsetX < 0.0D && other.minX >= this.maxX)
            {
                var4 = this.maxX - other.minX;

                if (var4 > offsetX)
                {
                    offsetX = var4;
                }
            }

            return offsetX;
        }
        else
        {
            return offsetX;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateYOffset(AxisAlignedBB other, double offsetY)
    {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ)
        {
            double var4;

            if (offsetY > 0.0D && other.maxY <= this.minY)
            {
                var4 = this.minY - other.maxY;

                if (var4 < offsetY)
                {
                    offsetY = var4;
                }
            }
            else if (offsetY < 0.0D && other.minY >= this.maxY)
            {
                var4 = this.maxY - other.minY;

                if (var4 > offsetY)
                {
                    offsetY = var4;
                }
            }

            return offsetY;
        }
        else
        {
            return offsetY;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateZOffset(AxisAlignedBB other, double offsetZ)
    {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY)
        {
            double var4;

            if (offsetZ > 0.0D && other.maxZ <= this.minZ)
            {
                var4 = this.minZ - other.maxZ;

                if (var4 < offsetZ)
                {
                    offsetZ = var4;
                }
            }
            else if (offsetZ < 0.0D && other.minZ >= this.maxZ)
            {
                var4 = this.maxZ - other.minZ;

                if (var4 > offsetZ)
                {
                    offsetZ = var4;
                }
            }

            return offsetZ;
        }
        else
        {
            return offsetZ;
        }
    }

    /**
     * Returns whether the given bounding box intersects with this one. Args: axisAlignedBB
     */
    public boolean intersectsWith(AxisAlignedBB other)
    {
        return other.maxX > this.minX && other.minX < this.maxX ? (other.maxY > this.minY && other.minY < this.maxY ? other.maxZ > this.minZ && other.minZ < this.maxZ : false) : false;
    }

    /**
     * Returns if the supplied Vec3D is completely inside the bounding box
     */
    public boolean isVecInside(Vec3 vec)
    {
        return vec.xCoord > this.minX && vec.xCoord < this.maxX ? (vec.yCoord > this.minY && vec.yCoord < this.maxY ? vec.zCoord > this.minZ && vec.zCoord < this.maxZ : false) : false;
    }

    /**
     * Returns the average length of the edges of the bounding box.
     */
    public double getAverageEdgeLength()
    {
        double var1 = this.maxX - this.minX;
        double var3 = this.maxY - this.minY;
        double var5 = this.maxZ - this.minZ;
        return (var1 + var3 + var5) / 3.0D;
    }

    /**
     * Returns a bounding box that is inset by the specified amounts
     */
    public AxisAlignedBB contract(double x, double y, double z)
    {
        double var7 = this.minX + x;
        double var9 = this.minY + y;
        double var11 = this.minZ + z;
        double var13 = this.maxX - x;
        double var15 = this.maxY - y;
        double var17 = this.maxZ - z;
        return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
    }

    public MovingObjectPosition calculateIntercept(Vec3 vecA, Vec3 vecB)
    {
        Vec3 var3 = vecA.getIntermediateWithXValue(vecB, this.minX);
        Vec3 var4 = vecA.getIntermediateWithXValue(vecB, this.maxX);
        Vec3 var5 = vecA.getIntermediateWithYValue(vecB, this.minY);
        Vec3 var6 = vecA.getIntermediateWithYValue(vecB, this.maxY);
        Vec3 var7 = vecA.getIntermediateWithZValue(vecB, this.minZ);
        Vec3 var8 = vecA.getIntermediateWithZValue(vecB, this.maxZ);

        if (!this.isVecInYZ(var3))
        {
            var3 = null;
        }

        if (!this.isVecInYZ(var4))
        {
            var4 = null;
        }

        if (!this.isVecInXZ(var5))
        {
            var5 = null;
        }

        if (!this.isVecInXZ(var6))
        {
            var6 = null;
        }

        if (!this.isVecInXY(var7))
        {
            var7 = null;
        }

        if (!this.isVecInXY(var8))
        {
            var8 = null;
        }

        Vec3 var9 = null;

        if (var3 != null)
        {
            var9 = var3;
        }

        if (var4 != null && (var9 == null || vecA.squareDistanceTo(var4) < vecA.squareDistanceTo(var9)))
        {
            var9 = var4;
        }

        if (var5 != null && (var9 == null || vecA.squareDistanceTo(var5) < vecA.squareDistanceTo(var9)))
        {
            var9 = var5;
        }

        if (var6 != null && (var9 == null || vecA.squareDistanceTo(var6) < vecA.squareDistanceTo(var9)))
        {
            var9 = var6;
        }

        if (var7 != null && (var9 == null || vecA.squareDistanceTo(var7) < vecA.squareDistanceTo(var9)))
        {
            var9 = var7;
        }

        if (var8 != null && (var9 == null || vecA.squareDistanceTo(var8) < vecA.squareDistanceTo(var9)))
        {
            var9 = var8;
        }

        if (var9 == null)
        {
            return null;
        }
        else
        {
            EnumFacing var10 = null;

            if (var9 == var3)
            {
                var10 = EnumFacing.WEST;
            }
            else if (var9 == var4)
            {
                var10 = EnumFacing.EAST;
            }
            else if (var9 == var5)
            {
                var10 = EnumFacing.DOWN;
            }
            else if (var9 == var6)
            {
                var10 = EnumFacing.UP;
            }
            else if (var9 == var7)
            {
                var10 = EnumFacing.NORTH;
            }
            else
            {
                var10 = EnumFacing.SOUTH;
            }

            return new MovingObjectPosition(var9, var10);
        }
    }

    /**
     * Checks if the specified vector is within the YZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInYZ(Vec3 vec)
    {
        return vec == null ? false : vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
    }

    /**
     * Checks if the specified vector is within the XZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXZ(Vec3 vec)
    {
        return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
    }

    /**
     * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXY(Vec3 vec)
    {
        return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
    }

    public String toString()
    {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}
