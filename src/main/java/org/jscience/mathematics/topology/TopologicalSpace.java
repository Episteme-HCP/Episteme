package org.jscience.mathematics.topology;

import org.jscience.mathematics.structures.sets.Set;

/**
 * Represents a topological space.
 * <p>
 * A topological space is a set equipped with a topology, which defines
 * which subsets are considered "open".
 * </p>
 * 
 * @param <T> the type of points in the space
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface TopologicalSpace<T> extends Set<T> {

    /**
     * Checks if this space contains the given point.
     * <p>
     * Named containsPoint to avoid erasure conflict with Set.contains.
     * </p>
     * 
     * @param point the point to check
     * @return true if the point is in this space
     */
    boolean containsPoint(T point);

    /**
     * Checks if this set is open in the topology.
     * 
     * @return true if this is an open set
     */
    boolean isOpen();

    /**
     * Checks if this set is closed in the topology.
     * 
     * @return true if this is a closed set
     */
    boolean isClosed();
}
