/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.yaml.snakeyaml.events;

import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.Event;

public final class DocumentStartEvent
extends Event {
    private final boolean explicit;
    private final DumperOptions.Version version;
    private final Map<String, String> tags;

    public DocumentStartEvent(Mark mark, Mark mark2, boolean bl, DumperOptions.Version version, Map<String, String> map) {
        super(mark, mark2);
        this.explicit = bl;
        this.version = version;
        this.tags = map;
    }

    public boolean getExplicit() {
        return this.explicit;
    }

    public DumperOptions.Version getVersion() {
        return this.version;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

    @Override
    public Event.ID getEventId() {
        return Event.ID.DocumentStart;
    }
}

