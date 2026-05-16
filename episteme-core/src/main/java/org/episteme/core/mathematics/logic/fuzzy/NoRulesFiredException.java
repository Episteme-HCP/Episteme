package org.episteme.core.mathematics.logic.fuzzy;

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
