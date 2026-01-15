package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.Constant;
import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.UnaryOperator;

/**
 * Class representing the absolute value of an Expression object.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = new Abs( e1 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see UnaryOperator
 */
public class Abs extends UnaryOperator {
    /**
     * Creates an object that represent the sine of an Expression
     * object.
     *
     * @param operand An Expression object.
     */
    public Abs(Expression operand) {
        super(operand);
    } // Abs( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.abs(getFirstOperand().eval());
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

        return new Abs(op1);
    }

    /**
     * The absolute value function is not differentiable so we
     * override the inherited isDifferentiable method.
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
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Abs) {
            if (getFirstOperand().equals(((Abs) e).getFirstOperand())) {
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
        return "abs(" + getFirstOperand().toString() + ")";
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.abs(" + getFirstOperand().toJava() + ")";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();

        // abs( const ) -> const
        if (firstOptimized instanceof Constant) {
            return new Constant(Math.abs(firstOptimized.eval()));
        }

        // abs( -a ) -> abs( a )
        if (firstOptimized instanceof Minus) {
            return new Abs(((Minus) firstOptimized).getFirstOperand());
        }

        // no special optimization was performed
        return new Abs(firstOptimized);
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<abs> " + getFirstOperand().toXML() + " </abs>";
    }
} // Abs
