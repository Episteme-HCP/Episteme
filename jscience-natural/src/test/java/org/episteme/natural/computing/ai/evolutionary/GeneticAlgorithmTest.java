package org.episteme.natural.computing.ai.evolutionary;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Automated baseline test for GeneticAlgorithm.
 */
public class GeneticAlgorithmTest {


    static class BinaryChromosome implements Chromosome<Boolean> {
        private final List<Boolean> genes;
        private final double fitness;
        private static final java.util.Random random = new java.util.Random();

        public BinaryChromosome(java.util.List<Boolean> genes) {
            this.genes = genes;
            this.fitness = calcFitness(genes);
        }

        public static BinaryChromosome random(int length) {
            java.util.List<Boolean> genes = new java.util.ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                genes.add(random.nextBoolean());
            }
            return new BinaryChromosome(genes);
        }

        private static double calcFitness(java.util.List<Boolean> genes) {
            return genes.stream().filter(b -> b).count();
        }

        @Override
        public java.util.List<Boolean> getGenes() {
            return genes;
        }

        @Override
        public double getFitness() {
            return fitness;
        }

        @Override
        public java.util.List<Chromosome<Boolean>> crossover(Chromosome<Boolean> other) {
            int pivot = random.nextInt(genes.size());
            java.util.List<Boolean> c1 = new java.util.ArrayList<>(genes.subList(0, pivot));
            c1.addAll(other.getGenes().subList(pivot, genes.size()));

            java.util.List<Boolean> c2 = new java.util.ArrayList<>(other.getGenes().subList(0, pivot));
            c2.addAll(genes.subList(pivot, genes.size()));

            java.util.List<Chromosome<Boolean>> offspring = new java.util.ArrayList<>();
            offspring.add(new BinaryChromosome(c1));
            offspring.add(new BinaryChromosome(c2));
            return offspring;
        }

        @Override
        public Chromosome<Boolean> mutate(double probability) {
            java.util.List<Boolean> mutated = new java.util.ArrayList<>(genes);
            boolean changed = false;
            for (int i = 0; i < mutated.size(); i++) {
                if (random.nextDouble() < probability) {
                    mutated.set(i, !mutated.get(i));
                    changed = true;
                }
            }
            return changed ? new BinaryChromosome(mutated) : this;
        }

        @Override
        public String toString() {
            return genes.stream().map(b -> b ? "1" : "0").collect(java.util.stream.Collectors.joining()) + " (Fit: " + fitness + ")";
        }
    }

    @Test
    public void testClassPresence() {
        assertNotNull(GeneticAlgorithm.class);
    }

    @Test
    public void testBinaryOptimization() {
        int targetLength = 20;
        int popSize = 50;

        // Initialize random population
        java.util.List<Chromosome<Boolean>> initialList = new java.util.ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            initialList.add(BinaryChromosome.random(targetLength));
        }
        Population<Boolean> pop = new Population<>(initialList);

        // Simple config: 1% mutation, 50% crossover, 2 elites, rank size 5
        GeneticAlgorithm<Boolean> ga = new GeneticAlgorithm<>(0.01, 0.9, 2, 5);

        System.out.println("Gen 0 Best: " + pop.getFittest());

        for (int i = 0; i < 50; i++) {
            pop = ga.evolve(pop);
            if (pop.getFittest().getFitness() == targetLength) {
                break;
            }
        }

        Chromosome<Boolean> best = pop.getFittest();
        System.out.println("Final Best: " + best);

        assertTrue(best.getFitness() >= 18,
                "Genetic Algorithm should converge near max fitness (20), got " + best.getFitness());
    }
}
