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

package org.jscience.mathematics.analysis.fitting;

import org.jscience.mathematics.analysis.SampledMapping;
import org.jscience.mathematics.analysis.SampledMappingIterator;
import org.jscience.mathematics.analysis.ValuedPair;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * This class provides sampled values of the function t -> [f(t), f'(t)].
 * <p/>
 * This class is a helper class used to compute a first guess of the
 * harmonic coefficients of a function <code>f (t) = a cos (omega t +
 * phi)</code>.
 *
 * @author L. Maisonobe
 * @version $Id: FFPIterator.java,v 1.2 2007-10-21 17:38:16 virtualcall Exp $
 * @see F2FP2Iterator
 * @see HarmonicCoefficientsGuesser
 */

class FFPIterator
        implements SampledMappingIterator, Serializable {

    private AbstractCurveFitter.FitMeasurement[] measurements;
    private int nextIndex;

    private AbstractCurveFitter.FitMeasurement previous;
    private double previousY;
    private double previousYP;

    private AbstractCurveFitter.FitMeasurement current;
    private double nextY;

    private AbstractCurveFitter.FitMeasurement next;
    private double currentY;
    private double currentYP;

    public FFPIterator(AbstractCurveFitter.FitMeasurement[] measurements) {
        this.measurements = measurements;

        // initialize the points of the raw sample
        current = measurements[0];
        currentY = current.getMeasuredValue();
        next = measurements[1];
        nextY = next.getMeasuredValue();
        nextIndex = 2;

    }

    public int getDimension() {
        return 2;
    }

    public SampledMapping getSampledMapping() {
        return null;
    }

    public boolean hasNext() {
        return nextIndex < measurements.length;
    }

    public ValuedPair next() {
        if (nextIndex >= measurements.length) {
            throw new NoSuchElementException();
        }

        // shift the points
        previous = current;
        previousY = currentY;
        current = next;
        currentY = nextY;
        next = measurements[nextIndex++];
        nextY = next.getMeasuredValue();

        // return the two dimensions vector [f(x), f'(x)]
        Double[] table = new Double[2];
        table[0] = new Double(currentY);
        table[1] = new Double((nextY - previousY) / (next.x - previous.x));
        Double[] param;
        param = new Double[1];
        param[0] = new Double(current.x);
        return new ValuedPair(param, table);

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
