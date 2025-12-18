package org.jscience.mathematics.logic.predicate;

import java.util.List;
import org.jscience.mathematics.logic.propositional.Proposition;

/**
 * Represents a predicate (relation) in predicate logic.
 * <p>
 * A predicate P(t1, ..., tn) evaluates to a truth value.
 * </p>
 * 
 * @param <T> the type of truth value
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Predicate<T> extends Proposition<T> {

    /**
     * Returns the name or symbol of this predicate.
     * 
     * @return the name
     */
    String getName();

    /**
     * Returns the terms (arguments) of this predicate.
     * 
     * @return the list of terms
     */
    List<Term> getTerms();
}
