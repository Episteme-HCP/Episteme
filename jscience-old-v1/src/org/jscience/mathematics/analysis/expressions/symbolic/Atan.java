package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.*;

/**
 * Class representing the inverse tangent function of an Expression object.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = new Atan( e1 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.0
 * @see UnaryOperator
 */
public class Atan extends UnaryOperator implements NumericalDifferentiable {
    /**
     * Creates a Atan object that represents the inverse tangent function of an
     * Expression object.
     *
     * @param operand An Expression object.
     */
    public Atan(Expression operand) {
        super(operand);
    } // Atan( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.atan(getFirstOperand().eval());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return new Division(getFirstOperand().diff(x),
                new Addition(new Constant(1),
                        new Pow(getFirstOperand(), new Constant(2))));
    } // diff

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double numEval() {
        if (evalValid) {
            return evalValue;
        }

        evalValue = Math.atan(((NumericalDifferentiable) getFirstOperand()).numEval());
        evalValid = true;

        return evalValue;
    } // numEval

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double numDiff(int i) {
        if (valid[i]) {
            return deriv[i];
        }

        double f = ((NumericalDifferentiable) getFirstOperand()).numEval();
        deriv[i] = ((NumericalDifferentiable) getFirstOperand()).numDiff(i) / (1.0 +
                (f * f));
        valid[i] = true;

        return deriv[i];
    } // numDiff

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

        return new Atan(op1);
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Atan) {
            if (getFirstOperand().equals(((Atan) e).getFirstOperand())) {
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
        if (Boolean.valueOf(JScience.getProperty("recursivePrint"))
                .booleanValue()) {
            return "atan(" + getFirstOperand().toString() + ")";
        } else {
            return "atan";
        }
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.atan(" + getFirstOperand().toJava() + ")";
    } // toJava

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
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();

        // atan( const ) -> const
        if (firstOptimized instanceof Constant) {
            return new Constant(Math.atan(firstOptimized.eval()));
        }

        // atan( -a ) -> - atan ( a )
        if (firstOptimized instanceof Minus) {
            return new Minus(new Atan(((Minus) firstOptimized).getFirstOperand()));
        }

        // no special optimization was performed
        return new Atan(firstOptimized);
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        if (!contains(e)) {
            return null;
        } else {
            return getFirstOperand().isolate(new Tan(f), e);
        }
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<atan> " + getFirstOperand().toXML() + " </atan>";
    } // toXML
} // Atan
