package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.BinaryOperator;
import org.jscience.mathematics.analysis.expressions.Constant;
import org.jscience.mathematics.analysis.expressions.Expression;

/**
 * Class representing the minimum of two Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Min( e1, e2 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Min extends BinaryOperator {
    /**
     * Creates a Min object that represents the minimum of two Expression objects.
     *
     * @param op1 First Expression object.
     * @param op2 Second Expression object.
     */
    public Min(Expression op1, Expression op2) {
        super(op1, op2);
    } // Min( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.min(getFirstOperand().eval(), getSecondOperand().eval());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferentiable() {
        return false;
    }

    /**
     * DOCUMENT ME!
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
     * @return DOCUMENT ME!
     */
    public Expression replace(Expression a, Expression b) {
        if (this.equals(a)) {
            return b;
        }

        Expression op1 = getFirstOperand().replace(a, b);
        Expression op2 = getSecondOperand().replace(a, b);

        return new Min(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Min) {
            if (getFirstOperand().equals(((Min) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Min) e).getSecondOperand())) {
                return true;
            }

            if (getFirstOperand().equals(((Min) e).getSecondOperand()) &&
                    getSecondOperand().equals(((Min) e).getFirstOperand())) {
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
        return "min(" + getFirstOperand().toString() + "," +
                getSecondOperand().toString() + ")";
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.min(" + getFirstOperand().toJava() + "," +
                getSecondOperand().toJava() + ")";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();
        Expression secondOptimized = getSecondOperand().optimize();

        // min( a, a ) -> a
        if (firstOptimized.equals(secondOptimized)) {
            return firstOptimized;
        }

        // if
        // min( const1, const2 ) -> minimum value
        if ((firstOptimized instanceof Constant) &&
                (secondOptimized instanceof Constant)) {
            return new Constant(Math.min(firstOptimized.eval(),
                    secondOptimized.eval()));
        }

        // if
        // min( -a, -b ) -> max( a, b )
        if ((firstOptimized instanceof Minus) &&
                (secondOptimized instanceof Minus)) {
            return new Max(((Minus) firstOptimized).getFirstOperand(),
                    ((Minus) secondOptimized).getFirstOperand());
        }

        // if
        // no special optimization was performed
        return new Min(firstOptimized, secondOptimized);
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<min> " + getFirstOperand().toXML() + " " +
                getSecondOperand().toXML() + " </min>";
    }
} // Min
