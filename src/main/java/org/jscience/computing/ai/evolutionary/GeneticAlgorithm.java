package org.jscience.computing.ai.evolutionary;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import java.util.Random;
import java.util.Arrays;

/**
 * Genetic Algorithm for global optimization.
 * <p>
 * Evolutionary algorithm inspired by natural selection.
 * Handles complex, multimodal, non-differentiable objective functions.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class GeneticAlgorithm {

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

    public GeneticAlgorithm(Function<Real[], Real> fitness, int dimensions,
            Real[] lowerBounds, Real[] upperBounds, int populationSize) {
        this.fitness = fitness;
        this.dimensions = dimensions;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.populationSize = populationSize;
    }

    /**
     * Individual in population.
     */
    private static class Individual implements Comparable<Individual> {
        Real[] genes;
        Real fitnessValue;

        Individual(int dimensions) {
            genes = new Real[dimensions];
        }

        @Override
        public int compareTo(Individual other) {
            return this.fitnessValue.compareTo(other.fitnessValue);
        }
    }

    /**
     * Runs genetic algorithm.
     * 
     * @param generations number of generations
     * @return best individual found
     */
    public Real[] optimize(int generations) {
        // Initialize population
        Individual[] population = new Individual[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = createRandomIndividual();
            evaluateFitness(population[i]);
        }

        Individual best = population[0];

        // Evolution loop
        for (int gen = 0; gen < generations; gen++) {
            // Sort by fitness (minimization: lower is better)
            Arrays.sort(population);

            // Track best
            if (population[0].fitnessValue.compareTo(best.fitnessValue) < 0) {
                best = clone(population[0]);
            }

            // Create next generation
            Individual[] nextGen = new Individual[populationSize];

            // Elitism: keep best individuals
            int eliteCount = (int) (populationSize * elitismRate);
            for (int i = 0; i < eliteCount; i++) {
                nextGen[i] = clone(population[i]);
            }

            // Fill rest with offspring
            for (int i = eliteCount; i < populationSize; i++) {
                // Selection (tournament)
                Individual parent1 = tournamentSelect(population);
                Individual parent2 = tournamentSelect(population);

                // Crossover
                Individual offspring;
                if (random.nextDouble() < crossoverRate) {
                    offspring = crossover(parent1, parent2);
                } else {
                    offspring = clone(parent1);
                }

                // Mutation
                mutate(offspring);

                // Evaluate
                evaluateFitness(offspring);
                nextGen[i] = offspring;
            }

            population = nextGen;
        }

        return best.genes;
    }

    private Individual createRandomIndividual() {
        Individual ind = new Individual(dimensions);
        for (int i = 0; i < dimensions; i++) {
            Real range = upperBounds[i].subtract(lowerBounds[i]);
            ind.genes[i] = lowerBounds[i].add(Real.of(random.nextDouble()).multiply(range));
        }
        return ind;
    }

    private void evaluateFitness(Individual ind) {
        ind.fitnessValue = fitness.evaluate(ind.genes);
    }

    private Individual tournamentSelect(Individual[] population) {
        int tournamentSize = 5;
        Individual best = population[random.nextInt(population.length)];

        for (int i = 1; i < tournamentSize; i++) {
            Individual competitor = population[random.nextInt(population.length)];
            if (competitor.fitnessValue.compareTo(best.fitnessValue) < 0) {
                best = competitor;
            }
        }

        return best;
    }

    private Individual crossover(Individual parent1, Individual parent2) {
        Individual offspring = new Individual(dimensions);

        // Uniform crossover
        for (int i = 0; i < dimensions; i++) {
            if (random.nextBoolean()) {
                offspring.genes[i] = parent1.genes[i];
            } else {
                offspring.genes[i] = parent2.genes[i];
            }
        }

        return offspring;
    }

    private void mutate(Individual ind) {
        for (int i = 0; i < dimensions; i++) {
            if (random.nextDouble() < mutationRate) {
                // Gaussian mutation
                Real sigma = upperBounds[i].subtract(lowerBounds[i]).multiply(Real.of(0.1));
                Real mutation = Real.of(random.nextGaussian()).multiply(sigma);
                ind.genes[i] = ind.genes[i].add(mutation);

                // Clamp to bounds
                if (ind.genes[i].compareTo(lowerBounds[i]) < 0) {
                    ind.genes[i] = lowerBounds[i];
                }
                if (ind.genes[i].compareTo(upperBounds[i]) > 0) {
                    ind.genes[i] = upperBounds[i];
                }
            }
        }
    }

    private Individual clone(Individual ind) {
        Individual copy = new Individual(dimensions);
        System.arraycopy(ind.genes, 0, copy.genes, 0, dimensions);
        copy.fitnessValue = ind.fitnessValue;
        return copy;
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
