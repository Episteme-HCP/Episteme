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
 * This interface represents an integrator for scalar samples.
 * <p/>
 * <p>The classes which are devoted to integrate scalar samples
 * should implement this interface.</p>
 *
 * @author L. Maisonobe
 * @version $Id: SampledMappingIntegrator.java,v 1.3 2007-10-23 18:19:26 virtualcall Exp $
 * @see org.jscience.mathematics.analysis.quadrature.SampledMappingIntegrator
 * @see PrimitiveMappingIntegrator
 */
public interface SampledMappingIntegrator {
    /**
     * Integrate a sample over its overall range
     *
     * @param iter iterator over the sample to integrate
     *
     * @return value of the integral over the sample range
     *
     * @throws ExhaustedSampleException if the sample does not have enough
     *         points for the integration scheme
     * @throws MappingException if the underlying sampled function throws one
     */
    public double integrate(SampledMappingIterator iter)
        throws ExhaustedSampleException, MappingException;
}
