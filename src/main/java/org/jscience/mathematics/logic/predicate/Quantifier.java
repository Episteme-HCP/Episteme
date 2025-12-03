package org.jscience.mathematics.logic.predicate;

import org.jscience.mathematics.logic.Proposition;
import org.jscience.mathematics.logic.Term;

/**
 * Represents a quantifier in predicate logic.
 * <p>
 * Examples: Universal (∀), Existential (∃).
 * </p>
 * 
 * @param <T> the type of truth value
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Quantifier<T> extends Proposition<T> {

    /**
     * Returns the variable being quantified.
     * 
     * @return the variable term
     */
    Term getVariable();

    /**
     * Returns the formula (body) bound by the quantifier.
     * 
     * @return the body proposition
     */
    Proposition<T> getBody();

    /**
     * Returns the type of quantifier.
     * 
     * @return "FORALL" or "EXISTS"
     */
    String getType();
}
