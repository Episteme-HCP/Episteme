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

package org.jscience.social.economics.models;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Models population dynamics under evolutionary game theory (Replicator Dynamics).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EvolutionaryGameTheory {

    private EvolutionaryGameTheory() {}

    /**
     * Calculates the change in frequency of a strategy using the Replicator Equation.
     * dx_i/dt = x_i * (f_i(x) - phi(x))
     * 
     * @param frequency Current frequency of strategy i (x_i).
     * @param fitness Fitness of strategy i (f_i).
     * @param averageFitness Average fitness of the population (phi).
     * @return The rate of change.
     */
    public static Real replicatorDynamics(Real frequency, Real fitness, Real averageFitness) {
        return frequency.multiply(fitness.subtract(averageFitness));
    }
}
