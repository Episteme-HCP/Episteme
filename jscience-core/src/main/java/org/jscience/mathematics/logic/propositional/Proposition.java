package org.jscience.mathematics.logic.propositional;

/**
 * Represents a logical proposition.
 * <p>
 * A proposition is a statement that has a truth value.
 * </p>
 * 
 * @param <T> the type of truth value (e.g., Boolean, Real)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Proposition<T> {

    /**
     * Evaluates this proposition to determine its truth value.
     * 
     * @return the truth value
     */
    TruthValue<T> evaluate();
}
