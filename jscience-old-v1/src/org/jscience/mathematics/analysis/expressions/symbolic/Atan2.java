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
import org.jscience.mathematics.analysis.expressions.BinaryOperator;
import org.jscience.mathematics.analysis.expressions.Constant;
import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.NamedDataExpression;

/**
 * Class representing the inverse tangent function of
 * two Expression objects.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Expression x = ...;
 * Expression y = ...;
 * Expression e = new Atan2( y, x );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see BinaryOperator
 */
public class Atan2 extends BinaryOperator {
    /**
     * Creates a Atan2 object that represents the inverse tangent function of two
     * Expression objects.
     *
     * @param operand1 An Expression object.
     * @param operand2 An Expression object.
     */
    public Atan2(Expression operand1, Expression operand2) {
        super(operand1, operand2);
    } // Atan2( )

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double eval() {
        return Math.atan2(getFirstOperand().eval(), getSecondOperand().eval());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        return new Division((new Division(getFirstOperand(), getSecondOperand())).diff(x),
                new Addition(new Constant(1),
                        new Pow(new Division(getFirstOperand(), getSecondOperand()),
                                new Constant(2))));
    } // diff

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

        return new Atan(op1);
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        if (e instanceof Atan2) {
            if (getFirstOperand().equals(((Atan2) e).getFirstOperand()) &&
                    getSecondOperand().equals(((Atan2) e).getSecondOperand())) {
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
            return "atan2(" + getFirstOperand().toString() + "," +
                    getSecondOperand().toString() + ")";
        } else {
            return "atan2";
        }
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "Math.atan2(" + getFirstOperand().toJava() + "," +
                getSecondOperand().toJava() + ")";
    } // toJava

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
     * @return DOCUMENT ME!
     */
    public Expression optimize() {
        Expression firstOptimized = getFirstOperand().optimize();
        Expression secondOptimized = getSecondOperand().optimize();

        // atan2( const, const ) -> const
        if (firstOptimized instanceof Constant &&
                secondOptimized instanceof Constant) {
            return new Constant(Math.atan2(firstOptimized.eval(),
                    secondOptimized.eval()));
        }

        // no special optimization was performed
        return new Atan2(firstOptimized, secondOptimized);
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Expression isolate(Expression f, Expression e) {
        return null;
    } // isolate

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<atan2> " + getFirstOperand().toXML() +
                getSecondOperand().toXML() + " </atan>";
    } // toXML
} // Atan2
