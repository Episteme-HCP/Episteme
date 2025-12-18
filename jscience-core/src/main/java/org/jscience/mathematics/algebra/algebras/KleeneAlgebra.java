package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.structures.rings.Semiring;

/**
 * A Kleene Algebra is an idempotent semiring with a closure operator (Kleene
 * star).
 * <p>
 * It is used in theoretical computer science to model regular expressions,
 * finite automata, and program semantics.
 * </p>
 * 
 * <h2>Mathematical Definition</h2>
 * <p>
 * A Kleene algebra (K, +, ·, *, 0, 1) is an idempotent semiring (a + a = a)
 * equipped with a unary operation * satisfying:
 * <ul>
 * <li>1 + a · a* ≤ a*</li>
 * <li>1 + a* · a ≤ a*</li>
 * <li>b + a · x ≤ x ⇒ a* · b ≤ x</li>
 * <li>b + x · a ≤ x ⇒ b · a* ≤ x</li>
 * </ul>
 * where a ≤ b is defined as a + b = b.
 * </p>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>Stephen Cole Kleene, "Representation of Events in Nerve Nets and Finite
 * Automata",
 * in Automata Studies, Princeton University Press, 1956, pp. 3-41</li>
 * <li>Dexter Kozen, "A Completeness Theorem for Kleene Algebras and the Algebra
 * of Regular Events",
 * Information and Computation, Vol. 110, No. 2, 1994, pp. 366-390</li>
 * </ul>
 * 
 * @param <E> the type of elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface KleeneAlgebra<E> extends Semiring<E> {

    /**
     * The Kleene star operator (closure).
     * Represents zero or more repetitions of the element.
     * 
     * @param a the element
     * @return a*
     */
    E star(E a);

    /**
     * The plus operator (a+ = a · a*).
     * Represents one or more repetitions.
     * 
     * @param a the element
     * @return a+
     */
    default E plus(E a) {
        return multiply(a, star(a));
    }
}
