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

package org.jscience.computing.ai.evolutionary;

/**
 * This interface allows any class to be evolvable by {@link
 * org.jscience.computing.ai.evolutionary.GeneticAlgorithm}. It is derived
 * from <code>Comparable</code> so they classes can be efficiently sorted by
 * the GA. Invariably, this is done by fitness.
 *
 * @author James Matthews
 */
public interface Evolvable extends java.lang.Comparable {
    /**
     * Initialize the class to random values. This allows for a default
     * constructor or initializor, as well as the random initializor required
     * for the genetic algorithm.
     */
    public void randomInitialize();

    /**
     * Mutate the class data.
     */
    public void mutate();

    /**
     * Mate with another <code>Evolvable</code> type. It is entirely up
     * to the class implementator how to implement the mating; one-point,
     * two-point crossover etc.
     *
     * @param partner the other mate.
     *
     * @return the child of <code>this</code> and <code>partner</code>.
     */
    public Evolvable mate(Evolvable partner);

    /**
     * Calculate the fitness of this object.
     */
    public void calculateFitness();

    /**
     * Return the fitness of this object.
     *
     * @return the fitness.
     */
    public double getFitness();
}
