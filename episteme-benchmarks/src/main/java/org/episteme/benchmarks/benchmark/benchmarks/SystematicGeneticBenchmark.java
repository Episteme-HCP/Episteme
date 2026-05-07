/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;

import com.google.auto.service.AutoService;

import org.episteme.core.mathematics.optimization.genetic.GeneticAlgorithmProvider;

import java.util.function.Function;

/**
 * A benchmark that systematically tests all available GeneticAlgorithmProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicGeneticBenchmark implements SystematicBenchmark<GeneticAlgorithmProvider> {

    private GeneticAlgorithmProvider currentProvider;
    private int dimensions = 10;
    private int populationSize = 50;
    private int generations = 100;
    private double mutationRate = 0.01;

    /**
     * Sphere function: f(x) = sum(x_i^2).
     * This is a standard continuous optimization benchmark.
     */
    private final Function<double[], Double> fitnessFunction = x -> {
        double sum = 0;
        for (double val : x) {
            sum += val * val;
        }
        return sum;
    };

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "genetic-systematic"; }
    @Override public String getNameBase() { return "Systematic Genetic Optimization"; }
    @Override public String getDescription() { return "Genetic algorithm optimization (Sphere function, population-based evolution)"; }
    @Override public String getDomain() { return "Optimization"; }
    @Override public Class<GeneticAlgorithmProvider> getProviderClass() { return GeneticAlgorithmProvider.class; }

    @Override
    public void setup() {
        // No heavy setup needed for continuous optimization benchmark
    }

    @Override
    public void setProvider(GeneticAlgorithmProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.solve(fitnessFunction::apply, dimensions, populationSize, generations, mutationRate);
        }
    }

    @Override
    public void teardown() {
    }
}
