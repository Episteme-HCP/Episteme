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

package org.jscience.util;

/**
 * This interface is used to provide a simple way of providing classes with a
 * standardized structure for time-steppable classes. A standard
 * initialization routine is also provided.
 *
 * @author James Matthews
 */

//also see org.jscience.Simulation for a similar interface
//one of these two classes should be deprecated although I don't know which one
public interface Steppable {
    /**
     * A standard initialization function. This should be assumed to be
     * a recurrable initialization procedure. Any one-off initializations
     * should be done in the constructor.
     */
    public void init();

    /**
     * Move the class forward one time-step.
     */
    public void doStep();

    /**
     * Reset the object. <code>reset</code> can often be substituted
     * for another call to <code>init</code>.
     */
    public void reset();
}
