/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.world.gen.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureMineshaftStart
extends StructureStart {
    private static final String __OBFID = "CL_00000450";

    public StructureMineshaftStart() {
    }

    public StructureMineshaftStart(World worldIn, Random p_i2039_2_, int p_i2039_3_, int p_i2039_4_) {
        super(p_i2039_3_, p_i2039_4_);
        StructureMineshaftPieces.Room var5 = new StructureMineshaftPieces.Room(0, p_i2039_2_, (p_i2039_3_ << 4) + 2, (p_i2039_4_ << 4) + 2);
        this.components.add(var5);
        var5.buildComponent(var5, this.components, p_i2039_2_);
        this.updateBoundingBox();
        this.markAvailableHeight(worldIn, p_i2039_2_, 10);
    }
}

