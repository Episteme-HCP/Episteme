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

package org.jscience.core.technical.backend.algorithms;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Multicore implementation of a Genetic Algorithm.
 * Uses 2D double arrays for the population and parallel evaluation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreGeneticAlgorithmProvider implements GeneticAlgorithmProvider {

    @Override
    public String getName() {
        return "Multicore Genetic Algorithm (CPU)";
    }

    @Override
    public double[] solve(Function<double[], Double> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate) {
        Random random = new Random();
        double[][] population = new double[populationSize][dimensions];
        double[] fitness = new double[populationSize];

        // Initialize population randomly
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < dimensions; j++) {
                population[i][j] = random.nextDouble() * 200 - 100; // Sample range [-100, 100]
            }
        }
        
        // Initial evaluation
        final double[][] initPop = population;
        IntStream.range(0, populationSize).parallel().forEach(i -> {
            fitness[i] = fitnessFunction.apply(initPop[i]);
        });

        for (int gen = 0; gen < generations; gen++) {
            double[][] nextPopulation = new double[populationSize][dimensions];
            
            // Elitism: carry over best
            int bestIdx = 0;
            for(int i=1; i<populationSize; i++) {
                if(fitness[i] < fitness[bestIdx]) bestIdx = i;
            }
            nextPopulation[0] = population[bestIdx].clone();

            for (int i = 1; i < populationSize; i++) {
                // Tournament selection
                double[] parent1 = select(population, fitness, random);
                double[] parent2 = select(population, fitness, random);

                // Crossover
                double[] child = crossover(parent1, parent2, random);

                // Mutation
                mutate(child, mutationRate, random);

                nextPopulation[i] = child;
            }

            population = nextPopulation;
            final double[][] currentPop = population;
            final double[] currentFit = fitness;
            IntStream.range(0, populationSize).parallel().forEach(i -> {
                currentFit[i] = fitnessFunction.apply(currentPop[i]);
            });
        }

        // Return best
        int bestIdx = 0;
        for (int i = 1; i < populationSize; i++) {
            if (fitness[i] < fitness[bestIdx]) {
                bestIdx = i;
            }
        }
        return population[bestIdx];
    }

    private double[] select(double[][] population, double[] fitness, Random random) {
        int i1 = random.nextInt(population.length);
        int i2 = random.nextInt(population.length);
        return fitness[i1] < fitness[i2] ? population[i1] : population[i2];
    }

    private double[] crossover(double[] p1, double[] p2, Random random) {
        double[] child = new double[p1.length];
        int pivot = random.nextInt(p1.length);
        for (int i = 0; i < p1.length; i++) {
            child[i] = i < pivot ? p1[i] : p2[i];
        }
        return child;
    }

    private void mutate(double[] individual, double rate, Random random) {
        for (int i = 0; i < individual.length; i++) {
            if (random.nextDouble() < rate) {
                individual[i] += random.nextGaussian();
            }
        }
    }
}
