/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterators
 */
package optifine;

import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;

public class NextTickHashSet
extends TreeSet {
    private LongHashMap longHashMap = new LongHashMap();
    private int minX = Integer.MIN_VALUE;
    private int minZ = Integer.MIN_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxZ = Integer.MIN_VALUE;
    private static final int UNDEFINED = Integer.MIN_VALUE;

    public NextTickHashSet(Set oldSet) {
        for (Object obj : oldSet) {
            this.add(obj);
        }
    }

    @Override
    public boolean contains(Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry entry = (NextTickListEntry)obj;
        Set set = this.getSubSet(entry, false);
        return set == null ? false : set.contains(entry);
    }

    @Override
    public boolean add(Object obj) {
        boolean addedParent;
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry entry = (NextTickListEntry)obj;
        if (entry == null) {
            return false;
        }
        Set set = this.getSubSet(entry, true);
        boolean added = set.add(entry);
        if (added != (addedParent = super.add(obj))) {
            throw new IllegalStateException("Added: " + added + ", addedParent: " + addedParent);
        }
        return addedParent;
    }

    @Override
    public boolean remove(Object obj) {
        boolean removedParent;
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry entry = (NextTickListEntry)obj;
        Set set = this.getSubSet(entry, false);
        if (set == null) {
            return false;
        }
        boolean removed = set.remove(entry);
        if (removed != (removedParent = super.remove(entry))) {
            throw new IllegalStateException("Added: " + removed + ", addedParent: " + removedParent);
        }
        return removedParent;
    }

    private Set getSubSet(NextTickListEntry entry, boolean autoCreate) {
        if (entry == null) {
            return null;
        }
        BlockPos pos = entry.field_180282_a;
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;
        return this.getSubSet(cx, cz, autoCreate);
    }

    private Set getSubSet(int cx, int cz, boolean autoCreate) {
        long key = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
        HashSet set = (HashSet)this.longHashMap.getValueByKey(key);
        if (set == null && autoCreate) {
            set = new HashSet();
            this.longHashMap.add(key, set);
        }
        return set;
    }

    @Override
    public Iterator iterator() {
        if (this.minX == Integer.MIN_VALUE) {
            return super.iterator();
        }
        if (this.size() <= 0) {
            return Iterators.emptyIterator();
        }
        int cMinX = this.minX >> 4;
        int cMinZ = this.minZ >> 4;
        int cMaxX = this.maxX >> 4;
        int cMaxZ = this.maxZ >> 4;
        ArrayList listIterators = new ArrayList();
        int x = cMinX;
        while (x <= cMaxX) {
            int z = cMinZ;
            while (z <= cMaxZ) {
                Set set = this.getSubSet(x, z, false);
                if (set != null) {
                    listIterators.add(set.iterator());
                }
                ++z;
            }
            ++x;
        }
        if (listIterators.size() <= 0) {
            return Iterators.emptyIterator();
        }
        if (listIterators.size() == 1) {
            return (Iterator)listIterators.get(0);
        }
        return Iterators.concat(listIterators.iterator());
    }

    public void setIteratorLimits(int minX, int minZ, int maxX, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public void clearIteratorLimits() {
        this.minX = Integer.MIN_VALUE;
        this.minZ = Integer.MIN_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
    }
}

