package org.jscience.computing.ai.fuzzylogic;

/**
 * <p/>
 * This exception is thrown if an error occurs during parsing a fuzzy rule.
 * </p>
 *
 * @author Levent Bayindir
 * @version 0.0.1
 */
public class RuleParsingException extends Exception {
    /**
     * Creates a new RuleParsingException object.
     *
     * @param s DOCUMENT ME!
     */
    public RuleParsingException(String s) {
        super(s);
    }

    /**
     * Creates a new RuleParsingException object.
     */
    public RuleParsingException() {
        super();
    }
}
