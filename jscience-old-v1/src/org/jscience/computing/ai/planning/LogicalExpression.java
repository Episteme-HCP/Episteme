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
 * Each logical expression at compile time is represented as a class
 * derived from this abstract class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public abstract class LogicalExpression extends CompileTimeObject {
    /**
     * The number of times function <code>getClassCnt()</code> is
     * called before.
     */
    private static int classCnt = 0;

    /**
     * The number of variables in this logical expression, used to
     * determine the size of bindings when unifiers are calculated.
     */
    private int varCount;

    /**
     * This function returns a unique integer every time called. This
     * unique integer is used at compile time to make names of classes that
     * implement preconditions at run time unique.
     *
     * @return the unique integer.
     */
    public int getClassCnt() {
        return classCnt++;
    }

    /**
     * This function produces Java code that initializes some data
     * structures that will be needed to create the precondition object that
     * implements this logical expression at run time.
     *
     * @return the produced code as a <code>String</code>.
     */
    public abstract String getInitCode();

    /**
     * This function returns the number of variables in this logical
     * expression.
     *
     * @return the number of variables in this logical expression.
     */
    public int getVarCount() {
        return varCount;
    }

    /**
     * This abstract function is called whenever the number of
     * variables for an object of this class is set. Classes that extend this
     * class should implement this function accordingly in order to update
     * their own data structures where they hold this information.
     *
     * @param varCountIn the number of variables for this logical expression.
     */
    protected abstract void propagateVarCount(int varCountIn);

    /**
     * This function is used to set the number of variables for this
     * logical expression.
     *
     * @param varCountIn the number of variables for this logical expression.
     */
    public void setVarCount(int varCountIn) {
        varCount = varCountIn;

        propagateVarCount(varCountIn);
    }
}
