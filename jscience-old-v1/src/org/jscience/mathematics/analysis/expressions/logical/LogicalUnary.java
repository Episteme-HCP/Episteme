package org.jscience.mathematics.analysis.expressions.logical;

import org.jscience.mathematics.analysis.expressions.Expression;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public abstract class LogicalUnary implements Logical {
    /** DOCUMENT ME! */
    protected static final String CR = System.getProperty("line.separator");

    /** DOCUMENT ME! */
    protected Logical operand;

/**
     * Creates a new LogicalUnary object.
     *
     * @param operand DOCUMENT ME!
     */
    public LogicalUnary(Logical operand) {
        this.operand = operand;
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical getFirstOperand() {
        return operand;
    } // getFirstOperand

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical optimize() {
        return this;
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        return operand.contains(e);
    } // contains
} // LogicalUnary
