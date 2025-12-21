/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.mathematics.analysis;

import org.jscience.mathematics.analysis.integration.Integrator;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides numerical integration capabilities.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Integration {

    private static Integrator DEFAULT_INTEGRATOR = new org.jscience.mathematics.analysis.integration.AdaptiveSimpsonIntegrator();

    private Integration() {
        // Utility class
    }

    /**
     * Sets the default integrator strategy.
     *
     * @param integrator the integrator to use by default
     */
    public static void setDefaultIntegrator(Integrator integrator) {
        if (integrator == null) {
            throw new IllegalArgumentException("Integrator cannot be null");
        }
        DEFAULT_INTEGRATOR = integrator;
    }

    /**
     * Gets the current default integrator.
     *
     * @return the default integrator
     */
    public static Integrator getDefaultIntegrator() {
        return DEFAULT_INTEGRATOR;
    }

    /**
     * Computes the definite integral of a function over the interval [a, b]
     * using the default integrator strategy.
     * 
     * @param f the function to integrate
     * @param a the lower bound
     * @param b the upper bound
     * @return the approximate value of the integral
     */
    public static Real integrate(RealFunction f, Real a, Real b) {
        return DEFAULT_INTEGRATOR.integrate(f, a, b);
    }

    /**
     * Computes the definite integral using the Trapezoidal rule (simpler, faster,
     * less accurate).
     * 
     * @param f the function
     * @param a lower bound
     * @param b upper bound
     * @param n number of intervals
     * @return integral value
     */
    public static Real trapezoidal(RealFunction f, Real a, Real b, int n) {
        return new org.jscience.mathematics.analysis.integration.TrapezoidalIntegrator(n).integrate(f, a, b);
    }
}
