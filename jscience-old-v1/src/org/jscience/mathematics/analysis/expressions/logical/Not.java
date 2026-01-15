package org.jscience.mathematics.analysis.expressions.logical;

import org.jscience.mathematics.analysis.expressions.Expression;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public class Not extends LogicalUnary {
/**
     * Creates a new Not object.
     *
     * @param operand DOCUMENT ME!
     */
    public Not(Logical operand) {
        super(operand);
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean truthValue() {
        return !getFirstOperand().truthValue();
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical replace(Expression a, Expression b) {
        return new Not(getFirstOperand().replace(a, b));
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "(!" + getFirstOperand() + ")";
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(!" + getFirstOperand().toJava() + ")";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<not> " + getFirstOperand().toXML() + " </not>";
    } // toXML
} // Not
