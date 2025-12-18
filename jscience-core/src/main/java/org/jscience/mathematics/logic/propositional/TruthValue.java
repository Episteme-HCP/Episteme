package org.jscience.mathematics.logic.propositional;

/**
 * Represents a truth value in a logical system.
 * <p>
 * In classical logic, this is just True or False.
 * In fuzzy logic, this is a value between 0 and 1.
 * </p>
 * 
 * @param <T> the underlying type of the truth value
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TruthValue<T> extends Comparable<TruthValue<T>> {

    /**
     * Returns the underlying value.
     * 
     * @return the value
     */
    T getValue();

    /**
     * Returns the negation of this truth value.
     * 
     * @return NOT this
     */
    TruthValue<T> not();

    /**
     * Returns the conjunction of this truth value and another.
     * 
     * @param other the other truth value
     * @return this AND other
     */
    TruthValue<T> and(TruthValue<T> other);

    /**
     * Returns the disjunction of this truth value and another.
     * 
     * @param other the other truth value
     * @return this OR other
     */
    TruthValue<T> or(TruthValue<T> other);

    /**
     * Checks if this value represents absolute truth (tautology).
     * 
     * @return true if this is the "True" value
     */
    boolean isTrue();

    /**
     * Checks if this value represents absolute falsity (contradiction).
     * 
     * @return true if this is the "False" value
     */
    boolean isFalse();
}
