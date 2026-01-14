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
 * Each call term, both at compile time and at run time, is an instance of
 * this class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class TermCall extends Term {
    /**
     * The list that represents the arguments of the call term. Using
     * LISP terminology, this is equal to <code>(CDR ct)</code> where
     * <code>ct</code> is this call term.
     */
    private List args;

    /**
     * The Java implementation of the function that is called in this
     * call term.
     */
    private Calculate calculate;

    /**
     * The name of the function that is called in this call term as a
     * <code>String</code>.
     */
    private String func;

/**
     * To initialize this call term. This constructor is used at compile time.
     *
     * @param argsIn the argument list.
     * @param funcIn the name of the function to be applied.
     */
    public TermCall(List argsIn, String funcIn) {
        args = argsIn;
        func = funcIn;
    }

/**
     * To initialize this call term. This constructor is used at run time.
     *
     * @param argsIn      the argument list.
     * @param calculateIn the Java implementation of the function to be
     *                    applied.
     * @param funcIn      the name of the function to be applied.
     */
    public TermCall(List argsIn, Calculate calculateIn, String funcIn) {
        args = argsIn;
        calculate = calculateIn;
        func = funcIn;
    }

    /**
     * To apply a given binding to the list of arguments of the
     * function call.
     *
     * @param binding DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Term bind(Term[] binding) {
        List boundArgs = args.bindList(binding);

        //-- As soon as all the variables are bound, replace the call term with
        //-- the result of the code call.
        if (boundArgs.isGroundList()) {
            return calculate.call(boundArgs);
        }

        //-- Not all the variables are bound yet, therefore, the code call can not
        //-- be executed.
        return new TermCall(boundArgs, calculate, func);
    }

    /**
     * Whether or not another term is equivalent to the result of this
     * call term.
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Term t) {
        return calculate.call(args).equals(t);
    }

    /**
     * Find a unifier between the result of this call term and another
     * given term.
     *
     * @param t DOCUMENT ME!
     * @param binding DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean findUnifier(Term t, Term[] binding) {
        return calculate.call(args).findUnifier(t, binding);
    }

    /**
     * Check the argument list of this call term for variables.
     *
     * @return DOCUMENT ME!
     */
    public boolean isGround() {
        return args.isGroundList();
    }

    /**
     * This function produces Java code to create this call term.
     *
     * @return DOCUMENT ME!
     */
    public String toCode() {
        return "new TermCall(" + args.toCode() + ", " + func + ", " + "\"" +
        func + "\"" + ")";
    }

    /**
     * This function is used to print this call term.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "(CALL " + func + " " + args.toString() + ")";
    }
}
