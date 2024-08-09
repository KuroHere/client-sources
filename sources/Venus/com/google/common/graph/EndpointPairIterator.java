/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import java.util.Iterator;
import java.util.Set;

abstract class EndpointPairIterator<N>
extends AbstractIterator<EndpointPair<N>> {
    private final Graph<N> graph;
    private final Iterator<N> nodeIterator;
    protected N node = null;
    protected Iterator<N> successorIterator = ImmutableSet.of().iterator();

    static <N> EndpointPairIterator<N> of(Graph<N> graph) {
        return graph.isDirected() ? new Directed(graph, null) : new Undirected(graph, null);
    }

    private EndpointPairIterator(Graph<N> graph) {
        this.graph = graph;
        this.nodeIterator = graph.nodes().iterator();
    }

    protected final boolean advance() {
        Preconditions.checkState(!this.successorIterator.hasNext());
        if (!this.nodeIterator.hasNext()) {
            return true;
        }
        this.node = this.nodeIterator.next();
        this.successorIterator = this.graph.successors(this.node).iterator();
        return false;
    }

    EndpointPairIterator(Graph graph, 1 var2_2) {
        this(graph);
    }

    private static final class Undirected<N>
    extends EndpointPairIterator<N> {
        private Set<N> visitedNodes;

        private Undirected(Graph<N> graph) {
            super(graph, null);
            this.visitedNodes = Sets.newHashSetWithExpectedSize(graph.nodes().size());
        }

        @Override
        protected EndpointPair<N> computeNext() {
            while (true) {
                if (this.successorIterator.hasNext()) {
                    Object e = this.successorIterator.next();
                    if (this.visitedNodes.contains(e)) continue;
                    return EndpointPair.unordered(this.node, e);
                }
                this.visitedNodes.add(this.node);
                if (!this.advance()) break;
            }
            this.visitedNodes = null;
            return (EndpointPair)this.endOfData();
        }

        @Override
        protected Object computeNext() {
            return this.computeNext();
        }

        Undirected(Graph graph, 1 var2_2) {
            this(graph);
        }
    }

    private static final class Directed<N>
    extends EndpointPairIterator<N> {
        private Directed(Graph<N> graph) {
            super(graph, null);
        }

        @Override
        protected EndpointPair<N> computeNext() {
            do {
                if (!this.successorIterator.hasNext()) continue;
                return EndpointPair.ordered(this.node, this.successorIterator.next());
            } while (this.advance());
            return (EndpointPair)this.endOfData();
        }

        @Override
        protected Object computeNext() {
            return this.computeNext();
        }

        Directed(Graph graph, 1 var2_2) {
            this(graph);
        }
    }
}

