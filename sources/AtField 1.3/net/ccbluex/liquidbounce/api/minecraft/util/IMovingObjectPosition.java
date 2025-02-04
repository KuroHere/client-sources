/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0001\u0016R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0012\u0010\u0012\u001a\u00020\u0013X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0017"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getBlockPos", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "entityHit", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getEntityHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "hitVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "getHitVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "sideHit", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getSideHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "typeOfHit", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "getTypeOfHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "WMovingObjectType", "AtField"})
public interface IMovingObjectPosition {
    @Nullable
    public IEnumFacing getSideHit();

    @NotNull
    public WMovingObjectType getTypeOfHit();

    @NotNull
    public WVec3 getHitVec();

    @Nullable
    public WBlockPos getBlockPos();

    @Nullable
    public IEntity getEntityHit();

    public static final class WMovingObjectType
    extends Enum {
        public static final /* enum */ WMovingObjectType BLOCK;
        private static final WMovingObjectType[] $VALUES;
        public static final /* enum */ WMovingObjectType MISS;
        public static final /* enum */ WMovingObjectType ENTITY;

        public static WMovingObjectType valueOf(String string) {
            return Enum.valueOf(WMovingObjectType.class, string);
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private WMovingObjectType() {
            void var2_-1;
            void var1_-1;
        }

        public static WMovingObjectType[] values() {
            return (WMovingObjectType[])$VALUES.clone();
        }

        static {
            WMovingObjectType[] wMovingObjectTypeArray = new WMovingObjectType[3];
            WMovingObjectType[] wMovingObjectTypeArray2 = wMovingObjectTypeArray;
            wMovingObjectTypeArray[0] = MISS = new WMovingObjectType("MISS", 0);
            wMovingObjectTypeArray[1] = ENTITY = new WMovingObjectType("ENTITY", 1);
            wMovingObjectTypeArray[2] = BLOCK = new WMovingObjectType("BLOCK", 2);
            $VALUES = wMovingObjectTypeArray;
        }
    }
}

