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

package org.jscience.ml.sbml.jep.function;

import java.util.Stack;


/**
 * All function classes must implement this interface to ensure that the run()
 * method is implemented.
 */
public interface PostfixMathCommandI {
    /**
     * Returns the number of required parameters, or -1 if any number
     * of parameters is allowed.
     *
     * @return DOCUMENT ME!
     */
    int getNumberOfParameters();

    /**
     * Run the function on the stack. Pops the arguments from the
     * stack, and pushes the result on the top of the stack.
     *
     * @param aStack DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    void run(Stack aStack) throws Exception;

    /**
     * Sets the number of current number of parameters used in the next
     * call of run(). This method is only called when the
     * reqNumberOfParameters is -1.
     *
     * @param n DOCUMENT ME!
     */
    void setCurNumberOfParameters(int n);
}
