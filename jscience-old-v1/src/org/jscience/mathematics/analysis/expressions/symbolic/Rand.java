package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.BinaryOperator;
import org.jscience.mathematics.analysis.expressions.Expression;

/**
 * Class representing a uniformly distributed random number between two Expression objects.
 * The two Expresssion objects will typically be numbers but may be arbitrary
 * Expressions.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Constant c1 = new Constant( -7 );
 * Constant c2 = new Constant( 11 );
 * Expression c3 = new Rand( c1, c2 ); // uniformly distributed random number between -7 and 11.
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Rand( e1, e2 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Rand extends BinaryOperator {
    private java.util.Random random = new java.util.Random();

    /**
     * Creates a Rand object that represent a random number between the
     * two expressions.
     *
     * @param op1  First Expression object.
     * @param op2  Second Expression object.
     * @param seed a seed for the random number generator.
     */
    public Rand(Expression op1, Expression op2, long seed) {
        super(op1, op2);
        random.setSeed(seed);
    } // Rand( , )

    /**
     * Creates a Rand object that represent a random number between the
     * two expressions.
     *
     * @param op1 First Expression object.
     * @param op2 Second Expression object.
     */
    public Rand(Expression op1, Expression op2) {
        super(op1, op2);
    } // Rand( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        double a = getFirstOperand().eval();
        double b = getSecondOperand().eval();

        return a + ((b - a) * random.nextDouble());
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

        return new Rand(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Rand) {
            if (getFirstOperand().equals(((Rand) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Rand) e).getSecondOperand())) {
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
        return "rand(" + getFirstOperand().toString() + "," +
                getSecondOperand().toString() + ")";
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(" + getFirstOperand().toJava() + "+(" +
                getSecondOperand().toJava() + "-" + getFirstOperand().toJava() +
                ")*random.nexTaylorDouble()" + ")";
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        return this;
    } // optimize()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<expression>" + System.getProperty("line.separator") +
                "<rand>" + System.getProperty("line.separator") +
                getFirstOperand().toXML() + getSecondOperand().toXML() + "</rand>" +
                System.getProperty("line.separator") + "</expression>" +
                System.getProperty("line.separator");
    }
} // Rand
