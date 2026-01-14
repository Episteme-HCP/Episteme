/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.analysis.expressions.symbolic;

import org.jscience.mathematics.analysis.expressions.BinaryOperator;
import org.jscience.mathematics.analysis.expressions.Expression;

/**
 * Class representing a Gaussian distributed random number
 * with specified mean and standard deviation.
 * The two Expresssion objects will typically be numbers but may be arbitrary
 * Expressions.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Constant c1 = new Constant( -7 );
 * Constant c2 = new Constant( 11 );
 * Expression c3 = new Gauss( c1, c2 ); // gaussian distributed random number between -7 and 11.
 * Expression e1 = ...;
 * Expression e2 = ...;
 * Expression e3 = new Gauss( e1, e2 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see BinaryOperator
 */
public class Gauss extends BinaryOperator {
    private java.util.Random random = new java.util.Random();

    /**
     * Creates a Gauss object that represent a random number between the
     * two expressions.
     *
     * @param op1  First Expression object.
     * @param op2  Second Expression object.
     * @param seed a seed for the random number generator.
     */
    public Gauss(Expression op1, Expression op2, long seed) {
        super(op1, op2);
        random.setSeed(seed);
    } // Gauss( , )

    /**
     * Creates a Gauss object that represent a random number between the
     * two expressions.
     *
     * @param op1 First Expression object.
     * @param op2 Second Expression object.
     */
    public Gauss(Expression op1, Expression op2) {
        super(op1, op2);
    } // Gauss( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        double mean = getFirstOperand().eval();
        double sigma = getSecondOperand().eval();

        return mean + (sigma * random.nextGaussian());
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

        return new Gauss(op1, op2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Gauss) {
            if (getFirstOperand().equals(((Gauss) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Gauss) e).getSecondOperand())) {
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
        return "gauss(" + getFirstOperand().toString() + "," +
                getSecondOperand().toString() + ")";
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(" + getFirstOperand().toJava() + "+(" +
                getSecondOperand().toJava() + ")*" + "random.nextGaussian()" + ")";
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
                "<gauss>" + System.getProperty("line.separator") +
                getFirstOperand().toXML() + getSecondOperand().toXML() + "</gauss>" +
                System.getProperty("line.separator") + "</expression>" +
                System.getProperty("line.separator");
    }
} // Gauss
