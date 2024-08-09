/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.block;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Set;
import java.util.stream.Stream;

public class WoodType {
    private static final Set<WoodType> VALUES = new ObjectArraySet<WoodType>();
    public static final WoodType OAK = WoodType.register(new WoodType("oak"));
    public static final WoodType SPRUCE = WoodType.register(new WoodType("spruce"));
    public static final WoodType BIRCH = WoodType.register(new WoodType("birch"));
    public static final WoodType ACACIA = WoodType.register(new WoodType("acacia"));
    public static final WoodType JUNGLE = WoodType.register(new WoodType("jungle"));
    public static final WoodType DARK_OAK = WoodType.register(new WoodType("dark_oak"));
    public static final WoodType CRIMSON = WoodType.register(new WoodType("crimson"));
    public static final WoodType WARPED = WoodType.register(new WoodType("warped"));
    private final String name;

    protected WoodType(String string) {
        this.name = string;
    }

    private static WoodType register(WoodType woodType) {
        VALUES.add(woodType);
        return woodType;
    }

    public static Stream<WoodType> getValues() {
        return VALUES.stream();
    }

    public String getName() {
        return this.name;
    }
}

