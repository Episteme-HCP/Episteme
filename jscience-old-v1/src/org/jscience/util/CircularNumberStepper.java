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
 * A class representing a way to iterate numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can use negative stepping
public class CircularNumberStepper extends BoundedNumberStepper {
    /** DOCUMENT ME! */
    private double endStep;

    //step must be different from zero
/**
     * Creates a new CircularNumberStepper object.
     *
     * @param beginStep DOCUMENT ME!
     * @param endStep DOCUMENT ME!
      * @param step DOCUMENT ME!
     */

    //when currentValue has reached endStep, it is rolled back to begin step
    public CircularNumberStepper(double beginStep, double endStep, double step) {
        super(beginStep, endStep, step);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //always true
    public boolean hasNext() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        if ((((getValue() + getStep()) < endStep) && (getStep() > 0)) ||
                (((getValue() + getStep()) > endStep) && (getStep() < 0))) {
            setValue(getValue() + getStep());
        } else {
            reset();
        }

        return new Double(getValue());
    }
}
