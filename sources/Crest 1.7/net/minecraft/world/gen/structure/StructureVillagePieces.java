// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockSandStone;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

public class StructureVillagePieces
{
    private static final String __OBFID = "CL_00000516";
    
    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(House1.class, "ViBH");
        MapGenStructureIO.registerStructureComponent(Field1.class, "ViDF");
        MapGenStructureIO.registerStructureComponent(Field2.class, "ViF");
        MapGenStructureIO.registerStructureComponent(Torch.class, "ViL");
        MapGenStructureIO.registerStructureComponent(Hall.class, "ViPH");
        MapGenStructureIO.registerStructureComponent(House4Garden.class, "ViSH");
        MapGenStructureIO.registerStructureComponent(WoodHut.class, "ViSmH");
        MapGenStructureIO.registerStructureComponent(Church.class, "ViST");
        MapGenStructureIO.registerStructureComponent(House2.class, "ViS");
        MapGenStructureIO.registerStructureComponent(Start.class, "ViStart");
        MapGenStructureIO.registerStructureComponent(Path.class, "ViSR");
        MapGenStructureIO.registerStructureComponent(House3.class, "ViTRH");
        MapGenStructureIO.registerStructureComponent(Well.class, "ViW");
    }
    
    public static List getStructureVillageWeightedPieceList(final Random p_75084_0_, final int p_75084_1_) {
        final ArrayList var2 = Lists.newArrayList();
        var2.add(new PieceWeight(House4Garden.class, 4, MathHelper.getRandomIntegerInRange(p_75084_0_, 2 + p_75084_1_, 4 + p_75084_1_ * 2)));
        var2.add(new PieceWeight(Church.class, 20, MathHelper.getRandomIntegerInRange(p_75084_0_, 0 + p_75084_1_, 1 + p_75084_1_)));
        var2.add(new PieceWeight(House1.class, 20, MathHelper.getRandomIntegerInRange(p_75084_0_, 0 + p_75084_1_, 2 + p_75084_1_)));
        var2.add(new PieceWeight(WoodHut.class, 3, MathHelper.getRandomIntegerInRange(p_75084_0_, 2 + p_75084_1_, 5 + p_75084_1_ * 3)));
        var2.add(new PieceWeight(Hall.class, 15, MathHelper.getRandomIntegerInRange(p_75084_0_, 0 + p_75084_1_, 2 + p_75084_1_)));
        var2.add(new PieceWeight(Field1.class, 3, MathHelper.getRandomIntegerInRange(p_75084_0_, 1 + p_75084_1_, 4 + p_75084_1_)));
        var2.add(new PieceWeight(Field2.class, 3, MathHelper.getRandomIntegerInRange(p_75084_0_, 2 + p_75084_1_, 4 + p_75084_1_ * 2)));
        var2.add(new PieceWeight(House2.class, 15, MathHelper.getRandomIntegerInRange(p_75084_0_, 0, 1 + p_75084_1_)));
        var2.add(new PieceWeight(House3.class, 8, MathHelper.getRandomIntegerInRange(p_75084_0_, 0 + p_75084_1_, 3 + p_75084_1_ * 2)));
        final Iterator var3 = var2.iterator();
        while (var3.hasNext()) {
            if (var3.next().villagePiecesLimit == 0) {
                var3.remove();
            }
        }
        return var2;
    }
    
    private static int func_75079_a(final List p_75079_0_) {
        boolean var1 = false;
        int var2 = 0;
        for (final PieceWeight var4 : p_75079_0_) {
            if (var4.villagePiecesLimit > 0 && var4.villagePiecesSpawned < var4.villagePiecesLimit) {
                var1 = true;
            }
            var2 += var4.villagePieceWeight;
        }
        return var1 ? var2 : -1;
    }
    
    private static Village func_176065_a(final Start p_176065_0_, final PieceWeight p_176065_1_, final List p_176065_2_, final Random p_176065_3_, final int p_176065_4_, final int p_176065_5_, final int p_176065_6_, final EnumFacing p_176065_7_, final int p_176065_8_) {
        final Class var9 = p_176065_1_.villagePieceClass;
        Object var10 = null;
        if (var9 == House4Garden.class) {
            var10 = House4Garden.func_175858_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == Church.class) {
            var10 = Church.func_175854_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == House1.class) {
            var10 = House1.func_175850_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == WoodHut.class) {
            var10 = WoodHut.func_175853_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == Hall.class) {
            var10 = Hall.func_175857_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == Field1.class) {
            var10 = Field1.func_175851_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == Field2.class) {
            var10 = Field2.func_175852_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == House2.class) {
            var10 = House2.func_175855_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (var9 == House3.class) {
            var10 = House3.func_175849_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        return (Village)var10;
    }
    
    private static Village func_176067_c(final Start p_176067_0_, final List p_176067_1_, final Random p_176067_2_, final int p_176067_3_, final int p_176067_4_, final int p_176067_5_, final EnumFacing p_176067_6_, final int p_176067_7_) {
        final int var8 = func_75079_a(p_176067_0_.structureVillageWeightedPieceList);
        if (var8 <= 0) {
            return null;
        }
        int var9 = 0;
        while (var9 < 5) {
            ++var9;
            int var10 = p_176067_2_.nextInt(var8);
            for (final PieceWeight var12 : p_176067_0_.structureVillageWeightedPieceList) {
                var10 -= var12.villagePieceWeight;
                if (var10 < 0) {
                    if (!var12.canSpawnMoreVillagePiecesOfType(p_176067_7_)) {
                        break;
                    }
                    if (var12 == p_176067_0_.structVillagePieceWeight && p_176067_0_.structureVillageWeightedPieceList.size() > 1) {
                        break;
                    }
                    final Village var13 = func_176065_a(p_176067_0_, var12, p_176067_1_, p_176067_2_, p_176067_3_, p_176067_4_, p_176067_5_, p_176067_6_, p_176067_7_);
                    if (var13 != null) {
                        final PieceWeight pieceWeight = var12;
                        ++pieceWeight.villagePiecesSpawned;
                        p_176067_0_.structVillagePieceWeight = var12;
                        if (!var12.canSpawnMoreVillagePieces()) {
                            p_176067_0_.structureVillageWeightedPieceList.remove(var12);
                        }
                        return var13;
                    }
                    continue;
                }
            }
        }
        final StructureBoundingBox var14 = Torch.func_175856_a(p_176067_0_, p_176067_1_, p_176067_2_, p_176067_3_, p_176067_4_, p_176067_5_, p_176067_6_);
        if (var14 != null) {
            return new Torch(p_176067_0_, p_176067_7_, p_176067_2_, var14, p_176067_6_);
        }
        return null;
    }
    
    private static StructureComponent func_176066_d(final Start p_176066_0_, final List p_176066_1_, final Random p_176066_2_, final int p_176066_3_, final int p_176066_4_, final int p_176066_5_, final EnumFacing p_176066_6_, final int p_176066_7_) {
        if (p_176066_7_ > 50) {
            return null;
        }
        if (Math.abs(p_176066_3_ - p_176066_0_.getBoundingBox().minX) <= 112 && Math.abs(p_176066_5_ - p_176066_0_.getBoundingBox().minZ) <= 112) {
            final Village var8 = func_176067_c(p_176066_0_, p_176066_1_, p_176066_2_, p_176066_3_, p_176066_4_, p_176066_5_, p_176066_6_, p_176066_7_ + 1);
            if (var8 != null) {
                final int var9 = (var8.boundingBox.minX + var8.boundingBox.maxX) / 2;
                final int var10 = (var8.boundingBox.minZ + var8.boundingBox.maxZ) / 2;
                final int var11 = var8.boundingBox.maxX - var8.boundingBox.minX;
                final int var12 = var8.boundingBox.maxZ - var8.boundingBox.minZ;
                final int var13 = (var11 > var12) ? var11 : var12;
                if (p_176066_0_.getWorldChunkManager().areBiomesViable(var9, var10, var13 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
                    p_176066_1_.add(var8);
                    p_176066_0_.field_74932_i.add(var8);
                    return var8;
                }
            }
            return null;
        }
        return null;
    }
    
    private static StructureComponent func_176069_e(final Start p_176069_0_, final List p_176069_1_, final Random p_176069_2_, final int p_176069_3_, final int p_176069_4_, final int p_176069_5_, final EnumFacing p_176069_6_, final int p_176069_7_) {
        if (p_176069_7_ > 3 + p_176069_0_.terrainType) {
            return null;
        }
        if (Math.abs(p_176069_3_ - p_176069_0_.getBoundingBox().minX) <= 112 && Math.abs(p_176069_5_ - p_176069_0_.getBoundingBox().minZ) <= 112) {
            final StructureBoundingBox var8 = Path.func_175848_a(p_176069_0_, p_176069_1_, p_176069_2_, p_176069_3_, p_176069_4_, p_176069_5_, p_176069_6_);
            if (var8 != null && var8.minY > 10) {
                final Path var9 = new Path(p_176069_0_, p_176069_7_, p_176069_2_, var8, p_176069_6_);
                final int var10 = (var9.boundingBox.minX + var9.boundingBox.maxX) / 2;
                final int var11 = (var9.boundingBox.minZ + var9.boundingBox.maxZ) / 2;
                final int var12 = var9.boundingBox.maxX - var9.boundingBox.minX;
                final int var13 = var9.boundingBox.maxZ - var9.boundingBox.minZ;
                final int var14 = (var12 > var13) ? var12 : var13;
                if (p_176069_0_.getWorldChunkManager().areBiomesViable(var10, var11, var14 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
                    p_176069_1_.add(var9);
                    p_176069_0_.field_74930_j.add(var9);
                    return var9;
                }
            }
            return null;
        }
        return null;
    }
    
    public static class Church extends Village
    {
        private static final String __OBFID = "CL_00000525";
        
        public Church() {
        }
        
        public Church(final Start p_i45564_1_, final int p_i45564_2_, final Random p_i45564_3_, final StructureBoundingBox p_i45564_4_, final EnumFacing p_i45564_5_) {
            super(p_i45564_1_, p_i45564_2_);
            this.coordBaseMode = p_i45564_5_;
            this.boundingBox = p_i45564_4_;
        }
        
        public static Church func_175854_a(final Start p_175854_0_, final List p_175854_1_, final Random p_175854_2_, final int p_175854_3_, final int p_175854_4_, final int p_175854_5_, final EnumFacing p_175854_6_, final int p_175854_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175854_3_, p_175854_4_, p_175854_5_, 0, 0, 0, 5, 12, 9, p_175854_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175854_1_, var8) == null) ? new Church(p_175854_0_, p_175854_7_, p_175854_2_, var8, p_175854_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 12 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 3, 3, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 5, 1, 3, 9, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 0, 3, 0, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 3, 10, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 1, 1, 4, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 4, 0, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 0, 4, 4, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 8, 3, 4, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 5, 4, 3, 10, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 5, 5, 3, 5, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 9, 0, 4, 9, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 0, 4, 4, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 0, 11, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 11, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 2, 11, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 2, 11, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 1, 1, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 1, 1, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 2, 1, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 3, 1, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 3, 1, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 1, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 1, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 3, 1, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 1)), 1, 2, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 0)), 3, 2, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 6, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 7, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 6, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 7, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 6, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 7, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 6, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 7, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 3, 8, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode.getOpposite()), 2, 4, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode.rotateY()), 1, 4, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode.rotateYCCW()), 3, 4, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode), 2, 4, 5, p_74875_3_);
            final int var4 = this.getMetadataWithOffset(Blocks.ladder, 4);
            for (int var5 = 1; var5 <= 9; ++var5) {
                this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(var4), 3, var5, 3, p_74875_3_);
            }
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 2, 0, p_74875_3_);
            this.func_175810_a(worldIn, p_74875_3_, p_74875_2_, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.func_175807_a(worldIn, 2, 0, -1, p_74875_3_).getBlock().getMaterial() == Material.air && this.func_175807_a(worldIn, 2, -1, -1, p_74875_3_).getBlock().getMaterial() != Material.air) {
                this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, p_74875_3_);
            }
            for (int var5 = 0; var5 < 9; ++var5) {
                for (int var6 = 0; var6 < 5; ++var6) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var6, 12, var5, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var6, -1, var5, p_74875_3_);
                }
            }
            this.spawnVillagers(worldIn, p_74875_3_, 2, 1, 2, 1);
            return true;
        }
        
        @Override
        protected int func_180779_c(final int p_180779_1_, final int p_180779_2_) {
            return 2;
        }
    }
    
    public static class Field1 extends Village
    {
        private Block cropTypeA;
        private Block cropTypeB;
        private Block cropTypeC;
        private Block cropTypeD;
        private static final String __OBFID = "CL_00000518";
        
        public Field1() {
        }
        
        public Field1(final Start p_i45570_1_, final int p_i45570_2_, final Random p_i45570_3_, final StructureBoundingBox p_i45570_4_, final EnumFacing p_i45570_5_) {
            super(p_i45570_1_, p_i45570_2_);
            this.coordBaseMode = p_i45570_5_;
            this.boundingBox = p_i45570_4_;
            this.cropTypeA = this.func_151559_a(p_i45570_3_);
            this.cropTypeB = this.func_151559_a(p_i45570_3_);
            this.cropTypeC = this.func_151559_a(p_i45570_3_);
            this.cropTypeD = this.func_151559_a(p_i45570_3_);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            super.writeStructureToNBT(p_143012_1_);
            p_143012_1_.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
            p_143012_1_.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
            p_143012_1_.setInteger("CC", Block.blockRegistry.getIDForObject(this.cropTypeC));
            p_143012_1_.setInteger("CD", Block.blockRegistry.getIDForObject(this.cropTypeD));
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            super.readStructureFromNBT(p_143011_1_);
            this.cropTypeA = Block.getBlockById(p_143011_1_.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(p_143011_1_.getInteger("CB"));
            this.cropTypeC = Block.getBlockById(p_143011_1_.getInteger("CC"));
            this.cropTypeD = Block.getBlockById(p_143011_1_.getInteger("CD"));
        }
        
        private Block func_151559_a(final Random p_151559_1_) {
            switch (p_151559_1_.nextInt(5)) {
                case 0: {
                    return Blocks.carrots;
                }
                case 1: {
                    return Blocks.potatoes;
                }
                default: {
                    return Blocks.wheat;
                }
            }
        }
        
        public static Field1 func_175851_a(final Start p_175851_0_, final List p_175851_1_, final Random p_175851_2_, final int p_175851_3_, final int p_175851_4_, final int p_175851_5_, final EnumFacing p_175851_6_, final int p_175851_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175851_3_, p_175851_4_, p_175851_5_, 0, 0, 0, 13, 4, 9, p_175851_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175851_1_, var8) == null) ? new Field1(p_175851_0_, p_175851_7_, p_175851_2_, var8, p_175851_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 12, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 0, 1, 8, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 10, 0, 1, 11, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 0, 1, 9, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
            for (int var4 = 1; var4 <= 7; ++var4) {
                this.func_175811_a(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 1, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 2, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 4, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 5, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 7, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 8, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 10, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 11, 1, var4, p_74875_3_);
            }
            for (int var4 = 0; var4 < 9; ++var4) {
                for (int var5 = 0; var5 < 13; ++var5) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var5, 4, var4, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.dirt.getDefaultState(), var5, -1, var4, p_74875_3_);
                }
            }
            return true;
        }
    }
    
    public static class Field2 extends Village
    {
        private Block cropTypeA;
        private Block cropTypeB;
        private static final String __OBFID = "CL_00000519";
        
        public Field2() {
        }
        
        public Field2(final Start p_i45569_1_, final int p_i45569_2_, final Random p_i45569_3_, final StructureBoundingBox p_i45569_4_, final EnumFacing p_i45569_5_) {
            super(p_i45569_1_, p_i45569_2_);
            this.coordBaseMode = p_i45569_5_;
            this.boundingBox = p_i45569_4_;
            this.cropTypeA = this.func_151560_a(p_i45569_3_);
            this.cropTypeB = this.func_151560_a(p_i45569_3_);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            super.writeStructureToNBT(p_143012_1_);
            p_143012_1_.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
            p_143012_1_.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            super.readStructureFromNBT(p_143011_1_);
            this.cropTypeA = Block.getBlockById(p_143011_1_.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(p_143011_1_.getInteger("CB"));
        }
        
        private Block func_151560_a(final Random p_151560_1_) {
            switch (p_151560_1_.nextInt(5)) {
                case 0: {
                    return Blocks.carrots;
                }
                case 1: {
                    return Blocks.potatoes;
                }
                default: {
                    return Blocks.wheat;
                }
            }
        }
        
        public static Field2 func_175852_a(final Start p_175852_0_, final List p_175852_1_, final Random p_175852_2_, final int p_175852_3_, final int p_175852_4_, final int p_175852_5_, final EnumFacing p_175852_6_, final int p_175852_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175852_3_, p_175852_4_, p_175852_5_, 0, 0, 0, 7, 4, 9, p_175852_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175852_1_, var8) == null) ? new Field2(p_175852_0_, p_175852_7_, p_175852_2_, var8, p_175852_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 6, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 0, 5, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 8, 5, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
            for (int var4 = 1; var4 <= 7; ++var4) {
                this.func_175811_a(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 1, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 2, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 4, 1, var4, p_74875_3_);
                this.func_175811_a(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(p_74875_2_, 2, 7)), 5, 1, var4, p_74875_3_);
            }
            for (int var4 = 0; var4 < 9; ++var4) {
                for (int var5 = 0; var5 < 7; ++var5) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var5, 4, var4, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.dirt.getDefaultState(), var5, -1, var4, p_74875_3_);
                }
            }
            return true;
        }
    }
    
    public static class Hall extends Village
    {
        private static final String __OBFID = "CL_00000522";
        
        public Hall() {
        }
        
        public Hall(final Start p_i45567_1_, final int p_i45567_2_, final Random p_i45567_3_, final StructureBoundingBox p_i45567_4_, final EnumFacing p_i45567_5_) {
            super(p_i45567_1_, p_i45567_2_);
            this.coordBaseMode = p_i45567_5_;
            this.boundingBox = p_i45567_4_;
        }
        
        public static Hall func_175857_a(final Start p_175857_0_, final List p_175857_1_, final Random p_175857_2_, final int p_175857_3_, final int p_175857_4_, final int p_175857_5_, final EnumFacing p_175857_6_, final int p_175857_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175857_3_, p_175857_4_, p_175857_5_, 0, 0, 0, 9, 7, 11, p_175857_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175857_1_, var8) == null) ? new Hall(p_175857_0_, p_175857_7_, p_175857_2_, var8, p_175857_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 0, 6, 8, 0, 10, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 6, 0, 6, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 6, 2, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 1, 6, 8, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 10, 7, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 0, 0, 8, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 5, 7, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 5, 7, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 4, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 0, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 0, 4, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 4, 3, p_74875_3_);
            final int var4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            final int var5 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            for (int var6 = -1; var6 <= 2; ++var6) {
                for (int var7 = 0; var7 <= 8; ++var7) {
                    this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var4), var7, 4 + var6, var6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var5), var7, 4 + var6, 5 - var6, p_74875_3_);
                }
            }
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 0, 2, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 0, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 8, 2, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 8, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 3, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 2, 1, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 1, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 3, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 5, 0, 1, 7, 0, 3, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.double_stone_slab.getDefaultState(), 6, 1, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.double_stone_slab.getDefaultState(), 6, 1, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode), 2, 3, 1, p_74875_3_);
            this.func_175810_a(worldIn, p_74875_3_, p_74875_2_, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.func_175807_a(worldIn, 2, 0, -1, p_74875_3_).getBlock().getMaterial() == Material.air && this.func_175807_a(worldIn, 2, -1, -1, p_74875_3_).getBlock().getMaterial() != Material.air) {
                this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, p_74875_3_);
            }
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 6, 1, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 6, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode.getOpposite()), 6, 3, 4, p_74875_3_);
            this.func_175810_a(worldIn, p_74875_3_, p_74875_2_, 6, 1, 5, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            for (int var6 = 0; var6 < 5; ++var6) {
                for (int var7 = 0; var7 < 9; ++var7) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var7, 7, var6, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var7, -1, var6, p_74875_3_);
                }
            }
            this.spawnVillagers(worldIn, p_74875_3_, 4, 1, 2, 2);
            return true;
        }
        
        @Override
        protected int func_180779_c(final int p_180779_1_, final int p_180779_2_) {
            return (p_180779_1_ == 0) ? 4 : super.func_180779_c(p_180779_1_, p_180779_2_);
        }
    }
    
    public static class House1 extends Village
    {
        private static final String __OBFID = "CL_00000517";
        
        public House1() {
        }
        
        public House1(final Start p_i45571_1_, final int p_i45571_2_, final Random p_i45571_3_, final StructureBoundingBox p_i45571_4_, final EnumFacing p_i45571_5_) {
            super(p_i45571_1_, p_i45571_2_);
            this.coordBaseMode = p_i45571_5_;
            this.boundingBox = p_i45571_4_;
        }
        
        public static House1 func_175850_a(final Start p_175850_0_, final List p_175850_1_, final Random p_175850_2_, final int p_175850_3_, final int p_175850_4_, final int p_175850_5_, final EnumFacing p_175850_6_, final int p_175850_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175850_3_, p_175850_4_, p_175850_5_, 0, 0, 0, 9, 9, 6, p_175850_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175850_1_, var8) == null) ? new House1(p_175850_0_, p_175850_7_, p_175850_2_, var8, p_175850_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 7, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 8, 0, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 5, 0, 8, 5, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 6, 1, 8, 6, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 7, 2, 8, 7, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            final int var4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            final int var5 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            for (int var6 = -1; var6 <= 2; ++var6) {
                for (int var7 = 0; var7 <= 8; ++var7) {
                    this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var4), var7, 6 + var6, var6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var5), var7, 6 + var6, 5 - var6, p_74875_3_);
                }
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 5, 8, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 1, 0, 8, 1, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 0, 0, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 5, 0, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 2, 5, 8, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 2, 0, 8, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 2, 1, 0, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 5, 7, 4, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 2, 1, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 7, 4, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 5, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 6, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 3, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 3, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 3, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 5, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 1, 7, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 4, 7, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 3, 4, 7, 3, 4, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 7, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), 7, 1, 3, p_74875_3_);
            int var6 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var6), 6, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var6), 5, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var6), 4, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var6), 3, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 6, 1, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 6, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 1, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 4, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.crafting_table.getDefaultState(), 7, 1, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, 2, 0, p_74875_3_);
            this.func_175810_a(worldIn, p_74875_3_, p_74875_2_, 1, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.func_175807_a(worldIn, 1, 0, -1, p_74875_3_).getBlock().getMaterial() == Material.air && this.func_175807_a(worldIn, 1, -1, -1, p_74875_3_).getBlock().getMaterial() != Material.air) {
                this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, p_74875_3_);
            }
            for (int var7 = 0; var7 < 6; ++var7) {
                for (int var8 = 0; var8 < 9; ++var8) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var8, 9, var7, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var8, -1, var7, p_74875_3_);
                }
            }
            this.spawnVillagers(worldIn, p_74875_3_, 2, 1, 2, 1);
            return true;
        }
        
        @Override
        protected int func_180779_c(final int p_180779_1_, final int p_180779_2_) {
            return 1;
        }
    }
    
    public static class House2 extends Village
    {
        private static final List villageBlacksmithChestContents;
        private boolean hasMadeChest;
        private static final String __OBFID = "CL_00000526";
        
        static {
            villageBlacksmithChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 3, 7, 5), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 0, 3, 7, 5), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
        }
        
        public House2() {
        }
        
        public House2(final Start p_i45563_1_, final int p_i45563_2_, final Random p_i45563_3_, final StructureBoundingBox p_i45563_4_, final EnumFacing p_i45563_5_) {
            super(p_i45563_1_, p_i45563_2_);
            this.coordBaseMode = p_i45563_5_;
            this.boundingBox = p_i45563_4_;
        }
        
        public static House2 func_175855_a(final Start p_175855_0_, final List p_175855_1_, final Random p_175855_2_, final int p_175855_3_, final int p_175855_4_, final int p_175855_5_, final EnumFacing p_175855_6_, final int p_175855_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175855_3_, p_175855_4_, p_175855_5_, 0, 0, 0, 10, 6, 7, p_175855_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175855_1_, var8) == null) ? new House2(p_175855_0_, p_175855_7_, p_175855_2_, var8, p_175855_6_) : null;
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            super.writeStructureToNBT(p_143012_1_);
            p_143012_1_.setBoolean("Chest", this.hasMadeChest);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            super.readStructureFromNBT(p_143011_1_);
            this.hasMadeChest = p_143011_1_.getBoolean("Chest");
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 9, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 9, 0, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 0, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 5, 0, 9, 5, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 5, 1, 8, 5, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 3, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 6, 0, 4, 6, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 3, 3, 1, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 2, 3, 3, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 1, 3, 5, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 6, 5, 3, 6, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 1, 0, 5, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 9, 1, 0, 9, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 1, 4, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.flowing_lava.getDefaultState(), 7, 1, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.flowing_lava.getDefaultState(), 8, 1, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), 9, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), 9, 2, 4, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 7, 2, 4, 8, 2, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 6, 1, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.furnace.getDefaultState(), 6, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.furnace.getDefaultState(), 6, 3, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.double_stone_slab.getDefaultState(), 8, 1, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 2, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 1, 1, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 4, p_74875_3_);
            if (!this.hasMadeChest && p_74875_3_.func_175898_b(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5)))) {
                this.hasMadeChest = true;
                this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 5, 1, 5, House2.villageBlacksmithChestContents, 3 + p_74875_2_.nextInt(6));
            }
            for (int var4 = 6; var4 <= 8; ++var4) {
                if (this.func_175807_a(worldIn, var4, 0, -1, p_74875_3_).getBlock().getMaterial() == Material.air && this.func_175807_a(worldIn, var4, -1, -1, p_74875_3_).getBlock().getMaterial() != Material.air) {
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), var4, 0, -1, p_74875_3_);
                }
            }
            for (int var4 = 0; var4 < 7; ++var4) {
                for (int var5 = 0; var5 < 10; ++var5) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var5, 6, var4, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var5, -1, var4, p_74875_3_);
                }
            }
            this.spawnVillagers(worldIn, p_74875_3_, 7, 1, 1, 1);
            return true;
        }
        
        @Override
        protected int func_180779_c(final int p_180779_1_, final int p_180779_2_) {
            return 3;
        }
    }
    
    public static class House3 extends Village
    {
        private static final String __OBFID = "CL_00000530";
        
        public House3() {
        }
        
        public House3(final Start p_i45561_1_, final int p_i45561_2_, final Random p_i45561_3_, final StructureBoundingBox p_i45561_4_, final EnumFacing p_i45561_5_) {
            super(p_i45561_1_, p_i45561_2_);
            this.coordBaseMode = p_i45561_5_;
            this.boundingBox = p_i45561_4_;
        }
        
        public static House3 func_175849_a(final Start p_175849_0_, final List p_175849_1_, final Random p_175849_2_, final int p_175849_3_, final int p_175849_4_, final int p_175849_5_, final EnumFacing p_175849_6_, final int p_175849_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175849_3_, p_175849_4_, p_175849_5_, 0, 0, 0, 9, 7, 12, p_175849_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175849_1_, var8) == null) ? new House3(p_175849_0_, p_175849_7_, p_175849_2_, var8, p_175849_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 0, 5, 8, 0, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 8, 0, 0, 8, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 0, 7, 2, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 5, 2, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 2, 0, 6, 2, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 0, 10, 7, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 2, 5, 2, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 4, 3, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 0, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 0, 4, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 4, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 4, 4, p_74875_3_);
            final int var4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            final int var5 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            for (int var6 = -1; var6 <= 2; ++var6) {
                for (int var7 = 0; var7 <= 8; ++var7) {
                    this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var4), var7, 4 + var6, var6, p_74875_3_);
                    if ((var6 > -1 || var7 <= 1) && (var6 > 0 || var7 <= 3) && (var6 > 1 || var7 <= 4 || var7 >= 6)) {
                        this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var5), var7, 4 + var6, 5 - var6, p_74875_3_);
                    }
                }
            }
            this.func_175804_a(worldIn, p_74875_3_, 3, 4, 5, 3, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 7, 4, 2, 7, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 5, 4, 4, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 6, 5, 4, 6, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 5, 6, 3, 5, 6, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            int var6 = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
            for (int var7 = 4; var7 >= 1; --var7) {
                this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), var7, 2 + var7, 7 - var7, p_74875_3_);
                for (int var8 = 8 - var7; var8 <= 10; ++var8) {
                    this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var6), var7, 2 + var7, var8, p_74875_3_);
                }
            }
            int var7 = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 6, 6, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 7, 5, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var7), 6, 6, 4, p_74875_3_);
            for (int var8 = 6; var8 <= 8; ++var8) {
                for (int var9 = 5; var9 <= 10; ++var9) {
                    this.func_175811_a(worldIn, Blocks.oak_stairs.getStateFromMeta(var7), var8, 12 - var8, var9, p_74875_3_);
                }
            }
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 0, 2, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 0, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 4, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 6, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 8, 2, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 8, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 2, 5, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 8, 2, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 8, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 8, 2, 9, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 2, 2, 6, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 7, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 8, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 2, 2, 9, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 4, 4, 10, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 5, 4, 10, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 6, 4, 10, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 5, 5, 10, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode), 2, 3, 1, p_74875_3_);
            this.func_175810_a(worldIn, p_74875_3_, p_74875_2_, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, -1, 3, 2, -1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (this.func_175807_a(worldIn, 2, 0, -1, p_74875_3_).getBlock().getMaterial() == Material.air && this.func_175807_a(worldIn, 2, -1, -1, p_74875_3_).getBlock().getMaterial() != Material.air) {
                this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, p_74875_3_);
            }
            for (int var8 = 0; var8 < 5; ++var8) {
                for (int var9 = 0; var9 < 9; ++var9) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var9, 7, var8, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var9, -1, var8, p_74875_3_);
                }
            }
            for (int var8 = 5; var8 < 11; ++var8) {
                for (int var9 = 2; var9 < 9; ++var9) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var9, 7, var8, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var9, -1, var8, p_74875_3_);
                }
            }
            this.spawnVillagers(worldIn, p_74875_3_, 4, 1, 2, 2);
            return true;
        }
    }
    
    public static class House4Garden extends Village
    {
        private boolean isRoofAccessible;
        private static final String __OBFID = "CL_00000523";
        
        public House4Garden() {
        }
        
        public House4Garden(final Start p_i45566_1_, final int p_i45566_2_, final Random p_i45566_3_, final StructureBoundingBox p_i45566_4_, final EnumFacing p_i45566_5_) {
            super(p_i45566_1_, p_i45566_2_);
            this.coordBaseMode = p_i45566_5_;
            this.boundingBox = p_i45566_4_;
            this.isRoofAccessible = p_i45566_3_.nextBoolean();
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            super.writeStructureToNBT(p_143012_1_);
            p_143012_1_.setBoolean("Terrace", this.isRoofAccessible);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            super.readStructureFromNBT(p_143011_1_);
            this.isRoofAccessible = p_143011_1_.getBoolean("Terrace");
        }
        
        public static House4Garden func_175858_a(final Start p_175858_0_, final List p_175858_1_, final Random p_175858_2_, final int p_175858_3_, final int p_175858_4_, final int p_175858_5_, final EnumFacing p_175858_6_, final int p_175858_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175858_3_, p_175858_4_, p_175858_5_, 0, 0, 0, 5, 6, 5, p_175858_6_);
            return (StructureComponent.findIntersecting(p_175858_1_, var8) != null) ? null : new House4Garden(p_175858_0_, p_175858_7_, p_175858_2_, var8, p_175858_6_);
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 4, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 4, 0, 4, 4, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 4, 1, 3, 4, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 0, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 0, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 0, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 0, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 0, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 0, 3, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 4, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 4, 1, 1, 4, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 4, 3, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 1, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 1, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 1, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 2, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 3, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 3, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 3, 1, 0, p_74875_3_);
            if (this.func_175807_a(worldIn, 2, 0, -1, p_74875_3_).getBlock().getMaterial() == Material.air && this.func_175807_a(worldIn, 2, -1, -1, p_74875_3_).getBlock().getMaterial() != Material.air) {
                this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, p_74875_3_);
            }
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 3, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (this.isRoofAccessible) {
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 5, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 2, 5, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 3, 5, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 5, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 2, 5, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 3, 5, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 3, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 3, p_74875_3_);
            }
            if (this.isRoofAccessible) {
                final int var4 = this.getMetadataWithOffset(Blocks.ladder, 3);
                this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(var4), 3, 1, 3, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(var4), 3, 2, 3, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(var4), 3, 3, 3, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(var4), 3, 4, 3, p_74875_3_);
            }
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode), 2, 3, 1, p_74875_3_);
            for (int var4 = 0; var4 < 5; ++var4) {
                for (int var5 = 0; var5 < 5; ++var5) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var5, 6, var4, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var5, -1, var4, p_74875_3_);
                }
            }
            this.spawnVillagers(worldIn, p_74875_3_, 1, 1, 2, 1);
            return true;
        }
    }
    
    public static class Path extends Road
    {
        private int averageGroundLevel;
        private static final String __OBFID = "CL_00000528";
        
        public Path() {
        }
        
        public Path(final Start p_i45562_1_, final int p_i45562_2_, final Random p_i45562_3_, final StructureBoundingBox p_i45562_4_, final EnumFacing p_i45562_5_) {
            super(p_i45562_1_, p_i45562_2_);
            this.coordBaseMode = p_i45562_5_;
            this.boundingBox = p_i45562_4_;
            this.averageGroundLevel = Math.max(p_i45562_4_.getXSize(), p_i45562_4_.getZSize());
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            super.writeStructureToNBT(p_143012_1_);
            p_143012_1_.setInteger("Length", this.averageGroundLevel);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            super.readStructureFromNBT(p_143011_1_);
            this.averageGroundLevel = p_143011_1_.getInteger("Length");
        }
        
        @Override
        public void buildComponent(final StructureComponent p_74861_1_, final List p_74861_2_, final Random p_74861_3_) {
            boolean var4 = false;
            for (int var5 = p_74861_3_.nextInt(5); var5 < this.averageGroundLevel - 8; var5 += 2 + p_74861_3_.nextInt(5)) {
                final StructureComponent var6 = this.getNextComponentNN((Start)p_74861_1_, p_74861_2_, p_74861_3_, 0, var5);
                if (var6 != null) {
                    var5 += Math.max(var6.boundingBox.getXSize(), var6.boundingBox.getZSize());
                    var4 = true;
                }
            }
            for (int var5 = p_74861_3_.nextInt(5); var5 < this.averageGroundLevel - 8; var5 += 2 + p_74861_3_.nextInt(5)) {
                final StructureComponent var6 = this.getNextComponentPP((Start)p_74861_1_, p_74861_2_, p_74861_3_, 0, var5);
                if (var6 != null) {
                    var5 += Math.max(var6.boundingBox.getXSize(), var6.boundingBox.getZSize());
                    var4 = true;
                }
            }
            if (var4 && p_74861_3_.nextInt(3) > 0 && this.coordBaseMode != null) {
                switch (StructureVillagePieces.SwitchEnumFacing.field_176064_a[this.coordBaseMode.ordinal()]) {
                    case 1: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                        break;
                    }
                    case 2: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    }
                    case 3: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    }
                    case 4: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    }
                }
            }
            if (var4 && p_74861_3_.nextInt(3) > 0 && this.coordBaseMode != null) {
                switch (StructureVillagePieces.SwitchEnumFacing.field_176064_a[this.coordBaseMode.ordinal()]) {
                    case 1: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;
                    }
                    case 2: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    }
                    case 3: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    }
                    case 4: {
                        func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    }
                }
            }
        }
        
        public static StructureBoundingBox func_175848_a(final Start p_175848_0_, final List p_175848_1_, final Random p_175848_2_, final int p_175848_3_, final int p_175848_4_, final int p_175848_5_, final EnumFacing p_175848_6_) {
            for (int var7 = 7 * MathHelper.getRandomIntegerInRange(p_175848_2_, 3, 5); var7 >= 7; var7 -= 7) {
                final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175848_3_, p_175848_4_, p_175848_5_, 0, 0, 0, 3, 3, var7, p_175848_6_);
                if (StructureComponent.findIntersecting(p_175848_1_, var8) == null) {
                    return var8;
                }
            }
            return null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            final IBlockState var4 = this.func_175847_a(Blocks.gravel.getDefaultState());
            final IBlockState var5 = this.func_175847_a(Blocks.cobblestone.getDefaultState());
            for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6) {
                for (int var7 = this.boundingBox.minZ; var7 <= this.boundingBox.maxZ; ++var7) {
                    BlockPos var8 = new BlockPos(var6, 64, var7);
                    if (p_74875_3_.func_175898_b(var8)) {
                        var8 = worldIn.func_175672_r(var8).offsetDown();
                        worldIn.setBlockState(var8, var4, 2);
                        worldIn.setBlockState(var8.offsetDown(), var5, 2);
                    }
                }
            }
            return true;
        }
    }
    
    public static class PieceWeight
    {
        public Class villagePieceClass;
        public final int villagePieceWeight;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;
        private static final String __OBFID = "CL_00000521";
        
        public PieceWeight(final Class p_i2098_1_, final int p_i2098_2_, final int p_i2098_3_) {
            this.villagePieceClass = p_i2098_1_;
            this.villagePieceWeight = p_i2098_2_;
            this.villagePiecesLimit = p_i2098_3_;
        }
        
        public boolean canSpawnMoreVillagePiecesOfType(final int p_75085_1_) {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
        
        public boolean canSpawnMoreVillagePieces() {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }
    
    public abstract static class Road extends Village
    {
        private static final String __OBFID = "CL_00000532";
        
        public Road() {
        }
        
        protected Road(final Start p_i2108_1_, final int p_i2108_2_) {
            super(p_i2108_1_, p_i2108_2_);
        }
    }
    
    public static class Start extends Well
    {
        public WorldChunkManager worldChunkMngr;
        public boolean inDesert;
        public int terrainType;
        public PieceWeight structVillagePieceWeight;
        public List structureVillageWeightedPieceList;
        public List field_74932_i;
        public List field_74930_j;
        private static final String __OBFID = "CL_00000527";
        
        public Start() {
            this.field_74932_i = Lists.newArrayList();
            this.field_74930_j = Lists.newArrayList();
        }
        
        public Start(final WorldChunkManager p_i2104_1_, final int p_i2104_2_, final Random p_i2104_3_, final int p_i2104_4_, final int p_i2104_5_, final List p_i2104_6_, final int p_i2104_7_) {
            super(null, 0, p_i2104_3_, p_i2104_4_, p_i2104_5_);
            this.field_74932_i = Lists.newArrayList();
            this.field_74930_j = Lists.newArrayList();
            this.worldChunkMngr = p_i2104_1_;
            this.structureVillageWeightedPieceList = p_i2104_6_;
            this.terrainType = p_i2104_7_;
            final BiomeGenBase var8 = p_i2104_1_.func_180300_a(new BlockPos(p_i2104_4_, 0, p_i2104_5_), BiomeGenBase.field_180279_ad);
            this.func_175846_a(this.inDesert = (var8 == BiomeGenBase.desert || var8 == BiomeGenBase.desertHills));
        }
        
        public WorldChunkManager getWorldChunkManager() {
            return this.worldChunkMngr;
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_176064_a;
        private static final String __OBFID = "CL_00001968";
        
        static {
            field_176064_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_176064_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_176064_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_176064_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_176064_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
    
    public static class Torch extends Village
    {
        private static final String __OBFID = "CL_00000520";
        
        public Torch() {
        }
        
        public Torch(final Start p_i45568_1_, final int p_i45568_2_, final Random p_i45568_3_, final StructureBoundingBox p_i45568_4_, final EnumFacing p_i45568_5_) {
            super(p_i45568_1_, p_i45568_2_);
            this.coordBaseMode = p_i45568_5_;
            this.boundingBox = p_i45568_4_;
        }
        
        public static StructureBoundingBox func_175856_a(final Start p_175856_0_, final List p_175856_1_, final Random p_175856_2_, final int p_175856_3_, final int p_175856_4_, final int p_175856_5_, final EnumFacing p_175856_6_) {
            final StructureBoundingBox var7 = StructureBoundingBox.func_175897_a(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 3, 4, 2, p_175856_6_);
            return (StructureComponent.findIntersecting(p_175856_1_, var7) != null) ? null : var7;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 2, 3, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 0, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 2, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.wool.getStateFromMeta(EnumDyeColor.WHITE.getDyeColorDamage()), 1, 3, 0, p_74875_3_);
            final boolean var4 = this.coordBaseMode == EnumFacing.EAST || this.coordBaseMode == EnumFacing.NORTH;
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode.rotateY()), var4 ? 2 : 0, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode), 1, 3, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode.rotateYCCW()), var4 ? 0 : 2, 3, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, this.coordBaseMode.getOpposite()), 1, 3, -1, p_74875_3_);
            return true;
        }
    }
    
    abstract static class Village extends StructureComponent
    {
        protected int field_143015_k;
        private int villagersSpawned;
        private boolean field_143014_b;
        private static final String __OBFID = "CL_00000531";
        
        public Village() {
            this.field_143015_k = -1;
        }
        
        protected Village(final Start p_i2107_1_, final int p_i2107_2_) {
            super(p_i2107_2_);
            this.field_143015_k = -1;
            if (p_i2107_1_ != null) {
                this.field_143014_b = p_i2107_1_.inDesert;
            }
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            p_143012_1_.setInteger("HPos", this.field_143015_k);
            p_143012_1_.setInteger("VCount", this.villagersSpawned);
            p_143012_1_.setBoolean("Desert", this.field_143014_b);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            this.field_143015_k = p_143011_1_.getInteger("HPos");
            this.villagersSpawned = p_143011_1_.getInteger("VCount");
            this.field_143014_b = p_143011_1_.getBoolean("Desert");
        }
        
        protected StructureComponent getNextComponentNN(final Start p_74891_1_, final List p_74891_2_, final Random p_74891_3_, final int p_74891_4_, final int p_74891_5_) {
            if (this.coordBaseMode != null) {
                switch (StructureVillagePieces.SwitchEnumFacing.field_176064_a[this.coordBaseMode.ordinal()]) {
                    case 1: {
                        return func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                    }
                    case 2: {
                        return func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                    }
                    case 3: {
                        return func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                    case 4: {
                        return func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                }
            }
            return null;
        }
        
        protected StructureComponent getNextComponentPP(final Start p_74894_1_, final List p_74894_2_, final Random p_74894_3_, final int p_74894_4_, final int p_74894_5_) {
            if (this.coordBaseMode != null) {
                switch (StructureVillagePieces.SwitchEnumFacing.field_176064_a[this.coordBaseMode.ordinal()]) {
                    case 1: {
                        return func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                    }
                    case 2: {
                        return func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                    }
                    case 3: {
                        return func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                    case 4: {
                        return func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                }
            }
            return null;
        }
        
        protected int getAverageGroundLevel(final World worldIn, final StructureBoundingBox p_74889_2_) {
            int var3 = 0;
            int var4 = 0;
            for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5) {
                for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6) {
                    final BlockPos var7 = new BlockPos(var6, 64, var5);
                    if (p_74889_2_.func_175898_b(var7)) {
                        var3 += Math.max(worldIn.func_175672_r(var7).getY(), worldIn.provider.getAverageGroundLevel());
                        ++var4;
                    }
                }
            }
            if (var4 == 0) {
                return -1;
            }
            return var3 / var4;
        }
        
        protected static boolean canVillageGoDeeper(final StructureBoundingBox p_74895_0_) {
            return p_74895_0_ != null && p_74895_0_.minY > 10;
        }
        
        protected void spawnVillagers(final World worldIn, final StructureBoundingBox p_74893_2_, final int p_74893_3_, final int p_74893_4_, final int p_74893_5_, final int p_74893_6_) {
            if (this.villagersSpawned < p_74893_6_) {
                for (int var7 = this.villagersSpawned; var7 < p_74893_6_; ++var7) {
                    final int var8 = this.getXWithOffset(p_74893_3_ + var7, p_74893_5_);
                    final int var9 = this.getYWithOffset(p_74893_4_);
                    final int var10 = this.getZWithOffset(p_74893_3_ + var7, p_74893_5_);
                    if (!p_74893_2_.func_175898_b(new BlockPos(var8, var9, var10))) {
                        break;
                    }
                    ++this.villagersSpawned;
                    final EntityVillager var11 = new EntityVillager(worldIn);
                    var11.setLocationAndAngles(var8 + 0.5, var9, var10 + 0.5, 0.0f, 0.0f);
                    var11.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(var11)), null);
                    var11.setProfession(this.func_180779_c(var7, var11.getProfession()));
                    worldIn.spawnEntityInWorld(var11);
                }
            }
        }
        
        protected int func_180779_c(final int p_180779_1_, final int p_180779_2_) {
            return p_180779_2_;
        }
        
        protected IBlockState func_175847_a(final IBlockState p_175847_1_) {
            if (this.field_143014_b) {
                if (p_175847_1_.getBlock() == Blocks.log || p_175847_1_.getBlock() == Blocks.log2) {
                    return Blocks.sandstone.getDefaultState();
                }
                if (p_175847_1_.getBlock() == Blocks.cobblestone) {
                    return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.func_176675_a());
                }
                if (p_175847_1_.getBlock() == Blocks.planks) {
                    return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.func_176675_a());
                }
                if (p_175847_1_.getBlock() == Blocks.oak_stairs) {
                    return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, p_175847_1_.getValue(BlockStairs.FACING));
                }
                if (p_175847_1_.getBlock() == Blocks.stone_stairs) {
                    return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, p_175847_1_.getValue(BlockStairs.FACING));
                }
                if (p_175847_1_.getBlock() == Blocks.gravel) {
                    return Blocks.sandstone.getDefaultState();
                }
            }
            return p_175847_1_;
        }
        
        @Override
        protected void func_175811_a(final World worldIn, final IBlockState p_175811_2_, final int p_175811_3_, final int p_175811_4_, final int p_175811_5_, final StructureBoundingBox p_175811_6_) {
            final IBlockState var7 = this.func_175847_a(p_175811_2_);
            super.func_175811_a(worldIn, var7, p_175811_3_, p_175811_4_, p_175811_5_, p_175811_6_);
        }
        
        @Override
        protected void func_175804_a(final World worldIn, final StructureBoundingBox p_175804_2_, final int p_175804_3_, final int p_175804_4_, final int p_175804_5_, final int p_175804_6_, final int p_175804_7_, final int p_175804_8_, final IBlockState p_175804_9_, final IBlockState p_175804_10_, final boolean p_175804_11_) {
            final IBlockState var12 = this.func_175847_a(p_175804_9_);
            final IBlockState var13 = this.func_175847_a(p_175804_10_);
            super.func_175804_a(worldIn, p_175804_2_, p_175804_3_, p_175804_4_, p_175804_5_, p_175804_6_, p_175804_7_, p_175804_8_, var12, var13, p_175804_11_);
        }
        
        @Override
        protected void func_175808_b(final World worldIn, final IBlockState p_175808_2_, final int p_175808_3_, final int p_175808_4_, final int p_175808_5_, final StructureBoundingBox p_175808_6_) {
            final IBlockState var7 = this.func_175847_a(p_175808_2_);
            super.func_175808_b(worldIn, var7, p_175808_3_, p_175808_4_, p_175808_5_, p_175808_6_);
        }
        
        protected void func_175846_a(final boolean p_175846_1_) {
            this.field_143014_b = p_175846_1_;
        }
    }
    
    public static class Well extends Village
    {
        private static final String __OBFID = "CL_00000533";
        
        public Well() {
        }
        
        public Well(final Start p_i2109_1_, final int p_i2109_2_, final Random p_i2109_3_, final int p_i2109_4_, final int p_i2109_5_) {
            super(p_i2109_1_, p_i2109_2_);
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2109_3_);
            switch (StructureVillagePieces.SwitchEnumFacing.field_176064_a[this.coordBaseMode.ordinal()]) {
                case 1:
                case 2: {
                    this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
                    break;
                }
            }
        }
        
        @Override
        public void buildComponent(final StructureComponent p_74861_1_, final List p_74861_2_, final Random p_74861_3_) {
            func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            func_176069_e((Start)p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 1, 4, 12, 4, Blocks.cobblestone.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 12, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 3, 12, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, 12, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 3, 12, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 13, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 14, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 13, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 14, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 13, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 14, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 13, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 4, 14, 4, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 1, 15, 1, 4, 15, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            for (int var4 = 0; var4 <= 5; ++var4) {
                for (int var5 = 0; var5 <= 5; ++var5) {
                    if (var5 == 0 || var5 == 5 || var4 == 0 || var4 == 5) {
                        this.func_175811_a(worldIn, Blocks.gravel.getDefaultState(), var5, 11, var4, p_74875_3_);
                        this.clearCurrentPositionBlocksUpwards(worldIn, var5, 12, var4, p_74875_3_);
                    }
                }
            }
            return true;
        }
    }
    
    public static class WoodHut extends Village
    {
        private boolean isTallHouse;
        private int tablePosition;
        private static final String __OBFID = "CL_00000524";
        
        public WoodHut() {
        }
        
        public WoodHut(final Start p_i45565_1_, final int p_i45565_2_, final Random p_i45565_3_, final StructureBoundingBox p_i45565_4_, final EnumFacing p_i45565_5_) {
            super(p_i45565_1_, p_i45565_2_);
            this.coordBaseMode = p_i45565_5_;
            this.boundingBox = p_i45565_4_;
            this.isTallHouse = p_i45565_3_.nextBoolean();
            this.tablePosition = p_i45565_3_.nextInt(3);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound p_143012_1_) {
            super.writeStructureToNBT(p_143012_1_);
            p_143012_1_.setInteger("T", this.tablePosition);
            p_143012_1_.setBoolean("C", this.isTallHouse);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound p_143011_1_) {
            super.readStructureFromNBT(p_143011_1_);
            this.tablePosition = p_143011_1_.getInteger("T");
            this.isTallHouse = p_143011_1_.getBoolean("C");
        }
        
        public static WoodHut func_175853_a(final Start p_175853_0_, final List p_175853_1_, final Random p_175853_2_, final int p_175853_3_, final int p_175853_4_, final int p_175853_5_, final EnumFacing p_175853_6_, final int p_175853_7_) {
            final StructureBoundingBox var8 = StructureBoundingBox.func_175897_a(p_175853_3_, p_175853_4_, p_175853_5_, 0, 0, 0, 4, 6, 5, p_175853_6_);
            return (Village.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(p_175853_1_, var8) == null) ? new WoodHut(p_175853_0_, p_175853_7_, p_175853_2_, var8, p_175853_6_) : null;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random p_74875_2_, final StructureBoundingBox p_74875_3_) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(worldIn, p_74875_3_);
                if (this.field_143015_k < 0) {
                    return true;
                }
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 3, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 3, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 0, 1, 2, 0, 3, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
            if (this.isTallHouse) {
                this.func_175804_a(worldIn, p_74875_3_, 1, 4, 1, 2, 4, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            }
            else {
                this.func_175804_a(worldIn, p_74875_3_, 1, 5, 1, 2, 5, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            }
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 1, 4, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 2, 4, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 1, 4, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 2, 4, 4, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 0, 4, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 0, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 0, 4, 3, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 3, 4, 1, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 3, 4, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.log.getDefaultState(), 3, 4, 3, p_74875_3_);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 0, 0, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 0, 3, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 4, 0, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 4, 3, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 3, 1, 1, 3, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175804_a(worldIn, p_74875_3_, 1, 1, 4, 2, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.glass_pane.getDefaultState(), 3, 2, 2, p_74875_3_);
            if (this.tablePosition > 0) {
                this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), this.tablePosition, 1, 3, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), this.tablePosition, 2, 3, p_74875_3_);
            }
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, 1, 0, p_74875_3_);
            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, 2, 0, p_74875_3_);
            this.func_175810_a(worldIn, p_74875_3_, p_74875_2_, 1, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
            if (this.func_175807_a(worldIn, 1, 0, -1, p_74875_3_).getBlock().getMaterial() == Material.air && this.func_175807_a(worldIn, 1, -1, -1, p_74875_3_).getBlock().getMaterial() != Material.air) {
                this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, p_74875_3_);
            }
            for (int var4 = 0; var4 < 5; ++var4) {
                for (int var5 = 0; var5 < 4; ++var5) {
                    this.clearCurrentPositionBlocksUpwards(worldIn, var5, 6, var4, p_74875_3_);
                    this.func_175808_b(worldIn, Blocks.cobblestone.getDefaultState(), var5, -1, var4, p_74875_3_);
                }
            }
            this.spawnVillagers(worldIn, p_74875_3_, 1, 1, 2, 1);
            return true;
        }
    }
}
