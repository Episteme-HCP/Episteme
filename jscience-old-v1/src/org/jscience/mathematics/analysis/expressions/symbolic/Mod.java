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
