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
 * Class representing the tangent function of an Expression object.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = new Tan( e1 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.0
 * @see UnaryOperator
 */
public class Tan extends UnaryOperator implements NumericalDifferentiable {
    /**
     * Creates a Tan object that represents the tangent function of an
     * Expression object.
     *
     * @param operand An Expression object.
     */
    public Tan(Expression operand) {
        super(operand);
    } // Tan( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.tan(getFirstOperand().eval());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return new Multiplication(new Addition(new Constant(1),
                new Pow(this, new Constant(2))), getFirstOperand().diff(x));
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

        deriv[i] = (1.0 + Math.pow(numEval(), 2)) * ((NumericalDifferentiable) getFirstOperand()).numDiff(i);
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

        evalValue = Math.tan(((NumericalDifferentiable) getFirstOperand()).numEval());
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

        return new Tan(op1);
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Tan) {
            if (getFirstOperand().equals(((Tan) e).getFirstOperand())) {
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
            return "tan(" + getFirstOperand().toString() + ")";
        } else {
            return "tan";
        }
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.tan(" + getFirstOperand().toJava() + ")";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "new TaylorDivide( new TaylorSin(" + getFirstOperand().toTaylorMap() + "), " +
                "new TaylorCos(" + getFirstOperand().toTaylorMap() + ") )";
    } // toTaylorMap

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();

        if (firstOptimized instanceof Constant) {
            return new Constant(Math.tan(firstOptimized.eval()));
        }

        if (firstOptimized instanceof Minus) {
            return new Minus(new Tan(((Minus) firstOptimized).getFirstOperand()));
        }

        return new Tan(firstOptimized);
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
            return getFirstOperand().isolate(new Atan(f), e);
        }
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<tan> " + getFirstOperand().toXML() + " </tan>";
    } // toXML
} // Tan
