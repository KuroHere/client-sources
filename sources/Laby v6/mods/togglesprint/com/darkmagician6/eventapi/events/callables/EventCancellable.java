package mods.togglesprint.com.darkmagician6.eventapi.events.callables;

import mods.togglesprint.com.darkmagician6.eventapi.events.Cancellable;
import mods.togglesprint.com.darkmagician6.eventapi.events.Event;

/**
 * Abstract example implementation of the Cancellable interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    protected EventCancellable() {
    }

    /**
     * @see mods.togglesprint.com.darkmagician6.eventapi.events.Cancellable.isCancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @see mods.togglesprint.com.darkmagician6.eventapi.events.Cancellable.setCancelled
     */
    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
