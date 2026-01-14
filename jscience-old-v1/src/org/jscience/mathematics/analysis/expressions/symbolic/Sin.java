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

import org.jscience.JScience;
import org.jscience.mathematics.analysis.expressions.*;

/**
 * Class representing the sine of an Expression object.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = new Sin( e1 );
 * <PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see UnaryOperator
 */
public class Sin extends UnaryOperator implements NumericalDifferentiable {
    /**
     * Creates an object that represent the sine of an Expression
     * object.
     *
     * @param operand An Expression object.
     */
    public Sin(Expression operand) {
        super(operand);
    } // Sin( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.sin(getFirstOperand().eval());
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return new Multiplication(new Cos(getFirstOperand()),
                getFirstOperand().diff(x));
    } // diff

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

        deriv[i] = Math.cos(((NumericalDifferentiable) getFirstOperand()).numEval()) * ((NumericalDifferentiable) getFirstOperand()).numDiff(i);
        valid[i] = true;

        return deriv[i];
    } // numDiff

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double numEval() {
        if (evalValid) {
            return evalValue;
        }

        evalValue = Math.sin(((NumericalDifferentiable) getFirstOperand()).numEval());
        evalValid = true;

        return evalValue;
    } // numEval

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

        return new Sin(op1);
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Sin) {
            if (getFirstOperand().equals(((Sin) e).getFirstOperand())) {
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
            return "sin(" + getFirstOperand().toString() + ")";
        } else {
            return "sin";
        }
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.sin(" + getFirstOperand().toJava() + ")";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "new TaylorSin(" + getFirstOperand().toTaylorMap() + ")";
    } // toTaylorMap

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();

        // sin( const ) -> const
        if (firstOptimized instanceof Constant) {
            return new Constant(Math.sin(firstOptimized.eval()));
        }

        // sin( -a ) -> - sin( a )
        if (firstOptimized instanceof Minus) {
            return new Minus(new Sin(((Minus) firstOptimized).getFirstOperand()));
        }

        // no special optimization was performed
        return new Sin(firstOptimized);
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
            return getFirstOperand().isolate(new Asin(f), e);
        }
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<sin> " + getFirstOperand().toXML() + "</sin>";
    } // toXML
} // Sin
