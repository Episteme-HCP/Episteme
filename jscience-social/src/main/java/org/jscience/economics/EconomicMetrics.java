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
import java.util.Collections;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Calculates economic metrics and population-wide indicators such as the Gini coefficient.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class EconomicMetrics {

    private EconomicMetrics() {}

    /**
     * Calculates the Gini coefficient for reaching a value between 0.0 (perfect equality) 
     * and 1.0 (perfect inequality).
     * 
     * @param incomes individual income data points
     * @return the Gini index as a Real number
     */
    public static Real giniCoefficient(List<Real> incomes) {
        if (incomes == null || incomes.size() < 2) return Real.ZERO;
        
        List<Real> sorted = new ArrayList<>(incomes);
        Collections.sort(sorted, Real::compareTo);
        
        int n = sorted.size();
        Real sumOfIncomes = Real.ZERO;
        Real weightedSum = Real.ZERO;
        
        for (int i = 0; i < n; i++) {
            sumOfIncomes = sumOfIncomes.add(sorted.get(i));
            Real weight = Real.of(2 * (i + 1) - n - 1);
            weightedSum = weightedSum.add(weight.multiply(sorted.get(i)));
        }
        
        if (sumOfIncomes.isZero()) return Real.ZERO;
        return weightedSum.divide(Real.of(n).multiply(sumOfIncomes));
    }
}
