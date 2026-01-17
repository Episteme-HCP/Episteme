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

package org.jscience.mathematics.montecarlo;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

/**
 * Primitive-based support for Monte Carlo Pi estimation.
 * Optimized for high-performance double-precision computation.
 
 * <p>
 * <b>Reference:</b><br>
 * Metropolis, N., et al. (1953). Equation of State Calculations by Fast Computing Machines. <i>The Journal of Chemical Physics</i>, 21(6), 1087.
 * </p>
 *
 */
public class MonteCarloPiPrimitiveSupport {

    /**
     * Counts the number of random points that fall inside a unit circle.
     * 
     * @param numSamples Number of samples to take
     * @return Number of points inside circle
     */
    public long countPointsInside(long numSamples) {
        return LongStream.range(0, numSamples).parallel().filter(i -> {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            return x * x + y * y <= 1.0;
        }).count();
    }
}
