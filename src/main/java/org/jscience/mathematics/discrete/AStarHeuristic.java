package org.jscience.mathematics.discrete;

/**
 * Heuristic function for A* search.
 * <p>
 * Estimates the cost from a vertex to the goal.
 * Must be admissible (never overestimate) for optimality.
 * </p>
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
@FunctionalInterface
public interface AStarHeuristic<V, W> {

    /**
     * Estimates the cost from vertex to goal.
     * 
     * @param vertex current vertex
     * @param goal   goal vertex
     * @return estimated cost to goal
     */
    W estimate(V vertex, V goal);
}
