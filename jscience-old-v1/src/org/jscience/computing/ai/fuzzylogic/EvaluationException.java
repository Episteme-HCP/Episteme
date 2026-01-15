package org.jscience.computing.ai.fuzzylogic;

/**
 * <p/>
 * This exception is thrown if an error occurs during evaluation of a rule.
 * </p>
 * <p/>
 * <p/>
 * Most likely a MembershipFunction name is written wrong in the rule!
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class EvaluationException extends Exception {
    /**
     * Creates a new EvaluationException object.
     *
     * @param s DOCUMENT ME!
     */
    public EvaluationException(String s) {
        super(s);
    }

    /**
     * Creates a new EvaluationException object.
     */
    public EvaluationException() {
        super();
    }
}
