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

package org.jscience.mathematics.analysis.expressions;

/**
 * Abstract class used as super class for classes representing both unary
 * and binary operators (and more).
 *
 * @author Martin Egholm Nielsen
 * @version 1.0
 *
 * @see Expression
 * @see UnaryOperator
 * @see BinaryOperator
 * @see DataExpression
 */
public abstract class OperatorExpression implements Expression {
    /**
     * The first (and maybe only) Expression object an operator works
     * on.
     *
     * @since 1.0
     */
    protected Expression firstOperand;

/**
     * Creates a new OperatorExpression object, and assigns the operand to the
     * given Expression.
     *
     * @param e1 The Expression object that the operator should work on.
     */
    public OperatorExpression(Expression e1) {
        setFirstOperand(e1);
    }

    /**
     * Assigns the first (and maybe only) operand that the
     * OperatorExpression object works on to a given Expression object.
     *
     * @param e1 An Expression object.
     *
     * @see #getFirstOperand
     * @since 1.0
     */
    public void setFirstOperand(Expression e1) {
        firstOperand = e1;
    }

    /**
     * Returns the first operand that the OperatorExpression object
     * works on.
     *
     * @return An Expression object that represents the first operand that the
     *         OperatorExpression object works on.
     *
     * @see #setFirstOperand
     * @since 1.0
     */
    public Expression getFirstOperand() {
        return firstOperand;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        java.util.List v = this.getElements();

        for (int i = 0; i < v.size(); i++)
            if (e.equals((Expression) v.get(i))) {
                return true;
            }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression e) {
        return isolate(new Constant(0), e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e) {
        return null;
    }
} // OperaterExpression
