/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.optimization.providers;

import org.jscience.core.technical.algorithm.GeneticAlgorithmProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.function.Function;

/**
 * Native multicore Genetic Algorithm provider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeMulticoreGeneticAlgorithmProvider implements GeneticAlgorithmProvider {

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public double[] solve(Function<double[], Double> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate) {
        // Optimization: Native C++/Fortran optimizer (Placeholder)
        return new org.jscience.core.technical.algorithm.genetic.MulticoreGeneticAlgorithmProvider()
            .solve(fitnessFunction, dimensions, populationSize, generations, mutationRate);
    }

    @Override
    public Real[] solveReal(Function<Real[], Real> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate) {
        Function<double[], Double> wrapper = d -> {
            Real[] r = new Real[d.length];
            for (int i = 0; i < d.length; i++) r[i] = Real.of(d[i]);
            return fitnessFunction.apply(r).doubleValue();
        };
        
        double[] result = solve(wrapper, dimensions, populationSize, generations, mutationRate);
        Real[] realResult = new Real[result.length];
        for (int i = 0; i < result.length; i++) realResult[i] = Real.of(result[i]);
        return realResult;
    }

    @Override
    public String getName() {
        return "Native Multicore Genetic Algorithm (C++/HPC)";
    }
}
