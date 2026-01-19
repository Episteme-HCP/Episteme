package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Measures economic inequality.
 */
public final class InequalityMetrics {

    private InequalityMetrics() {}

    /**
     * Gini Coefficient: G = Sum(Sum(|xi - xj|)) / (2 * n^2 * mean)
     */
    public static Real calculateGini(double[] incomes) {
        int n = incomes.length;
        double sumDiff = 0;
        double sumIncomes = 0;
        
        for (int i = 0; i < n; i++) {
            sumIncomes += incomes[i];
            for (int j = 0; j < n; j++) {
                sumDiff += Math.abs(incomes[i] - incomes[j]);
            }
        }
        
        double mean = sumIncomes / n;
        double gini = sumDiff / (2.0 * n * n * mean);
        return Real.of(gini);
    }

    /**
     * Lorenz Curve coordinates.
     */
    public static List<double[]> getLorenzCurve(double[] incomes) {
        double[] sorted = incomes.clone();
        Arrays.sort(sorted);
        double total = Arrays.stream(sorted).sum();
        
        List<double[]> curve = new ArrayList<>();
        curve.add(new double[]{0, 0});
        
        double cumulativeIncome = 0;
        for (int i = 0; i < sorted.length; i++) {
            cumulativeIncome += sorted[i];
            curve.add(new double[]{(double)(i+1)/sorted.length, cumulativeIncome/total});
        }
        
        return curve;
    }
}
