package org.jscience.biology.evolution;

import java.util.List;

/**
 * Represents a single candidate solution in the genetic algorithm.
 * 
 * @param <T> The type of the gene (e.g., Boolean, Integer, Double)
 */
public interface Chromosome<T> extends Comparable<Chromosome<T>> {

    /**
     * Gets the sequence of genes.
     * 
     * @return an unmodifiable list of genes
     */
    List<T> getGenes();

    /**
     * Calculates the fitness of this chromosome.
     * Higher fitness is better.
     * 
     * @return the fitness score
     */
    double getFitness();

    /**
     * Creates a new chromosome by crossing over with another.
     * 
     * @param other the partner chromosome
     * @return a pair of new chromosomes (offspring)
     */
    List<Chromosome<T>> crossover(Chromosome<T> other);

    /**
     * Mutates this chromosome.
     * 
     * @param probability chance of mutation per gene
     * @return a new mutated chromosome
     */
    Chromosome<T> mutate(double probability);

    @Override
    default int compareTo(Chromosome<T> other) {
        return Double.compare(this.getFitness(), other.getFitness());
    }
}
