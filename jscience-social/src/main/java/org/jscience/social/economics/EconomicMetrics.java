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
import java.util.Collections;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
 
/**
 * Calculates economic metrics and population-wide indicators such as the Gini coefficient.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class EconomicMetrics {
 
    private EconomicMetrics() {}
 
 
    /**
     * Calculates the Gini coefficient for reaching a value between 0.0 (perfect equality) 
     * and 1.0 (perfect inequality) using Real numbers directly.
     * 
     * @param incomes individual income data points as Real
     * @return the Gini index as a Real number
     */
    public static Real giniCoefficientReal(List<Real> incomes) {
        if (incomes == null || incomes.size() < 2) return Real.ZERO;
        
        List<Real> sorted = new ArrayList<>(incomes);
        Collections.sort(sorted);
        
        int n = sorted.size();
        Real sumOfIncomes = Real.ZERO;
        Real weightedSum = Real.ZERO;
        
        for (int i = 0; i < n; i++) {
            Real val = sorted.get(i);
            sumOfIncomes = sumOfIncomes.add(val);
            Real weight = Real.of(2 * (i + 1) - n - 1);
            weightedSum = weightedSum.add(weight.multiply(val));
        }
        
        if (sumOfIncomes.isZero()) return Real.ZERO;
        return weightedSum.divide(Real.of(n).multiply(sumOfIncomes));
    }
    
    /**
     * Overload for Quantity for convenience.
     */
    public static Real giniCoefficient(List<? extends Quantity<?>> incomes) {
        if (incomes == null || incomes.isEmpty()) return Real.ZERO;
        List<Real> rList = new ArrayList<>();
        for (Quantity<?> q : incomes) rList.add(q.getValue());
        return giniCoefficientReal(rList);
    }
}

