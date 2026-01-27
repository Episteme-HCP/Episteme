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

package org.jscience.biology.evolution;

import org.jscience.computing.ai.ga.Chromosome;
import org.jscience.computing.ai.ga.GeneticAlgorithm;
import org.jscience.computing.ai.ga.Population;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneticAlgorithmTest {

    static class BinaryChromosome implements Chromosome<Boolean> {
        private final List<Boolean> genes;
        private final double fitness;
        private static final Random random = new Random();

        public BinaryChromosome(List<Boolean> genes) {
            this.genes = genes;
            this.fitness = calcFitness(genes);
        }

        public static BinaryChromosome random(int length) {
            List<Boolean> genes = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                genes.add(random.nextBoolean());
            }
            return new BinaryChromosome(genes);
        }

        private static double calcFitness(List<Boolean> genes) {
            return genes.stream().filter(b -> b).count();
        }

        @Override
        public List<Boolean> getGenes() {
            return genes;
        }

        @Override
        public double getFitness() {
            return fitness;
        }

        @Override
        public List<Chromosome<Boolean>> crossover(Chromosome<Boolean> other) {
            int pivot = random.nextInt(genes.size());
            List<Boolean> c1 = new ArrayList<>(genes.subList(0, pivot));
            c1.addAll(other.getGenes().subList(pivot, genes.size()));

            List<Boolean> c2 = new ArrayList<>(other.getGenes().subList(0, pivot));
            c2.addAll(genes.subList(pivot, genes.size()));

            List<Chromosome<Boolean>> offspring = new ArrayList<>();
            offspring.add(new BinaryChromosome(c1));
            offspring.add(new BinaryChromosome(c2));
            return offspring;
        }

        @Override
        public Chromosome<Boolean> mutate(double probability) {
            List<Boolean> mutated = new ArrayList<>(genes);
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
            return genes.stream().map(b -> b ? "1" : "0").collect(Collectors.joining()) + " (Fit: " + fitness + ")";
        }
    }

    @Test
    public void testBinaryOptimization() {
        int targetLength = 20;
        int popSize = 50;

        // Initialize random population
        List<Chromosome<Boolean>> initialList = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            initialList.add(BinaryChromosome.random(targetLength));
        }
        Population<Boolean> pop = new Population<>(initialList);

        // Simple config: 1% mutation, 50% crossover, 2 elites, rank size 5
        GeneticAlgorithm<Boolean> ga = new GeneticAlgorithm<>(0.01, 0.9, 2, 5);

        System.out.println("Gen 0 Best: " + pop.getFittest());

        for (int i = 0; i < 50; i++) {
            pop = ga.evolve(pop);
            // System.out.println("Gen " + pop.getGeneration() + " Best: " +
            // pop.getFittest());
            if (pop.getFittest().getFitness() == targetLength) {
                break;
            }
        }

        Chromosome<Boolean> best = pop.getFittest();
        System.out.println("Final Best: " + best);

        // Assert we got somewhat close to optimal (20)
        // Random chance for 20 bits is 1/2^20 (1/1,000,000). GA should find it easily.
        assertTrue(best.getFitness() >= 18,
                "Genetic Algorithm should converge near max fitness (20), got " + best.getFitness());
    }
}
