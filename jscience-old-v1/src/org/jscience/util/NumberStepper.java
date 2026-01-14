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

import java.util.Iterator;


/**
 * A class representing a way to iterate numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can use negative stepping
public class NumberStepper implements Iterator {
    /** DOCUMENT ME! */
    private double beginStep;

    /** DOCUMENT ME! */
    private double currentValue;

    /** DOCUMENT ME! */
    private double step;

    //step must be different from zero
/**
     * Creates a new NumberStepper object.
     *
     * @param beginStep DOCUMENT ME!
     * @param step DOCUMENT ME!
     */
    public NumberStepper(double beginStep, double step) {
        this.beginStep = beginStep;
        currentValue = beginStep;
        this.step = step;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getBeginStep() {
        return beginStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @param beginStep DOCUMENT ME!
     */
    protected void setBeginStep(double beginStep) {
        this.beginStep = beginStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return currentValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    protected void setValue(double value) {
        currentValue = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getStep() {
        return step;
    }

    /**
     * DOCUMENT ME!
     *
     * @param step DOCUMENT ME!
     */

    //step must be different from zero
    protected void setStep(double step) {
        this.step = step;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        currentValue = beginStep;
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
        return new Double(currentValue);
    }

    //unsupported
    /**
     * DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
