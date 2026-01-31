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

package org.jscience.natural.biology;

import java.util.Objects;

/**
 * Models historical population dynamics focusing on the impact of catastrophic events.
 * Simulates effects of epidemics, famines, and mass migrations on historical demographic sets.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class PopulationDynamics {

    private PopulationDynamics() {
        // Prevent instantiation
    }

    /**
     * Simulates the impact of a high-mortality epidemic on a population distribution.
     * 
     * @param population array of population counts (e.g., by age or region)
     * @param mortalityRate mortality rate (0.0 to 1.0)
     * @return new population array after mortality impact
     * @throws NullPointerException if population is null
     * @throws IllegalArgumentException if mortalityRate is outside valid range
     */
    public static double[] simulateEpidemic(double[] population, double mortalityRate) {
        Objects.requireNonNull(population, "Population array cannot be null");
        if (mortalityRate < 0.0 || mortalityRate > 1.0) {
            throw new IllegalArgumentException("Mortality rate must be between 0.0 and 1.0");
        }
        
        double[] next = new double[population.length];
        double survivalRate = 1.0 - mortalityRate;
        for (int i = 0; i < population.length; i++) {
            next[i] = population[i] * survivalRate;
        }
        return next;
    }
}

