package org.jscience.computing.ai.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Standard Genetic Algorithm implementation.
 * Uses Elitism, Tournament Selection, and Uniform Crossover.
 * 
 * @param <T> Gene type
 */
public class GeneticAlgorithm<T> {

    private final double mutationRate;
    private final double crossoverRate;
    private final int elitismCount;
    private final int tournamentSize;
    private final Random random;

    public GeneticAlgorithm(double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
        this.random = new Random();
    }

    /**
     * Evolve a population to the next generation.
     * 
     * @param population current population
     * @return next generation population
     */
    public Population<T> evolve(Population<T> population) {
        List<Chromosome<T>> newChromosomes = new ArrayList<>(population.size());

        // Elitism: Keep best N
        population.sort();
        for (int i = 0; i < elitismCount && i < population.size(); i++) {
            newChromosomes.add(population.get(i));
        }

        // Crossover
        while (newChromosomes.size() < population.size()) {
            Chromosome<T> parent1 = selectTournament(population);
            Chromosome<T> parent2 = selectTournament(population);

            if (random.nextDouble() < crossoverRate) {
                List<Chromosome<T>> offspring = parent1.crossover(parent2);
                newChromosomes.addAll(offspring);
            } else {
                newChromosomes.add(parent1);
                if (newChromosomes.size() < population.size()) {
                    newChromosomes.add(parent2);
                }
            }
        }

        // Trim if we added too many due to pair crossover
        if (newChromosomes.size() > population.size()) {
            newChromosomes.subList(population.size(), newChromosomes.size()).clear();
        }

        // Mutation
        List<Chromosome<T>> mutatedChromosomes = new ArrayList<>(population.size());
        for (int i = 0; i < newChromosomes.size(); i++) {
            // Don't mutate elites (indices 0 to elitismCount-1)
            if (i < elitismCount) {
                mutatedChromosomes.add(newChromosomes.get(i));
            } else {
                mutatedChromosomes.add(newChromosomes.get(i).mutate(mutationRate));
            }
        }

        return new Population<>(mutatedChromosomes, population.getGeneration() + 1);
    }

    private Chromosome<T> selectTournament(Population<T> population) {
        Chromosome<T> best = null;
        for (int i = 0; i < tournamentSize; i++) {
            Chromosome<T> candidate = population.get(random.nextInt(population.size()));
            if (best == null || candidate.getFitness() > best.getFitness()) {
                best = candidate;
            }
        }
        return best;
    }
}
