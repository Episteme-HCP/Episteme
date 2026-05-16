/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.ai.evolutionary;

import org.episteme.core.mathematics.analysis.Function;
import org.episteme.core.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A chromosome representing a candidate solution as an array of Real numbers.
 */
public class RealChromosome implements Chromosome<Real> {

    private final Real[] genes;
    private final Function<Real[], Real> fitnessFunction;
    private final Real[] lowerBounds;
    private final Real[] upperBounds;
    private final Random random;
    private double cachedFitness = Double.NaN;

    public RealChromosome(Real[] genes, Function<Real[], Real> fitnessFunction, 
                          Real[] lowerBounds, Real[] upperBounds) {
        this.genes = genes.clone();
        this.fitnessFunction = fitnessFunction;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.random = new Random();
    }

    @Override
    public List<Real> getGenes() {
        return Arrays.asList(genes);
    }

    @Override
    public double getFitness() {
        if (Double.isNaN(cachedFitness)) {
            // EvolutionaryOptimizer usually minimizes, but Chromosome expects higher = better.
            // We transform the objective for the GA.
            Real f = fitnessFunction.evaluate(genes);
            cachedFitness = -f.doubleValue(); // Minimization to Maximization
        }
        return cachedFitness;
    }

    @Override
    public List<Chromosome<Real>> crossover(Chromosome<Real> other) {
        Real[] offspringGenes1 = new Real[genes.length];
        Real[] offspringGenes2 = new Real[genes.length];
        List<Real> otherGenes = other.getGenes();

        // Uniform crossover
        for (int i = 0; i < genes.length; i++) {
            if (random.nextBoolean()) {
                offspringGenes1[i] = genes[i];
                offspringGenes2[i] = otherGenes.get(i);
            } else {
                offspringGenes1[i] = otherGenes.get(i);
                offspringGenes2[i] = genes[i];
            }
        }

        List<Chromosome<Real>> offspring = new ArrayList<>();
        offspring.add(new RealChromosome(offspringGenes1, fitnessFunction, lowerBounds, upperBounds));
        offspring.add(new RealChromosome(offspringGenes2, fitnessFunction, lowerBounds, upperBounds));
        return offspring;
    }

    @Override
    public Chromosome<Real> mutate(double probability) {
        Real[] mutatedGenes = genes.clone();
        for (int i = 0; i < mutatedGenes.length; i++) {
            if (random.nextDouble() < probability) {
                // Gaussian mutation (10% of range)
                Real range = upperBounds[i].subtract(lowerBounds[i]);
                Real sigma = range.multiply(Real.of(0.1));
                Real mutation = Real.of(random.nextGaussian()).multiply(sigma);
                mutatedGenes[i] = mutatedGenes[i].add(mutation);

                // Clamp
                if (mutatedGenes[i].compareTo(lowerBounds[i]) < 0) mutatedGenes[i] = lowerBounds[i];
                if (mutatedGenes[i].compareTo(upperBounds[i]) > 0) mutatedGenes[i] = upperBounds[i];
            }
        }
        return new RealChromosome(mutatedGenes, fitnessFunction, lowerBounds, upperBounds);
    }
}
