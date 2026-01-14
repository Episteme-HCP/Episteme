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
 * can satisfy a call logical expression at run time. Note that in this case
 * there is at most one such binding.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class PreconditionCall extends Precondition {
    /**
     * The term this call logical expression represents, after all the
     * bindings are applied.
     */
    private Term boundT;

    /** The array this object will return as its next binding. */
    private Term[] retVal;

    /**
     * The term this call logical expression represents, without any
     * subsequent bindings applied to it.
     */
    private Term term;

/**
     * To initialize this call logical expression.
     *
     * @param termIn  the term this call logical expression represents.
     * @param unifier the current unifier.
     */
    public PreconditionCall(TermCall termIn, Term[] unifier) {
        //-- A call term can be satisfied only once, so pretend that this call term
        //-- is marked ':first'.
        setFirst(true);

        term = termIn.bind(unifier);

        //-- Initially, this object is not binding any variable, so set all the
        //-- elements of 'retVal' to null.
        retVal = new Term[unifier.length];

        for (int i = 0; i < unifier.length; i++)
            retVal[i] = null;
    }

    /**
     * To bind the call logical expression to some binding.
     *
     * @param binding DOCUMENT ME!
     */
    public void bind(Term[] binding) {
        boundT = boundT.bind(binding);
    }

    /**
     * To return the next satisfier for this call logical expression.
     *
     * @return DOCUMENT ME!
     */
    protected Term[] nextBindingHelper() {
        //-- This behavior is inherited from LISP. A NIL object represents a
        //-- logical falsehood, while everything else represnts true.
        if (boundT.isNil()) {
            return null;
        }

        return retVal;
    }

    /**
     * To reset this call logical expression.
     */
    protected void resetHelper() {
        //-- Undo the bindings.
        boundT = term;
    }
}
