package org.jscience.mathematics.topology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a metric (distance function).
 * <p>
 * A metric must satisfy:
 * <ul>
 * <li>Non-negativity: d(x,y) ≥ 0</li>
 * <li>Identity: d(x,y) = 0 ⟺ x = y</li>
 * <li>Symmetry: d(x,y) = d(y,x)</li>
 * <li>Triangle inequality: d(x,z) ≤ d(x,y) + d(y,z)</li>
 * </ul>
 * </p>
 * 
 * @param <T> the type of objects being measured
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Metric<T> {

    /**
     * Computes the distance between two objects.
     * 
     * @param a the first object
     * @param b the second object
     * @return the distance between a and b (always non-negative)
     */
    Real distance(T a, T b);
}

