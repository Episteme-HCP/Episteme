/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.statistics.montecarlo;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
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

    double integrate(ToDoubleFunction<double[]> function, double[] lowerBounds, 
                           double[] upperBounds, int samples);

    Real integrate(Function<Real[], Real> function, Real[] lowerBounds, 
                         Real[] upperBounds, int samples);

    double estimatePi(int samples);
    Real estimatePi(int samples, boolean useReal); // Using boolean to differentiate if needed, or just overload
    // Actually, Real estimatePi(int samples) is enough as return type differs but parameters are same.
    // Wait, Java doesn't allow overloads based ONLY on return type.
    // So for estimatePi, I might need to keep a different name or change parameters.
    // But wait, integrate() has different parameters (Function vs ToDoubleFunction).
    // For estimatePi(int samples), they HAVE the same parameters.
    // I will use Real estimatePiReal(int samples) -> keep this one or change to Real estimatePi(long samples) etc.
    // Let's use Real estimatePi(long samples) for the Real version if I want to overload.

    default long countPointsInside(long samples) {
        return (long) (samples * (estimatePi((int) samples) / 4.0));
    }

    @Override
    default String getName() {
        return "Monte Carlo Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Monte Carlo Simulation";
    }
}
