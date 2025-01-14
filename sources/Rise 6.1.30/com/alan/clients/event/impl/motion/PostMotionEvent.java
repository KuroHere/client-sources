package com.alan.clients.event.impl.motion;

import com.alan.clients.event.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptPostMotionEvent;

public final class PostMotionEvent implements Event {
    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptPostMotionEvent(this);
    }
}
