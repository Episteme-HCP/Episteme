package org.jscience.computing.ai.fuzzylogic;

/**
 * <p/>
 * This exception is thrown if defuzzification is attempted and no rules fired.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class NoRulesFiredException extends Exception {
    /**
     * Creates a new NoRulesFiredException object.
     *
     * @param s DOCUMENT ME!
     */
    public NoRulesFiredException(String s) {
        super(s);
    }

    /**
     * Creates a new NoRulesFiredException object.
     */
    public NoRulesFiredException() {
        super();
    }
}
