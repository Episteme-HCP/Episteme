package org.jscience.mathematics.analysis.expressions.comparison;

import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.logical.Logical;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public class LE extends BinaryComparison {
/**
     * Creates a new LE object.
     *
     * @param firstOperand  DOCUMENT ME!
     * @param secondOperand DOCUMENT ME!
     */
    public LE(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean truthValue() {
        return getFirstOperand().eval() <= getSecondOperand().eval();
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
        return new LE(getFirstOperand().replace(a, b),
            getSecondOperand().replace(a, b));
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof LE) {
            if (getFirstOperand().equals(((LE) e).getFirstOperand()) &&
                    getSecondOperand().equals(((LE) e).getSecondOperand())) {
                return true;
            }
        } // if

        return false;
    } // equals

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "le(" + getFirstOperand() + "," + getSecondOperand() + ")";
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "((" + getFirstOperand().toJava() + ")<=(" +
        getSecondOperand().toJava() + "))";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<le> <expression> " + getFirstOperand().toXML() +
        " </expression> <expression> " + getSecondOperand().toXML() +
        " </expression> </le>";
    } // toXML
} // LE
