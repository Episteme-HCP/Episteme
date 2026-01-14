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
 * Each atomic term in a logical expression at compile time is represented
 * as an instance of this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class LogicalExpressionAtomic extends LogicalExpression {
    /** The logical atom. */
    private Predicate logicalAtom;

/**
     * To initialize this atomic logical expression.
     *
     * @param logicalAtomIn the logical atom this class represents.
     */
    public LogicalExpressionAtomic(Predicate logicalAtomIn) {
        logicalAtom = logicalAtomIn;
    }

    /**
     * This class does not need any initialization code, therefore,
     * this function simply returns an empty <code>String</code>.
     *
     * @return DOCUMENT ME!
     */
    public String getInitCode() {
        return "";
    }

    /**
     * To propagate the variable count to the logical atom represented
     * by this object.
     *
     * @param varCount DOCUMENT ME!
     */
    protected void propagateVarCount(int varCount) {
        logicalAtom.setVarCount(varCount);
    }

    /**
     * This function produces the Java code to create a
     * <code>PreconditionAtomic</code> object that represents this atomic
     * logical expression at run time.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "new PreconditionAtomic(" + logicalAtom.toCode() + ", unifier)";
    }
}
