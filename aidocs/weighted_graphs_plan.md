# Weighted Graphs & Builder Pattern Implementation Plan

**Package**: `org.jscience.mathematics.discrete.graph`  
**Priority**: User Request (Priorities 3)  
**Status**: Planning

---

## Overview

Extend JScience's graph infrastructure to support weighted edges and provide a fluent builder pattern for convenient graph construction.

### Goals

✅ **Weighted Edges** - Support numeric edge weights  
✅ **Weighted Algorithms** - Dijkstra's algorithm, minimum spanning tree  
✅ **Builder Pattern** - Fluent API for graph construction  
✅ **Backward Compatible** - Existing unweighted graphs work unchanged  
✅ **Type Safe** - Generic weight types

---

## Architecture

### Weighted Graph Interfaces

```java
package org.jscience.mathematics.discrete.graph;

/**
 * An edge with an associated weight.
 */
public interface WeightedEdge<V, W> {
    V getSource();
    V getTarget();
    W getWeight();
    
    // For undirected interpretation
    default boolean connects(V u, V v) {
        return (getSource().equals(u) && getTarget().equals(v)) ||
               (getSource().equals(v) && getTarget().equals(u));
    }
}

/**
 * A graph with weighted edges.
 */
public interface WeightedGraph<V, W> extends Graph<V> {
    /**
     * Adds a weighted edge.
     */
    void addEdge(V source, V target, W weight);
    
    /**
     * Gets the weight of an edge.
     */
    W getWeight(V source, V target);
    
    /**
     * Returns all weighted edges.
     */
    Set<WeightedEdge<V, W>> getWeightedEdges();
    
    /**
     * Returns weighted edges from a vertex.
     */
    Set<WeightedEdge<V, W>> getWeightedEdgesFrom(V vertex);
}
```

### Builder Pattern

```java
/**
 * Fluent builder for graphs.
 */
public class GraphBuilder<V> {
    private final Graph<V> graph;
    
    // Factory methods
    public static <V> GraphBuilder<V> directed() {
        return new GraphBuilder<>(new DirectedGraph<>());
    }
    
    public static <V> GraphBuilder<V> undirected() {
        return new GraphBuilder<>(new UndirectedGraph<>());
    }
    
    // Builder methods
    public GraphBuilder<V> addVertex(V vertex) {
        graph.addVertex(vertex);
        return this;
    }
    
    public GraphBuilder<V> addEdge(V source, V target) {
        graph.addEdge(source, target);
        return this;
    }
    
    public GraphBuilder<V> addVertices(V... vertices) {
        for (V v : vertices) addVertex(v);
        return this;
    }
    
    public Graph<V> build() {
        return graph;
    }
}

/**
 * Builder for weighted graphs.
 */
public class WeightedGraphBuilder<V, W> {
    private final WeightedGraph<V, W> graph;
    
    public static < V, W> WeightedGraphBuilder<V, W> directed() {
        return new WeightedGraphBuilder<>(new DirectedWeightedGraph<>());
    }
    
    public WeightedGraphBuilder<V, W> addVertex(V vertex) {
        graph.addVertex(vertex);
        return this;
    }
    
    public WeightedGraphBuilder<V, W> addEdge(V source, V target, W weight) {
        graph.addEdge(source, target, weight);
        return this;
    }
    
    public WeightedGraph<V, W> build() {
        return graph;
    }
}
```

---

## Usage Examples

### Weighted Graph Construction

```java
import org.jscience.mathematics.discrete.graph.*;
import org.jscience.mathematics.number.Real;

// Using builder
WeightedGraph<String, Real> cityGraph = WeightedGraphBuilder
    .<String, Real>directed()
    .addVertex("Paris")
    .addVertex("London")
    .addVertex("Berlin")
    .addEdge("Paris", "London", Real.valueOf(344))  // km
    .addEdge("Paris", "Berlin", Real.valueOf(878))
    .addEdge("London", "Berlin", Real.valueOf(930))
    .build();

// Direct construction
WeightedGraph<String, Real> graph = new DirectedWeightedGraph<>();
graph.addVertex("A");
graph.addVertex("B");
graph.addEdge("A", "B", Real.valueOf(5.0));
```

### Shortest Path (Dijkstra)

```java
// Find shortest path
DijkstraShortestPath<String, Real> dijkstra = 
    new DijkstraShortestPath<>(cityGraph);

List<String> path = dijkstra.findPath("Paris", "Berlin");
Real distance = dijkstra.getDistance("Paris", "Berlin");

System.out.println("Path: " + path);  // [Paris, Berlin]
System.out.println("Distance: " + distance + " km");  // 878 km
```

### Minimum Spanning Tree

```java
UndirectedWeightedGraph<String, Real> graph = ...;

// Kruskal's or Prim's algorithm
MinimumSpanningTree<String, Real> mst = 
    new KruskalMST<>(graph);

Set<WeightedEdge<String, Real>> treeEdges = mst.getEdges();
Real totalWeight = mst.getTotalWeight();
```

### Unweighted Graph Builder

```java
// Simpler syntax for unweighted graphs
Graph<String> socialNetwork = GraphBuilder
    .<String>undirected()
    .addVertices("Alice", "Bob", "Charlie")
    .addEdge("Alice", "Bob")
    .addEdge("Bob", "Charlie")
    .build();
```

---

## Implementation Files

### Core Weighted Graph

```
org.jscience.mathematics.discrete.graph/
├── WeightedEdge.java (interface)
├── WeightedGraph.java (interface)
├── DirectedWeightedGraph.java (implementation)
├── UndirectedWeightedGraph.java (implementation)
└── DefaultWeightedEdge.java (simple implementation)
```

### Builders

```
org.jscience.mathematics.discrete.graph/
├── GraphBuilder.java (unweighted)
└── WeightedGraphBuilder.java (weighted)
```

### Weighted Algorithms

```
org.jscience.mathematics.discrete.graph/
├── DijkstraShortestPath.java
├── BellmanFordShortestPath.java (negative weights)
├── KruskalMST.java (minimum spanning tree)
├── PrimMST.java (minimum spanning tree)
└── FloydWarshallAllPairs.java (all-pairs shortest path)
```

---

## Design Decisions

### 1. Generic Weight Type

Use generics for weight type flexibility:

```java
WeightedGraph<V, W>  // W can be Real, Integer, Double, etc.
```

**Benefits**:
- Type safety
- Works with JScience number types (Real, Integer)
- Works with primitives (Double, Long)
- Custom weight types possible

### 2. Comparability  Requirement

Algorithms need ordered weights:

```java
public class DijkstraShortestPath<V, W extends Comparable<W>> {
    // W must be comparable for min-heap operations
}
```

### 3. Additive Weights

For shortest path, weights must be addable:

```java
public interface AdditiveWeight<W> {
    W add(W other);
    W zero();
}

// Real, Integer, Double all implement this
```

### 4. Builder Immutability

Builders modify mutable graph, then optionally freeze:

```java
public Graph<V> build() {
    return Collections.unmodifiableGraph(graph);  // Optional
}
```

---

## Weighted Graph Algorithms

### Dijkstra's Algorithm (Single-Source Shortest Path)

**Time Complexity**: O((V + E) log V) with binary heap  
**Use Case**: Non-negative weights, single source

```java
public class DijkstraShortestPath<V, W extends Comparable<W>> {
    public Map<V, W> computeDistances(V source);
    public List<V> findPath(V source, V target);
    public W getDistance(V source, V target);
}
```

### Kruskal's Algorithm (Minimum Spanning Tree)

**Time Complexity**: O(E log E)  
**Use Case**: Undirected weighted graphs

```java
public class KruskalMST<V, W extends Comparable<W>> {
    public Set<WeightedEdge<V, W>> getEdges();
    public W getTotalWeight();
}
```

### Bellman-Ford (Negative Weights)

**Time Complexity**: O(VE)  
**Detects**: Negative cycles

```java
public class BellmanFordShortestPath<V, W> {
    public Map<V, W> computeDistances(V source);
    public boolean hasNegativeCycle();
}
```

---

## Integration with Existing Code

### Backward Compatibility

Existing unweighted Graph interface unchanged:

```java
// Old code still works
Graph<String> graph = new DirectedGraph<>();
graph.addEdge("A", "B");  // No weight
```

Weighted graphs extend Graph:

```java
// New weighted graphs are also graphs
WeightedGraph<String, Real> wg = new DirectedWeightedGraph<>();
Graph<String> g = wg;  // Upcast works
```

### Default Unweighted Behavior

```java
public interface WeightedGraph<V, W> extends Graph<V> {
    default void addEdge(V source, V target) {
        // Default weight (e.g., 1.0)
        addEdge(source, target, getDefaultWeight());
    }
    
    W getDefaultWeight();  // Subclass defines
}
```

---

## Testing Strategy

### Unit Tests

- Edge weight retrieval
- Builder fluent API
- Graph construction edge cases
- Algorithm correctness (known graph solutions)

### Integration Tests

- Dijkstra on real-world graphs (cities, networks)
- MST on complete graphs
- Comparison with reference implementations

### Performance Tests

- Large graphs (10k+ vertices)
- Dense vs sparse graphs
- Algorithm time complexity validation

---

## Benefits

✅ **Expressiveness** - Builder pattern reduces boilerplate  
✅ **Safety** - Type-safe weights, compile-time checks  
✅ **Flexibility** - Works with any comparable weight type  
✅ **Standard Algorithms** - Dijkstra, MST out-of-the-box  
✅ **Backward Compatible** - Doesn't break existing code

---

## Next Steps

1. **Implement WeightedEdge/WeightedGraph interfaces**
2. **Create DirectedWeightedGraph implementation**
3. **Implement GraphBuilder for unweighted graphs**
4. **Implement WeightedGraphBuilder**
5. **Implement Dijkstra's algorithm**
6. **Implement Kruskal's MST**
7. **Write comprehensive tests**
8. **Document usage patterns**

---

## Success Criteria

✅ Weighted graphs with generic weight types  
✅ Builder pattern for both weighted and unweighted  
✅ Dijkstra's shortest path algorithm  
✅ Minimum spanning tree (Kruskal or Prim)  
✅ 90%+ test coverage  
✅ Examples in documentation  
✅ Backward compatible with existing Graph interface

