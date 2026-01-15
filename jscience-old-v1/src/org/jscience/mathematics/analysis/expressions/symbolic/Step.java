package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.Constant;
import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.TernaryOperator;


/**
 * Class representing the step function.
 *
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see TernaryOperator
 */
public class Step extends TernaryOperator {
/**
     * Creates an object that represents the step function. If the first
     * expression evaluates to a value larger than or equal to the second
     * expression, the step value is returned. If not zero is returned.
     *
     * @param time      Usually the normal time
     * @param steptime  The time from which the step function should be
     *                  stepvalue rather than zero
     * @param stepvalue The value to which the step function steps at time
     *                  steptime
     */
    public Step(Expression time, Expression steptime, Expression stepvalue) {
        super(time, steptime, stepvalue);
    } // Step( )

/**
     * Creates an object that represent the step function.
     *
     * @param steptime  DOCUMENT ME!
     * @param stepvalue DOCUMENT ME!
     */
    public Step(Expression steptime, Expression stepvalue) {
        this(new Constant(0), steptime, stepvalue);
    } // Step( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return (getFirstOperand().eval() >= getSecondOperand().eval())
        ? (getThirdOperand().eval()) : (0.0);
    } // eval

    /**
     * The step function is not differentiable so we override the
     * inherited isDifferentiable method.
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferentiable() {
        return false;
    }

    /**
     * The step function is not expandable so we override the inherited
     * isExpandable method.
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression replace(Expression a, Expression b) {
        if (this.equals(a)) {
            return b;
        }

        Expression op1 = getFirstOperand().replace(a, b);
        Expression op2 = getSecondOperand().replace(a, b);
        Expression op3 = getThirdOperand().replace(a, b);

        return new Step(op1, op2, op3);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Step) {
            if (getFirstOperand().equals(((Step) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Step) e).getSecondOperand()) &&
                    getThirdOperand().equals(((Step) e).getThirdOperand())) {
                return true;
            }
        } // if

        return false;
    } // equals()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "step(" + getFirstOperand().toString() + "," +
        getSecondOperand().toString() + "," + getThirdOperand() + ")";
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "((" + getFirstOperand().toJava() + ">=" +
        getSecondOperand().toJava() + ")?(" + getThirdOperand().toJava() +
        "):(0))";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();
        Expression secondOptimized = getSecondOperand().optimize();
        Expression thirdOptimized = getThirdOperand().optimize();

        return new Step(firstOptimized, secondOptimized, thirdOptimized);
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<expression>" + System.getProperty("line.separator") +
        "<step>" + System.getProperty("line.separator") +
        getFirstOperand().toXML() + getSecondOperand().toXML() +
        getThirdOperand().toXML() + "</step>" +
        System.getProperty("line.separator") + "</expression>" +
        System.getProperty("line.separator");
    }
} // Step
