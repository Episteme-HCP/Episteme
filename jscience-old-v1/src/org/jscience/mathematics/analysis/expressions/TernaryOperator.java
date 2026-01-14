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
 * Abstract class used as superclass for ternary operators.
 *
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see UnaryOperator
 * @see BinaryOperator
 * @see OperatorExpression
 */
public abstract class TernaryOperator extends OperatorExpression {
    /** The second {@link Expression} this TernaryOperator works on. */
    protected Expression secondOperand;

    /** The third {@link Expression} this TernaryOperator works on. */
    protected Expression thirdOperand;

/**
     * Creates a TernaryOperator object working on three given Expression
     * objects.
     *
     * @param e1 An {@link Expression} object.
     * @param e2 Another {@link Expression} object.
     * @param e3 Yet another {@link Expression} object.
     * @since 1.0
     */
    public TernaryOperator(Expression e1, Expression e2, Expression e3) {
        super(e1);
        setSecondOperand(e2);
        setThirdOperand(e3);
    } // TernaryOperator( , )

    /**
     * Assigns the second operand that the TernaryOperator object works
     * on to a given Expression object.
     *
     * @param e2 An Expression object.
     *
     * @see #getSecondOperand
     */
    public void setSecondOperand(Expression e2) {
        secondOperand = e2;
    }

    /**
     * Assigns the third operand that the TernaryOperator object works
     * on to a given Expression object.
     *
     * @param e3 An Expression object.
     *
     * @see #getThirdOperand
     */
    public void setThirdOperand(Expression e3) {
        thirdOperand = e3;
    }

    /**
     * Returns the second operand that the TernaryOperator object works
     * on.
     *
     * @return An Expression object that represents the second operand that the
     *         TernaryOperator object works on.
     *
     * @see #setSecondOperand
     */
    public Expression getSecondOperand() {
        return secondOperand;
    }

    /**
     * Returns the third operand that the TernaryOperator object works
     * on.
     *
     * @return An Expression object that represents the third operand that the
     *         TernaryOperator object works on.
     *
     * @see #setThirdOperand
     */
    public Expression getThirdOperand() {
        return thirdOperand;
    }

    /**
     * Default implementation of method isDifferentiable which assumes
     * that the BinaryOperator object is differentiable. All nondifferentiable
     * TernaryOperators must overrride this implementation.
     *
     * @see Expression#isDifferentiable
     */
    public boolean isDifferentiable() {
        return (firstOperand.isDifferentiable() &&
        secondOperand.isDifferentiable() && thirdOperand.isDifferentiable());
    }

    /**
     * Default implementation of method isExpandable which assumes that
     * the TernaryOperator object is expandable. All nonexpandable
     * TernaryOperators must overrride this implementation.
     *
     * @see Expression#isExpandable
     */
    public boolean isExpandable() {
        return (firstOperand.isExpandable() && secondOperand.isExpandable() &&
        thirdOperand.isExpandable());
    }

    /**
     * Class TernaryOperator provides a default diff method for use by
     * nondifferentiable objects.  All differentiable objects must override
     * this diff method.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return null;
    }

    /**
     * Dummy implementation for classes that are not expandable.
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        java.util.List v1 = getFirstOperand().getElements();
        java.util.List v2 = getSecondOperand().getElements();
        java.util.List v3 = getThirdOperand().getElements();
        java.util.List v = new java.util.ArrayList();
        v.add(this);

        for (int i = 0; i < v1.size(); i++)
            v.add(v1.get(i));

        for (int j = 0; j < v2.size(); j++)
            v.add(v2.get(j));

        for (int k = 0; k < v3.size(); k++)
            v.add(v3.get(k));

        return v;
    }
} // TernaryOperator
