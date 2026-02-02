/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Interface for Monte Carlo integration and estimation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MonteCarloProvider extends AlgorithmProvider {

    /**
     * Performs multi-dimensional Monte Carlo integration using double primitives.
     */
    double integrate(ToDoubleFunction<double[]> function, double[] lowerBounds, 
                           double[] upperBounds, int samples);

    /**
     * Performs multi-dimensional Monte Carlo integration using Real objects.
     */
    Real integrate(Function<Real[], Real> function, Real[] lowerBounds, 
                         Real[] upperBounds, int samples);

    /**
     * Estimates Pi using Monte Carlo method.
     */
    double estimatePi(int samples);

    /**
     * Estimates Pi using Monte Carlo method with Real precision.
     */
    Real estimatePiReal(int samples);

    /**
     * Counts how many points fall inside the unit circle out of total samples.
     * Useful for Pi estimation tasks that prefer raw counts.
     */
    default long countPointsInside(long samples) {
        return (long) (samples * (estimatePi((int) samples) / 4.0));
    }

    @Override
    default String getName() {
        return "Monte Carlo Provider";
    }
}
