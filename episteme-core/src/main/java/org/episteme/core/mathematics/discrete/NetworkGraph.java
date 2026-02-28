/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.episteme.core.mathematics.discrete;

import java.util.*;

/**
 * High-level graph implementation optimized for network visualization and social analysis.
 * Implements the core Graph interface.
 */
public final class NetworkGraph<V> implements Graph<V> {
    
    public record NetworkEdge<V>(V source, V target, String type, double weight) implements Graph.Edge<V> {}

    private final Set<V> vertices = new LinkedHashSet<>();
    private final Set<Graph.Edge<V>> edges = new LinkedHashSet<>();
    private final Map<V, Set<V>> adjacency = new HashMap<>();

    @Override
    public Set<V> vertices() { return Collections.unmodifiableSet(vertices); }

    @Override
    public Set<Graph.Edge<V>> edges() { return Collections.unmodifiableSet(edges); }

    @Override
    public boolean addVertex(V vertex) {
        if (vertices.add(vertex)) {
            adjacency.put(vertex, new LinkedHashSet<>());
            return true;
        }
        return false;
    }

    @Override
    public boolean addEdge(V source, V target) {
        return addEdge(source, target, "default", 1.0);
    }

    public boolean addEdge(V source, V target, String type, double weight) {
        addVertex(source);
        addVertex(target);
        NetworkEdge<V> edge = new NetworkEdge<>(source, target, type, weight);
        if (edges.add(edge)) {
            adjacency.get(source).add(target);
            return true;
        }
        return false;
    }

    @Override
    public Set<V> neighbors(V vertex) {
        return adjacency.getOrDefault(vertex, Collections.emptySet());
    }

    @Override
    public int degree(V vertex) {
        return neighbors(vertex).size();
    }

    @Override
    public boolean isDirected() {
        return false; // Default to undirected for most social networks
    }

    @Override
    public int vertexCount() {
        return vertices.size();
    }
}

