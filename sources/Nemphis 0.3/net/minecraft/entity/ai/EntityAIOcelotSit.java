/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSit
extends EntityAIMoveToBlock {
    private final EntityOcelot field_151493_a;
    private static final String __OBFID = "CL_00001601";

    public EntityAIOcelotSit(EntityOcelot p_i45315_1_, double p_i45315_2_) {
        super(p_i45315_1_, p_i45315_2_, 8);
        this.field_151493_a = p_i45315_1_;
    }

    @Override
    public boolean shouldExecute() {
        if (this.field_151493_a.isTamed() && !this.field_151493_a.isSitting() && super.shouldExecute()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return super.continueExecuting();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.field_151493_a.getAISit().setSitting(false);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.field_151493_a.setSitting(false);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        this.field_151493_a.getAISit().setSitting(false);
        if (!this.func_179487_f()) {
            this.field_151493_a.setSitting(false);
        } else if (!this.field_151493_a.isSitting()) {
            this.field_151493_a.setSitting(true);
        }
    }

    @Override
    protected boolean func_179488_a(World worldIn, BlockPos p_179488_2_) {
        if (!worldIn.isAirBlock(p_179488_2_.offsetUp())) {
            return false;
        }
        IBlockState var3 = worldIn.getBlockState(p_179488_2_);
        Block var4 = var3.getBlock();
        if (var4 == Blocks.chest) {
            TileEntity var5 = worldIn.getTileEntity(p_179488_2_);
            if (var5 instanceof TileEntityChest && ((TileEntityChest)var5).numPlayersUsing < 1) {
                return true;
            }
        } else {
            if (var4 == Blocks.lit_furnace) {
                return true;
            }
            if (var4 == Blocks.bed && var3.getValue(BlockBed.PART_PROP) != BlockBed.EnumPartType.HEAD) {
                return true;
            }
        }
        return false;
    }
}

