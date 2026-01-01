/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.discrete;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Adjacency list implementation of a directed weighted graph.
 * <p>
 * Stores edges with weights using an adjacency list structure.
 * Default weight is Real.ONE for backward compatibility.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DirectedWeightedGraph<V, W> implements WeightedGraph<V, W> {

    private final Map<V, Map<V, W>> adjacencyList;
    private final W defaultWeight;

    /**
     * Creates a new directed weighted graph with Real.ONE as default weight.
     */
    @SuppressWarnings("unchecked")
    public DirectedWeightedGraph() {
        this((W) Real.ONE);
    }

    /**
     * Creates a new directed weighted graph with specified default weight.
     * 
     * @param defaultWeight the default weight for unweighted edges
     */
    public DirectedWeightedGraph(W defaultWeight) {
        this.adjacencyList = new LinkedHashMap<>();
        this.defaultWeight = defaultWeight;
    }

    @Override
    public Set<V> vertices() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }

    @Override
    public int vertexCount() {
        return adjacencyList.size();
    }

    @Override
    public Set<Edge<V>> edges() {
        Set<Edge<V>> edges = new HashSet<>();
        for (V source : adjacencyList.keySet()) {
            for (V target : adjacencyList.get(source).keySet()) {
                edges.add(new SimpleEdge<>(source, target));
            }
        }
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public boolean addVertex(V vertex) {
        if (adjacencyList.containsKey(vertex)) {
            return false;
        }
        adjacencyList.put(vertex, new LinkedHashMap<>());
        return true;
    }

    @Override
    public boolean addEdge(V source, V target, W weight) {
        Objects.requireNonNull(weight, "Weight cannot be null");
        addVertex(source);
        addVertex(target);

        adjacencyList.get(source).put(target, weight);
        return true;
    }

    @Override
    public W getWeight(V source, V target) {
        Map<V, W> edges = adjacencyList.get(source);
        return edges != null ? edges.get(target) : null;
    }

    @Override
    public Set<WeightedEdge<V, W>> getWeightedEdges() {
        Set<WeightedEdge<V, W>> edges = new HashSet<>();
        for (V source : adjacencyList.keySet()) {
            Map<V, W> targets = adjacencyList.get(source);
            for (Map.Entry<V, W> entry : targets.entrySet()) {
                edges.add(new DefaultWeightedEdge<>(source, entry.getKey(), entry.getValue()));
            }
        }
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public Set<WeightedEdge<V, W>> getWeightedEdgesFrom(V vertex) {
        Map<V, W> targets = adjacencyList.get(vertex);
        if (targets == null) {
            return Collections.emptySet();
        }

        Set<WeightedEdge<V, W>> edges = new HashSet<>();
        for (Map.Entry<V, W> entry : targets.entrySet()) {
            edges.add(new DefaultWeightedEdge<>(vertex, entry.getKey(), entry.getValue()));
        }
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public Set<V> neighbors(V vertex) {
        Map<V, W> targets = adjacencyList.get(vertex);
        return targets != null ? Collections.unmodifiableSet(targets.keySet()) : Collections.emptySet();
    }

    @Override
    public int degree(V vertex) {
        return neighbors(vertex).size();
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public W getDefaultWeight() {
        return defaultWeight;
    }

    /**
     * Simple edge implementation for Graph.Edge interface.
     */
    private static class SimpleEdge<V> implements Edge<V> {
        private final V source;
        private final V target;

        SimpleEdge(V source, V target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public V source() {
            return source;
        }

        @Override
        public V target() {
            return target;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Edge))
                return false;
            Edge<?> other = (Edge<?>) obj;
            return source.equals(other.source()) && target.equals(other.target());
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }
    }
}

