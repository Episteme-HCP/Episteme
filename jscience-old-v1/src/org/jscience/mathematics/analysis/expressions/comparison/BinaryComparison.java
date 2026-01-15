package org.jscience.mathematics.analysis.expressions.comparison;

import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.logical.Logical;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public abstract class BinaryComparison implements Logical {
    /** DOCUMENT ME! */
    protected static final String CR = System.getProperty("line.separator");

    /** DOCUMENT ME! */
    protected Expression firstOperand;

    /** DOCUMENT ME! */
    protected Expression secondOperand;

/**
     * Creates a new BinaryComparison object.
     *
     * @param firstOperand  DOCUMENT ME!
     * @param secondOperand DOCUMENT ME!
     */
    public BinaryComparison(Expression firstOperand, Expression secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression getFirstOperand() {
        return firstOperand;
    } // getFirstOperand

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression getSecondOperand() {
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
} // BinaryComparison
