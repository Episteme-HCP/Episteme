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
 * Abstraction for the terminals in genetic programming.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public abstract class Terminal extends Program {
    /**
     * Returns the number of nodes this terminal has.
     *
     * @return 1 (each terminal has one node)
     */
    public int countNodes() {
        return 1;
    }

    /**
     * Returns the number of program nodes this terminal has.
     *
     * @return 1 (each terminal has one program)
     */
    public int countProgramNodes() {
        if (GeneticProgram.isProgram(this)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the number of function nodes this terminal has.
     *
     * @return 0 (this node is a terminal not a function)
     */
    public int countFunctionNodes() {
        if (GeneticProgram.isFunction(this)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns a deep copy of this Terminal instance
     *
     * @return deep copy of this terminal
     */
    public Object clone() {
        Terminal temp = null;

        try {
            temp = (Terminal) getClass().newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }

        return temp;
    }
}
