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
 * Abstract class used as superclass for classes representing data. Here,
 * data could refer to constants, variables or parameters.
 *
 * @author Martin Egholm Nielsen
 * @version 1.0
 *
 * @see Constant
 * @see NamedDataExpression
 * @see Expression
 */
public abstract class DataExpression implements Expression {
    /**
     * The numerical value representing the DataExpression object.
     *
     * @since 1.0
     */
    protected double value;

/**
     * Creates a DataExpression object and initializes the value that it
     * represents.
     *
     * @param value The value the DataExpression object should hold.
     * @since 1.0
     */
    public DataExpression(double value) {
        setValue(value);
    } // constructor

    /**
     * Sets the value of the DataExpression object.
     *
     * @param x The value the DataExpression object should have.
     *
     * @see #eval
     * @since 1.0
     */
    public void setValue(double x) {
        value = x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return value;
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferentiable() {
        return true;
    } // isDifferentiable

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable() {
        return true;
    } // isExpandable

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        java.util.List v = new java.util.ArrayList();
        v.add(this);

        return v;
    } // getElements

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        return this.equals(e);
    } // contains

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression e) {
        return isolate(new Constant(0), e);
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        if (this.equals(e)) {
            return f;
        } else {
            return null;
        }
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression factor(Expression e) {
        if (this.equals(e)) {
            return new Constant(1);
        } else {
            return null;
        }
    } // factor
} // DataExpression
