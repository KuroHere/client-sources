package net.minecraft.entity.monster;

import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Predicate;

public class VindicatorEntity extends AbstractIllagerEntity
{
    private static final Predicate<Difficulty> field_213681_b = (p_213678_0_) ->
    {
        return p_213678_0_ == Difficulty.NORMAL || p_213678_0_ == Difficulty.HARD;
    };
    private boolean johnny;

    public VindicatorEntity(EntityType <? extends VindicatorEntity > p_i50189_1_, World p_i50189_2_)
    {
        super(p_i50189_1_, p_i50189_2_);
    }

    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new VindicatorEntity.BreakDoorGoal(this));
        this.goalSelector.addGoal(2, new AbstractIllagerEntity.RaidOpenDoorGoal(this));
        this.goalSelector.addGoal(3, new AbstractRaiderEntity.FindTargetGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new VindicatorEntity.AttackGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(4, new VindicatorEntity.JohnnyAttackGoal(this));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    protected void updateAITasks()
    {
        if (!this.isAIDisabled() && GroundPathHelper.isGroundNavigator(this))
        {
            boolean flag = ((ServerWorld)this.world).hasRaid(this.getPosition());
            ((GroundPathNavigator)this.getNavigator()).setBreakDoors(flag);
        }

        super.updateAITasks();
    }

    public static AttributeModifierMap.MutableAttribute func_234322_eI_()
    {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.35F).createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D).createMutableAttribute(Attributes.MAX_HEALTH, 24.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);

        if (this.johnny)
        {
            compound.putBoolean("Johnny", true);
        }
    }

    public AbstractIllagerEntity.ArmPose getArmPose()
    {
        if (this.isAggressive())
        {
            return AbstractIllagerEntity.ArmPose.ATTACKING;
        }
        else
        {
            return this.getCelebrating() ? AbstractIllagerEntity.ArmPose.CELEBRATING : AbstractIllagerEntity.ArmPose.CROSSED;
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);

        if (compound.contains("Johnny", 99))
        {
            this.johnny = compound.getBoolean("Johnny");
        }
    }

    public SoundEvent getRaidLossSound()
    {
        return SoundEvents.ENTITY_VINDICATOR_CELEBRATE;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
    {
        ILivingEntityData ilivingentitydata = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        ((GroundPathNavigator)this.getNavigator()).setBreakDoors(true);
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return ilivingentitydata;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        if (this.getRaid() == null)
        {
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
        }
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn)
    {
        if (super.isOnSameTeam(entityIn))
        {
            return true;
        }
        else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER)
        {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        else
        {
            return false;
        }
    }

    public void setCustomName(@Nullable ITextComponent name)
    {
        super.setCustomName(name);

        if (!this.johnny && name != null && name.getString().equals("Johnny"))
        {
            this.johnny = true;
        }
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VINDICATOR_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VINDICATOR_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VINDICATOR_HURT;
    }

    public void applyWaveBonus(int wave, boolean p_213660_2_)
    {
        ItemStack itemstack = new ItemStack(Items.IRON_AXE);
        Raid raid = this.getRaid();
        int i = 1;

        if (wave > raid.getWaves(Difficulty.NORMAL))
        {
            i = 2;
        }

        boolean flag = this.rand.nextFloat() <= raid.getEnchantOdds();

        if (flag)
        {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemstack);
        }

        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
    }

    class AttackGoal extends MeleeAttackGoal
    {
        public AttackGoal(VindicatorEntity p_i50577_2_)
        {
            super(p_i50577_2_, 1.0D, false);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget)
        {
            if (this.attacker.getRidingEntity() instanceof RavagerEntity)
            {
                float f = this.attacker.getRidingEntity().getWidth() - 0.1F;
                return (double)(f * 2.0F * f * 2.0F + attackTarget.getWidth());
            }
            else
            {
                return super.getAttackReachSqr(attackTarget);
            }
        }
    }

    static class BreakDoorGoal extends net.minecraft.entity.ai.goal.BreakDoorGoal
    {
        public BreakDoorGoal(MobEntity p_i50578_1_)
        {
            super(p_i50578_1_, 6, VindicatorEntity.field_213681_b);
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldContinueExecuting()
        {
            VindicatorEntity vindicatorentity = (VindicatorEntity)this.entity;
            return vindicatorentity.isRaidActive() && super.shouldContinueExecuting();
        }

        public boolean shouldExecute()
        {
            VindicatorEntity vindicatorentity = (VindicatorEntity)this.entity;
            return vindicatorentity.isRaidActive() && vindicatorentity.rand.nextInt(10) == 0 && super.shouldExecute();
        }

        public void startExecuting()
        {
            super.startExecuting();
            this.entity.setIdleTime(0);
        }
    }

    static class JohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity>
    {
        public JohnnyAttackGoal(VindicatorEntity vindicator)
        {
            super(vindicator, LivingEntity.class, 0, true, true, LivingEntity::attackable);
        }

        public boolean shouldExecute()
        {
            return ((VindicatorEntity)this.goalOwner).johnny && super.shouldExecute();
        }

        public void startExecuting()
        {
            super.startExecuting();
            this.goalOwner.setIdleTime(0);
        }
    }
}
