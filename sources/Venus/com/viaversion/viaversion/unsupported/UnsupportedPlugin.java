/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.unsupported;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class UnsupportedPlugin
implements UnsupportedSoftware {
    private final String name;
    private final List<String> identifiers;
    private final String reason;

    public UnsupportedPlugin(String string, List<String> list, String string2) {
        Preconditions.checkNotNull(string);
        Preconditions.checkNotNull(string2);
        Preconditions.checkArgument(!list.isEmpty());
        this.name = string;
        this.identifiers = Collections.unmodifiableList(list);
        this.reason = string2;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public final @Nullable String match() {
        for (String string : this.identifiers) {
            if (!Via.getPlatform().hasPlugin(string)) continue;
            return string;
        }
        return null;
    }

    public static final class Reason {
        public static final String SECURE_CHAT_BYPASS = "Instead of doing the obvious (or nothing at all), these kinds of plugins completely break chat message handling, usually then also breaking other plugins.";
    }

    public static final class Builder {
        private final List<String> identifiers = new ArrayList<String>();
        private String name;
        private String reason;

        public Builder name(String string) {
            this.name = string;
            return this;
        }

        public Builder reason(String string) {
            this.reason = string;
            return this;
        }

        public Builder addPlugin(String string) {
            this.identifiers.add(string);
            return this;
        }

        public UnsupportedPlugin build() {
            return new UnsupportedPlugin(this.name, this.identifiers, this.reason);
        }
    }
}

