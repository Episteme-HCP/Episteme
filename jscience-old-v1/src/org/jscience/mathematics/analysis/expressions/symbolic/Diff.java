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
import org.jscience.mathematics.analysis.expressions.NamedDataExpression;

/**
 * Class representing the derivative of an Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression e1 = ...;
 * NamedDataExpression e2 = ...;
 * Expression e3 = new Diff( e1, e2 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see Expression
 * @see NamedDataExpression
 * @see BinaryOperator
 */
public class Diff extends BinaryOperator {
    /**
     * Creates a Diff object that represents the derivative of an
     * Expression objects with respect to a NamedDataExpression.
     *
     * @param op1 An Expression object.
     * @param op2 NamedDataExpression representing the independent variable.
     */
    public Diff(Expression op1, NamedDataExpression op2) {
        super(op1, op2);
    } // Diff( , )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return getFirstOperand().diff((NamedDataExpression) getSecondOperand())
                .eval();
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferentiable() {
        return getFirstOperand().diff((NamedDataExpression) getSecondOperand())
                .isDifferentiable();
    } // isDifferentiable

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpandable() {
        return getFirstOperand().diff((NamedDataExpression) getSecondOperand())
                .isExpandable();
    } // isExpandable

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

        return new Diff(op1, (NamedDataExpression) op2);
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Diff) {
            if (getFirstOperand().equals(((Diff) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Diff) e).getSecondOperand())) {
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
        return "diff(" + getFirstOperand().toString() + "," +
                getSecondOperand().toString() + ")";
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "(" +
                getFirstOperand().diff((NamedDataExpression) getSecondOperand()).toJava() +
                ")";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toTaylorMap() {
        return getFirstOperand().diff((NamedDataExpression) getSecondOperand())
                .toTaylorMap() + ")";
    } // toTaylorMap

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();
        Expression secondOptimized = getSecondOperand().optimize();

        return new Diff(firstOptimized, (NamedDataExpression) secondOptimized);
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<expression>" + System.getProperty("line.separator") +
                "<diff>" + System.getProperty("line.separator") +
                getFirstOperand().toXML() + getSecondOperand().toXML() + "</diff>" +
                System.getProperty("line.separator") + "</expression>" +
                System.getProperty("line.separator");
    } // toXML
} // Diff
