package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;

/**
 * Represents a Finite Group.
 * <p>
 * A group with a finite number of elements (finite order).
 * </p>
 * 
 * @param <E> the type of elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FiniteGroup<E> extends Group<E> {

    /**
     * Returns the order of the group (number of elements).
     * 
     * @return the size of the group
     */
    int order();

    /**
     * Returns the elements of the group as an iterable or array.
     * 
     * @return the elements
     */
    Iterable<E> elements();
}
