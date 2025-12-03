package org.jscience.mathematics.topology;

import org.jscience.mathematics.number.Real;

/**
 * Represents a metric space.
 * <p>
 * A metric space is a topological space equipped with a distance function
 * (metric) that satisfies the metric axioms.
 * </p>
 * 
 * @param <T> the type of points in the space
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface MetricSpace<T> extends TopologicalSpace<T> {

    /**
     * Computes the distance between two points.
     * 
     * @param a the first point
     * @param b the second point
     * @return the distance between a and b
     */
    Real distance(T a, T b);
}

