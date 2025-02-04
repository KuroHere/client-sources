package net.minecraft.src;

import java.util.*;

public class ComponentNetherBridgeNetherStalkRoom extends ComponentNetherBridgePiece
{
    public ComponentNetherBridgeNetherStalkRoom(final int par1, final Random par2Random, final StructureBoundingBox par3StructureBoundingBox, final int par4) {
        super(par1);
        this.coordBaseMode = par4;
        this.boundingBox = par3StructureBoundingBox;
    }
    
    @Override
    public void buildComponent(final StructureComponent par1StructureComponent, final List par2List, final Random par3Random) {
        this.getNextComponentNormal((ComponentNetherBridgeStartPiece)par1StructureComponent, par2List, par3Random, 5, 3, true);
        this.getNextComponentNormal((ComponentNetherBridgeStartPiece)par1StructureComponent, par2List, par3Random, 5, 11, true);
    }
    
    public static ComponentNetherBridgeNetherStalkRoom createValidComponent(final List par0List, final Random par1Random, final int par2, final int par3, final int par4, final int par5, final int par6) {
        final StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -5, -3, 0, 13, 14, 13, par5);
        return (ComponentNetherBridgePiece.isAboveGround(var7) && StructureComponent.findIntersecting(par0List, var7) == null) ? new ComponentNetherBridgeNetherStalkRoom(par6, par1Random, var7, par5) : null;
    }
    
    @Override
    public boolean addComponentParts(final World par1World, final Random par2Random, final StructureBoundingBox par3StructureBoundingBox) {
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 3, 0, 12, 4, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 5, 0, 12, 13, 12, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 5, 0, 1, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 11, 5, 0, 12, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 5, 11, 4, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 5, 11, 10, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 9, 11, 7, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 5, 0, 4, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 5, 0, 10, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 9, 0, 7, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 11, 2, 10, 12, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        for (int var4 = 1; var4 <= 11; var4 += 2) {
            this.fillWithBlocks(par1World, par3StructureBoundingBox, var4, 10, 0, var4, 11, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, var4, 10, 12, var4, 11, 12, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 10, var4, 0, 11, var4, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 10, var4, 12, 11, var4, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, var4, 13, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, var4, 13, 12, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, 0, 13, var4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherBrick.blockID, 0, 12, 13, var4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, var4 + 1, 13, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, var4 + 1, 13, 12, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, var4 + 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 12, 13, var4 + 1, par3StructureBoundingBox);
        }
        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, 12, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 0, 13, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.netherFence.blockID, 0, 12, 13, 0, par3StructureBoundingBox);
        for (int var4 = 3; var4 <= 9; var4 += 2) {
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 7, var4, 1, 8, var4, Block.netherFence.blockID, Block.netherFence.blockID, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, 11, 7, var4, 11, 8, var4, Block.netherFence.blockID, Block.netherFence.blockID, false);
        }
        int var4 = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 3);
        for (int var5 = 0; var5 <= 6; ++var5) {
            final int var6 = var5 + 4;
            for (int var7 = 5; var7 <= 7; ++var7) {
                this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var4, var7, 5 + var5, var6, par3StructureBoundingBox);
            }
            if (var6 >= 5 && var6 <= 8) {
                this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 5, var6, 7, var5 + 4, var6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
            }
            else if (var6 >= 9 && var6 <= 10) {
                this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 8, var6, 7, var5 + 4, var6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
            }
            if (var5 >= 1) {
                this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 6 + var5, var6, 7, 9 + var5, var6, 0, 0, false);
            }
        }
        for (int var5 = 5; var5 <= 7; ++var5) {
            this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var4, var5, 12, 11, par3StructureBoundingBox);
        }
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 6, 7, 5, 7, 7, Block.netherFence.blockID, Block.netherFence.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 6, 7, 7, 7, 7, Block.netherFence.blockID, Block.netherFence.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 13, 12, 7, 13, 12, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 5, 2, 3, 5, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 5, 9, 3, 5, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 5, 4, 2, 5, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 5, 2, 10, 5, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 5, 9, 10, 5, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 5, 4, 10, 5, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        int var5 = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 0);
        final int var6 = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 1);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var6, 4, 5, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var6, 4, 5, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var6, 4, 5, 9, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var6, 4, 5, 10, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var5, 8, 5, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var5, 8, 5, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var5, 8, 5, 9, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairsNetherBrick.blockID, var5, 8, 5, 10, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 4, 4, 4, 4, 8, Block.slowSand.blockID, Block.slowSand.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 4, 4, 9, 4, 8, Block.slowSand.blockID, Block.slowSand.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 5, 4, 4, 5, 8, Block.netherStalk.blockID, Block.netherStalk.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 5, 4, 9, 5, 8, Block.netherStalk.blockID, Block.netherStalk.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 2, 0, 8, 2, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, 4, 12, 2, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 0, 8, 1, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 9, 8, 1, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 4, 3, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 4, 12, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
        for (int var7 = 4; var7 <= 8; ++var7) {
            for (int var8 = 0; var8 <= 2; ++var8) {
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, var7, -1, var8, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, var7, -1, 12 - var8, par3StructureBoundingBox);
            }
        }
        for (int var7 = 0; var7 <= 2; ++var7) {
            for (int var8 = 4; var8 <= 8; ++var8) {
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, var7, -1, var8, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.netherBrick.blockID, 0, 12 - var7, -1, var8, par3StructureBoundingBox);
            }
        }
        return true;
    }
}
