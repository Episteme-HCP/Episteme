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
