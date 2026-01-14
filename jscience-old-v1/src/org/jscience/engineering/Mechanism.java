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

import java.util.Set;


/**
 * The Mechanism interface is the base class to describe complex physical
 * objects. A watch, a microprocessor are good candidates. There is a close
 * relation with graphs.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Mechanism {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getInputs(); //should contain a non empty set of Connectors

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOutputs(); //should contain a non empty set of Connectors

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getParts(); //should contain a non empty set of Mechanisms

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFinal(); //returns true if there is only one mechanism

    /**
     * DOCUMENT ME!
     *
     * @param startTime DOCUMENT ME!
     * @param endTime DOCUMENT ME!
     * @param timeStep DOCUMENT ME!
     */
    public void process(double startTime, double endTime, double timeStep); //given actual values of inputs, tries to process output values

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeasible(); //if there is a way to actually build this mechanism or if this cannot be built (for exemple a motor violating the second thermodynamic principle)
}
