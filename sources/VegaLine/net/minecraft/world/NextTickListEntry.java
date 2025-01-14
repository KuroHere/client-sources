/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class NextTickListEntry
implements Comparable<NextTickListEntry> {
    private static long nextTickEntryID;
    private final Block block;
    public final BlockPos position;
    public long scheduledTime;
    public int priority;
    private final long tickEntryID = nextTickEntryID++;

    public NextTickListEntry(BlockPos positionIn, Block blockIn) {
        this.position = positionIn.toImmutable();
        this.block = blockIn;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry nextticklistentry = (NextTickListEntry)p_equals_1_;
        return this.position.equals(nextticklistentry.position) && Block.isEqualTo(this.block, nextticklistentry.block);
    }

    public int hashCode() {
        return this.position.hashCode();
    }

    public NextTickListEntry setScheduledTime(long scheduledTimeIn) {
        this.scheduledTime = scheduledTimeIn;
        return this;
    }

    public void setPriority(int priorityIn) {
        this.priority = priorityIn;
    }

    @Override
    public int compareTo(NextTickListEntry p_compareTo_1_) {
        if (this.scheduledTime < p_compareTo_1_.scheduledTime) {
            return -1;
        }
        if (this.scheduledTime > p_compareTo_1_.scheduledTime) {
            return 1;
        }
        if (this.priority != p_compareTo_1_.priority) {
            return this.priority - p_compareTo_1_.priority;
        }
        if (this.tickEntryID < p_compareTo_1_.tickEntryID) {
            return -1;
        }
        return this.tickEntryID > p_compareTo_1_.tickEntryID ? 1 : 0;
    }

    public String toString() {
        return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
    }

    public Block getBlock() {
        return this.block;
    }
}

