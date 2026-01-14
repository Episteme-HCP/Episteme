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

import java.util.NoSuchElementException;


/**
 * A class representing a way to iterate numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can use negative stepping
public class BoundedNumberStepper extends NumberStepper {
    /** DOCUMENT ME! */
    private double endStep;

    //step must be different from zero
    //there is no error if endStep<beginStep and step<0 but endStep will be unreachable
    //there is no error if endStep>beginStep and step>0 but endStep will be unreachable
    //there is no error if endStep=beginStep
/**
     * Creates a new BoundedNumberStepper object.
     *
     * @param beginStep DOCUMENT ME!
     * @param endStep DOCUMENT ME!
       * @param step DOCUMENT ME!
     */
    public BoundedNumberStepper(double beginStep, double endStep, double step) {
        super(beginStep, step);
        this.endStep = endStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEndStep() {
        return endStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @param endStep DOCUMENT ME!
     */
    public void setEndStep(double endStep) {
        this.endStep = endStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return !((((getValue() + getStep()) < endStep) && (getStep() > 0)) ||
        (((getValue() + getStep()) > endStep) && (getStep() < 0)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public Object next() {
        if ((((getValue() + getStep()) < endStep) && (getStep() > 0)) ||
                (((getValue() + getStep()) > endStep) && (getStep() < 0))) {
            setValue(getValue() + getStep());

            return new Double(getValue());
        } else {
            throw new NoSuchElementException();
        }
    }
}
