/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.genetic;

import org.jscience.core.technical.algorithm.GeneticAlgorithmProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Multicore implementation of a Genetic Algorithm.
 * Uses parallel evaluation and supports both Real and double APIs.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class MulticoreGeneticAlgorithmProvider implements GeneticAlgorithmProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getName() {
        return "Multicore Genetic Algorithm (CPU)";
    }

    @Override
    public double[] solve(Function<double[], Double> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate) {
        Random random = new Random();
        double[][] population = new double[populationSize][dimensions];
        double[] fitness = new double[populationSize];

        // Initialize population
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < dimensions; j++) {
                population[i][j] = random.nextDouble() * 200 - 100;
            }
        }
        
        for (int gen = 0; gen < generations; gen++) {
            final double[][] currentPop = population;
            IntStream.range(0, populationSize).parallel().forEach(i -> {
                fitness[i] = fitnessFunction.apply(currentPop[i]);
            });

            double[][] nextPopulation = new double[populationSize][dimensions];
            
            // Selection (Tournament) and Mutation
            for (int i = 0; i < populationSize; i++) {
                int a = random.nextInt(populationSize);
                int b = random.nextInt(populationSize);
                nextPopulation[i] = (fitness[a] < fitness[b] ? population[a] : population[b]).clone();
                
                if (random.nextDouble() < mutationRate) {
                    nextPopulation[i][random.nextInt(dimensions)] += random.nextGaussian();
                }
            }
            population = nextPopulation;
        }

        // Final evaluation
        final double[][] finalPop = population;
        IntStream.range(0, populationSize).parallel().forEach(i -> {
            fitness[i] = fitnessFunction.apply(finalPop[i]);
        });

        int bestIdx = 0;
        for (int i = 1; i < populationSize; i++) {
            if (fitness[i] < fitness[bestIdx]) bestIdx = i;
        }
        return population[bestIdx];
    }

    @Override
    public Real[] solveReal(Function<Real[], Real> fitnessFunction, int dimensions, int populationSize, int generations, double mutationRate) {
        Random random = new Random();
        Real[][] population = new Real[populationSize][dimensions];
        Real[] fitness = new Real[populationSize];

        // Initialize population
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < dimensions; j++) {
                population[i][j] = Real.of(random.nextDouble() * 200 - 100);
            }
        }
        
        for (int gen = 0; gen < generations; gen++) {
            final Real[][] currentPop = population;
            IntStream.range(0, populationSize).parallel().forEach(i -> {
                fitness[i] = fitnessFunction.apply(currentPop[i]);
            });

            Real[][] nextPopulation = new Real[populationSize][dimensions];
            
            for (int i = 0; i < populationSize; i++) {
                int a = random.nextInt(populationSize);
                int b = random.nextInt(populationSize);
                nextPopulation[i] = (fitness[a].compareTo(fitness[b]) < 0 ? population[a] : population[b]).clone();
                
                if (random.nextDouble() < mutationRate) {
                    int dim = random.nextInt(dimensions);
                    nextPopulation[i][dim] = nextPopulation[i][dim].add(Real.of(random.nextGaussian()));
                }
            }
            population = nextPopulation;
        }

        final Real[][] finalPop = population;
        IntStream.range(0, populationSize).parallel().forEach(i -> {
            fitness[i] = fitnessFunction.apply(finalPop[i]);
        });

        int bestIdx = 0;
        for (int i = 1; i < populationSize; i++) {
            if (fitness[i].compareTo(fitness[bestIdx]) < 0) bestIdx = i;
        }
        return population[bestIdx];
    }
}
