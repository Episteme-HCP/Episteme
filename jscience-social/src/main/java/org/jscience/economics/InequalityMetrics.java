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

package org.jscience.economics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Advanced metrics for analyzing economic inequality, including Gini coefficients 
 * and Lorenz curve generation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class InequalityMetrics {

    private InequalityMetrics() {}

    /**
     * Calculates the Gini coefficient for a population income set.
     * Higher values indicate greater inequality.
     */
    public static Real calculateGini(double[] incomes) {
        if (incomes == null || incomes.length < 2) return Real.ZERO;
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
        if (mean == 0) return Real.ZERO;
        return Real.of(sumDiff / (2.0 * n * n * mean));
    }

    /**
     * Generates a list of coordinates (x, y) representing the Lorenz curve.
     */
    public static List<double[]> getLorenzCurve(double[] incomes) {
        if (incomes == null || incomes.length == 0) return List.of(new double[]{0,0}, new double[]{1,1});
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
