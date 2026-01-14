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

package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.*;

/**
 * This class implements an enhanced Simpson integrator as a sample.
 * <p/>
 * <p>A traditional Simpson integrator is based on a quadratic
 * approximation of the function on three equally spaced points. This
 * integrator does the same thing but can handle non-equally spaced
 * points. If it is used on a regular sample, it behaves exactly as a
 * traditional Simpson integrator.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EnhancedSimpsonNDIntegratorSampler.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 * @see EnhancedSimpsonIntegrator
 */

public class EnhancedSimpsonNDIntegratorSampler
        implements SampledMappingIterator {

    /**
     * Underlying sample iterator.
     */
    private SampledMappingIterator iter;

    /**
     * Next point.
     */
    private ValuedPair next;

    /**
     * Current running sum.
     */
    private double[] sum;

    /**
     * Constructor.
     * Build an integrator from an underlying sample iterator.
     *
     * @param iter iterator over the base function
     */
    public EnhancedSimpsonNDIntegratorSampler(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        this.iter = iter;

        // get the first point
        next = iter.next();

        // initialize the sum
        sum = new double[getDimension()];
        for (int i = 0; i < sum.length; ++i) {
            sum[i] = 0.0;
        }

    }

    public SampledMapping getSampledMapping() {
        return iter.getSampledMapping();
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public int getDimension() {
        return iter.getSampledMapping().numOutputDimensions();
    }

    public ValuedPair next() throws ExhaustedSampleException, MappingException {
        // performs one step of an enhanced Simpson scheme
        ValuedPair previous = next;
        ValuedPair current = iter.next();

        try {
            next = iter.next();

            double h1 = current.getX()[0].doubleValue() - previous.getX()[0].doubleValue();
            double h2 = next.getX()[0].doubleValue() - current.getX()[0].doubleValue();
            double cP = (h1 + h2) * (2 * h1 - h2) / (6 * h1);
            double cC = (h1 + h2) * (h1 + h2) * (h1 + h2) / (6 * h1 * h2);
            double cN = (h1 + h2) * (2 * h2 - h1) / (6 * h2);

            double[] pY = getNumbersAsDouble(previous.getY());
            double[] cY = getNumbersAsDouble(current.getY());
            double[] nY = getNumbersAsDouble(next.getY());
            for (int i = 0; i < sum.length; ++i) {
                sum[i] += cP * pY[i] + cC * cY[i] + cN * nY[i];
            }

        } catch (ExhaustedSampleException e) {
            // we have an incomplete step at the end of the sample
            // we use a trapezoid scheme for this last step
            double halfDx = 0.5 * (current.getX()[0].doubleValue() - previous.getX()[0].doubleValue());
            double[] pY = getNumbersAsDouble(previous.getY());
            double[] cY = getNumbersAsDouble(current.getY());
            for (int i = 0; i < sum.length; ++i) {
                sum[i] += halfDx * (pY[i] + cY[i]);
            }
            return new ValuedPair(current.getX(), getDoublesAsNumber(sum));
        }

        double[] values = new double[sum.length];
        System.arraycopy(sum, 0, values, 0, sum.length);
        return new ValuedPair(next.getX(), getDoublesAsNumber(values));

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

    private Number[] getDoublesAsNumber(double[] doubles) {
        Number[] result;
        result = new Number[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            result[i] = new Double(doubles[i]);
        }
        return result;
    }

}
