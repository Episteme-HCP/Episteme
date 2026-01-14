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
 * Each numerical term both at compile time and at run time, is an instance
 * of this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class TermNumber extends Term {
    /** The value of the numerical term. */
    private double number;

/**
     * To initialize this numerical term.
     *
     * @param numberIn the value this numerical term is set to have.
     */
    public TermNumber(double numberIn) {
        number = numberIn;
    }

    /**
     * Since this term is a numerical term, binding will not change it,
     * therefore, simply this numerical term itself is returned.
     *
     * @param binding DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Term bind(Term[] binding) {
        return this;
    }

    /**
     * Whether or not another term is equivalent to this numerical
     * term.
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Term t) {
        //-- 't' is not a numerical term.
        if (!(t instanceof TermNumber)) {
            return false;
        }

        //-- Check if 't' has the same numerical value.
        return (number == ((TermNumber) t).number);
    }

    /**
     * Find a unifier between this numerical term and another given
     * term. Since this term is a number, this boils down to whether or not
     * the other given term is equal to this one.
     *
     * @param t DOCUMENT ME!
     * @param binding DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean findUnifier(Term t, Term[] binding) {
        return ((t instanceof TermVariable) || equals(t));
    }

    /**
     * To get the value of this numerical term.
     *
     * @return the value of this numerical term.
     */
    public double getNumber() {
        return number;
    }

    /**
     * This function always returns <code>true</code> because a
     * numerical term is always ground by definition.
     *
     * @return DOCUMENT ME!
     */
    public boolean isGround() {
        return true;
    }

    /**
     * This function produces Java code to create this numerical.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "new TermNumber(" + number + ")";
    }

    /**
     * This function returns the <code>String</code> representation of
     * the value of this numerical term.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return String.valueOf(number);
    }
}
