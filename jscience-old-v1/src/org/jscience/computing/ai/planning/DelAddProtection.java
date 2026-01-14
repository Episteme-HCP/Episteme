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

import java.util.Vector;


/**
 * Each <code>Protection</code> element in the delete/add list of an
 * operator both at compile time and run time is represented as an instance of
 * this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class DelAddProtection extends DelAddElement {
    /** The atom to be protected/unprotected. */
    private Predicate atom;

/**
     * To initialize this <code>Protection</code> delete/add element.
     *
     * @param atomIn the atom to be protected/unprotected.
     */
    public DelAddProtection(Predicate atomIn) {
        atom = atomIn;
    }

    /**
     * To add this atom to the list of protected atoms.
     *
     * @param s DOCUMENT ME!
     * @param binding DOCUMENT ME!
     * @param delAddList DOCUMENT ME!
     */
    public void add(State s, Term[] binding, Vector[] delAddList) {
        //-- Apply the binding (and execute the possible code calls) first.
        Predicate p = atom.applySubstitution(binding);

        //-- Protect the resulting atom.
        s.addProtection(p);

        //-- Add the resulting atom to the list of added protections so that in
        //-- case of a backtrack the protection can be retracted.
        delAddList[3].add(p);
    }

    /**
     * To delete this atom from the list of protected atoms.
     *
     * @param s DOCUMENT ME!
     * @param binding DOCUMENT ME!
     * @param delAddList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean del(State s, Term[] binding, Vector[] delAddList) {
        //-- Apply the binding (and execute the possible code calls) first.
        Predicate p = atom.applySubstitution(binding);

        //-- Try to unprotect the resulting atom.
        if (s.delProtection(p)) {
            //-- If the atom was really unprotected (i.e., it was protected before),
            //-- add it to the list of unprotected atoms so that in case of a
            //-- backtrack it can be reprotected.
            delAddList[2].add(p);
        }

        //-- Trying to unprotect an atom (whether or not it is protected) is never
        //-- going to cause an operator to fail, so always return true.
        return true;
    }

    /**
     * To set the number of variables in this <code>Protection</code>
     * delete/add element.
     *
     * @param varCount DOCUMENT ME!
     */
    public void setVarCount(int varCount) {
        atom.setVarCount(varCount);
    }

    /**
     * This function produces Java code to create this
     * <code>Protection</code> delete/add element.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "new DelAddProtection(" + atom.toCode() + ")";
    }
}
