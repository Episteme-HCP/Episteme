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

package org.jscience.computing.ai.evolutionary.geneticprogramming;

/**
 * Abstraction for the programs in genetic programming.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public abstract class Program implements Cloneable {
    /**
     * Returns a string representation of this program. (Instead of
     * toString - Used by Javolution)
     *
     * @return number of function nodes
     */
    public abstract String toText();

    /**
     * Returns the name of this program.
     *
     * @return name of this program
     */
    public abstract String getName();

    /**
     * Returns evaluation of this program with the given parameter
     *
     * @param fitnessCase fitness case to evaluate
     *
     * @return evaluation result
     */
    public abstract Object eval(Object fitnessCase);

    /**
     * Returns the number of nodes this program has.
     *
     * @return number of children nodes
     */
    public abstract int countNodes();

    /**
     * Returns the number of program nodes this program has. This
     * method traverses all the children nodes recursively until the leaf
     * nodes are reached and counts the number of programs.
     *
     * @return number of program nodes
     */
    public abstract int countProgramNodes();

    /**
     * Returns the number of function nodes this program has. This
     * method traverses all the children nodes recursively until the leaf
     * nodes are reached and counts the number of functions.
     *
     * @return number of function nodes
     */
    public abstract int countFunctionNodes();

    /**
     * Returns a deep copy of this Program instance
     *
     * @return deep copy of this program
     */
    public abstract Object clone();
}
