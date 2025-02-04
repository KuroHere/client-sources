/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AggregateFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
abstract class CollectionFuture<V, C>
extends AggregateFuture<V, C> {
    CollectionFuture() {
    }

    static final class ListFuture<V>
    extends CollectionFuture<V, List<V>> {
        ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean bl) {
            this.init(new ListFutureRunningState(this, immutableCollection, bl));
        }

        private final class ListFutureRunningState
        extends CollectionFutureRunningState {
            final ListFuture this$0;

            ListFutureRunningState(ListFuture listFuture, ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean bl) {
                this.this$0 = listFuture;
                super(listFuture, immutableCollection, bl);
            }

            public List<V> combine(List<Optional<V>> list) {
                ArrayList<Object> arrayList = Lists.newArrayListWithCapacity(list.size());
                for (Optional optional : list) {
                    arrayList.add(optional != null ? (Object)optional.orNull() : null);
                }
                return Collections.unmodifiableList(arrayList);
            }

            public Object combine(List list) {
                return this.combine(list);
            }
        }
    }

    abstract class CollectionFutureRunningState
    extends AggregateFuture.RunningState {
        private List<Optional<V>> values;
        final CollectionFuture this$0;

        CollectionFutureRunningState(CollectionFuture collectionFuture, ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean bl) {
            this.this$0 = collectionFuture;
            super(collectionFuture, immutableCollection, bl, true);
            this.values = immutableCollection.isEmpty() ? ImmutableList.of() : Lists.newArrayListWithCapacity(immutableCollection.size());
            for (int i = 0; i < immutableCollection.size(); ++i) {
                this.values.add(null);
            }
        }

        final void collectOneValue(boolean bl, int n, @Nullable V v) {
            List list = this.values;
            if (list != null) {
                list.set(n, Optional.fromNullable(v));
            } else {
                Preconditions.checkState(bl || this.this$0.isCancelled(), "Future was done before all dependencies completed");
            }
        }

        @Override
        final void handleAllCompleted() {
            List list = this.values;
            if (list != null) {
                this.this$0.set(this.combine(list));
            } else {
                Preconditions.checkState(this.this$0.isDone());
            }
        }

        @Override
        void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.values = null;
        }

        abstract C combine(List<Optional<V>> var1);
    }
}

