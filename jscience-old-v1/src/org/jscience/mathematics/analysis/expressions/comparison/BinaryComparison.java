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

package org.jscience.mathematics.analysis.expressions.comparison;

import org.jscience.mathematics.analysis.expressions.Expression;
import org.jscience.mathematics.analysis.expressions.logical.Logical;


/**
 * DOCUMENT ME!
 *
 * @author Carsten Knudsen
 * @version 1.0
 */
public abstract class BinaryComparison implements Logical {
    /** DOCUMENT ME! */
    protected static final String CR = System.getProperty("line.separator");

    /** DOCUMENT ME! */
    protected Expression firstOperand;

    /** DOCUMENT ME! */
    protected Expression secondOperand;

/**
     * Creates a new BinaryComparison object.
     *
     * @param firstOperand  DOCUMENT ME!
     * @param secondOperand DOCUMENT ME!
     */
    public BinaryComparison(Expression firstOperand, Expression secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression getFirstOperand() {
        return firstOperand;
    } // getFirstOperand

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression getSecondOperand() {
        return secondOperand;
    } // getSecondOperand

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical optimize() {
        return this;
    } // optimize

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        return firstOperand.contains(e) || secondOperand.contains(e);
    } // contains
} // BinaryComparison
