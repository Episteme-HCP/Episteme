/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.technical.backend.algorithms;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

/**
 * Multicore implementation of MonteCarloPiProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreMonteCarloPiProvider implements MonteCarloPiProvider {

    @Override
    public long countPointsInside(long numSamples) {
        // Use parallel stream for efficient multicore utility.
        // ThreadLocalRandom is efficient for parallel streams.
        // We chunk the stream? LongStream.range does split efficiently?
        // Yes, but generating randoms in map is fine.

        // Optimisation: avoid mapToObj. Use generic map or just filter count.
        // filter(i -> inside).count()

        return LongStream.range(0, numSamples).parallel()
                .filter(i -> {
                    double x = ThreadLocalRandom.current().nextDouble();
                    double y = ThreadLocalRandom.current().nextDouble();
                    return (x * x + y * y) <= 1.0;
                })
                .count();
    }

    @Override
    public String getName() {
        return "Multicore Monte Carlo";
    }
}
