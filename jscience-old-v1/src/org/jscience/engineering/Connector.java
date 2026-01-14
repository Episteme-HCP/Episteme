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

package org.jscience.engineering;

/**
 * The Connector interface is the base class for connecting parts of a
 * mechanism (inputs, outputs). There is a close relation with graphs.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Connector {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Mechanism getInputMechanism(); //should return null for a source input

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Mechanism getOutputMechanism(); //should return null for a pit output

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(double time); //the value at t, returns null if unknown (although you should try to provide a value when the connecotr is at rest)

    //corresponding setters are highly recommended for this interface although optional
}
