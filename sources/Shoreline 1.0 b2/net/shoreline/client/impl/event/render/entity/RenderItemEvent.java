package net.shoreline.client.impl.event.render.entity;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;
import net.minecraft.entity.ItemEntity;

/**
 *
 * @author linus
 * @since 1.0
 */
@Cancelable
public class RenderItemEvent extends Event
{
    private final ItemEntity itemEntity;

    public RenderItemEvent(ItemEntity itemEntity)
    {
        this.itemEntity = itemEntity;
    }

    public ItemEntity getItem()
    {
        return itemEntity;
    }
}
