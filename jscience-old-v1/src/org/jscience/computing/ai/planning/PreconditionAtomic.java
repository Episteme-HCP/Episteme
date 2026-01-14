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
 * can satisfy an atomic logical expression at run time.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class PreconditionAtomic extends Precondition {
    /**
     * The predicate this atomic logical expression represents, after
     * all the bindings are applied.
     */
    private Predicate boundP;

    /**
     * The iterator this object will use to iterate over the atoms
     * and/or axioms that can possibly unify with the predicate this object
     * represents.
     */
    private MyIterator e;

    /**
     * The predicate this atomic logical expression represents, without
     * any subsequent bindings applied to it.
     */
    private Predicate p;

/**
     * To initialize this atomic logical expression.
     *
     * @param pIn     the predicate this atomic logical expression represents.
     * @param unifier the current unifier.
     */
    public PreconditionAtomic(Predicate pIn, Term[] unifier) {
        //-- An atomic logical expression can be potentially satisfied more than
        //-- once, so the default for the 'isFirstCall' flag is false.
        setFirst(false);

        p = pIn.applySubstitution(unifier);
    }

    /**
     * To bind the assignment logical expression to some binding.
     *
     * @param binding DOCUMENT ME!
     */
    public void bind(Term[] binding) {
        boundP = boundP.applySubstitution(binding);
    }

    /**
     * To return the next satisfier for this atomic logical expression.
     *
     * @return DOCUMENT ME!
     */
    protected Term[] nextBindingHelper() {
        return Planner.getState().nextBinding(boundP, e);
    }

    /**
     * To reset this atomic logical expression.
     */
    protected void resetHelper() {
        //-- Reset the iterator.
        e = Planner.getState().iterator(p.getHead());

        //-- Forget all the subsequent bindings.
        boundP = p;
    }
}
