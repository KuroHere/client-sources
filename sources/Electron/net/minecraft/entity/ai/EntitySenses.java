package net.minecraft.entity.ai;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitySenses {
    EntityLiving entityObj;
    List<Entity> seenEntities = Lists.newArrayList();
    List<Entity> unseenEntities = Lists.newArrayList();

    public EntitySenses(EntityLiving entityObjIn) {
        this.entityObj = entityObjIn;
    }

    /**
     * Clears canSeeCachePositive and canSeeCacheNegative.
     */
    public void clearSensingCache() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }

    /**
     * Checks, whether 'our' entity can see the entity given as argument (true) or not (false), caching the result.
     */
    public boolean canSee(Entity entityIn) {
        if (this.seenEntities.contains(entityIn)) {
            return true;
        } else if (this.unseenEntities.contains(entityIn)) {
            return false;
        } else {
            boolean flag = this.entityObj.canEntityBeSeen(entityIn);

            if (flag) {
                this.seenEntities.add(entityIn);
            } else {
                this.unseenEntities.add(entityIn);
            }

            return flag;
        }
    }
}
