package org.jscience.computing.ai.planning;

/**
 * Each <code>ForAll</code> logical expression at compile time is
 * represented as an instance of this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class LogicalExpressionForAll extends LogicalExpression {
    /** The consequence of this <code>ForAll</code> logical expression. */
    private LogicalExpression consequence;

    /** The premise of this <code>ForAll</code> logical expression. */
    private LogicalExpression premise;

/**
     * To initialize this <code>ForAll</code> logical expression.
     *
     * @param premiseIn     the premise of this <code>ForAll</code> logical
     *                      expression.
     * @param consequenceIn the consequence of this <code>ForAll</code> logical
     *                      expression.
     */
    public LogicalExpressionForAll(LogicalExpression premiseIn,
        LogicalExpression consequenceIn) {
        premise = premiseIn;
        consequence = consequenceIn;
    }

    /**
     * This function produces Java code that implements the classes any
     * object of which can be used at run time to represent the premise and
     * the consequence of the <code>ForAll</code> logical expression this
     * object is representing.
     *
     * @return DOCUMENT ME!
     */
    public String getInitCode() {
        return premise.getInitCode() + consequence.getInitCode();
    }

    /**
     * To propagate the variable count to the <code>ForAll</code>
     * logical expression represented by this object.
     *
     * @param varCount DOCUMENT ME!
     */
    protected void propagateVarCount(int varCount) {
        premise.setVarCount(varCount);
        consequence.setVarCount(varCount);
    }

    /**
     * This function produces the Java code to create a
     * <code>PreconditionForAll</code> object that represents this
     * <code>ForAll</code> logical expression at run time.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "new PreconditionForAll(" + premise.toCode() + ", " +
        consequence.toCode() + ", " + getVarCount() + ")";
    }
}
