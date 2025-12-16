package org.jscience.mathematics.algebra.algebras;

/**
 * Represents a C*-algebra (C-star algebra).
 * <p>
 * A C*-algebra is a Banach algebra with an involution * satisfying:
 * ||x*x|| = ||x||²
 * </p>
 * <p>
 * C*-algebras are fundamental in quantum mechanics and functional analysis.
 * </p>
 * 
 * @param <E> the element type
 * @param <F> the scalar type
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public interface CStarAlgebra<E, F> extends BanachAlgebra<E, F> {

    /**
     * Returns the involution (adjoint) of an element.
     * For matrices, this is the conjugate transpose.
     */
    E involution(E element);

    /**
     * Checks if an element is self-adjoint (Hermitian): x* = x
     */
    default boolean isSelfAdjoint(E element) {
        return element.equals(involution(element));
    }

    /**
     * Checks if an element is normal: x*x = xx*
     */
    default boolean isNormal(E element) {
        E xstar = involution(element);
        return multiply(xstar, element).equals(multiply(element, xstar));
    }

    /**
     * Checks if an element is unitary: x*x = xx* = 1
     */
    default boolean isUnitary(E element) {
        E xstar = involution(element);
        E id = identity();
        return multiply(xstar, element).equals(id) && multiply(element, xstar).equals(id);
    }

    /**
     * Checks if an element is positive: x = y*y for some y
     */
    boolean isPositive(E element);

    /**
     * Returns the spectral radius of an element.
     */
    double spectralRadius(E element);
}
