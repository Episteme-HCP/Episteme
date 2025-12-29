/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.function.Function;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Lebesgue integration support.
 * <p>
 * Focuses on methods that handle complex domains and high dimensions,
 * characteristic of Lebesgue integration utility.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LebesgueIntegration {

    private static final Random RANDOM = new Random();

    /**
     * Monte Carlo integration.
     * <p>
     * Approximates the integral of f over a hypercube defined by bounds.
     * Converges as O(1/√N), independent of dimension.
     * </p>
     * 
     * @param f           function to integrate (takes Vector<Real>, returns Real)
     * @param lowerBounds lower bounds of the hypercube
     * @param upperBounds upper bounds of the hypercube
     * @param samples     number of random samples
     * @return approximate integral
     */
    public static Real monteCarlo(Function<Vector<Real>, Real> f,
            Vector<Real> lowerBounds,
            Vector<Real> upperBounds,
            int samples) {

        int dim = lowerBounds.dimension();
        if (upperBounds.dimension() != dim) {
            throw new IllegalArgumentException("Bounds dimensions must match");
        }

        // Calculate volume of the hypercube
        double volume = 1.0;
        for (int i = 0; i < dim; i++) {
            volume *= (upperBounds.get(i).doubleValue() - lowerBounds.get(i).doubleValue());
        }

        double sum = 0.0;
        for (int i = 0; i < samples; i++) {
            // Generate random point
            List<Real> pointCoords = new ArrayList<>(dim);
            for (int d = 0; d < dim; d++) {
                double min = lowerBounds.get(d).doubleValue();
                double max = upperBounds.get(d).doubleValue();
                double val = min + (max - min) * RANDOM.nextDouble();
                pointCoords.add(Real.of(val));
            }
            Vector<Real> point = DenseVector.of(pointCoords, org.jscience.mathematics.sets.Reals.getInstance());

            sum += f.apply(point).doubleValue();
        }

        double average = sum / samples;
        return Real.of(volume * average);
    }

    /**
     * Integration of a simple function (step function).
     * <p>
     * Fundamental to definition of Lebesgue integral.
     * ∫ f dμ = Σ a_i * μ(E_i)
     * </p>
     * 
     * @param values   values (a_i) taken by the function
     * @param measures measures μ(E_i) of the sets where function takes value a_i
     * @return the integral value
     */
    public static Real integrateSimpleFunction(List<Real> values, List<Real> measures) {
        if (values.size() != measures.size()) {
            throw new IllegalArgumentException("Values and measures lists must have same size");
        }

        Real sum = Real.ZERO;
        for (int i = 0; i < values.size(); i++) {
            sum = sum.add(values.get(i).multiply(measures.get(i)));
        }
        return sum;
    }
}