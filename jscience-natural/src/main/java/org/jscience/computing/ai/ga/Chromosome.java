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

package org.jscience.computing.ai.ga;

import java.util.List;

/**
 * Represents a single candidate solution in the genetic algorithm.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


