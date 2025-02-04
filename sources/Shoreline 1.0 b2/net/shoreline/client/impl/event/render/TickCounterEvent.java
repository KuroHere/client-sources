package net.shoreline.client.impl.event.render;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

/**
 *
 *
 *
 * @author linus
 * @since 1.0
 */
@Cancelable
public class TickCounterEvent extends Event
{
    //
    private float ticks;

    /**
     *
     *
     * @param ticks
     */
    public void setTicks(float ticks)
    {
        this.ticks = ticks;
    }

    /**
     *
     * @return
     */
    public float getTicks()
    {
        return ticks;
    }
}
