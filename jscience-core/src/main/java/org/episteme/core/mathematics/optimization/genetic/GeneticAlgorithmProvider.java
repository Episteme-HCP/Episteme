/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.optimization.genetic;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.function.Function;

/**
 * Interface for Genetic Algorithm providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface GeneticAlgorithmProvider extends AlgorithmProvider {

    double[] solve(java.util.function.ToDoubleFunction<double[]> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate);
    Real[] solve(Function<Real[], Real> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate);

    @Override
    default String getName() {
        return "Genetic Algorithm Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Genetic Algorithm";
    }
}
