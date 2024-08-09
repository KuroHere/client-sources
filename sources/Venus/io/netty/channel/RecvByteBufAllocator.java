/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.util.internal.ObjectUtil;

public interface RecvByteBufAllocator {
    public Handle newHandle();

    public static class DelegatingHandle
    implements Handle {
        private final Handle delegate;

        public DelegatingHandle(Handle handle) {
            this.delegate = ObjectUtil.checkNotNull(handle, "delegate");
        }

        protected final Handle delegate() {
            return this.delegate;
        }

        @Override
        public ByteBuf allocate(ByteBufAllocator byteBufAllocator) {
            return this.delegate.allocate(byteBufAllocator);
        }

        @Override
        public int guess() {
            return this.delegate.guess();
        }

        @Override
        public void reset(ChannelConfig channelConfig) {
            this.delegate.reset(channelConfig);
        }

        @Override
        public void incMessagesRead(int n) {
            this.delegate.incMessagesRead(n);
        }

        @Override
        public void lastBytesRead(int n) {
            this.delegate.lastBytesRead(n);
        }

        @Override
        public int lastBytesRead() {
            return this.delegate.lastBytesRead();
        }

        @Override
        public boolean continueReading() {
            return this.delegate.continueReading();
        }

        @Override
        public int attemptedBytesRead() {
            return this.delegate.attemptedBytesRead();
        }

        @Override
        public void attemptedBytesRead(int n) {
            this.delegate.attemptedBytesRead(n);
        }

        @Override
        public void readComplete() {
            this.delegate.readComplete();
        }
    }

    public static interface ExtendedHandle
    extends Handle {
        public boolean continueReading(UncheckedBooleanSupplier var1);
    }

    @Deprecated
    public static interface Handle {
        public ByteBuf allocate(ByteBufAllocator var1);

        public int guess();

        public void reset(ChannelConfig var1);

        public void incMessagesRead(int var1);

        public void lastBytesRead(int var1);

        public int lastBytesRead();

        public void attemptedBytesRead(int var1);

        public int attemptedBytesRead();

        public boolean continueReading();

        public void readComplete();
    }
}

