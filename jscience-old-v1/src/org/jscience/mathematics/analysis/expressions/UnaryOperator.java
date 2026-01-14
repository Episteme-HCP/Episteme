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
 * Abstract class used as superclass for all unary operators.
 *
 * @author Martin Egholm Nielsen
 * @version 1.0
 *
 * @see BinaryOperator
 * @see OperatorExpression
 */
public abstract class UnaryOperator extends OperatorExpression {
    /** DOCUMENT ME! */
    protected boolean evalValid = false;

    /** DOCUMENT ME! */
    protected double evalValue = Double.NaN;

    /** DOCUMENT ME! */
    protected boolean[] valid = null;

    /** DOCUMENT ME! */
    protected double[] deriv = null;

/**
     * Creates a UnaryOperator object working on a given Expression object.
     *
     * @param e1 An Expression object.
     * @since 1.0
     */
    public UnaryOperator(Expression e1) {
        super(e1);
    }

    /**
     * Default implementation of method isDifferentiable which assumes
     * that the UnaryOperator object is differentiable. All nondifferentiable
     * UnaryOperators must overrride this implementation.
     *
     * @see Expression#isDifferentiable
     */
    public boolean isDifferentiable() {
        return firstOperand.isDifferentiable();
    }

    /**
     * Default implementation of method isExpandable which assumes that
     * the UnaryOperator object is expandable. All nonexpandable
     * UnaryOperators must overrride this implementation.
     *
     * @see Expression#isExpandable
     */
    public boolean isExpandable() {
        return firstOperand.isExpandable();
    }

    /**
     * Class UnaryOperator provides a default diff method for use by
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
     * 
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        java.util.List v = getFirstOperand().getElements();
        v.add(this);

        return v;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetNumEval() {
        if (evalValid) {
            evalValid = false;
            ((NumericalDifferentiable) getFirstOperand()).resetNumEval();
        } // if
    } // resetNumEval

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void resetNumDiff(int i) {
        if (valid[i]) {
            valid[i] = false;
            ((NumericalDifferentiable) getFirstOperand()).resetNumDiff(i);
        } // if
    } // resetNumDiff

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setNumberOfIndependents(int n) {
        if ((valid == null) || ((n + 1) != valid.length)) {
            valid = new boolean[n + 1];
            deriv = new double[n + 1];

            for (int i = 1; i <= n; i++) {
                valid[i] = false;
                deriv[i] = 0.0;
            } // for

            ((NumericalDifferentiable) getFirstOperand()).setNumberOfIndependents(n);
        } // if
    } // setNumberOfIndependents

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setIndex(int i) {
    } // setIndex
} // UnaryOperator
