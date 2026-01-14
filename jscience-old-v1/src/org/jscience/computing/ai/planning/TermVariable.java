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
 * Each variable symbol both at compile time and at run time, is an
 * instance of this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class TermVariable extends Term {
    /**
     * To represent the variable symbols that we know occur in the
     * domain description, so that there will be no duplicate copies of those
     * symbols. In other words, all variable symbols that represent the same
     * thing in different places point to the corresponding element in this
     * array at run time.
     */
    private static TermVariable[] variables;

    /**
     * Variable symbols are mapped to integers at compile time, and
     * these integers are used thereafter to represent the variable symbols.
     */
    private int index;

/**
     * To initialize this variable symbol.
     *
     * @param indexIn the integer associated with this variable symbol.
     */
    public TermVariable(int indexIn) {
        index = indexIn;
    }

    /**
     * Since this term is a variable symbol, binding it basically means
     * finding out whether or not this variable is already mapped to something
     * in the input, and if so, returning the value this variable is mapped
     * to.
     *
     * @param binding DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Term bind(Term[] binding) {
        //-- If this variable is already mapped to something in the input:
        if (binding[index] != null) {
            return binding[index];
        }

        //-- If this variable is not mapped to anything in the input, just return
        //-- the variable symbol itself.
        return this;
    }

    /**
     * This function returns <code>false</code>.
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Term t) {
        return false;
    }

    /**
     * Find a unifier between this variable symbol and another given
     * term.
     *
     * @param t DOCUMENT ME!
     * @param binding DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean findUnifier(Term t, Term[] binding) {
        //-- If 't' is a variable symbol, skip it.
        if (t instanceof TermVariable) {
            return true;
        }

        //-- If the variable has not already been mapped to something, map it:
        if (binding[index] == null) {
            binding[index] = t;

            return true;
        }

        //-- If the variable has already been mapped to something, check if it is
        //-- unified with the same thing again.
        return t.equals(binding[index]);
    }

    /**
     * To get the index for this variable symbol.
     *
     * @return the integer associated with this variable symobl.
     */
    public int getIndex() {
        return index;
    }

    /**
     * To return the correponding existing variable symbol.
     *
     * @param index the index of the variable symbol to be returned.
     *
     * @return the corresponding existing variable symbol.
     */
    public static TermVariable getVariable(int index) {
        return variables[index];
    }

    /**
     * To initialize an array of variable symbols that we know occur in
     * the domain description, so that there will be no duplicate copies of
     * those symbols. In other words, all variable symbols that represent the
     * same thing in different places point to the corresponding element in
     * this array at run time.
     *
     * @param size the number of existing variable symbols.
     */
    public static void initialize(int size) {
        variables = new TermVariable[size];

        for (int i = 0; i < size; i++)
            variables[i] = new TermVariable(i);
    }

    /**
     * This function always returns <code>false</code> because a
     * variable symbol is never ground by definition.
     *
     * @return DOCUMENT ME!
     */
    public boolean isGround() {
        return false;
    }

    /**
     * This function produces Java code to create this variable symbol
     * as a term.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "TermVariable.getVariable(" + index + ")";
    }

    /**
     * This function is used to print this variable symbol.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "VAR" + index;
    }
}
