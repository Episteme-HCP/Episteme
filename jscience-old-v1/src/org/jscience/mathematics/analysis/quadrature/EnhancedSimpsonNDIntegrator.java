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

import org.jscience.mathematics.analysis.ExhaustedSampleException;
import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.SampledMappingIterator;

/**
 * This class implements an enhanced Simpson-like integrator.
 * <p/>
 * <p>A traditional Simpson integrator is based on a quadratic
 * approximation of the function on three equally spaced points. This
 * integrator does the same thing but can handle non-equally spaced
 * points. If it is used on a regular sample, it behaves exactly as a
 * traditional Simpson integrator.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EnhancedSimpsonNDIntegrator.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 */

public class EnhancedSimpsonNDIntegrator
        implements SampledMappingNDIntegrator {
    public double[] integrate(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        EnhancedSimpsonIntegratorSampler sampler =
                new EnhancedSimpsonIntegratorSampler(iter);
        double[] sum = null;

        try {
            while (true) {
                sum = getNumbersAsDouble(sampler.next().getY());
            }
        } catch (ExhaustedSampleException e) {
        }

        return sum;

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
