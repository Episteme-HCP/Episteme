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

import org.jscience.util.Named;


/**
 * Abstract class used as superclass for classes representing named data.
 *
 * @author Martin Egholm Nielsen
 * @author Carsten Knudsen
 * @version 1.0
 *
 * @see Expression
 * @see DataExpression
 * @see Variable
 * @see Parameter
 */
public abstract class NamedDataExpression extends DataExpression
    implements NumericalDifferentiable, Named {
    /** The name the named DataExpression object should be characterized by. */
    protected String name;

    /** DOCUMENT ME! */
    private int index = 0;

/**
     * Creates a NamedDataExpression with a given name and value.
     *
     * @param name  The name the NamedDataExpression object should be
     *              characterized by.
     * @param value The value the NamedDataExpression object should have.
     */
    public NamedDataExpression(String name, double value) {
        super(value);
        setName(name);
    } // NamedDataExpression( , )

/**
     * Creates a NamedDataExpression with a given name. The value is set to a
     * default value of 0.
     *
     * @param name The name the NamedDataExpression object should be
     *             characterized by.
     */
    public NamedDataExpression(String name) {
        super(0);
        setName(name);
    } // NamedDataExpression( , )

    /**
     * The setValue method sets the value of the NamedDataExpression.
     *
     * @param x DOCUMENT ME!
     */
    public void setValue(double x) {
        this.value = x;
    } // setValue

    /**
     * Sets the name of the NamedDataExpression object.
     *
     * @param name The name the NamedDataExpression object should be
     *        characterized by.
     *
     * @see DataExpression#setValue
     * @since 1.0
     */
    public void setName(String name) {
        this.name = name;
    } // setName

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    } // getName

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Expression diff(NamedDataExpression x) {
        if (toString().equals(x.toString())) {
            return new Constant(1);
        } else {
            return new Constant(0);
        }
    } // diff

    /**
     * Set the index for this NamedDataExpression.
     *
     * @param i DOCUMENT ME!
     */
    public void setIndex(int i) {
        index = i;
    } // setIndex

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double numDiff(int i) {
        if (index == i) {
            return 1.0;
        } else {
            return 0.0;
        }
    } // numDiff

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double numEval() {
        return value;
    } // numEval

    /**
     * DOCUMENT ME!
     */
    public void resetNumEval() {
    } // resetNumEval

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setNumberOfIndependents(int n) {
    } // setNumberOfIndependents

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void resetNumDiff(int i) {
    } // resetNumDiff

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return name;
    }
} // NamedDataExpression
