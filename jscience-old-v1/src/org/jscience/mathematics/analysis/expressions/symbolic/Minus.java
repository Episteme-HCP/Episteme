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
 * Class representing the negated expression of an Expression object.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * Expression e2 = new Minus( e1 );
 * <PRE>
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 * @see UnaryOperator
 */
public class Minus extends UnaryOperator implements NumericalDifferentiable {
    /**
     * Creates a Minus object that represent the negated Expression
     * object.
     *
     * @param operand The Expression object that should be
     *                negated.
     */
    public Minus(Expression operand) {
        super(operand);
    } // Minus( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return -getFirstOperand().eval();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return new Minus(getFirstOperand().diff(x));
    } // diff()

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

        deriv[i] = -((NumericalDifferentiable) getFirstOperand()).numEval();
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

        evalValue = -((NumericalDifferentiable) getFirstOperand()).numEval();
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

        return new Minus(op1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Minus) {
            if (getFirstOperand().equals(((Minus) e).getFirstOperand())) {
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
            return "-" + getFirstOperand().toString();
        } else {
            return "-";
        }
    } // toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "-" + getFirstOperand().toJava();
    } // toJava()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "new TaylorNegate(" + getFirstOperand().toTaylorMap() + ")";
    } // toTaylorMap()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();

        // - const -> const
        if (firstOptimized instanceof Constant) {
            return new Constant(-firstOptimized.eval());
        }

        // - -a -> a
        if (firstOptimized instanceof Minus) {
            return ((Minus) firstOptimized).getFirstOperand();
        }

        // no special optimization performed
        return new Minus(firstOptimized);
    } // optimize()

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
            return isolate(new Minus(f), e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<minus> " + getFirstOperand().toXML() + " </minus>";
    }
} // Minus
