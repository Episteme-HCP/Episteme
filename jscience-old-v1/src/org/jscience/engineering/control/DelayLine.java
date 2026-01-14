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

package org.jscience.engineering.control;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class DelayLine extends BlackBox {
    // Constructor
    /**
     * Creates a new DelayLine object.
     *
     * @param delayTime DOCUMENT ME!
     * @param orderPade DOCUMENT ME!
     */
    public DelayLine(double delayTime, int orderPade) {
        super.setDeadTime(delayTime, orderPade);
        super.name = "DeadTime";
        super.fixedName = "DeadTime";
    }

    // Constructor
    /**
     * Creates a new DelayLine object.
     *
     * @param delayTime DOCUMENT ME!
     */
    public DelayLine(double delayTime) {
        super.setDeadTime(delayTime);
        super.name = "DeadTime";
        super.fixedName = "DeadTime";
    }

    // Set the delay time
    /**
     * DOCUMENT ME!
     *
     * @param delayTime DOCUMENT ME!
     */
    public void setDelayTime(double delayTime) {
        super.setDeadTime(delayTime);
    }

    // Set the delay time and the Pade approximation order
    /**
     * DOCUMENT ME!
     *
     * @param delayTime DOCUMENT ME!
     * @param orderPade DOCUMENT ME!
     */
    public void setDelayTime(double delayTime, int orderPade) {
        super.setDeadTime(delayTime, orderPade);
    }

    // Get the delay time
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDelayTime() {
        return super.deadTime;
    }
}
