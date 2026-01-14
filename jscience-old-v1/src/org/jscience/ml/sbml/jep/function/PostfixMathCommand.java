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

import org.jscience.ml.sbml.FunctionDefinition;

import java.util.Stack;

/**
 * Function classes extend this class. It is an implementation of the PostfixMathCommandI interface.
 * <p/>
 * <p/>
 * It includes a numberOfParameters member, that is checked when parsing the expression. This member should be initialized to an appropriate
 * value for all classes extending this class. If an arbitrary number of parameters should be allowed, initialize this member to -1.
 * </p>
 */

public class PostfixMathCommand extends FunctionDefinition implements PostfixMathCommandI {

    /**
     * Number of parameters to be used for the next run() invocation. Applies only if the required umber of parameters is variable
     * (numberOfParameters = -1).
     */

    protected int curNumberOfParameters;

    /**
     * Number of parameters a the function requires. Initialize this value to -1 if any number of parameters should be allowed.
     */

    protected int numberOfParameters;

    /**
     * Creates a new PostfixMathCommand class.
     */

    public PostfixMathCommand(int numParameters) {
        super();
        this.numberOfParameters = numParameters;
        curNumberOfParameters = numParameters;
    }

    /**
     * Return the required number of parameters.
     */

    public int getNumberOfParameters() {
        return numberOfParameters;
    }

    /**
     * Throws an exception because this method should never be called under normal circumstances. Each function should use it's own run() method
     * for evaluating the function. This includes popping off the parameters from the stack, and pushing the result back on the stack.
     */

    public void run(Stack s) throws Exception {
        throw new Exception("run() method of PostfixMathCommand called");
    }

    /**
     * Sets the number of current number of parameters used in the next call of run(). This method is only called when the reqNumberOfParameters
     * is -1.
     */

    public void setCurNumberOfParameters(int n) {
        curNumberOfParameters = n;
    }

    /**
     * Convenience method for use with the parser to set the parameters if they have changed.
     */

    public void setNumberOfParameters(int n) {
        numberOfParameters = n;
        curNumberOfParameters = n;
    }

    /**
     * Check whether the stack is not null, throw an Exception if it is.
     */

    protected void checkStack(Stack inStack) throws Exception {

        /* Check if stack is null */
        if (null == inStack)
            throw new Exception("Stack argument null");
    }
}
