/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFactory;
import io.netty.util.internal.StringUtil;

public class ReflectiveChannelFactory<T extends Channel>
implements ChannelFactory<T> {
    private final Class<? extends T> clazz;

    public ReflectiveChannelFactory(Class<? extends T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        this.clazz = clazz;
    }

    @Override
    public T newChannel() {
        try {
            return (T)((Channel)this.clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (Throwable throwable) {
            throw new ChannelException("Unable to create Channel from class " + this.clazz, throwable);
        }
    }

    public String toString() {
        return StringUtil.simpleClassName(this.clazz) + ".class";
    }
}

