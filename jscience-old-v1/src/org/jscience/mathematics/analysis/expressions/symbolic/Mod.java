package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.TernaryOperator;


/**
 * Class representing the modulus function.
 *
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see TernaryOperator
 */
public class Mod extends TernaryOperator {
/**
     * Creates an object that represents the modulus function.
     * The modulus should return normalized value <CODE>y</CODE>satisfying
     * <CODE>a<=y<b<CODE>.
     *
     * @param x The Expression to be normalized.
     * @param a An Expression representing the lower limit.
     * @param b An Expression representing the upper limit.
     */
    public Mod(Expression x, Expression a, Expression b) {
        super(x, a, b);
    } // Mod( )

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return eval(getFirstOperand().eval(), getFirstOperand().eval(),
            getThirdOperand().eval());
    } // eval (instance method)

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

        return new Mod(op1, op2, op3);
    }

    /**
     * The static method can be used to normalize numbers. The current
     * implementation is horribly inefficient.
     *
     * @param x DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double eval(double x, double a, double b) {
        while (x < a)
            x += (b - a);

        while (x >= b)
            x -= (b - a);

        return x;
    } // eval (static method)

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
     * The mod function is not expandable so we override the inherited
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
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Mod) {
            if (getFirstOperand().equals(((Mod) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Mod) e).getSecondOperand()) &&
                    getThirdOperand().equals(((Mod) e).getThirdOperand())) {
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
        return "mod(" + getFirstOperand().toString() + "," +
        getSecondOperand().toString() + "," + getThirdOperand() + ")";
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "org.jscience.mathematics.analysis.expressions.symbolic.Mod.eval(" +
        getFirstOperand().toJava() + "," + getSecondOperand().toJava() + "," +
        getThirdOperand().toJava() + ")";
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

        return new Mod(firstOptimized, secondOptimized, thirdOptimized);
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<mod>" + System.getProperty("line.separator") +
        getFirstOperand().toXML() + getSecondOperand().toXML() +
        getThirdOperand().toXML() + "</mod>" +
        System.getProperty("line.separator");
    }
} // Mod
