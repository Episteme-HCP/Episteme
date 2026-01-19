package org.jscience.mathematics.discrete;

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
