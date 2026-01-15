package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.BinaryOperator;
import org.jscience.mathematics.analysis.expressions.Constant;
import org.jscience.mathematics.analysis.expressions.Expression;

/**
 * Class representing the maximum of two Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Max( e1, e2 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Max extends BinaryOperator {
    /**
     * Creates a Max object that represents the maximum of two Expression objects.
     *
     * @param op1 First Expression object.
     * @param op2 Second Expression object.
     */
    public Max(Expression op1, Expression op2) {
        super(op1, op2);
    } // Max( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.max(getFirstOperand().eval(), getSecondOperand().eval());
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

        return new Max(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Max) {
            if (getFirstOperand().equals(((Max) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Max) e).getSecondOperand())) {
                return true;
            }

            if (getFirstOperand().equals(((Max) e).getSecondOperand()) &&
                    getSecondOperand().equals(((Max) e).getFirstOperand())) {
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
        return "max(" + getFirstOperand().toString() + "," +
                getSecondOperand().toString() + ")";
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.max(" + getFirstOperand().toJava() + "," +
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

        // max( a, a ) -> a
        if (firstOptimized.equals(secondOptimized)) {
            return firstOptimized;
        }

        // if
        // max( const1, const2 )
        if ((firstOptimized instanceof Constant) &&
                (secondOptimized instanceof Constant)) {
            return new Constant(Math.max(firstOptimized.eval(),
                    secondOptimized.eval()));
        }

        // if
        // max( -a, -b ) -> min( a, b )
        if ((firstOptimized instanceof Minus) &&
                (secondOptimized instanceof Minus)) {
            return new Min(((Minus) firstOptimized).getFirstOperand(),
                    ((Minus) secondOptimized).getFirstOperand());
        }

        // if
        // no special optimization was performed
        return new Max(firstOptimized, secondOptimized);
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<max> " + getFirstOperand().toXML() + " " +
                getSecondOperand().toXML() + " </max>";
    }
} // Max
