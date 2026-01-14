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
 * Each term, both at compile time and at run time, is an instance of a
 * class derived from this abstract class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public abstract class Term extends CompileTimeObject {
    /**
     * To apply a given binding to this term.
     *
     * @param binding an array of terms, indexed by the integers associated
     *        with variable symbols.
     *
     * @return the result of binding.
     */
    public abstract Term bind(Term[] binding);

    /**
     * Whether or not another term is equivalent to this term.
     *
     * @param t the term being compared to this term.
     *
     * @return <code>true</code> if the two terms are equal, <code>false</code>
     *         otherwise.
     */
    public abstract boolean equals(Term t);

    /**
     * To find a unifier that binds this term to another given term.
     *
     * @param t the term with which we are finding a unifier.
     * @param binding the unifier to be returned.
     *
     * @return <code>false</code> if the two terms can not be unified,
     *         <code>true</code> otherwise.
     */
    public abstract boolean findUnifier(Term t, Term[] binding);

    /**
     * Is this term ground (i.e., has no variables)?
     *
     * @return <code>true</code> if this term is ground, <code>false</code>
     *         otherwise.
     */
    public abstract boolean isGround();

    /**
     * Is this a <code>NIL</code> term?
     *
     * @return <code>true</code> if this is a <code>NIL</code> term,
     *         <code>false</code> otherwise.
     */
    public boolean isNil() {
        return (this instanceof TermList) &&
        (((TermList) this).getList() == null);
    }

    /**
     * This function is used to merge two bindings. Bindings are
     * represented as an array of terms, each element showing what its
     * corresponding variable should be mapped to.
     *
     * @param inp1 the first binding, also the result of merging.
     * @param inp2 the second binding.
     */
    public static void merge(Term[] inp1, Term[] inp2) {
        for (int i = 0; i < inp1.length; i++)

            //-- If some variable is not bound by the first binding,
            if (inp1[i] == null) {
                //-- Use the second binding to bind it.
                inp1[i] = inp2[i];
            }
    }

    /**
     * This function is used to merge an arbitrary number of bindings.
     *
     * @param inp the bindings to be merged. This is a two dimensional array,
     *        the first dimension indexes the different bindings, and the
     *        second dimension indexes variables within each binding.
     * @param howMany how many of available bindings we want to merge. Bindings
     *        indexed 0 to <code>howMany</code>-1 are merged.
     *
     * @return the result of merging.
     */
    public static Term[] merge(Term[][] inp, int howMany) {
        //-- Initialize the resulting array.
        Term[] retVal = new Term[inp[0].length];

        //-- For each variable,
        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = null;

            //-- Try all the bindings,
            for (int j = 0; j < howMany; j++) {
                //-- If in jth binding the ith variable is bound,
                if (inp[j][i] != null) {
                    //-- Update the resulting array.
                    retVal[i] = inp[j][i];

                    break;
                }
            }
        }

        return retVal;
    }

    /**
     * This function is used to print the value of this term.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * This function is used to produce a printable <code>String</code>
     * showing the value of this term.
     *
     * @return the printable <code>String</code> that shows the value of this
     *         term.
     */
    public abstract String toString();
}
