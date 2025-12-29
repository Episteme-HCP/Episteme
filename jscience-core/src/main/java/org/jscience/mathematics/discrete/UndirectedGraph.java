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

import java.util.*;

/**
 * Adjacency list implementation of an undirected graph.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class UndirectedGraph<V> implements Graph<V> {

    private final Map<V, Set<V>> adjacencyList;

    public UndirectedGraph() {
        this.adjacencyList = new HashMap<>();
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
        Set<V> visited = new HashSet<>();

        for (V source : adjacencyList.keySet()) {
            visited.add(source);
            for (V target : adjacencyList.get(source)) {
                if (!visited.contains(target)) {
                    edges.add(new SimpleEdge<>(source, target));
                }
            }
        }
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public boolean addVertex(V vertex) {
        if (adjacencyList.containsKey(vertex)) {
            return false;
        }
        adjacencyList.put(vertex, new HashSet<>());
        return true;
    }

    @Override
    public boolean addEdge(V source, V target) {
        addVertex(source);
        addVertex(target);

        adjacencyList.get(source).add(target);
        adjacencyList.get(target).add(source);
        return true;
    }

    @Override
    public Set<V> neighbors(V vertex) {
        Set<V> neighbors = adjacencyList.get(vertex);
        return neighbors != null ? Collections.unmodifiableSet(neighbors) : Collections.emptySet();
    }

    @Override
    public int degree(V vertex) {
        return neighbors(vertex).size();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    /**
     * Simple edge implementation.
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
            return (source.equals(other.source()) && target.equals(other.target())) ||
                    (source.equals(other.target()) && target.equals(other.source()));
        }

        @Override
        public int hashCode() {
            return source.hashCode() + target.hashCode();
        }
    }
}