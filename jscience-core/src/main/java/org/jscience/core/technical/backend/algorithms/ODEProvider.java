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

package org.jscience.core.technical.backend.algorithms;

import org.jscience.core.mathematics.analysis.Function;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.backend.AlgorithmProvider;

/**
 * Service provider interface for Ordinary Differential Equation (ODE) solvers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ODEProvider extends AlgorithmProvider {
    
    /**
     * Solves an initial value problem dy/dt = f(t, y).
     * 
     * @param f The derivative function f(t, y). Input is {t, y...}, Output is {dy/dt...}.
     * @param t0 Initial time.
     * @param y0 Initial state vector.
     * @param tEnd Target time.
     * @param h Step size (suggested).
     * @return The state vector at tEnd.
     */
    Real[] solve(Function<Real[], Real[]> f, Real t0, Real[] y0, Real tEnd, Real h);
}
