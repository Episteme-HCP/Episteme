package org.jscience.mathematics.logic.predicate;

/**
 * Represents a term in predicate logic.
 * <p>
 * A term is an object that can be the argument of a predicate.
 * Examples: constants, variables, function applications.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Term {

    /**
     * Returns the name or symbol of this term.
     * 
     * @return the name
     */
    String getName();

    /**
     * Checks if this term is a variable.
     * 
     * @return true if variable, false if constant/function
     */
    boolean isVariable();
}
