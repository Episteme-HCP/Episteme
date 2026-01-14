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

import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.PrimitiveMapping;


/**
 * This interface represents an integrator for scalar functions.
 * <p/>
 * <p>The classes which are devoted to integrate scalar functions
 * should implement this interface. The functions which can be handled
 * should implement the {@link
 * org.jscience.mathematics.analysis.functions.scalar.ComputableFunction
 * ComputableFunction} interface.</p>
 *
 * @author L. Maisonobe
 * @version $Id: PrimitiveMappingIntegrator.java,v 1.3 2007-10-23 18:19:25 virtualcall Exp $
 * @see org.jscience.mathematics.analysis.PrimitiveMapping
 */
public interface PrimitiveMappingIntegrator {
    /**
     * Integrate a function over a defined range.
     *
     * @param f function to integrate
     * @param a first bound of the range (can be lesser or greater than b)
     * @param b second bound of the range (can be lesser or greater than a)
     *
     * @return value of the integral over the range
     *
     * @throws MappingException if the underlying function throws one
     */
    public double integrate(PrimitiveMapping f, double a, double b)
        throws MappingException;
}
