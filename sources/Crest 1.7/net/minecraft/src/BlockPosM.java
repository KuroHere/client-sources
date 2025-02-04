// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.src;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;

public class BlockPosM extends BlockPos
{
    private int mx;
    private int my;
    private int mz;
    private int level;
    private BlockPosM[] facings;
    private boolean needsUpdate;
    
    public BlockPosM(final int x, final int y, final int z) {
        this(x, y, z, 0);
    }
    
    public BlockPosM(final double xIn, final double yIn, final double zIn) {
        this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }
    
    public BlockPosM(final int x, final int y, final int z, final int level) {
        super(0, 0, 0);
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.level = level;
    }
    
    @Override
    public int getX() {
        return this.mx;
    }
    
    @Override
    public int getY() {
        return this.my;
    }
    
    @Override
    public int getZ() {
        return this.mz;
    }
    
    public void setXyz(final int x, final int y, final int z) {
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.needsUpdate = true;
    }
    
    public void setXyz(final double xIn, final double yIn, final double zIn) {
        this.setXyz(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }
    
    @Override
    public BlockPos offset(final EnumFacing facing) {
        if (this.level <= 0) {
            return super.offset(facing, 1);
        }
        if (this.facings == null) {
            this.facings = new BlockPosM[EnumFacing.VALUES.length];
        }
        if (this.needsUpdate) {
            this.update();
        }
        final int index = facing.getIndex();
        BlockPosM bpm = this.facings[index];
        if (bpm == null) {
            final int nx = this.mx + facing.getFrontOffsetX();
            final int ny = this.my + facing.getFrontOffsetY();
            final int nz = this.mz + facing.getFrontOffsetZ();
            bpm = new BlockPosM(nx, ny, nz, this.level - 1);
            this.facings[index] = bpm;
        }
        return bpm;
    }
    
    @Override
    public BlockPos offset(final EnumFacing facing, final int n) {
        return (n == 1) ? this.offset(facing) : super.offset(facing, 1);
    }
    
    private void update() {
        for (int i = 0; i < 6; ++i) {
            final BlockPosM bpm = this.facings[i];
            if (bpm != null) {
                final EnumFacing facing = EnumFacing.VALUES[i];
                final int nx = this.mx + facing.getFrontOffsetX();
                final int ny = this.my + facing.getFrontOffsetY();
                final int nz = this.mz + facing.getFrontOffsetZ();
                bpm.setXyz(nx, ny, nz);
            }
        }
        this.needsUpdate = false;
    }
    
    public static Iterable getAllInBoxMutable(final BlockPos from, final BlockPos to) {
        final BlockPos posFrom = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos posTo = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable() {
            @Override
            public Iterator iterator() {
                return (Iterator)new AbstractIterator() {
                    private BlockPosM theBlockPosM = null;
                    
                    protected BlockPosM computeNext0() {
                        if (this.theBlockPosM == null) {
                            return this.theBlockPosM = new BlockPosM(posFrom.getX(), posFrom.getY(), posFrom.getZ(), 3);
                        }
                        if (this.theBlockPosM.equals(posTo)) {
                            return (BlockPosM)this.endOfData();
                        }
                        int bx = this.theBlockPosM.getX();
                        int by = this.theBlockPosM.getY();
                        int bz = this.theBlockPosM.getZ();
                        if (bx < posTo.getX()) {
                            ++bx;
                        }
                        else if (by < posTo.getY()) {
                            bx = posFrom.getX();
                            ++by;
                        }
                        else if (bz < posTo.getZ()) {
                            bx = posFrom.getX();
                            by = posFrom.getY();
                            ++bz;
                        }
                        this.theBlockPosM.setXyz(bx, by, bz);
                        return this.theBlockPosM;
                    }
                    
                    protected Object computeNext() {
                        return this.computeNext0();
                    }
                };
            }
        };
    }
}
