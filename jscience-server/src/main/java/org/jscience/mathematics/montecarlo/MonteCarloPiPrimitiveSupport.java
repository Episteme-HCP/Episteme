/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.montecarlo;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

/**
 * Primitive-based support for Monte Carlo Pi estimation.
 * Optimized for high-performance double-precision computation.
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
