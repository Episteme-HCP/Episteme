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

/**
 * This class provides sampled values of the function t -> [f(t)^2, f'(t)^2].
 * <p/>
 * This class is a helper class used to compute a first guess of the
 * harmonic coefficients of a function <code>f (t) = a cos (omega t +
 * phi)</code>.
 *
 * @author L. Maisonobe
 * @version $Id: F2FP2Iterator.java,v 1.2 2007-10-21 17:38:16 virtualcall Exp $
 * @see FFPIterator
 * @see HarmonicCoefficientsGuesser
 */

class F2FP2Iterator
        implements SampledMappingIterator, Serializable {

    private FFPIterator ffpIterator;

    public F2FP2Iterator(AbstractCurveFitter.FitMeasurement[] measurements) {
        ffpIterator = new FFPIterator(measurements);
    }

    public int getDimension() {
        return 2;
    }

    public SampledMapping getSampledMapping() {
        return null;
    }

    public boolean hasNext() {
        return ffpIterator.hasNext();
    }

    public ValuedPair next() {

        // get the raw values from the underlying FFPIterator
        ValuedPair point = (ValuedPair) ffpIterator.next();

        // hack the values (to avoid building a new object)
        double[] y = getNumbersAsDouble(point.getY());
        y[0] *= y[0];
        y[1] *= y[1];
        return point;

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private double[] getNumbersAsDouble(Number[] numbers) {
        double[] result;
        result = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            result[i] = numbers[i].doubleValue();
        }
        return result;
    }

}
