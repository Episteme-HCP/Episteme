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
 * This class represents an iterator over all the possible bindings that
 * can satisfy a negation logical expression at run time. Note that in this
 * case there is at most one such binding, and that is the empty binding.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class PreconditionNegation extends Precondition {
    /** The logical expression the negation of which this object represents. */
    private Precondition p;

    /** The array this object will return as its next binding. */
    private Term[] retVal;

/**
     * To initialize this negation logical expression.
     *
     * @param pIn  the logical expression the negation of which this object
     *             represents.
     * @param vars the number of variables this logical expression has. This is
     *             used to return a binding of the appropriate size.
     */
    public PreconditionNegation(Precondition pIn, int vars) {
        //-- A negative logical expression can be satisfied only once, so pretend
        //-- that this negative logical expression is marked ':first'.
        setFirst(true);

        p = pIn;

        //-- A negation never binds any variable to anything. It is merely a check
        //-- if something is true or not. Therefore, only an empty binding should
        //-- be returned by this object.
        retVal = new Term[vars];

        for (int i = 0; i < vars; i++)
            retVal[i] = null;
    }

    /**
     * To bind the negation logical expression to some binding.
     *
     * @param binding DOCUMENT ME!
     */
    public void bind(Term[] binding) {
        p.bind(binding);
    }

    /**
     * To return the next satisfier for this negation logical
     * expression.
     *
     * @return DOCUMENT ME!
     */
    protected Term[] nextBindingHelper() {
        //-- If the logical expression the negation of which this object represents
        //-- can be satisfied, return null, otherwise return an empty binding.
        if (p.nextBinding() != null) {
            return null;
        } else {
            return retVal;
        }
    }

    /**
     * To reset this negation logical expression.
     */
    protected void resetHelper() {
        //-- Reset the logical expression the negation of which this object
        //-- represents.
        p.reset();
    }
}
