/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.DefaultErrorHandler;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Integers;

public abstract class AbstractAppender
extends AbstractFilterable
implements Appender {
    private final String name;
    private final boolean ignoreExceptions;
    private final Layout<? extends Serializable> layout;
    private ErrorHandler handler = new DefaultErrorHandler(this);

    protected AbstractAppender(String string, Filter filter, Layout<? extends Serializable> layout) {
        this(string, filter, layout, true);
    }

    protected AbstractAppender(String string, Filter filter, Layout<? extends Serializable> layout, boolean bl) {
        super(filter);
        this.name = Objects.requireNonNull(string, "name");
        this.layout = layout;
        this.ignoreExceptions = bl;
    }

    public static int parseInt(String string, int n) {
        try {
            return Integers.parseInt(string, n);
        } catch (NumberFormatException numberFormatException) {
            LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", (Object)string, (Object)n, (Object)numberFormatException);
            return n;
        }
    }

    public void error(String string) {
        this.handler.error(string);
    }

    public void error(String string, LogEvent logEvent, Throwable throwable) {
        this.handler.error(string, logEvent, throwable);
    }

    public void error(String string, Throwable throwable) {
        this.handler.error(string, throwable);
    }

    @Override
    public ErrorHandler getHandler() {
        return this.handler;
    }

    @Override
    public Layout<? extends Serializable> getLayout() {
        return this.layout;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean ignoreExceptions() {
        return this.ignoreExceptions;
    }

    @Override
    public void setHandler(ErrorHandler errorHandler) {
        if (errorHandler == null) {
            LOGGER.error("The handler cannot be set to null");
        }
        if (this.isStarted()) {
            LOGGER.error("The handler cannot be changed once the appender is started");
            return;
        }
        this.handler = errorHandler;
    }

    public String toString() {
        return this.name;
    }

    public static abstract class Builder<B extends Builder<B>>
    extends AbstractFilterable.Builder<B> {
        @PluginBuilderAttribute
        private boolean ignoreExceptions = true;
        @PluginElement(value="Layout")
        private Layout<? extends Serializable> layout;
        @PluginBuilderAttribute
        @Required(message="No appender name provided")
        private String name;
        @PluginConfiguration
        private Configuration configuration;

        public String getName() {
            return this.name;
        }

        public boolean isIgnoreExceptions() {
            return this.ignoreExceptions;
        }

        public Layout<? extends Serializable> getLayout() {
            return this.layout;
        }

        public B withName(String string) {
            this.name = string;
            return (B)((Builder)this.asBuilder());
        }

        public B withIgnoreExceptions(boolean bl) {
            this.ignoreExceptions = bl;
            return (B)((Builder)this.asBuilder());
        }

        public B withLayout(Layout<? extends Serializable> layout) {
            this.layout = layout;
            return (B)((Builder)this.asBuilder());
        }

        public Layout<? extends Serializable> getOrCreateLayout() {
            if (this.layout == null) {
                return PatternLayout.createDefaultLayout();
            }
            return this.layout;
        }

        public Layout<? extends Serializable> getOrCreateLayout(Charset charset) {
            if (this.layout == null) {
                return PatternLayout.newBuilder().withCharset(charset).build();
            }
            return this.layout;
        }

        @Deprecated
        public B withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return (B)((Builder)this.asBuilder());
        }

        public B setConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return (B)((Builder)this.asBuilder());
        }

        public Configuration getConfiguration() {
            return this.configuration;
        }
    }
}

