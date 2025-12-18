package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.algebra.Algebra;

/**
 * Represents a Banach algebra - a complete normed algebra.
 * <p>
 * A Banach algebra is an algebra that is also a Banach space,
 * where the norm satisfies: ||xy|| ≤ ||x|| ||y||
 * </p>
 * 
 * @param <E> the element type
 * @param <F> the scalar type
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public interface BanachAlgebra<E, F> extends Algebra<E, F> {

    /**
     * Returns the norm of the given element.
     */
    double norm(E element);

    /**
     * Returns the identity element (unit) of the algebra.
     */
    E identity();

    /**
     * Returns the inverse of an invertible element.
     */
    E inverse(E element);

    /**
     * Checks if an element is invertible.
     */
    boolean isInvertible(E element);
}
