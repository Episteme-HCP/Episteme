package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;

/**
 * Represents an Ordered Group.
 * <p>
 * An ordered group is a group (G, +) equipped with a total order &le; such that
 * for all a, b, c in G, if a &le; b then a + c &le; b + c and c + a &le; c + b.
 * </p>
 * 
 * @param <E> the type of elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface OrderedGroup<E> extends Group<E>, Comparable<E> {

    /**
     * Checks if the first element is strictly less than the second.
     * 
     * @param a the first element
     * @param b the second element
     * @return true if a < b
     */
    boolean isStrictlyLessThan(E a, E b);

    /**
     * Checks if the first element is less than or equal to the second.
     * 
     * @param a the first element
     * @param b the second element
     * @return true if a <= b
     */
    boolean isLessThanOrEqualTo(E a, E b);
}
