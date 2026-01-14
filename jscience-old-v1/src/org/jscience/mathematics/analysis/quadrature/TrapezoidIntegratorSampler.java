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
 * This class implements a trapezoid integrator as a sample.
 * <p/>
 * <p>A trapezoid integrator is a very simple one that assumes the
 * function is constant over the integration step. Since it is very
 * simple, this algorithm needs very small steps to achieve high
 * accuracy, and small steps lead to numerical errors and
 * instabilities.</p>
 * <p/>
 * <p>This algorithm is almost never used and has been included in
 * this package only as a simple template for more useful
 * integrators.</p>
 *
 * @author L. Maisonobe
 * @version $Id: TrapezoidIntegratorSampler.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 * @see TrapezoidIntegrator
 */

public class TrapezoidIntegratorSampler implements SampledMappingIterator {

    /**
     * Underlying sample iterator.
     */
    private SampledMappingIterator iter;

    /**
     * Current point.
     */
    private ValuedPair current;

    /**
     * Current running sum.
     */
    private double sum;

    /**
     * Constructor.
     * Build an integrator from an underlying sample iterator.
     *
     * @param iter iterator over the base function
     */
    public TrapezoidIntegratorSampler(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        this.iter = iter;

        // get the first point
        current = iter.next();

        // initialize the sum
        sum = 0.0;

    }

    public SampledMapping getSampledMapping() {
        return iter.getSampledMapping();
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public ValuedPair next() throws ExhaustedSampleException, MappingException {
        // performs one step of a trapezoid scheme
        ValuedPair previous = current;
        current = iter.next();
        sum += 0.5
                * (current.getX()[0].doubleValue() - previous.getX()[0].doubleValue())
                * (previous.getY()[0].doubleValue() + current.getY()[0].doubleValue());

        return new ValuedPair(current.getX()[0].doubleValue(), sum);

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
