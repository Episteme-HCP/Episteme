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

package org.jscience.computing.ai.planning;

/**
 * Each logical precondition at compile time is represented as an instance
 * of this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class LogicalPrecondition extends CompileTimeObject {
    /**
     * Whether or not this logical precondition is marked
     * <code>:first</code>.
     */
    private boolean first;

    /**
     * The name of the function used in a <code>:sort-by</code> logical
     * precondition.
     */
    private String func;

    /** The logical expression this class represents. */
    private LogicalExpression le;

/**
     * To initialize this logical precondition.
     *
     * @param leIn    the logical expression this class represents.
     * @param firstIn whether or not this logical precondition is marked
     *                <code>:first</code>.
     */
    public LogicalPrecondition(LogicalExpression leIn, boolean firstIn) {
        le = leIn;
        first = firstIn;

        //-- This is not a :sort-by logical precondition, so the function name is
        //-- set null.
        func = null;
    }

/**
     * To initialize this logical precondition.
     *
     * @param leIn   the logical expression this class represents.
     * @param funcIn the name of the function used in a <code>:sort-by</code>
     *               logical precondition.
     */
    public LogicalPrecondition(LogicalExpression leIn, String funcIn) {
        le = leIn;
        func = funcIn;

        //-- A :sort-by logical precondition can not be marked :first.
        first = false;
    }

    /**
     * To check whether or not this logical precondition is marked
     * <code>:first</code>.
     *
     * @return <code>true</code> if this logical precondition is marked
     *         <code>:first</code>, <code>false</code> otherwise.
     */
    public boolean getFirst() {
        return first;
    }

    /**
     * This function produces Java code that implements the class any
     * object of which can be used at run time to represent this logical
     * precondition.
     *
     * @return DOCUMENT ME!
     */
    public String getInitCode() {
        return le.getInitCode();
    }

    /**
     * This function is used to set the number of variables for this
     * logical precondition.
     *
     * @param varCount the number of variables for this logical precondition.
     */
    public void setVarCount(int varCount) {
        le.setVarCount(varCount);
    }

    /**
     * This function produces the Java code to create an object that
     * represents this logical precondition at run time.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "(" + le.toCode() + ").setComparator(" + func + ")";
    }
}
