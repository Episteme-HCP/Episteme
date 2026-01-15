package org.jscience.mathematics.analysis.expressions.logical;

import org.jscience.mathematics.analysis.expressions.Expression;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public abstract class LogicalBinary implements Logical {
    /** DOCUMENT ME! */
    protected static final String CR = System.getProperty("line.separator");

    /** DOCUMENT ME! */
    protected Logical firstOperand;

    /** DOCUMENT ME! */
    protected Logical secondOperand;

/**
     * Creates a new LogicalBinary object.
     *
     * @param firstOperand  DOCUMENT ME!
     * @param secondOperand DOCUMENT ME!
     */
    public LogicalBinary(Logical firstOperand, Logical secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical getFirstOperand() {
        return firstOperand;
    } // getFirstOperand

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical getSecondOperand() {
        return secondOperand;
    } // getSecondOperand

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
        return firstOperand.contains(e) || secondOperand.contains(e);
    } // contains
} // LogicalBinary
