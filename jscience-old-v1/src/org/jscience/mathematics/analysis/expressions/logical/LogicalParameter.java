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
public class LogicalParameter implements Logical {
    /** DOCUMENT ME! */
    private static final String CR = System.getProperty("line.separator");

    /** DOCUMENT ME! */
    private boolean value;

    /** DOCUMENT ME! */
    private String name;

/**
     * Creates a new LogicalParameter object.
     *
     * @param name  DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public LogicalParameter(String name, boolean value) {
        this.name = name;
        this.value = value;
    } // constructor

/**
     * Creates a new LogicalParameter object.
     *
     * @param name DOCUMENT ME!
     */
    public LogicalParameter(String name) {
        this(name, true);
    } // constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean truthValue() {
        return value;
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
        return this;
    } // replace

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e) {
        return false;
    } // contains

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
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name;
    } // toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toJava() {
        return name;
    } // toJave

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXML() {
        return "<logicalparameter> " + name + " </logicalparameter>";
    } // toXML
} // LogicalParameter
