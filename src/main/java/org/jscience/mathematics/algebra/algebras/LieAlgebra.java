package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.vector.Vector;

/**
 * Represents a Lie Algebra.
 * <p>
 * A Lie Algebra is an algebra where the product (Lie bracket) satisfies:
 * <ul>
 * <li>Bilinearity</li>
 * <li>Alternativity: [x, x] = 0</li>
 * <li>Jacobi Identity: [x, [y, z]] + [y, [z, x]] + [z, [x, y]] = 0</li>
 * </ul>
 * </p>
 * 
 * @param <E> the type of elements (vectors)
 * @param <S> the type of scalars
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface LieAlgebra<E extends Vector<S>, S> extends Algebra<E, S> {

    /**
     * Returns the Lie bracket [a, b].
     * <p>
     * This corresponds to the {@link #multiply(Object, Object)} method of the
     * Algebra interface.
     * </p>
     * 
     * @param a the first element
     * @param b the second element
     * @return [a, b]
     */
    default E bracket(E a, E b) {
        return multiply(a, b);
    }

    // Note: Lie Algebras are generally non-associative, so they are not Rings in
    // the strict sense
    // if Ring implies associativity. However, in JScience Ring interface might not
    // strictly enforce associativity
    // or we accept it as a "Non-associative Ring" (Algebra).
    // The Ring interface usually implies (R, +, *) is a ring.
    // For Lie Algebra, * is the bracket.
}
