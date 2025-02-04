package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Predicate;

public class ExpirePOITask extends Task<LivingEntity>
{
    private final MemoryModuleType<GlobalPos> field_220591_a;
    private final Predicate<PointOfInterestType> poiType;

    public ExpirePOITask(PointOfInterestType p_i50338_1_, MemoryModuleType<GlobalPos> p_i50338_2_)
    {
        super(ImmutableMap.of(p_i50338_2_, MemoryModuleStatus.VALUE_PRESENT));
        this.poiType = p_i50338_1_.getPredicate();
        this.field_220591_a = p_i50338_2_;
    }

    protected boolean shouldExecute(ServerWorld worldIn, LivingEntity owner)
    {
        GlobalPos globalpos = owner.getBrain().getMemory(this.field_220591_a).get();
        return worldIn.getDimensionKey() == globalpos.getDimension() && globalpos.getPos().withinDistance(owner.getPositionVec(), 16.0D);
    }

    protected void startExecuting(ServerWorld worldIn, LivingEntity entityIn, long gameTimeIn)
    {
        Brain<?> brain = entityIn.getBrain();
        GlobalPos globalpos = brain.getMemory(this.field_220591_a).get();
        BlockPos blockpos = globalpos.getPos();
        ServerWorld serverworld = worldIn.getServer().getWorld(globalpos.getDimension());

        if (serverworld != null && !this.func_223020_a(serverworld, blockpos))
        {
            if (this.func_223019_a(serverworld, blockpos, entityIn))
            {
                brain.removeMemory(this.field_220591_a);
                worldIn.getPointOfInterestManager().release(blockpos);
                DebugPacketSender.func_218801_c(worldIn, blockpos);
            }
        }
        else
        {
            brain.removeMemory(this.field_220591_a);
        }
    }

    private boolean func_223019_a(ServerWorld world, BlockPos pos, LivingEntity p_223019_3_)
    {
        BlockState blockstate = world.getBlockState(pos);
        return blockstate.getBlock().isIn(BlockTags.BEDS) && blockstate.get(BedBlock.OCCUPIED) && !p_223019_3_.isSleeping();
    }

    private boolean func_223020_a(ServerWorld world, BlockPos pos)
    {
        return !world.getPointOfInterestManager().exists(pos, this.poiType);
    }
}
