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

package org.jscience.client.economics;

import org.jscience.client.algorithms.Algorithm;
import java.util.function.Function;

/**
 * Market Equilibrium Solver.
 * Finds the price point where Supply equals Demand.
 * Uses binary search for monotonic functions.
 */
public class MarketEquilibriumSolver implements Algorithm<MarketEquilibriumSolver.MarketData, Double> {

    public record MarketData(Function<Double, Double> supplyFunc, Function<Double, Double> demandFunc, double minPrice,
            double maxPrice) {
    }

    @Override
    public Double execute(MarketData input) {
        double low = input.minPrice;
        double high = input.maxPrice;
        double epsilon = 1e-6;

        for (int i = 0; i < 100; i++) { // Max iterations
            double mid = (low + high) / 2.0;
            double supply = input.supplyFunc.apply(mid);
            double demand = input.demandFunc.apply(mid);
            double diff = supply - demand;

            if (Math.abs(diff) < epsilon) {
                return mid;
            }

            // If supply > demand, price is too high (usually)
            if (diff > 0) {
                high = mid;
            } else {
                low = mid;
            }
        }
        return (low + high) / 2.0;
    }

    @Override
    public String getName() {
        return "Binary Search Equilibrium Solver";
    }

    @Override
    public String getComplexity() {
        return "O(log((max-min)/epsilon))";
    }

    @Override
    public double getQualityScore() {
        return 0.90;
    }
}
