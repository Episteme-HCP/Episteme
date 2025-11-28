package org.jscience.mathematics.discrete;

import java.util.List;
import java.util.Optional;

/**
 * Result of a shortest path computation.
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class ShortestPathResult<V, W> {

    private final V source;
    private final V target;
    private final W distance;
    private final List<V> path;
    private final boolean hasPath;
    private final boolean hasNegativeCycle;

    private ShortestPathResult(V source, V target, W distance, List<V> path,
            boolean hasPath, boolean hasNegativeCycle) {
        this.source = source;
        this.target = target;
        this.distance = distance;
        this.path = path;
        this.hasPath = hasPath;
        this.hasNegativeCycle = hasNegativeCycle;
    }

    /**
     * Creates a successful path result.
     */
    public static <V, W> ShortestPathResult<V, W> withPath(V source, V target,
            W distance, List<V> path) {
        return new ShortestPathResult<>(source, target, distance, path, true, false);
    }

    /**
     * Creates a no-path result (unreachable).
     */
    public static <V, W> ShortestPathResult<V, W> noPath(V source, V target) {
        return new ShortestPathResult<>(source, target, null, null, false, false);
    }

    /**
     * Creates a negative cycle result.
     */
    public static <V, W> ShortestPathResult<V, W> negativeCycle(V source, V target) {
        return new ShortestPathResult<>(source, target, null, null, false, true);
    }

    public V getSource() {
        return source;
    }

    public V getTarget() {
        return target;
    }

    public Optional<W> getDistance() {
        return Optional.ofNullable(distance);
    }

    public Optional<List<V>> getPath() {
        return Optional.ofNullable(path);
    }

    public boolean hasPath() {
        return hasPath;
    }

    public boolean hasNegativeCycle() {
        return hasNegativeCycle;
    }

    @Override
    public String toString() {
        if (hasNegativeCycle) {
            return "ShortestPath[NEGATIVE CYCLE: " + source + " -> " + target + "]";
        }
        if (!hasPath) {
            return "ShortestPath[NO PATH: " + source + " -> " + target + "]";
        }
        return "ShortestPath[" + source + " -> " + target + " = " + distance + ", path=" + path + "]";
    }
}
