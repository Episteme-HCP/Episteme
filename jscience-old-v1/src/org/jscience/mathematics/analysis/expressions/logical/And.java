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

package org.jscience.mathematics.analysis.expressions.logical;

import org.jscience.mathematics.analysis.expressions.Expression;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public class And extends LogicalBinary {
/**
     * Creates a new And object.
     *
     * @param firstOperand  DOCUMENT ME!
     * @param secondOperand DOCUMENT ME!
     */
    public And(Logical firstOperand, Logical secondOperand) {
        super(firstOperand, secondOperand);
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean truthValue() {
        return getFirstOperand().truthValue() &&
        getSecondOperand().truthValue();
    } // eval

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical replace(Expression a, Expression b) {
        return new And(getFirstOperand().replace(a, b),
            getSecondOperand().replace(a, b));
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "((" + getFirstOperand() + ")&&(" + getSecondOperand() + "))";
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return "((" + getFirstOperand().toJava() + ")&&(" +
        getSecondOperand().toJava() + "))";
    } // toJava

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<and> " + getFirstOperand().toXML() + " " +
        getSecondOperand().toXML() + " </and>";
    } // toXML
} // And
