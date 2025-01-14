/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import ru.govno.client.utils.TPSDetect;

public abstract class MobSpawnerBaseLogic {
    public int spawnDelay = 20;
    public final List<WeightedSpawnerEntity> potentialSpawns = Lists.newArrayList();
    public WeightedSpawnerEntity randomEntity = new WeightedSpawnerEntity();
    public double mobRotation;
    public double prevMobRotation;
    public int minSpawnDelay = 200;
    public int maxSpawnDelay = 800;
    public int spawnCount = 4;
    public Entity cachedEntity;
    public int maxNearbyEntities = 6;
    public int activatingRangeFromPlayer = 16;
    public int spawnRange = 4;
    public int timeToSpawn;

    @Nullable
    public ResourceLocation func_190895_g() {
        String s = this.randomEntity.getNbt().getString("id");
        return StringUtils.isNullOrEmpty(s) ? null : new ResourceLocation(s);
    }

    public void func_190894_a(@Nullable ResourceLocation p_190894_1_) {
        if (p_190894_1_ != null) {
            this.randomEntity.getNbt().setString("id", p_190894_1_.toString());
        }
    }

    public boolean isActivated() {
        BlockPos blockpos = this.getSpawnerPosition();
        return this.getSpawnerWorld().isAnyPlayerWithinRangeAt((double)blockpos.getX() + 0.5, (double)blockpos.getY() + 0.5, (double)blockpos.getZ() + 0.5, this.activatingRangeFromPlayer);
    }

    public void updateTime() {
        this.timeToSpawn = (int)((float)(this.spawnDelay * 50) * TPSDetect.getConpensationTPS(this.spawnDelay > 0) / 1000.0f + 0.5f);
    }

    public void updateSpawner() {
        if (!this.isActivated()) {
            this.prevMobRotation = this.mobRotation;
        } else {
            BlockPos blockpos = this.getSpawnerPosition();
            if (this.getSpawnerWorld().isRemote) {
                double d3 = (float)blockpos.getX() + this.getSpawnerWorld().rand.nextFloat();
                double d4 = (float)blockpos.getY() + this.getSpawnerWorld().rand.nextFloat();
                double d5 = (float)blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat();
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }
                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double)(1000.0f / ((float)this.spawnDelay + 200.0f))) % 360.0;
            } else {
                if (this.spawnDelay == -1) {
                    this.resetTimer();
                }
                this.timeToSpawn = (int)((float)(this.spawnDelay * 50) * TPSDetect.getConpensationTPS(this.spawnDelay > 0) / 1000.0f + 0.5f);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }
                boolean flag = false;
                for (int i = 0; i < this.spawnCount; ++i) {
                    double d2;
                    double d1;
                    int j;
                    double d0;
                    NBTTagCompound nbttagcompound = this.randomEntity.getNbt();
                    NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
                    World world = this.getSpawnerWorld();
                    Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0 = (j = nbttaglist.tagCount()) >= 1 ? nbttaglist.getDoubleAt(0) : (double)blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5, d1 = j >= 2 ? nbttaglist.getDoubleAt(1) : (double)(blockpos.getY() + world.rand.nextInt(3) - 1), d2 = j >= 3 ? nbttaglist.getDoubleAt(2) : (double)blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5, false);
                    if (entity == null) {
                        return;
                    }
                    int k = world.getEntitiesWithinAABB(entity.getClass(), new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1).expandXyz(this.spawnRange)).size();
                    if (k >= this.maxNearbyEntities) {
                        this.resetTimer();
                        return;
                    }
                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0f, 0.0f);
                    if (entityliving != null && (!entityliving.getCanSpawnHere() || !entityliving.isNotColliding())) continue;
                    if (this.randomEntity.getNbt().getSize() == 1 && this.randomEntity.getNbt().hasKey("id", 8) && entity instanceof EntityLiving) {
                        ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                    }
                    AnvilChunkLoader.spawnEntity(entity, world);
                    world.playEvent(2004, blockpos, 0);
                    if (entityliving != null) {
                        entityliving.spawnExplosionParticle();
                    }
                    flag = true;
                }
                if (flag) {
                    this.resetTimer();
                }
            }
        }
    }

    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }
        if (!this.potentialSpawns.isEmpty()) {
            this.setNextSpawnData(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.potentialSpawns));
        }
        this.broadcastEvent(1);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.spawnDelay = nbt.getShort("Delay");
        this.potentialSpawns.clear();
        if (nbt.hasKey("SpawnPotentials", 9)) {
            NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.potentialSpawns.add(new WeightedSpawnerEntity(nbttaglist.getCompoundTagAt(i)));
            }
        }
        if (nbt.hasKey("SpawnData", 10)) {
            this.setNextSpawnData(new WeightedSpawnerEntity(1, nbt.getCompoundTag("SpawnData")));
        } else if (!this.potentialSpawns.isEmpty()) {
            this.setNextSpawnData(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.potentialSpawns));
        }
        if (nbt.hasKey("MinSpawnDelay", 99)) {
            this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            this.spawnCount = nbt.getShort("SpawnCount");
        }
        if (nbt.hasKey("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }
        if (nbt.hasKey("SpawnRange", 99)) {
            this.spawnRange = nbt.getShort("SpawnRange");
        }
        if (this.getSpawnerWorld() != null) {
            this.cachedEntity = null;
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound p_189530_1_) {
        ResourceLocation resourcelocation = this.func_190895_g();
        if (resourcelocation == null) {
            return p_189530_1_;
        }
        p_189530_1_.setShort("Delay", (short)this.spawnDelay);
        p_189530_1_.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
        p_189530_1_.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        p_189530_1_.setShort("SpawnCount", (short)this.spawnCount);
        p_189530_1_.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        p_189530_1_.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
        p_189530_1_.setShort("SpawnRange", (short)this.spawnRange);
        p_189530_1_.setTag("SpawnData", this.randomEntity.getNbt().copy());
        NBTTagList nbttaglist = new NBTTagList();
        if (this.potentialSpawns.isEmpty()) {
            nbttaglist.appendTag(this.randomEntity.toCompoundTag());
        } else {
            for (WeightedSpawnerEntity weightedspawnerentity : this.potentialSpawns) {
                nbttaglist.appendTag(weightedspawnerentity.toCompoundTag());
            }
        }
        p_189530_1_.setTag("SpawnPotentials", nbttaglist);
        return p_189530_1_;
    }

    public Entity getCachedEntity() {
        if (this.cachedEntity == null) {
            this.cachedEntity = AnvilChunkLoader.readWorldEntity(this.randomEntity.getNbt(), this.getSpawnerWorld(), false);
            if (this.randomEntity.getNbt().getSize() == 1 && this.randomEntity.getNbt().hasKey("id", 8) && this.cachedEntity instanceof EntityLiving) {
                ((EntityLiving)this.cachedEntity).onInitialSpawn(this.getSpawnerWorld().getDifficultyForLocation(new BlockPos(this.cachedEntity)), null);
            }
        }
        return this.cachedEntity;
    }

    public boolean setDelayToMin(int delay) {
        if (delay == 1 && this.getSpawnerWorld().isRemote) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        return false;
    }

    public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
        this.randomEntity = p_184993_1_;
    }

    public abstract void broadcastEvent(int var1);

    public abstract World getSpawnerWorld();

    public abstract BlockPos getSpawnerPosition();

    public double getMobRotation() {
        return this.mobRotation;
    }

    public double getPrevMobRotation() {
        return this.prevMobRotation;
    }
}

