/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.function.Function;

/**
 * Interface for Genetic Algorithm providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface GeneticAlgorithmProvider extends AlgorithmProvider {

    /**
     * Solves an optimization problem using double precision.
     */
    double[] solve(Function<double[], Double> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate);

    /**
     * Solves an optimization problem using Real precision.
     */
    Real[] solveReal(Function<Real[], Real> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate);

    @Override
    default String getName() {
        return "Genetic Algorithm Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Genetic Algorithm";
    }
}
