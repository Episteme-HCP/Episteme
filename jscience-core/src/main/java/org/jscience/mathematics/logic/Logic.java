package org.jscience.mathematics.logic;

/**
 * Represents a logical system.
 * <p>
 * A logical system defines the set of truth values and the operations on them.
 * </p>
 * 
 * @param <T> the type of truth values in this logic (e.g., Boolean, Double for
 *            fuzzy)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Logic<T> {

    /**
     * Returns the truth value representing "True" (tautology).
     * 
     * @return the true value
     */
    T trueValue();

    /**
     * Returns the truth value representing "False" (contradiction).
     * 
     * @return the false value
     */
    T falseValue();

    /**
     * Logical AND (Conjunction).
     * 
     * @param a first operand
     * @param b second operand
     * @return a AND b
     */
    T and(T a, T b);

    /**
     * Logical OR (Disjunction).
     * 
     * @param a first operand
     * @param b second operand
     * @return a OR b
     */
    T or(T a, T b);

    /**
     * Logical NOT (Negation).
     * 
     * @param a operand
     * @return NOT a
     */
    T not(T a);

    /**
     * Logical Implication (a -> b).
     * 
     * @param a antecedent
     * @param b consequent
     * @return a IMPLIES b
     */
    default T implies(T a, T b) {
        return or(not(a), b);
    }

    /**
     * Logical Equivalence (a <-> b).
     * 
     * @param a first operand
     * @param b second operand
     * @return a IFF b
     */
    default T equivalent(T a, T b) {
        return and(implies(a, b), implies(b, a));
    }
}
