package org.jscience.computing.ai.planning;

/**
 * Each call term in a logical expression at compile time is represented as
 * an instance of this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class LogicalExpressionCall extends LogicalExpression {
    /** The call term this object represents. */
    private TermCall term;

/**
     * To initialize this call logical expression.
     *
     * @param termIn the call term this object represents.
     */
    public LogicalExpressionCall(TermCall termIn) {
        term = termIn;
    }

    /**
     * This class does not need any initialization code, therefore,
     * this function simply returns an empty <code>String</code>.
     *
     * @return DOCUMENT ME!
     */
    public String getInitCode() {
        return "";
    }

    /**
     * This class does not need to propagate the variable count,
     * therefore, this function does nothing.
     *
     * @param varCount DOCUMENT ME!
     */
    protected void propagateVarCount(int varCount) {
    }

    /**
     * This function produces the Java code to create a
     * <code>PreconditionCall</code> object that represents this call logical
     * expression at run time.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "new PreconditionCall(" + term.toCode() + ", unifier)";
    }
}
