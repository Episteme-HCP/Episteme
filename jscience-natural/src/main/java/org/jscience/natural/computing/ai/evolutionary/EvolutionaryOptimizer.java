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

package org.jscience.natural.computing.ai.evolutionary;

import org.jscience.core.mathematics.analysis.Function;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genetic Algorithm for global optimization.
 * <p>
 * Evolutionary algorithm inspired by natural selection.
 * Handles complex, multimodal, non-differentiable objective functions.
 * </p>
 * * <p>
 * <b>Reference:</b><br>
 * Nocedal, J., & Wright, S. J. (2006). <i>Numerical Optimization</i>. Springer.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EvolutionaryOptimizer {

    private final Function<Real[], Real> fitness;
    private final int populationSize;
    private final int dimensions;
    private final Real[] lowerBounds;
    private final Real[] upperBounds;
    private final Random random = new Random();

    // GA parameters
    private double mutationRate = 0.01;
    private double crossoverRate = 0.7;
    private double elitismRate = 0.1;

    public EvolutionaryOptimizer(Function<Real[], Real> fitness, int dimensions,
            Real[] lowerBounds, Real[] upperBounds, int populationSize) {
        this.fitness = fitness;
        this.dimensions = dimensions;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.populationSize = populationSize;
    }


    /**
     * Runs genetic algorithm.
     * 
     * @param generations number of generations
     * @return best individual found
     */
    public Real[] optimize(int generations) {
        GeneticAlgorithm<Real> ga = new GeneticAlgorithm<>(
            mutationRate, crossoverRate, (int)(populationSize * elitismRate), 5
        );

        List<Chromosome<Real>> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            initialChromosomes.add(createRandomIndividual());
        }

        Population<Real> population = new Population<>(initialChromosomes);
        
        for (int gen = 0; gen < generations; gen++) {
            population = ga.evolve(population);
        }

        return population.getFittest().getGenes().toArray(new Real[0]);
    }

    private RealChromosome createRandomIndividual() {
        Real[] genes = new Real[dimensions];
        for (int i = 0; i < dimensions; i++) {
            Real range = upperBounds[i].subtract(lowerBounds[i]);
            genes[i] = lowerBounds[i].add(Real.of(random.nextDouble()).multiply(range));
        }
        return new RealChromosome(genes, fitness, lowerBounds, upperBounds);
    }

    // Parameter setters
    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    public void setCrossoverRate(double rate) {
        this.crossoverRate = rate;
    }

    public void setElitismRate(double rate) {
        this.elitismRate = rate;
    }
}




