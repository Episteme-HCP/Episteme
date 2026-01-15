package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.Constant;
import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.UnaryOperator;

/**
 * Class representing the signum function of an Expression object.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = new Sign( e1 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see UnaryOperator
 */
public class Sign extends UnaryOperator {
    /**
     * Creates an object that represent the signum of an Expression
     * object.
     *
     * @param operand An Expression object.
     */
    public Sign(Expression operand) {
        super(operand);
    } // Sign( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return (getFirstOperand().eval() > 0.0) ? (1.0)
                : ((getFirstOperand().eval() < 0.0)
                ? (-1.0) : (0.0));
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

        return new Sign(op1);
    }

    /**
     * The signum function is not differentiable so we
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
        if (e instanceof Sign) {
            if (getFirstOperand().equals(((Sign) e).getFirstOperand())) {
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
        if (Boolean.valueOf(JScience.getProperty("recursivePrint"))
                .booleanValue()) {
            return "sign(" + getFirstOperand().toString() + ")";
        } else {
            return "sign";
        }
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "((" + getFirstOperand().toJava() + ">0.0)?(1.0):((" +
                getFirstOperand().toJava() + "<0.0)?(-1.0):(0.0)))";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();

        // sign( const ) -> const
        if (firstOptimized instanceof Constant) {
            return new Constant(((firstOptimized.eval() > 0.0) ? (1.0)
                    : ((firstOptimized.eval() < 0.0)
                    ? (-1.0) : (0.0))));
        }

        // sign( - op ) -> - sign( op )
        if (firstOptimized instanceof Minus) {
            return new Minus(new Sign(((Minus) firstOptimized).getFirstOperand()));
        }

        // no special optimization was performed
        return new Sign(firstOptimized);
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<sign> " + getFirstOperand().toXML() + " </sign>";
    }
} // Sign
