/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class MapGenVillage
extends MapGenStructure {
    public static final List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA);
    private int size;
    private int distance = 32;
    private final int minTownSeparation;

    public MapGenVillage() {
        this.minTownSeparation = 8;
    }

    public MapGenVillage(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("size")) {
                this.size = MathHelper.getInt(entry.getValue(), this.size, 0);
                continue;
            }
            if (!entry.getKey().equals("distance")) continue;
            this.distance = MathHelper.getInt(entry.getValue(), this.distance, 9);
        }
    }

    @Override
    public String getStructureName() {
        return "Village";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        boolean flag;
        int i = chunkX;
        int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.distance - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.distance - 1;
        }
        int k = chunkX / this.distance;
        int l = chunkZ / this.distance;
        Random random = this.worldObj.setRandomSeed(k, l, 10387312);
        k *= this.distance;
        l *= this.distance;
        return i == (k += random.nextInt(this.distance - 8)) && j == (l += random.nextInt(this.distance - 8)) && (flag = this.worldObj.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, VILLAGE_SPAWN_BIOMES));
    }

    @Override
    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_) {
        this.worldObj = worldIn;
        return MapGenVillage.func_191069_a(worldIn, this, pos, this.distance, 8, 10387312, false, 100, p_180706_3_);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(this.worldObj, this.rand, chunkX, chunkZ, this.size);
    }

    public static class Start
    extends StructureStart {
        private boolean hasMoreThanTwoComponents;

        public Start() {
        }

        public Start(World worldIn, Random rand, int x, int z, int size) {
            super(x, z);
            List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, size);
            StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
            this.components.add(structurevillagepieces$start);
            structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
            List<StructureComponent> list1 = structurevillagepieces$start.pendingRoads;
            List<StructureComponent> list2 = structurevillagepieces$start.pendingHouses;
            while (!list1.isEmpty() || !list2.isEmpty()) {
                if (list1.isEmpty()) {
                    int i = rand.nextInt(list2.size());
                    StructureComponent structurecomponent = list2.remove(i);
                    structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
                    continue;
                }
                int j = rand.nextInt(list1.size());
                StructureComponent structurecomponent2 = list1.remove(j);
                structurecomponent2.buildComponent(structurevillagepieces$start, this.components, rand);
            }
            this.updateBoundingBox();
            int k = 0;
            for (StructureComponent structurecomponent1 : this.components) {
                if (structurecomponent1 instanceof StructureVillagePieces.Road) continue;
                ++k;
            }
            this.hasMoreThanTwoComponents = k > 2;
        }

        @Override
        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }

        @Override
        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }

        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}

