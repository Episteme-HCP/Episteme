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

package org.jscience.core.mathematics.optimization;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.optimization.genetic.GeneticAlgorithmProvider;
import org.jscience.core.mathematics.optimization.genetic.providers.MulticoreGeneticAlgorithmProvider;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * High-level optimizer that uses a Genetic Algorithm.
 * Refactored to delegate to a high-performance technical backend using the Provider pattern.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeneticOptimizer {

    /**
     * Optimizes a function using a genetic algorithm.
     * 
     * @param fitnessFunction the function to minimize
     * @param dimensions number of variables
     * @param populationSize size of the population
     * @param generations number of iterations
     * @param mutationRate probability of mutation
     * @return the best individual found
     */
    public static Real[] optimize(Function<Real[], Real> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate) {
        // Use ProviderSelector for centralized execution and fallback
        return org.jscience.core.technical.algorithm.ProviderSelector.execute(GeneticAlgorithmProvider.class, 
            org.jscience.core.technical.algorithm.OperationContext.DEFAULT,
            backend -> {
                // Wrap the Real fitness function for the double-based backend
                Function<double[], Double> doubleFitness = d -> {
                    Real[] r = new Real[d.length];
                    for (int i = 0; i < d.length; i++) {
                        r[i] = Real.of(d[i]);
                    }
                    return fitnessFunction.apply(r).doubleValue();
                };
                
                double[] result = backend.solve(doubleFitness::apply, dimensions, populationSize, generations, mutationRate);
                
                Real[] finalResult = new Real[result.length];
                for (int i = 0; i < result.length; i++) {
                    finalResult[i] = Real.of(result[i]);
                }
                return finalResult;
            }
        );
    }
}
