/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.economics.inequality;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.social.economics.money.Money;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides metrics and models for analyzing wealth distribution and economic inequality,
 * such as the Gini Coefficient and the Lorenz Curve.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WealthDistribution {

    private WealthDistribution() {
        // Utility class
    }

    /**
     * Calculates the Gini Coefficient for a population's wealth.
     * <p>
     * The Gini coefficient is a measure of statistical dispersion intended to represent
     * the income or wealth distribution of a nation's residents, and is the most commonly used
     * measure of inequality. 0 represents perfect equality, 1 represents perfect inequality.
     * </p>
     *
     * @param wealthList A list of wealth values (as Real numbers).
     * @return The Gini coefficient (0 to 1).
     */
    public static Real calculateGiniCoefficient(List<Real> wealthList) {
        if (wealthList == null || wealthList.isEmpty()) {
            return Real.ZERO;
        }

        int n = wealthList.size();
        
        // 1. Convert to simple double for calculation ease, or stick to Real if strict precision needed.
        // Sorting is required.
        List<Double> sortedWealth = wealthList.stream()
                .map(Real::doubleValue)
                .sorted()
                .collect(Collectors.toList());
        
        // Check for non-negative values? Gini usually assumes non-negative income/wealth.
        // If we have degenerate case of all zeros, return 0.
        double sum = sortedWealth.stream().mapToDouble(Double::doubleValue).sum();
        if (sum == 0) return Real.ZERO;

        // Formula: G = (2 * sum(i * y_i)) / (n * sum(y_i)) - (n + 1) / n
        // where i is 1-indexed rank
        
        double numerator = 0.0;
        for (int i = 0; i < n; i++) {
            // i is 0-indexed in loop, formula uses 1-based rank usually.
            // Let's use the standard form:
            // G = (numerator) / (denominator)
            // numerator = Sum from i=1 to n of (2*i - n - 1) * x_i
            // denominator = n * Sum(x_i)
            // Wait, simpler form:
            
            numerator += (i + 1) * sortedWealth.get(i);
        }
        
        double G = (2 * numerator) / (n * sum) - (double)(n + 1) / n;
        
        return Real.of(G);
    }

    /**
     * Calculates the Gini Coefficient for a list of Money objects.
     *
     * @param incomes List of Money.
     * @return Gini coefficient.
     */
    public static Real calculateGiniFromMoney(List<Money> incomes) {
        if (incomes == null) return Real.ZERO;
        return calculateGiniCoefficient(
            incomes.stream()
                   .map(m -> Real.of(m.getValue().doubleValue())) // Assuming Money has getValue() returning Number or Real
                   .collect(Collectors.toList())
        );
    }
}
