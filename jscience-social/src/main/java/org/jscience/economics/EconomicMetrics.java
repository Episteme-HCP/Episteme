package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Economic metrics and inequality analysis.
 */
public final class EconomicMetrics {

    private EconomicMetrics() {}

    /**
     * Calculates the Gini coefficient for a population's income distribution.
     * G = (sum|xi - xj|) / (2 * n^2 * mean)
     * 
     * @param incomes List of individual incomes.
     * @return Gini coefficient (0.0 for perfect equality, 1.0 for perfect inequality).
     */
    public static Real giniCoefficient(List<Real> incomes) {
        if (incomes == null || incomes.size() < 2) return Real.of(0.0);
        
        List<Real> sortedIncomes = new ArrayList<>(incomes);
        Collections.sort(sortedIncomes, (a, b) -> a.compareTo(b));
        
        int n = sortedIncomes.size();
        Real sumOfAbsoluteDifferences = Real.of(0.0);
        Real sumOfIncomes = Real.of(0.0);
        
        for (int i = 0; i < n; i++) {
            sumOfIncomes = sumOfIncomes.add(sortedIncomes.get(i));
            // Optimization for Gini formula: sum( (2i - n - 1) * yi )
            Real weight = Real.of(2 * (i + 1) - n - 1);
            sumOfAbsoluteDifferences = sumOfAbsoluteDifferences.add(weight.multiply(sortedIncomes.get(i)));
        }
        
        Real mean = sumOfIncomes.divide(Real.of(n));
        if (mean.compareTo(Real.of(0.0)) == 0) return Real.of(0.0);
        
        // G = sumAbDiff / (n * sumIncomes)
        return sumOfAbsoluteDifferences.divide(Real.of(n).multiply(sumOfIncomes));
    }
}
