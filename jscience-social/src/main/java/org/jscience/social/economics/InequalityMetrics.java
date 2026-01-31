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

package org.jscience.social.economics;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
 
/**
 * Advanced metrics for analyzing economic inequality, including Gini coefficients 
 * and Lorenz curve generation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class InequalityMetrics {
 
    private InequalityMetrics() {}
 
    /**
     * Calculates the Gini coefficient for a population income set (Quantities).
     * Higher values indicate greater inequality.
     */
    public static Real calculateGini(Quantity<?>[] incomes) {
        if (incomes == null || incomes.length < 2) return Real.ZERO;
        int n = incomes.length;
        Real sumDiff = Real.ZERO;
        Real sumIncomes = Real.ZERO;
        
        for (int i = 0; i < n; i++) {
            Real valI = incomes[i].getValue();
            sumIncomes = sumIncomes.add(valI);
            for (int j = 0; j < n; j++) {
                sumDiff = sumDiff.add(valI.subtract(incomes[j].getValue()).abs());
            }
        }
        
        if (sumIncomes.isZero()) return Real.ZERO;
        
        // G = sumDiff / (2 * n * sumIncomes)
        Real denominator = Real.of(2L * n).multiply(sumIncomes);
        return sumDiff.divide(denominator);
    }
 
    /**
     * Overload for Real arrays.
     */
    public static Real calculateGini(Real[] incomes) {
        if (incomes == null) return Real.ZERO;
        Quantity<?>[] qArray = new Quantity<?>[incomes.length];
        for (int i = 0; i < incomes.length; i++) qArray[i] = Quantities.create(incomes[i], Units.ONE);
        return calculateGini(qArray);
    }
 
    /**
     * Generates a list of coordinates (x, y) representing the Lorenz curve.
     */
    public static List<Real[]> getLorenzCurve(Quantity<?>[] incomes) {
        if (incomes == null || incomes.length == 0) return List.of(new Real[]{Real.ZERO, Real.ZERO}, new Real[]{Real.ONE, Real.ONE});
        Quantity<?>[] sorted = incomes.clone();
        Arrays.sort(sorted, Comparator.comparing(Quantity::getValue));
        Real total = Real.ZERO;
        for (Quantity<?> q : sorted) total = total.add(q.getValue());
        
        List<Real[]> curve = new ArrayList<>();
        curve.add(new Real[]{Real.ZERO, Real.ZERO});
        
        if (total.isZero()) return curve;
 
        Real cumulativeIncome = Real.ZERO;
        for (int i = 0; i < sorted.length; i++) {
            cumulativeIncome = cumulativeIncome.add(sorted[i].getValue());
            curve.add(new Real[]{
                Real.of(i + 1).divide(Real.of(sorted.length)),
                cumulativeIncome.divide(total)
            });
        }
        return curve;
    }
 
    /**
     * Overload for Real arrays.
     */
    public static List<Real[]> getLorenzCurve(Real[] incomes) {
        if (incomes == null) return List.of(new Real[]{Real.ZERO, Real.ZERO}, new Real[]{Real.ONE, Real.ONE});
        Quantity<?>[] qArray = new Quantity<?>[incomes.length];
        for (int i = 0; i < incomes.length; i++) qArray[i] = Quantities.create(incomes[i], Units.ONE);
        return getLorenzCurve(qArray);
    }
}

